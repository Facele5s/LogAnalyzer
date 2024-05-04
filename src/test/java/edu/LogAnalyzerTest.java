package edu;

import edu.Formatters.AdocReportCreator;
import edu.Formatters.MarkdownReportCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LogAnalyzerTest {
    @Test
    @DisplayName("Проверка парсинга командной строки")
    public void cmdParseTest() {
        String[] args = new String[] {
            "-p", "logFilePathOrUrl", "-f", "2023-10-19", "-t", "2023-11-19", "-frmt", "Adoc"
        };
        CMDParser cmdParser = new CMDParser(args);

        assertEquals(cmdParser.getPath(), "logFilePathOrUrl");
        assertEquals(cmdParser.getFrom(), "2023-10-19");
        assertEquals(cmdParser.getTo(), "2023-11-19");
        assertEquals(cmdParser.getFormat(), "Adoc");

        args = new String[] {
            "-p", "logFilePathOrUrl"
        };
        cmdParser = new CMDParser(args);

        assertEquals(cmdParser.getPath(), "logFilePathOrUrl");
        assertNull(cmdParser.getFrom());
        assertNull(cmdParser.getTo());
        assertEquals(cmdParser.getFormat(), "Markdown");
    }

    @Test
    @DisplayName("Проверка парсинга логов")
    public void logsParseTest() {
        String[] args = {"-p",
            "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs",
        };
        CMDParser commandLineArguments = new CMDParser(args);

        String path = commandLineArguments.getPath();

        try {
            List<LogEntity> logs = LogParser.parseLogs(path);

            logs.forEach(Assertions::assertNotNull);

        } catch (IOException e) {
            fail();
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Анализ логов из локального файла")
    public void localFileLogAnalyze() {
        String[] args = {"-p",
            "src/main/java/edu/project3/nginx_logs.txt",
            "-f", "2015-05-01", "-t", "2015-06-10"};

        CMDParser commandLineArguments = new CMDParser(args);

        String path = commandLineArguments.getPath();
        String from = commandLineArguments.getFrom();
        String to = commandLineArguments.getTo();

        try {
            List<LogEntity> logs = LogParser.parseLogs(path);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;
            LocalDate fromDate = from != null ? LocalDate.parse(from, dateFormatter) : null;
            LocalDate toDate = to != null ? LocalDate.parse(to, dateFormatter) : null;
            AdocReportCreator reportCreator = new AdocReportCreator(path, logs, fromDate, toDate);
            reportCreator.printReport();
        } catch (IOException e) {
            fail();
            throw new RuntimeException(e);
        }

        try {
            List<LogEntity> logs = LogParser.parseLogs(path);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;
            LocalDate fromDate = from != null ? LocalDate.parse(from, dateFormatter) : null;
            LocalDate toDate = to != null ? LocalDate.parse(to, dateFormatter) : null;
            MarkdownReportCreator reportCreator = new MarkdownReportCreator(path, logs, fromDate, toDate);
            reportCreator.printReport();
        } catch (IOException e) {
            fail();
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Анализ логов через URL")
    public void urlLogAnalyze() {
        String[] args = {"-p",
            "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs",
            "-f", "2015-05-01", "-t", "2015-06-10"};

        CMDParser commandLineArguments = new CMDParser(args);

        String path = commandLineArguments.getPath();
        String from = commandLineArguments.getFrom();
        String to = commandLineArguments.getTo();

        try {
            List<LogEntity> logs = LogParser.parseLogs(path);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;
            LocalDate fromDate = from != null ? LocalDate.parse(from, dateFormatter) : null;
            LocalDate toDate = to != null ? LocalDate.parse(to, dateFormatter) : null;
            AdocReportCreator reportCreator = new AdocReportCreator(path, logs, fromDate, toDate);
            reportCreator.printReport();
        } catch (IOException e) {
            fail();
            throw new RuntimeException(e);
        }

        try {
            List<LogEntity> logs = LogParser.parseLogs(path);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;
            LocalDate fromDate = from != null ? LocalDate.parse(from, dateFormatter) : null;
            LocalDate toDate = to != null ? LocalDate.parse(to, dateFormatter) : null;
            MarkdownReportCreator reportCreator = new MarkdownReportCreator(path, logs, fromDate, toDate);
            reportCreator.printReport();
        } catch (IOException e) {
            fail();
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Сохранение собранной статистики в md файл")
    public void mdSaveTest() {
        String[] args = {"-p",
            "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs",
            "-f", "2015-05-01", "-t", "2015-06-10"};
        String mdPath = "src/test/resources/log_stats.md";

        File file = new File(mdPath);
        if (file.exists()) {
            file.delete();
        }

        CMDParser commandLineArguments = new CMDParser(args);

        String path = commandLineArguments.getPath();
        String from = commandLineArguments.getFrom();
        String to = commandLineArguments.getTo();

        try {
            List<LogEntity> logs = LogParser.parseLogs(path);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;
            LocalDate fromDate = from != null ? LocalDate.parse(from, dateFormatter) : null;
            LocalDate toDate = to != null ? LocalDate.parse(to, dateFormatter) : null;
            MarkdownReportCreator reportCreator = new MarkdownReportCreator(path, logs, fromDate, toDate);
            reportCreator.saveReport(mdPath);

            assertTrue(file.exists());
            assertTrue(Files.size(file.toPath()) > 0);
        } catch (IOException e) {
            fail();
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Сохранение собранной статистики в adoc файл")
    public void adocSaveTest() {
        String[] args = {"-p",
            "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs",
            "-f", "2015-05-01", "-t", "2015-06-10"};
        String adocFile = "src/test/resources/log_stats.adoc";

        File file = new File(adocFile);
        if (file.exists()) {
            file.delete();
        }

        CMDParser commandLineArguments = new CMDParser(args);

        String path = commandLineArguments.getPath();
        String from = commandLineArguments.getFrom();
        String to = commandLineArguments.getTo();

        try {
            List<LogEntity> logs = LogParser.parseLogs(path);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;
            LocalDate fromDate = from != null ? LocalDate.parse(from, dateFormatter) : null;
            LocalDate toDate = to != null ? LocalDate.parse(to, dateFormatter) : null;
            AdocReportCreator reportCreator = new AdocReportCreator(path, logs, fromDate, toDate);
            reportCreator.saveReport(adocFile);

            assertTrue(file.exists());
            assertTrue(Files.size(file.toPath()) > 0);
        } catch (IOException e) {
            fail();
            throw new RuntimeException(e);
        }
    }
}
