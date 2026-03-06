package com.fis.fpt.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
public class TimeUtil {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String yyyyMMddssSSS = "yyyyMMddssSSS";
    public static ConcurrentHashMap<String, SimpleDateFormat> dfmList = new ConcurrentHashMap<>();
    public static ObjectMapper mapper = new ObjectMapper();

    public static String fomatTime(String pattern, Date date) {
        SimpleDateFormat dfm = dfmList.get(pattern);
        if (dfm == null) {
            dfm = new SimpleDateFormat(pattern);
            dfmList.put(pattern, dfm);
        }
        return dfm.format(date);
    }

    public static String currentDateVN() {
        return new SimpleDateFormat("yyyyMMddhhmmss", Locale.US).format(Date.from(Instant.now().plus(Duration.ofHours(7))));
    }

    public static String fomatTime(String pattern, Instant instant) {
        SimpleDateFormat dfm = dfmList.get(pattern);
        if (dfm == null) {
            dfm = new SimpleDateFormat(pattern);
            dfmList.put(pattern, dfm);
        }
        return dfm.format(Date.from(instant));
    }

    public static Date getDate(String pattern, String date) throws Exception {
        SimpleDateFormat dfm = dfmList.get(pattern);
        if (dfm == null) {
            dfm = new SimpleDateFormat(pattern);
            dfmList.put(pattern, dfm);
        }
        return dfm.parse(date);
    }

    public static Instant getInstant(String pattern, String date) throws Exception {
        SimpleDateFormat dfm = dfmList.get(pattern);
        if (dfm == null) {
            dfm = new SimpleDateFormat(pattern);
            dfmList.put(pattern, dfm);
        }
        return dfm.parse(date).toInstant();
    }


    public static String fomatIntTimeToTime(int intTime) {
        int hour = intTime / 100;
        int minute = intTime % 100;
        String sHour = hour + "";
        String sMinute = minute + "";
        if (hour < 10) {
            sHour = "0" + sHour;
        }
        if (minute < 10) {
            sMinute = "0" + sMinute;
        }
        return sHour + ":" + sMinute;
    }

    public static OffsetDateTime timeResponse(Timestamp intTime) {
        return OffsetDateTime.ofInstant(intTime.toInstant(), ZoneId.of("UTC"));
    }


    public static boolean checkExpired(Date date) {
        if (date == null) {
            return false;
        }
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        String todayAsString = df.format(new Date());
        String expireDateAsString = df.format(date);
        Date today;
        Date expireDate;
        try {
            expireDate = df.parse(expireDateAsString);
            today = df.parse(todayAsString);
            return today.compareTo(expireDate) > 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static String getCurrentDate() {
        return fomatTime(YYYY_MM_DD, new Date());
    }

    public static String getStartDate() {
        return "1971-01-01";
    }


    public static Instant getTimeFormat(String s) {
        try {
            return Timestamp.valueOf(s).toInstant();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getTime2Response(Timestamp s) {
        if (s == null) {
            return null;
        }
        return s.toString();
    }


    @SafeVarargs
    public static <T> void removeDuplicatesFromList(List<T> list, Function<T, ?>... keyFunctions) {
        Set<List<?>> set = new HashSet<>();
        ListIterator<T> iter = list.listIterator();
        while (iter.hasNext()) {
            T element = iter.next();
            List<?> functionResults = Arrays.stream(keyFunctions)
                .map(function -> function.apply(element))
                .collect(Collectors.toList());
            if (!set.add(functionResults)) {
                iter.remove();
            }
        }
    }

    @SafeVarargs
    public static <T> List<T> getListWithoutDuplicates(List<T> list, Function<T, ?>... keyFunctions) {
        List<T> result = new ArrayList<>();
        Set<List<?>> set = new HashSet<>();
        for (T element : list) {
            List<?> functionResults = Arrays.stream(keyFunctions)
                .map(function -> function.apply(element))
                .collect(Collectors.toList());
            if (set.add(functionResults)) {
                result.add(element);
            }
        }
        return result;
    }

    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:ms");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public static boolean isValidDateCampaign(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
}

