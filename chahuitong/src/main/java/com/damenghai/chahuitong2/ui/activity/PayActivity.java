package com.damenghai.chahuitong2.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.bean.Goods;
import com.damenghai.chahuitong2.bean.Order;
import com.damenghai.chahuitong2.config.SessionKeeper;
import com.damenghai.chahuitong2.pay.alipay.AlipayManager;
import com.damenghai.chahuitong2.pay.wxpay.WxpayManager;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.view.TopBar;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseResp;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class PayActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private TopBar mTopBar;

    private RadioButton mCbAli;

    private RadioButton mCbWx;

    private RadioButton mCbUpmp;

    private RadioButton mCurChecked;

    private TextView mTvTotal;

    private Order mOrder;
    private View mView;
    private ProgressDialog mDialogPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        EventBus.getDefault().register(this);

        mOrder = (Order) getIntent().getSerializableExtra("order");

        bindView();

        initView();
    }

    @Override
    protected void bindView() {
        mTopBar = (TopBar) findViewById(R.id.pay_bar);

        mTvTotal = (TextView) findViewById(R.id.pay_tv_total);

        mCbAli = (RadioButton) findViewById(R.id.pay_rbtn_alipay);
        mCbWx = (RadioButton) findViewById(R.id.pay_rbtn_wxpay);
        mCbUpmp = (RadioButton) findViewById(R.id.pay_rbtn_upmp);
    }

    @Override
    protected void initView() {
        mTopBar.setOnButtonClickListener(new TopBar.OnButtonClickListener() {
            @Override
            public void onLeftClick() {
                finishActivity();
            }

            @Override
            public void onRightClick() {
                goHome();
            }
        });

        mCbAli.setOnCheckedChangeListener(this);
        mCbWx.setOnCheckedChangeListener(this);
        mCbUpmp.setOnCheckedChangeListener(this);

        mCurChecked = mCbAli;

        if (mOrder != null) {
            mTvTotal.setText(mOrder.getOrder_amount());
        }
    }

    public void toPay(final View view) {
        if(mOrder == null || mOrder.getExtend_order_goods() == null) return;

        view.setEnabled(false);
        mDialogPay = new ProgressDialog(this);
        mDialogPay.setCanceledOnTouchOutside(false);

        ArrayList<Goods> goodsList = mOrder.getExtend_order_goods();

        switch (mCurChecked.getId()) {
            case R.id.pay_rbtn_alipay :
                mDialogPay.setMessage(getResources().getString(R.string.dialog_loading_alipay));
                mDialogPay.show();

                StringBuilder title = new StringBuilder();
                for (Goods goods : goodsList) {
                    title.append(goods.getName() + " x" + goods.getGoods_num() + ", ");
                }

                AlipayManager manager = AlipayManager.getInstance(this);
                manager.pay(title.toString(), title.toString(), mOrder.getOrder_amount().replace("￥", ""), mOrder.getPay_sn(), new AlipayManager.AlipayListener() {
                    @Override
                    public void onSuccess() {
                        T.showShort(PayActivity.this, R.string.toast_pay_success);
                        EventBus.getDefault().post(mOrder);
                        Intent intent = new Intent(PayActivity.this, OrderListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                    }

                    @Override
                    public void onConfirming() {
                        T.showShort(PayActivity.this, R.string.toast_pay_comfirming);
                        view.setEnabled(true);
                        if (mDialogPay != null && mDialogPay.isShowing()) mDialogPay.dismiss();
                    }

                    @Override
                    public void onError() {
                        T.showShort(PayActivity.this, R.string.toast_pay_fail);
                        view.setEnabled(true);
                        if (mDialogPay != null && mDialogPay.isShowing()) mDialogPay.dismiss();
                    }
                });
                break;
            case R.id.pay_rbtn_wxpay:
                mDialogPay.setMessage(getResources().getString(R.string.dialog_loading_wxpay));
                mDialogPay.show();
                mView = view;

                WxpayManager wxpayManager = WxpayManager.getInstance(PayActivity.this);
                wxpayManager.pay(goodsList.get(0).getName(), Double.parseDouble(mOrder.getOrder_amount().replace("￥", "")), mOrder.getPay_sn());
                break;
            case R.id.pay_rbtn_upmp:
                String url = "http://www.chahuitong.com/mobile/index.php?act=member_payment&op=pay&key="
                        + SessionKeeper.readSession(PayActivity.this).trim()
                        + "&pay_sn=" + mOrder.getPay_sn().trim() + "&payment_code=yinlian";

                Intent intent = new Intent(PayActivity.this, WebViewActivity.class);
                intent.putExtra("url", url.trim());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
        }
    }

    public void onEventMainThread(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            if (resp.errCode == 0) {
                Intent intent = new Intent(PayActivity.this, OrderListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishActivity();
            } else {
                if(mView != null) mView.setEnabled(true);
                if(mDialogPay != null && mDialogPay.isShowing()) mDialogPay.dismiss();
            }

        }
    }

    public void toChange(View view) {
        switch (view.getId()) {
            case R.id.pay_layout_alipay :
                if(!mCbAli.isChecked()) {
                    mCbAli.setChecked(true);
                }
                break;
            case R.id.pay_layout_wxpay :
                if(!mCbWx.isChecked()) {
                    mCbWx.setChecked(true);
                }
                break;
            case R.id.pay_layout_upmp :
                if(!mCbUpmp.isChecked()) {
                    mCbUpmp.setChecked(true);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (mCurChecked.getId() != compoundButton.getId()) {
            mCurChecked.setChecked(false);
            mCurChecked = (RadioButton) compoundButton;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
