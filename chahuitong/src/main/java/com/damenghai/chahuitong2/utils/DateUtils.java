package com.damenghai.chahuitong2.utils;

import android.app.DatePickerDialog;
import android.content.Context;

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

    /** 获取聊天时间：因为sdk的时间默认到秒故应该乘1000
     * @Title: getChatTime
     * @Description: TODO
     * @param @param timesamp
     * @param @return
     * @return String
     * @throws
     */
    public static String getChatTime(long timesamp) {
        long clearTime = timesamp*1000;
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(clearTime);
        int temp = Integer.parseInt(sdf.format(today))
                - Integer.parseInt(sdf.format(otherDay));

        switch (temp) {
            case 0:
                result = "今天 " + getHourAndMin(clearTime);
                break;
            case 1:
                result = "昨天 " + getHourAndMin(clearTime);
                break;
            case 2:
                result = "前天 " + getHourAndMin(clearTime);
                break;

            default:
                result = getTime(clearTime);
                break;
        }

        return result;
    }

    public static String getDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        return format.format(new Date(time * 1000L));
    }

    public static String getDateTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return format.format(new Date((time + 3600) * 1000L));
    }

    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
        return format.format(new Date(time));
    }

    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

}
