package com.damenghai.chahuitong2.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.damenghai.chahuitong2.api.AccountApi;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.BaseListAdapter;
import com.damenghai.chahuitong2.bean.Leader;
import com.damenghai.chahuitong2.listener.FollowListener;
import com.damenghai.chahuitong2.listener.UnFollowListener;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.utils.L;
import com.damenghai.chahuitong2.utils.ViewHolder;
import com.damenghai.chahuitong2.view.TopBar;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/8/23.
 */
public class LeadersActivity extends BaseActivity implements OnRefreshListener, OnLastItemVisibleListener {
    private TopBar mTopBar;
    private PullToRefreshGridView mPgv;
    private GridViewAdapter mAdapter;
    private ArrayList<Leader> mDatas;

    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaders);

        bindView();

        initView();

        loadData(1);
    }

    @Override
    protected void bindView() {
        mTopBar = (TopBar) findViewById(R.id.leaders_bar);
        mPgv = (PullToRefreshGridView) findViewById(R.id.leader_list_gv);
    }

    @Override
    protected void initView() {
        mTopBar.setOnLeftClickListener(new TopBar.OnLeftClickListener() {
            @Override
            public void onLeftClick() {
                finishActivity();
            }
        });

        mTopBar.setOnRightClickListener(new TopBar.onRightClickListener() {
            @Override
            public void onRightClick() {
                Intent intent = new Intent(LeadersActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        mDatas = new ArrayList<Leader>();
        mAdapter = new GridViewAdapter(this, mDatas, R.layout.gridview_item_leader_list);
        mPgv.setAdapter(mAdapter);
        mPgv.setOnRefreshListener(this);
        mPgv.setOnLastItemVisibleListener(this);
        mPgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Leader leader = mDatas.get(i);
                Bundle bundle = new Bundle();
                bundle.putSerializable("leader", leader);
                openActivity(LeaderActivity.class, bundle);
            }
        });
    }

    private void loadData(final int page) {
        AccountApi.followShow(this, page, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                mCurrentPage = page;

                if (page == 1) {
                    mDatas.clear();
                }

                try {
                    JSONObject obj = new JSONObject(response);
                    L.d(obj.toString());
                    JSONArray array = obj.getJSONArray("content");
                    for (int i = 0; i < array.length(); i++) {
                        Leader leader = new Gson().fromJson(array.get(i).toString(), Leader.class);
                        if (!mDatas.contains(leader)) mDatas.add(leader);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAllDone() {
                super.onAllDone();
                mPgv.onRefreshComplete();
            }
        });
    }

    @Override
    public void onLastItemVisible() {
        loadData(mCurrentPage + 1);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        loadData(1);
    }

    private class GridViewAdapter extends BaseListAdapter<Leader> {

        public GridViewAdapter(Context context, List<Leader> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @SuppressLint("NewApi")
        @Override
        public void convert(final ViewHolder holder, final Leader leader) {
            holder.setText(R.id.leader_item_name, leader.getMember_name())
                    .setText(R.id.leader_item_title, leader.getRank().replaceAll("[</*[a-z]+>]", ""))
                    .setText(R.id.leader_item_favorites, leader.getFans() + " 关注")
                    .loadDefaultImage(R.id.leader_item_avatar, leader.getMember_avatar());
            if(leader.getBeInstered() == 1) {
                holder.setText(R.id.leader_item_follow, "取消关注")
                        .setTextColor(R.id.leader_item_follow, R.color.primary)
                        .setTextDrawableLeft(R.id.leader_item_follow, R.drawable.icon_followed)
                        .setOnClickListener(R.id.leader_item_follow, new UnFollowListener(mContext, leader, holder));
            } else {
                holder.setText(R.id.leader_item_follow, "加关注")
                        .setTextColor(R.id.leader_item_follow, android.R.color.black)
                        .setTextDrawableLeft(R.id.leader_item_follow, R.drawable.icon_unfollowed)
                        .setTextOnClickListener(R.id.leader_item_follow, new FollowListener(mContext, leader, holder));
            }
        }
    }
}
