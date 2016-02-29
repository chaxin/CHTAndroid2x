package com.damenghai.chahuitong2.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.api.ShopApi;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.bean.Banner;
import com.damenghai.chahuitong2.response.ResponseCallBackListener;
import com.damenghai.chahuitong2.utils.DensityUtils;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.view.BannerViewPager;
import com.damenghai.chahuitong2.view.TopBar;
import com.damenghai.chahuitong2.view.WrapHeightGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.UnderlinePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/10/20.
 */
public class CategoryActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @ViewInject(R.id.category_pager)
    private BannerViewPager mViewPager;

    @ViewInject(R.id.category_indicator)
    private UnderlinePageIndicator mIndicator;

    @ViewInject(R.id.category_gv)
    private WrapHeightGridView mGv;

    private ShopApi mApi;

    private ArrayList<Banner> mBanners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mApi = new ShopApi(this);

        ViewUtils.inject(this);

        bindView();

        initView();
    }

    @Override
    protected void bindView() {}

    @Override
    protected void initView() {
        ((TopBar) findViewById(R.id.category_bar)).setOnButtonClickListener(new TopBar.OnButtonClickListener() {
            @Override
            public void onLeftClick() {
                finishActivity();
            }

            @Override
            public void onRightClick() {
                goHome();
            }
        });

        CategoryGridViewAdapter adapter = new CategoryGridViewAdapter(this, R.layout.gridview_item_image);
        mGv.setAdapter(adapter);
        mGv.setOnItemClickListener(this);

        setBanner();
    }

    private void setBanner() {
        mBanners = new ArrayList<>();

        mIndicator.setFades(false);

        mApi.showCategoryBanners(new ResponseCallBackListener<List<Banner>>() {
            @Override
            public void onSuccess(List<Banner> banners) {
                for (Banner banner : banners) {
                    if (!mBanners.contains(banner)) {
                        mBanners.add(banner);
                    }
                }

                mViewPager.setImageUrl("", mBanners);
                mViewPager.setIndicator(mIndicator);
            }

            @Override
            public void onError(int errorEvent, String message) {
                T.showShort(CategoryActivity.this, message);
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", i);
        openActivity(ShopActivity.class, bundle);
    }

    private class CategoryGridViewAdapter extends BaseAdapter {
        private final int[] CATEGORIES = new int[] {
                R.drawable.category_image_puer, R.drawable.category_image_wulong,
                R.drawable.category_image_hongcha, R.drawable.category_image_lucha,
                R.drawable.category_image_heicha, R.drawable.category_image_huacha,
                R.drawable.category_image_baicha, R.drawable.category_image_chaju,
                R.drawable.category_image_qita };

        private Context mContext;
        private int mRes;

        public CategoryGridViewAdapter(Context context, int resId) {
            this.mContext = context;
            this.mRes = resId;
        }

        @Override
        public int getCount() {
            return CATEGORIES.length;
        }

        @Override
        public Object getItem(int i) {
            return CATEGORIES[i];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null) {
                convertView = View.inflate(mContext, mRes, null);

                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.gridview_item_image);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            WrapHeightGridView gv = (WrapHeightGridView) parent;

            int numCol = gv.getNumColumns();
            int width = gv.getWidth() / numCol;

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, RelativeLayout.LayoutParams.WRAP_CONTENT);
            holder.iv.setLayoutParams(params);

            int padding = DensityUtils.dp2px(mContext, 17);
            holder.iv.setPadding(padding, padding / 2, padding, padding / 2);
            holder.iv.setImageResource(android.R.color.white);

            holder.iv.setImageResource(CATEGORIES[position]);

            return convertView;
        }

        private class ViewHolder {
            ImageView iv;
        }
    }

}
