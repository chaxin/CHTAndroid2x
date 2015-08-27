package com.damenghai.chahuitong.ui.activity;

import android.app.MediaRouteButton;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.damenghai.chahuitong.BaseActivity;
import com.damenghai.chahuitong.BaseFragmentActivity;
import com.damenghai.chahuitong.R;
import com.damenghai.chahuitong.adapter.CommonAdapter;
import com.damenghai.chahuitong.api.HodorAPI;
import com.damenghai.chahuitong.bean.Status;
import com.damenghai.chahuitong.bean.User;
import com.damenghai.chahuitong.config.SessionKeeper;
import com.damenghai.chahuitong.request.VolleyRequest;
import com.damenghai.chahuitong.ui.fragment.CommentFragment;
import com.damenghai.chahuitong.ui.fragment.LeaderFragment;
import com.damenghai.chahuitong.ui.fragment.TopicFragment;
import com.damenghai.chahuitong.utils.L;
import com.damenghai.chahuitong.utils.ViewHolder;
import com.damenghai.chahuitong.view.RoundImageView;
import com.lidroid.xutils.BitmapUtils;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sgun on 15/8/26.
 */
public class CoterieActivity extends BaseFragmentActivity {
    private ViewPager mVp;
    private ViewPagerAdapter mAdapter;
    private TabPageIndicator mIndicator;
    private RoundImageView mIvAvator;

    private User mUser;
    private TextView mTvUsername;
    private Button mBtnLogin;
    private ImageView mIvBack;
    private ImageView mIvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coterie);

        mUser = (User) getIntent().getSerializableExtra("user");

        findViewById();

        initView();

    }

    private void findViewById() {
        mIvBack = (ImageView) findViewById(R.id.personal_back);
        mIvLogout = (ImageView) findViewById(R.id.personal_logout);
        mVp = (ViewPager) findViewById(R.id.coterie_vp);
        mIndicator = (TabPageIndicator) findViewById(R.id.coterie_indicator);
        mIvAvator = (RoundImageView) findViewById(R.id.personal_avator);
        mTvUsername = (TextView) findViewById(R.id.personal_username);
        mBtnLogin = (Button) findViewById(R.id.personal_login);
    }

    private void initView() {
        setUserInfo();

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mIvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionKeeper.writeSession(CoterieActivity.this, "");
                SessionKeeper.writeUsername(CoterieActivity.this, "");
                mIvAvator.setVisibility(View.GONE);
                mTvUsername.setVisibility(View.GONE);
                mBtnLogin.setVisibility(View.VISIBLE);
            }
        });

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mVp.setAdapter(mAdapter);
        mIndicator.setViewPager(mVp);
    }

    private void setUserInfo() {
        BitmapUtils util = new BitmapUtils(this, this.getCacheDir().getAbsolutePath());
        util.configDefaultLoadingImage(R.drawable.default_load_image);
        util.configDefaultLoadFailedImage(R.drawable.default_load_image);
        util.display(mIvAvator, mUser.getAvator());
        mTvUsername.setText(mUser.getUsername());
        mIvAvator.setVisibility(View.VISIBLE);
        mTvUsername.setVisibility(View.VISIBLE);
        mBtnLogin.setVisibility(View.GONE);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = new String[] {"关注", "晒一晒", "活动", "评论"};
        private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments.add(new LeaderFragment());
            mFragments.add(new TopicFragment());
            mFragments.add(new LeaderFragment());
            mFragments.add(new CommentFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }

    private class ListAdapter extends CommonAdapter<Status> {

        public ListAdapter(Context context, List<Status> mDatas, int resId) {
            super(context, mDatas, resId);
        }

        @Override
        public void convert(ViewHolder holder, Status status) {
            holder.setText(R.id.coterie_text, status.getText());
        }
    }
}
