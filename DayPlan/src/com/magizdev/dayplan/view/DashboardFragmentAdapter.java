package com.magizdev.dayplan.view;

import com.magizdev.dayplan.util.INavigate;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class DashboardFragmentAdapter extends FragmentStatePagerAdapter {
	private INavigate navigate;
	private ViewPager pager;
	
	public DashboardFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public DashboardFragmentAdapter(FragmentManager fm, INavigate navigate, ViewPager pager) {
		super(fm);
		this.navigate = navigate;
		this.pager = pager;
	}

	@Override
	public Fragment getItem(int arg0) {
		navigate.SetPostion(arg0 + 1 - getCount());
		DashboardFragment fragment = new DashboardFragment();
		fragment.setDataSource(navigate);
		fragment.setPager(pager);
		return fragment;
	}

	@Override
	public int getCount() {
		return 10;
	}

}
