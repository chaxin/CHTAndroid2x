package com.damenghai.chahuitong.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.damenghai.chahuitong.BaseFragment;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.bean.Leader;
import com.damenghai.chahuitong.bean.response.LeaderResponse;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.ui.activity.LeaderActivity;
import com.damenghai.chahuitong.utils.ViewHolder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/8/22.
 */
public class LeaderFragment extends BaseFragment {
    private View mView;
    private ListView mListView;
    private ListViewAdapter mAdapter;
    private List<Leader> mDatas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list, null);

        mListView = (ListView) mView.findViewById(R.id.commond_listview);
        mDatas = new ArrayList<Leader>();
        initDatas();
        mAdapter = new ListViewAdapter(getActivity(), mDatas, R.layout.listview_item_leader);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("leader", mDatas.get(i));
                openActivity(LeaderActivity.class, bundle);
            }
        });

        return mView;
    }

    private void initDatas() {
        HodorAPI.myFollowShow(SessionKeeper.readSession(getActivity()), SessionKeeper.readUsername(getActivity()), new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                LeaderResponse leaderResponse = new Gson().fromJson(response, LeaderResponse.class);
                addDatas(leaderResponse.getContent());
            }
        });

//        Leader leader1 = new Leader();
//        leader1.setName("周杰伦");
//        leader1.setTitle("中国茶学院院长\n中国茶叶流通协会常务副会长");
//        leader1.setFavorites(true);
//        mDatas.add(leader1);
//
//        Leader leader2 = new Leader();
//        leader2.setName("王心凌");
//        leader2.setTitle("著名茶叶专栏作家\n茶友会《茶友汇》栏目主持");
//        leader2.setFavorites(false);
//        mDatas.add(leader2);
//
//        Leader leader3 = new Leader();
//        leader3.setName("陈奕迅");
//        leader3.setTitle("中国精神病学院荣誉会长\n中国茶叶流通协会常务副会长");
//        leader3.setFavorites(true);
//        mDatas.add(leader3);
//
//        Leader leader4 = new Leader();
//        leader4.setName("罗永浩");
//        leader4.setTitle("著名茶叶专栏作家\n茶友会《茶友汇》栏目主持");
//        leader4.setFavorites(false);
//        mDatas.add(leader4);

    }

    private void addDatas(ArrayList<Leader> leaders) {
        for(int i=0; i<leaders.size(); i++) {
            Leader leader = leaders.get(i);
            if(!mDatas.contains(leader)) mDatas.add(leader);
        }

        mAdapter.notifyDataSetChanged();
    }

    private class ListViewAdapter extends CommonAdapter<Leader> {

        public ListViewAdapter(Context context, List<Leader> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(ViewHolder holder, Leader leader) {
            holder.setText(R.id.leader_name, leader.getName())
                    .setText(R.id.leader_title, leader.getTitle())
                    .setTextDrawableTop(R.id.leader_favorites, leader.isFavorites() ? R.drawable.icon_forum_favorites : R.drawable.icon_forum_unfavorites)
                    .setText(R.id.leader_favorites, leader.isFavorites() ? "已关注" : "关注他");
        }
    }
}
