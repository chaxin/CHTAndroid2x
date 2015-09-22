package com.damenghai.chahuitong.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.L;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

/**
 * Created by Sgun on 15/9/15.
 */
public class PaymentActivity extends BaseActivity {
    // 00 银联正式环境，银联测试环境，该环境不产生真实交易
    private String mMode = "01";
    private static final String TN_URL_01 = "http://202.101.25.178:8080/sim/gettn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        getTn();
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {

    }

    private void getTn() {
        /*************************************************
         * 步骤1：从网络开始,获取交易流水号即TN
         ************************************************/
//        HodorAPI.getTn("20", new VolleyRequest() {
//            @Override
//            public void onSuccess(String response) {
//                super.onSuccess(response);
//                if(response != null && !response.equals("")) {
//                    /*************************************************
//                     * 步骤2：通过银联工具类启动支付插件
//                     ************************************************/
//                    doStartUnionPayPlugin(response);
//                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
//                    builder.setTitle("错误提示");
//                    builder.setTitle("错误提示");
//                    builder.setMessage("网络连接失败,请重试!");
//                    builder.setNegativeButton("确定",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    builder.create().show();
//                }
//            }
//        });
    }

    private void doStartUnionPayPlugin(String tn) {
        UPPayAssistEx.startPayByJAR(PaymentActivity.this, PayActivity.class, null, null, tn, mMode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*************************************************
         * 步骤3：处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        // builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
