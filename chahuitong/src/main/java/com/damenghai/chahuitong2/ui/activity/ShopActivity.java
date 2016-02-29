package com.damenghai.chahuitong2.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.base.BaseFragmentActivity;
import com.damenghai.chahuitong2.ui.fragment.BrandFragment;
import com.damenghai.chahuitong2.view.TopBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

/**
 * Created by Sgun on 15/8/10.
 */
public class ShopActivity extends BaseFragmentActivity {
    @ViewInject(R.id.shop_viewpager)
    private ViewPager mViewpager;

    @ViewInject(R.id.shop_indicator)
    private TabPageIndicator mIndicator;

    @ViewInject(R.id.shop_topbar)
    private TopBar mTopBar;

    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        ViewUtils.inject(this);

        mPosition = getIntent().getIntExtra("position", 0);

        initView();
    }

    private void initView() {
        mTopBar.setOnButtonClickListener(new TopBar.OnButtonClickListener() {
            @Override
            public void onLeftClick() {
                finishActivity();
            }

            @Override
            public void onRightClick() {
                goHome();
            }
        });

        Adapter adapter = new Adapter(getSupportFragmentManager());
        mViewpager.setAdapter(adapter);
        mIndicator.setViewPager(mViewpager);
        mViewpager.setCurrentItem(mPosition);
    }

    private class Adapter extends FragmentPagerAdapter {
        private final String[] CATEGORY_ID = getResources().getStringArray(R.array.tab_category_ids);
        private final String[] TITLES = getResources().getStringArray(R.array.tab_categories);

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return BrandFragment.get(CATEGORY_ID[position], "shop");
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }
}
