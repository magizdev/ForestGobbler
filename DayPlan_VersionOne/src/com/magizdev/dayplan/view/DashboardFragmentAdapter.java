package com.magizdev.dayplan.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.magizdev.dayplan.util.DayNavigate;
import com.magizdev.dayplan.util.INavigate;
import com.magizdev.dayplan.util.WeekNavigate;

public class DashboardFragmentAdapter extends FragmentStatePagerAdapter {

	private Context context;
	private ViewPager pager;
	private int navigateType;

	public DashboardFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public DashboardFragmentAdapter(FragmentManager fm, Context context,
			ViewPager pager, int navigateType) {
		super(fm);
		this.context = context;
		this.pager = pager;
		this.navigateType = navigateType;
	}

	@Override
	public Fragment getItem(int arg0) {
		Log.w("Adapter", arg0 + "");
		INavigate navigate;
		if (navigateType == 0) {
			navigate = new WeekNavigate(context);
		} else {
			navigate = new DayNavigate(context);
		}
		navigate.SetPostion(arg0 + 1 - getCount());
		DashboardFragment fragment = new DashboardFragment();
		fragment.setPager(pager);
		fragment.setDataSource(navigate);
		return fragment;
	}

	@Override
	public int getCount() {
		if (navigateType == 0) {
			return 8;
		} else {
			return 31;
		}
	}

}
