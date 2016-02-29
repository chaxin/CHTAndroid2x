package com.damenghai.chahuitong2.api;

import android.content.Context;

import com.damenghai.chahuitong2.bean.Status;
import com.damenghai.chahuitong2.config.SessionKeeper;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.google.gson.Gson;

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

    /**
     * 点赞
     *
     * @param context
     * @param content_id
     * @param l
     */
    public static void statusLike(Context context, int content_id, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("content_id", content_id + "");
        map.put("key", SessionKeeper.readSession(context));
        map.put("username", SessionKeeper.readUsername(context));
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/add_zan_api", map, l);
    }

    /**
     * 分享微博
     *
     * @param statusID
     * @param l
     */
    public static void statusShare(int statusID, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("content_id", statusID + "");
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/add_share_api", map, l);
    }

    /**
     * 获取我发表的微博
     *
     * @param l
     */
    public static void myStatusShow(Context context, int page, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("username", SessionKeeper.readUsername(context));
        map.put("page", page + "");
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/home/discuz/get_user_topic_api", map, l);
    }

    /**
     * 删除我的微博
     *
     * @param content_id
     * @param key
     * @param username
     * @param l
     */
    public static void deleteStatus(int content_id, String key, String username, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("content_id", content_id + "");
        map.put("key", key);
        map.put("username", username);
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/home/discuz/del_content_api", map, l);
    }

    /**
     * 获取某条微博的评论
     *
     * @param content_id
     * @param l
     */
    public static void commentShow(int content_id, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("content_id", content_id + "");
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/get_content_comment_api", map, l);
    }

    /**
     * 提交评论
     *
     * @param context
     * @param content_id
     * @param comment
     * @param reply_to
     * @param l
     */
    public static void uploadComment(Context context, int content_id, String comment, String reply_to, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("username", SessionKeeper.readUsername(context));
        map.put("content_id", content_id + "");
        map.put("comment", comment);
        if(reply_to != null) map.put("reply_to", reply_to);
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/add_content_comment_api", map, l);
    }

    /**
     * 我的评论列表
     *
     * @param l
     */
    public static void myCommentShow(Context context, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("username", SessionKeeper.readUsername(context));
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/get_comment_api", map, l);
    }

}
