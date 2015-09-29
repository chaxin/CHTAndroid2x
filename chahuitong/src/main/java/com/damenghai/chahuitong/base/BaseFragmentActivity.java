package com.damenghai.chahuitong.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.damenghai.chahuitong.R;
import com.pgyersdk.activity.FeedbackActivity;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Sgun on 15/8/26.
 */
public class BaseFragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    protected void onResume() {
        super.onResume();

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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void openActivity(Class<? extends Activity> clazz) {
        openActivity(clazz, null);
    }

    protected void openActivity(Class<? extends Activity> clazz, Bundle extras) {
        Intent intent = new Intent(this, clazz);
        if(extras != null) intent.putExtras(extras);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

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
        finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }
}
