package com.damenghai.chahuitong.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.view.NewWebView;
import com.damenghai.chahuitong.view.TopBar;
import com.damenghai.chahuitong.view.TopBar.OnLeftClickListener;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class WebViewActivity extends BaseActivity implements NewWebView.OnReceivedTitle {
	private String mUrl, mTitle;
	private TopBar mTopBar;
	private NewWebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);

		findViewById();
		initView();
		initUmengShare();
	}

	private void checkLogin() {
		String key = SessionKeeper.readSession(this);
		if(key != null && !key.equals("")) {
			mWebView.setCookie("key", key);
		} else {
            mWebView.removeCookie();
        }
	}

	@Override
	protected void findViewById() {
		mTopBar = (TopBar) findViewById(R.id.topBar);
		mWebView = (NewWebView) findViewById(R.id.new_webview);
	}

	@Override
	protected void initView() {
		Intent intent = getIntent();
		Bundle extra = intent.getExtras();
		if(extra != null) {
			mUrl = extra.getString("url");
		}

		mWebView.loadUrl(mUrl);
		mWebView.setOnReceivedTitle(this);
        checkLogin();

		mTopBar.setOnLeftClickListener(new OnLeftClickListener() {
			@Override
			public void onLeftClick() {
				finishActivity();
			}
		});

		if(mUrl.contains("goods")) {
			mTopBar.setRightSrc(R.drawable.icon_share);// 设置分享内容

			mTopBar.setOnRightClickListener(new TopBar.onRightClickListener() {
				@Override
				public void onRightClick() {
					if(mTitle != null) {
						mController.setShareContent(mTitle + "，" + mUrl);
					}
					// 是否只有已登录用户才能打开分享选择页
					mController.openShare(WebViewActivity.this, false);
				}
			});
		} else {
			mTopBar.setOnRightClickListener(new TopBar.onRightClickListener() {
				@Override
				public void onRightClick() {
					Intent intent = new Intent(WebViewActivity.this, HomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
			});
		}

	}

	@Override
	public void receivedTitle(String title) {
		if(title != null && !title.trim().equals("")) {
			mTitle = title;
			mTopBar.setTitle(title);
		}
	}

}
