package com.damenghai.chahuitong.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.damenghai.chahuitong.base.BaseFragment;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.ui.activity.PublishActivity;

/**
 * Created by Sgun on 15/8/12.
 */
public class PublishFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private LinearLayout mSaleView, mBuyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_publish, null);

        findViewById();
        initView();

        return view;
    }

    private void findViewById() {
        mSaleView = (LinearLayout) view.findViewById(R.id.publish_ll_sale);
        mBuyView = (LinearLayout) view.findViewById(R.id.publish_ll_buy);
    }

    private void initView() {
        mSaleView.setOnClickListener(this);
        mBuyView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.publish_ll_sale :
                bundle.putBoolean("tab", true);
                break;
            case R.id.publish_ll_buy :
                bundle.putBoolean("tab", false);
                break;
        }
        openActivity(PublishActivity.class, bundle);
    }
}
