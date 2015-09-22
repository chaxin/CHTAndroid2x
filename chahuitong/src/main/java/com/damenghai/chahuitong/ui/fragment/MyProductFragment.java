package com.damenghai.chahuitong.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.damenghai.chahuitong.api.TeaMarketAPI;
import com.damenghai.chahuitong.base.BaseFragment;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.bean.Product;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.ui.activity.EditActivity;
import com.damenghai.chahuitong.ui.activity.ProductActivity;
import com.damenghai.chahuitong.utils.DateUtils;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.utils.ViewHolder;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MyProductFragment extends BaseFragment implements OnItemClickListener, PullToRefreshBase.OnRefreshListener {
    private ProgressBar mPb;
	private	TextView mNoProduct;
    private PullToRefreshListView mListView;

	private ListViewAdapter mAdapter;
	private ArrayList<Product> mData;
    private View mView;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_product, container, false);

        initView();

		loadData();

		return mView;
	}

    private void initView() {
        mPb = (ProgressBar) mView.findViewById(R.id.id_individual_pb);
        mListView = (PullToRefreshListView) mView.findViewById(R.id.mine_listview);
        mNoProduct = (TextView) mView.findViewById(R.id.id_no_product);

        mData = new ArrayList<Product>();
        mAdapter = new ListViewAdapter(getActivity(), mData, R.layout.listview_item_my_product);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnRefreshListener(this);
    }

	private void loadData() {
		TeaMarketAPI.myProduct(getActivity(), new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                mData.clear();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        Product product = new Gson().fromJson(array.getString(i), Product.class);
                        if (!mData.contains(product)) mData.add(product);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
                if (mListView.isRefreshing()) mListView.onRefreshComplete();
            }
        });
	}

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Product product = mData.get(i - 1);
        Bundle extras = new Bundle();
        extras.putSerializable("product", product);
        openActivity(ProductActivity.class, extras);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        loadData();
    }

    private class ListViewAdapter extends CommonAdapter<Product> {
        public ListViewAdapter(Context context, List<Product> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(ViewHolder holder, final Product product) {
            holder.setText(R.id.mine_tv_title, product.getName())
                    .setText(R.id.mine_tv_date, DateUtils.convert2US(product.getDate()))
                    .setText(R.id.mine_tv_price, "￥" + product.getPrice())
                    .loadDefaultImage(R.id.mine_iv_image, product.getImgUrl())
                    .setTextOnClickListener(R.id.mine_btn_delete, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setMessage("确定要删除？");
                            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    TeaMarketAPI.deleteMyProduct(product.getId(), new VolleyRequest() {
                                        @Override
                                        public void onSuccess(String response) {
                                            super.onSuccess(response);
                                            if (response.equals("1")) {
                                                T.showShort(getActivity(), "删除成功");
                                                mDatas.remove(product);
                                                notifyDataSetChanged();
                                            }
                                        }
                                    });
                                }
                            });
                            builder.setPositiveButton("取消", null);
                            builder.create().show();
                        }
                    })
                    .setOnClickListener(R.id.my_product_edit, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("product", product);
                            openActivity(EditActivity.class, bundle);
                        }
                    });
        }
    }

}
