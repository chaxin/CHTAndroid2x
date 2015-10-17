package com.damenghai.chahuitong.base;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.utils.SharePreferenceUtil;
import com.pgyersdk.crash.PgyCrashManager;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Sgun on 15/8/13.
 */
public class BaseApplication extends Application {
    private static BaseApplication mInstance;

    private static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/HYQIHEI-45S.OTF")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

//        PgyCrashManager.register(this);
    }

    public static BaseApplication getInstance() {
        return mInstance;
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;

    }

////////////////////////////Bmob//////////////////////

    // 单例模式，才能及时返回数据
    SharePreferenceUtil mSpUtil;
    public static final String PREFERENCE_NAME = "_sharedinfo";

    public synchronized SharePreferenceUtil getSpUtil() {
        if (mSpUtil == null) {
            String currentId = BmobUserManager.getInstance(
                    getApplicationContext()).getCurrentUserObjectId();
            String sharedName = currentId + PREFERENCE_NAME;
            mSpUtil = new SharePreferenceUtil(this, sharedName);
        }
        return mSpUtil;
    }

    public final String PREF_LONGTITUDE = "longtitude";// 经度
    private String longtitude = "";

    /**
     * 获取经度
     *
     * @return
     */
    public String getLongtitude() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        longtitude = preferences.getString(PREF_LONGTITUDE, "");
        return longtitude;
    }

    /**
     * 设置经度
     *
     * @param lon
     */
    public void setLongtitude(String lon) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_LONGTITUDE, lon).commit()) {
            longtitude = lon;
        }
    }

    public final String PREF_LATITUDE = "latitude";// 纬度
    private String latitude = "";

    /**
     * 获取纬度
     *
     * @return
     */
    public String getLatitude() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        latitude = preferences.getString(PREF_LATITUDE, "");
        return latitude;
    }

    /**
     * 设置维度
     *
     * @param lat
     */
    public void setLatitude(String lat) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_LATITUDE, lat).commit()) {
            latitude = lat;
        }
    }

    private Map<String, BmobChatUser> contactList = new HashMap<String, BmobChatUser>();

    /**
     * 设置好友user list到内存中
     * @param contactList
     */
    public void setContactList(Map<String, BmobChatUser> contactList) {
        if (this.contactList != null) {
            this.contactList.clear();
        }
        this.contactList = contactList;
    }

    /**
     * 退出Bmob，清除数据
     *
     */
    public void logout() {
        BmobUserManager.getInstance(getApplicationContext()).logout();
        setContactList(null);
        setLatitude(null);
        setLongtitude(null);
    }
}
