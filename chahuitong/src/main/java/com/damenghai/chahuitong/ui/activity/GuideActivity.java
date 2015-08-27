package com.damenghai.chahuitong.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.GuideViewPagerAdapter;

public class GuideActivity extends Activity implements OnPageChangeListener  {
	private ViewPager mVp;
	private PagerAdapter mAdapter;
	
	private List<View> mViews;
	private ImageView[] mDots;
	private LinearLayout ll;
	
	private int mCurrentIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.guide);
		
		initViews();
		initDots();
	}
	
	private void initViews() {
		mViews = new ArrayList<View>();

		LayoutInflater inflater = LayoutInflater.from(this);
		
		mViews.add(inflater.inflate(R.layout.guide_new_first, null));
		mViews.add(inflater.inflate(R.layout.guide_new_second, null));
		mViews.add(inflater.inflate(R.layout.guide_new_third, null));
		mViews.add(inflater.inflate(R.layout.guide_new_fourth, null));
		mViews.add(inflater.inflate(R.layout.guide_new_fifth, null));
		
		mVp = (ViewPager) findViewById(R.id.viewPager_guide);
		mAdapter = new GuideViewPagerAdapter(mViews,GuideActivity.this);
		mVp.setAdapter(mAdapter);
		mVp.setOnPageChangeListener(this);
	}
	
	private void initDots() {
		ll = (LinearLayout) findViewById(R.id.ll);
		mDots = new ImageView[mViews.size()];
		mCurrentIndex = 0;
		
		for(int i=0; i<mViews.size(); i++) {
			mDots[i] = (ImageView) ll.getChildAt(i);
			mDots[i].setEnabled(true);
		}
		
		mDots[mCurrentIndex].setEnabled(false);
	}
	
	private void setCurrentDot(int position) {
		if(position<0 || position>mViews.size() || position==mCurrentIndex) return;
		mDots[mCurrentIndex].setEnabled(true);
		mDots[position].setEnabled(false);
		mCurrentIndex = position;
	}

	//当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	//当页面滑动时调动
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	//当页面选中时调用
	@Override
	public void onPageSelected(int arg0) {
		//设置指示器在淡入淡出
		if(arg0 == mViews.size()-1) {
			AnimationSet animationSet = new AnimationSet(true);
			AlphaAnimation alphaAnim = new AlphaAnimation(1, 0);
			alphaAnim.setDuration(700);
			animationSet.addAnimation(alphaAnim);
			ll.startAnimation(animationSet);
			ll.setVisibility(View.INVISIBLE);
		} else if(ll.getVisibility() == View.INVISIBLE) {
			AnimationSet animationSet = new AnimationSet(true);
			AlphaAnimation alphaAnim = new AlphaAnimation(0, 1);
			alphaAnim.setDuration(700);
			animationSet.addAnimation(alphaAnim);
			ll.startAnimation(animationSet);
			ll.setVisibility(View.VISIBLE);
		}
		setCurrentDot(arg0);
	}
}
