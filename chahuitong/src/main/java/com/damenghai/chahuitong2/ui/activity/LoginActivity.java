package com.damenghai.chahuitong2.ui.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.api.AccountApi;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.bean.event.CookieEvent;
import com.damenghai.chahuitong2.config.Config;
import com.damenghai.chahuitong2.config.SessionKeeper;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.ui.fragment.RegisterFragment;
import com.damenghai.chahuitong2.utils.L;
import com.damenghai.chahuitong2.utils.T;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.SaveListener;
import de.greenrobot.event.EventBus;


public class LoginActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private LinearLayout mRoot;
    private ImageView mBtnBack;

    private RadioGroup mGroup;

    private LinearLayout mLogin;
    private FrameLayout mRegister;

    private EditText mLoginUsername, mLoginPassword;
    private Button mBtnLoginIn;
    private TextView mForgot;
    private ImageView mBtnHome,mIvQQ,mWeibo;

    private UMSocialService mController;

    private String mPassword = "";
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initThird();

        bindView();

        initView();
    }

    private void initThird() {
        mController = UMServiceFactory.getUMSocialService("com.umeng.login");
        // 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, Config.qqAppID, Config.qqAppSecret);
        qqSsoHandler.addToSocialSDK();

        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(this, Config.wxAppID,Config.wxAppSecret);
        wxHandler.addToSocialSDK();

        //设置新浪SSO handler
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
    }

    @Override
    protected void bindView() {
        mRoot = (LinearLayout) findViewById(R.id.login_root);
        mBtnBack = (ImageView) findViewById(R.id.id_btn_back);
        mBtnHome = (ImageView) findViewById(R.id.login_btn_home);

        mGroup = (RadioGroup) findViewById(R.id.login_group);

        mLogin = (LinearLayout) findViewById(R.id.login_layout);
        mRegister = (FrameLayout) findViewById(R.id.register_container);

        mLoginUsername = (EditText) findViewById(R.id.id_input_username);
        mLoginPassword = (EditText) findViewById(R.id.id_input_password);
        mBtnLoginIn = (Button) findViewById(R.id.id_btn_login_in);
        mForgot = (TextView) findViewById(R.id.login_forgot);

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

        mForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(ForgotActivity.class);
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
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在登录");
        mDialog.setCanceledOnTouchOutside(false);

        switch (view.getId()) {
            case R.id.id_btn_login_in :
                if (mLoginUsername.length() <= 0) {
                    T.showShort(LoginActivity.this, "手机号不能为空");
                } else if (mLoginPassword.length() <= 0) {
                    T.showShort(LoginActivity.this, "密码不能为空");
                } else {
                    final String mobile = mLoginUsername.getText().toString();
                    final String password = mLoginPassword.getText().toString();

                    mDialog.show();

                    AccountApi.login(mobile, password, new VolleyRequest(mDialog) {
                        @Override
                        public void onSuccess(String response) {
                            super.onSuccess(response);

                            L.d("login: " + response);

                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONObject datas = obj.getJSONObject("datas");

                                if (!datas.has("error")) {
                                    String key = datas.getString("key");
                                    String username = datas.getString("username");

                                    SessionKeeper.writeSession(LoginActivity.this, key);
                                    SessionKeeper.writeUsername(LoginActivity.this, username);

                                    CookieEvent event = new CookieEvent();
                                    event.setKey(key);
                                    event.setUsername(mobile);
                                    EventBus.getDefault().post(event);

                                    final BmobChatUser user = new BmobChatUser();
                                    user.setUsername(mobile);
                                    user.setPassword(password);
                                    mUserManager.login(user, new SaveListener() {

                                        @Override
                                        public void onSuccess() {
                                            //更新用户的地理位置以及好友的资料，可自行到BaseActivity类中查看此方法的具体实现，建议添加
//                                            updateUserInfos();
                                            //省略其他代码
                                        }

                                        @Override
                                        public void onFailure(int code, String msg) {
                                            user.setMobilePhoneNumber(mobile);
                                            //将user和设备id进行绑定aa
                                            user.setDeviceType("android");
                                            user.setInstallId(BmobInstallation.getInstallationId(LoginActivity.this));

                                            user.signUp(LoginActivity.this, new SaveListener() {

                                                @Override
                                                public void onSuccess() {
                                                    // 将设备与username进行绑定
                                                    mUserManager.bindInstallationForRegister(user.getUsername());
                                                    // 更新地理位置信息
                                                    // updateUserLocation();
                                                    T.showShort(LoginActivity.this, "登录成功");
                                                }

                                                @Override
                                                public void onFailure(int error, String msg) {
                                                    L.d(error + msg);
                                                }
                                            });

                                        }
                                    });

                                    mDialog.dismiss();
                                    setResult(Activity.RESULT_OK);
                                    finishActivity();
                                } else {
                                    T.showShort(LoginActivity.this, datas.getString("error"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onAllDone() {
                            super.onAllDone();
                        }
                    });
                }
                break;
            case R.id.login_qq :

                mController.doOauthVerify(LoginActivity.this, SHARE_MEDIA.QQ, new SocializeListeners.UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                        T.showShort(LoginActivity.this, "授权开始");
                        mDialog.show();
                    }
                    @Override
                    public void onError(SocializeException e, SHARE_MEDIA platform) {
                        T.showShort(LoginActivity.this, "授权出错");
                        mDialog.dismiss();
                    }
                    @Override
                    public void onComplete(Bundle value, SHARE_MEDIA platform) {
                        T.showShort(LoginActivity.this, "授权完成");
                        mPassword = value.getString("openid");
                        final String access_token = value.getString("access_token");
                        final String expires_in = value.getString("expires_in");

                        //获取相关授权信息
                        mController.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, new SocializeListeners.UMDataListener() {
                            @Override
                            public void onStart() {
                                T.showShort(LoginActivity.this, "获取平台数据开始...");
                            }
                            @Override
                            public void onComplete(int status, Map<String, Object> info) {
                                if(status == 200 && info != null){
                                    loginSuccess(info.get("screen_name").toString(), "qq", access_token, expires_in, mPassword);
                                }else{
                                    T.showShort(LoginActivity.this, status + "获取数据出错");
                                }
                            }
                        });

                    }
                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        T.showShort(LoginActivity.this, "授权取消 ");
                        mDialog.dismiss();
                    }
                } );
                break;
            case R.id.login_weibo :
                mController.doOauthVerify(LoginActivity.this, SHARE_MEDIA.SINA, new SocializeListeners.UMAuthListener() {

                    @Override
                    public void onError(SocializeException e, SHARE_MEDIA platform) {
                        T.showShort(LoginActivity.this, "授权出错");
                        mDialog.dismiss();
                    }

                    @Override
                    public void onComplete(Bundle value, SHARE_MEDIA platform) {
                        if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
                            mPassword = value.getString("uid");
                            final String access_token = value.getString("access_token");
                            final String expires_in = value.getString("expires_in");

                            mController.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.SINA, new SocializeListeners.UMDataListener() {
                                @Override
                                public void onStart() {
                                    T.showShort(LoginActivity.this, "获取平台数据开始...");
                                }

                                @Override
                                public void onComplete(int status, Map<String, Object> info) {
                                    T.showShort(LoginActivity.this, "获取数据成功" + mPassword);
                                    if (status == 200 && info != null) {
                                        loginSuccess(info.get("screen_name").toString(), "sina", access_token, expires_in, mPassword);
                                    } else {
                                        T.showShort(LoginActivity.this, status + "获取数据出错");
                                    }
                                }
                            });
                        } else {
                            T.showShort(LoginActivity.this, "授权失败");
                        }
                    }
                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        mDialog.dismiss();
                    }
                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                        mDialog.show();
                    }
                });
                break;
            case R.id.login_weixin :
                mController.doOauthVerify(LoginActivity.this, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                        T.showShort(LoginActivity.this, "授权开始");
                        mDialog.show();
                    }
                    @Override
                    public void onError(SocializeException e, SHARE_MEDIA platform) {
                        T.showShort(LoginActivity.this, "授权错误");
                        mDialog.dismiss();
                    }
                    @Override
                    public void onComplete(Bundle value, SHARE_MEDIA platform) {
                        T.showShort(LoginActivity.this, "授权完成");
                        mPassword = value.getString("openid");
                        final String access_token = value.getString("access_token");
                        final String expires_in = value.getString("expires_in");

                        //获取相关授权信息
                        mController.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMDataListener() {
                            @Override
                            public void onStart() {
                                T.showShort(LoginActivity.this, "获取平台数据开始...");
                            }

                            @Override
                            public void onComplete(int status, Map<String, Object> info) {
                                if (status == 200 && info != null) {
                                    loginSuccess(info.get("nickname").toString(), "wechat", access_token, expires_in, mPassword);
                                } else {
                                    L.d("发生错误：" + status);
                                }
                            }
                        });
                    }
                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        T.showShort(LoginActivity.this, "授权取消");
                        mDialog.dismiss();
                    }
                } );
                break;
        }
    }

    private void loginSuccess(final String username, final String snsType,
                              final String access_token, final String expires_in, final String primaryKey) {

        final String type = snsType.equals("sina") ? "weibo" : (snsType.equals("wechat") ? "weixin" : snsType);

        AccountApi.createAccount(snsType, username, primaryKey, primaryKey, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                try {
                    final JSONObject object = new JSONObject(response);

                    if (object.getInt("code") == 200) {
                        final String key = object.getString("key");

                        BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth(type, access_token, expires_in, primaryKey);
                        BmobUser.loginWithAuthData(LoginActivity.this, authInfo, new OtherLoginListener() {

                            @Override
                            public void onSuccess(JSONObject userAuth) {
                                T.showShort(LoginActivity.this, "登录成功");

                                if (mDialog.isShowing()) {
                                    mDialog.dismiss();
                                }

                                SessionKeeper.writeSession(LoginActivity.this, key);
                                SessionKeeper.writeUsername(LoginActivity.this, username);

                                setResult(Activity.RESULT_OK);
                                finishActivity();
                            }

                            @Override
                            public void onFailure(int code, String msg) {
                                T.showShort(LoginActivity.this, "登录错误");
                                if (mDialog.isShowing()) {
                                    mDialog.dismiss();
                                }
                            }

                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                T.showShort(LoginActivity.this, error);
            }
        });

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
