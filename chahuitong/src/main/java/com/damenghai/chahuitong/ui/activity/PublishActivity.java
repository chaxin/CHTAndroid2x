package com.damenghai.chahuitong.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.bean.Product;
import com.damenghai.chahuitong.config.Constants;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.utils.ImageManager;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.view.CustomSpinner;
import com.damenghai.chahuitong.view.TopBar;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.Calendar;

public class PublishActivity extends BaseActivity implements OnClickListener {
	private final int CAMERA_REQUEST_CODE = 1;
	private final int CALLERY_REQUEST_CODE = 2;

	private final String[] CHOOSE_ITEM = new String[] {"拍照", "图库"};

	private String mSaleway = "1";

    private LinearLayout mInfo;
	private TopBar mTopBar;
	private RadioGroup mTabButtonGroup;
	private EditText mBrand, mName, mPrice, mQuantity, mAddress, mPhone, mDescBuy;
	private CustomSpinner mYearSpinner, mDetailSpinner;

	private ImageView mIvFrist, mIvSecond, mIvThird;
	private Button mBtnSubmit;
	private SparseArray<ImageManager> mProductImage = new SparseArray<ImageManager>();

	View mClickView;

	private Product mProduct;
	private boolean mIsEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);

		mProduct = (Product) getIntent().getSerializableExtra("product");
		mIsEdit = getIntent().getBooleanExtra("isEdit", false);

		findViewById();

		initView();
	}

	@Override
    protected void findViewById() {
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
		mDetailSpinner = (CustomSpinner) findViewById(R.id.id_spinner_detail); //是否详谈下拉框
		mDescBuy = (EditText) findViewById(R.id.id_product_desc);
		//添加图片
		mIvFrist = (ImageView) findViewById(R.id.id_upload_img1);
		mIvSecond = (ImageView) findViewById(R.id.id_upload_img2);
		mIvThird = (ImageView) findViewById(R.id.id_upload_img3);
		mBtnSubmit = (Button) findViewById(R.id.id_btn_submit); //提交按钮

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
			mQuantity.setText(mProduct.getQuantity() + "");
			mAddress.setText(mProduct.getAddress() != null ? mProduct.getAddress() : "");
			mDescBuy.setText(mProduct.getPrice() != null ? mProduct.getPrice() : "");
			mPhone.setText(mProduct.getPhone());
		}

		mTopBar.setOnLeftClickListener(new TopBar.OnLeftClickListener() {
			@Override
			public void onLeftClick() {
				finishActivity();
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

		//设置是否详谈
		ArrayList<CharSequence> details = new ArrayList<CharSequence>();
		details.add("是");
		details.add("否");
		mDetailSpinner.setAdapter(details);

		mIvFrist.setOnClickListener(this);
		mIvSecond.setOnClickListener(this);
		mIvThird.setOnClickListener(this);
		mBtnSubmit.setOnClickListener(new submitOnClickListener());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == CALLERY_REQUEST_CODE) {
			if(resultCode == Activity.RESULT_OK) {
				try {
					if(data == null) return;

					Uri uri = data.getData();
					ImageManager pi = mProductImage.get(mClickView.getId());
					if(pi == null) {
						pi = new ImageManager(this);
						mProductImage.put(mClickView.getId(), pi);
					}

					if(pi.checkUri(uri)) {
						pi.display(mClickView);
					} else {
						T.showLong(this, "资源不可用");
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		} else if(requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			if(data == null) return;
			Bundle extra = data.getExtras();
			if(extra != null) {
				Bitmap bm = extra.getParcelable("data");
				if(bm == null && mClickView == null) return;
				((ImageView) mClickView).setImageBitmap(bm);
				ImageManager pi = mProductImage.get(mClickView.getId());
				if(pi == null) {
					pi = new ImageManager(this);
					mProductImage.put(mClickView.getId(), pi);
				}
				pi.setBitmap(bm);
				pi.setView(mClickView);
			}
		}
		
	}

    @Override
	public void onClick(final View v) {
		mClickView = v;
		new AlertDialog.Builder(this).setItems(CHOOSE_ITEM, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case 0:
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(intent, CAMERA_REQUEST_CODE);
						break;
					case 1:
						Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(i, CALLERY_REQUEST_CODE);
						break;
				}
			}

		}).show();
	}

    private class submitOnClickListener implements OnClickListener {
		private ProgressDialog pd;
		
		@Override
		public void onClick(View v) {
			RequestParams params = new RequestParams();
			final String brand = mBrand.getText().toString();
			final String name = mName.getText().toString();
			final String year = mYearSpinner.getSelectedItem();
			final String price = mPrice.getText().toString();
			final String detail = mDetailSpinner.getSelectedItem();
			final String quantity = mQuantity.getText().toString();
			String address = mAddress.getText().toString();
			String phone = mPhone.getText().toString();

			if(mProduct != null && mProduct.getId() > 0 && mIsEdit) {
				params.addBodyParameter("id", mProduct.getId() + "");
			}
			params.addBodyParameter("key", SessionKeeper.readSession(PublishActivity.this));
			params.addBodyParameter("username", SessionKeeper.readUsername(PublishActivity.this));
			params.addBodyParameter("saleway", mSaleway);
			params.addBodyParameter("brand", brand);
			params.addBodyParameter("name", name);
			params.addBodyParameter("year", year);
			params.addBodyParameter("price", price);
			params.addBodyParameter("arrow_order", detail.equals("是") ? "1" : "0");
			params.addBodyParameter("weight", quantity);
			params.addBodyParameter("address", address);
			params.addBodyParameter("phone", phone);

			if(mProductImage != null) {
				for(int i=0; i<mProductImage.size(); i++) {
					ImageManager pi = mProductImage.valueAt(i);
					String imgKey = "img" + (i+1);
					params.addBodyParameter(imgKey, pi.getCompressFile());
				}
			}

			HttpUtils http = new HttpUtils();
			http.send(HttpRequest.HttpMethod.POST,
			    Constants.URL_POST_SAVE,
			    params,
			    new RequestCallBack<String>() {

			        @Override
			        public void onStart() {
			        	pd = new ProgressDialog(PublishActivity.this);
			            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			            pd.setMessage("上传中....");
			            pd.setCancelable(false);
			            pd.show();
			        }

			        @Override
			        public void onLoading(long total, long current, boolean isUploading) {
			            if (isUploading) {
			            	pd.setProgress((int) ((current * 1.0/total)*100));
			            } else {
			            	pd.dismiss();
			            }
			        }

			        @Override
			        public void onSuccess(ResponseInfo<String> responseInfo) {
			        	new AlertDialog.Builder(PublishActivity.this).setMessage("上传成功").setPositiveButton("确定", null).show();
						finish();
			        	pd.dismiss();

			        	//把图片选择图片的控件恢复成默认图片
			        	if(mProductImage != null) {
			        		for(int i=0; i<mProductImage.size(); i++) {
			        			int key = mProductImage.keyAt(i);
			        			ImageManager pi = mProductImage.get(key);
			        			pi.displayDefault();
			        			pi.deleteTempFile();
			        		}
			        		mProductImage.clear();
			        	}

			        }

			        @Override
			        public void onFailure(HttpException error, String msg) {
			        	pd.dismiss();
			        	if(msg.contains("FileNotFoundException")) {
			        		new AlertDialog.Builder(PublishActivity.this).setTitle("上传失败").setMessage("文件不存在请重新选择").setPositiveButton("确定", null).show();
			        		return;
			        	}
			        	new AlertDialog.Builder(PublishActivity.this).setTitle(error.getExceptionCode() + ":" + msg).setMessage("请检查网络").setPositiveButton("确定", null).show();
			        }
			});

		}
		
	}
}
