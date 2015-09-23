package com.damenghai.chahuitong.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.adapter.StatusesAdapter;
import com.damenghai.chahuitong.adapter.TravelViewPagerAdapter;
import com.damenghai.chahuitong.api.HodorRequest;
import com.damenghai.chahuitong.base.BaseFragmentActivity;
import com.damenghai.chahuitong.bean.Leader;
import com.damenghai.chahuitong.bean.Status;
import com.damenghai.chahuitong.bean.Travel;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.ui.fragment.LeaderFragment;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.utils.ViewHolder;
import com.damenghai.chahuitong.view.TopBar;
import com.damenghai.chahuitong.view.WrapHeightListView;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ForumActivity extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ViewPager mLeaderViewPager;
    private CirclePageIndicator mLeaderIndicator;

    private TopBar mTopBar;

    private ScrollView mScrollView;

    // 今日新声
    private ImageView mTopicImage;
    private TextView mTopicTitle;
    private TextView mTopicText;
    private TextView mTopicHost;
    private TextView mTopicCount;

    // 茶客聚聚
    private ViewPager mTravelViewPager;
    private CirclePageIndicator mTravelIndicator;
    private TravelViewPagerAdapter mTravelPagerAdapter;
    private ArrayList<Travel> mTravels;
    private WrapHeightListView mTravelListView;
    private TravelListViewAdapter mTravelListAdapter;

    // 晒一晒
    private WrapHeightListView mStatusList;
    private StatusesAdapter mStatusAdapter;
    private ArrayList<Status> mStatuses;

    // 更多
    private TextView mLeaderMore, mTopicMore, mTravelMore, mStatusesMore;

    private ImageView mScrollTop;
    private ImageView mWriteStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        findViewById();

        initView();

        loadData();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void findViewById() {
        mLeaderViewPager = (ViewPager) findViewById(R.id.forum_vp_leader);
        mLeaderIndicator = (CirclePageIndicator) findViewById(R.id.forum_leader_indicator);

        mScrollView = (ScrollView) findViewById(R.id.forum_scroll_view);

        mTopBar = (TopBar) findViewById(R.id.forum_home_bar);

        // 今日新声
        mTopicImage = (ImageView) findViewById(R.id.forum_topic_image);
        mTopicTitle = (TextView) findViewById(R.id.forum_topic_title);
        mTopicText = (TextView) findViewById(R.id.forum_topic_text);
        mTopicHost = (TextView) findViewById(R.id.forum_topic_host);
        mTopicCount = (TextView) findViewById(R.id.forum_topic_count);

        // 茶客聚聚
        mTravelViewPager = (ViewPager) findViewById(R.id.forum_vp_party);
        mTravelIndicator = (CirclePageIndicator) findViewById(R.id.travel_indicator);
        mTravelListView = (WrapHeightListView) findViewById(R.id.travel_lv);

        // 晒一晒
        mStatusList = (WrapHeightListView) findViewById(R.id.forum_statuses);

        // 更多
        mLeaderMore = (TextView) findViewById(R.id.leader_tv_more);
        mTopicMore = (TextView) findViewById(R.id.topic_tv_more);
        mTravelMore = (TextView) findViewById(R.id.travel_tv_more);
        mStatusesMore = (TextView) findViewById(R.id.statuses_tv_more);

        mScrollTop = (ImageView) findViewById(R.id.iv_scroll_top);
        mWriteStatus = (ImageView) findViewById(R.id.iv_write_status);
    }

    private void initView() {
        mTopBar.setOnLeftClickListener(new TopBar.OnLeftClickListener() {
            @Override
            public void onLeftClick() {
                finishActivity();
            }
        });

        mTopBar.setOnRightClickListener(new TopBar.onRightClickListener() {
            @Override
            public void onRightClick() {
                Intent intent = new Intent(ForumActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        LeaderViewPagerAdapter mLeaderAdapter = new LeaderViewPagerAdapter(getSupportFragmentManager());
        mLeaderViewPager.setAdapter(mLeaderAdapter);
        mLeaderIndicator.setFillColor(getResources().getColor(R.color.primary));
        mLeaderIndicator.setPageColor(getResources().getColor(R.color.background));
        mLeaderIndicator.setViewPager(mLeaderViewPager);
        mLeaderIndicator.setSnap(true);

        // 茶客聚聚
        mTravels = new ArrayList<Travel>();
        mTravelPagerAdapter = new TravelViewPagerAdapter(mTravels, this);
        mTravelViewPager.setAdapter(mTravelPagerAdapter);
        mTravelIndicator.setViewPager(mTravelViewPager);
        mTravelListAdapter = new TravelListViewAdapter(ForumActivity.this, mTravels, R.layout.listview_item_home_travel);
        mTravelListView.setFocusable(false);
        mTravelListView.setOnItemClickListener(this);
        mTravelListView.setAdapter(mTravelListAdapter);

        // 晒一晒
        mStatuses = new ArrayList<Status>();
        mStatusAdapter = new StatusesAdapter(ForumActivity.this, mStatuses, R.layout.listview_item_status, true);
        mStatusList.setAdapter(mStatusAdapter);
        mStatusList.setFocusable(false);

        // 更多按钮点击事件监听
        mLeaderMore.setOnClickListener(this);
        mTopicMore.setOnClickListener(this);
        mTravelMore.setOnClickListener(this);
        mStatusesMore.setOnClickListener(this);

        mScrollTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScrollView.scrollTo(0, 0);
            }
        });
        mWriteStatus.setOnClickListener(this);

    }

    private void loadData() {
        // 获取今日新声
        HodorRequest.getRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/last_news_api", new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("code") != 404) {
                        Status status = new Gson().fromJson(obj.getString("content"), Status.class);
                        mTopicTitle.setText(status.getTitle());
                        mTopicText.setText(status.getText());
                        mTopicCount.setText("已有" + status.getComment() + "人参加");

                        String image = status.getImage().substring(0, status.getImage().indexOf(","));
                        BitmapUtils utils = new BitmapUtils(ForumActivity.this);
                        utils.display(mTopicImage, "http://www.chahuitong.com/data/upload/qunzi/" + image);

                        Leader leader = new Gson().fromJson(obj.getString("memberInfo"), Leader.class);
                        mTopicHost.setText("话题主理人：" + leader.getMember_name());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        HodorRequest.getRequest("http://www.chahuitong.com/wap/index.php/Home/discuz/get_allperson_active_api", new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("code") != 404) {
                        JSONArray array = obj.getJSONArray("content");
                        for (int i = 0; i < array.length(); i++) {
                            Travel travel = new Gson().fromJson(array.getString(i), Travel.class);
                            mTravels.add(travel);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mTravelPagerAdapter.notifyDataSetChanged();
                mTravelListAdapter.notifyDataSetChanged();
            }
        });

        HodorRequest.getRequest("http://www.chahuitong.com/wap/index.php/Home/Discuz/get_allperson_content_api", new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("code") != 404) {
                        JSONArray array = obj.getJSONArray("content");
                        for (int i = 0; i < array.length(); i++) {
                            Status status = new Gson().fromJson(array.getString(i), Status.class);
                            if (!mStatuses.contains(status)) mStatuses.add(status);
                        }
                    } else {
                        T.showShort(ForumActivity.this, obj.getString("content"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mStatusAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.leader_tv_more :
                openActivity(LeadersActivity.class);
                break;
            case R.id.topic_tv_more :
                openActivity(TopicsActivity.class);
                break;
            case R.id.travel_tv_more :
                openActivity(TravelsActivity.class);
                break;
            case R.id.statuses_tv_more :
                openActivity(StatusesActivity.class);
                break;
            case R.id.iv_write_status :
                openActivity(WriteStatusActivity.class);
                break;
            default :
                openActivity(TravelActivity.class);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("travel", mTravels.get(i + 3));
        openActivity(TravelActivity.class, bundle);
    }

    private class LeaderViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;

        public LeaderViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<Fragment>();
            fragments.add(LeaderFragment.get(0));
            fragments.add(LeaderFragment.get(4));
            fragments.add(LeaderFragment.get(8));
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    private class TravelListViewAdapter extends CommonAdapter<Travel> {
        @Override
        public int getCount() {
            return mDatas.size() > 3 ? Math.min(2, mDatas.size() - 3) : 0;
        }

        @Override
        public Travel getItem(int position) {
            return mDatas.size() > 3 ? mDatas.get(position + 3) : null;
        }

        public TravelListViewAdapter(Context context, List<Travel> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(ViewHolder holder, Travel travel) {
            holder.setText(R.id.home_travel_title, travel.getTitle())
                    .setText(R.id.home_travel_text, travel.getContent())
                    .setText(R.id.home_travel_update, "最后更新：" + travel.getDuration() + "分钟前");
            if (travel.getMemberInfo() != null) {
                holder.setText(R.id.home_travel_user, travel.getMemberInfo().getMember_name())
                        .loadDefaultImage(R.id.home_travel_avatar, travel.getMemberInfo().getMember_avatar());
            }
        }
    }
}
