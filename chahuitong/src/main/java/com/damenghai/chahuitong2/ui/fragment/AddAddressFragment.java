package com.damenghai.chahuitong2.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.api.PersonalAPI;
import com.damenghai.chahuitong2.base.BaseFragment;
import com.damenghai.chahuitong2.bean.Address;
import com.damenghai.chahuitong2.request.VolleyRequest;
import com.damenghai.chahuitong2.ui.activity.AreaActivity;
import com.damenghai.chahuitong2.utils.T;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class AddAddressFragment extends BaseFragment implements View.OnClickListener {
    private final String KEY_ADDRESS = "AddressFragment:Address";
    private final int REQUEST_CODE_CHOOSE_AREA = 0x001;

    private EditText mEtName;

    private EditText mEtMobile;

    private TextView mTvArea;

    private EditText mEtAddress;

    private Button mBtnSave;

    private String mCityId;

    private String mAreaId;

    private String mAreaInfo;

    private Address mAddress;

    public static AddAddressFragment get(Address address) {
        AddAddressFragment fragment = new AddAddressFragment();
        fragment.mAddress = address;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_ADDRESS)) {
            mAddress = (Address) savedInstanceState.get(KEY_ADDRESS);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.include_add_address, container, false);

        bindView(view);

        if(mAddress != null) {
            mEtName.setText(mAddress.getTrue_name());
            mEtMobile.setText(mAddress.getMob_phone());
            mTvArea.setText(mAddress.getArea_info());
            mEtAddress.setText(mAddress.getAddress());
            mCityId = mAddress.getCity_id();
            mAreaId = mAddress.getArea_id();
            mAreaInfo = mAddress.getArea_info();
        }

        mTvArea.setOnClickListener(this);

        mBtnSave.setOnClickListener(this);

        return view;
    }

    private void bindView(View view) {
        mEtName = (EditText) view.findViewById(R.id.add_address_et_name);
        mEtMobile = (EditText) view.findViewById(R.id.add_address_et_mobile);
        mTvArea = (TextView) view.findViewById(R.id.add_address_et_area);
        mEtAddress = (EditText) view.findViewById(R.id.add_address_et_address);
        mBtnSave = (Button) view.findViewById(R.id.add_address_btn_save);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_CHOOSE_AREA && resultCode == Activity.RESULT_OK) {
            mCityId = data.getStringExtra("cityId");
            mAreaId = data.getStringExtra("areaId");
            mAreaInfo = data.getStringExtra("area");
            mTvArea.setText(mAreaInfo);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_address_et_area :
                openActivityForResult(AreaActivity.class, REQUEST_CODE_CHOOSE_AREA);
                break;
            case R.id.add_address_btn_save :
                final String name = mEtName.getText().toString();
                final String phone = mEtMobile.getText().toString();
                final String area = mTvArea.getText().toString();
                final String addressInfo = mEtAddress.getText().toString();
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)
                        || TextUtils.isEmpty(area) || TextUtils.isEmpty(addressInfo)) {
                    T.showShort(getActivity(), R.string.toast_not_fill);
                    return;
                }

                if(name.length() < 2) {
                    T.showShort(getActivity(), R.string.toast_name_invalid);
                    return;
                }

                if(phone.length() != 11) {
                    T.showShort(getActivity(), R.string.toast_mobile_invalid);
                    return;
                }

                if(area.length() < 1) {
                    T.showShort(getActivity(), R.string.toast_area_invalid);
                    return;
                }

                if(addressInfo.length() < 1) {
                    T.showShort(getActivity(), R.string.toast_address_invalid);
                    return;
                }

                if(mAddress != null && !TextUtils.isEmpty(mAddress.getAddress_id())) {
                    PersonalAPI.addressEdit(getActivity(), mAddress.getAddress_id(), name, phone, mCityId, mAreaId, area, addressInfo, new VolleyRequest() {
                        @Override
                        public void onSuccess(String response) {
                            super.onSuccess(response);
                            try {
                                JSONObject object = new JSONObject(response);
                                if(object.getString("datas").equals("1")) {
                                    T.showShort(getActivity(), R.string.toast_edit_success);
                                    mAddress.setCity_id(mCityId);
                                    mAddress.setArea_id(mAreaId);
                                    mAddress.setTrue_name(name);
                                    mAddress.setMob_phone(phone);
                                    mAddress.setArea_info(area);
                                    mAddress.setAddress(addressInfo);
                                    EventBus.getDefault().post(mAddress);
                                } else {
                                    T.showShort(getActivity(), object.getJSONObject("datas").getString("error"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    PersonalAPI.addressAdd(getActivity(), name, phone, mCityId, mAreaId, area, addressInfo, new VolleyRequest() {
                        @Override
                        public void onSuccess(String response) {
                            super.onSuccess(response);
                            try {
                                JSONObject object = new JSONObject(response);
                                JSONObject datas = object.getJSONObject("datas");
                                if(!datas.has("error")) {
                                    T.showShort(getActivity(), R.string.toast_add_success);
                                    String address_id = datas.getString("address_id");
                                    Address address = new Address();
                                    address.setAddress_id(address_id);
                                    address.setCity_id(mCityId);
                                    address.setArea_id(mAreaId);
                                    address.setTrue_name(name);
                                    address.setMob_phone(phone);
                                    address.setArea_info(area);
                                    address.setAddress(addressInfo);
                                    EventBus.getDefault().post(address);
                                } else {
                                    T.showShort(getActivity(), datas.getString("datas"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_ADDRESS, mAddress);
    }
}
