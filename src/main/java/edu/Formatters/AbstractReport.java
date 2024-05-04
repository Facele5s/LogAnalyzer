package edu.Formatters;

import edu.LogAnalyzer;
import edu.LogEntity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"MagicNumber", "MultipleStringLiterals"})
public abstract class AbstractReport {
    private static final Map<Integer, String> RESPONSE_CODES = Map.ofEntries(
        Map.entry(200, "OK"),
        Map.entry(404, "Not Found"),
        Map.entry(500, "Internal Server Error"),
        Map.entry(502, "Bad Gateway"),
        Map.entry(503, "Service Unavailable"),
        Map.entry(504, "Gateway Timeout")
    );

    protected final StringBuilder report;

    protected LocalDate fromDate = null;
    protected LocalDate toDate = null;

    protected final StringBuilder tableRow;

    protected final String[] headerMain = new String[] {
        "Метрика", "Значение"
    };
    protected final String[][] tableMain;
    protected final String[] headerResources = new String[] {
        "Ресурс", "Количество"
    };
    protected final String[][] tableResources;
    protected final String[] headerResponses = new String[] {
        "Код", "Имя", "Количество"
    };
    protected final String[][] tableResponses;

    public AbstractReport(String logFile, List<LogEntity> logs, LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        report = new StringBuilder();

        tableRow = new StringBuilder();
        LogAnalyzer analyzer = new LogAnalyzer(logs);

        tableMain = new String[][] {
            {"Файл (-ы)", logFile},
            {"Начальная дата", (fromDate != null ? fromDate.format(DateTimeFormatter.ISO_DATE) : "-")},
            {"Конечная дата", (toDate != null ? toDate.format(DateTimeFormatter.ISO_DATE) : "-")},
            {"Количество запросов", Long.toString(analyzer.countRequests(fromDate, toDate))},
            {"Средний размер ответа", analyzer.calculateAverageResponseSize(fromDate, toDate) + "b"}
        };

        Map<String, Long> resources = analyzer.countResourceRequests(fromDate, toDate);
        tableResources = new String[resources.size()][2];
        int i = 0;
        for (Map.Entry<String, Long> entry : resources.entrySet()) {
            tableResources[i][0] = entry.getKey();
            tableResources[i][1] = Long.toString(entry.getValue());
            i++;
        }
        Arrays.sort(tableResources, Comparator.comparingLong(n -> -Long.parseLong(n[1])));

        Map<Integer, Long> responses = analyzer.countResponseCodes(fromDate, toDate);
        tableResponses = new String[responses.size()][3];
        i = 0;
        for (Map.Entry<Integer, Long> entry : responses.entrySet()) {
            tableResponses[i][0] = Integer.toString(entry.getKey());
            tableResponses[i][1] = RESPONSE_CODES.get(entry.getKey());
            tableResponses[i][2] = Long.toString(entry.getValue());
            i++;
        }
        Arrays.sort(tableResponses, Comparator.comparingLong(n -> -Long.parseLong(n[2])));
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
}
