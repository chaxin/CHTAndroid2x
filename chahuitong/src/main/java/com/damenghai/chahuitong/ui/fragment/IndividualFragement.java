package com.damenghai.chahuitong.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.damenghai.chahuitong.BaseFragment;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.bean.Product;
import com.damenghai.chahuitong.config.Constants;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.ui.activity.ProductActivity;
import com.damenghai.chahuitong.utils.DateUtils;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.utils.ViewHolder;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndividualFragement extends BaseFragment implements OnItemClickListener {
	private ProgressBar mPb;
	private PullToRefreshListView mListView;
	private	TextView mNoProduct;
	private ListViewAdapter mAdapter;
	private ArrayList<Product> mDatas;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_my_product, container,
				false);
		mPb = (ProgressBar) view.findViewById(R.id.id_individual_pb);
		mListView = (PullToRefreshListView) view.findViewById(R.id.mine_listview);
		mNoProduct = (TextView) view.findViewById(R.id.id_no_product);
		mDatas = new ArrayList<Product>();
        mAdapter = new ListViewAdapter(getActivity(), mDatas, R.layout.listview_item_my_product);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

		loadDatas();

		return view;
	}

	private void loadDatas() {
        Product product1 = new Product();
        product1.setName("1中茶2015大红印圆茶，“红印”普洱圆茶是中国普洱茶里的里程碑");
        product1.setImgUrl("1437457626_1.jpg");
        product1.setPrice("2,000");
        product1.setDate("2011-12-11 10:10:10");
        Product product2 = new Product();
        product2.setName("2中茶2015大红印圆茶，“红印”普洱圆茶是中国普洱茶里的里程碑");
        product2.setImgUrl("1437457626_1.jpg");
        product2.setPrice("2,000");
        Product product3 = new Product();
        product3.setName("3中茶2015大红印圆茶，“红印”普洱圆茶是中国普洱茶里的里程碑");
        product3.setImgUrl("1437457626_1.jpg");
        product3.setPrice("2,000");
        Product product4 = new Product();
        product4.setName("4中茶2015大红印圆茶，“红印”普洱圆茶是中国普洱茶里的里程碑");
        product4.setImgUrl("1437457626_1.jpg");
        product4.setPrice("2,000");
        mDatas.add(product1);
        mDatas.add(product2);
        mDatas.add(product3);
        mDatas.add(product4);

        mAdapter.notifyDataSetChanged();

//		HodorAPI.myProductShow(SessionKeeper.readSession(getActivity()), new VolleyRequest() {
//			@Override
//			public void onSuccess(String response) {
//				super.onSuccess(response);
//				L.d(response);
//			}
//		});

	}

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Product product = mDatas.get(i - 1);
        Bundle extras = new Bundle();
        extras.putSerializable("product", product);
        openActivity(ProductActivity.class, extras);
    }

    private class ListViewAdapter extends CommonAdapter<Product> {

        public ListViewAdapter(Context context, List<Product> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(ViewHolder holder, final Product product) {
            holder.setText(R.id.mine_tv_title, product.getName())
                    .setText(R.id.mine_tv_date, DateUtils.convert2US(product.getDate()))
                    .setText(R.id.mine_tv_price, product.getPrice())
                    .loadUrlImage(R.id.mine_iv_image, product.getImgUrl())
                    .setTextOnClickListener(R.id.mine_btn_delete, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("id", product.getProductId() + "");
                            HodorAPI.stringPostRequest(Constants.API_DELETE_PRODUCT, map, new VolleyRequest() {
                                @Override
                                public void onSuccess(String response) {
                                    super.onSuccess(response);
                                    // Todo
                                    L.d("delete");
                                }
                            });
                        }
                    });
        }
    }

}
