package com.damenghai.chahuitong2.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.api.Api;
import com.damenghai.chahuitong2.api.HomeApi;
import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.bean.Banner;
import com.damenghai.chahuitong2.bean.Goods;
import com.damenghai.chahuitong2.config.Config;
import com.damenghai.chahuitong2.response.ResponseCallBackListener;
import com.damenghai.chahuitong2.utils.ImageConfigHelper;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.view.BannerViewPager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.LinePageIndicator;

import java.util.List;

import cn.bmob.im.BmobChat;

public class HomeActivity extends BaseActivity {
    /**
     * 产品页根路径
     */
    private final String GOODS_DETAIL = "http://www.chahuitong.com/wap/index.php/Home/Index/goods?goods_id=";

    private long mBackTime = 0L;

    @ViewInject(R.id.home_bne_banner)
    private BannerViewPager mBanner;

    @ViewInject(R.id.home_indicator)
    private LinePageIndicator mIndicator;

    @ViewInject(R.id.home_product_layout)
    private LinearLayout mLayout;

    @ViewInject(R.id.home_iv_product)
    private ImageView mIvProduct;

    @ViewInject(R.id.home_tv_title)
    private TextView mTitle;

    @ViewInject(R.id.home_tv_desc)
    private TextView mTvDesc;

    @ViewInject(R.id.home_tv_price)
    private TextView mTvPrice;

    @ViewInject(R.id.home_tv_like)
    private TextView mTvLike;

    private HomeApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ViewUtils.inject(this);

        bindView();

        initView();

        loadData();
    }

    public void initView() {
        mApi = new HomeApi(this);

//        // 设置任意网络环境下都提示更新
//        UmengUpdateAgent.setUpdateOnlyWifi(false);
//        // 友盟自动检测更新
//        UmengUpdateAgent.update(this);

        // 蒲公英内测更新
//        PgyUpdateManager.register(this);

        //可设置调试模式，当为true的时候，会在logcat的BmobChat下输出一些日志，包括推送服务是否正常运行，如果服务端返回错误，也会一并打印出来。方便开发者调试，正式发布应注释此句。
//		BmobChat.DEBUG_MODE = true;
        //BmobIM SDK初始化--只需要这一段代码即可完成初始化
        BmobChat.getInstance(this).init(Config.applicationId);
    }

    public void bindView() {}

    /** 设置Banner图片 */
    public void showBanner(List<Banner> banners) {
        mBanner.setImageUrl(Api.IMAGE_PATH_ROOT, banners);
        mBanner.setIndicator(mIndicator);
    }

    /** 显示推荐的产品 */
    public void showProduct(final Goods goods) {
        BitmapUtils utils = new BitmapUtils(HomeActivity.this);
        utils.display(mIvProduct, Api.IMAGE_PRODUCT_PATH_ROOT + goods.getImageUrl(),
                ImageConfigHelper.getIconConfig(HomeActivity.this));
        mTitle.setText(goods.getName());
        mTvDesc.setText(goods.getDescription());
        mTvPrice.setText("￥" + goods.getPrice());
        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("url", GOODS_DETAIL + goods.getGoods_id());
                openActivity(WebViewActivity.class, bundle);
            }
        });
        mTvLike.setText(goods.getFav());
    }

    public void toStartModule(View v) {
        switch (v.getId()) {
            case R.id.home_btn_market:
                openActivity(MarketActivity.class);
                break;
            case R.id.home_btn_forum:
                openActivity(ForumActivity.class);
                break;
            case R.id.home_btn_news:
                openActivity(NewsActivity.class);
                break;
            case R.id.home_btn_shop:
                openActivity(CategoryActivity.class);
                break;
            case R.id.home_personal :
                openActivity(PersonalActivity.class);
                break;
        }
    }

    private void loadData() {
        mApi.showBanners(new ResponseCallBackListener<List<Banner>>() {
            @Override
            public void onSuccess(List<Banner> data) {
                showBanner(data);
            }

            @Override
            public void onError(int errorEvent, String message) {
                T.showShort(HomeActivity.this, message);
            }
        });

        mApi.showProduct(new ResponseCallBackListener<Goods>() {
            @Override
            public void onSuccess(Goods goods) {
                showProduct(goods);
            }

            @Override
            public void onError(int errorEvent, String message) {
                T.showShort(HomeActivity.this, message);
            }
        });

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
