package com.damenghai.chahuitong2.response;

import android.app.Dialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Sgun on 15/9/19.
 */
public abstract class JsonArrayListener extends StringListener {
    public JsonArrayListener(Context context) {
        super(context);
    }

    public JsonArrayListener(Context context, Dialog dialog) {
        super(context, dialog);
    }

    @Override
    public void onSuccess(String response) {
        try {
            JSONArray array = new JSONArray(response);
            onSuccess(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public abstract void onSuccess(JSONArray array);
}
