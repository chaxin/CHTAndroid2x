package com.damenghai.chahuitong2.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.bean.Travel;
import com.damenghai.chahuitong2.config.SessionKeeper;
import com.damenghai.chahuitong2.utils.L;
import com.damenghai.chahuitong2.view.BannerViewPager;
import com.damenghai.chahuitong2.view.TopBar;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by LiaoPeiKun on 15/9/2.
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
    private TextView mEnroll;
    private TextView mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        mTravel = (Travel) getIntent().getSerializableExtra("travel");


        L.d("hahah");

        bindView();

        initView();
    }

    @Override
    protected void bindView() {
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
        mState = (TextView) findViewById(R.id.travel_tv_state);
        mEnroll = (TextView) findViewById(R.id.travel_tv_enroll);
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
        mEnroll.setText(mTravel.getEnrolled());
        mEnroll.setOnClickListener(this);
        mState.setText(mTravel.getState());

        if(mTravel.getEnrolled().equals("已报名")) {
            mEnroll.setBackgroundResource(R.color.primary_light);
            mEnroll.setEnabled(false);
        } else if(mTravel.getEnrolled().equals("未报名")) {
            mEnroll.setEnabled(true);
            mEnroll.setBackgroundResource(R.color.primary);
            mEnroll.setText("报名");
        }

        if (mTravel.getState().equals("已结束")) {
            mEnroll.setEnabled(false);
            mEnroll.setBackgroundResource(R.color.background_pressed);
            mEnroll.setText("已结束");
        }

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
