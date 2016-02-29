package com.damenghai.chahuitong2.ui.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.BaseListAdapter;
import com.damenghai.chahuitong2.api.ShopApi;
import com.damenghai.chahuitong2.base.BaseFragment;
import com.damenghai.chahuitong2.bean.Brand;
import com.damenghai.chahuitong2.bean.response.BrandResponse;
import com.damenghai.chahuitong2.response.ResponseCallBackListener;
import com.damenghai.chahuitong2.ui.activity.QuotationActivity;
import com.damenghai.chahuitong2.ui.activity.WebViewActivity;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.utils.ViewHolder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/8/18.
 */
public class BrandFragment extends BaseFragment implements OnRefreshListener<GridView>, OnItemClickListener {
    private static final String KEY_CATEGORY = "BrandFragment:category";

    private static final String KEY_SIGN = "BrandFragment:sign";

    /** 类别ID:1-普洱茶, 2-乌龙茶, 3-红茶, 256-绿茶, 308-黑茶, 470-花茶, 530-白茶, 662-茶具, 593-其他 */
    private String mCategory;

    /** 用于区别显示位置的标识，分别有行情-quotation, 商城-shop */
    private String mSign;

    @ViewInject(R.id.shop_ad)
    private TextView mTvAd;

    @ViewInject(R.id.shop_gridview)
    private PullToRefreshGridView mGridView;

    private ShopApi mApi;

    private List<Brand> mDatas;

    private Adapter mAdapter;

    public static BrandFragment get(String categoryId, String sign) {
        BrandFragment fragment = new BrandFragment();
        fragment.mCategory = categoryId;
        fragment.mSign = sign;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_CATEGORY)) mCategory = savedInstanceState.getString(KEY_CATEGORY);
            if (savedInstanceState.containsKey(KEY_SIGN)) mSign = savedInstanceState.getString(KEY_SIGN);
        }

        mApi = new ShopApi(getActivity());

        mDatas = new ArrayList<>();

        mAdapter = new Adapter(getActivity(), mDatas, R.layout.gridview_item_image);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brand, null);

        ViewUtils.inject(this, view);

        mGridView.setAdapter(mAdapter);
        mGridView.setOnRefreshListener(this);
        mGridView.setOnItemClickListener(this);

        loadData();

        return view;
    }

    private void loadData() {

        mApi.showBrands(mCategory, new ResponseCallBackListener<BrandResponse>() {

            @Override
            public void onSuccess(final BrandResponse brandResponse) {
                if (mSign.equals("quotation")) {
                    mTvAd.setVisibility(View.GONE);
                } else if (!TextUtils.isEmpty(brandResponse.getAdv().getSlogan())) {
                    mTvAd.setText(brandResponse.getAdv().getSlogan());
                    mTvAd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle bundle = new Bundle();
                            bundle.putString("url", brandResponse.getAdv().getGoods_link());
                            openActivity(WebViewActivity.class, bundle);
                        }
                    });
                }

                mAdapter.addList(brandResponse.getBrands());

                if (mGridView.isRefreshing()) mGridView.onRefreshComplete();
            }

            @Override
            public void onError(int errorEvent, String message) {
                T.showShort(getActivity(), message);
            }

        });

    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        loadData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CATEGORY, mCategory);
        outState.putString(KEY_SIGN, mSign);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(mSign != null && mSign.equals("quotation")) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("brand", mDatas.get(i));
            openActivity(QuotationActivity.class, bundle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("url", genBrandDetailUrl(mDatas.get(i).getId()));
            openActivity(WebViewActivity.class, bundle);
        }
    }

    public String genBrandDetailUrl(String id) {
        return "http://www.chahuitong.com/wap/index.php/Home/Index/brandGoods/bid/" + id;
    }

    private class Adapter extends BaseListAdapter<Brand> {

        public Adapter(Context context, List<Brand> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void convert(ViewHolder holder, Brand brand) {
            GridView gv = (GridView) holder.getParent();
            int numCol = gv.getNumColumns();
            int width;
            if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.JELLY_BEAN) {
                width = (gv.getWidth() - gv.getPaddingLeft() * (numCol + 1)) / numCol;
            } else {
                width = (gv.getWidth() - gv.getHorizontalSpacing() * (numCol - 1)
                        - gv.getPaddingRight() - gv.getPaddingLeft()) / numCol;
            }

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
            ImageView iv_logo = holder.getView(R.id.gridview_item_image);
            iv_logo.setLayoutParams(params);

            holder.loadDefaultImage(R.id.gridview_item_image, "http://www.chahuitong.com/data/upload/shop/brand/" + brand.getPic());
        }
    }

}
