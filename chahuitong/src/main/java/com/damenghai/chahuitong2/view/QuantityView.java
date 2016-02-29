package com.damenghai.chahuitong2.view;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.damenghai.chahuitong2.R;

/**
 * Copyright (c) 2015. LiaoPeiKun Inc. All rights reserved.
 */
public class QuantityView extends LinearLayout implements View.OnClickListener {

    private Button mBtnLeft;

    private TextView mTvCount;

    private Button mBtnRight;

    public interface OnCountChangedListener {
        void countChanged(int count);
    }

    private OnCountChangedListener mListener;

    public QuantityView(Context context) {
        this(context, null);
    }

    public QuantityView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.view_quantity_view, this);

        mTvCount = (TextView) findViewById(R.id.count_et_quantity);
        mBtnLeft = (Button) findViewById(R.id.count_btn_left);
        mBtnRight = (Button) findViewById(R.id.count_btn_right);

        initView();
    }

    private void initView() {
        mTvCount.addTextChangedListener(new CountTextWatcher(mBtnLeft, mTvCount));

        mTvCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext(), R.style.Theme_AudioDialog);
                dialog.setContentView(R.layout.dialog_edit_count);
                final EditText etCount = (EditText) dialog.findViewById(R.id.dialog_et_count);
                final Button btnLeft = (Button) dialog.findViewById(R.id.dialog_btn_sub);
                final Button btnRight = (Button) dialog.findViewById(R.id.dialog_btn_add);

                etCount.addTextChangedListener(new CountTextWatcher(btnLeft, etCount));
                etCount.setText(mTvCount.getText());
                btnLeft.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                changeCount(etCount, -1);
                            }
                        });
                btnRight.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                changeCount(etCount, 1);
                            }
                        });
                dialog.findViewById(R.id.dialog_btn_ok)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mTvCount.setText(etCount.getText());

                                if(mListener != null) {
                                    if(etCount.length() < 1 || etCount.length() == 0) {
                                        mListener.countChanged(0);
                                    } else {
                                        int count = Integer.parseInt(etCount.getText().toString());
                                        mListener.countChanged(count);
                                    }
                                }
                                dialog.dismiss();
                            }
                        });
                dialog.findViewById(R.id.dialog_btn_cancel)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                dialog.show();
            }
        });

        mBtnLeft.setOnClickListener(this);
        mBtnRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // 数量变化的监听,数量变化价格也要跟着变化
        if(mListener != null) {
            if(mTvCount.length() < 1 || mTvCount.length() == 0) {
                mListener.countChanged(0);
            } else {
                int count = Integer.parseInt(mTvCount.getText().toString());
                mListener.countChanged(count);
            }
        }

        switch (view.getId()) {
            case R.id.count_btn_left :
                changeCount(mTvCount, -1);
                break;
            case R.id.count_btn_right :
                changeCount(mTvCount, 1);
                break;
        }
    }

    public void setOnCountChangedListener(OnCountChangedListener listener) {
        mListener = listener;
    }

    public String getCount() {
        return mTvCount.getText().toString();
    }

    private void changeCount(TextView tv, int delta) {
        if (tv.length() > 0) {
            int count = Integer.parseInt(tv.getText().toString());
            int after = count + delta;
            if (after > 0) {
                tv.setText((count + delta) + "");
            }
        }
    }

    // 获取数量
    public void setCount(CharSequence text) {
        if(!TextUtils.isEmpty(text)) {
            mTvCount.setText(text);
        }
    }

    private class CountTextWatcher implements TextWatcher {

        private TextView tvCount;

        private Button btnLeft;

        public CountTextWatcher(Button btnLeft, TextView tvCount) {
            this.btnLeft = btnLeft;
            this.tvCount = tvCount;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // 设置左右按钮状态
            if (tvCount.length() < 1 || tvCount.getText().toString().equals("1")) {
                btnLeft.setBackgroundResource(R.drawable.count_left_disable);
                btnLeft.setEnabled(false);
            } else {
                btnLeft.setBackgroundResource(R.drawable.count_left_sel);
                btnLeft.setEnabled(true);
            }

            // 用户输入数量不能大于5位数或不能等于0
            if(tvCount.length() > 5 || tvCount.getText().toString().equals("0")) {
                tvCount.setText("1");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    }
}
