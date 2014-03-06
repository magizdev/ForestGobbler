package com.magizdev.dayplan.versionone;

import android.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public abstract class MenuFragment extends Fragment {

	public boolean onCreateOptionsMenu(MenuInflater inflater, Menu menu) {
		if (optionMenuResource() != 0) {
			inflater.inflate(optionMenuResource(), menu);
		}else {
			menu.clear();
		}
		return true;
	}

	public abstract int optionMenuResource();

	public abstract boolean onOptionsItemSelected(MenuItem item);
}
