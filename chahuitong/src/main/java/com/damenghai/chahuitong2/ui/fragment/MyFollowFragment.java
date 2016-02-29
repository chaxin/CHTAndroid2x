package com.damenghai.chahuitong2.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.BaseListAdapter;
import com.damenghai.chahuitong2.api.AccountApi;
import com.damenghai.chahuitong2.base.BaseFragment;
import com.damenghai.chahuitong2.bean.Leader;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.ui.activity.LeaderActivity;
import com.damenghai.chahuitong2.utils.DensityUtils;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.utils.ViewHolder;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/9/18.
 */
public class MyFollowFragment extends BaseFragment {
    private View mView;
    private PullToRefreshListView mPlv;
    private ListViewAdapter mAdapter;
    private List<Leader> mData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list, null);

        mPlv = (PullToRefreshListView) mView.findViewById(R.id.fragment_list);
        mData = new ArrayList<Leader>();
        mAdapter = new ListViewAdapter(getActivity(), mData, R.layout.listview_item_leader);
        mPlv.setAdapter(mAdapter);
        mPlv.getRefreshableView().setDividerHeight(DensityUtils.dp2px(getActivity(), 6));
        mPlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                Leader leader = mData.get(i - 1);
                leader.setBeInstered(1);
                bundle.putSerializable("leader", leader);
                openActivity(LeaderActivity.class, bundle);
            }
        });

        loadData();

        return mView;
    }

    private void loadData() {
        AccountApi.myFollowShow(getActivity(), 1, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("code") != 404) {
                        JSONArray array = obj.getJSONArray("content");
                        for (int i = 0; i < array.length(); i++) {
                            Leader leader = new Gson().fromJson(array.get(i).toString(), Leader.class);
                            if (!mData.contains(leader)) mData.add(leader);
                        }
                    } else {
                        T.showShort(getActivity(), obj.getString("content"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private class ListViewAdapter extends BaseListAdapter<Leader> {

        public ListViewAdapter(Context context, List<Leader> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(final ViewHolder holder, final Leader leader) {
            holder.loadDefaultImage(R.id.leader_avatar, !leader.getMember_avatar().equals("null") ? leader.getMember_avatar() : "")
                    .setText(R.id.leader_name, leader.getMember_name())
                    .setText(R.id.leader_title, leader.getRank().replaceAll("[</*[a-z]+>]", ""))
                    .setVisibility(R.id.leader_favorites, View.GONE);
        }
    }

}
