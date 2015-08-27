package com.damenghai.chahuitong.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.damenghai.chahuitong.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.bean.Travel;
import com.damenghai.chahuitong.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/8/23.
 */
public class TravleActivity extends BaseActivity {
    private ListView mLv;

    private Adapter mAdapter;
    private ArrayList<Travel> mTravels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        findViewById();
    }

    private void findViewById() {
        mLv = (ListView) findViewById(R.id.travels_lv);
        initView();
    }

    private void initView() {
        mTravels = new ArrayList<Travel>();
        initDatas();
        mAdapter = new Adapter(this, mTravels, R.layout.listview_item_travle);
        mLv.setAdapter(mAdapter);
    }

    private void initDatas() {
        Travel travel1 = new Travel();
        travel1.setTime("行程天数：3天\n行程时间：2015-08-23~2015-08-31");
        travel1.setTitle("布郎山淘茶之旅");
        travel1.setImageRes(R.drawable.test_travel_image1);
        mTravels.add(travel1);

        Travel travel2 = new Travel();
        travel2.setTime("行程天数：4天\n行程时间：2015-08-23~2015-08-24");
        travel2.setTitle("武夷山淘茶之旅");
        travel2.setImageRes(R.drawable.test_travel_image2);
        mTravels.add(travel2);

        Travel travel3 = new Travel();
        travel3.setTime("行程天数：5天\n行程时间：2015-08-23~2015-08-11");
        travel3.setTitle("布郎山淘茶之旅");
        travel3.setImageRes(R.drawable.test_travel_image3);
        mTravels.add(travel3);

        Travel travel4 = new Travel();
        travel4.setTime("行程天数：6天\n行程时间：2015-08-23~2015-08-14");
        travel4.setTitle("武夷山淘茶之旅");
        travel4.setImageRes(R.drawable.test_travel_image1);
        mTravels.add(travel4);
    }

    private class Adapter extends CommonAdapter<Travel> {

        public Adapter(Context context, List<Travel> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(ViewHolder holder, Travel travel) {
                holder.setText(R.id.travel_item_title, travel.getTitle())
                        .setImageResource(R.id.travel_item_image, travel.getImageRes())
                        .setText(R.id.travel_item_content, "地       点：西双版纳 百望山 艾冷山 勐海\n状       态：即将出发\n" + travel.getTime());
        }
    }
}
