package org.example;

import org.apache.commons.cli.*;

import java.io.PrintWriter;

public class Main {

    private static final Option o = Option.builder("o").hasArg(true).optionalArg(true)
            .desc("Path for results files <not required>").build();
    private static final Option p = Option.builder("p").hasArg(true).optionalArg(true)
            .desc("Prefix for file naming <not required>").build();
    private static final Option a = Option.builder("a").hasArg(false).optionalArg(true)
            .desc("Mode of append in results files <not required>").build();
    private static final Option s = Option.builder("s").hasArg(false).optionalArg(true)
            .desc("Displays shor statistics in the console <[-s|-f] not required>  (by default)").build();
    private static final Option f = Option.builder("f").hasArg(false).optionalArg(true)
            .desc("Displays full statistics in the console <[-s|-f] not required>").build();
    private static final Option h = Option.builder("h").hasArg(false).optionalArg(true)
            .desc("HELP").build();

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter printWriter = new PrintWriter(System.out);
        printWriter.println("<-HELP->");
        formatter.printUsage(printWriter, 100, "java -jar *.jar [options] in1.txt in2.txt...");
        printWriter.println("options: ");
        formatter.printOptions(printWriter, 100, options, 2, 4);
        printWriter.close();
    }

    public static void main(String[] args) {
        CommandLineParser commandLineParser = new DefaultParser();
        Options options = new Options();
        options.addOption(o)
                .addOption(p)
                .addOption(a)
                .addOptionGroup(new OptionGroup().addOption(s).addOption(f))
                .addOption(h);
        try {
            Filter filter = new Filter();
            CommandLine commandLine = commandLineParser.parse(options, args);
            if (commandLine.hasOption(h.getOpt())) {
                printHelp(options);
                System.exit(-1);
            }
            if (commandLine.hasOption(o.getOpt())) filter.setPathOfResults(commandLine.getOptionValue(o.getOpt()));
            if (commandLine.hasOption(p.getOpt())) filter.setPrefix(commandLine.getOptionValue(p.getOpt()));
            if (commandLine.hasOption(a.getOpt())) filter.setAppend(true);
            if (commandLine.hasOption(s.getOpt())) filter.setFullStats(false);
            if (commandLine.hasOption(f.getOpt())) filter.setFullStats(true);
            if (commandLine.getArgs().length > 0) {
                filter.setInFiles(commandLine.getArgs());
                filter.start();
            } else {
                System.out.println("Input files must be more than 1");
                System.exit(-1);
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            printHelp(options);
        }

    }
}