package com.damenghai.chahuitong2.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.damenghai.chahuitong2.api.StatusAPI;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.bean.event.AddCommentEvent;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.view.TopBar;

import de.greenrobot.event.EventBus;

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

        bindView();

        initView();
    }

    @Override
    protected void bindView() {
        mTopBar = (TopBar) findViewById(R.id.write_status_bar);
        mText = (EditText) findViewById(R.id.write_status_text);
        mCommit = (Button) findViewById(R.id.status_commit);
    }

    @Override
    protected void initView() {
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
            StatusAPI.uploadComment(this, mStatusID, text, mReply, new VolleyRequest() {
                @Override
                public void onSuccess(String response) {
                    super.onSuccess(response);

                    T.showShort(WriteCommentActivity.this, "评论成功");
                    EventBus.getDefault().post(new AddCommentEvent(mStatusID));
                    setResult(Activity.RESULT_OK);
                    finishActivity();
                }

                @Override
                public void onError(String error) {
                    super.onError(error);
                    T.showShort(WriteCommentActivity.this, error);
                }
            });
        }
    }
}
