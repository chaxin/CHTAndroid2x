package com.damenghai.chahuitong2.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.BaseListAdapter;
import com.damenghai.chahuitong2.api.PersonalAPI;
import com.damenghai.chahuitong2.base.BaseFragmentActivity;
import com.damenghai.chahuitong2.bean.Address;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.ui.fragment.AddAddressFragment;
import com.damenghai.chahuitong2.utils.L;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.utils.ViewHolder;
import com.damenghai.chahuitong2.view.TopBar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class AddressListActivity extends BaseFragmentActivity {
    private TopBar mTopBar;

    private ListView mListView;

    private List<Address> mAddresses;

    private AddressListAdapter mAdapter;

    private FrameLayout mContainer;

    private LinearLayout mListLayout;

    private AddAddressFragment mFragment;

    private String mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        EventBus.getDefault().register(this);

        mState = getIntent().getStringExtra("state");

        bindView();

        initView();

        loadData();
    }

    protected void bindView() {
        mTopBar = (TopBar) findViewById(R.id.address_list_bar);

        mListLayout = (LinearLayout) findViewById(R.id.address_list_layout);

        mListView = (ListView) findViewById(R.id.address_list_lv);

        mContainer = (FrameLayout) findViewById(R.id.address_list_container);
    }

    protected void initView() {
        mTopBar.setOnButtonClickListener(new TopBar.OnButtonClickListener() {
            @Override
            public void onLeftClick() {
                onEventMainThread(null);
            }

            @Override
            public void onRightClick() {
                goHome();
            }
        });

        mAddresses = new ArrayList<Address>();
        mAdapter = new AddressListAdapter(this, mAddresses, R.layout.item_list_address);
        mListView.setAdapter(mAdapter);
    }

    public void toAddAddress(View view) {
        mListLayout.setVisibility(View.GONE);
        mContainer.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mFragment = new AddAddressFragment();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.address_list_container, mFragment);
        ft.commit();
    }

    public void editAddress(Address address) {
        mListLayout.setVisibility(View.GONE);
        mContainer.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mFragment = AddAddressFragment.get(address);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.address_list_container, mFragment);
        ft.commit();
    }

    public void deleteAddress(final Address address) {
        PersonalAPI.addressDelete(AddressListActivity.this, address.getAddress_id(), new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                L.d(response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("datas").equals("1")) {
                        T.showShort(AddressListActivity.this, R.string.toast_delete_success);
                        if (mAddresses.contains(address)) {
                            mAddresses.remove(address);
                            mAdapter.notifyDataSetChanged();
                        }
                    } else if (object.getJSONObject("datas").has("error")) {
                        T.showShort(AddressListActivity.this, object.getJSONObject("datas").getString("error"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadData() {
        PersonalAPI.addressShow(this, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject datas = object.getJSONObject("datas");
                    JSONArray array = datas.getJSONArray("address_list");
                    for (int i = 0; i < array.length(); i++) {
                        Address address = new Gson().fromJson(array.getString(i), Address.class);
                        if (!mAddresses.contains(address)) mAddresses.add(address);
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // 新增地址或编辑地址的回调
    public void onEventMainThread(Address address) {
        if(mContainer.getVisibility() == View.VISIBLE && mFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            ft.remove(mFragment);
            ft.commit();

            mContainer.setVisibility(View.GONE);
            mListLayout.setVisibility(View.VISIBLE);
        } else {
            finishActivity();
        }

        if(address != null) {
            if(!mAddresses.contains(address)) {
                mAddresses.add(0, address);
            } else {
                mAddresses.set(mAddresses.indexOf(address), address);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            onEventMainThread(null);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class AddressListAdapter extends BaseListAdapter<Address> {

        public AddressListAdapter(Context context, List<Address> data, int resId) {
            super(context, data, resId);
        }

        @Override
        public void convert(final ViewHolder holder, final Address address) {
            holder.setText(R.id.address_tv_name, address.getTrue_name())
                    .setText(R.id.address_tv_mobile, address.getMob_phone())
                    .setText(R.id.address_tv_address, address.getArea_info() + address.getAddress())
                    .setOnLongClickListener(R.id.address_item_layout, new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            AlertDialog.Builder items = new AlertDialog.Builder(mContext);
                            items.setItems(R.array.alert_item_address, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    switch (i) {
                                        case 0:
                                            editAddress(address);
                                            break;
                                        case 1:
                                            deleteAddress(address);
                                            break;
                                    }
                                }
                            }).show();
                            return true;
                        }
                    });

            if(mState != null && mState.equals("choose")) {
                holder.setOnClickListener(R.id.address_item_layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(address);
                        finishActivity();
                    }
                });
            }

        }
    }

}
