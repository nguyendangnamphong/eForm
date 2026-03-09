package com.vnu.uet.converter;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateConverter {
    public static Instant parseStringToZonedDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        // Chuyển đổi chuỗi thành LocalDate
        LocalDate localDate = LocalDate.parse(dateString, formatter);

        // Tạo ZonedDateTime với múi giờ +7 và giờ, phút, giây là 00:00:00
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.of("Asia/Ho_Chi_Minh"));

        // Chuyển đổi ZonedDateTime thành Instant
        Instant instant = zonedDateTime.toInstant();
        return instant;
    }

    public static ZonedDateTime parseStringToZonedDateTime2(String dateStr) {
        return ZonedDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);
    }

    public static Instant parseStringToZonedDateTime3(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        // Chuyển đổi chuỗi thành LocalDate
        LocalDate localDate = LocalDate.parse(dateString, formatter);

        // Tạo LocalDateTime với giờ, phút, giây là 23:59:59
        LocalDateTime endOfDay = localDate.atTime(23, 59, 59);

        // Tạo ZonedDateTime với múi giờ +7
        ZonedDateTime zonedDateTime = endOfDay.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));

        // Chuyển đổi ZonedDateTime thành Instant
        Instant instant = zonedDateTime.toInstant();
        return instant;
    }
}
