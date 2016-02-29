package com.damenghai.chahuitong2.api;

import android.content.Context;

import com.damenghai.chahuitong2.config.SessionKeeper;
import com.damenghai.chahuitong2.request.VolleyRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sgun on 15/9/22.
 */
public class PersonalAPI {

    /**
     * 个人中心用户信息
     *
     * @param key
     * @param l
     */
    public static void userInfo(String key, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", key);
        HodorRequest.postRequest("http://www.chahuitong.com/mobile/index.php?act=member_index", map, l);
    }

    /**
     * 获取我的地址列表
     *
     * @param context
     * @param listener
     */
    public static void addressShow(Context context, VolleyRequest listener) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        HodorRequest.postRequest("http://www.chahuitong.com/mobile/index.php?act=member_address&op=address_list", map, listener);
    }

    /**
     * 增加新的地址
     *
     * @param context
     * @param l
     */
    public static void addressAdd(Context context, String name, String mobile, String cityId,
                                  String areaId, String area_info, String address, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("true_name", name);
        map.put("mob_phone", mobile);
        map.put("city_id", cityId);
        map.put("area_id", areaId);
        map.put("area_info", area_info);
        map.put("address", address);
        HodorRequest.postRequest("http://www.chahuitong.com/mobile/index.php?act=member_address&op=address_add", map, l);
    }

    /**
     * 编辑地址
     *
     * @param context
     * @param l
     */
    public static void addressEdit(Context context, String addressId, String name, String mobile, String cityId,
                                  String areaId, String area_info, String address, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("address_id", addressId);
        map.put("true_name", name);
        map.put("mob_phone", mobile);
        map.put("city_id", cityId);
        map.put("area_id", areaId);
        map.put("area_info", area_info);
        map.put("address", address);
        HodorRequest.postRequest("http://www.chahuitong.com/mobile/index.php?act=member_address&op=address_edit", map, l);
    }

    /**
     * 删除地址
     *
     * @param context
     * @param l
     */
    public static void addressDelete(Context context, String addressId, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("address_id", addressId);
        HodorRequest.postRequest("http://www.chahuitong.com/mobile/index.php?act=member_address&op=address_del", map, l);
    }

    /**
     * 设置默认地址
     *
     * @param context
     * @param addressId
     * @param l
     */
    public static void addressSetDefault(Context context, String addressId, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("id", addressId);
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Index/changeaddress", map, l);
    }

    /**
     * 优惠券列表
     *
     * @param context
     *                  上下文
     * @param state
     *                  四种类型：1未使用，2已使用，3已过期，被回收
     * @param l
     *                  回调函数
     */
    public static void voucherShow(Context context, String state, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        map.put("voucher_state", state);
        HodorRequest.postRequest("http://www.chahuitong.com/wap/index.php/Home/Index/get_voucher_api/", map, l);
    }

    public static void cartList(Context context, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", SessionKeeper.readSession(context));
        HodorRequest.postRequest("http://www.chahuitong.com/mobile/index.php?act=member_cart&op=cart_list", map, l);
    }

}
