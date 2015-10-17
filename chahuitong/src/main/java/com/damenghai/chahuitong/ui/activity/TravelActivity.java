package com.damenghai.chahuitong.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.bean.Travel;
import com.damenghai.chahuitong.config.SessionKeeper;
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
    private TextView mSchedule;
    private TextView mDuration;
    private TextView mTime;
    private TextView mCount;
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

    @Override
    protected void findViewById() {
        mTopBar = (TopBar) findViewById(R.id.event_bar);
        mBanner = (BannerViewPager) findViewById(R.id.travel_banner);
        mIndicator = (CirclePageIndicator) findViewById(R.id.travel_indicator);
        mTitle = (TextView) findViewById(R.id.travel_title);
        mLocation = (TextView) findViewById(R.id.travel_location);
        mSchedule = (TextView) findViewById(R.id.travel_schedule);
        mDuration = (TextView) findViewById(R.id.travel_duration);
        mTime = (TextView) findViewById(R.id.travel_time);
        mCount = (TextView) findViewById(R.id.travel_count);
        mContent = (TextView) findViewById(R.id.travel_content);
        mFee = (TextView) findViewById(R.id.travel_fee);
        mJoin = (TextView) findViewById(R.id.btn_join);
    }

    @Override
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

        mBanner.setImageUrl(mTravel.getThumbImage());
        mBanner.setIndicator(mIndicator);

        mTopBar.setTitle(mTravel.getTitle());
        mTitle.setText(mTravel.getTitle());
        mLocation.setText(mTravel.getLocation());
        mSchedule.setText(getResources().getString(R.string.event_time) + mTravel.getDuration());
        mDuration.setText(getResources().getString(R.string.event_target) + mTravel.getObject());
        mTime.setText(getResources().getString(R.string.event_join_time) + mTravel.getJoin_time());
        mCount.setText(getResources().getString(R.string.event_count) + mTravel.getNumber());
        mContent.setText(mTravel.getContent());
        mFee.setText("￥" + mTravel.getFee());
        mJoin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(!SessionKeeper.readSession(TravelActivity.this).equals("")) {
            Bundle bundle = new Bundle();
            bundle.putInt("id", mTravel.getActive_id());
            openActivity(JoinEventActivity.class, bundle);
        } else {
            openActivity(LoginActivity.class);
        }
    }
}
