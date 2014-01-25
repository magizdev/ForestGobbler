package com.magizdev.babyoneday;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class DayOneViewFragment extends Fragment {
	private final static String STOPWATCH_TAB = "stopwatch";
	private final static String TIMER_TAB = "timer";
	private TabHost tabHost;
	private View rootView;
	private FragmentManager fragmentManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_day_view, container,
				false);

		tabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
		tabHost.setup();

		FrameLayout contentFrame = (FrameLayout) rootView
				.findViewById(android.R.id.tabcontent);
		fragmentManager = getActivity()
				.getFragmentManager();
		//
		// TabSpec timer = tabHost.newTabSpec(TIMER_TAB);
		// timer.
		// Intent timerIntent = new Intent(this, TimerActivity.class);
		// timer.setContent(timerIntent);
		// timer.setIndicator("",
		// getResources().getDrawable(R.drawable.ic_hourglass));
		// tabHost.addTab(timer);
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
				fragmentManager
						.beginTransaction()
						.replace(android.R.id.tabcontent,
								new RawDataViewFragment()).commit();
			}
		});
		tabHost.setCurrentTab(0);
		// manually start loading stuff in the first tab
		updateTab("test", R.id.rawDataView);
	}

	private void setupTabs() {
		tabHost.setup(); // you must call this before adding your tabs!
		tabHost.addTab(newTab("raw_data", R.string.action_cancel,
				R.id.rawDataView));
		tabHost.addTab(newTab("ti_wen", R.string.action_cancel,
				R.id.shenGaoView));
	}

	private TabSpec newTab(String tag, int labelId, int tabContentId) {

		TabSpec tabSpec = tabHost.newTabSpec(tag);
		tabSpec.setIndicator("test");
		tabSpec.setContent(tabContentId);
		return tabSpec;
	}

	private void updateTab(String tabId, int placeholder) {
		FragmentManager fm = getFragmentManager();
		if (fm.findFragmentByTag(tabId) == null) {
			fm.beginTransaction()
					.replace(placeholder, new RawDataViewFragment(), tabId)
					.commit();
		}
	}
}