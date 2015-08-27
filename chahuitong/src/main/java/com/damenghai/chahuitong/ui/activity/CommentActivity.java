package com.damenghai.chahuitong.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;

import com.damenghai.chahuitong.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.bean.Comment;
import com.damenghai.chahuitong.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/8/25.
 */
public class CommentActivity extends BaseActivity {
    private ListView mLv;
    private ArrayList<Comment> mDatas;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        findViewById();

        initView();
    }

    private void findViewById() {
        mLv = (ListView) findViewById(R.id.comment_lv);
    }

    private void initView() {
        mDatas = new ArrayList<Comment>();
        initData();
        mAdapter = new Adapter(this, mDatas, R.layout.listview_item_comment);
        mLv.setAdapter(mAdapter);
    }

    private void initData() {
//        for(int i=0; i<5; i++) {
//            Comment comment = new Comment();
//            comment.setTime("2015-8-5 09:40");
//            comment.setFrom("Jay" + i);
//            comment.setText("茶，属于山茶科，为常绿灌木或小乔木植物，植株高达1-6米，茶树喜欢湿润的气候，在中国长江流域以南地区广泛栽培。茶树叶子制成茶叶，泡水后使用。茶树种植3年就可以采叶子。一般清明前后采摘长出4-5个叶的嫩芽，制作茶叶质量非常好，属于珍品。");
//            ArrayList<Comment> replys = new ArrayList<Comment>();
//            for(int j=0; j<5; j++) {
//                Comment reply = new Comment();
//                reply.setText("这是回复别人的" + j);
//                replys.add(reply);
//            }
//            comment.setReplys(replys);
//            mDatas.add(comment);
//        }
    }

    private class Adapter extends CommonAdapter<Comment> {
        private Context mContext;

        public Adapter(Context context, List<Comment> mDatas, int resId) {
            super(context, mDatas, resId);
            mContext = context;
        }

        @Override
        public void convert(ViewHolder holder, Comment comment) {
            ListView listView = holder.getView(R.id.comment_reply_lv);
            ArrayList<Comment> comments = comment.getReplys();
            ReplyAdapter adapter = new ReplyAdapter(mContext, comments, R.layout.listview_item_reply);
            listView.setAdapter(adapter);

//            holder.setText(R.id.comment_from, comment.getFrom())
//                    .setText(R.id.comment_time, comment.getTime())
//                    .setText(R.id.comment_text, comment.getText());
        }

    }

    private class ReplyAdapter extends CommonAdapter<Comment> {

        public ReplyAdapter(Context context, List<Comment> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(ViewHolder holder, Comment comment) {
            holder.setText(R.id.reply_text, comment.getText());
        }
    }
}
