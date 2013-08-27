package com.magizdev.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class DisplayOnce {
	private static final String FIRST_TIME_OPEN = "first_time_open";
	IDisplayOnce displayOnce;
	SharedPreferences pref;

	public DisplayOnce(Context context, IDisplayOnce displayOnce) {
		pref = PreferenceManager.getDefaultSharedPreferences(context);
		this.displayOnce = displayOnce;
	}

	public void Check() {
		boolean isFirstTime = pref.getBoolean(FIRST_TIME_OPEN, true);
		if(isFirstTime){
			displayOnce.OnFirstTime();
		}
		Editor editor = pref.edit();
		editor.putBoolean(FIRST_TIME_OPEN, false);
		editor.commit();
	}
}
