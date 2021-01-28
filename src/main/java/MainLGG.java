import org.apache.commons.cli.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import rdf.RDFModelFactory;
import rdfcomputation.LggGraphs;
import rdfcomputation.LggQueries;
import rdfcomputation.RDFComputation;
import rdfio.CSVFileIO;
import tools.DefaultParameter;
import tools.LggMode;



/**
 *
 * MainLGG est la classe principale contenant la fonction main.
 *
 * @version 1.0.0
 */
public class MainLGG {
    //EXECUTION:
    //java -jar MainLGG [OPTION] pathToFile1 pathToFile2 -i pathToInfoFile -o pathToOutputDirectory -d pathToDictionary

    /**
     * Retourne la validité des paramtètres en entrées.
     * Cette méthode effectue le parsing des arguments entrés lors de l'exécution.
     * @param args Arguments en entrée de l'applicattion.
     * @return True si les arguments sont conformes, False sinon.
     */
    private static int parsingInput(String[] args) {

        CommandLine commandLine;
        Option option_d = Option.builder("d").argName("dico").hasArg()
                .desc(DefaultParameter.dictionaryArgumentDesc).optionalArg(true).build();
        Option option_f = Option.builder("f").argName("input").hasArgs().valueSeparator(' ')
                .desc(DefaultParameter.graphArgumentDesc).required().optionalArg(true).build();
        Option option_g = Option.builder("g").argName("modeGraph").hasArg().valueSeparator(' ')
                .desc(DefaultParameter.graphArgumentDesc).required().optionalArg(true).build();
        Option option_h = Option.builder("h").argName("help").longOpt("help").desc(DefaultParameter.helpDesc).build();
        Option option_i = Option.builder("i").argName("info").hasArg()
                .desc(DefaultParameter.infoArgumentDesc).optionalArg(true).build();
        Option option_o = Option.builder("o").argName("output").hasArg()
                .desc(DefaultParameter.outputArgumentDesc).optionalArg(true).build();
        Option option_q = Option.builder("g").argName("modeQuery").hasArg().valueSeparator(' ')
                .desc(DefaultParameter.graphArgumentDesc).required().optionalArg(true).build();

        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        options.addOption(option_d);
        options.addOption(option_f);
        options.addOption(option_g);
        options.addOption(option_h);
        options.addOption(option_i);
        options.addOption(option_o);
        options.addOption(option_q);

        HelpFormatter formatter = new HelpFormatter();

        int executionMode;

        try {
            commandLine = parser.parse(options, args);

            if (commandLine.hasOption("d")) {
                DefaultParameter.dictionaryPathUsed = commandLine.getOptionValue("d");
            }

            if (commandLine.hasOption("i")) {
                DefaultParameter.infoPathUsed = commandLine.getOptionValue("i");
            }

            if (commandLine.hasOption("o")) {
                DefaultParameter.outputDirectoryUsed = commandLine.getOptionValue("o");
            }

            if (commandLine.hasOption("help")) {
                System.out.println("Option test is present.  This is a flag option.");
                formatter.printHelp("CLIsample", DefaultParameter.header, options, DefaultParameter.footer, true);
            }

            if (commandLine.hasOption("f")) {
                String[] remainder = commandLine.getOptionValues("g");
                if (remainder.length == 2) {
                    DefaultParameter.graphPath1 = remainder[0];
                    DefaultParameter.graphPath2 = remainder[1];
                    DefaultParameter.graphResult = DefaultParameter.outputDirectoryUsed + "/Lgg" +
                            DefaultParameter.graphPath1.split("/")[DefaultParameter.graphPath1.split("/").length - 1].split("\\.")[0] +
                            DefaultParameter.graphPath2.split("/")[DefaultParameter.graphPath2.split("/").length - 1].split("\\.")[0] +
                            ".n3";
                } else {
                    String reason = remainder.length < 2 ? "too few arguments" : "too much arguments";
                    System.err.println("ARGUMENT ERROR : " + reason +"("+remainder.length+")");
                    formatter.printHelp("CLIsample", DefaultParameter.header, options, DefaultParameter.footer, true);
                    return -1;
                }
            }

            if (commandLine.hasOption('g')){
                executionMode = 1 ;
            } else if (commandLine.hasOption('q')) {
                executionMode = 2 ;
            } else {
                executionMode = 3 ;
            }
        }catch (ParseException exception) {
            System.out.print("Parse error...");
            System.out.println(exception.getMessage());
            return 0 ;
        }
        return executionMode;
    }

    public static void main(String[] args) {
        LggMode mode = LggMode.DEFAULT;

        switch (parsingInput(args)) {
            case -1 :
                System.err.println("un ou plusieurs arguments obligatoires manquants.");
                mode = LggMode.INPUT_ERROR ;
                break ;
            case 0 :
                //ATTENTION BUG POTENTIEL: voir notes - presences d'espaces dans un argument
                for(String str : args){
                    System.out.print(str+"_");
                }
                System.out.println();
                System.out.println(mode);
                return;
            case 1 :
                mode = LggMode.LGG_GRAPH_MODE ;
                break ;
            case 2 :
                mode = LggMode.LGG_QUERY_MODE ;
                break ;
            case 3 :
                mode = LggMode.INPUT_ERROR ;
                break;
            default:
                System.err.println("Erreur de parsing : valeur de retour non reconnue");
                return;
        }

        RDFModelFactory modelFactory = new RDFModelFactory();

        RDFComputation lgg = null ;
        LggGraphs lggGraphs ;
        LggQueries lggQueries ;

        if (mode == LggMode.LGG_GRAPH_MODE) {
            lggGraphs = (LggGraphs) modelFactory.loadlgg(DefaultParameter.graphPath1,
                    DefaultParameter.graphPath2);
            lgg = lggGraphs ;
        }
        if (mode == LggMode.LGG_QUERY_MODE) {
            lggQueries = (LggQueries) modelFactory.loadlgg(DefaultParameter.graphPath1,
                    DefaultParameter.graphPath2);
            lgg = lggQueries ;
        }

        if(mode != LggMode.INPUT_ERROR && lgg != null) {
            if (lgg.getVars1().size() == lgg.getVars2().size()) {
                Model resultat = ModelFactory.createDefaultModel();
                long timeProd = 0L;
                for (int i = 0; i < 5; ++i) {
                    long start = System.nanoTime();
                    resultat = lgg.productGraph();
                    timeProd += System.nanoTime() - start;
                }
                timeProd /= 5L;
                //resultat.write((OutputStream)System.out, "N-TRIPLE");
                //System.out.println(Lang.NTRIPLES.getLabel());
                resultat.write(System.out, Lang.NTRIPLES.getLabel());
                CSVFileIO csvFileIO = new CSVFileIO(DefaultParameter.dictionaryPathUsed);
                if (!csvFileIO.checkFile()) {
                    System.err.println("Le fichier " + DefaultParameter.infoPathUsed + " n'existe pas");
                    return;
                }
                csvFileIO.writeInfo(resultat.size(), timeProd);
                System.out.println("Writing ...");
                lgg.writelgg();
                System.out.println("End");
                if (lgg.lggQueryexists()) {
                    lgg.writelgg();
                }
            } else {
                System.out.println();
            }
        }


    }

}
