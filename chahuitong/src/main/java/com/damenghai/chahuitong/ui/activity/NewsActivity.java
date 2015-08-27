package com.damenghai.chahuitong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.ui.fragment.NewsFragment;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * Created by Sgun on 15/8/10.
 */
public class NewsActivity extends FragmentActivity {
    /**
     * 茶事
     */
    private static final String EVENT = "89";

    /**
     * 公开课
     */
    private static final String COURSE = "92";

    private static final String[] CONTENT = new String[] {"茶事", "公开课"};

    private Button mBtnHill;
    private TabPageIndicator mIndicator;
    private ViewPager mViewPager;
    private Adapter mAdapter;
    private ArrayList<Fragment> mFragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        findViewById();

        initView();
    }

    private void findViewById() {
        mIndicator = (TabPageIndicator) findViewById(R.id.news_indicator);
        mViewPager = (ViewPager) findViewById(R.id.news_viewpager);
        mBtnHill = (Button) findViewById(R.id.btn_hill);
    }

    private void initView() {
        mAdapter = new Adapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);

        mFragments = new ArrayList<Fragment>();
        mFragments.add(NewsFragment.get(EVENT));
        mFragments.add(NewsFragment.get(COURSE));
        mBtnHill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsActivity.this, ContentActivity.class);
                intent.putExtra("url", "http://www.chahuitong.com/mobile/app/b2b/index.php/News/Index/Home");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
            }
        });
    }

    private class Adapter extends FragmentPagerAdapter {

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position % CONTENT.length);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length];
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }

}
