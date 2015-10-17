package com.damenghai.chahuitong.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.damenghai.chahuitong.R;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * Created by Sgun on 15/9/11.
 */
public class CoterieFragment extends Fragment {
    private ViewPager mVp;
    private TabPageIndicator mIndicator;

    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = View.inflate(getActivity(), R.layout.fragment_coterie, null);

        findViewById();

        initView();

        return mView;
    }

    private void findViewById() {
        mVp = (ViewPager) mView.findViewById(R.id.coterie_vp);
        mIndicator = (TabPageIndicator) mView.findViewById(R.id.coterie_indicator);
    }

    private void initView() {
        ViewPagerAdapter mAdapter = new ViewPagerAdapter(((FragmentActivity) getActivity()).getSupportFragmentManager());
        mVp.setAdapter(mAdapter);
        mIndicator.setViewPager(mVp);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = new String[]{"关注", getActivity().getResources().getString(R.string.timeline), "茶客聚聚", "评论"};
        private ArrayList<android.support.v4.app.Fragment> mFragments = new ArrayList<android.support.v4.app.Fragment>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments.add(new MyFollowFragment());
            mFragments.add(new MyStatusFragment());
            mFragments.add(new MyTravelFragment());
            mFragments.add(new CommentFragment());
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
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

}
