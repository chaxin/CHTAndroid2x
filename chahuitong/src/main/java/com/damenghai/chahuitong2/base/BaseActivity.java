package com.damenghai.chahuitong2.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.ui.activity.HomeActivity;
import com.damenghai.chahuitong2.utils.ShareManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.sso.UMSsoHandler;

import cn.bmob.im.BmobUserManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity extends Activity {
    // Bmob用户管理
    protected BmobUserManager mUserManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mUserManager = BmobUserManager.getInstance(this);
    }

    protected abstract void bindView();

    protected abstract void initView();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /* 用于微博分享的回调 */
        if(ShareManager.mController != null) {
            UMSsoHandler ssoHandler = ShareManager.mController.getConfig().getSsoHandler(requestCode);
            if (ssoHandler != null) {
                ssoHandler.authorizeCallBack(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 通过类名启动Activity
     * @param pClass
     */
    protected void openActivity(Class<? extends Activity> pClass) {
        openActivity(pClass, null);
    }

    /**
     * 通过类名启动Activity,并含有Bundle数据
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<? extends Activity> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if(pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    /**
     * 通过Action启动Activity
     * @param pAction
     */
    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    /**
     * 通过Action启动Activity,并含有Bundle数据
     * @param pAction
     * @param pBundle
     */
    protected void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if(pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    /**
     * 回调函数启动Activity
     *
     * @param clazz
     * @param requestCode
     */
    protected void openActivityForResult(Class<? extends Activity> clazz, int requestCode) {
        openActivityForResult(clazz, requestCode, null);
    }

    protected void openActivityForResult(Class<? extends Activity> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if(bundle != null) intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    protected void finishActivity() {
        this.finish();
        this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    protected void goHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }
}
