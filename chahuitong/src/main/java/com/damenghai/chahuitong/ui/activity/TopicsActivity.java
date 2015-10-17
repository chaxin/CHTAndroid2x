package com.damenghai.chahuitong.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.StatusesAdapter;
import com.damenghai.chahuitong.api.StatusAPI;
import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.bean.Leader;
import com.damenghai.chahuitong.bean.Status;
import com.damenghai.chahuitong.bean.response.TopicResponse;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.utils.ViewHolder;
import com.damenghai.chahuitong.view.TopBar;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/8/23.
 */
public class TopicsActivity extends BaseActivity implements OnLastItemVisibleListener, OnRefreshListener, AdapterView.OnItemClickListener {
    private TopBar mTopBar;
    private PullToRefreshListView mPlv;

    private ArrayList<Status> mTopics;
    private ListViewAdapter mLvAdapter;
    private int mCurrPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        findViewById();

        initView();

        loadData(1);
    }

    @Override
    protected void findViewById() {
        mTopBar = (TopBar) findViewById(R.id.topics_top_bar);
        mPlv = (PullToRefreshListView) findViewById(R.id.topics_lv);
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
                Intent intent = new Intent(TopicsActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        mTopics = new ArrayList<Status>();
        mLvAdapter = new ListViewAdapter(this, mTopics, R.layout.listview_item_topic);
        mPlv.setAdapter(mLvAdapter);
        mPlv.setOnLastItemVisibleListener(this);
        mPlv.setOnRefreshListener(this);
    }

    private void loadData(final int page) {
        StatusAPI.leaderStatus(1, page, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                mCurrPage = page;

                if (page == 1) mTopics.clear();

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("code") != 404) {
                        TopicResponse topicResponse = new Gson().fromJson(obj.toString(), TopicResponse.class);
                        addData(topicResponse);
                    } else {
                        T.showShort(TopicsActivity.this, obj.getString("content"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAllDone() {
                super.onAllDone();
                mPlv.onRefreshComplete();
            }
        });
    }

    private void addData(TopicResponse topicResponse) {
        ArrayList<Status> statuses = topicResponse.getContent();
        Leader leader = topicResponse.getMemberInfo();
        for(Status status : statuses) {
            status.setMemberInfo(leader);
            if(!mTopics.contains(status)) mTopics.add(status);
        }

        mLvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLastItemVisible() {
        loadData(1);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        loadData(mCurrPage);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("status", mTopics.get(i - 1));
        openActivity(StatusDetailActivity.class);
    }

    private class ListViewAdapter extends StatusesAdapter {
        private boolean mIsSet = false;

        public ListViewAdapter(Context context, List<Status> statuses, int resId) {
            super(context, statuses, resId, false);
        }

        @Override
        public void convert(ViewHolder holder, final Status status) {
            setImages(holder, status);

            if(holder.getPosition() == 0 && !mIsSet) {
                holder.setVisibility(R.id.topic_item_top, View.VISIBLE)
                        .setText(R.id.topic_top_title, "今日新声")
                        .setVisibility(R.id.topic_top_desc, View.VISIBLE)
                        .setText(R.id.topic_top_desc, "每日一话");
                mIsSet = true;
            }

            if(holder.getPosition() == 1) {
                holder.setVisibility(R.id.topic_item_top, View.VISIBLE)
                        .setText(R.id.topic_top_title, "往期话题");
            }

            holder.setText(R.id.topic_item_title, status.getTitle())
                    .setText(R.id.topic_item_text, status.getText())
                    .setText(R.id.topic_item_user, "话题主理人：" + status.getMemberInfo().getMember_name())
                    .setText(R.id.control_tv_share, status.getShare() + "")
                    .setText(R.id.control_tv_comment, status.getComment() + "")
                    .setText(R.id.control_tv_like, status.getView() + "")
                    .setOnClickListener(R.id.topic_item_latyou, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("status", status);
                            openActivity(StatusDetailActivity.class, bundle);
                        }
                    });

            setControl(holder, status);
        }
    }
}
