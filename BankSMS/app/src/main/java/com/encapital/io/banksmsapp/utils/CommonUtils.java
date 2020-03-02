package com.encapital.io.banksmsapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CommonUtils {

    public static String getCurrentDate() {
        return getCurrentDate("yyyy-MM-dd");
    }

    public static String getCurrentDate(String format) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
//        Calendar cal = Calendar.getInstance();
//        return dateFormat.format(cal);
        return new SimpleDateFormat(format).format(new java.util.Date());
    }

}
