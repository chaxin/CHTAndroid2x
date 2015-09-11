package com.damenghai.chahuitong.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.damenghai.chahuitong.base.BaseApplication;
import com.damenghai.chahuitong.bean.Status;
import com.damenghai.chahuitong.bean.Travel;
import com.damenghai.chahuitong.config.Constants;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.utils.StringUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import im.yixin.algorithm.MD5;

/**
 * Created by Sgun on 15/8/13.
 */
public class HodorAPI {
    /**
     * 以Get方式请求网络数据
     *
     * @param url
     *              请求的地址
     * @param l
     *              请求成功后的回调
     */
    public static void getRequest(String url, final VolleyRequest l) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.onAllDone();
                l.onSuccess(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getInt("code") != 404) {
                        l.onSuccess();
                        l.onListSuccess(obj.getJSONArray("content"));
                    } else {
                        l.onError(obj.getString("content"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.onAllDone();
                l.onError(error.toString());
            }
        });

        BaseApplication.getRequestQueue().add(request);
    }

    /**
     * 以Post方式请求网络数据
     *
     * @param url
     *              请求的地址
     * @param params
     *              请求的参数
     * @param l
     *              请求成功后的回调
     *
     */
    public static void postRequest(String url, final Map<String, String> params, final VolleyRequest l) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.onAllDone();
                l.onSuccess(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getInt("code") != 404) {
                        l.onSuccess();
                        l.onListSuccess(obj.getJSONArray("content"));
                    }
                    else l.onError(obj.getString("content"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.onAllDone();
                l.onError(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        BaseApplication.getRequestQueue().add(request);
    }

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
        postRequest("http://106.ihuyi.cn/webservice/sms.php?method=Submit", map, l);
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
        postRequest("http://www.chahuitong.com/wap/index.php/Home/Index/add_count_api", map, l);
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
        map.put("username", username);
        map.put("password", password);
        map.put("client", "android");
        postRequest("http://www.chahuitong.com/mobile/index.php?act=login", map, l);
    }

    /**
     * 获取茶市产品列表
     *
     * @param saleway
     *              产品的供求信息，1代表卖，2代表买
     * @param page
     *              获取的产品页数，每页产品数为10
     */
    public static void teaMarketProducts(String saleway, int page, final VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("saleway", saleway);
        map.put("page", page + "");
        postRequest(Constants.API_MARKET_PRODUCT, map, l);
    }

    /**
     * 获取商品被收藏数
     *
     * @param goods_id
     * @param l
     */
    public static void favorites(String goods_id, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("goods_id", goods_id);
        postRequest("http://www.chahuitong.com/wap/index.php/Home/Index/homeapi/", map, l);
    }

    /**
     * 获取资讯文章数
     *
     * @param id
     *              栏目的ID
     * @param page
     *              页数
     * @param l
     */
    public static void newsShow(String id, int page, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("page", page + "");
        postRequest("http://www.chahuitong.com/wap/index.php/home/news/showMoreApi/", map, l);
    }

    /**
     * 获取品牌列表
     *
     * @param categoryId
     * @param l
     */
    public static void brandShow(int categoryId, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("catId", categoryId + "");
        postRequest("http://www.chahuitong.com/wap/index.php/Home/Index/brandAjax", map, l);
    }

    /**
     * 个人中心用户信息
     *
     * @param key
     * @param l
     */
    public static void userInfo(String key, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", key);
        postRequest("http://www.chahuitong.com/mobile/index.php?act=member_index", map, l);
    }

    /**
     * 显示领袖列表，如果传入的key和username不为0则获取的数据将会有一个当前用户是否关注的字段
     * 如果传入的page有值刚会获取所有领袖列表，否则刚会返回首页显示的列表
     *
     * @param key
     *              key
     * @param username
     *              用户名
     * @param l
     *              监听
     */
    public static void followShow(int page, String key, String username, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        if(page > 0) map.put("page", page + "");
        map.put("key", key);
        map.put("username", username);
        postRequest("http://www.chahuitong.com/wap/index.php/home/discuz/home_page_leader_api", map, l);
    }

    /**
     * 获取我关注的人
     *
     * @param key
     * @param username
     * @param l
     */
    public static void myFollowShow(int page, String key, String username, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", page + "");
        map.put("key", key);
        map.put("username", username);
        postRequest("http://www.chahuitong.com/wap/index.php/home/discuz/insterst_member_api", map, l);
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
        map.put("key", SessionKeeper.readSession(context));
        map.put("username", SessionKeeper.readUsername(context));
        postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/add_insterst_api", map, l);
    }

    /**
     * 取消对某人的关注
     *
     * @param member_id
     * @param l
     */
    public static void removeFollow(int member_id, String key, String username, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("member_id", member_id + "");
        map.put("key", key);
        map.put("username", username);
        postRequest("http://www.chahuitong.com/wap/index.php/home/discuz/move_instersters ", map, l);
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
        postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/add_zan_api", map, l);
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
        postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/add_share_api", map, l);
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
        postRequest("http://www.chahuitong.com/wap/index.php/home/discuz/get_user_topic_api", map, l);
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
        postRequest("http://www.chahuitong.com/wap/index.php/home/discuz/del_content_api", map, l);
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
        postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/get_content_comment_api", map, l);
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
        postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/add_content_comment_api", map, l);
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
        postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/get_comment_api", map, l);
    }

    /**
     * 获取活动列表
     *
     * @param page
     * @param l
     */
    public static void travelShow(int page, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page", page + "");
        postRequest("http://www.chahuitong.com/wap/index.php/Home/discuz/get_allperson_active_api", map, l);
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
        postRequest("http://www.chahuitong.com/wap/index.php/home/discuz/get_active_api", map, l);
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
        postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/join_active_api", map, l);
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
        postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/save_active_api", map, l);
    }

    public static void deleteEvent(Context context, int id, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("username", SessionKeeper.readUsername(context));
        map.put("active_id", id + "");
        postRequest("http://www.chahuitong.com/wap/index.php/home/discuz/del_active_api", map, l);
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
        postRequest("http://www.chahuitong.com/wap/index.php/home/discuz/active_editor_api", map, l);
    }

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
        postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/member_content_api", map, l);
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
        postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/get_allperson_content_api", map, l);
    }

    /**
     * 发表新微博
     *
     * @param context
     * @param status
     * @param l
     */
    public static void uploadStatus(Context context, Status status, VolleyRequest l) {
        String content = new Gson().toJson(status);
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("username", SessionKeeper.readUsername(context));
        map.put("content", content);
        L.d("content:" + content);
        postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/save_content_api", map, l);
    }

    /**
     * 资讯中文章详情
     *
     * @param id
     * @param l
     */
    public static void articleShow(String id, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("article_id", id);
        postRequest("http://www.chahuitong.com/wap/index.php/home/news/newsDetailApi", map, l);
    }

    /**
     * 我发布的茶市产品
     *
     * @param l
     */
    public static void myProductShow(Context context, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("username", SessionKeeper.readUsername(context));
        postRequest("http://www.chahuitong.com/mobile/app/b2b/index.php/Home/index/myListapi", map, l);
    }

    /**
     * 删除我发布的茶市产品
     *
     * @param l
     */
    public static void deleteMyProduct(int id, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id + "");
        postRequest("http://www.chahuitong.com/mobile/app/b2b/index.php/Home/index/deleteapi", map, l);
    }
}
