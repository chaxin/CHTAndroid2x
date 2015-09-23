package com.damenghai.chahuitong.api;

import android.content.Context;

import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.response.IResponseListener;
import com.damenghai.chahuitong.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sgun on 15/9/22.
 */
public class PaymentAPI {
    public static void getOrders(Context context, VolleyRequest l) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("total", "0.1");
        HodorRequest.postRequest("http://www.chahuitong.com/mobile/api/payment/yinlian/AppConsume.php", map, l);
    }
}
