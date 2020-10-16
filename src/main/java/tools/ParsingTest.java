package tools;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
public class ParsingTest {


    public static void main(String[] args) {
        CommandLine commandLine;
        Option option_d = Option.builder("d").argName("dico").hasArg().desc(DefaultParameter.dictionaryArgumentDesc).build();
        Option option_g = Option.builder("g").argName("rdfGraph").hasArg().desc(DefaultParameter.dictionaryArgumentDesc).build();
        Option option_i = Option.builder("i").argName("info").hasArg().desc(DefaultParameter.infoArgumentDesc).build();
        Option option_o = Option.builder("o").argName("output").hasArg().desc(DefaultParameter.outputArgumentDesc).build();
        Option option_h = Option.builder().longOpt("help").desc("The help option.").build();
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        options.addOption(option_d);
        options.addOption(option_g);
        options.addOption(option_i);
        options.addOption(option_o);
        options.addOption(option_h);

        HelpFormatter formatter = new HelpFormatter();

        try {
            commandLine = parser.parse(options, args);

            if (commandLine.hasOption("d")) {
                System.out.print("Option d is present.  The value is: ");
                System.out.println(commandLine.getOptionValue("d"));
            }

            if (commandLine.hasOption("i")) {
                System.out.print("Option i is present.  The value is: ");
                System.out.println(commandLine.getOptionValue("i"));
            }

            if (commandLine.hasOption("o")) {
                System.out.print("Option o is present.  The value is: ");
                System.out.println(commandLine.getOptionValue("o"));
            }

            if (commandLine.hasOption("help")) {
                System.out.println("Option test is present.  This is a flag option.");
                formatter.printHelp("CLIsample", DefaultParameter.header, options, DefaultParameter.footer, true);
            }

            String[] remainder = commandLine.getArgs();
            System.out.print("Remaining arguments: ");
            for (String argument : remainder) {
                System.out.print(argument);
                System.out.print(" ");
            }

            System.out.println();

        }catch (ParseException exception) {
            System.out.print("Parse error: ");
            System.out.println(exception.getMessage());
        }

    }
}
