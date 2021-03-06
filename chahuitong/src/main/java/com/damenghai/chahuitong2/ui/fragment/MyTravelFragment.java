package com.damenghai.chahuitong2.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.BaseListAdapter;
import com.damenghai.chahuitong2.api.EventAPI;
import com.damenghai.chahuitong2.base.BaseFragment;
import com.damenghai.chahuitong2.bean.Travel;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.ui.activity.InitiateEventActivity;
import com.damenghai.chahuitong2.ui.activity.TravelActivity;
import com.damenghai.chahuitong2.utils.DateUtils;
import com.damenghai.chahuitong2.utils.DensityUtils;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.utils.ViewHolder;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/8/22.
 */
public class MyTravelFragment extends BaseFragment implements OnRefreshListener, OnLastItemVisibleListener {
    private View mView;
    private PullToRefreshListView mListView;
    private ListViewAdapter mAdapter;
    private List<Travel> mDatas;
    private int mUid;
    private int mCurrPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list, null);

        mListView = (PullToRefreshListView) mView.findViewById(R.id.fragment_list);
        mListView.getRefreshableView().setDividerHeight(DensityUtils.dp2px(getActivity(), 4));
        mDatas = new ArrayList<Travel>();
        mAdapter = new ListViewAdapter(getActivity(), mDatas, R.layout.listview_item_my_travel);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("travel", mDatas.get(i - 1));
                openActivity(TravelActivity.class, bundle);
            }
        });

        mListView.setOnLastItemVisibleListener(this);
        mListView.setOnRefreshListener(this);

        loadData(1);

        return mView;
    }

    private void loadData(final int page) {
        EventAPI.myTravelShow(getActivity(), page, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                if (page == 1) mDatas.clear();

                mCurrPage = page;

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("code") == 404) {
                        T.showShort(getActivity(), object.getString("content"));
                    } else {
                        mUid = object.getInt("uid");
                        JSONArray array = object.getJSONArray("content");
                        for (int i = 0; i < array.length(); i++) {
                            Travel travel = new Gson().fromJson(array.get(i).toString(), Travel.class);
                            if (!mDatas.contains(travel)) mDatas.add(travel);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                T.showShort(getActivity(), error);
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
        loadData(mCurrPage + 1);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        loadData(1);
    }

    private class ListViewAdapter extends BaseListAdapter<Travel> {
        public ListViewAdapter(Context context, List<Travel> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(final ViewHolder holder, final Travel travel) {
            holder.setText(R.id.travel_title, travel.getTitle())
                    .setTextDrawableLeft(R.id.travel_title, travel.getUid() == mUid ? R.drawable.icon_initiator : R.drawable.icon_related)
                    .setText(R.id.travel_desc, travel.getContent())
                    .setText(R.id.travel_date, travel.getJoin_time())
                    .setText(R.id.travel_my_state, DateUtils.smallerCurrentTime(travel.getJoin_time()) ? "已结束" : "已报名")
                    .setOnClickListener(R.id.travel_delete, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EventAPI.deleteEvent(getActivity(), travel.getActive_id(), new VolleyRequest() {
                                @Override
                                public void onSuccess() {
                                    super.onSuccess();
                                    mData.remove(travel);
                                    notifyDataSetChanged();
                                    T.showShort(getActivity(), "删除成功");
                                }
                            });
                        }
                    })
                    .setTextOnClickListener(R.id.travel_edit, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("event", travel);
                            openActivity(InitiateEventActivity.class, bundle);
                        }
                    });
        }
    }
}
