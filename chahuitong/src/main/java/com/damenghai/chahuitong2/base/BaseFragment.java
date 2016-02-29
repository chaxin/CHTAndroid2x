package com.damenghai.chahuitong2.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.damenghai.chahuitong2.R;

/**
 * Created by Sgun on 15/8/12.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void openActivity(Class<? extends Activity> clazz) {
        openActivity(clazz, null);
    }

    protected void openActivity(Class<? extends Activity> clazz, Bundle extras) {
        Intent intent = new Intent(getActivity(), clazz);
        if(extras != null) intent.putExtras(extras);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
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
        Intent intent = new Intent(getActivity(), clazz);
        if(bundle != null) intent.putExtras(bundle);
        this.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

}
