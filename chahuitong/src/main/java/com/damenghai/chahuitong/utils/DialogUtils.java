package com.damenghai.chahuitong.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import com.damenghai.chahuitong.R;

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
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.DialogLoadding);
        return  progressDialog;
    }
}
