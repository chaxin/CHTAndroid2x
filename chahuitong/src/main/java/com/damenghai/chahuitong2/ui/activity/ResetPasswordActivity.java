package com.damenghai.chahuitong2.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.api.AccountApi;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.utils.L;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.view.TopBar;

/**
 * Created by Sgun on 15/10/13.
 */
public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private String mMobile;

    private TopBar mTopBar;
    private EditText mNewPassword;
    private EditText mConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mMobile = getIntent().getStringExtra("mobile");

        bindView();

        initView();
    }

    @Override
    protected void bindView() {
        mTopBar = (TopBar) findViewById(R.id.reset_bar);
        mNewPassword = (EditText) findViewById(R.id.reset_new);
        mConfirm = (EditText) findViewById(R.id.reset_confirm);
    }

    @Override
    protected void initView() {
        mTopBar.setOnButtonClickListener(new TopBar.OnButtonClickListener() {
            @Override
            public void onLeftClick() {
                finishActivity();
            }

            @Override
            public void onRightClick() {
                goHome();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(TextUtils.isEmpty(mMobile)) {
            T.showShort(this, "号码出错，请重试");
            return;
        }

        String newPassword = mNewPassword.getText().toString();
        String confirm = mConfirm.getText().toString();

        if(TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirm)) {
            T.showShort(this, "不能为空");
            return;
        }

        if(!confirm.equals(newPassword)) {
            T.showShort(this, "两次输入的密码不同");
            return;
        }

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在修改...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        AccountApi.updatePassword(mMobile, newPassword, new VolleyRequest(dialog) {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                L.d("update password: " + response);
            }

            @Override
            public void onSuccess() {
                super.onSuccess();
                T.showShort(ResetPasswordActivity.this, "更新成功");
                finishActivity();
            }
        });
    }
}
