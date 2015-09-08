package com.damenghai.chahuitong.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.TravelListAdapter;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.bean.Travel;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.view.TopBar;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Sgun on 15/8/23.
 */
public class TravelsActivity extends BaseActivity implements OnItemClickListener, OnRefreshListener, OnLastItemVisibleListener {
    private PullToRefreshListView mPlv;
    private TopBar mTopBar;

    private TravelListAdapter mAdapter;
    private ArrayList<Travel> mTravels;

    private int mCurrPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels);

        findViewById();
        initView();
        loadData(1);
    }

    private void findViewById() {
        mTopBar = (TopBar) findViewById(R.id.event_bar);
        mPlv = (PullToRefreshListView) findViewById(R.id.travels_lv);
    }

    private void initView() {
        mTopBar.setOnRightClickListener(new TopBar.onRightClickListener() {
            @Override
            public void onRightClick() {
                openActivity(InitiateEventActivity.class);
            }
        });

        mTravels = new ArrayList<Travel>();
        mAdapter = new TravelListAdapter(this, mTravels, R.layout.listview_item_travel);
        mPlv.setAdapter(mAdapter);
        mPlv.setOnItemClickListener(this);
        mPlv.setOnRefreshListener(this);
        mPlv.setOnLastItemVisibleListener(this);
    }

    private void loadData(final int page) {
        HodorAPI.travelShow(page, new VolleyRequest() {
            @Override
            public void onListSuccess(JSONArray array) {
                super.onListSuccess(array);

                if(page == 1) mTravels.clear();
                mCurrPage = page;

                try {
                    for (int i = 0; i < array.length(); i++) {
                        Travel travel = new Gson().fromJson(array.getString(i), Travel.class);
                        if (!mTravels.contains(travel)) mTravels.add(travel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                T.showShort(TravelsActivity.this, error);
            }

            @Override
            public void onAllDone() {
                super.onAllDone();
                mPlv.onRefreshComplete();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("travel", mTravels.get(i - 1));
        openActivity(TravelActivity.class, bundle);
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
