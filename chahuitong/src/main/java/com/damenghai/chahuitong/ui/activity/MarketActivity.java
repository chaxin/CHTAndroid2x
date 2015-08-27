package com.damenghai.chahuitong.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.damenghai.chahuitong.AppManager;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.MarketFragmentAdapter;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

public class MarketActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private MarketFragmentAdapter mAdapter;
	private TabPageIndicator mIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getInstance().addActivity(this);
		setContentView(R.layout.activity_market);

		findViewById();

		initView();
	}

	private void findViewById() {
		mViewPager = (ViewPager) findViewById(R.id.id_vp);
		mIndicator = (TabPageIndicator) findViewById(R.id.title_indicator);
	}

	private void initView() {
		mAdapter = new MarketFragmentAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mViewPager);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
