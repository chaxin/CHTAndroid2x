package com.damenghai.chahuitong.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.damenghai.chahuitong.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.adapter.GridViewImagesAdapter;
import com.damenghai.chahuitong.bean.ImageUrls;
import com.damenghai.chahuitong.bean.Topic;
import com.damenghai.chahuitong.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/8/23.
 */
public class TopicsActivity extends BaseActivity {
    private View mHeader;
    private GridView mGvHeader;
    private ListView mListView;

    private ArrayList<Topic> mTopics;
    private ArrayList<ImageUrls> mGvImages;
    private GridViewImagesAdapter mGvAdapter;
    private ListViewAdapter mLvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        findViewById();

        initView();
    }

    private void findViewById() {
        mHeader = View.inflate(this, R.layout.header_topics_listview, null);
        mGvHeader = (GridView) mHeader.findViewById(R.id.wrap_gridview_images);
        mListView = (ListView) findViewById(R.id.topics_lv);
    }

    private void initView() {
        mTopics = new ArrayList<Topic>();
        mGvImages = new ArrayList<ImageUrls>();
        initDatas();
        mGvAdapter = new GridViewImagesAdapter(this, mGvImages, R.layout.gridview_item_image);
        mLvAdapter = new ListViewAdapter(this, mTopics, R.layout.listview_item_topic);
        mGvHeader.setAdapter(mGvAdapter);
        mListView.addHeaderView(mHeader);
        mListView.setAdapter(mLvAdapter);
    }

    private void initDatas() {
//        Topic topic = new Topic();
//        topic.setTitle("当今的普洱茶的收藏价值怎么样");
//        topic.setFrom("话题主理人：林林林");
//        topic.setDesc("近几年的普洱茶越来越被大家所熟知和接受，普洱茶的收藏也被大家纷纷提出，其实普洱茶在很早的时候就已经被各大收藏家收藏，现如今");
//        mTopics.add(topic);
//
//        Topic topic2 = new Topic();
//        topic2.setTitle("当今的普洱茶的收藏价值怎么样");
//        topic2.setFrom("话题主理人：林林林");
//        topic2.setDesc("近几年的普洱茶越来越被大家所熟知和接受，普洱茶的收藏也被大家纷纷提出，其实普洱茶在很早的时候就已经被各大收藏家收藏，现如今");
//        mTopics.add(topic2);
//
//        Topic topic3 = new Topic();
//        topic3.setTitle("当今的普洱茶的收藏价值怎么样");
//        topic3.setFrom("话题主理人：林林林");
//        topic3.setDesc("近几年的普洱茶越来越被大家所熟知和接受，普洱茶的收藏也被大家纷纷提出，其实普洱茶在很早的时候就已经被各大收藏家收藏，现如今");
//        mTopics.add(topic3);
//
//        ImageUrls image1 = new ImageUrls();
//        image1.setResId(R.drawable.test_gridview_image1);
//        mGvImages.add(image1);
//
//        ImageUrls image2 = new ImageUrls();
//        image2.setResId(R.drawable.test_gridview_image2);
//        mGvImages.add(image2);
//
//        ImageUrls image3 = new ImageUrls();
//        image3.setResId(R.drawable.test_gridview_image3);
//        mGvImages.add(image3);
    }

    private class ListViewAdapter extends CommonAdapter<Topic> {

        public ListViewAdapter(Context context, List<Topic> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(ViewHolder holder, Topic topic) {
            if(holder.getPosition() == 0) {
                holder.getView(R.id.topic_item_top).setVisibility(View.VISIBLE);
            }

            holder.setText(R.id.topic_item_title, topic.getTitle())
                    .setText(R.id.topic_item_info, topic.getFrom());
        }
    }
}
