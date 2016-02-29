package com.damenghai.chahuitong2.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.view.TopBar;

/**
 * Created by Sgun on 15/9/20.
 */
public class ModifyActivity extends BaseActivity implements TextWatcher, View.OnClickListener {
    private TopBar mTopBar;
    private EditText mEtText;
    private ImageView mClear;

    private String mTitle;
    private String mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        mTitle = getIntent().getStringExtra("title");
        mText = getIntent().getStringExtra("text");

        bindView();

        initView();
    }

    @Override
    protected void bindView() {
        mTopBar = (TopBar) findViewById(R.id.modify_bar);
        mEtText = (EditText) findViewById(R.id.modify_text);
        mClear = (ImageView) findViewById(R.id.modify_clear);
    }

    @Override
    protected void initView() {
        mTopBar.setTitle(mTitle);
        mTopBar.setOnLeftClickListener(new TopBar.OnLeftClickListener() {
            @Override
            public void onLeftClick() {
                setResult(Activity.RESULT_CANCELED);
                finishActivity();
            }
        });
        mTopBar.setOnRightClickListener(new TopBar.onRightClickListener() {
            @Override
            public void onRightClick() {
                goHome();
            }
        });

        mEtText.addTextChangedListener(this);
        mEtText.setText(mText);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if(count > 0) {
            mClear.setVisibility(View.VISIBLE);
            mClear.setOnClickListener(this);
        } else {
            mClear.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.modify_clear :
                if(mEtText.length() > 0) {
                    mEtText.setText("");
                    mClear.setVisibility(View.GONE);
                }
                break;
            case R.id.modify_commit :
                Intent intent = new Intent();
                intent.putExtra("text", mEtText.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finishActivity();
                break;
        }
    }
}
