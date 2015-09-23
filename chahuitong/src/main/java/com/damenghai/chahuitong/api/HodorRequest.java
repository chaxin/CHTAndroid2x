package com.damenghai.chahuitong.api;

import android.content.Context;
import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.damenghai.chahuitong.response.IResponseListener;
import com.damenghai.chahuitong.base.BaseApplication;
import com.damenghai.chahuitong.bean.Status;
import com.damenghai.chahuitong.bean.Travel;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.ui.activity.LoginActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sgun on 15/8/13.
 */
public class HodorRequest {
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
     * 以Get方式请求网络数据
     *
     * @param url
     *              请求的地址
     * @param l
     *              请求成功后的回调
     */
    public static void getRequest(String url, final IResponseListener l) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.onAllDone();
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getInt("code") == 200) {
                        l.onSuccess(object.getString("content"));
                    } else {
                        l.onError(object.getString("content"));
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
//                l.onError(error);
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
    public static void postRequest(String url, final Map<String, String> params, final IResponseListener l) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                l.onAllDone();
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getInt("code") == 200) {
                        l.onSuccess(object.getString("content"));
                    } else {
                        l.onError(object.getString("content"));
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        BaseApplication.getRequestQueue().add(request);
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
        postRequest("http://www.chahuitong.com/wap/index.php/home/discuz/home_page_leader_api", map, l);
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
        if(SessionKeeper.readSession(context).equals("") || SessionKeeper.readUsername(context).equals("")) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        } else {
            map.put("key", SessionKeeper.readSession(context));
            map.put("username", SessionKeeper.readUsername(context));
            postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/add_insterst_api", map, l);
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
            postRequest("http://www.chahuitong.com/wap/index.php/home/discuz/move_instersters ", map, l);
        }
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
        postRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/save_content_api", map, l);
    }

}
