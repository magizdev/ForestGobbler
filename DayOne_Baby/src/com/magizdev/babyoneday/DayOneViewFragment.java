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
	private final static String TAB_RAWDATA = "view_rawdata";
	private final static String TAB_SHENGAO = "view_shengao";
	private final static String TAB_TIZHONG = "view_tizhong";
	private final static String TAB_JIYI = "view_jiyi";
	private TabHost tabHost;
	private View rootView;
	private FragmentManager fragmentManager;
	private Fragment rawDataFragment;
	private Fragment shenGaoFragment;
	private Fragment tiZhongFragment;
	private Fragment shiKeFragment;

	private Fragment getRawDataFragment() {
		if (rawDataFragment == null) {
			rawDataFragment = new RawDataViewFragment();
		}
		return rawDataFragment;
	}

	private Fragment getShenGaoFragment() {
		if (shenGaoFragment == null) {
			shenGaoFragment = new DayOneProfileFragment();
		}
		return shenGaoFragment;
	}

	private Fragment getTiZhongFragment() {
		if (tiZhongFragment == null) {

		}
		return tiZhongFragment;
	}

	private Fragment getShiKeFragment() {
		if (shiKeFragment == null) {

		}
		return shiKeFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_day_view, container,
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
				if (tabId == TAB_RAWDATA) {
					tabHost.setCurrentTab(0);
					updateTab(tabId, R.id.rawDataView);
				} else if (tabId == TAB_SHENGAO) {
					tabHost.setCurrentTab(1);
					updateTab(tabId, R.id.shenGaoView);
				} else if (tabId == TAB_TIZHONG) {
					tabHost.setCurrentTab(2);
					updateTab(tabId, R.id.tiZhongView);
				} else if (tabId == TAB_JIYI) {
					tabHost.setCurrentTab(3);
					updateTab(tabId, R.id.shiKeView);
				}
			}
		});
		tabHost.setCurrentTab(0);
		// manually start loading stuff in the first tab
		updateTab(TAB_RAWDATA, R.id.rawDataView);
	}

	@Override
	public void onResume() {
		// tabHost.setCurrentTab(0);
		// updateTab(TAB_RAWDATA, R.id.rawDataView);
		super.onResume();

	}

	private void setupTabs() {
		tabHost.setup(); // you must call this before adding your tabs!
		tabHost.addTab(newTab(TAB_RAWDATA, R.string.view_rawdata,
				R.id.rawDataView));
		tabHost.addTab(newTab(TAB_SHENGAO, R.string.view_shengao,
				R.id.shenGaoView));
		tabHost.addTab(newTab(TAB_TIZHONG, R.string.view_tizhong,
				R.id.tiZhongView));
		tabHost.addTab(newTab(TAB_JIYI, R.string.view_jiyi, R.id.shiKeView));
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
		case R.id.rawDataView:
			content = getRawDataFragment();
			break;
		case R.id.shenGaoView:
			content = getShenGaoFragment();
			break;
		case R.id.tiZhongView:
			content = getTiZhongFragment();
			break;
		case R.id.shiKeView:
			content = getShiKeFragment();
			break;
		default:
			break;
		}
		fragmentManager.beginTransaction().replace(placeholder, content)
				.commit();

	}
}