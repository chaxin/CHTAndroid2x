package com.damenghai.chahuitong.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.bean.Product;
import com.damenghai.chahuitong.config.Constants;
import com.damenghai.chahuitong.view.BannerViewPager;
import com.damenghai.chahuitong.view.TopBar;
import com.damenghai.chahuitong.view.TopBar.OnLeftClickListener;
import com.viewpagerindicator.CirclePageIndicator;

public class ProductActivity extends BaseActivity implements OnLeftClickListener,
		OnClickListener {
	private Product mProduct;
	private TopBar mTopBar;
	private TextView mBrand, mPrice, mName, mYear, mQuantity, mAddress,
			mPhone, mDesc, mDate;
    private BannerViewPager mBanner;
    private Button mContact;
    private CirclePageIndicator mIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);

		mProduct = (Product) getIntent().getSerializableExtra("product");

		findViewById();

		initView();

		initData();
	}

	@Override
	protected void findViewById() {
		mTopBar = (TopBar) findViewById(R.id.id_product_topBar);
		mBanner = (BannerViewPager) findViewById(R.id.id_product_img);
		mBrand = (TextView) findViewById(R.id.id_product_brand);
		mPrice = (TextView) findViewById(R.id.id_product_price);
		mName = (TextView) findViewById(R.id.id_product_name);
		mYear = (TextView) findViewById(R.id.id_product_year);
		mQuantity = (TextView) findViewById(R.id.id_product_quantity);
		mAddress = (TextView) findViewById(R.id.id_product_address);
		mPhone = (TextView) findViewById(R.id.id_product_phone);
		mDesc = (TextView) findViewById(R.id.id_product_desc);
		mDate = (TextView) findViewById(R.id.id_product_date);
		mContact = (Button) findViewById(R.id.id_contact);
        mIndicator = (CirclePageIndicator) findViewById(R.id.product_indicator);
    }

	@Override
	protected void initView() {
		mTopBar.setOnLeftClickListener(this);
		mContact.setOnClickListener(this);
	}

	private void initData() {
        if (mProduct == null) return;

		if(mProduct.getImgUrls() != null) {
			mBanner.setImageUrl(Constants.IMAGE_URL, mProduct.getImgUrls(), false);
			mBanner.stopScroll();
			mBanner.setIndicator(mIndicator);
		}
		mBrand.setText(mProduct.getBrand());
		mName.setText(mProduct.getName());
		mPrice.setText(mProduct.getPrice());
		mYear.setText(mProduct.getYear());
		mQuantity.setText(mProduct.getQuantity() + "");
		mAddress.setText(mProduct.getAddress());
		mPhone.setText(mProduct.getPhone());
		mDesc.setText(mProduct.getDesc());
		mDate.setText("发布时间 " + mProduct.getDate());
	}

	@Override
	public void onLeftClick() {
		finish();
	}

	@Override
	public void onClick(View v) {
        Bundle extras = new Bundle();
        extras.putSerializable("product", mProduct);
		openActivity(ContactActivity.class, extras);
	}
}
