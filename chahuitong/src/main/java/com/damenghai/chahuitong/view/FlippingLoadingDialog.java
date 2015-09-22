package com.damenghai.chahuitong.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.config.Constants;

/**
 * 正在加载对话框
 *
 * Created by Sgun on 15/9/11.
 */
public class FlippingLoadingDialog extends Dialog {
    public FlippingLoadingDialog(Context context) {
        this(context, "");
    }

    public FlippingLoadingDialog(Context context, String text) {
        super(context, R.style.DialogLoadding);
        setContentView(R.layout.flipping_loading_dialog);
        if(!text.equals("")) {
            TextView textView = (TextView) findViewById(R.id.loading_text);
            textView.setText(text);
        }
    }
}
