package com.damenghai.chahuitong2.api;

import android.content.Context;

import com.damenghai.chahuitong2.bean.Banner;
import com.damenghai.chahuitong2.bean.response.BrandResponse;
import com.damenghai.chahuitong2.bean.response.ResponseContent;
import com.damenghai.chahuitong2.response.ResponseCallBackListener;
import com.damenghai.chahuitong2.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;

import java.util.List;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class ShopApi extends UserApi implements Api {

    /** 接口根路径 */
    private static final String BASE_URL = "http://www.chahuitong.com/wap/index.php/Home/Index/";

    /** 分类页Banner */
    private static final String CATE_BANNER = BASE_URL + "mallpic_api";

    /** 品牌列表 */
    private static final String BRAND_LIST = BASE_URL + "brandAjax";

    private Context mContext;

    public ShopApi(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * 分类页Banner
     *
     * @param listener
     */
    public void showCategoryBanners(final ResponseCallBackListener<List<Banner>> listener) {
        if (!NetworkUtils.isConnected(mContext) && listener != null) {
            listener.onError(ErrorEvent.NETWORK_ERROR_CODE, ErrorEvent.NETWORK_ERROR_MSG);
            return;
        }

        new HttpAsyncTask<List<Banner>>(GET, CATE_BANNER, listener) {

            @Override
            public ResponseContent<List<Banner>> parseNetworkResponse(String result) {
                return new Gson().fromJson(result, new TypeToken<ResponseContent<List<Banner>>>(){}.getType());
            }

        }.execute();
    }


    /**
     * 获取品牌列表
     *
     * @param categoryId
     *        1-普洱茶, 2-乌龙茶, 3-红茶, 256-绿茶, 308-黑茶, 470-花茶, 530-白茶, 662-茶具, 593-其他
     */
    public void showBrands(String categoryId, ResponseCallBackListener<BrandResponse> listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("catId", categoryId);

        new HttpAsyncTask<BrandResponse>(POST, BRAND_LIST, params, listener) {

            @Override
            public ResponseContent<BrandResponse> parseNetworkResponse(String result) {
                return new Gson().fromJson(result, new TypeToken<ResponseContent<BrandResponse>>(){}.getType());
            }

        }.execute();

    }

}
