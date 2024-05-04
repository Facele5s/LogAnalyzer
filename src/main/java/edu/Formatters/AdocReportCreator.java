package edu.Formatters;

import edu.LogEntity;
import edu.TablePrinter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("MultipleStringLiterals")
public class AdocReportCreator extends AbstractReport implements ReportCreator {
    private static final Logger LOGGER = LogManager.getLogger();

    public AdocReportCreator(String logFile, List<LogEntity> logs, LocalDate fromDate, LocalDate toDate) {
        super(logFile, logs, fromDate, toDate);
    }

    @Override
    public String createReport() {
        report.append("=== Общая информация\n");
        report.append(createTable(headerMain, tableMain));

        report.append("=== Запрашиваемые ресурсы\n");
        report.append(createTable(headerResources, tableResources));

        report.append("=== Коды ответа\n");
        report.append(createTable(headerResponses, tableResponses));

        return report.toString();
    }

    @Override
    public String createTable(String[] header, String[][] table) {
        StringBuilder builder = new StringBuilder("[cols=\"" + "1,".repeat(header.length - 1) + "1\"]\n");
        builder.append("|===\n");

        for (String h: header) {
            builder.append("| ").append(h).append(" ");
        }
        builder.append("|\n");

        for (String[] row: table) {
            for (String str: row) {
                builder.append("| ").append(str).append(" ");
            }
            builder.append("|\n");
        }

        builder.append("|===\n");

        return builder.toString();
    }

    @Override
    public void printReport() {
        LOGGER.info("=== Общая информация");
        TablePrinter.printTable(headerMain, tableMain);

        LOGGER.info("=== Запрашиваемые ресурсы");
        TablePrinter.printTable(headerResources, tableResources);

        LOGGER.info("=== Коды ответа");
        TablePrinter.printTable(headerResponses, tableResponses);
    }

    @Override
    public void saveReport(String path) throws IOException {
        if (!path.endsWith(".adoc")) {
            throw new IOException();
        }

        Path filePath = Path.of(path);
        Files.writeString(filePath, createReport(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        LOGGER.info("Отчёт сохранён: " + path);
    }
}
