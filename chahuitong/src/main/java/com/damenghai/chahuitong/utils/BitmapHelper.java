package com.damenghai.chahuitong.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Sgun on 15/9/24.
 */
public class BitmapHelper {

    /**
     * 判断是否为Bitmap类型
     *
     * @param is
     * @return
     */
    public static boolean verifyBitmap(InputStream is) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Object bis = is instanceof BufferedInputStream ? is : new BufferedInputStream(is);
        BitmapFactory.decodeStream((InputStream) bis, null, options);

        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return options.outWidth > 0 && options.outHeight > 0;
    }

    /**
     * 判断是否为Bitmap类型
     *
     * @param path 文件的绝对路径
     * @return
     */
    public static boolean verifyBitmap(String path) {
        try {
            return !TextUtils.isEmpty(path) && verifyBitmap(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
