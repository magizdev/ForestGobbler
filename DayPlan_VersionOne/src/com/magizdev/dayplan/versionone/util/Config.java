package com.magizdev.dayplan.versionone.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Config {
	static String PROCESSED_DATE = "processed_date";
	Context context;

	public Config(Context context) {
		this.context = context;
	}

	public int getProcessedDate() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getInt(PROCESSED_DATE, 0);
	}

	public void setProcessedDate(int processedDate) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putInt(PROCESSED_DATE, processedDate);
		editor.commit();
	}

}
