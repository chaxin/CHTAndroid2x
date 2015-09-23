package com.damenghai.chahuitong.api;

import com.damenghai.chahuitong.request.VolleyRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sgun on 15/9/22.
 */
public class UserAPI {

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
     *http://www.chahuitong.com/wap/index.php/Home/Index/account_create_api
     * post   op (sina  qq)      key(md5  shopnc)     username   password      (sinaopenid ,qqopenid)
     */
    public static void createAccount(String id, String username, String password, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("op", id);
        map.put("key", "804451dc13014b1c785fb73b1617b760");
        map.put("username", username);
        map.put("password", password);
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Index/account_create_api", map, l);
    }
}
