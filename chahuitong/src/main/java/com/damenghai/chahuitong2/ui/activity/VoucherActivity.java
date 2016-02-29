package com.damenghai.chahuitong2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.VoucherPagerAdapter;
import com.damenghai.chahuitong2.base.BaseFragmentActivity;
import com.damenghai.chahuitong2.view.TopBar;
import com.viewpagerindicator.TabPageIndicator;

/**
 * Created by Sgun on 15/10/15.
 */
public class VoucherActivity extends BaseFragmentActivity {
    private TopBar mTopBar;
    private TabPageIndicator mIndicator;
    private ViewPager mViewPager;

    private VoucherPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);

        findViewById();

        initView();

    }

    protected void findViewById() {
        mTopBar = (TopBar) findViewById(R.id.voucher_bar);
        mIndicator = (TabPageIndicator) findViewById(R.id.voucher_indicator);
        mViewPager = (ViewPager) findViewById(R.id.voucher_viewpager);
    }

    protected void initView() {
        mTopBar.setOnButtonClickListener(new TopBar.OnButtonClickListener() {
            @Override
            public void onLeftClick() {
                finishActivity();
            }

            @Override
            public void onRightClick() {
                Intent intent = new Intent(VoucherActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
            }
        });

        mAdapter = new VoucherPagerAdapter(this);
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);
    }

}
