package com.damenghai.chahuitong.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.utils.DensityUtils;

public class TabHodor extends RelativeLayout implements OnClickListener {
	private Button mBtnAll, mBtnSale, mBtnBuy;
	private Button mCurrentTab;
	private ImageView mSortBtn;

	private OnItemChangeListener mChangeListener;

	public TabHodor(Context context) {
		super(context, null);
	}

	public TabHodor(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater.from(context).inflate(R.layout.custom_web_tab, this);
		mBtnAll = (Button) findViewById(R.id.id_btn_all);
		mBtnSale = (Button) findViewById(R.id.id_btn_sale);
		mBtnBuy = (Button) findViewById(R.id.id_btn_buy);
		mSortBtn = (ImageView) findViewById(R.id.id_sort);
		
		mBtnAll.setSelected(true);
		mCurrentTab = mBtnAll;

		mBtnAll.setOnClickListener(this);
		mBtnSale.setOnClickListener(this);
		mBtnBuy.setOnClickListener(this);
		mSortBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_btn_all:
			setTabSelected(mBtnAll);
			break;

		case R.id.id_btn_sale:
			setTabSelected(mBtnSale);
			break;

		case R.id.id_btn_buy:
			setTabSelected(mBtnBuy);
			break;

		default:
			break;
		}
	}

	public void setCurrentTab(int index) {
		switch (index) {
		case 0:
			setTabSelected(mBtnAll);
			break;
		case 1:
			setTabSelected(mBtnSale);
			break;
		case 2:
			setTabSelected(mBtnBuy);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 设置被选中控件的样式
	 * @param view 被选中的控件
	 */
	@SuppressLint("NewApi")
	private void setTabSelected(View view) {
		// 设置当前点击的按钮样式
		Button btn = (Button) view;
		if (btn.isSelected())
			return;
		btn.setSelected(true);
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) btn
				.getLayoutParams();
		lp.setMargins(lp.leftMargin, 0, lp.rightMargin, 0);
		btn.setLayoutParams(lp);

		// 设置上一个按钮的样式
		LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams) mCurrentTab
				.getLayoutParams();
		mParams.setMargins(DensityUtils.dp2px(getContext(), 3), 0, 0,
				DensityUtils.dp2px(getContext(), 3));
		mCurrentTab.setSelected(false);
		mCurrentTab.setLayoutParams(mParams);
		// 当前按钮更新为当前点击按钮
		mCurrentTab = btn;
		if(mChangeListener != null) {
			mChangeListener.OnItemChange(getCurrentItem());
		}
	}

	private int getCurrentItem() {
		switch (mCurrentTab.getId()) {
		case R.id.id_btn_all:
			return 0;
		case R.id.id_btn_sale:
			return 1;
		case R.id.id_btn_buy:
			return 2;
		}
		return 0;
	}
	
	/**
	 * 设置标签切换监听器
	 * @param l
	 */
	public void setOnItemChangeListener(OnItemChangeListener l) {
		this.mChangeListener = l;
	}
	
	/**
	 * 标签切换监听器
	 * @author LiaoPeiKun
	 *
	 */
	public interface OnItemChangeListener {
		void OnItemChange(int currentTab);
	}

}
