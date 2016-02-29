package com.damenghai.chahuitong2.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.damenghai.chahuitong2.api.StatusAPI;
import com.damenghai.chahuitong2.base.BaseFragment;
import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.BaseListAdapter;
import com.damenghai.chahuitong2.bean.Comment;
import com.damenghai.chahuitong2.request.VolleyRequest;
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
 * Created by Sgun on 15/8/26.
 */
public class CommentFragment extends BaseFragment {
    private ArrayList<Comment> mDatas;
    private ListViewAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_list, null);

        PullToRefreshListView mPlv = (PullToRefreshListView) mView.findViewById(R.id.fragment_list);
        mPlv.getRefreshableView().setDividerHeight(DensityUtils.dp2px(getActivity(), 4));
        mDatas = new ArrayList<Comment>();
        mAdapter = new ListViewAdapter(getActivity(), mDatas, R.layout.listview_item_comment);
        mPlv.setAdapter(mAdapter);
//        mPlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("topic", mData.get(i - 1));
//            }
//        });

        loadData();

        return mView;
    }

    private void loadData() {
        StatusAPI.myCommentShow(getActivity(), new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("code") == 200) {
                        JSONArray array = obj.getJSONArray("content");
                        for (int i = 0; i < array.length(); i++) {
                            Comment comment = new Gson().fromJson(array.get(i).toString(), Comment.class);
                            if (!mDatas.contains(comment)) mDatas.add(comment);
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

    private class ListViewAdapter extends BaseListAdapter<Comment> {

        public ListViewAdapter(Context context, List<Comment> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(ViewHolder holder, Comment comment) {
            holder.setText(R.id.comment_text, comment.getComment())
                    .setText(R.id.comment_time, comment.getComment_time())
                    .setText(R.id.comment_user, comment.getMember_name())
                    .loadDefaultImage(R.id.comment_avatar, comment.getMember_avatar());
        }
    }
}
