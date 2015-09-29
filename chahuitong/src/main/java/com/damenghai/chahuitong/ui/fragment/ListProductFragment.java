package com.damenghai.chahuitong.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.damenghai.chahuitong.api.TeaMarketAPI;
import com.damenghai.chahuitong.base.BaseFragment;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.bean.response.MarketProductsResponse;
import com.damenghai.chahuitong.bean.Product;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.ui.activity.ProductActivity;
import com.damenghai.chahuitong.utils.DateUtils;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.utils.ViewHolder;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

public class ListProductFragment extends BaseFragment implements OnItemClickListener {
	private final static String BUNDLE_SALEWAY = "sale";

	/**
	 * 以每页10个产品分页
	 */
	private final int PAGE_ITEM = 10;

    private View mView;

	private int mSaleway, mCurrentPage;

	private ArrayList<Product> mDatas;

	private PullToRefreshListView mListView;
	private ListViewAdapter mAdapter;
	private LinearLayout mLoading;
    private View mFooterView;

	public ListProductFragment() {
		super();
	}

	public static ListProductFragment get(int saleway) {
		ListProductFragment fragment = new ListProductFragment();
		Bundle bundle  = new Bundle();
		bundle.putInt(BUNDLE_SALEWAY, saleway);
		fragment.setArguments(bundle);
		return fragment;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle;
        if((bundle = getArguments()) != null) {
            mSaleway = bundle.getInt(BUNDLE_SALEWAY);
        }
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

        mDatas = new ArrayList<Product>();
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
                loadData(mCurrentPage + 1);
            }
        });
        mListView.setOnItemClickListener(ListProductFragment.this);
        mFooterView = View.inflate(getActivity(), R.layout.load_more_footer, null);
    }

    /**
     * 根据页数加载数据
     *
     * @param page
     */
	private void loadData(final int page) {
        TeaMarketAPI.productsShow(mSaleway + "", page, new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                mLoading.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);

                L.d(response);

                mCurrentPage = page;

                if (page == 1) {
                    mDatas.clear();
                }

                MarketProductsResponse productsResponse = new Gson().fromJson(response, MarketProductsResponse.class);
                addData(productsResponse);
            }

            @Override
            public void onAllDone() {
                super.onAllDone();
                mListView.onRefreshComplete();
            }
        });
	}

    private void addData(MarketProductsResponse resBean) {
        for(Product product : resBean.getData()) {
            if(!mDatas.contains(product)) {
                mDatas.add(product);
            }
        }

        mAdapter.notifyDataSetChanged();

        if(mCurrentPage < (resBean.getTotal() + PAGE_ITEM -1)/PAGE_ITEM) {
            addFootView(mListView, mFooterView);
        } else {
            removeFootView(mListView, mFooterView);
        }
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
        Bundle extras = new Bundle();
        extras.putSerializable("product", mDatas.get(position-1));
        openActivity(ProductActivity.class, extras);
	}

    private class ListViewAdapter extends CommonAdapter<Product> {

        public ListViewAdapter(Context context, ArrayList<Product> datas, int resId) {
            super(context, datas, resId);
        }

        @Override
        public void convert(ViewHolder holder, final Product t) {
            holder.setText(R.id.id_tv_title, t.getTitle())
                    .setText(R.id.market_tv_desc, t.getDesc())
                    .setText(R.id.id_tv_price, t.getPrice().equals("") ? "" : "￥" + t.getPrice())
                    .setText(R.id.id_tv_date, DateUtils.convert2US(t.getDate()))
                    .loadDefaultImage(R.id.id_img, t.getImgUrl())
            ;

        }

    }

}
