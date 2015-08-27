package com.damenghai.chahuitong.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.config.Constants;
import com.damenghai.chahuitong.ui.activity.HomeActivity;

import java.util.List;

public class GuideViewPagerAdapter extends PagerAdapter {
	private List<View> mViews;
	private Activity mActivity;
	
	public GuideViewPagerAdapter(List<View> mViews, Activity mActivity) {
		super();
		this.mViews = mViews;
		this.mActivity = mActivity;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mViews.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = mViews.get(position);
		container.addView(view);
		if(position == mViews.size() - 1) {
			ImageView imgStart = (ImageView) container.findViewById(R.id.start_btn);
			imgStart.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setGuided();
					goMain();
				}
				
			});
		}
		return view;
	}

	@Override
	public int getCount() {
		if(mViews != null) {
			return mViews.size();
		}
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	private void setGuided() {
		SharedPreferences preferences = mActivity.getSharedPreferences(Constants.SHARED_PREFERERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isFirstIn", false);
		editor.commit();
	}
	
	private void goMain() {
		Intent intent = new Intent(mActivity, HomeActivity.class);
		mActivity.startActivity(intent);
		mActivity.finish();
	}
}
