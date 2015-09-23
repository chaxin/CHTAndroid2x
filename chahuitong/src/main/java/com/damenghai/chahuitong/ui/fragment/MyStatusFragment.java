package com.damenghai.chahuitong.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.StatusesAdapter;
import com.damenghai.chahuitong.api.HodorRequest;
import com.damenghai.chahuitong.base.BaseFragment;
import com.damenghai.chahuitong.bean.Status;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.DensityUtils;
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
public class MyStatusFragment extends BaseFragment implements OnRefreshListener, OnLastItemVisibleListener {
    private ArrayList<Status> mDatas;
    private StatusesAdapter mAdapter;
    private PullToRefreshListView mListView;
    private int mCurrPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_list, null);

        mListView = (PullToRefreshListView) mView.findViewById(R.id.commond_listview);
        mListView.getRefreshableView().setDividerHeight(DensityUtils.dp2px(getActivity(), 4));
        mDatas = new ArrayList<Status>();
        mAdapter = new StatusesAdapter(getActivity(), mDatas, R.layout.listview_item_status, false);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("status", mDatas.get(i));
            }
        });
        mListView.setOnLastItemVisibleListener(this);
        mListView.setOnRefreshListener(this);

        loadDatas(1);

        return mView;
    }

    private void loadDatas(final int page) {
        HodorRequest.myStatusShow(getActivity(), page, new VolleyRequest() {
            @Override
            public void onListSuccess(JSONArray array) {
                super.onListSuccess(array);
                if (page == 1) mDatas.clear();

                mCurrPage = page;

                try {
                    for (int i = 0; i < array.length(); i++) {
                        Status status = new Gson().fromJson(array.get(i).toString(), Status.class);
                        if (!mDatas.contains(status)) mDatas.add(status);
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
        loadDatas(mCurrPage);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        loadDatas(1);
    }
}
