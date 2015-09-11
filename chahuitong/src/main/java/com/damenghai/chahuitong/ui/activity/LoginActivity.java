package com.damenghai.chahuitong.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.config.Constants;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.DialogUtils;
import com.damenghai.chahuitong.utils.HttpUtils;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.view.FlippingLoadingDialog;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity implements TextWatcher, View.OnClickListener {
    private Drawable drawable;
    private String mUsernameText, mPasswordText;
    private ImageView mBtnBack;
    private LinearLayout mLoginLayout, mRegisterLayout;
    private EditText mLoginUsername, mLoginPassword, mRegPassword;
    private EditText mEtPhone;
    private EditText mEtCode;
    private Button mBtSend;
    private Button mTabLogin, mTabRegister, mBtnLoginIn, mBtnRegister;
    private ImageView mBtnHome;

    private int mCode;

    private CountTimer mTimer;

    private FlippingLoadingDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById();
        initView();
    }

    private void findViewById() {
        mTabLogin = (Button) findViewById(R.id.id_tab_login);
        mTabRegister = (Button) findViewById(R.id.id_tab_register);
        mBtnBack = (ImageView) findViewById(R.id.id_btn_back);
        mBtnHome = (ImageView) findViewById(R.id.login_btn_home);

        mLoginLayout = (LinearLayout) findViewById(R.id.id_login_layout);
        mRegisterLayout = (LinearLayout) findViewById(R.id.id_register_layout);

        mLoginUsername = (EditText) findViewById(R.id.id_input_username);
        mLoginPassword = (EditText) findViewById(R.id.id_input_password);
        mBtnLoginIn = (Button) findViewById(R.id.id_btn_login_in);

        mRegPassword = (EditText) findViewById(R.id.register_password);
        mBtnRegister = (Button) findViewById(R.id.id_btn_register);

        mEtPhone = (EditText) findViewById(R.id.register_phone);
        mEtCode = (EditText) findViewById(R.id.register_code);
        mBtSend = (Button) findViewById(R.id.register_send);
    }

    private void initView() {
        drawable = getResources().getDrawable(R.drawable.triangle_indicator);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTabLogin.setCompoundDrawables(null, null, null, drawable);

        // 单击返回按钮
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finishActivity();
            }
        });

        // 单击首页
        mBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        mTabLogin.setOnClickListener(this);
        mTabRegister.setOnClickListener(this);

        // 发送验证码
        mBtSend.setOnClickListener(this);

        // 监听输入手机号码
        mEtPhone.addTextChangedListener(this);

        // 单击登录按钮
        mBtnLoginIn.setOnClickListener(this);
        // 单击立即注册按钮
        mBtnRegister.setOnClickListener(this);

    }

    private void getCode() {
        String mobile = mEtPhone.getText().toString();

        mCode = (int)((Math.random()*9+1)*100000);

        HodorAPI.sendSMS(mobile, mCode, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                try {
                    Document doc = DocumentHelper.parseText(response);
                    Element root = doc.getRootElement();

                    String code = root.elementText("code");
                    String msg = root.elementText("msg");

                    if (!"2".equals(code)) {
                        T.showShort(LoginActivity.this, msg);
                    }

                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @SuppressLint("NewApi")
    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        String phone = mEtPhone.getText().toString();
        if(phone.length() == 11) {
            mBtSend.setBackground(getResources().getDrawable(R.drawable.draw_primary2dark_sel));
            mBtSend.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_login_in :
                if (mLoginUsername.length() <= 0) {
                    T.showShort(LoginActivity.this, "手机号不能为空");
                } else if (mLoginPassword.length() <= 0) {
                    T.showShort(LoginActivity.this, "密码不能为空");
                } else {
                    mUsernameText = mLoginUsername.getText().toString();
                    mPasswordText = mLoginPassword.getText().toString();

                    if(mDialog == null) {
                        mDialog = new FlippingLoadingDialog(LoginActivity.this);
                    }
                    mDialog.show();

                    HodorAPI.login(mUsernameText, mPasswordText, new VolleyRequest(mDialog) {
                        @Override
                        public void onSuccess(String response) {
                            super.onSuccess(response);
                            try {
                                JSONObject obj = new JSONObject(response);

                                if(obj.getInt("code") == 200) {
                                    T.showShort(LoginActivity.this, "登录成功");
                                    JSONObject datas = obj.getJSONObject("datas");
                                    SessionKeeper.writeSession(LoginActivity.this, datas.getString("key"));
                                    SessionKeeper.writeUsername(LoginActivity.this, datas.getString("username"));
                                    setResult(Activity.RESULT_OK);
                                    finishActivity();
                                } else {
                                    T.showShort(LoginActivity.this, "登录失败");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onAllDone() {
                            super.onAllDone();
                            DialogUtils.createLoadingDialog(LoginActivity.this).dismiss();
                        }
                    });
                }
                break;
            case R.id.register_send :
                if(mTimer == null) {
                    mTimer = new CountTimer(60000, 1000);
                }
                mTimer.start();
                mBtSend.setBackgroundResource(R.color.primary_light);
                getCode();
                break;
            case R.id.id_btn_register :
                String mobile = mEtPhone.getText().toString();
                String password = mRegPassword.getText().toString();
                String code = mEtCode.getText().toString();

                if(TextUtils.isEmpty(mobile) || mobile.length() != 11) {
                    T.showShort(LoginActivity.this, "请填写正确的手机号码");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    T.showShort(LoginActivity.this, "请填写密码");
                    return;
                }
                if(TextUtils.isEmpty(code)) {
                    T.showShort(LoginActivity.this, "请填写手机号后获取验证码");
                    return;
                }

                if(!code.equals(mCode + "")) {
                    T.showShort(LoginActivity.this, "验证码错误");
                    return;
                }

                if(mDialog == null) {
                    mDialog = new FlippingLoadingDialog(LoginActivity.this);
                }
                mDialog.show();

                HodorAPI.register(mobile, password, new VolleyRequest(mDialog) {
                    @Override
                    public void onSuccess(String response) {
                        super.onSuccess(response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getInt("code") == 200) {
                                T.showShort(LoginActivity.this, "注册成功");
                                SessionKeeper.writeSession(LoginActivity.this, obj.getString("key"));
                                SessionKeeper.writeUsername(LoginActivity.this, "username");
                                setResult(Activity.RESULT_OK);
                                finishActivity();
                            } else {
                                T.showShort(LoginActivity.this, obj.getString("content"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                break;
            case R.id.id_tab_login :
                if(!mTabLogin.isSelected()) {
                    mTabLogin.setSelected(true);
                    mTabLogin.setCompoundDrawables(null, null, null, drawable);
                    mTabRegister.setSelected(false);
                    mTabRegister.setCompoundDrawables(null, null, null, null);
                    mLoginLayout.setVisibility(View.VISIBLE);
                    mRegisterLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.id_tab_register :
                if(!mTabRegister.isSelected()) {
                    mTabRegister.setSelected(true);
                    mTabRegister.setCompoundDrawables(null, null, null, drawable);
                    mTabLogin.setSelected(false);
                    mTabLogin.setCompoundDrawables(null, null, null, null);
                    mRegisterLayout.setVisibility(View.VISIBLE);
                    mLoginLayout.setVisibility(View.GONE);
                }
                break;
        }
    }

    /* 定义一个倒计时的内部类 */
    class CountTimer extends CountDownTimer {
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            mBtSend.setEnabled(false);
            mBtSend.setText("(" + l/1000 + "秒)");
        }

        @Override
        public void onFinish() {
            mBtSend.setEnabled(true);
            mBtSend.setText("发送验证码");
        }
    }
}
