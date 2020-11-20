import org.apache.commons.cli.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import rdf.RDFModelFactory;
import rdfcomputation.LggGraphs;
import rdfio.CSVFileIO;
import tools.DefaultParameter;
import tools.LggMode;

public class MainLGG {
    //EXECUTION:
    //java -jar MainLGG [OPTION] pathToFile1 pathToFile2 -i pathToInfoFile -o pathToOutputDirectory -d pathToDictionary
    private static Boolean parsingInput(String[] args) {

        CommandLine commandLine;
        Option option_d = Option.builder("d").argName("dico").hasArg()
                .desc(DefaultParameter.dictionaryArgumentDesc).optionalArg(true).build();
        Option option_g = Option.builder("g").argName("rdfGraphs").hasArgs().valueSeparator(' ')
                .desc(DefaultParameter.graphArgumentDesc).required().optionalArg(true).build();
        Option option_i = Option.builder("i").argName("info").hasArg()
                .desc(DefaultParameter.infoArgumentDesc).optionalArg(true).build();
        Option option_o = Option.builder("o").argName("output").hasArg()
                .desc(DefaultParameter.outputArgumentDesc).optionalArg(true).build();
        Option option_h = Option.builder("h").argName("help").longOpt("help").desc(DefaultParameter.helpDesc).build();
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        options.addOption(option_d);
        options.addOption(option_g);
        options.addOption(option_h);
        options.addOption(option_i);
        options.addOption(option_o);

        HelpFormatter formatter = new HelpFormatter();

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

            if (commandLine.hasOption("g")) {
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
                    return false;
                }
            }
        }catch (ParseException exception) {
            System.out.print("Parse error: ");
            System.out.println(exception.getMessage());
            return false ;
        }
        return true;
    }

    public static void main(String[] args) {
        LggMode mode = LggMode.DEFAULT;

        if (!parsingInput(args)) {
            //ATTENTION BUG POTENTIEL: voir notes - presences d'espaces dans un argument
            for(String str : args){
                System.out.print(str+"_");
            }
            System.out.println();
            System.out.println(mode);
            return;
        }

        RDFModelFactory modelFactory = new RDFModelFactory();
        LggGraphs lggGraphs = modelFactory.loadlgg(DefaultParameter.graphPath1,
                DefaultParameter.graphPath2);


        if (lggGraphs.getVars1().size() == lggGraphs.getVars2().size()) {
            Model resultat = ModelFactory.createDefaultModel();
            long timeProd = 0L;
            for (int i = 0; i < 5; ++i) {
                long start = System.nanoTime();
                resultat = lggGraphs.productGraph();
                timeProd += System.nanoTime() - start;
            }
            timeProd /= 5L;
            //resultat.write((OutputStream)System.out, "N-TRIPLE");
            //System.out.println(Lang.NTRIPLES.getLabel());
            resultat.write(System.out,Lang.NTRIPLES.getLabel());
            CSVFileIO csvFileIO =  new CSVFileIO(DefaultParameter.dictionaryPathUsed) ;
            if (!csvFileIO.checkFile()) {
                System.err.println("Le fichier " + DefaultParameter.infoPathUsed + " n'existe pas");
                return;
            }
            csvFileIO.writeInfo(resultat.size(),timeProd);
            System.out.println("Writing ...");
            lggGraphs.writelgg();
            System.out.println("End");
        }
        else {
            System.out.println();
        }


    }

}
