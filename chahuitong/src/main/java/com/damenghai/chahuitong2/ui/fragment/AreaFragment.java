package com.damenghai.chahuitong2.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.BaseListAdapter;
import com.damenghai.chahuitong2.api.AddressApi;
import com.damenghai.chahuitong2.base.BaseFragment;
import com.damenghai.chahuitong2.bean.Area;
import com.damenghai.chahuitong2.response.ResponseCallBackListener;
import com.damenghai.chahuitong2.utils.ViewHolder;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class AreaFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private final String KEY_AREA_ID = "AreaFragment:id";

    private String mAreaId;
    private List<Area> mAreas;
    private AreaAdapter mAdapter;

    private AddressApi mApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_AREA_ID)) {
            mAreaId = savedInstanceState.getString(KEY_AREA_ID);
        }

        mApi = new AddressApi(getActivity());

        mAreas = new ArrayList<>();

        mAdapter = new AreaAdapter(getActivity(), mAreas, android.R.layout.simple_expandable_list_item_1);
    }

    public static AreaFragment get(String areaId) {
        AreaFragment fragment = new AreaFragment();
        fragment.mAreaId = areaId;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        PullToRefreshListView plv = (PullToRefreshListView) view.findViewById(R.id.fragment_list);
        plv.setAdapter(mAdapter);

        loadData();

        plv.setOnItemClickListener(this);

        return view;
    }

    private void loadData() {
        mApi.areaList(mAreaId, new ResponseCallBackListener<List<Area>>() {
            @Override
            public void onSuccess(List<Area> areas) {
                mAdapter.addList(areas);
            }

            @Override
            public void onError(int errorEvent, String message) {

            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Area area = mAreas.get(i - 1);
        EventBus.getDefault().post(area);
    }

    private class AreaAdapter extends BaseListAdapter<Area> {

        public AreaAdapter(Context context, List<Area> data, int resId) {
            super(context, data, resId);
        }

        @Override
        public void convert(ViewHolder holder, Area area) {
            holder.setText(android.R.id.text1, area.getArea_name());
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_AREA_ID, mAreaId);
    }
}
