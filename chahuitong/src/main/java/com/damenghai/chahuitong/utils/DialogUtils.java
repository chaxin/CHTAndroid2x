package com.damenghai.chahuitong.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.view.FlippingLoadingDialog;

/**
 * Created by Sgun on 15/8/12.
 */
public class DialogUtils {


    /**
     * 创建正在加载对话框
     * @param context
     * @return
     */
    public static Dialog createLoadingDialog(Context context) {
        return new FlippingLoadingDialog(context);
    }

    public static void dismiss(Context context) {
        new FlippingLoadingDialog(context).dismiss();
    }
}
