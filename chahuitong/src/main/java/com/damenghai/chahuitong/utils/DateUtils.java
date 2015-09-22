package com.damenghai.chahuitong.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sgun on 15/8/12.
 */
public class DateUtils {
    /**
     * 转换成美制格式
     *
     * @param dateStr
     * @return
     */
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

    /**
     * 如果str1 比str2小就返回true
     *
     */
    public static boolean smallerCurrentTime(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        long curTime = System.currentTimeMillis();
        try {
            Date date = format.parse(dateStr);

            return date.getTime() < curTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String str = format.format(date);
        return str;
    }

    /**
     * 显示日期选择器窗口
     *
     */
    public static void showDateDialog(Context context, DatePickerDialog.OnDateSetListener listener) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        DatePickerDialog dialog = new DatePickerDialog(context, listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE));
        dialog.show();
    }
}
