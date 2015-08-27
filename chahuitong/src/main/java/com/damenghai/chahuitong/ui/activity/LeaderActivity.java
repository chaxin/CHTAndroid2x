package com.damenghai.chahuitong.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.damenghai.chahuitong.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.bean.Leader;
import com.damenghai.chahuitong.bean.Status;
import com.damenghai.chahuitong.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/8/25.
 */
public class LeaderActivity extends BaseActivity {
    private Leader mLeader;

    private TextView mName;
    private TextView mTitle;
    private ListView mLv;

    private ArrayList<Status> mDatas;
    private ListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader);

        mLeader = (Leader) getIntent().getSerializableExtra("leader");

        findViewById();

        initView();
    }

    public void findViewById() {
        mName = (TextView) findViewById(R.id.leader_detail_name);
        mTitle = (TextView) findViewById(R.id.leader_detail_title);
        mLv = (ListView) findViewById(R.id.leader_lv_detail);
    }

    private void initView() {
        mDatas = new ArrayList<Status>();
        initDatas();
        mAdapter = new ListViewAdapter(this, mDatas, R.layout.listview_item_leader_detail);
        mLv.setAdapter(mAdapter);

        if(mLeader != null) {
            mName.setText(mLeader.getName());
            mTitle.setText(mLeader.getTitle());
        }
    }

    private void initDatas() {
        Status status = new Status();
        status.setSource("56分钟 来自捡来的iphone6");
        status.setText("从纽约到柏林，什么都能少，唯一不能少的是壶好茶。从纽约到柏林，什么都能少，唯一不能少的是壶好茶。");
        mDatas.add(status);

        Status status1 = new Status();
        status1.setSource("4分钟前 来自 微博 weibo.com");
        status1.setText("这是一个很酷的HTML5技术应用，用户可以在画布上用画笔写出自己想看的文字或图案，这个应该能将你写的东西用3D方式呈现，而且让它围着中心3D旋转，只要你设计的巧妙，你可以绘制出让人惊叹的效果和图案。");
        mDatas.add(status1);

        Status status2 = new Status();
        status2.setSource("11分钟前 来自 微博 weibo.com");
        status2.setText("华尔街日报：中国创业公司的黄金时代已结束？】一些投资人和创业者指出，由于市场对经济的信心不足以及股市大跌，创业圈火爆的局面很可能将会改变。甚至有人认为，中国市场存在泡沫，而泡沫将在一两年内破灭。详情请戳：");
        mDatas.add(status2);
    }

    private class ListViewAdapter extends CommonAdapter<Status> {

        public ListViewAdapter(Context context, List<Status> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(ViewHolder holder, Status status) {
            holder.setText(R.id.status_detail_source, status.getSource())
                    .setText(R.id.status_detail_text, status.getText());
        }
    }
}
