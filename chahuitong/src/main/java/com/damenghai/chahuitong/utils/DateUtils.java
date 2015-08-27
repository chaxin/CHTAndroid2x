package com.damenghai.chahuitong.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sgun on 15/8/12.
 */
public class DateUtils {
    public static String convert2US(String dateStr) {
        if(dateStr == null) return "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
        String result = "";
        try {
            Date date = format.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            result = calendar.get(Calendar.DATE) + "th." + date.toString().substring(4, 7);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }
}
