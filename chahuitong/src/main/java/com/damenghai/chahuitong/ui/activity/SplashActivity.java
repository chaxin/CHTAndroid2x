package com.damenghai.chahuitong.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.damenghai.chahuitong.BaseActivity;
import com.damenghai.chahuitong.config.Constants;

public class SplashActivity extends BaseActivity {

	boolean isFirstIn = false;
	
//    private static final int GO_HOME = 1000;
//    private static final int GO_GUIDE = 1001;
//    // 延迟3秒
//    private static final long SPLASH_DELAY_MILLIS = 300;

//    /**
//     * Handler:跳转到不同界面
//     */
//    private Handler mHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//            case GO_HOME:
//                goHome();
//                break;
//            case GO_GUIDE:
//                goGuide();
//                break;
//            }
//            super.handleMessage(msg);
//        }
//    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		init();
	}

	private void init() {		
		SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERERENCE_NAME, MODE_PRIVATE);
        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        isFirstIn = preferences.getBoolean("isFirstIn", true);
        // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
        if (!isFirstIn) {
        	openActivity(HomeActivity.class);
        	//mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        } else {
            openActivity(GuideActivity.class);
            //mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
        }
        finish();
	}

	private void goHome() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        SplashActivity.this.startActivity(intent);
        finish();
    }

    private void goGuide() {
        Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
        SplashActivity.this.startActivity(intent);
        finish();
    }
	
}
