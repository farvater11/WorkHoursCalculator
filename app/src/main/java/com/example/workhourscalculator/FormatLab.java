package com.example.workhourscalculator;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormatLab {
    static final private SimpleDateFormat dateFormatDayOfWeek = new SimpleDateFormat("EE");
    static final private SimpleDateFormat dateFormatDayOfMonth = new SimpleDateFormat("dd MMM");
    static final private SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm");

    public static SimpleDateFormat getDateFormatDayOfWeek() {
        return dateFormatDayOfWeek;
    }
    public static SimpleDateFormat getDateFormatDayOfMonth() {
        return dateFormatDayOfMonth;
    }
    public static SimpleDateFormat getDateFormatTime() {
        return dateFormatTime;
    }


    public static int getHourOfDay(Date date){
        return getCalendar(date).get(Calendar.HOUR_OF_DAY);
    }
    public static int getMinute(Date date){
        return getCalendar(date).get(Calendar.MINUTE);
    }
    public static int getYear(Date date){
        return getCalendar(date).get(Calendar.YEAR);
    }
    public static int getMonth(Date date){
        return getCalendar(date).get(Calendar.MONTH);
    }
    public static int getDayOfMonth(Date date){
         return getCalendar(date).get(Calendar.DAY_OF_MONTH);
    }
    public static int getDayOfWeek(Date date){
        return getCalendar(date).get(Calendar.DAY_OF_WEEK);
    }


    private static Calendar getCalendar(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
