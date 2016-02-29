package com.damenghai.chahuitong2.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.bean.event.CookieEvent;
import com.damenghai.chahuitong2.config.SessionKeeper;
import com.damenghai.chahuitong2.utils.NetworkUtils;
import com.damenghai.chahuitong2.utils.ShareManager;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.view.TopBar;
import com.damenghai.chahuitong2.view.TopBar.OnLeftClickListener;
import com.umeng.socialize.controller.UMSocialService;

import de.greenrobot.event.EventBus;

public class WebViewActivity extends BaseActivity {
	private String mUrl, mTitle;

    private String mSession;

    private CookieManager mCookieManager;

	private TopBar mTopBar;

	private ProgressBar mProgressBar;

	private WebView mWebView;

	private WebSettings mSettings;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		EventBus.getDefault().register(this);

		mUrl = getIntent().getStringExtra("url");

        if(!TextUtils.isEmpty(mUrl)) {
            mSession = SessionKeeper.readSession(this);
            mCookieManager = CookieManager.getInstance();
        }

		bindView();

		initView();
	}

	@Override
	protected void bindView() {
		mTopBar = (TopBar) findViewById(R.id.topBar);
		mProgressBar = (ProgressBar) findViewById(R.id.loading);
		mWebView = (WebView) findViewById(R.id.new_webview);
	}

	@SuppressLint("SetJavaScriptEnabled")
	protected void initView() {

		mTopBar.setOnLeftClickListener(new OnLeftClickListener() {
			@Override
			public void onLeftClick() {
                if(mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
			}
		});

		if(mUrl.contains("goods")) {
			mTopBar.setRightSrc(R.drawable.icon_share);// 设置分享内容
			final UMSocialService controller = ShareManager.create(WebViewActivity.this);
			ShareManager.setShareContent(WebViewActivity.this, "", mUrl, mTitle, "");

			mTopBar.setOnRightClickListener(new TopBar.onRightClickListener() {
				@Override
				public void onRightClick() {
					if(mTitle != null) {
						controller.setShareContent(mTitle + "，" + mUrl);
					}
					// 是否只有已登录用户才能打开分享选择页
					controller.openShare(WebViewActivity.this, false);
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

        if(mUrl.contains("yinlian")) {
            mTopBar.setVisibility(View.GONE);
        }

		mSettings = mWebView.getSettings();
		mSettings.setJavaScriptEnabled(true);
		mSettings.setBlockNetworkImage(true);
        mSettings.setDomStorageEnabled(true);

        if(NetworkUtils.isConnected(WebViewActivity.this)) {
            mSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            mSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

		//往浏览器标识字符串里添加自定义的字符串，用于服务器判断是否为客户端
		if (!mSettings.getUserAgentString().endsWith("/android_client")) {
			mSettings.setUserAgentString(mSettings.getUserAgentString() + "/android_client");
		}

		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);

        if(!TextUtils.isEmpty(mSession)) {
            String key = getCookieValue("key");
            if(!key.equals(mSession)) {
                setCookie();
            }
        } else {
            if(!TextUtils.isEmpty(getCookieValue("key"))) {
                removeCookie();
            }
        }

		mWebView.loadUrl(mUrl);
		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.setWebChromeClient(new MyWebChromeClient());
	}

	private void setCookie() {
        mCookieManager.setCookie(mUrl, "key=" + (TextUtils.isEmpty(mSession) ? SessionKeeper.readSession(this) : mSession));
	}

    private void removeCookie() {
        mCookieManager.removeAllCookie();
    }

    /**
     * 通过key查找value值
     *
     * @param key
     * @return
     */
	private String getCookieValue(String key) {
		if(!TextUtils.isEmpty(key)) {
			String cookie = mCookieManager.getCookie(mUrl);
			if(!TextUtils.isEmpty(cookie)) {
				String[] cookies = cookie.split(";");
				for(String set : cookies) {
					String[] values = set.split("=");
					if(key.equals(values[0]) && values.length > 1) return set.split("=")[1];
				}
			}
		}
		return "";
	}

    // 用户登录后回调
    public void onEventBackgroundThread(CookieEvent event){
        setCookie();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(mUrl.contains("95516")) {
                finishActivity();
            } else if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finishActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.endsWith("login")) {
                openActivity(LoginActivity.class);
                return true;
            }
            else if (url.contains("buynum")) {
                if(mUrl.contains("goods_id")) {
                    String id = mUrl.substring(mUrl.lastIndexOf("=") + 1);
                    Bundle bundle = new Bundle();
                    bundle.putString("goods_id", id);
                    openActivity(OrderActivity.class, bundle);
                } else {
                    openActivity(OrderActivity.class);
                }
                return true;
            }
            else if(url.contains("ifcart=1")) {
                String cartId = url.substring(url.lastIndexOf("=") + 1);
                Bundle bundle = new Bundle();
                bundle.putString("cart_id", cartId);
                openActivity(OrderActivity.class, bundle);
                return true;
            }
            else if(url.contains("tel:0592-5990900")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:05925990900"));
                startActivity(intent);
                return true;
            } else if(url.contains("114.215.108.10:30001") || url.contains("95516")) {
                view.loadUrl(url);
                mUrl = url;
                return super.shouldOverrideUrlLoading(view, url);
            } else if (url.contains("index.php/Home/Index/orderList")) {
                openActivity(OrderListActivity.class);
                finishActivity();
                return true;
            }
            else {
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                openActivity(WebViewActivity.class, bundle);
                return true;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mSettings.setBlockNetworkImage(false);
            mProgressBar.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);

            CookieManager manager = CookieManager.getInstance();
            if(!TextUtils.isEmpty(SessionKeeper.readSession(WebViewActivity.this))) {
                setCookie();
            } else if(!TextUtils.isEmpty(getCookieValue("key"))) {
                manager.removeAllCookie();
            }
        }

    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            T.showShort(WebViewActivity.this, message);
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            mTitle = title;
            mTopBar.setTitle(title);
        }
    }

}
