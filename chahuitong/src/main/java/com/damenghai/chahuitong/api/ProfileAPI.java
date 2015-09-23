package com.damenghai.chahuitong.api;

import android.content.Context;
import android.content.Intent;

import com.damenghai.chahuitong.response.IResponseListener;
import com.damenghai.chahuitong.bean.Leader;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.ui.activity.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sgun on 15/9/19.
 */
public class ProfileAPI {

    /**
     * 获取个人资料
     *
     * @param context
     */
    public static void show(Context context, IResponseListener l) {
        Map<String, String> map = new HashMap<String, String>();
        if(SessionKeeper.readSession(context).equals("")) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        } else {
            map.put("key", SessionKeeper.readSession(context));
            HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Index/mermber_info_api", map, l);
        }
    }

    /**
     * 更新个人资料
     *
     * @param context
     * @param leader
     * @param l
     */
    public static void update(Context context, Leader leader, IResponseListener l) {
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting().create();
        String content = gson.toJson(leader);
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("content", content);
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Index/info_update_api", map, l);
    }

}
