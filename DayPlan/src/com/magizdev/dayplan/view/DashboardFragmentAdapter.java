package com.magizdev.dayplan.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.magizdev.dayplan.util.INavigate;

public class DashboardFragmentAdapter extends FragmentStatePagerAdapter {
	private INavigate navigate;

	public DashboardFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public DashboardFragmentAdapter(FragmentManager fm, INavigate navigate) {
		super(fm);
		this.navigate = navigate;
	}

	@Override
	public Fragment getItem(int arg0) {
		navigate.SetPostion(arg0 + 1 - getCount());
		DashboardFragment fragment = new DashboardFragment();
		fragment.setDataSource(navigate);
		return fragment;
	}

	@Override
	public int getCount() {
		return 10;
	}

}
