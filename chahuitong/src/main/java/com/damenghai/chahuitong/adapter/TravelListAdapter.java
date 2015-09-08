package com.damenghai.chahuitong.adapter;

import android.content.Context;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.bean.Travel;
import com.damenghai.chahuitong.utils.ViewHolder;

import java.util.List;

/**
 * Created by Sgun on 15/9/2.
 */
public class TravelListAdapter extends CommonAdapter<Travel> {

    public TravelListAdapter(Context context, List<Travel> mDatas, int resId) {
        super(context, mDatas, resId);
    }

    @Override
    public void convert(ViewHolder holder, Travel travel) {
        holder.setText(R.id.travel_item_title, travel.getTitle())
                .setText(R.id.travel_item_content, "地       点：" + travel.getLocation()
                                                    + "\n状       态："
                                                    + "\n行程天数：" + travel.getDuration() + "天"
                                                    + "\n行程时间：" + travel.getJoin_time()
                                                    + "\n活动宣言：" + travel.getContent()
                                                    + "\n费       用：" + travel.getFee());
        if(!travel.getThumbImage().equals("")) holder.loadImage(R.id.travel_item_image, travel.getThumbImage());
    }
}
