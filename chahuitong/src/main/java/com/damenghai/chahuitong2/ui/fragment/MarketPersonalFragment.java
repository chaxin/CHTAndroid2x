package com.damenghai.chahuitong2.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.damenghai.chahuitong2.api.TeaMarketApi;
import com.damenghai.chahuitong2.base.BaseFragment;
import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.BaseListAdapter;
import com.damenghai.chahuitong2.bean.Product;
import com.damenghai.chahuitong2.response.ResponseCallBackListener;
import com.damenghai.chahuitong2.ui.activity.MarketActivity;
import com.damenghai.chahuitong2.ui.activity.ProductActivity;
import com.damenghai.chahuitong2.ui.activity.PublishActivity;
import com.damenghai.chahuitong2.utils.DateUtils;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.utils.ViewHolder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MarketPersonalFragment extends BaseFragment implements OnItemClickListener, PullToRefreshBase.OnRefreshListener {
    private PullToRefreshListView mListView;

	private ListViewAdapter mAdapter;
	private ArrayList<Product> mData;
    private View mView;

    TeaMarketApi mApi;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MarketActivity) {
            mApi = ((MarketActivity) context).mApi;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_product, container, false);

        initView();

		loadData(1);

		return mView;
	}

    private void initView() {
        mListView = (PullToRefreshListView) mView.findViewById(R.id.mine_listview);

        mData = new ArrayList<>();
        mAdapter = new ListViewAdapter(getActivity(), mData, R.layout.listview_item_my_product);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnRefreshListener(this);
    }

	private void loadData(final int page) {
        mApi.myProduct(new ResponseCallBackListener<List<Product>>() {
            @Override
            public void onSuccess(List<Product> data) {
                if (page == 1) mData.clear();

                mAdapter.addList(data);
                if (mListView.isRefreshing()) mListView.onRefreshComplete();
            }

            @Override
            public void onError(int errorEvent, String message) {
                T.showShort(getActivity(), message);
            }
        });

	}

    public void onEvent(Product product) {
        loadData(1);
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
        loadData(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private class ListViewAdapter extends BaseListAdapter<Product> {

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
                                    TeaMarketApi api = new TeaMarketApi(mContext);
                                    api.deleteProduct(product.getId(), new ResponseCallBackListener<String>() {
                                        @Override
                                        public void onSuccess(String data) {
                                            T.showShort(getActivity(), "删除成功");
                                            mData.remove(product);
                                            notifyDataSetChanged();
                                            EventBus.getDefault().post(product);
                                        }

                                        @Override
                                        public void onError(int errorEvent, String message) {
                                            T.showShort(mContext, message);
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
                            openActivity(PublishActivity.class, bundle);
                        }
                    });
        }
    }

}
