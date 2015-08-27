package com.damenghai.chahuitong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.damenghai.chahuitong.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.view.Navigation;
import com.damenghai.chahuitong.view.NewWebView;
import com.damenghai.chahuitong.view.TopBar;
import com.damenghai.chahuitong.view.TopBar.OnLeftClickListener;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class ContentActivity extends BaseActivity implements NewWebView.OnReceivedTitle {
	private String mUrl, mTitle;
	private TopBar mTopBar;
	private NewWebView mWebView;
	private Navigation mNavigation;

	// 友盟分享成员变量
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	
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

	private void findViewById() {
		mTopBar = (TopBar) findViewById(R.id.topBar);
		mWebView = (NewWebView) findViewById(R.id.new_webview);
		mNavigation = (Navigation) findViewById(R.id.web_navigation);
	}

	private void initView() {
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
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
				finish();
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
					mController.openShare(ContentActivity.this, false);
				}
			});
		} else {
			mTopBar.setOnRightClickListener(new TopBar.onRightClickListener() {
				@Override
				public void onRightClick() {
					Intent intent = new Intent(ContentActivity.this, HomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
			});
		}

	}

	public void initUmengShare() {
		String appID = "wx58ea4f88c26aa4b0";
		String appSecret = "1999b86ded76a858588083ac46615b8d";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this,appID,appSecret);
		wxHandler.addToSocialSDK();
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this,appID,appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// 分享给qq好友，参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "100424468",
				"c7394704798a158208a74ab60104f0ba");
		qqSsoHandler.addToSocialSDK();

		//分享到qq空间，参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "100424468",
				"c7394704798a158208a74ab60104f0ba");
		qZoneSsoHandler.addToSocialSDK();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/**使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
		if(ssoHandler != null){
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		
		if(resultCode == LoginActivity.LOGIN_RESULT_CODE) {
			mWebView.setCookie("key", SessionKeeper.readSession(this));
			mWebView.setCookie("username", SessionKeeper.readUsername(this));
		} else if(resultCode == LoginActivity.LOGOUT_RESULT_CODE) {
			SessionKeeper.clearSession(ContentActivity.this);
			mWebView.removeCookie();
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
