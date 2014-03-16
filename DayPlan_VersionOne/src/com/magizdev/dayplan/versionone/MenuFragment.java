package com.magizdev.dayplan.versionone;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.magizdev.dayplan.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public abstract class MenuFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(layoutResource(), container, false);
		LinearLayout adContainer = (LinearLayout) rootView
				.findViewById(R.id.adContainer);
		AdView adView = new AdView(getActivity(), AdSize.BANNER,
				"a152770fe669b6c");
		adContainer.addView(adView);
		AdRequest adRequest = new AdRequest();
		adView.loadAd(adRequest);
		return rootView;
	}

	public abstract int layoutResource();

	public boolean onCreateOptionsMenu(MenuInflater inflater, Menu menu) {
		if (optionMenuResource() != 0) {
			inflater.inflate(optionMenuResource(), menu);
		} else {
			menu.clear();
		}
		return true;
	}

	public abstract int optionMenuResource();

	public abstract boolean onOptionsItemSelected(MenuItem item);
}
