package com.damenghai.chahuitong.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.damenghai.chahuitong.base.BaseActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.ImageBrowserAdapter;
import com.damenghai.chahuitong.bean.ImageUrls;
import com.damenghai.chahuitong.bean.Status;

import java.util.ArrayList;

/**
 * Created by Sgun on 15/8/28.
 */
public class ImageBrowserActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ImageBrowserAdapter mAdapter;
    private ArrayList<ImageUrls> mPicUrls;
    private Status mStatus;
    private int mPosition;

    private ViewPager mViewPager;
    private TextView mTextIndex;
    private Button mBtnSave;
    private Button mBtnOriginalImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browse);

        mPicUrls = (ArrayList<ImageUrls>) getIntent().getSerializableExtra("pic");
        mPosition = getIntent().getIntExtra("position", 0);

        findViewById();

        initViwe();
    }

    private void findViewById() {
        mViewPager = (ViewPager) findViewById(R.id.vp_image_brower);
        mTextIndex = (TextView) findViewById(R.id.tv_image_index);
        mBtnSave = (Button) findViewById(R.id.btn_save);
        mBtnOriginalImage = (Button) findViewById(R.id.btn_original_image);
    }

    private void initViwe() {
        mAdapter = new ImageBrowserAdapter(this, mPicUrls);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);

        mBtnSave.setOnClickListener(this);
        mBtnOriginalImage.setOnClickListener(this);

        if(mPicUrls.size() > 1) {
            mViewPager.setCurrentItem(mPosition);
            mTextIndex.setText(mPosition + 1 + "/" + mPicUrls.size());
        } else {
            mTextIndex.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

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
}
