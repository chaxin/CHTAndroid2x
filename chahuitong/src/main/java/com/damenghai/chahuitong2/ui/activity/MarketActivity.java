package com.damenghai.chahuitong2.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.MarketFragmentAdapter;
import com.damenghai.chahuitong2.api.TeaMarketApi;
import com.damenghai.chahuitong2.base.BaseFragmentActivity;
import com.damenghai.chahuitong2.config.SessionKeeper;
import com.damenghai.chahuitong2.view.TopBar;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator;

public class MarketActivity extends BaseFragmentActivity {
	private TopBar mTopBar;
	private ViewPager mViewPager;

    private TabPageIndicator mIndicator;

    public TeaMarketApi mApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_market);

        mApi = new TeaMarketApi(this);

		findViewById();

		initView();
	}

	private void findViewById() {
		mTopBar = (TopBar) findViewById(R.id.market_bar);
		mViewPager = (ViewPager) findViewById(R.id.id_vp);
		mIndicator = (TabPageIndicator) findViewById(R.id.title_indicator);
	}

	private void initView() {
		mTopBar.setOnButtonClickListener(new TopBar.OnButtonClickListener() {
			@Override
			public void onLeftClick() {
				finishActivity();
			}

			@Override
			public void onRightClick() {
				if (SessionKeeper.readSession(MarketActivity.this).equals("")) {
					openActivity(LoginActivity.class);
				} else {
					openActivity(PublishActivity.class);
				}
			}
		});

        MarketFragmentAdapter adapter = new MarketFragmentAdapter(getSupportFragmentManager(), this);
		mViewPager.setAdapter(adapter);
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
