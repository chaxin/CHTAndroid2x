package com.damenghai.chahuitong.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.bean.Travel;
import com.damenghai.chahuitong.view.BannerViewPager;
import com.damenghai.chahuitong.view.TopBar;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by Sgun on 15/9/2.
 */
public class TravelActivity extends BaseActivity implements View.OnClickListener {
    private Travel mTravel;

    private TopBar mTopBar;
    private BannerViewPager mBanner;
    private CirclePageIndicator mIndicator;
    private TextView mTitle;
    private TextView mLocation;
    private TextView mCost;
    private TextView mDuration;
    private TextView mTime;
    private TextView mContent;
    private TextView mFee;
    private TextView mJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        mTravel = (Travel) getIntent().getSerializableExtra("travel");

        findViewById();

        initView();
    }

    private void findViewById() {
        mTopBar = (TopBar) findViewById(R.id.event_bar);
        mBanner = (BannerViewPager) findViewById(R.id.travel_banner);
        mIndicator = (CirclePageIndicator) findViewById(R.id.travel_indicator);
        mTitle = (TextView) findViewById(R.id.travel_title);
        mLocation = (TextView) findViewById(R.id.travel_location);
        mCost = (TextView) findViewById(R.id.travel_cost);
        mDuration = (TextView) findViewById(R.id.travel_duration);
        mTime = (TextView) findViewById(R.id.travel_time);
        mContent = (TextView) findViewById(R.id.travel_content);
        mFee = (TextView) findViewById(R.id.travel_fee);
        mJoin = (TextView) findViewById(R.id.btn_join);
    }

    private void initView() {
        mTopBar.setOnLeftClickListener(new TopBar.OnLeftClickListener() {
            @Override
            public void onLeftClick() {

            }
        });

        mTopBar.setOnRightClickListener(new TopBar.onRightClickListener() {
            @Override
            public void onRightClick() {
                if (mTitle != null) {
                    mController.setShareContent(mTravel.getTitle());
                }
                // 是否只有已登录用户才能打开分享选择页
                mController.openShare(TravelActivity.this, false);
            }
        });

        mBanner.setImageUrl(mTravel.getThumbImage());
        mBanner.setIndicator(mIndicator);

        mTopBar.setTitle(mTravel.getTitle());
        mTitle.setText(mTravel.getTitle());
        mLocation.setText(mTravel.getLocation());
        mCost.setText("￥" + mTravel.getFee());
        mDuration.setText("行程天数：" + mTravel.getDuration());
        mTime.setText("行程时间：" + mTravel.getTime());
        mContent.setText(mTravel.getContent());
        mFee.setText("￥" + mTravel.getFee());
        mJoin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", mTravel.getActive_id());
        openActivity(JoinEventActivity.class, bundle);
    }
}
