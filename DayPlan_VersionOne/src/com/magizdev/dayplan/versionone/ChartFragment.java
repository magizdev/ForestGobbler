package com.magizdev.dayplan.versionone;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.versionone.util.WeekNavigate;
import com.magizdev.dayplan.versionone.view.DashboardFragment;

public class ChartFragment extends MenuFragment {

	private ViewPager pager;

	// private DashboardFragmentAdapter pagerAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_dashboard,
				container, false);

		pager = (ViewPager) rootView.findViewById(R.id.pager);
		// LinearLayout adContainer = (LinearLayout) rootView
		// .findViewById(R.id.adContainer);
		//
		// AdView adView = new AdView(getActivity(), AdSize.BANNER,
		// "a152770fe669b6c");
		// adContainer.addView(adView);
		// AdRequest adRequest = new AdRequest();
		// adView.loadAd(adRequest);

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		ArrayAdapter<CharSequence> mAdapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.ReportTypes,
				android.R.layout.simple_spinner_dropdown_item);
		DashboardFragment fragment = new DashboardFragment();
		fragment.setPager(pager);
		fragment.setDataSource(new WeekNavigate(getActivity()));
		getActivity().getFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// actionBar.setListNavigationCallbacks(mAdapter,
		// new OnNavigationListener() {
		//
		// @Override
		// public boolean onNavigationItemSelected(int itemPosition,
		// long itemId) {
		// switch (itemPosition) {
		// case 0:
		// pagerAdapter = new DashboardFragmentAdapter(
		// getActivity().getSupportFragmentManager(),
		// getActivity(), pager, 1);
		// pager.setAdapter(pagerAdapter);
		// pager.setPageTransformer(true,
		// new ZoomOutPageTransformer());
		// pager.setCurrentItem(pagerAdapter.getCount() - 1);
		//
		// return true;
		// case 1:
		// pagerAdapter = new DashboardFragmentAdapter(
		// getActivity().getSupportFragmentManager(),
		// getActivity(), pager, 0);
		// pager.setAdapter(pagerAdapter);
		// pager.setPageTransformer(true,
		// new ZoomOutPageTransformer());
		// pager.setCurrentItem(pagerAdapter.getCount() - 1);
		//
		// return true;
		// default:
		// break;
		// }
		// return false;
		// }
		// });
		//
		// pagerAdapter = new DashboardFragmentAdapter(getActivity()
		// .getSupportFragmentManager(), getActivity(), pager, 1);
		// pager.setAdapter(pagerAdapter);
		// pager.setPageTransformer(true, new ZoomOutPageTransformer());
		// pager.setCurrentItem(pagerAdapter.getCount() - 1);

		return rootView;
	}

	public static class PieChartData {
		public long biid;
		public String backlogName;
		public int data;

		public PieChartData(long biid, String backlogName, int data) {
			this.biid = biid;
			this.backlogName = backlogName;
			this.data = data;
		}
	}

	@Override
	public int optionMenuResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return false;
	}

}
