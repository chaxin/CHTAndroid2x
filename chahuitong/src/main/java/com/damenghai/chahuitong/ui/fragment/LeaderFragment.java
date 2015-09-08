package com.damenghai.chahuitong.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.damenghai.chahuitong.base.BaseFragment;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.bean.Leader;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.ui.activity.LeaderActivity;
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
    private final String KEY_INDEX = "LeaderFragment:isList";

    private String mKey;
    private String mUsername;

    private View mView;
    private PullToRefreshListView mListView;
    private ListViewAdapter mAdapter;
    private List<Leader> mDatas;

    private int mDataIndex;

    public static LeaderFragment get(int index) {
        LeaderFragment fragment = new LeaderFragment();
        fragment.mDataIndex = index;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_INDEX)) {
            mDataIndex = savedInstanceState.getInt(KEY_INDEX);
        }

        mKey = SessionKeeper.readSession(getActivity());
        mUsername = SessionKeeper.readUsername(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list, null);

        mListView = (PullToRefreshListView) mView.findViewById(R.id.commond_listview);
        mDatas = new ArrayList<Leader>();
        initData();
        mAdapter = new ListViewAdapter(getActivity(), mDatas, R.layout.listview_item_leader);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("leader", mDatas.get(i - 1));
                openActivity(LeaderActivity.class, bundle);
            }
        });

        return mView;
    }

    private void initData() {
        if(mDataIndex >= 0) {
            HodorAPI.followShow(0, mKey, mUsername, new VolleyRequest() {
                @Override
                public void onSuccess(String response) {
                    super.onSuccess(response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getInt("code") != 404) {
                            JSONArray array = obj.getJSONArray("content");
                            for (int i = mDataIndex; i < mDataIndex + 4; i++) {
                                Leader leader = new Gson().fromJson(array.get(i).toString(), Leader.class);
                                if (!mDatas.contains(leader)) mDatas.add(leader);
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
        } else {
            HodorAPI.myFollowShow(1, mKey, mUsername, new VolleyRequest() {
                @Override
                public void onSuccess(String response) {
                    super.onSuccess(response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getInt("code") != 404) {
                            JSONArray array = obj.getJSONArray("content");
                            for (int i = 0; i < array.length(); i++) {
                                Leader leader = new Gson().fromJson(array.get(i).toString(), Leader.class);
                                if (!mDatas.contains(leader)) mDatas.add(leader);
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
            holder.loadDefaultImage(R.id.leader_avatar, !leader.getMember_avatar().equals("null") ? leader.getMember_avatar() : "")
                    .setText(R.id.leader_name, leader.getMember_name())
                    .setText(R.id.leader_title, leader.getRank().replaceAll("[</*[a-z]+>]", ""));

            if(leader.getBeInstered() == 1) {
                holder.setText(R.id.leader_favorites, "已关注")
                        .setTextDrawableTop(R.id.leader_favorites, R.drawable.icon_forum_favorites)
                        .setTextOnClickListener(R.id.leader_favorites, new RemoveFavoritesListener(leader, mContext, holder));
            } else {
                holder.setText(R.id.leader_favorites, "加关注")
                        .setTextDrawableTop(R.id.leader_favorites, R.drawable.icon_forum_unfavorites)
                        .setTextOnClickListener(R.id.leader_favorites, new AddFavoritesListener(leader, mContext, holder));
            }
        }
    }

    public class RemoveFavoritesListener implements View.OnClickListener {
        private Leader leader;
        private Context mContext;
        private ViewHolder holder;

        public RemoveFavoritesListener(Leader leader, Context mContext, ViewHolder holder) {
            this.leader = leader;
            this.mContext = mContext;
            this.holder = holder;
        }

        @Override
        public void onClick(View view) {
            HodorAPI.removeFollow(leader.getMember_id(), SessionKeeper.readSession(mContext),
                    SessionKeeper.readUsername(mContext), new VolleyRequest() {
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
                                            .setTextOnClickListener(R.id.leader_favorites, new AddFavoritesListener(leader, mContext, holder));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    public class AddFavoritesListener implements View.OnClickListener {
        private Leader leader;
        private Context mContext;
        private ViewHolder holder;

        public AddFavoritesListener(Leader leader, Context mContext, ViewHolder holder) {
            this.leader = leader;
            this.mContext = mContext;
            this.holder = holder;
        }

        @Override
        public void onClick(View view) {
            HodorAPI.addFollow(mContext, leader.getMember_id(), new VolleyRequest() {
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
                            holder.setTextOnClickListener(R.id.leader_favorites, new RemoveFavoritesListener(leader, mContext, holder));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                    });
        }
    }
}
