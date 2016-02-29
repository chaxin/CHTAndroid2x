package com.damenghai.chahuitong2.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.BaseListAdapter;
import com.damenghai.chahuitong2.api.OrderApi;
import com.damenghai.chahuitong2.bean.Address;
import com.damenghai.chahuitong2.bean.Goods;
import com.damenghai.chahuitong2.bean.Order;
import com.damenghai.chahuitong2.bean.Store;
import com.damenghai.chahuitong2.config.SessionKeeper;
import com.damenghai.chahuitong2.pay.alipay.AlipayManager;
import com.damenghai.chahuitong2.pay.wxpay.WxpayManager;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.ui.fragment.AddAddressFragment;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.utils.ViewHolder;
import com.damenghai.chahuitong2.view.QuantityView;
import com.damenghai.chahuitong2.view.TopBar;
import com.damenghai.chahuitong2.view.WrapHeightListView;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseResp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class OrderActivity extends FragmentActivity {
    @ViewInject(R.id.order_bar)
    private TopBar mTopBar;

    @ViewInject(R.id.order_address_container)
    private FrameLayout mContainerAddress;

    @ViewInject(R.id.order_ll_address)
    private LinearLayout mLayoutAddress;

    @ViewInject(R.id.order_tv_consignee)
    private TextView mTvConsignee;

    @ViewInject(R.id.order_tv_address)
    private TextView mTvAddress;

    @ViewInject(R.id.store_tv_name)
    private TextView mTvStoreName;

    @ViewInject(R.id.store_tv_total_count)
    private TextView mTvStoreCount;

    @ViewInject(R.id.store_tv_total)
    private TextView mTvStoreTotal;

    @ViewInject(R.id.order_group_pay)
    private RadioGroup mGroupPay;

    @ViewInject(R.id.store_lv_goods)
    private WrapHeightListView mListView;

    @ViewInject(R.id.order_tv_pay_total)
    private TextView mTvOrderTotal;

    private String mGoodsId;

    private String mAddressId;

    private Address mAddress;

    private String mCartId;

    private ArrayList<Goods> mGoodses;

    private GoodsListAdapter mAdapter;

    private String mVatHash;
    private String mOffPayHash;
    private String mOffPayHashBatch;
    private String mFreightHash;

    public static OrderActivity mInstance;

    private OrderApi mApi;

    public static String mPaySn;

    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ViewUtils.inject(this);

        EventBus.getDefault().register(this);

        mInstance = OrderActivity.this;

        mGoodsId = getIntent().getStringExtra("goods_id");

        mCartId = getIntent().getStringExtra("cart_id");

        initView();

        loadData();
    }

    protected void initView() {
        mTopBar.setOnButtonClickListener(new TopBar.OnButtonClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                Intent intent = new Intent(OrderActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        mGoodses = new ArrayList<>();
        mAdapter = new GoodsListAdapter(this, mGoodses, R.layout.item_list_goods);
        mListView.setAdapter(mAdapter);

        mApi = new OrderApi(this);
    }

    private void loadData() {
        String cartId;
        String ifCart = "";
        if(!TextUtils.isEmpty(mCartId)) {
            cartId = mCartId;
            ifCart = "1";
        } else if(!TextUtils.isEmpty(mGoodsId)) {
            cartId = mGoodsId + "|1";
        } else {
            T.showShort(this, "获取数据出错");
            return;
        }
        OrderApi.getOrderInfo(this, cartId, ifCart, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.getInt("code") == 200) {
                        JSONObject datas = object.getJSONObject("datas");

                        String addressInfo = datas.getString("address_info").replace("[]", "");
                        if (!TextUtils.isEmpty(addressInfo)) {
                            mAddress = new Gson().fromJson(addressInfo, Address.class);
                            onEventMainThread(mAddress);
                        } else {
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            AddAddressFragment fragment = new AddAddressFragment();
                            ft.replace(R.id.order_address_container, fragment);
                            ft.commit();
                            mContainerAddress.setVisibility(View.VISIBLE);
                            mLayoutAddress.setVisibility(View.GONE);
                        }

                        mOffPayHash = datas.getString("offpay_hash");
                        mOffPayHashBatch = datas.getString("offpay_hash_batch");
                        mVatHash = datas.getString("vat_hash");
                        mFreightHash = datas.getString("freight_hash");

                        JSONObject storeList = datas.getJSONObject("store_cart_list");
                        Store store = new Gson().fromJson(storeList.getString("2"), Store.class);
                        setStore(store);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void onEventMainThread(Address address) {
        mAddressId = address.getAddress_id();
        mLayoutAddress.setVisibility(View.VISIBLE);
        mContainerAddress.setVisibility(View.GONE);
        mTvConsignee.setText(getResources().getString(R.string.label_consignee) + address.getTrue_name() + " " + address.getMob_phone());
        mTvAddress.setText(getResources().getString(R.string.label_address) + address.getArea_info() + " " + address.getAddress());
        OrderApi.changeAddress(this, address.getCity_id(), address.getArea_id(), mFreightHash, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                JSONObject object;
                try {
                    object = new JSONObject(response);
                    JSONObject datas = object.getJSONObject("datas");
                    if (!datas.has("error")) {
                        mOffPayHash = datas.getString("offpay_hash");
                        mOffPayHashBatch = datas.getString("offpay_hash_batch");
                    } else {
                        T.showShort(OrderActivity.this, datas.getString("error"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    // 微信支付回调
    // 0	成功	展示成功页面
    //-1	错误	可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
    //-2	用户取消	无需处理。发生场景：用户不支付了，点击取消，返回APP。
    public void onEventMainThread(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if(resp.errCode == 0) {
                openActivity(OrderListActivity.class, null, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable("order", mOrder);
                openActivity(PayActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            finish();
        }
    }

    private void setStore(Store store) {
        ArrayList<Goods> goodsList = store.getGoods_list();
        mTvStoreName.setText(store.getStore_name());
        mTvStoreCount.setText("小计(共" + goodsList.size() + "件)");
        mTvStoreTotal.setText("￥" + store.getStore_goods_total());

        for (Goods goods : goodsList) {
            if(!mGoodses.contains(goods)) {
                mGoodses.add(goods);
            }
        }
        mAdapter.notifyDataSetChanged();

        mTvOrderTotal.setText("￥" + store.getStore_goods_total().trim());
    }

    public void toChangeAddress(View view) {
        Intent intent = new Intent(OrderActivity.this, AddressListActivity.class);
        intent.putExtra("state", "choose");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    public void toPay(View view) {
        String cartId;
        String ifCart = "0";
        if(TextUtils.isEmpty(mCartId)) {
            cartId = mGoodsId + "|" + mAdapter.getGoodsCount();
        } else {
            cartId = mCartId;
            ifCart = "1";
        }

        view.setEnabled(false);

        OrderApi.getPaySn(this, ifCart, cartId, mAddressId, mVatHash, mFreightHash, mOffPayHash, mOffPayHashBatch, new VolleyRequest() {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject datas = object.getJSONObject("datas");
                    if (datas.has("error")) {
                        T.showShort(OrderActivity.this, datas.getString("error"));
                    } else {
                        mPaySn = datas.getString("pay_sn");
                        pay(mPaySn);

                        mOrder = new Gson().fromJson(datas.getString("order"), Order.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public void pay(final String paySn) {
        if(TextUtils.isEmpty(paySn)) {
            T.showShort(this, "生成订单号出错啦");
            return;
        }

        StringBuilder title = new StringBuilder();
        StringBuilder body = new StringBuilder();
        for(int i=0; i<mAdapter.getCount(); i++) {
            Goods goods = mAdapter.getList().get(i);
            title.append(goods.getName());
            if(i < mAdapter.getCount() - 1) {
                body.append(goods.getName() + " + ");
            } else {
                body.append(goods.getName());
            }
        }

        double price = Double.parseDouble(mTvOrderTotal.getText().toString().replace("￥", ""));

        switch (mGroupPay.getCheckedRadioButtonId()) {
            case R.id.order_rbtn_alipay :
                AlipayManager mManager = AlipayManager.getInstance(this);
                mManager.pay(title.toString(), body.toString(), price + "", paySn, new AlipayManager.AlipayListener() {
                    @Override
                    public void onSuccess() {
                        T.showShort(OrderActivity.this, "支付成功");
                        openActivity(OrderListActivity.class, null, 0);
                        finish();
                    }

                    @Override
                    public void onConfirming() {
                        T.showShort(OrderActivity.this, "支付结果确认中");
                    }

                    @Override
                    public void onError() {
                        T.showShort(OrderActivity.this, "支付失败");
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("order", mOrder);
                        openActivity(PayActivity.class, bundle, 0);
                        finish();
                    }
                });
                break;
            case R.id.order_rbtn_wxpay :
                WxpayManager manager = WxpayManager.getInstance(this);
                manager.pay(mAdapter.getItem(0).getName(), price, paySn);
                break;
            case R.id.order_rbtn_upmp :
                String url = "http://www.chahuitong.com/mobile/index.php?act=member_payment&op=pay&key="
                        + SessionKeeper.readSession(OrderActivity.this).trim()
                        + "&pay_sn=" + paySn.trim() + "&payment_code=yinlian";
                Bundle bundle = new Bundle();
                bundle.putString("url", url.trim());
                openActivity(WebViewActivity.class, bundle, 0);
                finish();
                break;
        }
    }

    private void openActivity(Class<? extends Activity> clazz, Bundle bundle, int flags) {
        Intent intent = new Intent(OrderActivity.this, clazz);
        if (flags != 0) intent.setFlags(flags);
        if (bundle != null) intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private class GoodsListAdapter extends BaseListAdapter<Goods> {

        private String mCount;

        public GoodsListAdapter(Context context, List<Goods> data, int resId) {
            super(context, data, resId);
        }

        public String getGoodsCount() {
            return mCount;
        }

        @Override
        public void convert(ViewHolder holder, Goods goods) {

            if(TextUtils.isEmpty(mCartId)) {
                holder.setVisibility(R.id.goods_count_view, View.VISIBLE)
                        .setVisibility(R.id.goods_tv_count, View.GONE);

                final int price = (int) Double.parseDouble(goods.getGoods_price());

                QuantityView viewCount = holder.getView(R.id.goods_count_view);
                mCount = goods.getGoods_num();
                viewCount.setCount(goods.getGoods_num());
                viewCount.setOnCountChangedListener(new QuantityView.OnCountChangedListener() {
                    @Override
                    public void countChanged(int count) {
                        mCount = count + "";
                        mTvStoreCount.setText("共" + count + "件商品：");
                        mTvStoreTotal.setText("￥" + (count * price));
                        mTvOrderTotal.setText("￥" + (count * price));
                    }
                });
            } else {
                holder.setText(R.id.goods_tv_count, "x" + goods.getGoods_num())
                        .setVisibility(R.id.goods_count_view, View.GONE)
                        .setVisibility(R.id.goods_tv_count, View.VISIBLE);
            }

            holder.loadImage(R.id.goods_iv_thumb, goods.getGoods_image_url())
                    .setText(R.id.goods_tv_title, goods.getName())
                    .setText(R.id.goods_tv_price, "￥" + goods.getGoods_price());

            }

    }
}
