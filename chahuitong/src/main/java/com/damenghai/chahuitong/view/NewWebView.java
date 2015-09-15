package com.damenghai.chahuitong.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.config.Constants;
import com.damenghai.chahuitong.ui.activity.WebViewActivity;
import com.damenghai.chahuitong.ui.activity.LoginActivity;
import com.damenghai.chahuitong.ui.activity.MarketActivity;
import com.damenghai.chahuitong.utils.T;


/**
 * Created by Sgun on 15/8/7.
 */
public class NewWebView extends RelativeLayout {
    /**
     * UA标识字符
     */
    private final String UA_STRING = "/android_client";

    public static final int LOGIN_IN_REQUEST_CODE = 0;

    private String mUrl;
    private String mKey;

    private View mView;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private WebSettings mSettings;

    private OnReceivedTitle l;

    public NewWebView(Context context) {
        this(context, null);
    }

    public NewWebView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        mView = LayoutInflater.from(context).inflate(R.layout.custom_webview, this);
        mWebView = (WebView) mView.findViewById(R.id.web_webview);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.web_pb);

        if(isInEditMode()) return;

        mSettings = mWebView.getSettings();
        mSettings.setJavaScriptEnabled(true);
        mSettings.setBlockNetworkImage(true);
        mSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //往浏览器标识字符串里添加自定义的字符串，用于服务器判断是否为客户端
        if (!mSettings.getUserAgentString().endsWith(UA_STRING)) {
            mSettings.setUserAgentString(mSettings.getUserAgentString()
                    + UA_STRING);
        }
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(Constants.MARKET_URL)) {
                    Intent intent = new Intent(context, MarketActivity.class);
                    context.startActivity(intent);
                    return true;
                } else if (url.contains("login")) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    ((Activity) context).startActivityForResult(intent, LOGIN_IN_REQUEST_CODE);
                    return true;
                } else if(url.contains("back")) {
                    T.showShort(context, "kkkk");
                }
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", url);
                context.startActivity(intent);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mSettings.setBlockNetworkImage(false);
                mProgressBar.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (l != null) {
                    l.receivedTitle(title);
                }
            }
        });

    }

    public void loadUrl(String url) {
        if(null != url) {
            mWebView.loadUrl(url);
            mUrl = url;
        }
    }

    public void removeCookie() {
        CookieManager cm = CookieManager.getInstance();
        String cookie = cm.getCookie(mUrl);
        if(cookie != null && cookie.contains("key")) {
            cm.removeAllCookie();
        }
    }

    public void setCookie(String key, String value) {
        if(key == null || value == null || key.trim().equals("") || value.trim().equals("")) return;
        CookieManager cm = CookieManager.getInstance();
        cm.setCookie(mUrl, key + "=" +value);
        mWebView.reload();
    }

    /**
     * 获取当前WebView的Key值
     *
     */
    public void getKey() {
        CookieManager cm = CookieManager.getInstance();
        cm.getCookie(mUrl);
    }

    public interface OnReceivedTitle {
        void receivedTitle(String title);
    }

    public void setOnReceivedTitle(OnReceivedTitle l) {
        this.l = l;
    }

}
