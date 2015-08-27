package com.damenghai.chahuitong.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.damenghai.chahuitong.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.adapter.GridViewImagesAdapter;
import com.damenghai.chahuitong.bean.ImageUrls;
import com.damenghai.chahuitong.bean.Status;
import com.damenghai.chahuitong.bean.User;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.utils.T;
import com.damenghai.chahuitong.utils.ViewHolder;
import com.damenghai.chahuitong.view.WrapHeightGridView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/8/23.
 */
public class StatusesActivity extends BaseActivity {
    private ListView mLv;

    private ListViewAdapter mAdapter;
    private ArrayList<Status> mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        findViewById();

        initView();
    }
    private void findViewById() {
        mLv = (ListView) findViewById(R.id.statuses_lv);
    }

    private void initView() {
        mStatus = new ArrayList<Status>();
        initDatas();
        mAdapter = new ListViewAdapter(this, mStatus, R.layout.listview_item_status);
        mLv.setAdapter(mAdapter);
    }

    private void initDatas() {
        Status status1 = new Status();
        status1.setCreated_at("刚刚");
        status1.setText("这是两张图片的");
        status1.setSource("捡来的iphone6");
        ArrayList<ImageUrls> images1 = new ArrayList<ImageUrls>();
        ImageUrls image1 = new ImageUrls();
        image1.setResId(R.drawable.test_gridview_image1);
        ImageUrls image2 = new ImageUrls();
        image2.setResId(R.drawable.test_gridview_image2);
        images1.add(image1);
        images1.add(image2);
        status1.setImageUrls(images1);
        User user = new User();
        user.setUsername("user1");
        status1.setUser(user);
        mStatus.add(status1);

        Status status2 = new Status();
        status2.setCreated_at("10分钟前");
        status2.setText("我10分钟前发了一条微博");
        status2.setSource("用肾换来的iphone6");
        ArrayList<ImageUrls> images2 = new ArrayList<ImageUrls>();
        ImageUrls image3 = new ImageUrls();
        image3.setResId(R.drawable.test_gridview_image3);
        ImageUrls image4 = new ImageUrls();
        image4.setResId(R.drawable.test_gridview_image2);
        images2.add(image3);
        images2.add(image4);
        status2.setImageUrls(images2);
        User user2 = new User();
        user2.setUsername("user2");
        status2.setUser(user2);
        mStatus.add(status2);

        Status status3 = new Status();
        status3.setCreated_at("4-03");
        status3.setText("这只有一张大图");
        status3.setSource("iphone100");
        ArrayList<ImageUrls> images3 = new ArrayList<ImageUrls>();
        ImageUrls image5 = new ImageUrls();
        image5.setResId(R.drawable.test_statuses_image);
        images3.add(image5);
        status3.setImageUrls(images3);
        User user3 = new User();
        user3.setUsername("user3");
        status3.setUser(user3);
        mStatus.add(status3);

        Status status4 = new Status();
        status4.setCreated_at("2013-03-3");
        status4.setText("从东京到柏林，从伦敦到纽约，我什么都可以不带，唯一不能少的就是一壶茶");
        status4.setSource("微博客户端");
        User user4 = new User();
        user4.setUsername("user4");
        status4.setUser(user4);
        mStatus.add(status4);
    }

    private class ListViewAdapter extends CommonAdapter<Status> {
        private ArrayList<ImageUrls> mImageUrls;
        private GridViewImagesAdapter mGvAdapter;

        public ListViewAdapter(Context context, List<Status> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(ViewHolder holder, Status status) {
            // 设置微博图片
            FrameLayout includeStatusImage = holder.getView(R.id.status_images);
            WrapHeightGridView gv = (WrapHeightGridView) includeStatusImage.findViewById(R.id.wrap_gridview_images);
            ImageView iv = (ImageView) includeStatusImage.findViewById(R.id.status_iv_item);
            setImage(status, includeStatusImage, gv, iv);

            // 设置信息显示
            holder.setText(R.id.status_text, status.getText())
                    .setText(R.id.status_source, status.getCreated_at() + "  来自" + status.getSource())
                    .setText(R.id.status_name, status.getUser().getUsername() + holder.getPosition());


            LinearLayout statusComment = holder.getView(R.id.control_comment);
            LinearLayout statusShare = holder.getView(R.id.control_share);
            LinearLayout statusLike = holder.getView(R.id.control_like);
            statusShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    T.showShort(StatusesActivity.this, "转发");
                }
            });
            statusComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openActivity(CommentActivity.class);
                }
            });
            statusLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    T.showShort(StatusesActivity.this, "点赞");
                }
            });
        }
    }

    private void setImage(final Status status, FrameLayout imgContainer, GridView gv_images, ImageView iv_image) {
        ArrayList<ImageUrls> picUrls = status.getImageUrls();
        if(picUrls != null && picUrls.size() > 1) {
            imgContainer.setVisibility(View.VISIBLE);
            gv_images.setVisibility(View.VISIBLE);
            iv_image.setVisibility(View.GONE);

            GridViewImagesAdapter mGvAdapter = new GridViewImagesAdapter(this, picUrls, R.layout.gridview_item_image);
            gv_images.setAdapter(mGvAdapter);
        } else if(picUrls != null && picUrls.size() == 1) {
            imgContainer.setVisibility(View.VISIBLE);
            gv_images.setVisibility(View.GONE);
            iv_image.setVisibility(View.VISIBLE);

            iv_image.setImageResource(picUrls.get(0).getResId());
        } else {
            imgContainer.setVisibility(View.GONE);
        }
    }
}
