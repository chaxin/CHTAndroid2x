package com.damenghai.chahuitong.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.config.Constants;
import com.damenghai.chahuitong.ui.activity.HomeActivity;
import com.damenghai.chahuitong.utils.ShareManager;
import com.pgyersdk.activity.FeedbackActivity;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity extends Activity {
    protected SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences(Constants.SHARED_PREFERERENCE_NAME, Context.MODE_PRIVATE);

        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    protected abstract void findViewById();

    protected abstract void initView();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**用于微博分享的回调 */
        UMSsoHandler ssoHandler = ShareManager.mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
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

        // 自定义摇一摇的灵敏度，默认为950，数值越小灵敏度越高。
        PgyFeedbackShakeManager.setShakingThreshold(1200);

        // 以Activity的形式打开，这种情况下必须在AndroidManifest.xml配置FeedbackActivity
        // 打开沉浸式,默认为false
        // FeedbackActivity.setBarImmersive(true);
        PgyFeedbackShakeManager.register(this, false);

        // 设置顶部导航栏和底部bar的颜色
        FeedbackActivity.setBarBackgroundColor("#1b8b80");

        // 设置顶部按钮和底部按钮按下时的反馈色
        FeedbackActivity.setBarButtonPressedColor("#166f66");

        // 设置颜色选择器的背景色
        FeedbackActivity.setColorPickerBackgroundColor("#1b8b80");
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
