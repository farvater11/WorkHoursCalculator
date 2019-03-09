package com.example.workhourscalculator;

import java.text.SimpleDateFormat;

public class FormatLab {
    static final private SimpleDateFormat dateFormatDayOfWeek = new SimpleDateFormat("EEEE");
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
}
