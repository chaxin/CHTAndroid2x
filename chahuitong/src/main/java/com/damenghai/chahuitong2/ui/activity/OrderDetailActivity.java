package com.damenghai.chahuitong2.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.GoodsListAdapter;
import com.damenghai.chahuitong2.api.OrderApi;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.bean.Goods;
import com.damenghai.chahuitong2.bean.Order;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.utils.DateUtils;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.view.TopBar;
import com.damenghai.chahuitong2.view.WrapHeightListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class OrderDetailActivity extends BaseActivity {
    private TopBar mTopBar;

    private Order mOrder;

    private TextView mTvState;

    private TextView mTvStore;

    private WrapHeightListView mLvGoods;

    private TextView mTvFreight;

    private TextView mTvTotal;

    private TextView mTvOrderNo;

    private TextView mTvPayNo;

    private TextView mTvCreateTime;

    private LinearLayout mLayoutBtn;

    private Button mBtnLeft;

    private Button mBtnRight;

    private GoodsListAdapter mAdapter;

    private ArrayList<Goods> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        mOrder = (Order) getIntent().getSerializableExtra("order");

        bindView();

        initView();
    }

    @Override
    protected void bindView() {
        mTopBar = (TopBar) findViewById(R.id.order_detail_bar);
        mTvState = (TextView) findViewById(R.id.order_detail_tv_state);
        mTvStore = (TextView) findViewById(R.id.order_detail_tv_store);
        mLvGoods = (WrapHeightListView) findViewById(R.id.order_detail_goods_list);
        mTvFreight = (TextView) findViewById(R.id.order_detail_tv_freight);
        mTvTotal = (TextView) findViewById(R.id.order_detail_tv_total);
        mTvOrderNo = (TextView) findViewById(R.id.order_detail_tv_order_num);
        mTvPayNo = (TextView) findViewById(R.id.order_detail_tv_pay_num);
        mTvCreateTime = (TextView) findViewById(R.id.order_detail_tv_time);

        mLayoutBtn = (LinearLayout) findViewById(R.id.order_detail_btn_layout);
        mBtnLeft = (Button) findViewById(R.id.order_detail_btn_left);
        mBtnRight = (Button) findViewById(R.id.order_detail_btn_right);
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

        if (mOrder != null) {
            mTvState.setText(mOrder.getState_desc());
            mTvStore.setText(mOrder.getStore_name());
            mTvFreight.setText(mOrder.getShipping_fee());
            mTvTotal.setText(mOrder.getOrder_amount());
            mTvOrderNo.setText(getResources().getString(R.string.label_order_num) + mOrder.getOrder_sn());
            mTvPayNo.setText(getResources().getString(R.string.label_pay_num) + mOrder.getPay_sn());
            mTvCreateTime.setText(getResources().getString(R.string.label_time_create) + DateUtils.getDateTime(mOrder.getAdd_time()));
            mData = mOrder.getExtend_order_goods();
            if (mData != null) {
                mAdapter = new GoodsListAdapter(this, mData, R.layout.item_list_goods);
                mLvGoods.setAdapter(mAdapter);
            }

            switch (mOrder.getOrder_state()) {
                case OrderListActivity.STATE_UNPAID :
                    mBtnLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cancelOrder();
                        }
                    });
                    mBtnRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            payOrder();
                        }
                    });
                    break;
                case OrderListActivity.STATE_RECEIVE :
                    mBtnLeft.setText(R.string.btn_view_delivery);
                    mBtnLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewDelivery();
                        }
                    });

                    mBtnRight.setText(R.string.btn_sure_order);
                    mBtnRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sureOrder();
                        }
                    });
                    break;
                case OrderListActivity.STATE_UNCOMMENT :
                    mBtnLeft.setText(R.string.btn_view_delivery);
                    mBtnLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewDelivery();
                        }
                    });

                    mBtnRight.setText(R.string.btn_comment);
                    mBtnRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            comment();
                        }
                    });
                    break;
                default:
                    mLayoutBtn.setVisibility(View.GONE);
                    break;
            }
        }
    }

    public void payOrder() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("order", mOrder);
        openActivity(PayActivity.class, bundle);
    }

    public void cancelOrder() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(R.string.dialog_cancel_order)
                .setNegativeButton(R.string.btn_cancel, null)
                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        OrderApi.cancelOrder(OrderDetailActivity.this, mOrder.getOrder_id(), new VolleyRequest() {
                            @Override
                            public void onSuccess(String response) {
                                super.onSuccess(response);
                                try {
                                    JSONObject object = new JSONObject(response);
                                    if (!object.has("error")) {
                                        T.showShort(OrderDetailActivity.this, R.string.toast_cancel_success);
                                        EventBus.getDefault().post(mOrder);
                                        mLayoutBtn.setVisibility(View.GONE);
                                    } else {
                                        T.showShort(OrderDetailActivity.this, object.getString("error"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                }).show();

    }

    public void viewDelivery() {
        Bundle bundle = new Bundle();
        bundle.putString("url", "http://www.chahuitong.com/wap/tmpl/member/order_delivery.html?order_id=" + mOrder.getOrder_id());
        openActivity(WebViewActivity.class, bundle);
    }

    public void sureOrder() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(R.string.dialog_sure_order)
                .setNegativeButton(R.string.btn_cancel, null)
                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        OrderApi.sureOrder(OrderDetailActivity.this, mOrder.getOrder_id(), new VolleyRequest() {
                            @Override
                            public void onSuccess(String response) {
                                super.onSuccess(response);
                                try {
                                    JSONObject object = new JSONObject(response);
                                    if (object.has("error")) {
                                        T.showShort(OrderDetailActivity.this, object.getString("error"));
                                    } else {
                                        T.showShort(OrderDetailActivity.this, R.string.toast_sure_success);
                                        EventBus.getDefault().post(mOrder);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                })
                .show();
    }

    public void comment() {
        Bundle bundle = new Bundle();
        bundle.putString("url", "http://www.chahuitong.com/wap/index.php/Home/Index/pingjiaorder/oid/" + mOrder.getOrder_id());
        openActivity(WebViewActivity.class, bundle);
    }

    public void toCall(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:05925990900"));
        startActivity(intent);
    }
}
