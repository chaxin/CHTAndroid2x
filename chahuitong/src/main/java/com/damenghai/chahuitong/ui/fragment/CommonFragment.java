package com.damenghai.chahuitong.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.damenghai.chahuitong.BaseFragment;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.bean.BaseBean;
import com.damenghai.chahuitong.bean.Comment;
import com.damenghai.chahuitong.utils.DensityUtils;
import com.damenghai.chahuitong.utils.ViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用的显示列表样式的Fragment
 *
 * Created by Sgun on 15/8/27.
 */
public class CommonFragment<T extends BaseBean> extends BaseFragment {
    private static final String KEY_API = "CommonFragment:api";
    private static final String KEY_ITEM_RES_ID ="CommonFragment:itemResId";

    private String mAPI;
    private int mItemRes;
    private ArrayList<T> mDatas;
    private ListViewAdapter mAdapter;

    public static CommonFragment get(String api, int itemResId) {
        CommonFragment fragment = new CommonFragment();
        fragment.mAPI = api;
        fragment.mItemRes = itemResId;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_API)
                && savedInstanceState.containsKey(KEY_ITEM_RES_ID)) {
            mAPI = savedInstanceState.getString(KEY_API);
            mItemRes = savedInstanceState.getInt(KEY_ITEM_RES_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        ListView listView = (ListView) view.findViewById(R.id.commond_listview);

        listView.setDividerHeight(DensityUtils.dp2px(getActivity(), 4));
        mDatas = new ArrayList<T>();
        loadDatas();
        mAdapter = new ListViewAdapter(getActivity(), mDatas, mItemRes);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("topic", mDatas.get(i));
            }
        });

        return view;
    }

    private void loadDatas() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_API, mAPI);
        outState.putInt(KEY_ITEM_RES_ID, mItemRes);
    }

    private class ListViewAdapter extends CommonAdapter<T> {

        public ListViewAdapter(Context context, List<T> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(ViewHolder holder, T t) {

        }
    }
}
