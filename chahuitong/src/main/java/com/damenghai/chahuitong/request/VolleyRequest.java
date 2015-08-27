package com.damenghai.chahuitong.request;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sgun on 15/8/13.
 */
public class VolleyRequest {
    public void onSuccess(String response) {
        onAllDone();
    }

    public void onError(String error) {
        onAllDone();
    }

    public void onAllDone() {}

}
