package com.damenghai.chahuitong2.api;

import android.content.Context;

import com.damenghai.chahuitong2.bean.Area;
import com.damenghai.chahuitong2.bean.response.ResponseContent;
import com.damenghai.chahuitong2.response.ResponseCallBackListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class AddressApi extends UserApi {

    /** 地区列表 */
    private static final String ADDRESS_AREA_LIST = BASE_URL + "act=member_address&op=area_list";

    public AddressApi(Context context) {
        super(context);
    }

    /**
     * 获取省市级
     *
     * @param areaId
     *                  通过ID查找
     * @param listener
     *                  监听
     */
    public void areaList(String areaId, ResponseCallBackListener<List<Area>> listener) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("key", mKey);
        params.addBodyParameter("area_id", areaId);
        new HttpAsyncTask<List<Area>>(POST, ADDRESS_AREA_LIST, params, listener) {

            @Override
            public ResponseContent<List<Area>> parseNetworkResponse(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.length() > 0) {
                        ResponseContent<List<Area>> response = new ResponseContent<List<Area>>();

                        JSONObject datas = jsonObject.getJSONObject("datas");
                        if (!datas.has("error")) {
                            Type type = new TypeToken<List<Area>>() {}.getType();
                            List<Area> content = new Gson().fromJson(datas.getJSONArray("area_list").toString(), type);
                            response.setContent(content);
                            response.setCode(200);
                        } else {
                            response.setCode(404);
                            response.setMsg(datas.getString("error"));
                        }

                        return response;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

        }.execute();

    }

}
