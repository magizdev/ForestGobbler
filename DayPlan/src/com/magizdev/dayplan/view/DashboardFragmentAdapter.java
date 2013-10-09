package com.magizdev.dayplan.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.magizdev.dayplan.util.INavigate;
import com.magizdev.dayplan.util.WeekNavigate;

public class DashboardFragmentAdapter extends FragmentStatePagerAdapter {
	
	private Context context;

	public DashboardFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public DashboardFragmentAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int arg0) {
		INavigate navigate = new WeekNavigate(context);
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
