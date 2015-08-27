package com.damenghai.chahuitong.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.ui.fragment.IndividualFragement;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * Created by Sgun on 15/8/21.
 */
public class MyTeaMarketActivity extends FragmentActivity {
    private TabPageIndicator mIndicator;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;

    private ArrayList<IndividualFragement> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        findViewById();

        initView();
    }

    private void findViewById() {
        mIndicator = (TabPageIndicator) findViewById(R.id.mine_indicator);
        mViewPager = (ViewPager) findViewById(R.id.mine_vp);
    }

    private void initView() {
        mFragments = new ArrayList<IndividualFragement>();
        mFragments.add(new IndividualFragement());
        mFragments.add(new IndividualFragement());

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        public final String[] TITLES = new String[] {"我的求购", "我的出售"};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            return new IndividualFragement();
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }
}
