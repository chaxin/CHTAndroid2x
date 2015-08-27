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
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.bean.Topic;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.DateUtils;
import com.damenghai.chahuitong.utils.DensityUtils;
import com.damenghai.chahuitong.utils.ViewHolder;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/8/26.
 */
public class TopicFragment extends BaseFragment {
    private ArrayList<Topic> mDatas;
    private ListViewAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_list, null);

        ListView mListView = (ListView) mView.findViewById(R.id.commond_listview);
        mListView.setDividerHeight(DensityUtils.dp2px(getActivity(), 4));
        mDatas = new ArrayList<Topic>();
        loadDatas();
        mAdapter = new ListViewAdapter(getActivity(), mDatas, R.layout.listview_item_topic);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("topic", mDatas.get(i));
            }
        });

        return mView;
    }

    private void loadDatas() {
        HodorAPI.myTopicShow(SessionKeeper.readSession(getActivity()), SessionKeeper.readUsername(getActivity()), new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONArray array = new JSONObject(response).getJSONArray("content");
                    for(int i=0; i<array.length(); i++) {
                        Topic topic = new Gson().fromJson(array.get(i).toString(), Topic.class);
                        if(!mDatas.contains(topic)) mDatas.add(topic);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private class ListViewAdapter extends CommonAdapter<Topic> {

        public ListViewAdapter(Context context, List<Topic> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(ViewHolder holder, Topic topic) {
            holder.setText(R.id.topic_item_title, topic.getTitle())
                    .setText(R.id.topic_item_desc, topic.getContent())
                    .setText(R.id.topic_item_info, DateUtils.convert2US(topic.getTime()));
        }
    }
}
