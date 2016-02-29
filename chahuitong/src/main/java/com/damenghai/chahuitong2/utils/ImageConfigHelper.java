package com.damenghai.chahuitong2.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.damenghai.chahuitong2.R;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;

/**
 * Created by Sgun on 15/9/7.
 */
public class ImageConfigHelper {
    public static BitmapDisplayConfig getIconConfig(Context context) {
        BitmapDisplayConfig config = new BitmapDisplayConfig();
        config.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.default_load_image));
        config.setLoadingDrawable(context.getResources().getDrawable(R.drawable.default_load_image));
        config.setBitmapConfig(Bitmap.Config.RGB_565);
        return config;
    }

    public static BitmapDisplayConfig getGrayConfig(Context context) {
        BitmapDisplayConfig config = new BitmapDisplayConfig();
        config.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.timeline_image_failure));
        config.setLoadingDrawable(context.getResources().getDrawable(R.drawable.timeline_image_loading));
        config.setBitmapConfig(Bitmap.Config.RGB_565);
        return config;
    }

    public static BitmapDisplayConfig getAvatarConfig(Context context) {
        BitmapDisplayConfig config = new BitmapDisplayConfig();
        config.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.avatar_default));
        config.setLoadingDrawable(context.getResources().getDrawable(R.drawable.avatar_default));
        config.setBitmapConfig(Bitmap.Config.RGB_565);
        return config;
    }
}
