package com.damenghai.chahuitong.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.StatusesAdapter;
import com.damenghai.chahuitong.api.HodorRequest;
import com.damenghai.chahuitong.api.StatusAPI;
import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.bean.Leader;
import com.damenghai.chahuitong.bean.Status;
import com.damenghai.chahuitong.listener.FollowListener;
import com.damenghai.chahuitong.listener.UnFollowListener;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.view.LastRefreshListView;
import com.damenghai.chahuitong.view.LastRefreshListView.OnLastRefreshListener;
import com.damenghai.chahuitong.view.RoundImageView;
import com.damenghai.chahuitong.view.TopBar;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sgun on 15/8/25.
 */
public class LeaderActivity extends BaseActivity implements OnClickListener, OnLastRefreshListener {
    private Leader mLeader;

    private View mHeader;

    private TopBar mTopBar;
    private RoundImageView mAvatar;
    private TextView mName;
    private TextView mTitle;
    private TextView mFollowers;
    private TextView mFollow;
    private LastRefreshListView mLlv;

    private ArrayList<Status> mData;
    private StatusesAdapter mAdapter;

    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader);

        mLeader = (Leader) getIntent().getSerializableExtra("leader");

        findViewById();

        initView();

        loadData(1);
    }

    @Override
    protected void findViewById() {
        mHeader = View.inflate(this, R.layout.include_leader_header, null);
        mTopBar = (TopBar) findViewById(R.id.leader_bar);
        mAvatar = (RoundImageView) mHeader.findViewById(R.id.leader_avatar);
        mName = (TextView) mHeader.findViewById(R.id.leader_detail_name);
        mTitle = (TextView) mHeader.findViewById(R.id.leader_detail_title);
        mFollowers = (TextView) mHeader.findViewById(R.id.leader_detail_followers);
        mFollow = (TextView) mHeader.findViewById(R.id.leader_detail_follow);
        mLlv = (LastRefreshListView) findViewById(R.id.leader_lv_detail);
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

            }
        });

        mData = new ArrayList<Status>();
        mAdapter = new StatusesAdapter(this, mData, R.layout.listview_item_status, false);
        mLlv.setAdapter(mAdapter);
        mLlv.addHeaderView(mHeader);
        mLlv.setOnLastRefreshListener(this);

        if (mLeader != null) {
            mName.setText(mLeader.getMember_name());
            mTitle.setText(mLeader.getRank().replaceAll("</*[a-z]+>", ""));
            mFollowers.setText(mLeader.getGuanzhu() + " 关注");
            if (mLeader.getBeInstered() > 0) {
                mFollow.setText("取消关注");
                mFollow.setTextColor(getResources().getColor(R.color.primary));
                Drawable drawable = getResources().getDrawable(R.drawable.icon_followed);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                mFollow.setCompoundDrawables(drawable, null, null, null);
                mFollow.setOnClickListener(new UnFollowListener(LeaderActivity.this, mLeader, mFollow));
            } else {
                mFollow.setText("加关注");
                mFollow.setTextColor(getResources().getColor(android.R.color.black));
                Drawable drawable = getResources().getDrawable(R.drawable.icon_unfollowed);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                mFollow.setCompoundDrawables(drawable, null, null, null);
                mFollow.setOnClickListener(new FollowListener(LeaderActivity.this, mLeader, mFollow));
            }

            BitmapUtils util = new BitmapUtils(this, this.getCacheDir().getAbsolutePath());
            util.configDefaultLoadingImage(R.drawable.default_load_image);
            util.configDefaultLoadFailedImage(R.drawable.default_load_image);
            util.display(mAvatar, !mLeader.getMember_avatar().equals("null") ? mLeader.getMember_avatar() : "");
        }
    }

    private void loadData(final int page) {
        if (mLeader == null) return;
        StatusAPI.leaderStatus(mLeader.getMember_id(), page, new VolleyRequest() {
            @Override
            public void onListSuccess(JSONArray array) {
                super.onListSuccess(array);

                mCurrentPage = page;

                if (page == 1) mData.clear();

                try {
                    for (int i = 0; i < array.length(); i++) {
                        Status status = new Gson().fromJson(array.get(i).toString(), Status.class);
                        if (!mData.contains(status)) mData.add(status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAllDone() {
                super.onAllDone();
                mLlv.refreshComplete();
            }
        });
    }

    @Override
    public void onClick(View view) {
        HodorRequest.addFollow(this, mLeader.getMember_id(), new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    T.showShort(LeaderActivity.this, obj.getInt("code") == 404 ? obj.getString("content") : "关注成功");
                    if (obj.getInt("code") != 404) {
                        mFollow.setText("已关注");
                        mFollow.setTextColor(getResources().getColor(R.color.background));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        loadData(mCurrentPage + 1);
    }
}
