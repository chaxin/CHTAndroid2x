package com.damenghai.chahuitong2.adapter;

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

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.config.Constants;
import com.damenghai.chahuitong2.ui.activity.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class GuideViewPagerAdapter extends PagerAdapter {
	private List<View> mViews;
    private int[] mRes;
	private Context mContext;

	public GuideViewPagerAdapter(Context context, int[] res) {
		super();
		this.mContext = context;
        this.mRes = res;
        initView();
	}

    private void initView() {
        mViews = new ArrayList<View>();

        for(int i=0; i<mRes.length; i++) {
            View view = View.inflate(mContext, R.layout.pager_guide, null);
            mViews.add(view);
        }
    }

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mViews.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = mViews.get(position);

		ImageView iv = (ImageView) view.findViewById(R.id.guide_image);
        iv.setImageDrawable(mContext.getResources().getDrawable(mRes[position]));

		if(position == mRes.length - 1) {
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setGuided();
					goMain();
				}

			});
		}

		container.addView(view);
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
		SharedPreferences preferences = mContext.getSharedPreferences(Constants.SHARED_PREFERERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isFirstIn", false);
		editor.commit();
	}
	
	private void goMain() {
        Activity activity = (Activity) mContext;
		Intent intent = new Intent(mContext, HomeActivity.class);
		mContext.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		activity.finish();
	}
}
