package com.damenghai.chahuitong2.api;

import android.content.Context;
import android.content.Intent;

import com.damenghai.chahuitong2.bean.Leader;
import com.damenghai.chahuitong2.config.Config;
import com.damenghai.chahuitong2.config.SessionKeeper;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.response.IResponseListener;
import com.damenghai.chahuitong2.ui.activity.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class AccountApi {

    /**
     * 发送验证码
     *
     * @param mobile
     * @param code
     * @param l
     */
    public static void sendSMS(String mobile, int code, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("account", "cf_chahuitong");
        map.put("password", "chahuitong2015");
        map.put("mobile", mobile);
        map.put("content", "你申请的手机验证码是：" + code + "，请不要把验证码泄露给其他人。如非本人操作，请忽略本条消息！");
        HodorRequest.postRequest("http://106.ihuyi.cn/webservice/sms.php?method=Submit", map, l);
    }

    /**
     * 注册账号
     *
     * @param mobile
     * @param password
     * @param l
     */
    public static void register(String mobile, String password, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobilenumber", mobile);
        map.put("password", password);
        map.put("check", "804451dc13014b1c785fb73b1617b760");
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Index/add_count_api", map, l);
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param l
     */
    public static void login(String username, String password, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("usermobile", username);
        map.put("password", password);
        map.put("client", "android");
        HodorRequest.postRequest("http://www.chahuitong.com/mobile/index.php?act=login", map, l);
    }

    /**
     * 使用第三方登录
     *
     * post   op (sina  qq)      key(md5  shopnc)     username   password      (sinaopenid ,qqopenid)
     *
     */
    public static void createAccount(String type, String username, String password, String openid, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("op", type);
        map.put("key", Config.md5Key);
        map.put("username", username);
        map.put("password", password);
        map.put("openid", openid);
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Index/account_create_api", map, l);
    }

    /**
     * 获取个人资料
     *
     * @param context
     */
    public static void showProfile(Context context, IResponseListener l) {
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
    public static void updateProfile(Context context, Leader leader, IResponseListener l) {
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

    /**
     * 检测账号是否注册
     *
     * @param mobile
     * @param l
     */
    public static void isRegister(String mobile, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", Config.md5Key);
        map.put("mobile", mobile);
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Index/isExistsMobileApi", map, l);
    }

    /**
     * 更改用户密码
     *
     * @param mobile
     * @param password
     * @param l
     */
    public static void updatePassword(String mobile, String password, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", Config.md5Key);
        map.put("mobile", mobile);
        map.put("password", password);
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Index/updateMemberPasswdApi", map, l);
    }


    /**
     * 显示领袖列表，如果传入的key和username不为空则获取的数据将会有一个当前用户是否关注的字段
     * 如果传入的page有值刚会获取所有领袖列表，否则刚会返回首页显示的列表
     *
     * @param l
     *              监听
     */
    public static void followShow(Context context, int page, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        if(page > 0) map.put("page", page + "");
        map.put("key", SessionKeeper.readSession(context));
        map.put("username", SessionKeeper.readUsername(context));
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/home/discuz/home_page_leader_api", map, l);
    }

    /**
     * 获取我关注的人
     *
     * @param l
     */
    public static void myFollowShow(Context context, int page, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", page + "");
        map.put("key", SessionKeeper.readSession(context));
        map.put("username", SessionKeeper.readUsername(context));
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/home/discuz/insterst_member_api", map, l);
    }

    /**
     * 添加对某人的关注
     *
     * @param member_id
     * @param l
     */
    public static void addFollow(Context context, int member_id, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("member_id", member_id + "");
        if(SessionKeeper.readSession(context).equals("") || SessionKeeper.readUsername(context).equals("")) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        } else {
            map.put("key", SessionKeeper.readSession(context));
            map.put("username", SessionKeeper.readUsername(context));
            HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/add_insterst_api", map, l);
        }
    }

    /**
     * 取消对某人的关注
     *
     * @param member_id
     * @param l
     */
    public static void removeFollow(Context context, int member_id, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("member_id", member_id + "");
        if(SessionKeeper.readSession(context).equals("") || SessionKeeper.readUsername(context).equals("")) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        } else {
            map.put("key", SessionKeeper.readSession(context));
            map.put("username", SessionKeeper.readUsername(context));
            HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/home/discuz/move_instersters", map, l);
        }
    }

}
