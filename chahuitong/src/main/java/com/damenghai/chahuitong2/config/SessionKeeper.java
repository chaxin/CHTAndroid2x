package com.damenghai.chahuitong2.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Sgun on 15/8/11.
 */
public class SessionKeeper {
    private static final String PREFERENCES_NAME = "chahuitong_prefs";
    private static final String KEY = "key";
    private static final String USERNAME = "username";

    /**
     * 保存信息到 SharedPreference
     *
     * @param context
     * @param key
     * @param value
     */
    public static void writeValue(Context context, String key, String value) {
        if(null == context) return;

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String readValue(Context context, String key) {
        if(null == context) return null;

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    /**
     * 保存 Key 字符串到 SharedPreference
     *
     * @param context
     *              应用程序上下文
     * @param key
     *              返回 Key 字符串
     */
    public static void writeSession(Context context, String key) {
        writeValue(context, KEY, key);
    }

    /**
     * 从 SharedPreference 获取Key
     *
     * @param context
     *              应用程序上下文
     * @return
     *              返回 Key 字符串
     */
    public static String readSession(Context context) {
        return readValue(context, KEY);
    }

    /**
     * 保存用户名到SharedPreference
     *
     * @param context
     *              应用程序上下文
     * @param username
     *              返回 Username 字符串
     */
    public static void writeUsername(Context context, String username) {
        writeValue(context, USERNAME, username);
    }

    /**
     * 从SharedPreference中获取用户名
     *
     * @return
     *          返回用户名
     */
    public static String readUsername(Context context) {
        return readValue(context, USERNAME);
    }

    /**
     * 清空 SharedPreference 中的 Key 信息
     *
     * @param context 应用程序上下文
     */
    public static void clearSession(Context context) {
        if(null == context) return;

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
