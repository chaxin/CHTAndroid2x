package com.damenghai.chahuitong2.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.damenghai.chahuitong2.base.BaseActivity;
import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.adapter.ImageBrowserAdapter;
import com.damenghai.chahuitong2.bean.ImageUrls;

import java.util.ArrayList;

/**
 * Created by Sgun on 15/8/28.
 */
public class ImageBrowserActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ImageBrowserAdapter mAdapter;
    private ArrayList<ImageUrls> mPicUrls;
    private int mPosition;

    private ViewPager mViewPager;
    private TextView mTextIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browse);

        mPicUrls = (ArrayList<ImageUrls>) getIntent().getSerializableExtra("pic");
        mPosition = getIntent().getIntExtra("position", 0);

        bindView();

        initView();
    }

    protected void bindView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_image_brower);
        mTextIndex = (TextView) findViewById(R.id.tv_image_index);
    }

    @Override
    protected void initView() {
        mAdapter = new ImageBrowserAdapter(this, mPicUrls);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);

        if(mPicUrls.size() > 1) {
            mViewPager.setCurrentItem(mPosition);
            mTextIndex.setText(mPosition + 1 + "/" + mPicUrls.size());
        } else {
            mTextIndex.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTextIndex.setText(position + 1 + "/" + mPicUrls.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
