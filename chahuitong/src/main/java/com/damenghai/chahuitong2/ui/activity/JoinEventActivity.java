package com.damenghai.chahuitong2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.damenghai.chahuitong2.api.EventAPI;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.view.TopBar;

/**
 * Created by Sgun on 15/9/2.
 */
public class JoinEventActivity extends BaseActivity implements View.OnClickListener {
    private int mID;

    private TopBar mTopBar;
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

        bindView();

        initView();
    }

    @Override
    protected void bindView() {
        mTopBar = (TopBar) findViewById(R.id.initiate_bar);
        mName = (EditText) findViewById(R.id.info_name);
        mPhone = (EditText) findViewById(R.id.info_phone);
        mCount = (EditText) findViewById(R.id.info_count);
        mComment = (EditText) findViewById(R.id.info_comment);
        mCommit = (Button) findViewById(R.id.travel_tv_enroll);
    }

    @Override
    protected void initView() {
        mTopBar.setOnLeftClickListener(new TopBar.OnLeftClickListener() {
            @Override
            public void onLeftClick() {
                finishActivity();
            }
        });
        mTopBar.setOnRightClickListener(new TopBar.onRightClickListener() {
            @Override
            public void onRightClick() {
                Intent intent = new Intent(JoinEventActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        mCommit.setOnClickListener(this);
    }

    private void commitData(String phone) {
        EventAPI.joinTravel(this, mID, phone, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
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
