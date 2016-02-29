package com.damenghai.chahuitong2.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.BaseListAdapter;
import com.damenghai.chahuitong2.api.PersonalAPI;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.bean.Goods;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.utils.ViewHolder;
import com.damenghai.chahuitong2.view.QuantityView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class CartActivity extends BaseActivity {

    private ListView mListView;

    private List<Goods> mGoodsList;

    private CartListAdapter mAdapter;

    private Button mBalance;

    private CheckBox mCheckAll;

    private TextView mTvTotal;

    private int mTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        bindView();

        initView();

        loadData();
    }

    @Override
    protected void bindView() {
        mListView = (ListView) findViewById(R.id.cart_list);

        mCheckAll = (CheckBox) findViewById(R.id.cart_cb_all);

        mBalance = (Button) findViewById(R.id.cart_btn_balance);

        mTvTotal = (TextView) findViewById(R.id.cart_tv_total);
    }

    @Override
    protected void initView() {
        mGoodsList = new ArrayList<Goods>();
        mAdapter = new CartListAdapter(this, mGoodsList, R.layout.item_list_goods);
        mListView.setAdapter(mAdapter);
        mCheckAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mAdapter.ChangeState(b);
            }
        });
    }

    private void loadData() {
        PersonalAPI.cartList(this, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject datas = object.getJSONObject("datas");
                    mTotal = datas.getInt("sum");
                    JSONArray cartList = datas.getJSONArray("cart_list");
                    for (int i = 0; i < cartList.length(); i++) {
                        Goods goods = new Gson().fromJson(cartList.getString(i), Goods.class);
                        if (!mGoodsList.contains(goods)) mGoodsList.add(goods);
                    }
                    mAdapter.notifyDataSetChanged();
                    mBalance.setText("结算(" + mGoodsList.size() + "" + ")");
                    mTvTotal.setText(mTotal + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 监听计算价格变化
     *
     * @param checkBoxes
     *                  存放所有CheckBox
     * @param count
     *                  变化的产品数量，如果为负表示减少，正数为增加
     * @param price
     *                  变化的产品单价
     */
    public void checkedChanged(ArrayList<CheckBox> checkBoxes, int count, String price) {
        if(allState(checkBoxes, true)) {
            mCheckAll.setChecked(true);
        } else {
            mCheckAll.setChecked(false);
        }

        mTotal = (int) (mTotal + (count * (Double.parseDouble(price))));
        mTvTotal.setText(mTotal + "");
    }

    public boolean allState(ArrayList<CheckBox> checkBoxes, boolean state) {
        for(CheckBox cb : checkBoxes) {
            if(cb.isChecked() != state) {
                return false;
            }
        }
        return true;
    }

    public void ChangeState(ArrayList<CheckBox> checkBoxes, boolean state) {
        if(allState(checkBoxes, true) || allState(checkBoxes, false)) {
            for(CheckBox cb : checkBoxes) {
                cb.setChecked(state);
            }
        }
    }

    private class CartListAdapter extends BaseListAdapter<Goods> {

        ArrayList<CheckBox> mPos;

        public CartListAdapter(Context context, List<Goods> data, int resId) {
            super(context, data, resId);
            mPos = new ArrayList<CheckBox>();
        }

        public void ChangeState(boolean state) {
            if(allState(true) || allState(false)) {
                for(CheckBox cb : mPos) {
                    cb.setChecked(state);
                }
            }
        }

        public boolean allState(boolean state) {
            for(CheckBox cb : mPos) {
                if(cb.isChecked() != state) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void convert(final ViewHolder holder, final Goods goods) {
            final double price = Double.parseDouble(goods.getGoods_price());
            holder.loadImage(R.id.goods_iv_thumb, goods.getGoods_image_url())
                    .setText(R.id.goods_tv_title, goods.getName())
                    .setText(R.id.goods_tv_price, goods.getGoods_price());

            final CheckBox cb = holder.getView(R.id.goods_cb);
            cb.setVisibility(View.VISIBLE);
            cb.setChecked(true);
            mPos.add(cb);

            final QuantityView countView = holder.getView(R.id.goods_count_view);
            countView.setCount(goods.getGoods_num());
            countView.setOnCountChangedListener(new QuantityView.OnCountChangedListener() {
                @Override
                public void countChanged(int count) {
                    int goodsCount = Integer.parseInt(goods.getGoods_num());
                    int delta = 0;
                    if(count - goodsCount > 0) {
                        delta = 1;
                    } else {
                        delta = -1;
                    }


//                    if (count < goodsCount) {
//                        mTotal -= Double.parseDouble(goods.getGoods_price());
//                    } else {
//                        mTotal += Double.parseDouble(goods.getGoods_price());
//                    }

//                    mTvTotal.setText(mTotal + "");
//                    L.d("goodsCount: " + goodsCount + ", count:" + count + ", price: " + goods.getGoods_price());

                    checkedChanged(mPos, delta, goods.getGoods_price());
                }
            });

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    if (allState(true)) {
//                        mCheckAll.setChecked(true);
//                    } else {
//                        mCheckAll.setChecked(false);
//                    }
//
//                    if (!b) {
//                        mTotal -= goods.getGoods_sum();
//                        mTvTotal.setText(mTotal + "");
//                    } else {
//                        mTotal += goods.getGoods_sum();
//                        mTvTotal.setText(mTotal + "");
//                    }

                    int count = 0;
                    if(b) count = Integer.parseInt(countView.getCount());
                    else count = -Integer.parseInt(countView.getCount());

                    checkedChanged(mPos, count, goods.getGoods_price());
                }
            });

        }
    }

}
