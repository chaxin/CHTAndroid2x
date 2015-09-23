package com.damenghai.chahuitong.api;

import android.content.Context;

import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.response.IResponseListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sgun on 15/9/22.
 */
public class PersonalAPI {

    /**
     * 个人中心用户信息
     *
     * @param key
     * @param l
     */
    public static void userInfo(String key, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", key);
        HodorRequest.postRequest("http://www.chahuitong.com/mobile/index.php?act=member_index", map, l);
    }

    /**
     * 获取我的地址列表
     *
     * @param context
     * @param listener
     */
    public static void addressShow(Context context, IResponseListener listener) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        HodorRequest.postRequest("http://www.chahuitong.com/mobile/index.php?act=member_address&op=address_list", map, listener);
    }
}
