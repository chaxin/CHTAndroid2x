package com.damenghai.chahuitong.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.bean.Leader;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.utils.ViewHolder;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/8/23.
 */
public class LeadersActivity extends BaseActivity {
    private GridView mGv;
    private GridViewAdapter mAdapter;
    private ArrayList<Leader> mDatas;

    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaders);

        findViewById();

        initView();

        loadDatas();
    }

    private void findViewById() {
        mGv = (GridView) findViewById(R.id.leader_list_gv);
    }

    private void initView() {
        mDatas = new ArrayList<Leader>();
        mAdapter = new GridViewAdapter(this, mDatas, R.layout.gridview_item_leader_list);
        mGv.setAdapter(mAdapter);
        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Leader leader = mDatas.get(i);
                Bundle bundle = new Bundle();
                bundle.putSerializable("leader", leader);
                openActivity(LeaderActivity.class, bundle);
            }
        });
    }

    private void loadDatas() {
        HodorAPI.followShow(1, SessionKeeper.readSession(this), SessionKeeper.readUsername(this), new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray array = obj.getJSONArray("content");
                    for(int i=0; i<array.length(); i++) {
                        Leader leader = new Gson().fromJson(array.get(i).toString(), Leader.class);
                        if(!mDatas.contains(leader)) mDatas.add(leader);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private class GridViewAdapter extends CommonAdapter<Leader> {

        public GridViewAdapter(Context context, List<Leader> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @SuppressLint("NewApi")
        @Override
        public void convert(final ViewHolder holder, final Leader leader) {
            holder.setText(R.id.leader_item_name, leader.getMember_name())
                    .setText(R.id.leader_item_title, leader.getRank().replaceAll("[</*[a-z]+>]", ""))
                    .setText(R.id.leader_item_favorites, leader.getGuanzhu() + " 关注")
                    .loadDefaultImage(R.id.leader_item_avatar, leader.getMember_avatar());
            if(leader.getBeInstered() == 1) {
                holder.setTextColor(R.id.leader_item_follow, R.color.primary)
                        .setTextDrawableLeft(R.id.leader_item_follow, R.drawable.icon_followed);
            } else {
                holder.setTextColor(R.id.leader_item_follow, android.R.color.black)
                        .setTextDrawableLeft(R.id.leader_item_follow, R.drawable.icon_unfollowed)
                        .setTextOnClickListener(R.id.leader_item_follow, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                HodorAPI.addFollow(mContext, leader.getMember_id(), new VolleyRequest() {
                                    @Override
                                    public void onSuccess(String response) {
                                        super.onSuccess(response);
                                        try {
                                            JSONObject obj = new JSONObject(response);
                                            if(obj.getInt("code") != 404) {
                                                holder.setTextColor(R.id.leader_item_follow, R.color.primary)
                                                        .setTextDrawableLeft(R.id.leader_item_follow, R.drawable.icon_followed);
                                                leader.setBeInstered(1);
                                                T.showShort(mContext, "关注成功");
                                            } else {
                                                T.showShort(mContext, obj.getString("content"));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
            }
        }
    }
}
