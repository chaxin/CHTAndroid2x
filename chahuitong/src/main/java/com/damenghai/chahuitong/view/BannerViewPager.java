package com.damenghai.chahuitong.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.bean.Product;
import com.lidroid.xutils.BitmapUtils;
import com.viewpagerindicator.PageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BannerViewPager extends RelativeLayout implements OnPageChangeListener {
    /**
     * Banner地址
     */
    public static String BANNER_URL;

    /**
     * Banner页数
     */
    public static int PAGER_COUNT;

	/**
	 * 左边用于过度的页面索引
	 */
	private final int FIRST_ITEM = 1;

    /**
     * 用来记录当前pager状态是否改变
     */
    private boolean isChange = false;

	private List<ImageView> mImageViews;
	private ViewPager mViewPager;
	private MyAdapter mAdapter;
	
	private int mCurrent = FIRST_ITEM;
	boolean isAutoScroll = true;

	private Timer timer = null;
	private TimerTask task = null;
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if(mImageViews.size() != 0)
				mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1) % mImageViews.size());
		}
	};
	
	public BannerViewPager(Context context) {
		this(context, null);
	}

	public BannerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.custom_banner, this);

		mImageViews = new ArrayList<ImageView>();
		mViewPager = (ViewPager) findViewById(R.id.id_custom_banner);
		mAdapter = new MyAdapter();

		startScrollThread();
	}

	private void startScrollThread() {
		if(isAutoScroll) {
			timer = new Timer();
			task = new TimerTask() {

				@Override
				public void run() {
					Message msg = handler.obtainMessage();
					handler.sendMessage(msg);
				}
			};

			timer.schedule(task, 4000);
		}
	}

	private class MyAdapter extends PagerAdapter {	
		
		@Override
		public int getCount() {
			return mImageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public ImageView instantiateItem(ViewGroup container, int position) {
			ImageView iv = mImageViews.get(position);
            container.addView(iv);
			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mImageViews.get(position));
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
        if(isChange && arg0 == ViewPager.SCROLL_STATE_IDLE) {
            isChange = false;
            mViewPager.setCurrentItem(mCurrent, false);
        }
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		isChange = true;
		if(arg0 > PAGER_COUNT) {
			mCurrent = FIRST_ITEM;
		} else if(arg0 < FIRST_ITEM) {
			mCurrent = PAGER_COUNT;
		} else {
			mCurrent = arg0;
		}
		if(timer != null) {
			timer.cancel();
		}
		startScrollThread();
	}

	public void stopScroll() {
		isAutoScroll = false;
		if(timer != null) timer.cancel();
	}
	
	public void setImageUrl(String rootURL, String name) {
        if(name == null) return;

        String[] urls = name.split(",");
        BANNER_URL = rootURL;
        PAGER_COUNT = urls.length;
        addImageView(urls[PAGER_COUNT - FIRST_ITEM]);
        for(int i=0; i<urls.length; i++) {
            addImageView(urls[i]);
		}
        addImageView(urls[0]);
		
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(mCurrent, false);
        mViewPager.setOnPageChangeListener(this);
	}

    public void setIndicator(PageIndicator indicator) {
        indicator.setViewPager(mViewPager, true);
        indicator.setCurrentItem(mCurrent);
        indicator.setOnPageChangeListener(this);
    }

    private void addImageView(String imageUrl) {
        BitmapUtils bitmapUtils = new BitmapUtils(getContext());
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ScaleType.CENTER_CROP);
        bitmapUtils.display(imageView, BANNER_URL + imageUrl);
        mImageViews.add(imageView);
    }

}
