package com.damenghai.chahuitong.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.config.Config;
import com.damenghai.chahuitong.config.Constants;
import com.pgyersdk.update.PgyUpdateManager;

import cn.bmob.im.BmobChat;

public class SplashActivity extends BaseActivity {
	boolean isFirstIn = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

//        // 设置任意网络环境下都提示更新
//        UmengUpdateAgent.setUpdateOnlyWifi(false);
//        // 友盟自动检测更新
//        UmengUpdateAgent.update(this);

		// 蒲公英内测更新
		PgyUpdateManager.register(this);

        // bugly初始化
//        CrashReport.initCrashReport(getApplicationContext(), "101172467", false);

		//可设置调试模式，当为true的时候，会在logcat的BmobChat下输出一些日志，包括推送服务是否正常运行，如果服务端返回错误，也会一并打印出来。方便开发者调试，正式发布应注释此句。
		BmobChat.DEBUG_MODE = true;
		//BmobIM SDK初始化--只需要这一段代码即可完成初始化
		BmobChat.getInstance(this).init(Config.applicationId);

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
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFirstIn) {
                    openActivity(HomeActivity.class);
                } else {
                    openActivity(GuideActivity.class);
                }
                finish();
            }
        }, 1000);

	}
	
}
