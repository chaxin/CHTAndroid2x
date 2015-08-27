package com.damenghai.chahuitong.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.damenghai.chahuitong.BaseApplication;
import com.damenghai.chahuitong.config.Constants;
import com.damenghai.chahuitong.request.VolleyRequest;

import java.util.HashMap;
import java.util.Map;

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
    public static void stringPostRequest(String url, final Map<String, String> params, final VolleyRequest l) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.onAllDone();
                l.onSuccess(response);
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
        stringPostRequest(Constants.API_MARKET_PRODUCT, map, l);
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
        stringPostRequest("http://www.chahuitong.com/wap/index.php/Home/Index/homeapi/", map, l);
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
        stringPostRequest("http://www.chahuitong.com/wap/index.php/home/news/showMoreApi/", map, l);
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
        stringPostRequest("http://www.chahuitong.com/wap/index.php/Home/Index/brandAjax", map, l);
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
        stringPostRequest("http://www.chahuitong.com/mobile/index.php?act=member_index", map, l);
    }

    /**
     * 获取我关注的人
     *
     * @param key
     * @param username
     * @param l
     */
    public static void myFollowShow(String key, String username, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", key);
        map.put("username", username);
        stringPostRequest("http://www.chahuitong.com/wap/index.php/home/discuz/insterst_member_api", map, l);
    }

    /**
     * 获取我发表的话题
     *
     *
     *
     * @param key
     * @param username
     * @param l
     */
    public static void myTopicShow(String key, String username, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", key);
        map.put("username", username);
        stringPostRequest("http://www.chahuitong.com/wap/index.php/home/discuz/get_user_topic_api", map, l);
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
        stringPostRequest("http://www.chahuitong.com/wap/index.php/home/news/newsDetailApi", map, l);
    }

    public static void myCommentShow(String key, String username, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", key);
        stringPostRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/get_comment_api", map, l);
    }

    public static void myProductShow(String key, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", key);
        stringPostRequest("http://www.chahuitong.com/mobile/app/b2b/index.php/Home/index/myListapi", map, l);
    }
}
