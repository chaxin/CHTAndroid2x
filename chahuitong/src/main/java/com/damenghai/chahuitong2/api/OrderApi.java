package com.damenghai.chahuitong2.api;

import android.content.Context;
import android.text.TextUtils;

import com.damenghai.chahuitong2.config.SessionKeeper;
import com.damenghai.chahuitong2.request.VolleyRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class OrderApi extends UserApi implements Api {

    /** 购买第一步 */
    private static final String BUY_STEP_FIRST = BASE_URL + "act=member_buy&op=buy_step1";

    /** 购买第二步 */
    private static final String BUY_STEP_SECOND = BASE_URL + "act=member_buy&op=buy_step2";

    /** 购买时更换收货地址 */
    private static final String BUY_CHANGE_ADDRESS = BASE_URL + "act=member_buy&op=change_address";

    /** 订单列表是否获取支付方式 */
    private static final String ORDER_LIST_GET_PAYMENT = "&getpayment=true";

    /** 订单列表每页显示数量 */
    private static final String ORDER_LIST_PAGE_SIZE = "&page=20";

    /** 订单列表 */
    private static final String ORDER_LIST = BASE_URL + "act=member_order&op=order_list" + ORDER_LIST_GET_PAYMENT + ORDER_LIST_PAGE_SIZE;

    /** 取消订单 */
    private static final String ORDER_CANCEL = BASE_URL + "act=member_order&op=order_cancel";

    /** 取消订单 */
    private static final String ORDER_RECEIVE = BASE_URL + "act=member_order&op=order_receive";

    public OrderApi(Context context) {
        super(context);
    }

    public static void getOrderInfo(Context context, String cartId, String ifCart, VolleyRequest l) {
        Map<String, String> map = new HashMap<>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("cart_id", cartId);
        if(!TextUtils.isEmpty(ifCart)) map.put("ifcart", ifCart);
        HodorRequest.postRequest(BUY_STEP_FIRST, map, l);
//
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("key", mKey);
//        params.addBodyParameter("cart_id", cartId);
//        if(!TextUtils.isEmpty(ifCart)) params.addBodyParameter("ifcart", ifCart);
//
//        new HttpAsyncTask<>()
    }

    /**
     * 生成支付订单
     *
     * @param context
     *                  上下文
     * @param ifCart
     *                  是否为购物车
     * @param cartId
     *                  支付产品ID
     * @param addressId
     *                  收货地址
     * @param vatHash
     *                  不知道什么作用
     * @param freightHash
     *                  运费哈西码
     * @param offPayHash
     *                  货到付款哈西码
     * @param offPayHashBatch
     *                  货到付款哈西码
     * @param l
     *                  监听
     */
    public static void getPaySn(Context context, String ifCart, String cartId, String addressId, String vatHash,
                                String freightHash, String offPayHash, String offPayHashBatch, VolleyRequest l) {
        Map<String, String> map = new HashMap<>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("cart_id", cartId);
        map.put("address_id", addressId);
        map.put("vat_hash", vatHash);
        map.put("freight_hash", freightHash);
        map.put("offpay_hash", offPayHash);
        map.put("offpay_hash_batch", offPayHashBatch);
        map.put("pay_name", "online");
        map.put("ifcart", ifCart);
        map.put("allow_offpay", "0");
        map.put("invoice_id", "");
        HodorRequest.postRequest(BUY_STEP_SECOND, map, l);
    }

//    public void generateOrder(String ifCart, String cartId, String addressId, String vatHash,
//                              String freightHash, String offPayHash, String offPayHashBatch, final ResponseCallBackListener<Order> listener) {
//        if(!NetworkUtils.isConnected(mContext) && listener != null) {
//            listener.onError(ErrorEvent.NETWORK_ERROR_CODE, ErrorEvent.NETWORK_ERROR_MSG);
//            return;
//        }
//
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("key", mKey);
//        params.addBodyParameter("cart_id", cartId);
//        params.addBodyParameter("address_id", addressId);
//        params.addBodyParameter("vat_hash", vatHash);
//        params.addBodyParameter("freight_hash", freightHash);
//        params.addBodyParameter("offpay_hash", offPayHash);
//        params.addBodyParameter("offpay_hash_batch", offPayHashBatch);
//        params.addBodyParameter("pay_name", "online");
//        params.addBodyParameter("ifcart", ifCart);
//        params.addBodyParameter("allow_offpay", "0");
//        params.addBodyParameter("invoice_id", "");
//
//        new HttpAsyncTask<Order>(POST, BUY_STEP_TWO, params, listener) {
//
//            @Override
//            public Order parseNetworkResponse(String result) {
//                try {
//                    JSONObject object = new JSONObject(result);
//                    if (!object.has("error")) {
//                        return new Gson().fromJson(object.getJSONObject("order").toString(), Order.class);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//        }.execute();
//    }

    /**
     * 通过地址获取生成订单所需要的offpay_hash值
     *
     * @param context
     * @param cityId
     * @param areaId
     * @param freightHash
     * @param l
     */
    public static void changeAddress(Context context, String cityId, String areaId, String freightHash, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("city_id", cityId);
        map.put("area_id", areaId);
        map.put("freight_hash", freightHash);
        HodorRequest.postRequest(BUY_CHANGE_ADDRESS, map, l);
    }

    /**
     * 我的订单列表
     *
     * @param context
     *                      用于获取key
     * @param orderState
     *                      订单状态。10-待支付 20-待发货 30-待收货 40待评价
     * @param curPage
     *                      当前页
     * @param l
     *                      监听
     */
    public static void orderList(Context context, String orderState, String curPage, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("order_state", orderState);
        HodorRequest.postRequest(ORDER_LIST + "&curpage=" + curPage, map, l);
    }

    /**
     * 取消订单
     *
     * @param context
     *                      用于获取key
     * @param orderId
     *                      要取消的订单ID
     * @param l
     *                      监听
     */
    public static void cancelOrder(Context context, String orderId, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("order_id", orderId);
        HodorRequest.postRequest(ORDER_CANCEL, map, l);
    }

    /**
     * 订单确认收货
     * @param context
     * @param orderId
     * @param l
     */
    public static void sureOrder(Context context, String orderId, VolleyRequest l) {
        Map<String, String> map = new HashMap<>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("order_id", orderId);
        HodorRequest.postRequest(ORDER_RECEIVE, map, l);
    }

}
