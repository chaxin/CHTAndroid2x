package com.damenghai.chahuitong.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.StatusesAdapter;
import com.damenghai.chahuitong.api.HodorRequest;
import com.damenghai.chahuitong.api.StatusAPI;
import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.bean.Status;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.view.TopBar;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Sgun on 15/8/23.
 */
public class StatusesActivity extends BaseActivity implements OnRefreshListener, OnLastItemVisibleListener {
    private int REQUEST_CODE_WRITE = 1;

    private TopBar mTopBar;
    private PullToRefreshListView mLv;

    private StatusesAdapter mAdapter;
    private ArrayList<Status> mStatuses;

    private int mCurrPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        findViewById();

        initView();

        loadData(1);
    }

    @Override
    protected void findViewById() {
        mTopBar = (TopBar) findViewById(R.id.statuses_bar);
        mLv = (PullToRefreshListView) findViewById(R.id.statuses_lv);
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
                if(!TextUtils.isEmpty(SessionKeeper.readSession(StatusesActivity.this))) {
                    openActivityForResult(WriteStatusActivity.class, REQUEST_CODE_WRITE);
                } else {
                    openActivity(LoginActivity.class);
                }
            }
        });

        mStatuses = new ArrayList<Status>();
        mAdapter = new StatusesAdapter(this, mStatuses, R.layout.listview_item_status, true);
        mLv.setAdapter(mAdapter);
        mLv.setOnRefreshListener(this);
        mLv.setOnLastItemVisibleListener(this);
    }

    private void loadData(final int page) {
        StatusAPI.statusShow(page, new VolleyRequest() {
            @Override
            public void onListSuccess(JSONArray array) {
                super.onListSuccess(array);
                if (page == 1) mStatuses.clear();

                mCurrPage = page;

                for (int i = 0; i < array.length(); i++) {
                    try {
                        Status status = new Gson().fromJson(array.getString(i), Status.class);
                        if (!mStatuses.contains(status)) mStatuses.add(status);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAllDone() {
                super.onAllDone();
                mLv.onRefreshComplete();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_WRITE && resultCode == Activity.RESULT_OK) {
            loadData(1);
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        loadData(1);
    }

    @Override
    public void onLastItemVisible() {
        loadData(mCurrPage + 1);
    }

}
