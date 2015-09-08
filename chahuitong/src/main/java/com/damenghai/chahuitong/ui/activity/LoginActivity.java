package com.damenghai.chahuitong.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.config.Constants;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.utils.HttpUtils;
import com.damenghai.chahuitong.utils.T;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity {

    /**
     * 登录请求接口
     */
    private final String LOGIN_IN_URL = "/index.php?act=login";

    /**
     * 注册请求接口
     */
    private final String REGISTER_URL = "/index.php?act=login&op=register";

    /**
     * 登录请求参数：客户端标识
     */
    private final String CLIENT_AGENT = "android";

    public static final int LOGIN_RESULT_CODE = 0;
    public static final int LOGOUT_RESULT_CODE = 1;

    SharedPreferences mSp;
    private Drawable drawable;
    private String mUsernameText, mPasswordText, mRegisterUsernameText, mRegisterPasswordText, mRegisterConfirmText, mRegisterEmailText;
    private ImageView mBtnBack;
    private LinearLayout mLoginLayout, mRegisterLayout;
    private EditText mLoginUsername, mLoginPassword, mRegisterUsername, mRegisterPassword, mRegisterEmail, mRegisterConfirm;
    private Button mTabLogin, mTabRegister, mBtnLoginIn, mBtnRegister;
    private ImageView mBtnHome;

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

        mRegisterUsername = (EditText) findViewById(R.id.id_editUsername);
        mRegisterPassword = (EditText) findViewById(R.id.id_editPassword);
        mRegisterEmail = (EditText) findViewById(R.id.id_editEmail);
        mRegisterConfirm = (EditText) findViewById(R.id.id_editConfirm);
        mBtnRegister = (Button) findViewById(R.id.id_btn_register);
    }

    private void initView() {
        drawable = getResources().getDrawable(R.drawable.triangle_indicator);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTabLogin.setCompoundDrawables(null, null, null, drawable);

        mTabLogin.setOnClickListener(new ChangeTabListener());
        mTabRegister.setOnClickListener(new ChangeTabListener());

        // 单击返回按钮
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
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

        // 单击登录按钮
        mBtnLoginIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoginUsername.length() <= 0) {
                    Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (mLoginPassword.length() <= 0) {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    mUsernameText = mLoginUsername.getText().toString();
                    mPasswordText = mLoginPassword.getText().toString();

                    requestData(Constants.API_URL + LOGIN_IN_URL, "username=" + mUsernameText + "&password=" + mPasswordText + "&client=" + CLIENT_AGENT);
                }
            }
        });

        // 单击立即注册按钮
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRegisterUsername.length() <= 0) {
                    T.showShort(LoginActivity.this, "用户名不能为空");
                } else if (mRegisterPassword.length() <= 0) {
                    T.showShort(LoginActivity.this, "密码不能为空");
                } else if (mRegisterPassword.length() < 6) {
                    T.showShort(LoginActivity.this, "密码长度不为小6位");
                } else if (mRegisterEmail.length() <= 0) {
                    T.showShort(LoginActivity.this, "邮箱地址不能为空");
                } else if (mRegisterConfirm.length() <= 0) {
                    T.showShort(LoginActivity.this, "确认密码不能为空");
                } else {
                    Pattern pattern = Pattern.compile("([a-zA-z0-9-_])+@([a-zA-z0-9-_])+.([a-zA-z0-9-_])+");
                    Matcher matcher = pattern.matcher(mRegisterEmail.getText().toString());
                    if (!matcher.matches()) {
                        T.showShort(LoginActivity.this, "邮箱格式不正确");
                    } else if (!mRegisterPassword.getText().toString().equals(mRegisterConfirm.getText().toString())) {
                        T.showShort(LoginActivity.this, "两次密码输入不一致");
                    } else {
                        mRegisterUsernameText = mRegisterUsername.getText().toString();
                        mRegisterPasswordText = mRegisterPassword.getText().toString();
                        mRegisterConfirmText = mRegisterConfirm.getText().toString();
                        mRegisterEmailText = mRegisterEmail.getText().toString();
                        String params = "username=" + mRegisterUsernameText + "&password=" + mRegisterPasswordText
                                + "&password_confirm=" + mRegisterConfirmText + "&email=" + mRegisterEmailText + "&client=" + CLIENT_AGENT;

                        requestData(Constants.API_URL + REGISTER_URL, params);
                    }
                }
            }
        });
    }

    // 异步请求网络数据
    private void requestData(String url, String params) {
        try {
            HttpUtils.doPostAsyn(url, params, new HttpUtils.CallBack() {
                @Override
                public void onRequestComplete(String result) {
                    if(result != null && !result.equals("")) {
                        try {
                            JSONObject json = new JSONObject(result);
                            JSONObject datas = json.getJSONObject("datas");
                            if (!datas.has("error")) {
                                String username = datas.getString("username");
                                String key = datas.getString("key");

                                if(username != null && key != null && !username.trim().equals("") && !key.trim().equals("")) {
                                    SessionKeeper.writeSession(LoginActivity.this, key);
                                    SessionKeeper.writeUsername(LoginActivity.this, username);
                                    setResult(LOGIN_RESULT_CODE);
                                    finish();
                                }
                            } else {
                                final String error = datas.getString("error");
                                if(error != null && !error.trim().equals("")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            T.showShort(LoginActivity.this, error);
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 切换标签
    private class ChangeTabListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
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
    }
}
