package com.damenghai.chahuitong2.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.BaseListAdapter;
import com.damenghai.chahuitong2.api.TeaMarketApi;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.bean.Brand;
import com.damenghai.chahuitong2.bean.Quotation;
import com.damenghai.chahuitong2.response.ResponseCallBackListener;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.utils.ViewHolder;
import com.damenghai.chahuitong2.view.TopBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class QuotationActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener
        , PullToRefreshBase.OnLastItemVisibleListener {
    private TopBar mTopBar;

    private View mHeader;

    private TextView mTvTitle;

    private Brand mBrand;

    private TeaMarketApi mApi;

    private PullToRefreshListView mPlv;

    private ArrayList<Quotation> mQuotations;

    private QuotationListAdapter mAdapter;

    private int mCurrentPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        mApi = new TeaMarketApi(this);

        mBrand = (Brand) getIntent().getSerializableExtra("brand");

        bindView();

        initView();

        loadData(1);
    }

    @Override
    protected void bindView() {
        mTopBar = (TopBar) findViewById(R.id.quotation_bar);
        mPlv = (PullToRefreshListView) findViewById(R.id.quotation_list);

        mHeader = View.inflate(this, R.layout.header_quotation_item, null);
        mTvTitle = (TextView) mHeader.findViewById(R.id.quotation_tv_title);
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

        mQuotations = new ArrayList<>();
        mAdapter = new QuotationListAdapter(this, mQuotations, R.layout.item_list_quotation);
        mPlv.setAdapter(mAdapter);
        mTvTitle.setText(mBrand.getName());
        mPlv.getRefreshableView().addHeaderView(mHeader);
        mPlv.setOnRefreshListener(this);
        mPlv.setOnLastItemVisibleListener(this);
    }

    private void loadData(final int page) {
        mApi.quotationList(page, mBrand.getId(), new ResponseCallBackListener<List<Quotation>>() {
            @Override
            public void onSuccess(List<Quotation> data) {
                mCurrentPage = page;
                if (page == 1) {
                    mQuotations.clear();
                }

                mAdapter.addList(data);
            }

            @Override
            public void onError(int errorEvent, String message) {
                T.showShort(QuotationActivity.this, message);
            }
        });
    }

    @Override
    public void onLastItemVisible() {
        loadData(mCurrentPage + 1);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        loadData(1);
    }

    private class QuotationListAdapter extends BaseListAdapter<Quotation> {

        public QuotationListAdapter(Context context, List<Quotation> data, int resId) {
            super(context, data, resId);
        }

        @Override
        public void convert(ViewHolder holder, Quotation quotation) {
            holder.setText(R.id.quotation_tv_name, quotation.getBrand_name())
                    .setText(R.id.quotation_tv_year, quotation.getYear())
                    .setText(R.id.quotation_tv_unit, quotation.getUnit())
                    .setText(R.id.quotation_tv_price, quotation.getPrice());
        }
    }

}
