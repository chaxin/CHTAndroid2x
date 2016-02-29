package com.damenghai.chahuitong2.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.bean.Travel;
import com.damenghai.chahuitong2.ui.activity.TravelActivity;
import com.damenghai.chahuitong2.utils.ImageConfigHelper;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by Sgun on 15/9/2.
 */

public class TravelViewPagerAdapter extends PagerAdapter {
    private ArrayList<Travel> mTravels;
    private Context mContext;
    private ArrayList<View> mViews;

    public TravelViewPagerAdapter(ArrayList<Travel> mTravels, Context mContext) {
        this.mTravels = mTravels;
        this.mContext = mContext;
        mViews = new ArrayList<View>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = View.inflate(mContext, R.layout.viewpager_item_travel, null);

        TextView title = (TextView) view.findViewById(R.id.travel_title);
        TextView time = (TextView) view.findViewById(R.id.travel_time);
        ImageView image = (ImageView) view.findViewById(R.id.travel_image);

        Travel travel = mTravels.get(position);
        title.setText(travel.getTitle());
        time.setText("开始时间：" + travel.getJoin_time() + "\n结束时间：" + travel.getDuration());
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TravelActivity.class);
                intent.putExtra("travel", mTravels.get(position));
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
            }
        });
        BitmapUtils utils = new BitmapUtils(mContext);
        utils.display(image, travel.getThumbImage(), ImageConfigHelper.getGrayConfig(mContext));

        mViews.add(view);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public int getCount() {
        return mTravels.size() > 3 ? 3 : mTravels.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
