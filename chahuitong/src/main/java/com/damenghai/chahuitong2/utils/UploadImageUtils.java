package com.damenghai.chahuitong2.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Sgun on 15/9/24.
 */
public class UploadImageUtils {

    public static File revisionPostImageSize(Context context, Uri picUri) {
        return revisionPostImageSize(context, ImageUtils.getImageAbsolutePath19((Activity) context, picUri));
    }

    public static File revisionPostImageSize(Context context, String filePath) {
        try {
            if(NetworkUtils.isWifi(context)) {
                // 需要高清图
                // revisionImageSize(file, 1600, 75);
                return revisionImageSize(context, filePath, 1500, 75);
            } else {
                return revisionImageSize(context, filePath, 1024, 75);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void revisionImageSizeHD(Context context, String picFile, int size, int quality) throws IOException{
        revisionImageSize(context, picFile, size, quality);
    }

    /**
     * 压缩图片
     *
     * @param context
     * @param picPath
     * @param size
     * @param quality
     * @return
     * @throws IOException
     */
    private static File revisionImageSize(Context context, String picPath, int size, int quality) throws IOException {
        if(size < 0) {
            throw new IllegalArgumentException("size must be greater than 0");
        } else if(!FileUtils.doesExisted(picPath)) {
            throw  new FileNotFoundException(picPath == null ? "null" : picPath);
        } else if(!BitmapHelper.verifyBitmap(picPath)) {
            throw new IOException("the file type is not image");
        } else {
            FileInputStream fis = new FileInputStream(picPath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(fis, null, options);

            int temp;
            // 右移两位就相当于除以2
            temp = 0;
            while (options.outWidth >> temp > size || options.outHeight >> temp > size) {
                ++temp;
            }

            options.inSampleSize = (int) Math.pow(2.0, temp);
            options.inJustDecodeBounds = false;
            Bitmap bmp = safeDecodeBitmap(picPath, options);
            File file;
            if(bmp == null) {
                throw new IOException("bitmap decode error!");
            } else {
                String name = new File(picPath).getName();

                file = new File(context.getExternalCacheDir(), name);
                FileOutputStream output = new FileOutputStream(file);

                if (options.outMimeType != null && options.outMimeType.contains("png")) {
                    bmp.compress(Bitmap.CompressFormat.PNG, quality, output);
                } else {
                    bmp.compress(Bitmap.CompressFormat.JPEG, quality, output);
                }

                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                bmp.recycle();

                return file;
            }
        }
    }

    /**
     * 防止OOM
     *
     * @param bmpFile
     * @param options
     * @return
     */
    public static Bitmap safeDecodeBitmap(String bmpFile, BitmapFactory.Options options) {
        BitmapFactory.Options optsTmp = options;
        if(optsTmp == null) {
            optsTmp = new BitmapFactory.Options();
            optsTmp.inSampleSize = 1;
        }

        Bitmap bmp = null;
        FileInputStream fis = null;

        int i = 1;
        while (i < 5) {
            try {
                fis = new FileInputStream(bmpFile);
                bmp = BitmapFactory.decodeStream(fis, null, optsTmp);
                break;
            } catch (OutOfMemoryError error) {
                error.printStackTrace();

                optsTmp.inSampleSize *= 2;

                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ++i;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }

        return bmp;
    }
}
