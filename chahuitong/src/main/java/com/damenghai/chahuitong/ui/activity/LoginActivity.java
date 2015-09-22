package com.damenghai.chahuitong.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.DialogUtils;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.view.FlippingLoadingDialog;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

public class LoginActivity extends BaseActivity implements TextWatcher, View.OnClickListener {
    private Drawable drawable;
    private String mUsernameText, mPasswordText;

    private LinearLayout mLayout;
    private ImageView mBtnBack;
    private LinearLayout mLoginLayout, mRegisterLayout;
    private EditText mLoginUsername, mLoginPassword, mRegPassword;
    private EditText mEtPhone;
    private EditText mEtCode;
    private Button mBtSend;
    private Button mTabLogin, mTabRegister, mBtnLoginIn, mBtnRegister;
    private ImageView mBtnHome;
    private ImageView mIvQQ;
    private ImageView mWeibo;

    private int mCode;

    private CountTimer mTimer;

    private FlippingLoadingDialog mDialog;
    private UMSocialService mController;
    private Tencent mTencent;

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
        // 分享给qq好友，参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1104563629", "rJbMttJCa47MBsCk");
        qqSsoHandler.addToSocialSDK();

        //设置新浪SSO handler
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
    }

    @Override
    protected void findViewById() {
        mLayout = (LinearLayout) findViewById(R.id.login_layout);

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

        mIvQQ = (ImageView) findViewById(R.id.login_qq);
        mWeibo = (ImageView) findViewById(R.id.login_weibo);
    }

    @Override
    protected void initView() {
        if(!SessionKeeper.readUsername(this).equals("")) {
            mLoginUsername.setText(SessionKeeper.readUsername(this));
        }

        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

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

        // 第三方登录
        mIvQQ.setOnClickListener(this);
        mWeibo.setOnClickListener(this);
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
                        T.showShort(LoginActivity.this, "授权完成");
                        //获取相关授权信息
                        mController.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, new SocializeListeners.UMDataListener() {
                            @Override
                            public void onStart() {
                                T.showShort(LoginActivity.this, "获取平台数据开始...");
                            }
                            @Override
                            public void onComplete(int status, Map<String, Object> info) {
                                if(status == 200 && info != null){
                                    StringBuilder sb = new StringBuilder();
                                    Set<String> keys = info.keySet();
                                    for(String key : keys){
                                        sb.append(key+"="+info.get(key).toString()+"\r\n");
                                    }
                                    L.d("TestData" + sb.toString());
                                }else{
                                    L.d("TestData" + "发生错误：" + status);
                                }
                            }
                        });
                    }
                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        T.showShort(LoginActivity.this, "授权取消 ");
                    }
                } );
//                L.d("click qq");
//                mTencent = Tencent.createInstance("100424468", this.getApplicationContext());
//                if (!mTencent.isSessionValid()) {
//                    L.d("isSessionValid");
//                    mTencent.login(this, "all", new IUiListener() {
//                        @Override
//                        public void onComplete(Object o) {
//                            JSONObject obj = (JSONObject) o;
//                            L.d(obj.toString());
//                            L.d("授权成功");
//                        }
//
//                        @Override
//                        public void onError(UiError e) {
//                            L.d("onError:", "code:" + e.errorCode + ", msg:"
//                                    + e.errorMessage + ", detail:" + e.errorDetail);
//                        }
//
//                        @Override
//                        public void onCancel() {
//
//                        }
//                    });
//                }
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
