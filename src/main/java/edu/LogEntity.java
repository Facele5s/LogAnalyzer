package edu;

import java.time.LocalDate;

public record LogEntity(
    String ip,
    LocalDate date,
    String request,
    int requestStatus,
    int responseSize,
    String referer,
    String userAgent
) {

}
