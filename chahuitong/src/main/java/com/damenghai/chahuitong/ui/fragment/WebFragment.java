package com.damenghai.chahuitong.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.config.Constants;
import com.damenghai.chahuitong.ui.activity.MainActivity;
import com.damenghai.chahuitong.view.NewWebView;

import de.greenrobot.event.EventBus;

public class WebFragment extends Fragment {
	private final String TAG = getClass().getSimpleName();

	/**
	 * 茶里茶外地址
	 */
	public static final String BBS_URL = "http://xiaoqu.qq.com/mobile/barindex.html?bid=197979&_wv=1027&from=wsqjump";

	/**
	 * UA标识字符
	 */
	private final String UA_STRING = "/android_client";

	private static final int LOGIN_IN_REQUEST_CODE = 0;

	private String mUrl;
	private String mCookie;

	private SharedPreferences mSp;

	private NewWebView mWebView;

	public WebFragment() {
		super();
	}

	public static WebFragment get(String url) {
		WebFragment fragment = new WebFragment();
		Bundle bundle = new Bundle();
		bundle.putString("url", url);
		fragment.setArguments(bundle);
		return fragment;
	}

	public String getCookie() {
		if(mCookie != null && !mCookie.equals("")) {
			return mCookie;
		}
		CookieManager cm = CookieManager.getInstance();
		String cookie = cm.getCookie(mUrl);
		if(cookie != null && !cookie.equals("")) {
			mCookie = cookie;
			return cookie;
		}
		return null;
	}

	public void setCookie(String key, String value) {
		String ck = getCookie();

		if(ck != null && ck.contains(key)) {
			return;
		}
		else {
			CookieManager cm = CookieManager.getInstance();
			cm.setCookie(mUrl, key + "=" + value);
		}
	}

	public void removeCookie() {
		CookieManager cm = CookieManager.getInstance();
		String cookie = cm.getCookie(mUrl);

		if(cookie != null) {
			cm.removeAllCookie();
		}
	}

//	public void reload() {
//		if(mWebView != null) {
//			mWebView.reload();
//		}
//	}
//
//	public boolean goBack() {
//		if(mWebView.canGoBack()) {
//			mWebView.goBack();
//			return true;
//		}
//		return false;
//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if(bundle != null) {
			mUrl = bundle.getString("url");
		}

		EventBus.getDefault().register(this);

		mSp = getActivity().getSharedPreferences(Constants.SHARED_PREFERERENCE_NAME,
				Context.MODE_PRIVATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_fragment, container, false);
		mWebView = (NewWebView) view.findViewById(R.id.main_fragment_webview);

		if (mUrl.contains("wsq.qq.com")) {
			mUrl = BBS_URL;
		}
		mWebView.loadUrl(mUrl);

		return view;
	}

	// 登录注册页的回调
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == LOGIN_IN_REQUEST_CODE) {
			if(resultCode == Activity.RESULT_CANCELED) {
				if(getActivity() instanceof MainActivity) {
					MainActivity activity = (MainActivity) getActivity();
					activity.setCurrentItem(MainActivity.ITEM_MAIN);
					activity.removeFragment(R.id.home_tab_cart);
					activity.removeFragment(R.id.home_tab_member);
				}
			}
		}
	}

	// This method will be called after login success.
	public void onEventMainThread(String key) {
		setCookie("key", key);
//		reload();
	}

}
