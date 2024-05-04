package edu;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CMDParser {
    private String path;
    private String from;
    private String to;
    private String format;

    @SuppressWarnings("MultipleStringLiterals")
    public CMDParser(String[] args) {
        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);

            path = cmd.getOptionValue("path");
            from = cmd.getOptionValue("from");
            to = cmd.getOptionValue("to");
            format = cmd.getOptionValue("format", "Markdown");
        } catch (ParseException e) {
            formatter.printHelp("aawdawd", options);
            System.exit(-1);
        }
    }

    private Options getOptions() {
        Options options = new Options();

        Option pathOption = new Option("p", "path", true, "Path to log file");
        pathOption.setRequired(true);
        options.addOption(pathOption);

        Option fromOption = new Option("f", "from", true, "Start date in ISO8601");
        options.addOption(fromOption);

        Option toOption = new Option("t", "to", true, "End date in ISO8601");
        options.addOption(toOption);

        Option formatOption = new Option("frmt", "format", true, "Markdown or adoc format");
        options.addOption(formatOption);
        return options;
    }

    public String getPath() {
        return path;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getFormat() {
        return format;
    }
}
