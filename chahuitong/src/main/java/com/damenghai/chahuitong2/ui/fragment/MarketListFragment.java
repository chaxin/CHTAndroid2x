package com.damenghai.chahuitong2.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.damenghai.chahuitong2.api.TeaMarketApi;
import com.damenghai.chahuitong2.base.BaseFragment;
import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.BaseListAdapter;
import com.damenghai.chahuitong2.bean.response.MarketProductsResponse;
import com.damenghai.chahuitong2.bean.Product;
import com.damenghai.chahuitong2.response.ResponseCallBackListener;
import com.damenghai.chahuitong2.ui.activity.MarketActivity;
import com.damenghai.chahuitong2.ui.activity.ProductActivity;
import com.damenghai.chahuitong2.utils.T;
import com.damenghai.chahuitong2.utils.ViewHolder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class MarketListFragment extends BaseFragment implements OnItemClickListener {
	private final static String KEY_TAB = "MarketListFragment:tab";

	/**
	 * 以每页10个产品分页
	 */
	private final int PAGE_ITEM = 10;

    private View mView;

	private String mTab;
    private int mCurPage;

	private ArrayList<Product> mDatas;

	private PullToRefreshListView mListView;
	private ListViewAdapter mAdapter;
	private LinearLayout mLoading;
    private View mFooterView;

    private TeaMarketApi mApi;

	public static MarketListFragment get(String tab) {
		MarketListFragment fragment = new MarketListFragment();
		fragment.mTab = tab;
        return fragment;
	}

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

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_TAB)) {
            mTab = savedInstanceState.getString(KEY_TAB);
        }

        EventBus.getDefault().register(this);
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        initView();

		loadData(1);

		return mView;
	}

    private void initView() {
        mView = View.inflate(getActivity(), R.layout.fragment_pull2refresh_list, null);
        mListView = (PullToRefreshListView) mView.findViewById(R.id.id_product_list);
        mLoading =  (LinearLayout) mView.findViewById(R.id.id_list_pd);

        mDatas = new ArrayList<>();
        mAdapter = new ListViewAdapter(getActivity(), mDatas, R.layout.listview_item_product);
        mListView.setAdapter(mAdapter);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(1);
            }
        });
        mListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                loadData(mCurPage + 1);
            }
        });
        mListView.setOnItemClickListener(MarketListFragment.this);
        mFooterView = View.inflate(getActivity(), R.layout.load_more_footer, null);
    }

    /**
     * 根据页数加载数据
     *
     * @param page
     */
	private void loadData(final int page) {
        mApi.productsShow(mTab, page, new ResponseCallBackListener<MarketProductsResponse>() {
            @Override
            public void onSuccess(MarketProductsResponse data) {
                mLoading.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);

                mCurPage = page;

                if (page == 1) {
                    mDatas.clear();
                }

                mAdapter.addList(data.getData());

                if(mCurPage < (data.getTotal() + PAGE_ITEM -1)/PAGE_ITEM) {
                    addFootView(mListView, mFooterView);
                } else {
                    removeFootView(mListView, mFooterView);
                }

                if (mListView.isRefreshing()) mListView.onRefreshComplete();
            }

            @Override
            public void onError(int errorEvent, String message) {
                T.showShort(getActivity(), message);
            }
        });
    }

    private void addFootView(PullToRefreshListView plv, View footView) {
        ListView lv = plv.getRefreshableView();
        if(lv.getFooterViewsCount() == 1) {
            lv.addFooterView(footView);
        }
    }

    private void removeFootView(PullToRefreshListView plv, View footView) {
        ListView lv = plv.getRefreshableView();
        if(lv.getFooterViewsCount() > 1) {
            lv.removeFooterView(footView);
        }
    }

    public void onEvent(Product product) {
        loadData(1);
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
        Bundle extras = new Bundle();
        extras.putSerializable("product", mDatas.get(position-1));
        openActivity(ProductActivity.class, extras);
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TAB, mTab);
    }

    private class ListViewAdapter extends BaseListAdapter<Product> {

        public ListViewAdapter(Context context, ArrayList<Product> datas, int resId) {
            super(context, datas, resId);
        }

        @Override
        public void convert(ViewHolder holder, final Product t) {
            holder.setText(R.id.id_tv_title, t.getTitle())
                    .setText(R.id.market_tv_desc, t.getDesc())
                    .setText(R.id.id_tv_price, t.getPrice().equals("") ? "" : "￥" + t.getPrice())
                    .setText(R.id.id_tv_date, t.getDate().substring(0, 10))
                    .loadDefaultImage(R.id.id_img, t.getImgUrl());
        }

    }

}
