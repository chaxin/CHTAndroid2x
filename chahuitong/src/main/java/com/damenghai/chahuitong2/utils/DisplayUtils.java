package com.damenghai.chahuitong2.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

public class DisplayUtils {
	/**
	 * 获取屏幕宽度
	 */
	public static int getScreenWidthPixels(Activity context) {
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric.widthPixels;
	}

	/**
	 * 获取屏幕高度
	 */
	public static int getScreenHeightPixels(Activity context) {
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric.heightPixels;
	}
}