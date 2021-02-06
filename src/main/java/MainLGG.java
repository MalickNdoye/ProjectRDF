import org.apache.commons.cli.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.tdb.base.file.FileException;
import rdf.DictionaryNode;
import rdf.RDFModelFactory;
import rdfcomputation.LggGraphs;
import rdfcomputation.LggQueries;
import rdfio.CSVFileIO;
import tools.DefaultParameter;
import tools.LggMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;


/**
 *
 * MainLGG est la classe principale contenant la fonction main.
 *
 * @version 1.0.0
 */
public class MainLGG {
    //EXECUTION:
    //java -jar MainLGG [OPTION] pathToFile1 pathToFile2 -i pathToInfoFile -o pathToOutputDirectory -d pathToDictionary
    public static void main(String[] args) {
        LggMode mode = LggMode.DEFAULT;
        //PARSING DES ENTRÉES
        switch (parsingInput(args)) {
            case -1 :
                mode = LggMode.INPUT_ERROR ;
                break ;
            case 0 :
                //ATTENTION BUG POTENTIEL: voir notes - presences d'espaces dans un argument
                System.out.print("Program arguments : ");
                for(String str : args){
                    System.out.print(str+" ");
                }
                System.out.println();
                return;
            case 1 :
                mode = LggMode.LGG_GRAPH_MODE ;
                break ;
            case 2 :
                mode = LggMode.LGG_QUERY_MODE ;
                break ;
            default:
                System.err.println("Erreur de parsing : valeur de retour non reconnue");
                return;
        }

        //Exécution des algorithmes
        RDFModelFactory modelFactory = new RDFModelFactory();
        switch (mode) {
            case LGG_GRAPH_MODE:
                LggGraphs lggGraphs = modelFactory.loadGraphs(DefaultParameter.graphPath1,
                        DefaultParameter.graphPath2);
                graphModeExecution(lggGraphs);
                break ;
            case LGG_QUERY_MODE:
                LggQueries lggQueries = modelFactory.loadQueries(DefaultParameter.graphPath1,
                        DefaultParameter.graphPath2);
                queryModeExecution(lggQueries);
                break ;
            default:
                break;
        }


    }

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
        Option option_f = Option.builder("f").argName("input").hasArgs().valueSeparator(' ').numberOfArgs(2)
                .desc(DefaultParameter.fileArgumentDesc).optionalArg(true).build();
        Option option_g = Option.builder("g").argName("modeGraph").hasArg(false)
                .desc(DefaultParameter.graphModeArgumentDesc).optionalArg(true).build();
        Option option_h = Option.builder("h").argName("help").longOpt("help")
                .desc(DefaultParameter.helpDesc).build();
        Option option_i = Option.builder("i").argName("info").hasArg()
                .desc(DefaultParameter.infoArgumentDesc).optionalArg(true).build();
        Option option_o = Option.builder("o").argName("output").hasArg()
                .desc(DefaultParameter.outputArgumentDesc).optionalArg(true).build();
        Option option_q = Option.builder("q").argName("modeQuery").hasArg(false)
                .desc(DefaultParameter.queryModeArgumentDesc).optionalArg(true).build();


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
            if (args.length==0){
                throw new ParseException("This application needs mandatory arguments to run.");
            }
            commandLine = parser.parse(options, args);
            if (commandLine.hasOption("d")) {
                DefaultParameter.dictionaryPathUsed = commandLine.getOptionValue("d");
                checkFile(DefaultParameter.dictionaryPathUsed,"The RDF file must be a csv file.");
                DictionaryNode.getInstance().setDictionaryPath(DefaultParameter.dictionaryPathUsed);
            }else {
                DictionaryNode.getInstance().setDictionaryPath(DefaultParameter.dictionaryPath) ;
            }

            if (commandLine.hasOption("i")) {
                DefaultParameter.infoPathUsed = commandLine.getOptionValue("i");
                checkFile(DefaultParameter.infoPathUsed,"The RDF file must be a csv file.");
            }

            if (commandLine.hasOption("o")) {
                DefaultParameter.outputDirectoryUsed = commandLine.getOptionValue("o");
                checkDirectory(DefaultParameter.outputDirectoryUsed);
            }

            if (commandLine.hasOption("help")) {
                formatter.printHelp("CLIsample", DefaultParameter.header, options, DefaultParameter.footer, true);
                return 0 ;
            }

            if (commandLine.hasOption("f")) {
                String[] remainder = commandLine.getOptionValues("f");
                if (remainder.length == 2) {
                    DefaultParameter.graphPath1 = remainder[0];
                    DefaultParameter.graphPath2 = remainder[1];
                    checkFile(DefaultParameter.graphPath1,"The RDF file must be a '.n3' or '.ttl' file.");
                    checkFile(DefaultParameter.graphPath2,"The RDF file must be a '.n3' or '.ttl' file.");
                    DefaultParameter.graphResult = DefaultParameter.outputDirectoryUsed + "/Lgg" +
                            DefaultParameter.graphPath1.split("/")[DefaultParameter.graphPath1.split("/").length - 1].split("\\.")[0] +
                            DefaultParameter.graphPath2.split("/")[DefaultParameter.graphPath2.split("/").length - 1].split("\\.")[0] ;
                    if(commandLine.hasOption('g')) {
                        DefaultParameter.graphResult += ".n3";
                        executionMode = 1 ;
                    }else if(commandLine.hasOption('q')){
                        DefaultParameter.graphResult += ".sparql";
                        executionMode = 2 ;
                    } else {
                        throw new ParseException("no execution mode specified.") ;
                    }
                } else {
                    String reason = remainder.length < 2 ? "too few arguments." : "too much arguments.";
                    throw new ParseException(reason);
                }
            }else{
                throw  new ParseException("no RDF files specified. The option -f is mandatory.");
            }

        }catch (FileNotFoundException fileException){
            try {
                fileException.printStackTrace();
                throw new ParseException(fileException.getMessage());
            }catch (ParseException exception){
                System.err.println("PARSING ERROR : "+exception.getMessage());
                exception.printStackTrace();
                System.out.println("Please use the option [-h] or [-help] to show the manpage" +
                        "or refer to its documentations.");
                return -1 ;
            }
        }catch (ParseException exception) {
            System.err.println("PARSING ERROR : "+exception.getMessage());
            exception.printStackTrace();
            System.out.println("Please use the option [-h] or [-help] to show the manpage" +
                    "or refer to its documentations.");
            return -1 ;
        }
        return executionMode;
    }



    private static void queryModeExecution(LggQueries lggQueries) {
        if (lggQueries.getVars1().size() == lggQueries.getVars2().size()) {

            Model resultat = ModelFactory.createDefaultModel();
            long timeProd = 0;
            int i=0;
            while (i<5) {
                long start = System.nanoTime();
                resultat = lggQueries.product();
                timeProd += System.nanoTime() - start;
                i++;
            }

            timeProd = timeProd/5;
            writeInfo(resultat,timeProd);
            resultat.write(System.out, "N-Triples");


            if (lggQueries.lggexists()) {
                System.out.println("An lgg exists");
                lggQueries.writelgg();
            }
            else {
                System.out.println("Lgg for these queries does not exist.");
            }


        }
    }


    private static void graphModeExecution(LggGraphs lgg) {
        if (lgg.getVars1().size() == lgg.getVars2().size()) {
            Model resultat = ModelFactory.createDefaultModel();
            long timeProd = 0L;
            for (int i = 0; i < 5; ++i) {
                long start = System.nanoTime();
                resultat = lgg.productGraph();
                timeProd += System.nanoTime() - start;
            }
            timeProd /= 5L;
            resultat.write(System.out, Lang.NTRIPLES.getLabel());
            writeInfo(resultat,timeProd);
            System.out.println("Writing ...");
            lgg.writelgg();
            System.out.println("End");
        } else {
            System.out.println();
        }
    }

    private static void writeInfo(Model resultat,long timeProd) {
        CSVFileIO csvFileIO = new CSVFileIO(DefaultParameter.infoPathUsed);
        if (!csvFileIO.checkFile()) {
            System.err.println("Le fichier " + DefaultParameter.infoPathUsed + " n'existe pas");
            return;
        }
        csvFileIO.writeInfo(resultat.size(), timeProd);
        timeProd = TimeUnit.MILLISECONDS.convert(timeProd, TimeUnit.NANOSECONDS);
        System.out.println("Graphe RDF de "+resultat.size()+" triplet(s) calculé en "+timeProd+"ms");
    }

    private static void checkFile(String path,String errorMessage) throws FileNotFoundException {
        if (!new File(path).isFile()){
            throw new FileNotFoundException(errorMessage);
        }
    }

    private static void checkDirectory(String path) throws FileNotFoundException {
        if (!new File(path).isDirectory()){
            throw new FileNotFoundException("The output argument must be a directory.");
        }
    }
}
