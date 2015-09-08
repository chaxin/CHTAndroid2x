package com.damenghai.chahuitong.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.view.TopBar;

/**
 * Created by Sgun on 15/9/5.
 */
public class WriteCommentActivity extends BaseActivity implements View.OnClickListener {
    private int mStatusID;
    private String mReply;

    private TopBar mTopBar;
    private EditText mText;
    private Button mCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_status);

        mStatusID = getIntent().getIntExtra("status_id", 0);
        mReply = getIntent().getStringExtra("reply_to");

        findViewById();

        initView();
    }

    private void findViewById() {
        mTopBar = (TopBar) findViewById(R.id.write_status_bar);
        mText = (EditText) findViewById(R.id.write_status_text);
        mCommit = (Button) findViewById(R.id.status_commit);
    }

    private void initView() {
        mTopBar.setTitle("评论");
        mCommit.setText("发送");

        mTopBar.setOnLeftClickListener(new TopBar.OnLeftClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });

        mCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String text = mText.getText().toString();
        if(TextUtils.isEmpty(text)) T.showShort(this, "内容不能为空");
        else {
            HodorAPI.uploadComment(this, mStatusID, text, mReply, new VolleyRequest() {
                @Override
                public void onSuccess() {
                    super.onSuccess();
                    T.showShort(WriteCommentActivity.this, "评论成功");
                    finish();
                }
            });
        }
    }
}
