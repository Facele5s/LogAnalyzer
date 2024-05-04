package edu;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogAnalyzer {
    private final List<LogEntity> logs;

    public LogAnalyzer(List<LogEntity> logs) {
        this.logs = logs;
    }

    public long countRequests(LocalDate from, LocalDate to) {
        return logs.stream()
            .filter(log -> isBetweenDates(log.date(), from, to))
            .count();
    }

    public Map<String, Long> countResourceRequests(LocalDate from, LocalDate to) {
        return logs.stream()
            .filter(log -> isBetweenDates(log.date(), from, to))
            .collect(Collectors.groupingBy(LogEntity::request, Collectors.counting()));
    }

    public Map<Integer, Long> countResponseCodes(LocalDate from, LocalDate to) {
        return logs.stream()
            .filter(log -> isBetweenDates(log.date(), from, to))
            .collect(Collectors.groupingBy(LogEntity::requestStatus, Collectors.counting()));
    }

    public double calculateAverageResponseSize(LocalDate from, LocalDate to) {
        List<LogEntity> correctLogs = logs.stream()
            .filter(log -> isBetweenDates(log.date(), from, to)).toList();

        if (correctLogs.isEmpty()) {
            return 0D;
        }

        return correctLogs.stream()
            .mapToDouble(LogEntity::responseSize)
            .sum() / correctLogs.size();
    }

    public static boolean isBetweenDates(LocalDate date, LocalDate from, LocalDate to) {
        return ((from == null || date.isAfter(from) || date.isEqual(from))
            && (to == null || date.isBefore(to) || date.isEqual(to)));
    }
}
