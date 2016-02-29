package com.damenghai.chahuitong2.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.StatusesAdapter;
import com.damenghai.chahuitong2.api.StatusAPI;
import com.damenghai.chahuitong2.base.BaseFragment;
import com.damenghai.chahuitong2.bean.Status;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.ui.activity.StatusDetailActivity;
import com.damenghai.chahuitong2.utils.DensityUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Sgun on 15/8/26.
 */
public class MyStatusFragment extends BaseFragment implements OnRefreshListener, OnLastItemVisibleListener, OnItemClickListener {
    private ArrayList<Status> mData;
    private StatusesAdapter mAdapter;
    private PullToRefreshListView mListView;
    private int mCurrPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_list, null);

        mListView = (PullToRefreshListView) mView.findViewById(R.id.fragment_list);
        mListView.getRefreshableView().setDividerHeight(DensityUtils.dp2px(getActivity(), 4));
        mData = new ArrayList<Status>();
        mAdapter = new StatusesAdapter(getActivity(), mData, R.layout.listview_item_status, false);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("status", mData.get(i));
            }
        });
        mListView.setOnLastItemVisibleListener(this);
        mListView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);

        loadData(1);

        return mView;
    }

    private void loadData(final int page) {
        StatusAPI.myStatusShow(getActivity(), page, new VolleyRequest() {
            @Override
            public void onListSuccess(JSONArray array) {
                super.onListSuccess(array);
                if (page == 1) mData.clear();

                mCurrPage = page;

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
                mListView.onRefreshComplete();
            }
        });
    }

    @Override
    public void onLastItemVisible() {
        loadData(mCurrPage);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        loadData(1);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("status", mData.get(i - 1));
        openActivity(StatusDetailActivity.class, bundle);
    }
}
