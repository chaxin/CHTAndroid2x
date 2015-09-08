package com.damenghai.chahuitong.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.damenghai.chahuitong.base.BaseFragment;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.bean.Comment;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.DensityUtils;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.utils.ViewHolder;
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

        PullToRefreshListView mListView = (PullToRefreshListView) mView.findViewById(R.id.commond_listview);
        mListView.getRefreshableView().setDividerHeight(DensityUtils.dp2px(getActivity(), 4));
        mDatas = new ArrayList<Comment>();
        mAdapter = new ListViewAdapter(getActivity(), mDatas, R.layout.listview_item_comment);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("topic", mDatas.get(i));
            }
        });

        loadDatas();

        return mView;
    }

    private void loadDatas() {
        HodorAPI.myCommentShow(getActivity(), new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                L.d(response);
                try {
                    JSONArray array = new JSONObject(response).getJSONArray("content");
                    for (int i = 0; i < array.length(); i++) {
                        Comment comment = new Gson().fromJson(array.get(i).toString(), Comment.class);
                        if (!mDatas.contains(comment)) mDatas.add(comment);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private class ListViewAdapter extends CommonAdapter<Comment> {

        public ListViewAdapter(Context context, List<Comment> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(ViewHolder holder, Comment comment) {
            holder.setText(R.id.comment_text, comment.getComment())
                    .setText(R.id.comment_time, comment.getComment_time())
                    .setText(R.id.comment_user, comment.getMemberInfo().getMember_name())
                    .loadDefaultImage(R.id.comment_avatar, comment.getMemberInfo().getMember_avatar());
        }
    }
}
