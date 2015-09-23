package com.damenghai.chahuitong.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.StatusCommentAdapter;
import com.damenghai.chahuitong.api.HodorRequest;
import com.damenghai.chahuitong.bean.Comment;
import com.damenghai.chahuitong.bean.Status;
import com.damenghai.chahuitong.config.SessionKeeper;
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
    private final int REQUEST_CODE_WRITE = 0x200;

    private Status mStatus;

    private TopBar mTopBar;
    private PullToRefreshListView mPlv;
    private Button mWrite;

    private ArrayList<Comment> mData;
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

    @Override
    protected void findViewById() {
        mTopBar = (TopBar) findViewById(R.id.comment_bar);
        mPlv = (PullToRefreshListView) findViewById(R.id.comment_lv);
        mWrite = (Button) findViewById(R.id.write_comment);
    }

    @Override
    protected void initView() {
        mTopBar.setOnLeftClickListener(new TopBar.OnLeftClickListener() {
            @Override
            public void onLeftClick() {
                finishActivity();
            }
        });

        mData = new ArrayList<Comment>();
        mAdapter = new StatusCommentAdapter(this, mData, R.layout.listview_item_comment);
        mPlv.setAdapter(mAdapter);
        mPlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(1);
            }
        });

        mWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!SessionKeeper.readSession(CommentActivity.this).equals("")) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("status_id", mStatus.getContent_id());
                    openActivityForResult(WriteCommentActivity.class, REQUEST_CODE_WRITE, bundle);
                } else {
                    openActivity(LoginActivity.class);
                }
            }
        });
    }

    private void loadData(final int page) {
        HodorRequest.commentShow(mStatus.getContent_id(), new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                if (page == 1) mData.clear();

                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray array = obj.getJSONArray("content");
                    for (int i = 0; i < array.length(); i++) {
                        Comment comment = new Gson().fromJson(array.get(i).toString(), Comment.class);
                        if (!mData.contains(comment)) mData.add(comment);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAllDone() {
                super.onAllDone();
                mPlv.onRefreshComplete();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_CANCELED) return;

        if(requestCode == REQUEST_CODE_WRITE && resultCode == Activity.RESULT_OK) {
            mPlv.setRefreshing();
        }
    }
}
