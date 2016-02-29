package com.damenghai.chahuitong2.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.damenghai.chahuitong2.R;
import com.damenghai.chahuitong2.ui.fragment.BrandFragment;
import com.damenghai.chahuitong2.ui.fragment.MarketListFragment;
import com.damenghai.chahuitong2.ui.fragment.MarketPersonalFragment;

public class MarketFragmentAdapter extends FragmentPagerAdapter {
	private final String[] TITLES;

	public MarketFragmentAdapter(FragmentManager fm, Context context) {
		super(fm);
        TITLES = context.getResources().getStringArray(R.array.tab_tea_market);
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
            case 0:
                return MarketListFragment.get("1");
            case 1:
                return MarketListFragment.get("2");
            case 2 :
                return BrandFragment.get("" , "quotation");
            case 3:
                return new MarketPersonalFragment();
		}

		return null;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TITLES[position % TITLES.length];
	}

	@Override
	public int getCount() {
		return TITLES.length;
	}

}
