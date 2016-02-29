package com.damenghai.chahuitong2.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.api.OrderApi;
import com.damenghai.chahuitong2.bean.Order;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.ui.activity.OrderDetailActivity;
import com.damenghai.chahuitong2.ui.activity.OrderListActivity;
import com.damenghai.chahuitong2.ui.activity.PayActivity;
import com.damenghai.chahuitong2.ui.activity.WebViewActivity;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.utils.ViewHolder;
import com.damenghai.chahuitong2.view.WrapHeightListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class OrderListAdapter extends BaseListAdapter<Order> {

    private GoodsListAdapter mGoodsListAdapter;

    public OrderListAdapter(Context context, List<Order> data, int resId) {
        super(context, data, resId);
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getOrder_state();
    }

    @Override
    public void convert(ViewHolder holder, final Order order) {
        holder.setText(R.id.order_tv_store, order.getStore_name())
                .setText(R.id.order_tv_state, order.getState_desc())
                .setText(R.id.order_tv_count, "共" + order.getExtend_order_goods().size() + "件商品")
                .setText(R.id.order_tv_total, order.getOrder_amount());

        WrapHeightListView lv = holder.getView(R.id.order_lv_goods);
        if(order.getExtend_order_goods() != null) {
            mGoodsListAdapter = new GoodsListAdapter(mContext, order.getExtend_order_goods(), R.layout.item_list_goods);
            lv.setAdapter(mGoodsListAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order", order);
                    openActivity(OrderDetailActivity.class, bundle);
                }
            });
        }

        switch (getItemViewType(holder.getPosition())) {
            case OrderListActivity.STATE_UNPAID :
                // 待付款
                holder.setVisibility(R.id.order_btn_left, View.VISIBLE)
                        .setVisibility(R.id.order_btn_right, View.VISIBLE)
                        .setOnClickListener(R.id.order_btn_left, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                                dialog.setMessage(R.string.dialog_cancel_order).setNegativeButton(R.string.btn_cancel, null)
                                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                OrderApi.cancelOrder(mContext, order.getOrder_id(), new VolleyRequest() {
                                                    @Override
                                                    public void onSuccess(String response) {
                                                        super.onSuccess(response);
                                                        try {
                                                            JSONObject object = new JSONObject(response);
                                                            if (!object.has("error")) {
                                                                T.showShort(mContext, R.string.toast_cancel_success);
                                                                EventBus.getDefault().post(order);
                                                            } else {
                                                                T.showShort(mContext, object.getString("error"));
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });

                                            }
                                        }).show();

                            }
                        }).setOnClickListener(R.id.order_btn_right, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("order", order);
                        openActivity(PayActivity.class, bundle);
                    }
                });
                break;
            case OrderListActivity.STATE_PAID :
                // 待发货
                holder.setVisibility(R.id.order_btn_left, View.GONE)
                        .setVisibility(R.id.order_btn_right, View.GONE);
                break;
            case OrderListActivity.STATE_RECEIVE :
                // 待收货 查看物流：http://www.chahuitong.com/wap/tmpl/member/order_delivery.html?order_id=267
                holder.setVisibility(R.id.order_btn_left, View.VISIBLE)
                        .setText(R.id.order_btn_left, R.string.btn_view_delivery)
                        .setOnClickListener(R.id.order_btn_left, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle bundle = new Bundle();
                                bundle.putString("url", "http://www.chahuitong.com/wap/tmpl/member/order_delivery.html?order_id=" + order.getOrder_id());
                                openActivity(WebViewActivity.class, bundle);
                            }
                        })
                        .setVisibility(R.id.order_btn_right, View.VISIBLE)
                        .setText(R.id.order_btn_right, R.string.btn_sure_order)
                        .setOnClickListener(R.id.order_btn_right, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                                dialog.setMessage(R.string.dialog_sure_order)
                                        .setNegativeButton(R.string.btn_cancel, null)
                                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                OrderApi.sureOrder(mContext, order.getOrder_id(), new VolleyRequest() {
                                                    @Override
                                                    public void onSuccess(String response) {
                                                        super.onSuccess(response);
                                                        try {
                                                            JSONObject object = new JSONObject(response);
                                                            if (object.has("error")) {
                                                                T.showShort(mContext, object.getString("error"));
                                                            } else {
                                                                T.showShort(mContext, R.string.toast_sure_success);
                                                                EventBus.getDefault().post(order);
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
                        });
                break;
            case OrderListActivity.STATE_UNCOMMENT :
                holder.setVisibility(R.id.order_btn_left, View.VISIBLE)
                        .setText(R.id.order_btn_left, R.string.btn_view_delivery)
                        .setOnClickListener(R.id.order_btn_left, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle bundle = new Bundle();
                                bundle.putString("url", "http://www.chahuitong.com/wap/tmpl/member/order_delivery.html?order_id=" + order.getOrder_id());
                                openActivity(WebViewActivity.class, bundle);
                            }
                        })
                        .setVisibility(R.id.order_btn_right, View.VISIBLE)
                        .setText(R.id.order_btn_right, R.string.btn_comment)
                        .setOnClickListener(R.id.order_btn_right, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle bundle = new Bundle();
                                bundle.putString("url", "http://www.chahuitong.com/wap/index.php/Home/Index/pingjiaorder/oid/" + order.getOrder_id());
                                openActivity(WebViewActivity.class, bundle);
                            }
                        });
                break;
            default :
                // 其他
                holder.setVisibility(R.id.order_btn_left, View.GONE)
                        .setVisibility(R.id.order_btn_right, View.GONE);
                break;
        }

    }

}

