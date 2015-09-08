package com.damenghai.chahuitong.ui.activity;

import android.os.Bundle;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.StatusesAdapter;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.bean.Status;
import com.damenghai.chahuitong.request.VolleyRequest;
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

        loadDatas(1);
    }
    private void findViewById() {
        mLv = (PullToRefreshListView) findViewById(R.id.statuses_lv);
    }

    private void initView() {
        mStatuses = new ArrayList<Status>();
        mAdapter = new StatusesAdapter(this, mStatuses, R.layout.listview_item_status, true);
        mLv.setAdapter(mAdapter);
        mLv.setOnRefreshListener(this);
        mLv.setOnLastItemVisibleListener(this);
    }

    private void loadDatas(final int page) {
        HodorAPI.statusShow(page, new VolleyRequest() {
            @Override
            public void onListSuccess(JSONArray array) {
                super.onListSuccess(array);
                if(page == 1) mStatuses.clear();

                mCurrPage = page;

                for(int i=0; i<array.length(); i++) {
                    try {
                        Status status = new Gson().fromJson(array.getString(i), Status.class);
                        if(!mStatuses.contains(status)) mStatuses.add(status);
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
    public void onRefresh(PullToRefreshBase refreshView) {
        loadDatas(1);
    }

    @Override
    public void onLastItemVisible() {
        loadDatas(mCurrPage + 1);
    }
}
