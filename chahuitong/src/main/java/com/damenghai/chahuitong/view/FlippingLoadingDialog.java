package com.damenghai.chahuitong.view;

import android.app.Dialog;
import android.content.Context;

import com.damenghai.chahuitong.R;

/**
 * 正在加载对话框
 *
 * Created by Sgun on 15/9/11.
 */
public class FlippingLoadingDialog extends Dialog {
    public FlippingLoadingDialog(Context context) {
        super(context, R.style.DialogLoadding);
        setContentView(R.layout.flipping_loading_dialog);
    }
}
