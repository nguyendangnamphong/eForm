package com.fis.fpt;


import org.apache.logging.log4j.util.Strings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class DateUtil {

    public static final String CHARGE_CAMPAIGN_USE_RESULT_PATTERN = "yyyy/MM/dd HH:mm";
    public static final String YEAR_MONTH_TIME= "yyyy-MM-dd HH:mm:ss";
    public static final String YEAR_MONTH = "yyyyMM";
    public static final String YEAR_MONTH_DASH = "yyyy-MM";
    public static final String YEAR_MONTH_DAY_DASH = "yyyy-MM-dd";
    public static final String YEAR_MONTH_DAY_SLASH = "yyyy/MM/dd";

    public static Date parse(String date, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.parse(date);
    }

    public static String format(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static String toString(Date date) {
        if (date == null) {
            return Strings.EMPTY;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        return dateFormat.format(date);
    }

    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public static String getFirstDayOfMonthFormat(Date date, String format) {
        return format(getFirstDayOfMonth(date), format);
    }

    public static int diffInMinutes(Date fromDate, Date toDate) {
        // TODO: check null
        Instant start = fromDate.toInstant();
        Instant end = toDate.toInstant();
        Duration timeElapsed = Duration.between(start, end);
        return (int) timeElapsed.toMinutes();
    }

    public static final String getTimeByMinutes(int times) {
        int hours = times / 60; // since both are ints, you get an int
        int minutes = times % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    public static final String getDateDiffIn24HFormatByDuration(Date fromDate, Date toDate) {
        //TODO: check NPE
        long duration = toDate.getTime() - fromDate.getTime();
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * list of years from now to now year plus extend
     */
    public static List<Integer> getListYearExtend(int extend) {
        List<Integer> years = new ArrayList<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i <= extend; i++) {
            years.add(year);
            year++;
        }
        return years;
    }

    public static List<Integer> getListMonth() {
        List<Integer> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            months.add(i);
        }
        return months;
    }

    public static Date getCurrentTimeByFormat(String format) throws ParseException {
        DateFormat dF = new SimpleDateFormat(format);
        return dF.parse(dF.format(Calendar.getInstance().getTime()));
    }

    public static Date getStartMonthByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            return calendar.getTime();
        } else {
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            date = calendar.getTime();
            return date;
        }
    }

    public static String getCurrentTimeStringByFormat(String format) throws ParseException {
        DateFormat dF = new SimpleDateFormat(format);
        return dF.format(Calendar.getInstance().getTime());
    }

    public static String convertDateToStringByFormat(Date date, String format) {
        DateFormat dF = new SimpleDateFormat(format);
        return dF.format(date);
    }

    public static String convertDateFormatJP(Date date) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, new Locale("ja"));
        return df.format(date);
    }

    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1; // +1 because JANUARY = 0
    }

    public static Date add(Date date, int type, int value) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(type, value);
        return c.getTime();
    }

    public static int getYearOfDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    public static int getMonthOfDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;// +1 because JANUARY = 0
    }

    public static Date convertStringToDate(String date, String format) throws ParseException {
        SimpleDateFormat sd = new SimpleDateFormat(format);
        return sd.parse(date);
    }

    public static String convertFormatDate(String date, String formatOld, String formatNew) throws ParseException {
        DateFormat oldDate = new SimpleDateFormat(formatOld);
        Date dateformatOld = oldDate.parse(date);
        DateFormat newDate = new SimpleDateFormat(formatNew);
        return newDate.format(dateformatOld);
    }
}

