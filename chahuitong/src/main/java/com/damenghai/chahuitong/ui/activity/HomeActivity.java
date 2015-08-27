package com.damenghai.chahuitong.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.damenghai.chahuitong.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.bean.Goods;
import com.damenghai.chahuitong.bean.response.RecommendResponse;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.view.BannerViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.viewpagerindicator.LinePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 图片路径
     */
    private final String IMAGE_URL = "http://www.chahuitong.com/data/upload/shop/store/goods/2/";

    /**
     * 产品页根路径
     */
    private final String GOODS_DETAIL = "http://www.chahuitong.com/wap/index.php/Home/Index/goods?goods_id=";

    /**
     * Banner图片根路径
     */
    private final String BANNER_URL = "http://www.chahuitong.com/data/upload/mobile/special/s0/";

    private BannerViewPager mBanner;
    private LinePageIndicator mIndicator;
    private Button mBtnMarket;
    private Button mBtnForum;
    private Button mBtnNews;
    private Button mBtnShop;
    private Button mBtnPersonal;
    private ImageView mIvProduct;
    private TextView mTitleOne, mDescOne, mPriceOne, mFavoritesOne;
    private Button mBtnOne;

    private Goods goods1, goods2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewByid();
        initView();
        loadData();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int height = mIvProduct.getHeight();
        ViewGroup.LayoutParams params = mIvProduct.getLayoutParams();
        params.width = height;
        mIvProduct.setLayoutParams(params);
    }

    private void findViewByid() {
        mBanner = (BannerViewPager) findViewById(R.id.home_banner);
        mIndicator = (LinePageIndicator) findViewById(R.id.home_indicator);

        mBtnMarket = (Button) findViewById(R.id.home_market);
        mBtnForum = (Button) findViewById(R.id.home_Forum);
        mBtnNews = (Button) findViewById(R.id.home_News);
        mBtnShop= (Button) findViewById(R.id.home_shop);
        mBtnPersonal = (Button) findViewById(R.id.home_personal);

        mIvProduct = (ImageView) findViewById(R.id.home_iv_product);

        View goodsOne = findViewById(R.id.goods_one);
        mTitleOne = (TextView) goodsOne.findViewById(R.id.home_tv_title);
        mDescOne = (TextView) goodsOne.findViewById(R.id.home_tv_desc);
        mPriceOne = (TextView) goodsOne.findViewById(R.id.home_tv_price);
        mBtnOne = (Button) goodsOne.findViewById(R.id.home_btn_detail);
        mFavoritesOne = (TextView) goodsOne.findViewById(R.id.home_tv_favorites);

    }

    private void initView() {
        mBtnMarket.setOnClickListener(this);
        mBtnForum.setOnClickListener(this);
        mBtnNews.setOnClickListener(this);
        mBtnShop.setOnClickListener(this);
        mBtnPersonal.setOnClickListener(this);

        mBanner.setImageUrl(BANNER_URL, "s0_04895059675997715.jpg,s0_04895059874744164.jpg,s0_04895060498121693.jpg");
        mIndicator.setSelectedColor(android.R.color.white);
        mIndicator.setUnselectedColor(R.color.caption);
        mIndicator.setLineWidth(50);
        mBanner.setIndicator(mIndicator);
    }

    private void loadData() {
        HodorAPI.getRequest("http://www.chahuitong.com/wap/index.php/Home/Index/homePromotionGoods", new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                RecommendResponse recommend = new Gson().fromJson(response, RecommendResponse.class);
                goods1 = recommend.getContent().get(0);
                BitmapUtils utils = new BitmapUtils(HomeActivity.this);
                utils.display(mIvProduct, IMAGE_URL + goods1.getImageUrl());
                mTitleOne.setText(goods1.getName());
                mDescOne.setText(goods1.getDescription());
                mPriceOne.setText(("￥" + goods1.getPrice()));
                mBtnOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (goods1 != null) {
                            Bundle bundleGoods1 = new Bundle();
                            bundleGoods1.putString("url", GOODS_DETAIL + goods1.getGoods_id());
                            openActivity(ContentActivity.class, bundleGoods1);
                        }
                    }
                });
                HodorAPI.favorites(goods1.getGoods_id(), new VolleyRequest() {
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
//                Bundle extras = new Bundle();
//                extras.putString("url", "http://www.chahuitong.com/wap/index.php/Home/news");
                openActivity(NewsActivity.class);
                break;
            case R.id.home_shop :
//                Bundle shop = new Bundle();
//                shop.putString("url", "http://www.chahuitong.com/wap/index.php/Home/Index/brand");
                openActivity(ShopActivity.class);
                break;
            case R.id.home_personal :
//                Bundle personal = new Bundle();
//                personal.putString("url", "http://www.chahuitong.com/wap/index.php/Home/Index/member");
                openActivity(PersonalActivity.class);
                break;
        }
    }
}
