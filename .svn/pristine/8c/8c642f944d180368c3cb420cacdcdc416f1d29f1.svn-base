package com.damenghai.chahuitong.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.damenghai.chahuitong.R;

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
}
