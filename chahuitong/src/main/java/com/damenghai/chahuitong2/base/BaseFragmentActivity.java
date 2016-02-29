package com.damenghai.chahuitong2.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.ui.activity.HomeActivity;
import com.damenghai.chahuitong2.utils.ShareManager;
import com.umeng.socialize.sso.UMSsoHandler;

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
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

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

    protected void goHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
