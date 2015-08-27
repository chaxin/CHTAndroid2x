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
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.bean.Travel;
import com.damenghai.chahuitong.ui.fragment.LeaderFragment;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ForumActivity extends FragmentActivity implements View.OnClickListener {
    private LinearLayout mIncludeStatuses;

    private ViewPager mLeaderViewPager;
    private CirclePageIndicator mLeaderIndicator;
    private LeaderViewPagerAdapter mLeaderAdapter;
    private LinearLayout mStatusComment;

    private ViewPager mTravelViewPager;
    private CirclePageIndicator mTravelIndicator;
    private TravelViewPagerAdapter mTravelAdapter;
    private ArrayList<Travel> mTravels;

    private TextView mLeaderMore, mTopicMore, mTravelMore, mStatusesMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        findViewById();

        initView();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void findViewById() {
        mIncludeStatuses = (LinearLayout) findViewById(R.id.include_forum_statuses);
        mStatusComment = (LinearLayout) mIncludeStatuses.findViewById(R.id.control_comment);

        mLeaderViewPager = (ViewPager) findViewById(R.id.forum_vp_leader);
        mLeaderIndicator = (CirclePageIndicator) findViewById(R.id.forum_leader_indicator);

        mTravelViewPager = (ViewPager) findViewById(R.id.forum_vp_party);
        mTravelIndicator = (CirclePageIndicator) findViewById(R.id.travel_indicator);

        mLeaderMore = (TextView) findViewById(R.id.leader_tv_more);
        mTopicMore = (TextView) findViewById(R.id.topic_tv_more);
        mTravelMore = (TextView) findViewById(R.id.travel_tv_more);
        mStatusesMore = (TextView) findViewById(R.id.statuses_tv_more);
    }

    private void initView() {
        mLeaderAdapter = new LeaderViewPagerAdapter(getSupportFragmentManager());
        mLeaderViewPager.setAdapter(mLeaderAdapter);
        mLeaderIndicator.setFillColor(getResources().getColor(R.color.primary));
        mLeaderIndicator.setPageColor(getResources().getColor(R.color.backcolor));
        mLeaderIndicator.setViewPager(mLeaderViewPager);
        mLeaderIndicator.setSnap(true);

        initData();
        mTravelAdapter = new TravelViewPagerAdapter(mTravels, this);
        mTravelViewPager.setAdapter(mTravelAdapter);
        mTravelIndicator.setFillColor(getResources().getColor(R.color.primary));
        mTravelIndicator.setPageColor(getResources().getColor(R.color.backcolor));
        mTravelIndicator.setViewPager(mTravelViewPager);
        mTravelIndicator.setSnap(true);

        // 更多按钮点击事件监听
        mLeaderMore.setOnClickListener(this);
        mTopicMore.setOnClickListener(this);
        mTravelMore.setOnClickListener(this);
        mStatusesMore.setOnClickListener(this);

        // 评论按钮
        mStatusComment.setOnClickListener(this);
    }

    private void initData() {
        mTravels = new ArrayList<Travel>();
        Travel travel1 = new Travel();
        travel1.setTitle("布郎山淘茶之旅");
        travel1.setTime("行程时间：2015-09-01~2015-10-10\n行程天数：5天");
        mTravels.add(travel1);

        Travel travel2 = new Travel();
        travel2.setTitle("武夷山淘茶之旅");
        travel2.setTime("行程时间：2015-08-02~2015-08-12\n行程天数：10天");
        mTravels.add(travel2);

        Travel travel3 = new Travel();
        travel3.setTitle("布郎山淘茶之旅");
        travel3.setTime("行程时间：2015-08-23~2015-08-30\n行程天数：7天");
        mTravels.add(travel3);

        Travel travel4 = new Travel();
        travel4.setTitle("武夷山淘茶之旅");
        travel4.setTime("行程时间：2015-09-01~2015-10-10\n行程天数：9天");
        mTravels.add(travel4);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.leader_tv_more :
                intent.setClass(ForumActivity.this, LeadersActivity.class);
                break;
            case R.id.topic_tv_more :
                intent.setClass(ForumActivity.this, TopicsActivity.class);
                break;
            case R.id.travel_tv_more :
                intent.setClass(ForumActivity.this, TravleActivity.class);
                break;
            case R.id.statuses_tv_more :
                intent.setClass(ForumActivity.this, StatusesActivity.class);
                break;
            case R.id.control_comment :
                intent.setClass(ForumActivity.this, CommentActivity.class);
                break;
        }
        startActivity(intent);
    }

    private class LeaderViewPagerAdapter extends FragmentPagerAdapter {

        public LeaderViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new LeaderFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    private class TravelViewPagerAdapter extends PagerAdapter {
        private ArrayList<Travel> mTravels;
        private Context mContext;
        private ArrayList<View> mViews;

        public TravelViewPagerAdapter(ArrayList<Travel> mTravels, Context mContext) {
            this.mTravels = mTravels;
            this.mContext = mContext;
            mViews = new ArrayList<View>();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(mContext, R.layout.viewpager_item_travel, null);
            TextView title = (TextView) view.findViewById(R.id.travel_title);
            TextView time = (TextView) view.findViewById(R.id.travel_time);

            title.setText(mTravels.get(position).getTitle());
            time.setText(mTravels.get(position).getTime());

            mViews.add(view);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews.get(position));
        }

        @Override
        public int getCount() {
            return mTravels.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
