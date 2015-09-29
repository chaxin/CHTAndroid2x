package com.damenghai.chahuitong.api;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.damenghai.chahuitong.bean.Status;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.ui.activity.WriteStatusActivity;
import com.damenghai.chahuitong.utils.CommonTool;
import com.damenghai.chahuitong.utils.ImageUtils;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.utils.T;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sgun on 15/9/24.
 */
public class StatusAPI {

    /**
     * 查看领袖发表微博列表
     *
     * @param id
     * @param l
     */
    public static void leaderStatus(int id, int page, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("member_id", id + "");
        map.put("page", page + "");
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/member_content_api", map, l);
    }

    /**
     * 微博列表
     *
     * @param page
     * @param l
     */
    public static void statusShow(int page, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", page + "");
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/get_allperson_content_api", map, l);
    }

    /**
     * 发表新微博
     *
     * @param context
     * @param status
     * @param l
     */
    public static void uploadStatus(final Context context, final Status status, final VolleyRequest l) {
        String content = new Gson().toJson(status);
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("username", SessionKeeper.readUsername(context));
        map.put("content", content);
        HodorRequest.postRequestOnMainThread("http://www.chahuitong.com/wap/index.php/Home/Discuz/save_content_api", map, l);
    }

}
