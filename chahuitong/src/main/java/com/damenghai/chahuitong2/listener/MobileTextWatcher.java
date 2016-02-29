package com.damenghai.chahuitong2.listener;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.api.AccountApi;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.utils.T;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sgun on 15/10/13.
 */
public class MobileTextWatcher implements TextWatcher {
    private Context mContext;
    private EditText mMobile;
    private Button mBtSend;

    public MobileTextWatcher(Context context, EditText mobile, Button btSend) {
        mContext = context;
        mMobile = mobile;
        mBtSend = btSend;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String mobile = mMobile.getText().toString();
        if(mobile.length() == 11) {
            AccountApi.isRegister(mobile, new VolleyRequest() {
                @Override
                public void onSuccess(String response) {
                    super.onSuccess(response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getInt("code") == 200) {
                            mBtSend.setBackgroundResource(R.drawable.draw_primary2dark_sel);
                            mBtSend.setEnabled(true);
                        } else {
                            T.showShort(mContext, R.string.toast_not_registered);
                            mBtSend.setBackgroundResource(R.color.primary_light);
                            mBtSend.setEnabled(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            });
        } else {
            mBtSend.setBackgroundResource(R.color.primary_light);
            mBtSend.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
