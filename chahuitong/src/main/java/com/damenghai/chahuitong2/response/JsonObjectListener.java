package com.damenghai.chahuitong2.response;

import android.app.Dialog;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sgun on 15/9/19.
 */
public abstract class JsonObjectListener extends StringListener {
    public JsonObjectListener(Context context) {
        super(context);
    }

    public JsonObjectListener(Context context, Dialog dialog) {
        super(context, dialog);
    }

    @Override
    public void onSuccess(String response) {
        try {
            JSONObject object = new JSONObject(response);
            onSuccess(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public abstract void onSuccess(JSONObject object);

}
