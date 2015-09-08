package com.damenghai.chahuitong.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.T;

/**
 * Created by Sgun on 15/9/2.
 */
public class JoinEventActivity extends BaseActivity implements View.OnClickListener {
    private int mID;

    private EditText mName;
    private EditText mPhone;
    private EditText mCount;
    private EditText mComment;
    private Button mCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_event);

        mID = getIntent().getIntExtra("id", 0);

        findViewById();

        initView();
    }

    private void findViewById() {
        mName = (EditText) findViewById(R.id.info_name);
        mPhone = (EditText) findViewById(R.id.info_phone);
        mCount = (EditText) findViewById(R.id.info_count);
        mComment = (EditText) findViewById(R.id.info_comment);
        mCommit = (Button) findViewById(R.id.btn_join);
    }

    private void initView() {
        mCommit.setOnClickListener(this);
    }

    private void commitData(String phone) {
        HodorAPI.joinTravel(this, mID, phone, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess();
                T.showShort(JoinEventActivity.this, "添加成功");
            }
        });
    }

    @Override
    public void onClick(View view) {
        String name = mName.getText().toString();
        String phone = mPhone.getText().toString();
        String count = mCount.getText().toString();
        String comment = mComment.getText().toString();

        if(name.length() <= 0 || phone.length() <= 0 || count.length() <= 0 || comment.length() <= 0) T.showShort(this, "请填写完整的信息");

        commitData(phone);
    }
}
