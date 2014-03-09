   package com.magizdev.dayplan.versionone;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.versionone.util.DayNavigate;
import com.magizdev.dayplan.versionone.util.WeekNavigate;
import com.magizdev.dayplan.versionone.view.DashboardFragment;

public class ReportFragment extends MenuFragment {
	private final static String TAB_DAILY = "view_daily";
	private final static String TAB_WEEKLY = "view_weekly";
	private final static String TAB_BURNDOWN = "view_burndown";
	
	private TabHost tabHost;
	private View rootView;
	private FragmentManager fragmentManager;
	private Fragment dailyReportFragment;
	private Fragment weeklyReportFragment;
	private Fragment burndownFragment;
	private Fragment currentFragment;

	private Fragment getDailyFragment() {
		if (dailyReportFragment == null) {
			DashboardFragment _dailyReportFragment = new DashboardFragment();
			_dailyReportFragment.setDataSource(new DayNavigate(getActivity()));
			dailyReportFragment = _dailyReportFragment;
		}
		return dailyReportFragment;
	}

	private Fragment getWeeklyFragment() {
		if (weeklyReportFragment == null) {
			DashboardFragment _weeklyReportFragment = new DashboardFragment();
			_weeklyReportFragment.setDataSource(new WeekNavigate(getActivity()));
			weeklyReportFragment = _weeklyReportFragment;
		}
		return weeklyReportFragment;
	}

	private Fragment getBurndownFragment() {
		if (burndownFragment == null) {

		}
		return burndownFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragement_report_container, container,
				false);

		tabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
		tabHost.setup();

		FrameLayout contentFrame = (FrameLayout) rootView
				.findViewById(android.R.id.tabcontent);
		fragmentManager = getActivity().getFragmentManager();
		setupTabs();
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				if (tabId == TAB_DAILY) {
					tabHost.setCurrentTab(0);
					updateTab(tabId, R.id.dailyReportView);
				} else if (tabId == TAB_WEEKLY) {
					tabHost.setCurrentTab(1);
					updateTab(tabId, R.id.weeklyReportView);
				} else if (tabId == TAB_BURNDOWN) {
					tabHost.setCurrentTab(2);
					updateTab(tabId, R.id.burndownReportView);
				}
			}
		});
		tabHost.setCurrentTab(0);
		// manually start loading stuff in the first tab
		updateTab(TAB_DAILY, R.id.dailyReportView);
	}

	@Override
	public void onResume() {
		// tabHost.setCurrentTab(0);
		// updateTab(TAB_RAWDATA, R.id.rawDataView);
		super.onResume();

	}

	private void setupTabs() {
		tabHost.setup(); // you must call this before adding your tabs!
		tabHost.addTab(newTab(TAB_DAILY, R.string.tab_label_daily,
				R.id.dailyReportView));
		tabHost.addTab(newTab(TAB_WEEKLY, R.string.tab_label_weekly,
				R.id.weeklyReportView));
		tabHost.addTab(newTab(TAB_BURNDOWN, R.string.tab_label_burndown,
				R.id.burndownReportView));
	}

	private TabSpec newTab(String tag, int labelId, int tabContentId) {
		String label = getActivity().getResources().getString(labelId);
		TabSpec tabSpec = tabHost.newTabSpec(tag);
		tabSpec.setIndicator(label);
		tabSpec.setContent(tabContentId);
		return tabSpec;
	}

	private void updateTab(String tabId, int placeholder) {
		Fragment content = null;
		switch (placeholder) {
		case R.id.dailyReportView:
			content = getDailyFragment();
			break;
		case R.id.weeklyReportView:
			content = getWeeklyFragment();
			break;
		case R.id.burndownReportView:
			content = getBurndownFragment();
			break;
		default:
			break;
		}
		fragmentManager.beginTransaction().replace(placeholder, content)
				.commit();

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