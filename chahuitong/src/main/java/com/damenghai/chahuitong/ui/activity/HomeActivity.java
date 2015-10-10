package com.damenghai.chahuitong.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.api.HodorRequest;
import com.damenghai.chahuitong.bean.Banner;
import com.damenghai.chahuitong.bean.Goods;
import com.damenghai.chahuitong.bean.response.RecommendResponse;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.response.JsonArrayListener;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.view.BannerViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.viewpagerindicator.LinePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 图片路径
     */
    private final String IMAGE_URL = "http://www.chahuitong.com/data/upload/shop/store/goods/2/";

    /**
     * 产品页根路径
     */
    private final String GOODS_DETAIL = "http://www.chahuitong.com/wap/index.php/Home/Index/goods?goods_id=";

    private long mBackTime = 0L;

    private BannerViewPager mBanner;
    private LinePageIndicator mIndicator;
    private Button mBtnMarket;
    private Button mBtnForum;
    private Button mBtnNews;
    private Button mBtnShop;
    private Button mBtnPersonal;
    private ImageView mIvProduct;
    private TextView mTitle, mDesc, mPrice, mFavoritesOne;
    private LinearLayout mLayout;

    private ArrayList<Banner> mBanners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById();

        initView();

        loadData();
    }

    public void findViewById() {
        mBanner = (BannerViewPager) findViewById(R.id.home_banner);
        mIndicator = (LinePageIndicator) findViewById(R.id.home_indicator);

        mBtnMarket = (Button) findViewById(R.id.home_market);
        mBtnForum = (Button) findViewById(R.id.home_Forum);
        mBtnNews = (Button) findViewById(R.id.home_News);
        mBtnShop= (Button) findViewById(R.id.home_shop);
        mBtnPersonal = (Button) findViewById(R.id.home_personal);

        mIvProduct = (ImageView) findViewById(R.id.home_iv_product);

        mLayout = (LinearLayout) findViewById(R.id.home_recommend_layout);
        View include_recommend = findViewById(R.id.goods_one);
        mTitle = (TextView) include_recommend.findViewById(R.id.home_tv_title);
        mDesc = (TextView) include_recommend.findViewById(R.id.home_tv_desc);
        mPrice = (TextView) include_recommend.findViewById(R.id.home_tv_price);
        mFavoritesOne = (TextView) include_recommend.findViewById(R.id.home_tv_favorites);
    }

    public void initView() {
        mBanners = new ArrayList<Banner>();

        mBtnMarket.setOnClickListener(this);
        mBtnForum.setOnClickListener(this);
        mBtnNews.setOnClickListener(this);
        mBtnShop.setOnClickListener(this);
        mBtnPersonal.setOnClickListener(this);
    }

    private void loadData() {
        HodorRequest.getRequest("http://www.chahuitong.com/wap/index.php/Home/Index/homepic_api", new JsonArrayListener(HomeActivity.this) {
            @Override
            public void onSuccess(JSONArray array) {
                for(int i=0; i<array.length(); i++) {
                    try {
                        Banner banner = new Gson().fromJson(array.getString(i), Banner.class);
                        mBanners.add(banner);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mBanner.setImageUrl("http://www.chahuitong.com/wap/Public/upload/", mBanners);
                mBanner.setIndicator(mIndicator);
            }
        });

        HodorRequest.getRequest("http://www.chahuitong.com/wap/index.php/Home/Index/homePromotionGoods", new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                RecommendResponse recommend = new Gson().fromJson(response, RecommendResponse.class);
                final Goods goods = recommend.getContent().get(0);
                BitmapUtils utils = new BitmapUtils(HomeActivity.this);
                utils.display(mIvProduct, IMAGE_URL + goods.getImageUrl());
                mTitle.setText(goods.getName());
                mDesc.setText(goods.getDescription());
                mPrice.setText("￥" + goods.getPrice());
                mLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundleGoods1 = new Bundle();
                        bundleGoods1.putString("url", GOODS_DETAIL + goods.getGoods_id());
                        openActivity(WebViewActivity.class, bundleGoods1);
                    }
                });
                HodorRequest.favorites(goods.getGoods_id(), new VolleyRequest() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            String favorites = new JSONObject(response).getString("content");
                            mFavoritesOne.setText(favorites);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_market :
                openActivity(MarketActivity.class);
                break;
            case R.id.home_Forum :
                openActivity(ForumActivity.class);
                break;
            case R.id.home_News :
                openActivity(NewsActivity.class);
                break;
            case R.id.home_shop :
                openActivity(ShopActivity.class);
                break;
            case R.id.home_personal :
                openActivity(PersonalActivity.class);
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ViewGroup.LayoutParams params = mIvProduct.getLayoutParams();
        params.width = mIvProduct.getHeight();
        mIvProduct.setLayoutParams(params);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(System.currentTimeMillis() - mBackTime < 2000) {
                finish();
            } else {
                T.showShort(HomeActivity.this, "再按一次退出茶汇通");
                mBackTime = System.currentTimeMillis();
            }
        }
        return true;
    }
}
