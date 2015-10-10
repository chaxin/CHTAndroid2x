package com.damenghai.chahuitong.ui.activity;

import java.util.List;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.GuideViewPagerAdapter;
import com.damenghai.chahuitong.base.BaseActivity;
import com.viewpagerindicator.CirclePageIndicator;

public class GuideActivity extends BaseActivity implements OnPageChangeListener  {
	private ViewPager mViewPager;
    private CirclePageIndicator mIndicator;

    private int[] mRes;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_guide);

        findViewById();

		initView();
	}

    @Override
    protected void findViewById() {
        mViewPager = (ViewPager) findViewById(R.id.guide_vp);
        mIndicator = (CirclePageIndicator) findViewById(R.id.guide_indicator);
    }

    @Override
    protected void initView() {
        mRes = new int[]{R.drawable.guide_03, R.drawable.guide_04, R.drawable.guide_05};
        GuideViewPagerAdapter adapter = new GuideViewPagerAdapter(GuideActivity.this, mRes);
        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(this);
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
	public void onPageSelected(int position) {
		//设置指示器在淡入淡出
		if(position == mRes.length-1) {
			AlphaAnimation alphaAnim = new AlphaAnimation(1, 0);
			alphaAnim.setDuration(700);
			mIndicator.startAnimation(alphaAnim);
            mIndicator.setVisibility(View.INVISIBLE);
		} else if(mIndicator.getVisibility() == View.INVISIBLE) {
			AlphaAnimation alphaAnim = new AlphaAnimation(0, 1);
			alphaAnim.setDuration(700);
            mIndicator.startAnimation(alphaAnim);
            mIndicator.setVisibility(View.VISIBLE);
		}
	}
}
