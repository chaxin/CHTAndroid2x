package com.damenghai.chahuitong2.api;

import android.content.Context;

import com.damenghai.chahuitong2.bean.Banner;
import com.damenghai.chahuitong2.bean.Goods;
import com.damenghai.chahuitong2.bean.response.ResponseContent;
import com.damenghai.chahuitong2.response.ResponseCallBackListener;
import com.damenghai.chahuitong2.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class HomeApi implements Api {
    private static final String BASE_URL = "http://www.chahuitong.com/wap/index.php/Home/Index/";

    private static final String BANNER = BASE_URL + "homepic_api";

    private static final String PRODUCT = BASE_URL + "home_promotion_goods";

    private Context mContext;

    public HomeApi(Context context) {
        mContext = context;
    }

    public void showBanners(final ResponseCallBackListener<List<Banner>> listener) {

        if (!NetworkUtils.isConnected(mContext)) {
            if (listener != null) {
                listener.onError(ErrorEvent.NETWORK_ERROR_CODE, ErrorEvent.NETWORK_ERROR_MSG);
            }
        }

        new HttpAsyncTask<List<Banner>>(GET, BANNER, listener) {

            @Override
            public ResponseContent<List<Banner>> parseNetworkResponse(String result) {
                Type type = new TypeToken<ResponseContent<List<Banner>>>() {}.getType();
                ResponseContent<List<Banner>> banners = new Gson().fromJson(result, type);
                if (banners != null && banners.isSuccess()) return banners;
                return null;
            }

        }.execute();

    }

    public void showProduct(final ResponseCallBackListener<Goods> listener) {
        if (!NetworkUtils.isConnected(mContext) && listener != null) {
            listener.onError(ErrorEvent.NETWORK_ERROR_CODE, ErrorEvent.NETWORK_ERROR_MSG);
        }

        new HttpAsyncTask<Goods>(GET, PRODUCT, listener) {

            @Override
            public ResponseContent<Goods> parseNetworkResponse(String result) {
                Type type = new TypeToken<ResponseContent<Goods>>(){}.getType();
                ResponseContent<Goods> goods = new Gson().fromJson(result, type);
                if (goods != null && goods.isSuccess()) return goods;
                return null;
            }

        }.execute();

    }

}