package com.magizdev.dayplan;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.magizdev.dayplan.util.InAppNavigation;

public abstract class NavigationBaseActivity extends Activity {
	private InAppNavigation navigation;
	private ActionBar actionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		ArrayAdapter<CharSequence> mAdapter = ArrayAdapter.createFromResource(
				this, R.array.Pages,
				android.R.layout.simple_spinner_dropdown_item);

		navigation = new InAppNavigation(this);
		actionBar.setListNavigationCallbacks(mAdapter, navigation);
		navigation.setEnable(false);
	}

	@Override
	public void onResume() {
		super.onResume();
		actionBar.setSelectedNavigationItem(getMyPostion());
		navigation.setEnable(true);
	}
	
	protected abstract int getMyPostion();

}
