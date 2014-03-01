package com.magizdev.babyoneday.util;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Profile {
	private Context context;
	public String name;
	public int gender;
	public float shengao;
	public float tizhong;
	public int birthday;

	public Profile(Context context) {
		this.context = context;
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		name = preferences.getString("name", "");
		gender = preferences.getInt("gender", 0);
		shengao = preferences.getFloat("shengao", 0);
		tizhong = preferences.getFloat("tizhong", 0);
		birthday = preferences.getInt("birthday", DayUtil.toDate(new Date()));
	}

	public void Update() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString("name", this.name);
		editor.putInt("gender", gender);
		editor.putFloat("shengao", shengao);
		editor.putFloat("tizhong", tizhong);
		editor.putInt("birthday", birthday);
		editor.commit();
	}

	public int getDays() {
		return DayUtil.toDate(new Date()) - birthday;
	}
}
