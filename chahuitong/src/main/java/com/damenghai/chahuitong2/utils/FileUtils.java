package com.damenghai.chahuitong2.utils;

import android.text.TextUtils;

import java.io.File;

/**
 * Created by Sgun on 15/9/24.
 */
public class FileUtils {
    public static boolean doesExisted(File file) {
        return file != null && file.exists();
    }

    public static boolean doesExisted(String path) {
        return !TextUtils.isEmpty(path) && doesExisted(new File(path));
    }

}
