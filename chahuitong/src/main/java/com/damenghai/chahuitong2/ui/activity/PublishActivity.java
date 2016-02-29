package com.damenghai.chahuitong2.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.damenghai.chahuitong2.api.ErrorEvent;
import com.damenghai.chahuitong2.api.TeaMarketApi;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.bean.Product;
import com.damenghai.chahuitong2.response.ResponseCallBackListener;
import com.damenghai.chahuitong2.utils.ImageUtils;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.view.CustomSpinner;
import com.damenghai.chahuitong2.view.TopBar;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.Calendar;

import de.greenrobot.event.EventBus;

public class PublishActivity extends BaseActivity {

	private String mSaleway = "1";

    private LinearLayout mInfo;
	private TopBar mTopBar;
	private RadioGroup mTabButtonGroup;
	private EditText mBrand, mName, mPrice, mQuantity, mAddress, mPhone, mDescBuy, mContact;
	private CustomSpinner mYearSpinner;

    private SparseArray<String> mUris;

	private View mClickView;

	private Product mProduct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);

		mProduct = (Product) getIntent().getSerializableExtra("product");

		bindView();

		initView();
	}

	@Override
    protected void bindView() {
        mInfo = (LinearLayout) findViewById(R.id.publish_info);
        mTopBar = (TopBar) findViewById(R.id.publish_topbar);
		mTabButtonGroup = (RadioGroup) findViewById(R.id.tab_group);

		//文本数据
		mBrand = (EditText) findViewById(R.id.id_input_brand); //品牌名编辑框
		mName = (EditText) findViewById(R.id.id_input_name); //品名编辑框
		mPrice = (EditText) findViewById(R.id.id_input_price); //价格
		mQuantity = (EditText) findViewById(R.id.id_input_quantity); //数量
		mAddress = (EditText) findViewById(R.id.id_input_address); //货源所在地
		mPhone = (EditText) findViewById(R.id.id_input_phone); //手机号码
		mYearSpinner = (CustomSpinner) findViewById(R.id.id_spinner_years); //年份下拉框
		mContact = (EditText) findViewById(R.id.publish_input_contact);
		mDescBuy = (EditText) findViewById(R.id.id_product_desc);
	}

	@Override
	protected void initView() {
		mInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
		});
        mTopBar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
		});

		if(mProduct != null) {
			mBrand.setText(mProduct.getBrand() != null ? mProduct.getBrand() : "");
			mName.setText(mProduct.getName() != null ? mProduct.getName() : "");
			mPrice.setText(mProduct.getPrice() != null ? mProduct.getPrice() : "");
			mQuantity.setText(mProduct.getQuantity() != null ? mProduct.getQuantity() : "");
			mAddress.setText(mProduct.getAddress() != null ? mProduct.getAddress() : "");
			mDescBuy.setText(mProduct.getDesc() != null ? mProduct.getDesc() : "");
			mPhone.setText(mProduct.getPhone());
            mContact.setText(mProduct.getContact() != null ? mProduct.getContact() : "");
		}

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

		mTabButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.tab_sale :
						mDescBuy.setHint(getResources().getString(R.string.product_sale_desction));
						mSaleway = "1";
						break;
					case R.id.tab_buy :
						mDescBuy.setHint(getResources().getString(R.string.product_buy_desction));
						mSaleway = "2";
						break;
				}
			}
		});

		//设置年份下拉框为今年前20年
		ArrayList<CharSequence> years = new ArrayList<CharSequence>();
		Calendar cal = Calendar.getInstance();
		for(int i=0; i<20; i++) {
			years.add("" + (cal.get(Calendar.YEAR) - i));
		}
		mYearSpinner.setAdapter(years);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == ImageUtils.GALLERY_REQUEST_CODE) {
                if (data == null) return;
                try {
                    Uri uri = data.getData();
                    if (uri != null) {
                        BitmapUtils bu = new BitmapUtils(PublishActivity.this);
                        String path = ImageUtils.getImageAbsolutePath19(PublishActivity.this, uri);
                        bu.display(mClickView, path);

                        mUris.put(mClickView.getId(), path);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == ImageUtils.CAMERA_REQUEST_CODE) {
                BitmapUtils bu = new BitmapUtils(PublishActivity.this);
                String path = ImageUtils.getCameraImagePath(PublishActivity.this);
                bu.display(mClickView, path);

                mUris.put(mClickView.getId(), path);
            }
        } else if (resultCode == Activity.RESULT_CANCELED && requestCode == ImageUtils.CAMERA_REQUEST_CODE){
            ImageUtils.deleteImageUri(PublishActivity.this);
        }
	}

    public void toAddPicture(final View view) {
        mClickView = view;

        if (mUris == null) {
            mUris = new SparseArray<>();
        }

        if (!TextUtils.isEmpty(mUris.get(view.getId(), ""))) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(PublishActivity.this);
            dialog.setMessage("是否要删除这张图片?").setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mUris.delete(view.getId());
                    ((ImageView) mClickView).setImageResource(R.drawable.btn_publish_add_picture_selector);
                }
            }).setNegativeButton("取消", null).show();
        } else {
            ImageUtils.showImagePickDialog(this);
        }
    }

    public void toPublish(View view) {
        final ProgressDialog dialog = new ProgressDialog(PublishActivity.this);
        dialog.setMessage("正在上传...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        TeaMarketApi api = new TeaMarketApi(PublishActivity.this);
        api.publishProduct(mProduct != null ? mProduct.getId() : "", mSaleway, mBrand.getText().toString(), mName.getText().toString(),
                mYearSpinner.getSelectedItem(), mPrice.getText().toString(), mQuantity.getText().toString(),
                mAddress.getText().toString(), mPhone.getText().toString(), mContact.getText().toString(),
                mDescBuy.getText().toString(), mUris, new ResponseCallBackListener<String>() {
                    @Override
                    public void onSuccess(String data) {
                        dialog.dismiss();
                        EventBus.getDefault().post(new Product());
                        setResult(Activity.RESULT_OK);
                        finishActivity();
                    }

                    @Override
                    public void onError(int errorEvent, String message) {
                        if (errorEvent == ErrorEvent.SHOULD_LOGIN) {
                            openActivity(LoginActivity.class);
                        }
                        T.showShort(PublishActivity.this, message);
                        dialog.dismiss();
                    }
                });
    }

}
