package com.damenghai.chahuitong.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.damenghai.chahuitong.api.HodorRequest;
import com.damenghai.chahuitong.base.BaseFragment;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.bean.Leader;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.ui.activity.LeaderActivity;
import com.damenghai.chahuitong.utils.DensityUtils;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.utils.ViewHolder;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/8/22.
 */
public class LeaderFragment extends BaseFragment {
    private final String KEY_INDEX = "LeaderFragment:index";

    private PullToRefreshListView mPlv;
    private ListViewAdapter mAdapter;
    private List<Leader> mData;

    private int mDataIndex;

    public static LeaderFragment get(int index) {
        LeaderFragment fragment = new LeaderFragment();
        fragment.mDataIndex = index;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);

        mPlv = (PullToRefreshListView) view.findViewById(R.id.common_listview);
        mData = new ArrayList<Leader>();
        mAdapter = new ListViewAdapter(getActivity(), mData, R.layout.listview_item_leader);
        mPlv.setAdapter(mAdapter);
        mPlv.getRefreshableView().setDividerHeight(DensityUtils.dp2px(getActivity(), 1));
        mPlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("leader", mData.get(i - 1));
                openActivity(LeaderActivity.class, bundle);
            }
        });

        initData();

        return view;
    }

    private void initData() {
        HodorRequest.followShow(getActivity(), 0, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("code") != 404) {
                        JSONArray array = obj.getJSONArray("content");
                        for (int i = mDataIndex; i < mDataIndex + 4; i++) {
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, mDataIndex);
    }

    private class ListViewAdapter extends CommonAdapter<Leader> {

        public ListViewAdapter(Context context, List<Leader> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(final ViewHolder holder, final Leader leader) {
            holder.loadAvatarImage(R.id.leader_avatar, !leader.getMember_avatar().equals("null") ? leader.getMember_avatar() : "")
                    .setText(R.id.leader_name, leader.getMember_name())
                    .setText(R.id.leader_title, leader.getRank().replaceAll("[</*[a-z]+>]", ""));

            if(leader.getBeInstered() == 1) {
                holder.setText(R.id.leader_favorites, "已关注")
                        .setTextDrawableTop(R.id.leader_favorites, R.drawable.icon_forum_favorites)
                        .setTextOnClickListener(R.id.leader_favorites, new UnFollowListener(leader, mContext, holder));
            } else {
                holder.setText(R.id.leader_favorites, "加关注")
                        .setTextDrawableTop(R.id.leader_favorites, R.drawable.icon_forum_unfavorites)
                        .setTextOnClickListener(R.id.leader_favorites, new FollowListener(leader, mContext, holder));
            }
        }
    }

    private class FollowListener implements View.OnClickListener {
        private Leader leader;
        private Context mContext;
        private ViewHolder holder;

        public FollowListener(Leader leader, Context mContext, ViewHolder holder) {
            this.leader = leader;
            this.mContext = mContext;
            this.holder = holder;
        }

        @Override
        public void onClick(View view) {
            HodorRequest.addFollow(mContext, leader.getMember_id(), new VolleyRequest() {
                @Override
                public void onSuccess(String response) {
                    super.onSuccess(response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getInt("code") == 404) {
                            T.showShort(mContext, obj.getString("content"));
                        } else {
                            T.showShort(mContext, "关注成功");
                            holder.setText(R.id.leader_favorites, "已关注")
                                    .setTextDrawableTop(R.id.leader_favorites, R.drawable.icon_forum_favorites);
                            holder.setTextOnClickListener(R.id.leader_favorites, new UnFollowListener(leader, mContext, holder));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private class UnFollowListener implements View.OnClickListener {
        private Leader leader;
        private Context mContext;
        private ViewHolder holder;

        public UnFollowListener(Leader leader, Context mContext, ViewHolder holder) {
            this.leader = leader;
            this.mContext = mContext;
            this.holder = holder;
        }

        @Override
        public void onClick(View view) {
            HodorRequest.removeFollow(mContext, leader.getMember_id(), new VolleyRequest() {
                @Override
                public void onSuccess(String response) {
                    super.onSuccess(response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getInt("code") == 404) {
                            T.showShort(mContext, obj.getString("content"));
                        } else {
                            T.showShort(mContext, "取消成功");
                            holder.setText(R.id.leader_favorites, "加关注")
                                    .setTextDrawableTop(R.id.leader_favorites, R.drawable.icon_forum_unfavorites)
                                    .setTextOnClickListener(R.id.leader_favorites, new FollowListener(leader, mContext, holder));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
