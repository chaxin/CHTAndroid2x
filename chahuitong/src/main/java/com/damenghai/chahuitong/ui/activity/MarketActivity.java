package com.damenghai.chahuitong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.MarketFragmentAdapter;
import com.damenghai.chahuitong.base.BaseFragmentActivity;
import com.damenghai.chahuitong.view.TopBar;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator;

public class MarketActivity extends BaseFragmentActivity implements View.OnClickListener {
	private TopBar mTopBar;
	private ViewPager mViewPager;
	private ImageView mScrollTop;
	private ImageView mPublish;

	private MarketFragmentAdapter mAdapter;
	private TabPageIndicator mIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_market);

		findViewById();

		initView();
	}

	private void findViewById() {
		mTopBar = (TopBar) findViewById(R.id.market_bar);
		mViewPager = (ViewPager) findViewById(R.id.id_vp);
		mIndicator = (TabPageIndicator) findViewById(R.id.title_indicator);
		mScrollTop = (ImageView) findViewById(R.id.iv_scroll_top);
		mPublish = (ImageView) findViewById(R.id.iv_publish_product);
	}

	private void initView() {
		mTopBar.setOnLeftClickListener(new TopBar.OnLeftClickListener() {
			@Override
			public void onLeftClick() {
				finish();
			}
		});

		mTopBar.setOnRightClickListener(new TopBar.onRightClickListener() {
			@Override
			public void onRightClick() {
				Intent intent = new Intent(MarketActivity.this, HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});

		mAdapter = new MarketFragmentAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mViewPager);

		mScrollTop.setOnClickListener(this);
		mPublish.setOnClickListener(this);
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

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.iv_scroll_top :

				break;
			case R.id.iv_publish_product :
				openActivity(PublishActivity.class);
				break;
		}
	}
}
