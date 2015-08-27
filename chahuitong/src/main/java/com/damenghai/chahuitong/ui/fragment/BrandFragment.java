package com.damenghai.chahuitong.ui.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.bean.Brand;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.ui.activity.ContentActivity;
import com.damenghai.chahuitong.utils.ViewHolder;
import com.damenghai.chahuitong.view.TopBar;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/8/18.
 */
public class BrandFragment extends Fragment implements PullToRefreshBase.OnRefreshListener{
    private static final String KEY_TITLE = "BrandFragment:title";
    private static final String KEY_CATEGORY = "BrandFragment:category";

    private View mView;
    private TopBar mTopbar;
    private PullToRefreshGridView mGridView;

    private List<Brand> mDatas;
    private Adapter mAdapter;

    private String mTitle;
    private int mCategory;

    private AbsListView.OnScrollListener l;

    public static BrandFragment get(String title, int categoryId) {
        BrandFragment fragment = new BrandFragment();
        fragment.mTitle = title;
        fragment.mCategory = categoryId;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_TITLE)) {
            mTitle = savedInstanceState.getString(KEY_TITLE);
            mCategory = savedInstanceState.getInt(KEY_CATEGORY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_brand, null);

        mTopbar = (TopBar) mView.findViewById(R.id.shop_topbar);
        mGridView = (PullToRefreshGridView) mView.findViewById(R.id.shop_gridview);

        mDatas = new ArrayList<Brand>();
        mAdapter = new Adapter(getActivity(), mDatas, R.layout.gridview_item_image);

        if(mTitle != null) mTopbar.setTitle(mTitle);

        mGridView.setAdapter(mAdapter);
        mGridView.setOnRefreshListener(this);

        loadDatas();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("url", "http://www.chahuitong.com/wap/index.php/Home/Index/brandGoods/bid/" + mDatas.get(i).getId());
                startActivity(intent);
            }
        });

        return mView;
    }

    private void loadDatas() {
        if(mCategory <= 0) mCategory = 1;

        HodorAPI.brandShow(mCategory,new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONArray array = new JSONArray(response);
                    mDatas.clear();
                    for (int i = 0; i < array.length(); i++) {
                        Brand brand = new Gson().fromJson(array.get(i).toString(), Brand.class);
                        mDatas.add(brand);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAllDone() {
                super.onAllDone();
                mGridView.onRefreshComplete();
            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        loadDatas();
    }

    private class Adapter extends CommonAdapter<Brand> {

        public Adapter(Context context, List<Brand> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void convert(ViewHolder holder, Brand brand) {GridView gv = (GridView) holder.getParent();
            int horizontalSpacing = gv.getHorizontalSpacing();
            int numCol = gv.getNumColumns();
            int width = (gv.getWidth() - horizontalSpacing * (numCol - 1)
                    - gv.getPaddingRight() - gv.getPaddingLeft()) / numCol;

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
            ImageView iv_logo = holder.getView(R.id.gridview_item_image);
            iv_logo.setLayoutParams(params);

            holder.loadUrlImage(R.id.gridview_item_image, "http://www.chahuitong.com/data/upload/shop/brand/" + brand.getPic());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TITLE, mTitle);
        outState.putInt(KEY_CATEGORY, mCategory);
    }
}
