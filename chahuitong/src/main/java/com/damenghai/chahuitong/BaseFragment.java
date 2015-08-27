package com.damenghai.chahuitong;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.damenghai.chahuitong.utils.DialogUtils;

/**
 * Created by Sgun on 15/8/12.
 */
public class BaseFragment extends Fragment {
    Dialog dialogLoadding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogLoadding = DialogUtils.createLoadingDialog(getActivity());
    }

    protected void openActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    protected void openActivity(Class<? extends Activity> clazz, Bundle extras) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtras(extras);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }
}
