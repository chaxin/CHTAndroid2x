package com.damenghai.chahuitong.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.api.UserAPI;
import com.damenghai.chahuitong.base.BaseFragment;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.T;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sgun on 15/9/23.
 */
public class RegisterFragment extends Fragment implements OnClickListener, TextWatcher {
    private int mCode;
    private CountTimer mTimer;
    
    private View mView;
    private EditText mEtPassword;
    private Button mBtnRegister;
    private EditText mEtPhone;
    private EditText mEtCode;
    private Button mBtSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_register, null);
        
        initControl();
        
        initView();
        
        return mView;
    }

    public void initControl() {
        mEtPhone = (EditText) mView.findViewById(R.id.register_phone);
        mEtCode = (EditText) mView.findViewById(R.id.register_code);
        mEtPassword = (EditText) mView.findViewById(R.id.register_password);

        mBtSend = (Button) mView.findViewById(R.id.register_send);
        mBtnRegister = (Button) mView.findViewById(R.id.id_btn_register);
    }
    
    public void initView() {
        mBtSend.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        // 监听输入手机号码
        mEtPhone.addTextChangedListener(this);
    }

    private void getCode() {
        String mobile = mEtPhone.getText().toString();

        mCode = (int)((Math.random()*9+1)*100000);

        UserAPI.sendSMS(mobile, mCode, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                try {
                    Document doc = DocumentHelper.parseText(response);
                    Element root = doc.getRootElement();

                    String code = root.elementText("code");
                    String msg = root.elementText("msg");

                    if (!"2".equals(code)) {
                        T.showShort(getActivity(), msg);
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
                String password = mEtPassword.getText().toString();
                String code = mEtCode.getText().toString();

                if(TextUtils.isEmpty(mobile) || mobile.length() != 11) {
                    T.showShort(getActivity(), "请填写正确的手机号码");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    T.showShort(getActivity(), "请填写密码");
                    return;
                }
                if(TextUtils.isEmpty(code)) {
                    T.showShort(getActivity(), "请填写手机号后获取验证码");
                    return;
                }

                if(!code.equals(mCode + "")) {
                    T.showShort(getActivity(), "验证码错误");
                    return;
                }

                UserAPI.register(mobile, password, new VolleyRequest() {
                    @Override
                    public void onSuccess(String response) {
                        super.onSuccess(response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getInt("code") == 200) {
                                T.showShort(getActivity(), "注册成功");
                                SessionKeeper.writeSession(getActivity(), obj.getString("key"));
                                SessionKeeper.writeUsername(getActivity(), "username");
                                getActivity().setResult(Activity.RESULT_OK);
                                getActivity().finish();
                            } else {
                                T.showShort(getActivity(), obj.getString("content"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

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
