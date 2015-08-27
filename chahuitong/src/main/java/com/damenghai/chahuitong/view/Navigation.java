package com.damenghai.chahuitong.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.ui.activity.HomeActivity;
import com.damenghai.chahuitong.ui.activity.PersonalActivity;
import com.damenghai.chahuitong.utils.T;

/**
 *
 * 左下角返回、首页、个人中心的导航
 *
 * Created by Sgun on 15/8/12.
 */
public class Navigation extends LinearLayout implements View.OnClickListener {
    private Activity mActivity;

    private View mView;
    private ImageView mIvBack, mIvHome, mIvPsn;

    public Navigation(Context context) {
        this(context, null);
    }

    public Navigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(isInEditMode()) return;
        mActivity = (Activity) getContext();

        mView = LayoutInflater.from(context).inflate(R.layout.navigation_layout, this);
        findViewById();
        initView();
    }

    private void initView() {
        mIvBack.setOnClickListener(this);
        mIvHome.setOnClickListener(this);
        mIvPsn.setOnClickListener(this);
    }

    private void findViewById() {
        mIvBack = (ImageView) mView.findViewById(R.id.navigation_back);
        mIvHome = (ImageView) mView.findViewById(R.id.navigation_home);
        mIvPsn = (ImageView) mView.findViewById(R.id.navigation_personal);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navigation_back :
                mActivity.finish();
                break;
            case R.id.navigation_home :
                Intent intent = new Intent(mActivity, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivity(intent);
                break;
            case R.id.navigation_personal :
                Intent personal = new Intent(mActivity, PersonalActivity.class);
                mActivity.startActivity(personal);
                break;
        }
    }
}
