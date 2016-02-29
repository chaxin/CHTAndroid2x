package com.damenghai.chahuitong2.response;

import android.app.Dialog;
import android.content.Context;

import com.damenghai.chahuitong2.utils.T;

/**
 * Created by Sgun on 15/9/19.
 */
public class StringListener implements IResponseListener {
    private Context mContext;
    private Dialog mDialog;

    public StringListener(Context context) {
        this.mContext = context;
    }

    public StringListener(Context context, Dialog dialog) {
        mContext = context;
        mDialog = dialog;
    }

    @Override
    public void onSuccess(String response) {

    }

    @Override
    public void onError(String error) {
        T.showShort(mContext, error);
    }

    @Override
    public void onAllDone() {
        if(mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

}
