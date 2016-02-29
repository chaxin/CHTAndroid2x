package com.damenghai.chahuitong2.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.config.SessionKeeper;
import com.damenghai.chahuitong2.ui.activity.LoginActivity;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class UserApi implements Api {
    protected Context mContext;

    protected String mKey;

    protected String mUsername;

    public UserApi(Context context) {
        mContext = context;
        mKey = SessionKeeper.readSession(context);
        mUsername = SessionKeeper.readUsername(context);

        if (TextUtils.isEmpty(mKey)) startLogin();
    }

    private void startLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

}
