package com.damenghai.chahuitong.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.api.PersonalAPI;
import com.damenghai.chahuitong.bean.Voucher;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.utils.DensityUtils;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.utils.ViewHolder;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sgun on 15/10/15.
 */
public class VoucherPagerAdapter extends PagerAdapter {
    private final String[] TITLE = new String[] {"未使用", "已使用", "已过期", "被回收"};
    private final String[] TYPE = new String[] {"1", "2", "3", "4"};

    private ArrayList<View> mViews;

    private Context mContext;

    public VoucherPagerAdapter(Context context) {
        mContext = context;

        initViews();
    }

    private void initViews() {
        mViews = new ArrayList<View>();
        for (String title : TITLE) {
            mViews.add(View.inflate(mContext, R.layout.fragment_list, null));
        }
    }

    @Override
    public int getCount() {
        return TITLE.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews.get(position);

        PullToRefreshListView plv = (PullToRefreshListView) view.findViewById(R.id.common_listview);
        plv.setMode(PullToRefreshBase.Mode.DISABLED);
        plv.getRefreshableView().setDividerHeight(DensityUtils.dp2px(mContext, 4));
        loadData(plv, position);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    private void loadData(final PullToRefreshListView plv, final int position) {

        PersonalAPI.voucherShow(mContext, TYPE[position], new VolleyRequest() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getInt("code") == 200) {
                        JSONArray array = object.getJSONArray("content");
                        List<Voucher> vouchers = new ArrayList<Voucher>();
                        for (int i = 0; i < array.length(); i++) {
                            Voucher voucher = new Gson().fromJson(array.getString(i), Voucher.class);
                            vouchers.add(voucher);
                        }
                        VoucherListAdapter adapter = new VoucherListAdapter(mContext, vouchers, R.layout.listview_item_voucher, TYPE[position]);
                        plv.setAdapter(adapter);
                    } else {
                        T.showShort(mContext, object.getString("content"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private class VoucherListAdapter extends CommonAdapter<Voucher> {
        private String mType;

        public VoucherListAdapter(Context context, List<Voucher> data, int resId) {
            super(context, data, resId);
        }

        public VoucherListAdapter(Context context, List<Voucher> data, int resId, String type) {
            super(context, data, resId);
            this.mType = type;
        }

        @Override
        public void convert(ViewHolder holder, Voucher voucher) {
            holder.setText(R.id.voucher_item_value, voucher.getVoucher_price())
                    .setText(R.id.voucher_item_valid, voucher.getVoucher_start_date() + "~" + voucher.getVoucher_end_date())
                    .setText(R.id.voucher_item_limit, "【满" + voucher.getVoucher_limit() + "元使用】")
                    .setText(R.id.voucher_item_title, voucher.getVoucher_title());

            if(!TextUtils.isEmpty(mType)) {
                if (mType.equals("2")) {
                    setTypeUI(holder);
                    holder.setImageResource(R.id.voucher_item_sign, R.drawable.sign_voucher_used);
                } else if (mType.equals("3")) {
                    setTypeUI(holder);
                    holder.setImageResource(R.id.voucher_item_sign, R.drawable.sign_voucher_expired);
                } else if (mType.equals("4")) {
                    setTypeUI(holder);
                    holder.setImageResource(R.id.voucher_item_sign, R.drawable.sign_voucher_recovery);
                }
            }

        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private void setTypeUI(ViewHolder holder) {
            RelativeLayout layout = holder.getView(R.id.voucher_item_layout);
            layout.setBackground(mContext.getResources().getDrawable(R.drawable.background_dotted_line_voucher_used));
            holder.setImageResource(R.id.voucher_item_mark, R.drawable.icon_mark_vouch_used)
                    .setTextColor(R.id.voucher_item_title, R.color.text_body)
                    .setTextColor(R.id.voucher_item_yuan, R.color.text_body)
                    .setTextColor(R.id.voucher_item_value, R.color.text_body)
                    .setTextColor(R.id.voucher_item_title, R.color.text_body)
                    .setTextColor(R.id.voucher_item_valid, R.color.text_body)
                    .setVisibility(R.id.voucher_item_sign, View.VISIBLE);
        }
    }

}
