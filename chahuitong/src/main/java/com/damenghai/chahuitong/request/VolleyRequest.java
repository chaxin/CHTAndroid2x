package com.damenghai.chahuitong.request;

import android.app.Dialog;
import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.utils.T;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sgun on 15/8/13.
 */
public class VolleyRequest {
    private Dialog mDialog;

    public VolleyRequest() {
    }

    public VolleyRequest(Dialog mDialog) {
        this.mDialog = mDialog;
    }

    public void onSuccess() {
        onAllDone();
    }

    public void onSuccess(String response) {
        onAllDone();
    }

    public void onListSuccess(JSONArray array) {
        onAllDone();
    }

    public void onError(String error) {
        onAllDone();
    }

//    public void onError(VolleyError error) {
//        byte[] htmlBodyBytes = error.networkResponse.data;
//        L.d(new String(htmlBodyBytes));
//    }

    public void onAllDone() {
        if(mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

}
