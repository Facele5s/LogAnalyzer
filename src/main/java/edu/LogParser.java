package edu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {
    private static final Pattern LOG_PATTERN = Pattern.compile(
        "^([\\d.]+) - - \\[(.*)] \"(\\S+ \\S+ \\S+)\" (\\d+) (\\d+) \"(\\S+)\" \"(\\S+)\"$"
    );

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
        "dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH
    );

    private LogParser() {

    }

    public static List<LogEntity> parseLogs(String address) throws IOException {
        List<LogEntity> logs;

        try {
            logs = parseLogsFromFile(Path.of(address));
        } catch (Exception e) {
            logs = parseLogsFromUrl(address);
        }

        return logs;
    }

    private static List<LogEntity> parseLogsFromUrl(String url) throws MalformedURLException {
        URL urlFile = new URL(url);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlFile.openStream()))) {
            return reader.lines()
                .map(LogParser::parseLogFromLine)
                .filter(Objects::nonNull)
                .toList();
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private static List<LogEntity> parseLogsFromFile(Path path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            return reader.lines()
                .map(LogParser::parseLogFromLine)
                .filter(Objects::nonNull)
                .toList();
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    @SuppressWarnings("MagicNumber")
    private static LogEntity parseLogFromLine(String line) {
        Matcher matcher = LOG_PATTERN.matcher(line);

        if (matcher.matches()) {
            String ip = matcher.group(1);

            String strDate = matcher.group(2);
            LocalDate date = LocalDate.parse(strDate, DATE_TIME_FORMATTER);

            String request = matcher.group(3);

            int requestStatus = Integer.parseInt(matcher.group(4));

            int responseSize = Integer.parseInt(matcher.group(5));

            String referer = matcher.group(6);

            String userAgent = matcher.group(7);

            return new LogEntity(ip, date, request, requestStatus, responseSize, referer, userAgent);
        }

        return null;
    }

}
