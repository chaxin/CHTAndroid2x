package com.damenghai.chahuitong2.api;

import android.os.Handler;
import android.os.Looper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.damenghai.chahuitong2.base.BaseApplication;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.response.IResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Sgun on 15/8/13.
 */
public class HodorRequest {
    static Handler mMainLooperHandler = new Handler(Looper.getMainLooper());

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
            public void onResponse(final String response) {
                mMainLooperHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        l.onAllDone();
                        l.onSuccess(response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getInt("code") != 404) {
                                l.onSuccess();
                                l.onListSuccess(obj.getJSONArray("content"));
                            } else {
                                l.onError(obj.getString("content"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

    public static void postRequestOnMainThread(String url, final Map<String, String> params, final VolleyRequest l) {
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
     * 以Post方式请求网络数据，将监听方式封闭成接口，根据实际情况传入不同的实现类可以处理不同数据类型
     *
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

}
