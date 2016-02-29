package com.damenghai.chahuitong2.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.config.Constants;

public class SplashActivity extends BaseActivity {
	boolean isFirstIn = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

        // bugly初始化
//        CrashReport.initCrashReport(getApplicationContext(), "101172467", false);

		init();
	}

	private void init() {
		SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERERENCE_NAME, MODE_PRIVATE);
		isFirstIn = preferences.getBoolean("isFirstIn", true);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
//				if (!isFirstIn) {
					openActivity(HomeActivity.class);
//				} else {
//					openActivity(GuideActivity.class);
//				}
				finish();
			}
		}, 2000);

	}

	@Override
	protected void bindView() {

	}

	@Override
	protected void initView() {

	}

}
