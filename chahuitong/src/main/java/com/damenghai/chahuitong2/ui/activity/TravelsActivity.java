package com.damenghai.chahuitong2.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.damenghai.chahuitong2.api.EventAPI;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.TravelListAdapter;
import com.damenghai.chahuitong2.bean.Travel;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.view.TopBar;
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
    private final int REQUEST_CODE_INITIATE = 0x300;

    private PullToRefreshListView mPlv;
    private TopBar mTopBar;

    private TravelListAdapter mAdapter;
    private ArrayList<Travel> mTravels;

    private int mCurrPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels);

        bindView();
        initView();
        loadData(1);
    }

    @Override
    protected void bindView() {
        mTopBar = (TopBar) findViewById(R.id.event_bar);
        mPlv = (PullToRefreshListView) findViewById(R.id.travels_lv);
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

        mTravels = new ArrayList<Travel>();
        mAdapter = new TravelListAdapter(this, mTravels, R.layout.listview_item_travel);
        mPlv.setAdapter(mAdapter);
        mPlv.setOnItemClickListener(this);
        mPlv.setOnRefreshListener(this);
        mPlv.setOnLastItemVisibleListener(this);
    }

    private void loadData(final int page) {
        EventAPI.travelShow(this, page, new VolleyRequest() {

            @Override
            public void onListSuccess(JSONArray array) {
                super.onListSuccess(array);

                if (page == 1) mTravels.clear();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_CANCELED) return;

        if(requestCode == REQUEST_CODE_INITIATE && resultCode == Activity.RESULT_OK) {
            mPlv.setRefreshing();
        }
    }
}
