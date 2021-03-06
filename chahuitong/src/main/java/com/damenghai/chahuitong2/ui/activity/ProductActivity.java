package com.damenghai.chahuitong2.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.damenghai.chahuitong2.api.TeaMarketApi;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.bean.ImageUrls;
import com.damenghai.chahuitong2.bean.Product;
import com.damenghai.chahuitong2.config.Constants;
import com.damenghai.chahuitong2.config.SessionKeeper;
import com.damenghai.chahuitong2.response.JsonArrayListener;
import com.damenghai.chahuitong2.utils.DensityUtils;
import com.damenghai.chahuitong2.utils.ImageConfigHelper;
import com.damenghai.chahuitong2.utils.L;
import com.damenghai.chahuitong2.view.TopBar;
import com.lidroid.xutils.BitmapUtils;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.FindListener;

public class ProductActivity extends BaseActivity implements OnClickListener {
	private Product mProduct;
	private TopBar mTopBar;
	private TextView mTvBrand, mTvPrice, mTvName, mTvYear, mTvQuantity, mTvAddress, mTvDesc, mTvDate;
	private TextView mTvMobile, mTvFrom;
	private Button mBtnMobile, mBtnSms, mBtnChat;

    private ViewPager mBanner;
    private List<ImageView> mImages;
    private List<ImageUrls> mImageUrls;
    private ProductViewPagerAdapter mAdapter;
    private CirclePageIndicator mIndicator;

    private BmobUserManager mManager;
    private BmobChatUser mUser;
    private String mUsername;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);

		mProduct = (Product) getIntent().getSerializableExtra("product");

		bindView();

		initView();

		initData();
	}

	@Override
	protected void bindView() {
		mTopBar = (TopBar) findViewById(R.id.id_product_topBar);
		mBanner = (ViewPager) findViewById(R.id.id_product_img);
		mTvBrand = (TextView) findViewById(R.id.id_product_brand);
		mTvPrice = (TextView) findViewById(R.id.id_product_price);
		mTvName = (TextView) findViewById(R.id.id_product_name);
		mTvYear = (TextView) findViewById(R.id.id_product_year);
		mTvQuantity = (TextView) findViewById(R.id.id_product_quantity);
		mTvAddress = (TextView) findViewById(R.id.id_product_address);
		mTvDesc = (TextView) findViewById(R.id.id_product_desc);
		mTvDate = (TextView) findViewById(R.id.id_product_date);

		mTvMobile = (TextView) findViewById(R.id.product_mobile_number);
		mTvFrom = (TextView) findViewById(R.id.product_from);
		mBtnMobile  = (Button) findViewById(R.id.product_btn_mobile);
		mBtnSms = (Button) findViewById(R.id.product_btn_sms);
		mBtnChat = (Button) findViewById(R.id.product_btn_chat);

		mIndicator = (CirclePageIndicator) findViewById(R.id.product_indicator);
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

		mBtnMobile.setOnClickListener(this);
		mBtnSms.setOnClickListener(this);
		mBtnChat.setOnClickListener(this);
        mBtnChat.setEnabled(false);

        mManager = BmobUserManager.getInstance(this);

        mImageUrls = new ArrayList<>();
	}

	private void initData() {
        if (mProduct == null) return;

		if(!TextUtils.isEmpty(mProduct.getImgUrls())) {
            mImages = new ArrayList<>();
            mAdapter = new ProductViewPagerAdapter(mImages);

            String urls = mProduct.getImgUrls();
            if(urls.contains(",")) {
                String[] images = urls.split(",");
                for(String image : images) {
                    addImageView(image);
                }
            } else {
                addImageView(urls);
            }

            mBanner.setAdapter(mAdapter);
			mIndicator.setViewPager(mBanner);
		} else {
            mBanner.setVisibility(View.GONE);
        }
		mTvBrand.setText(mProduct.getBrand());
		mTvName.setText(mProduct.getName());
		mTvPrice.setText(TextUtils.isEmpty(mProduct.getPrice()) ? "" : mProduct.getPrice() + "元");
		mTvYear.setText(mProduct.getYear());
		mTvQuantity.setText(mProduct.getQuantity() + "");
		mTvAddress.setText(mProduct.getAddress());
		mTvDesc.setText(mProduct.getDesc());
		mTvDate.setText("发布时间 " + mProduct.getDate());

		mTvMobile.setText(mProduct.getPhone());
		mTvFrom.setText(TextUtils.isEmpty(mProduct.getContact()) ? "匿名" : mProduct.getContact());

        loadUser();
	}

    private void addImageView(String imageUrl) {
        ImageView imageView = new ImageView(this);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        int size = DensityUtils.dp2px(ProductActivity.this, 256);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(size, size);
        imageView.setLayoutParams(params);

        BitmapUtils bitmapUtils = new BitmapUtils(this);
        bitmapUtils.display(imageView, Constants.IMAGE_URL + imageUrl, ImageConfigHelper.getGrayConfig(this));
        mImages.add(imageView);

        ImageUrls urls = new ImageUrls();
        urls.setBmiddle_pic(Constants.IMAGE_URL + imageUrl);
        mImageUrls.add(urls);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.product_btn_mobile :
				if(!TextUtils.isEmpty(mProduct.getPhone())) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mProduct.getPhone()));
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                }
				break;
			case R.id.product_btn_sms :
                if(!TextUtils.isEmpty(mProduct.getPhone())) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + mProduct.getPhone()));
                    intent.putExtra("sms_body", "您好，我对您在茶汇通发布的『" + mProduct.getName() + "』很感兴趣，希望和您详细了解一下。");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                }
				break;
			case R.id.product_btn_chat :
                if(TextUtils.isEmpty(SessionKeeper.readSession(this))) {
                    openActivity(LoginActivity.class);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", mUser);
                    openActivity(ChatActivity.class, bundle);
                }
				break;
		}
	}

	private void loadUser() {
//        mApi.getUserInfo(mProduct.getId(), new ResponseCallBackListener() {
//            @Override
//            public void onSuccess(Object data) {
//
//            }
//
//            @Override
//            public void onError(int errorEvent, String message) {
//
//            }
//        });
        TeaMarketApi.getUserInfo(mProduct.getId(), new JsonArrayListener(this) {
            @Override
            public void onSuccess(JSONArray array) {
                try {
                    JSONObject object = array.getJSONObject(0);
                    mUsername = object.getString("member_mobile");

                    initBmobUser();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
	}

	private void initBmobUser() {
        if(!TextUtils.isEmpty(mUsername) && !mUsername.equals(SessionKeeper.readUsername(this))) {
            mManager.queryUser(mUsername, new FindListener<BmobChatUser>() {
                @Override
                public void onSuccess(List<BmobChatUser> list) {
                    if (list != null && list.size() > 0) {
                        mUser = list.get(0);
                        mBtnChat.setEnabled(true);
                    }
                }

                @Override
                public void onError(int i, String s) {
                    L.d("获取用户失败" + s);
                }
            });
        }
	}

	private class ProductViewPagerAdapter extends PagerAdapter {
        private List<ImageView> mViews;

        public ProductViewPagerAdapter(List<ImageView> views) {
            mViews = views;
        }

        @Override
		public int getCount() {
			return mViews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
            ImageView iv = mViews.get(position);
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    bundle.putSerializable("pic", mProduct.getImgUrls());
                    openActivity(ImageBrowserActivity.class);
                }
            });
            container.addView(iv);
			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImages.get(position));
		}
	}
}
