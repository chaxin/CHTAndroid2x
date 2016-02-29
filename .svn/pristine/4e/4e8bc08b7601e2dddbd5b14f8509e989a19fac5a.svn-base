package com.damenghai.chahuitong.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Sgun on 15/7/20.
 */
public class T {

    private T() {
		/* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static Toast toast;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param resId
     */
    public static void showShort(Context context, int resId) {
        show(context, resId, Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        show(context, message, Toast.LENGTH_LONG);
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param resId
     */
    public static void showLong(Context context, int resId) {
        show(context, resId, Toast.LENGTH_LONG);
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    private static void show(Context context, CharSequence message, int duration) {
        if(toast == null) {
            toast = Toast.makeText(context, message, duration);
        } else {
            toast.setText(message);
            toast.setDuration(duration);
        }
        toast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param resId
     * @param duration
     */
    private static void show(Context context, int resId, int duration) {
        if(toast == null) {
            toast = Toast.makeText(context, resId, duration);
        } else {
            toast.setText(resId);
            toast.setDuration(duration);
        }
        toast.show();
    }

}
