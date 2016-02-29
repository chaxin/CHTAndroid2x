package com.damenghai.chahuitong2.adapter;

import android.content.Context;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.bean.Travel;
import com.damenghai.chahuitong2.utils.ViewHolder;

import java.util.List;

/**
 * Created by Sgun on 15/9/2.
 */
public class TravelListAdapter extends BaseListAdapter<Travel> {

    public TravelListAdapter(Context context, List<Travel> mDatas, int resId) {
        super(context, mDatas, resId);
    }

    @Override
    public void convert(ViewHolder holder, Travel travel) {
        holder.setText(R.id.travel_item_title, travel.getTitle())
                .setText(R.id.travel_item_content,  mContext.getResources().getString(R.string.event_location) + travel.getLocation()
                                                    + "\n" + mContext.getResources().getString(R.string.event_state) + travel.getState()
                                                    + "\n" + mContext.getResources().getString(R.string.event_time) + travel.getJoin_time()
                                                    + "\n" + mContext.getResources().getString(R.string.event_target) + travel.getObject()
                                                    + "\n" + mContext.getResources().getString(R.string.event_count) + travel.getNumber());
        if(!travel.getThumbImage().equals("")) holder.loadImage(R.id.travel_item_image, travel.getThumbImage());
    }
}
