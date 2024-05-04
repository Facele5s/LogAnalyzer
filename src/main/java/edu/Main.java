package edu;

import edu.Formatters.AdocReportCreator;
import edu.Formatters.MarkdownReportCreator;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {
    private Main() {

    }

    public static void main(String[] args) {
        CMDParser cmdParser = new CMDParser(args);

        String path = cmdParser.getPath();
        String from = cmdParser.getFrom();
        String to = cmdParser.getTo();
        String format = cmdParser.getFormat();

        try {
            List<LogEntity> logs = LogParser.parseLogs(path);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;
            LocalDate dateFrom = from != null ? LocalDate.parse(from, dateTimeFormatter) : null;
            LocalDate dateTo = to != null ? LocalDate.parse(to, dateTimeFormatter) : null;

            if (format.equals("Markdown")) {
                MarkdownReportCreator reportCreator = new MarkdownReportCreator(path, logs, dateFrom, dateTo);
                reportCreator.setFromDate(dateFrom);
                reportCreator.setToDate(dateTo);
                reportCreator.printReport();
                reportCreator.saveReport("src/main/resources/someLogs.md");
            } else {
                AdocReportCreator reportCreator = new AdocReportCreator(path, logs, dateFrom, dateTo);
                reportCreator.setFromDate(dateFrom);
                reportCreator.setToDate(dateTo);
                reportCreator.printReport();
                reportCreator.saveReport("src/main/resources/someLogs.adoc");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
