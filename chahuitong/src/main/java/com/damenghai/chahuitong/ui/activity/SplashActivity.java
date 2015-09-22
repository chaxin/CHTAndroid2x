package com.damenghai.chahuitong.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.config.Constants;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.update.UmengUpdateAgent;

public class SplashActivity extends BaseActivity {
	boolean isFirstIn = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        // 设置任意网络环境下都提示更新
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        // 友盟自动检测更新
        UmengUpdateAgent.update(this);

        // bugly初始化
//        CrashReport.initCrashReport(getApplicationContext(), "101172467", false);

		init();
	}

	@Override
	protected void findViewById() {

	}

	@Override
	protected void initView() {

	}

	private void init() {		
		SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERERENCE_NAME, MODE_PRIVATE);
        isFirstIn = preferences.getBoolean("isFirstIn", true);
        if (!isFirstIn) {
        	openActivity(HomeActivity.class);
        } else {
            openActivity(GuideActivity.class);
        }
        finish();
	}
	
}
