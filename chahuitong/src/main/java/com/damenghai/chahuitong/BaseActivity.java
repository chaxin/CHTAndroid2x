package com.damenghai.chahuitong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.damenghai.chahuitong.config.Constants;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends Activity {
    protected SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        sp = getSharedPreferences(Constants.SHARED_PREFERERENCE_NAME, Context.MODE_PRIVATE);

        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 设置任意网络环境下都提示更新
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        // 友盟自动检测更新
        UmengUpdateAgent.update(this);
        // bugly初始化
        CrashReport.initCrashReport(getApplicationContext(), "101172467", false);
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
        AppManager.getInstance().removeActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 通过类名启动Activity
     * @param pClass
     */
    public void openActivity(Class<? extends Activity> pClass) {
        openActivity(pClass, null);
    }

    /**
     * 通过类名启动Activity,并含有Bundle数据
     * @param pClass
     * @param pBundle
     */
    public void openActivity(Class<? extends Activity> pClass, Bundle pBundle) {
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
    public void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    /**
     * 通过Action启动Activity,并含有Bundle数据
     * @param pAction
     * @param pBundle
     */
    public void openActivity(String pAction, Bundle pBundle) {
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
    public void openResultActivity(Class<? extends Activity> clazz, int requestCode) {
        openResultActivity(clazz, requestCode, null);
    }

    public void openResultActivity(Class<? extends Activity> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if(bundle != null) intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }
}
