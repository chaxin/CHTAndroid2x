package com.damenghai.chahuitong2.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.damenghai.chahuitong2.bean.Product;
import com.damenghai.chahuitong2.bean.Quotation;
import com.damenghai.chahuitong2.bean.response.MarketProductsResponse;
import com.damenghai.chahuitong2.bean.response.ResponseContent;
import com.damenghai.chahuitong2.config.SessionKeeper;
import com.damenghai.chahuitong2.response.IResponseListener;
import com.damenghai.chahuitong2.response.ResponseCallBackListener;
import com.damenghai.chahuitong2.utils.NetworkUtils;
import com.damenghai.chahuitong2.utils.UploadImageUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 茶市接口
 *
 * Created by Sgun on 15/9/19.
 */
public class TeaMarketApi implements Api {

    private static final String BASE_URL = "http://www.chahuitong.com/mobile/app/b2b/index.php/Home/Index/";

    /** 产品详情 */
    private static final String USER_INFO = BASE_URL + "productDetailApi";

    /** 获取行情 */
    private static final String PRODUCT_QUOTATIONS = BASE_URL + "qutation_api";

    /** 获取茶市所有产品 */
    private static final String PRODUCT_LIST = BASE_URL + "product_list";

    /** 我的产品列表 */
    private static final String MY_PRODUCT_LIST = BASE_URL + "myListapi";

    /** 删除产品 */
    private static final String DELETE_PRODUCT = BASE_URL + "deleteapi";

    /** 发布产品 */
    private static final String PUBLISH_PRODUCT = BASE_URL + "post_save";

    /** 登录令牌 */
    private String mKey;

    /** 上下文 */
    private Context mContext;

    public TeaMarketApi(Context context) {
        this.mContext = context;
        this.mKey = SessionKeeper.readSession(context);
    }

    /**
     * 获取该商品发布者ID
     *
     *  TODO 接口返回的数据有错，需要改
     *
     * @param productId
     *                  商品ID
     */
    public static void getUserInfo(String productId, IResponseListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("id", productId);
        HodorRequest.postRequest(USER_INFO, map, listener);

//        RequestParams params = new RequestParams();
//        params.addBodyParameter("id", productId);
//        mHttpUtils.send(POST, USER_INFO, params, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                L.d("user info: " + responseInfo.result);
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//
//            }
//        });
    }

    /**
     * 获取行情列表
     *
     * @param page
     *                  当前页数
     * @param brandId
     *                  品牌ID
     */
    public void quotationList(final int page, final String brandId, final ResponseCallBackListener<List<Quotation>> listener) {

        if (page <= 0) {
            if (listener != null) listener.onError(ErrorEvent.PARAM_ILLEGAL, "页数出错");
            return;
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("page", page + "");
        params.addBodyParameter("brandid", brandId);
        new HttpAsyncTask<List<Quotation>>(POST, PRODUCT_QUOTATIONS, params, listener) {

            @Override
            public ResponseContent<List<Quotation>> parseNetworkResponse(String result) {
                Type type = new TypeToken<ResponseContent<List<Quotation>>>(){}.getType();
                ResponseContent<List<Quotation>> quotations = new Gson().fromJson(result, type);
                if (quotations.isSuccess()) return quotations;
                return null;
            }

        }.execute();

    }

    /**
     * 获取茶市产品列表
     *
     * @param saleway
     *              产品的供求信息，1代表出售，2代表求购
     * @param page
     *              获取的产品页数，每页产品数为10
     */
    public void productsShow(final String saleway, final int page, final ResponseCallBackListener<MarketProductsResponse> listener) {

        if (!NetworkUtils.isConnected(mContext)) {
            if (listener != null) {
                listener.onError(ErrorEvent.NETWORK_ERROR_CODE, "网络不可用");
            }
            return;
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("saleway", saleway);
        params.addBodyParameter("page", page + "");

        new HttpAsyncTask<MarketProductsResponse>(POST, PRODUCT_LIST, params, listener) {

            @Override
            public ResponseContent<MarketProductsResponse> parseNetworkResponse(String result) {
                Type type = new TypeToken<ResponseContent<MarketProductsResponse>>() {}.getType();
                return new Gson().fromJson(result, type);
            }

        }.execute();
    }

    /**
     * 我发布的茶市产品
     *
     */
    public void myProduct(final ResponseCallBackListener<List<Product>> listener) {

        if (!NetworkUtils.isConnected(mContext)) {
            if (listener != null) {
                listener.onError(ErrorEvent.NETWORK_ERROR_CODE, "网络不可用");
            }
            return;
        }

        if (TextUtils.isEmpty(mKey)) {
            if (listener != null) {
                listener.onError(ErrorEvent.SHOULD_LOGIN, "请登录");
            }
            return;
        }

        final String username = SessionKeeper.readUsername(mContext);
        if (TextUtils.isEmpty(username)) {
            if (listener != null) {
                listener.onError(ErrorEvent.SHOULD_LOGIN, "请登录");
            }
            return;
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("key", mKey);
        params.addBodyParameter("username", username);

        new HttpAsyncTask<List<Product>>(POST, MY_PRODUCT_LIST, params, listener) {

            @Override
            public ResponseContent<List<Product>> parseNetworkResponse(String result) {
                return new Gson().fromJson(result, new TypeToken<ResponseContent<List<Product>>>() {}.getType());
            }
        }.execute();

    }

    /**
     * 发布产品
     *
     */
    public void publishProduct(String id, String tab, String brand, String name, String year, String price, String quantity,
                               String address, String mobile, String contact, String detail,
                               SparseArray<String> uris, final ResponseCallBackListener<String> listener) {

        if (!NetworkUtils.isConnected(mContext)) {
            if (listener != null) {
                listener.onError(ErrorEvent.NETWORK_ERROR_CODE, "网络不可用");
            }
            return;
        }

        if (TextUtils.isEmpty(mKey)) {
            if (listener != null) {
                listener.onError(ErrorEvent.SHOULD_LOGIN, "请登录");
            }
            return;
        }

        if (TextUtils.isEmpty(brand)) {
            if (listener != null) {
                listener.onError(ErrorEvent.PARAM_NULL, "品牌不能为空");
            }
            return;
        }

        if (TextUtils.isEmpty(mobile)) {
            if (listener != null) {
                listener.onError(ErrorEvent.PARAM_NULL, "请输入手机号码");
            }
            return;
        }

        if (mobile.length() != 11) {
            if (listener != null) {
                listener.onError(ErrorEvent.PARAM_ILLEGAL, "手机号码格式不正确");
            }
            return;
        }

        RequestParams params = new RequestParams();
        if(!TextUtils.isEmpty(id)) params.addBodyParameter("id", id);
        // 登录令牌
        params.addBodyParameter("key", mKey);
        // 用户名如果不传将无法保存用户信息
        params.addBodyParameter("username", SessionKeeper.readUsername(mContext));
        // 出售或者求购
        params.addBodyParameter("saleway", tab);
        // 品牌
        params.addBodyParameter("brand", brand);
        // 品名
        params.addBodyParameter("name", name);
        // 年
        params.addBodyParameter("year", year);
        // 价格
        params.addBodyParameter("price", price);
        // 数量
        params.addBodyParameter("weight", quantity);
        // 地址
        params.addBodyParameter("address", address);
        // 电话
        params.addBodyParameter("phone", mobile);
        // 联系人
        params.addBodyParameter("contact", contact);
        // 详细
        params.addBodyParameter("content", detail);

        if (uris != null) {
            for (int i=1; i<=uris.size(); i++) {
                File file = UploadImageUtils.revisionPostImageSize(mContext, uris.valueAt(i - 1));
                if (file != null) params.addBodyParameter("img" + i, file);
            }
        }

        mHttpUtils.send(HttpRequest.HttpMethod.POST,
                PUBLISH_PRODUCT,
                params,
                new RequestCallBack<String>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        if (listener != null) listener.onSuccess(responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        if (listener != null) listener.onError(error.getExceptionCode(), msg);
                    }

                });
    }


    /**
     * 删除我发布的茶市产品
     *
     */
    public void deleteProduct(String id, final ResponseCallBackListener<String> listener) {

        if (!NetworkUtils.isConnected(mContext)) {
            if (listener != null) {
                listener.onError(ErrorEvent.NETWORK_ERROR_CODE, "网络不可用");
            }
            return;
        }

        if (TextUtils.isEmpty(id)) {
            if (listener != null) {
                listener.onError(ErrorEvent.PARAM_NULL, "产品不存在");
            }
            return;
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("id", id);

        mHttpUtils.send(POST, DELETE_PRODUCT, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (listener != null && responseInfo.result.equals("1")) listener.onSuccess(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (listener != null) listener.onError(e.getExceptionCode(), s);
            }
        });
    }

}
