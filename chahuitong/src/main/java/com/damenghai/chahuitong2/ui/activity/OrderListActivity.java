package com.damenghai.chahuitong2.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.base.BaseFragmentActivity;
import com.damenghai.chahuitong2.ui.fragment.OrderListFragment;
import com.damenghai.chahuitong2.view.TopBar;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class OrderListActivity extends BaseFragmentActivity {
    /**
     * 未付款
     */
    public static final int STATE_UNPAID = 10;
    /**
     * 已付款，待发货
     */
    public static final int STATE_PAID = 20;
    /**
     * 已发货，待收货
     */
    public static final int STATE_RECEIVE = 30;
    /**
     * 交易完成，待评价
     */
    public static final int STATE_UNCOMMENT = 40;


    private TopBar mTopBar;

    private TabPageIndicator mIndicator;

    private ViewPager mViewPager;

    private OrderPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        bindView();

        initView();
    }

    protected void bindView() {
        mTopBar = (TopBar) findViewById(R.id.order_list_bar);
        mIndicator = (TabPageIndicator) findViewById(R.id.order_list_indicator);
        mViewPager = (ViewPager) findViewById(R.id.order_list_pager);
    }

    protected void initView() {
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

        mAdapter = new OrderPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);
    }

    private class OrderPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES;
        private final String[] STATES;

        private ArrayList<OrderListFragment> mFragments;

        public OrderPagerAdapter(FragmentManager fm) {
            super(fm);
            TITLES = getApplicationContext().getResources().getStringArray(R.array.tab_order_list);
            STATES = getApplicationContext().getResources().getStringArray(R.array.tab_order_list_id);
            initFragments();
        }

        private void initFragments() {
            mFragments = new ArrayList<OrderListFragment>();
            for(int i=0; i<TITLES.length; i++) {
                mFragments.add(OrderListFragment.get(STATES[i]));
            }
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }

}
