package com.damenghai.chahuitong.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.StatusCommentAdapter;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.bean.Comment;
import com.damenghai.chahuitong.bean.Status;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.view.TopBar;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 评论
 * Created by Sgun on 15/8/25.
 */
public class CommentActivity extends BaseActivity {
    private Status mStatus;

    private TopBar mTopBar;
    private PullToRefreshListView mLv;
    private Button mWrite;

    private ArrayList<Comment> mDatas;
    private StatusCommentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        mStatus = (Status) getIntent().getSerializableExtra("status");

        findViewById();

        initView();

        loadData(1);
    }

    private void findViewById() {
        mTopBar = (TopBar) findViewById(R.id.comment_bar);
        mLv = (PullToRefreshListView) findViewById(R.id.comment_lv);
        mWrite = (Button) findViewById(R.id.write_comment);
    }

    private void initView() {
        mTopBar.setOnLeftClickListener(new TopBar.OnLeftClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });

        mDatas = new ArrayList<Comment>();
        mAdapter = new StatusCommentAdapter(this, mDatas, R.layout.listview_item_comment);
        mLv.setAdapter(mAdapter);
        mLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(1);
            }
        });

        mWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("status_id", mStatus.getContent_id());
                openActivity(WriteCommentActivity.class, bundle);
            }
        });
    }

    private void loadData(final int page) {
        HodorAPI.commentShow(mStatus.getContent_id(), new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                if(page == 1) mDatas.clear();

                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray array = obj.getJSONArray("content");
                    for (int i = 0; i < array.length(); i++) {
                        Comment comment = new Gson().fromJson(array.get(i).toString(), Comment.class);
                        if (!mDatas.contains(comment)) mDatas.add(comment);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAllDone() {
                super.onAllDone();
                mLv.onRefreshComplete();
            }
        });
    }

}
