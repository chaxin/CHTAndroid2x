package com.damenghai.chahuitong.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.damenghai.chahuitong.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.bean.Brand;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.ui.fragment.BrandFragment;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.utils.ViewHolder;
import com.damenghai.chahuitong.view.TopBar;
import com.google.gson.Gson;
import com.viewpagerindicator.TabPageIndicator;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sgun on 15/8/10.
 */
public class ShopActivity extends FragmentActivity {
    private Adapter mAdapter;
    private ViewPager mViewpager;
    private TabPageIndicator mIndicator;
    private TopBar mTopBar;

    private List<Brand> mDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        findViewById();

        initView();
    }

    private void findViewById() {
        mViewpager = (ViewPager) findViewById(R.id.shop_viewpager);
        mIndicator = (TabPageIndicator) findViewById(R.id.shop_indicator);
        mTopBar = (TopBar) findViewById(R.id.shop_topbar);
    }

    private void initView() {
        mDatas = new ArrayList();
        mAdapter = new Adapter(getSupportFragmentManager());
        mViewpager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewpager);
        mTopBar.setOnLeftClickListener(new TopBar.OnLeftClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });

        mTopBar.setOnRightClickListener(new TopBar.onRightClickListener() {
            @Override
            public void onRightClick() {
                Intent intent = new Intent(ShopActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    private class Adapter extends FragmentPagerAdapter {
        private final int[] CATEGORY_ID = new int[] {1, 2, 3, 256, 308, 470, 530};
        private final String[] TITLES = new String[] {"普洱茶", "乌龙茶", "红茶", "绿茶", "黑茶", "黄茶", "白茶"};

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return BrandFragment.get(TITLES[position], CATEGORY_ID[position]);
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
