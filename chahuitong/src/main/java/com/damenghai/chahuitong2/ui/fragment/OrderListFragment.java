package com.damenghai.chahuitong2.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.OrderListAdapter;
import com.damenghai.chahuitong2.api.OrderApi;
import com.damenghai.chahuitong2.base.BaseFragment;
import com.damenghai.chahuitong2.bean.Order;
import com.damenghai.chahuitong2.bean.OrderGroup;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.utils.DensityUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseResp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class OrderListFragment extends BaseFragment implements
        OnRefreshListener, OnLastItemVisibleListener {
    private final String KEY_ORDER_STATE = "OrderListFragment:OrderState";

    private String mState;

    private int mCurPage;

    private ArrayList<Order> mOrders;

    private OrderListAdapter mAdapter;

    private PullToRefreshListView mPlv;

    private boolean mHasMore;

    private int mPageTotal;

    public static OrderListFragment get(String orderState) {
        OrderListFragment fragment = new OrderListFragment();
        fragment.mState = orderState;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mPlv = (PullToRefreshListView) view.findViewById(R.id.fragment_list);

        mOrders = new ArrayList<>();
        mAdapter = new OrderListAdapter(getActivity(), mOrders, R.layout.item_list_order);
        mPlv.getRefreshableView().setDividerHeight(DensityUtils.dp2px(getActivity(), 8));
        mPlv.setAdapter(mAdapter);
        mPlv.setOnRefreshListener(this);
        mPlv.setOnLastItemVisibleListener(this);

        loadData(1);

        return view;
    }

    private void loadData(final int page) {
        OrderApi.orderList(getActivity(), mState, page + "", new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject datas = object.getJSONObject("datas");
                    if (!datas.has("error")) {
                        mHasMore = object.getBoolean("hasmore");
                        mPageTotal = object.getInt("page_total");

                        mCurPage = page;

                        if (page == 1) {
                            mOrders.clear();
                        }

                        JSONArray orderGroupList = datas.getJSONArray("order_group_list");
                        for (int i = 0; i < orderGroupList.length(); i++) {
                            OrderGroup orderGroup = new Gson().fromJson(orderGroupList.getString(i), OrderGroup.class);
                            ArrayList<Order> orderList = orderGroup.getOrder_list();
                            for (Order order : orderList) {
                                if (!mOrders.contains(order)) mOrders.add(order);
                            }
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAllDone() {
                super.onAllDone();
                mPlv.onRefreshComplete();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_ORDER_STATE)) {
           mState = savedInstanceState.getString(KEY_ORDER_STATE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_ORDER_STATE, mState);
    }

    @Override
    public void onLastItemVisible() {
        if(mHasMore && mPageTotal > 0) loadData(mCurPage + 1);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        loadData(1);
    }

    public void onEventMainThread(Order order) {
        loadData(1);
    }

    public void onEvent(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX && resp.errCode == 0) {
            loadData(1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
