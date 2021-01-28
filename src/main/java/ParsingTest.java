import org.apache.commons.cli.*;
import tools.DefaultParameter;


/**
 *  ParsingTest est la classe qui sert à tester le parsing des entrées de l'application.
 *
 * @author tndoye
 * @version 1.0.0
 */
public class ParsingTest {


    public static void main(String[] args) {

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
                System.out.print("Option d is present.  The value is: ");
                System.out.println(commandLine.getOptionValue("d"));
            }

            if (commandLine.hasOption("g")) {
                System.out.print("Option g is present.  The value is: ");
                String[] values = commandLine.getOptionValues('g');
                for (String argument : values) {
                    System.out.print(argument);
                    System.out.print(" ");
                }
                System.out.println();
            }

            if (commandLine.hasOption("i")) {
                System.out.print("Option i is present.  The value is: ");
                System.out.println(commandLine.getOptionValue("i"));
            }

            if (commandLine.hasOption("o")) {
                System.out.print("Option o is present.  The value is: ");
                System.out.println(commandLine.getOptionValue("o"));
            }

            if (commandLine.hasOption("help") || commandLine.hasOption("h")) {
                System.out.println("Option test is present.  This is a flag option.");
                formatter.printHelp("LGGGraph", DefaultParameter.header, options, DefaultParameter.footer, true);
            }

            String[] remainder = commandLine.getArgs();
            System.out.print("Remaining arguments: ");
            for (String argument : remainder) {
                System.out.print(argument);
                System.out.print(" ");
            }

            System.out.println();

        }catch (ParseException exception) {
            System.err.print("Parse error: ");
            System.err.println(exception.getMessage());
            formatter.printHelp("LGGGraph", DefaultParameter.header, options, DefaultParameter.footer, true);
        }

    }
}
