import org.apache.commons.cli.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import rdf.RDFModelFactory;
import rdfcomputation.LggGraphs;
import tools.DefaultParameter;
import tools.LggMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class MainLGG {
    private final Model rdfGraph1 ;
    private final Model rdfGraph2 ;

    private MainLGG(){
        rdfGraph1 = ModelFactory.createDefaultModel() ;
        rdfGraph2 = ModelFactory.createDefaultModel() ;
        LggMode mode = LggMode.DEFAULT;
    }

    private MainLGG(String pathGraph1, String pathGraph2, LggMode mode){
        rdfGraph1 = ModelFactory.createDefaultModel() ;
        rdfGraph2 = ModelFactory.createDefaultModel() ;
        switch (mode){
            case LGG_GRAPH_MODE:
                //lire
                break;
            case LGG_QUERY_MODE:
                //lire
                break;
            case DEFAULT:
            default:
                System.out.println("Choisir l'option de calcul de LGG");
                break;
        }
    }

    //EXECUTION:
    //java -jar MainLGG [OPTION] pathToFile1 pathToFile2 -i pathToInfoFile -o pathToOutputDirectory -d pathToDictionary
    private static Boolean parsingInput(String[] args, LggMode mode) {

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

        mode = LggMode.LGG_GRAPH_MODE;
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
                    System.err.println("ARGUMENT ERROR : " + reason);
                    formatter.printHelp("CLIsample", DefaultParameter.header, options, DefaultParameter.footer, true);
                    mode = LggMode.INPUT_ERROR;
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

        if (!parsingInput(args, mode)) {
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
                final long start = System.nanoTime();
                resultat = lggGraphs.ProductGraph(DefaultParameter.dictionaryPathUsed);
                timeProd += System.nanoTime() - start;
            }
            timeProd /= 5L;
            //resultat.write((OutputStream)System.out, "N-TRIPLE");
            resultat.write(System.out, "NTRIPLES");
            File csvFile = new File(DefaultParameter.dictionaryPathUsed);
            if (!csvFile.exists()) {
                System.err.println("Le fichier " + DefaultParameter.infoPathUsed + " n'existe pas");
                return;
            }
            PrintStream l_out = null;
            try {
                l_out = new PrintStream(new FileOutputStream(DefaultParameter.infoPathUsed, true));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            l_out.print(DefaultParameter.graphPath1.split("/")[DefaultParameter.graphPath1.split("/").length - 1].substring(0, DefaultParameter.graphPath1.split("/")[DefaultParameter.graphPath1.split("/").length - 1].length() - 4) + ";");
            l_out.print(DefaultParameter.graphPath1.split("/")[DefaultParameter.graphPath1.split("/").length - 1].substring(0, DefaultParameter.graphPath1.split("/")[DefaultParameter.graphPath1.split("/").length - 1].length() - 4) + ";");
            l_out.print(resultat.size() + ";");
            l_out.print(timeProd + ";");
            l_out.println();
            l_out.flush();
            l_out.close();
            l_out = null;
            System.out.println("Writing ...");
            lggGraphs.writelgg();
            System.out.println("End");
        }
        else {
            System.out.println();
        }


    }

}
