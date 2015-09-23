package com.damenghai.chahuitong.api;

import android.content.Context;

import com.damenghai.chahuitong.bean.Product;
import com.damenghai.chahuitong.config.Constants;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * 茶市接口
 *
 * Created by Sgun on 15/9/19.
 */
public class TeaMarketAPI {

    /**
     * 获取茶市产品列表
     *
     * @param saleway
     *              产品的供求信息，1代表卖，2代表买
     * @param page
     *              获取的产品页数，每页产品数为10
     */
    public static void productsShow(String saleway, int page, final VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("saleway", saleway);
        map.put("page", page + "");
        HodorRequest.postRequest(Constants.API_MARKET_PRODUCT, map, l);
    }

    /**
     * 我发布的茶市产品
     *
     * @param l
     */
    public static void myProduct(Context context, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("username", SessionKeeper.readUsername(context));
        HodorRequest.postRequest("http://www.chahuitong.com/mobile/app/b2b/index.php/Home/index/myListapi", map, l);
    }

    /**
     * 删除我发布的茶市产品
     *
     * @param l
     */
    public static void deleteMyProduct(int id, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id + "");
        HodorRequest.postRequest("http://www.chahuitong.com/mobile/app/b2b/index.php/Home/index/deleteapi", map, l);
    }

    /**
     * 编辑我的产品
     *
     * @param context
     * @param product
     * @param l
     */
    public static void EditProduct(Context context, Product product, VolleyRequest l) {
        String json = new Gson().toJson(product);
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("username", SessionKeeper.readUsername(context));
        map.put("content", json);
        HodorRequest.postRequest("http://www.chahuitong.com/mobile/app/b2b/index.php/Home/index/EditorApi/", map, l);
    }
}
