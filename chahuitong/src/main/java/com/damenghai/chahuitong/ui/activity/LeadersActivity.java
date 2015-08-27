package com.damenghai.chahuitong.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.GridView;

import com.damenghai.chahuitong.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.bean.Leader;
import com.damenghai.chahuitong.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/8/23.
 */
public class LeadersActivity extends BaseActivity {
    private GridView mGv;
    private GridViewAdapter mAdapter;
    private ArrayList<Leader> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaders);

        findViewById();

        initView();
    }

    private void findViewById() {
        mGv = (GridView) findViewById(R.id.leader_list_gv);
    }

    private void initView() {
        mDatas = new ArrayList<Leader>();

        initData();

        mAdapter = new GridViewAdapter(this, mDatas, R.layout.gridview_item_leader_list);
        mGv.setAdapter(mAdapter);
    }

    private void initData() {
        Leader leader1 = new Leader();
        leader1.setFavorites(true);
        leader1.setName("Eson");
        leader1.setTitle("中国茶协会会长");
        mDatas.add(leader1);
        Leader leader2 = new Leader();
        leader2.setFavorites(false);
        leader2.setName("Jay");
        leader2.setTitle("中国茶协会会长");
        mDatas.add(leader2);
        Leader leader3 = new Leader();
        leader3.setFavorites(true);
        leader3.setName("Fuck");
        leader3.setTitle("中国茶协会会长");
        mDatas.add(leader3);
        Leader leader4 = new Leader();
        leader4.setFavorites(false);
        leader4.setName("Caly");
        leader4.setTitle("中国茶协会会长");
        mDatas.add(leader4);
        Leader leader5 = new Leader();
        leader5.setFavorites(true);
        leader5.setName("Caly");
        leader5.setTitle("中国茶协会会长");
        mDatas.add(leader5);
        Leader leader6 = new Leader();
        leader6.setFavorites(false);
        leader6.setName("Caly");
        leader6.setTitle("中国茶协会会长");
        mDatas.add(leader6);
    }

    private class GridViewAdapter extends CommonAdapter<Leader> {

        public GridViewAdapter(Context context, List<Leader> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @SuppressLint("NewApi")
        @Override
        public void convert(ViewHolder holder, Leader leader) {
//            GridView gv = (GridView) holder.getParent();
//            int numCols = gv.getNumColumns();
//            int width = (gv.getWidth() - gv.getHorizontalSpacing() * (numCols - 1)
//                    - gv.getPaddingLeft() - gv.getPaddingRight()) / numCols;
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);

            holder.setText(R.id.leader_item_name, leader.getName())
                    .setText(R.id.leader_item_title, leader.getTitle())
                    .setTextColor(R.id.leader_item_follow, leader.isFavorites() ? R.color.primary : android.R.color.black)
                    .setTextDrawableLeft(R.id.leader_item_follow, leader.isFavorites() ? R.drawable.icon_followed : R.drawable.icon_follow);
        }
    }
}
