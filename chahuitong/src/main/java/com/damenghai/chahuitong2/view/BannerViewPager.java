package com.damenghai.chahuitong2.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.bean.Banner;
import com.damenghai.chahuitong2.ui.activity.CategoryActivity;
import com.damenghai.chahuitong2.ui.activity.ShopActivity;
import com.damenghai.chahuitong2.ui.activity.WebViewActivity;
import com.damenghai.chahuitong2.utils.ImageConfigHelper;
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

	/**
	 * 自动滚动的时间间隔
	 */
	private final int SCROLL_DURATION = 5000;

	private boolean mLoop = true;

	private List<Banner> mBanners;
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
		inflater.inflate(R.layout.view_banner, this);

		mImageViews = new ArrayList<ImageView>();
		mViewPager = (ViewPager) findViewById(R.id.id_custom_banner);
		mAdapter = new MyAdapter(context);

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

			timer.schedule(task, SCROLL_DURATION);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int currentItem, float arg1, int arg2) {
        if(isChange && (arg1 > 0.9 || arg1 < 0.1)) {
            isChange = false;
            mViewPager.setCurrentItem(mCurrent, false);
        }
	}

	@Override
	public void onPageSelected(int position) {
		isChange = true;
        if(mLoop) {
            if (position > PAGER_COUNT) {
                mCurrent = FIRST_ITEM;
            } else if (position < FIRST_ITEM) {
                mCurrent = PAGER_COUNT;
            } else {
                mCurrent = position;
            }
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

	public void setImageUrl(ArrayList<Banner> name) {
		setImageUrl("", name);
	}

	/**
	 * 设置轮播图片地址
	 *
	 * @param rootURL
	 * 					图片根路径
	 * @param banners
	 * 					图片集合
	 */
	public void setImageUrl(String rootURL, List<Banner> banners) {
        if(banners == null) return;

        BANNER_URL = rootURL;
        PAGER_COUNT = banners.size();
		mBanners = banners;

        if(PAGER_COUNT != 1) addImageView(banners.get(PAGER_COUNT - FIRST_ITEM).getImage());
        for(Banner banner : banners) {
            addImageView(banner.getImage());
		}
        if(PAGER_COUNT != 1) addImageView(banners.get(0).getImage());
		
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(mCurrent, false);
        mViewPager.setOnPageChangeListener(this);
	}

	public void setImageUrl(String names) {
		setImageUrl("", names);
	}

	/**
	 * 设置Banner的图片地址，默认开启循环
	 *
	 * @param rootURL 根路径
	 * @param names 图片名称
	 */
	public void setImageUrl(String rootURL, String names) {
		setImageUrl(rootURL, names, true);
	}

    /**
     * 设置Banner图片地址并设置是否循环
     *
     * @param rootURL
     * @param names
     * @param loop
     */
    public void setImageUrl(String rootURL, String names, boolean loop) {
        if(names == null) return;

        String[] urls = names.split(",");
        BANNER_URL = rootURL;
        PAGER_COUNT = urls.length;
        mLoop = loop;

        if(PAGER_COUNT != 1 && loop) addImageView(urls[PAGER_COUNT - FIRST_ITEM]);
        for (String url : urls) {
            addImageView(url);
        }
        if(PAGER_COUNT != 1 && loop) addImageView(urls[0]);

        mViewPager.setAdapter(mAdapter);
        if(loop) mViewPager.setCurrentItem(mCurrent, false);
        mViewPager.addOnPageChangeListener(this);
    }

	/**
	 * 设置指示器
	 *
	 * @param indicator
	 */
    public void setIndicator(PageIndicator indicator) {
		if(PAGER_COUNT != 1 && mLoop) {
            indicator.setViewPager(mViewPager, true);
            indicator.setCurrentItem(mCurrent);
            indicator.setOnPageChangeListener(this);
        }
		else indicator.setViewPager(mViewPager, false);
    }

    private void addImageView(String imageUrl) {
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ScaleType.CENTER_CROP);
		BitmapUtils bitmapUtils = new BitmapUtils(getContext());
        bitmapUtils.display(imageView, BANNER_URL + imageUrl, ImageConfigHelper.getGrayConfig(getContext()));
        mImageViews.add(imageView);
    }

    private void openActivity(Class<? extends Activity> clazz, Bundle bundle) {
        Intent intent = new Intent(getContext(), clazz);
        if(bundle != null) intent.putExtras(bundle);
        getContext().startActivity(intent);
        if(getContext() instanceof Activity) ((Activity) getContext()).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

	private class MyAdapter extends PagerAdapter {
		private Context mContext;

		public MyAdapter(Context context) {
			mContext = context;
		}

		@Override
		public int getCount() {
			return mImageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public ImageView instantiateItem(ViewGroup container, final int position) {
			ImageView iv = mImageViews.get(position);

			if(mBanners != null) {
				iv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						if(mLoop) {
							if(position - 1 < mBanners.size()) {
                                if(mBanners.get(position - 1).getLink().contains("index.php/Home/index/brandLoad")) {
                                    Intent intent = new Intent(getContext(), CategoryActivity.class);
                                    getContext().startActivity(intent);
                                }  else if (mBanners.get(position - 1).getLink().contains("brand/catId")) {
                                    String link = mBanners.get(position - 1).getLink();
                                    String cartId = link.substring(link.lastIndexOf("/") + 1);
                                    if(TextUtils.isEmpty(cartId)) {
                                        openActivity(ShopActivity.class, null);
                                    } else {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("position", Integer.parseInt(cartId));
                                        openActivity(ShopActivity.class, bundle);
                                    }
                                } else {
                                    Intent intent = new Intent(mContext, WebViewActivity.class);
                                    intent.putExtra("url", mBanners.get(position - 1).getLink());
                                    mContext.startActivity(intent);
                                }
							}
						} else if (mBanners.get(position - 1).getLink().contains("index.php/Home/index/brandLoad")) {
                            openActivity(CategoryActivity.class, null);
						} else {
                            Bundle bundle = new Bundle();
                            bundle.putString("url", mBanners.get(position).getLink());
                            openActivity(WebViewActivity.class, bundle);

                            Intent intent = new Intent(mContext, WebViewActivity.class);
                            intent.putExtra("url", mBanners.get(position).getLink());
                            mContext.startActivity(intent);
                        }
					}
				});
			}

			container.addView(iv);
			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mImageViews.get(position));
		}

	}

}
