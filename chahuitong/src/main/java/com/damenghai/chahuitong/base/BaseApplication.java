package com.damenghai.chahuitong.base;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.damenghai.chahuitong.R;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Sgun on 15/8/13.
 */
public class BaseApplication extends Application {
    private static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/msyh.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
