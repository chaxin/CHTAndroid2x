package com.damenghai.chahuitong.ui.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.api.UserAPI;
import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.ui.fragment.RegisterFragment;
import com.damenghai.chahuitong.utils.DialogUtils;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.view.FlippingLoadingDialog;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeUser;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;


public class LoginActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private LinearLayout mRoot;
    private ImageView mBtnBack;

    private RadioGroup mGroup;

    private LinearLayout mLogin;
    private FrameLayout mRegister;

    private EditText mLoginUsername, mLoginPassword;
    private Button mBtnLoginIn;
    private ImageView mBtnHome,mIvQQ,mWeibo;

    private FlippingLoadingDialog mDialog;
    private UMSocialService mController;

    private String mPassword = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initThird();

        findViewById();

        initView();
    }

    private void initThird() {
        mController = UMServiceFactory.getUMSocialService("com.umeng.login");
        // 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1104563629", "rJbMttJCa47MBsCk");
        qqSsoHandler.addToSocialSDK();

        //设置新浪SSO handler
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
    }

    @Override
    protected void findViewById() {
        mRoot = (LinearLayout) findViewById(R.id.login_root);
        mBtnBack = (ImageView) findViewById(R.id.id_btn_back);
        mBtnHome = (ImageView) findViewById(R.id.login_btn_home);

        mGroup = (RadioGroup) findViewById(R.id.login_group);

        mLogin = (LinearLayout) findViewById(R.id.login_layout);
        mRegister = (FrameLayout) findViewById(R.id.register_container);

        mLoginUsername = (EditText) findViewById(R.id.id_input_username);
        mLoginPassword = (EditText) findViewById(R.id.id_input_password);
        mBtnLoginIn = (Button) findViewById(R.id.id_btn_login_in);

        mIvQQ = (ImageView) findViewById(R.id.login_qq);
        mWeibo = (ImageView) findViewById(R.id.login_weibo);
    }

    @Override
    protected void initView() {
        if(!SessionKeeper.readUsername(this).equals("") && SessionKeeper.readSession(this).length() == 1) {
            mLoginUsername.setText(SessionKeeper.readUsername(this));
        }

        mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

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

        mGroup.setOnCheckedChangeListener(this);

        // 单击登录按钮
        mBtnLoginIn.setOnClickListener(this);

        // 第三方登录
        mIvQQ.setOnClickListener(this);
        mWeibo.setOnClickListener(this);
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
                    String usernameText = mLoginUsername.getText().toString();
                    String passwordText = mLoginPassword.getText().toString();

                    if(mDialog == null) {
                        mDialog = new FlippingLoadingDialog(LoginActivity.this);
                    }
                    mDialog.show();

                    UserAPI.login(usernameText, passwordText, new VolleyRequest(mDialog) {
                        @Override
                        public void onSuccess(String response) {
                            super.onSuccess(response);
                            try {
                                JSONObject obj = new JSONObject(response);

                                if (obj.getInt("code") == 200) {
                                    T.showShort(LoginActivity.this, "登录成功");
                                    JSONObject datas = obj.getJSONObject("datas");
                                    SessionKeeper.writeSession(LoginActivity.this, datas.getString("key"));
                                    SessionKeeper.writeUsername(LoginActivity.this, datas.getString("username"));
                                    setResult(Activity.RESULT_OK);
                                    L.d("text:" + response);
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
            case R.id.login_qq :
                mController.doOauthVerify(LoginActivity.this, SHARE_MEDIA.QQ, new SocializeListeners.UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                        T.showShort(LoginActivity.this, "授权开始");
                    }
                    @Override
                    public void onError(SocializeException e, SHARE_MEDIA platform) {
                        T.showShort(LoginActivity.this, "授权错误");
                    }
                    @Override
                    public void onComplete(Bundle value, SHARE_MEDIA platform) {
                        if(value != null && TextUtils.isEmpty(value.get("openid").toString())) {
                            T.showShort(LoginActivity.this, "授权完成");
                            mPassword = value.getString("openid");
                            //获取相关授权信息
                            mController.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, new SocializeListeners.UMDataListener() {
                                @Override
                                public void onStart() {}

                                @Override
                                public void onComplete(int status, Map<String, Object> info) {
                                    if (status == 200 && info != null) {
                                        String username = info.get("nickname").toString();

                                    } else {
                                        T.showShort(LoginActivity.this, "发生错误");
                                    }
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        T.showShort(LoginActivity.this, "授权取消 ");
                    }
                } );
                break;
            case R.id.login_weibo :
                mController.doOauthVerify(LoginActivity.this, SHARE_MEDIA.SINA,new SocializeListeners.UMAuthListener() {
                    @Override
                    public void onError(SocializeException e, SHARE_MEDIA platform) {
                    }
                    @Override
                    public void onComplete(Bundle value, SHARE_MEDIA platform) {
                        if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
                            T.showShort(LoginActivity.this, "授权成功");
                            mPassword = value.getString("uid");
                            mController.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.SINA, new SocializeListeners.UMDataListener() {
                                @Override
                                public void onStart() {}

                                @Override
                                public void onComplete(int status, Map<String, Object> info) {
                                    if (status == 200 && info != null) {
                                        String username = info.get("screen_name").toString();

                                        L.d("uid: " + mPassword + ", username:" + username);

                                        UserAPI.createAccount(mPassword, username, mPassword, new VolleyRequest() {
                                            @Override
                                            public void onSuccess(String response) {
                                                super.onSuccess(response);
                                                try {
                                                    L.d("response: " + new JSONObject(response).toString());
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    } else {
                                        L.d("TestData" + "发生错误：" + status);
                                    }
                                }
                            });
                        } else {
                            T.showShort(LoginActivity.this, "授权失败");
                        }
                    }
                    @Override
                    public void onCancel(SHARE_MEDIA platform) {}
                    @Override
                    public void onStart(SHARE_MEDIA platform) {}
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkID) {
        FragmentManager fm =getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        RegisterFragment fragment = new RegisterFragment();

        switch (checkID) {
            case R.id.tab_login :
                ft.remove(fragment);
                mLogin.setVisibility(View.VISIBLE);
                mRegister.setVisibility(View.GONE);
                break;
            case R.id.tab_register :
                ft.replace(R.id.register_container, fragment);
                mLogin.setVisibility(View.GONE);
                mRegister.setVisibility(View.VISIBLE);
                break;
        }

        ft.commit();
    }

}
