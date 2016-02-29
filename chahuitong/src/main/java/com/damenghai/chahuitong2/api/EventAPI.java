package com.damenghai.chahuitong2.api;

import android.content.Context;

import com.damenghai.chahuitong2.bean.Travel;
import com.damenghai.chahuitong2.config.SessionKeeper;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sgun on 15/10/31.
 */
public class EventAPI {

    /**
     * 获取活动列表
     *
     * @param page
     * @param l
     */
    public static void travelShow(Context context, int page, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("page", page + "");

        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/discuz/get_allperson_active_api", map, l);
    }

    /**
     * 获取我的活动列表
     *
     * @param l
     */
    public static void myTravelShow(Context context, int page, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("username", SessionKeeper.readUsername(context));
        map.put("page", page + "");
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/home/discuz/get_active_api", map, l);
    }

    /**
     * 参加活动
     *
     * @param context
     * @param id
     * @param phone
     * @param l
     */
    public static void joinTravel(Context context, int id, String phone, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("active_id", id + "");
        map.put("key", SessionKeeper.readSession(context));
        map.put("username", SessionKeeper.readUsername(context));
        map.put("telphone", phone);
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/join_active_api", map, l);
    }

    /**
     * 发布一个新的活动
     *
     * @param context
     */
    public static void initiateEvent(Context context, Travel travel, VolleyRequest l) {
        String content = new Gson().toJson(travel);
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("username", SessionKeeper.readUsername(context));
        map.put("content", content);
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/save_active_api", map, l);
    }

    /**
     * 删除我发布的活动
     *
     * @param context
     * @param id
     * @param l
     */
    public static void deleteEvent(Context context, int id, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("username", SessionKeeper.readUsername(context));
        map.put("active_id", id + "");
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/home/discuz/del_active_api", map, l);
    }

    /**
     * 编辑我的活动
     *
     * @param id
     * @param key
     * @param username
     * @param l
     */
    public static void editTravel(String id, String key, String username, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("active_id", id);
        map.put("key", key);
        map.put("username", username);
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/home/discuz/active_editor_api", map, l);
    }

}
