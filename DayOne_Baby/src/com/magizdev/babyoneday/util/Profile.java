package com.magizdev.babyoneday.util;

import java.util.Date;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

@Table(name="profile")
public class Profile extends Model {
	private Context context;
	
	@Column(name="name")
	public String name;
	
	@Column(name="gender")
	public int gender;
	
	@Column(name="shengao")
	public float shengao;
	
	@Column(name="tizhong")
	public float tizhong;
	
	@Column(name="birthday")
	public int birthday;
	
	@Column(name="avatar")
	public Bitmap pic;
	
	public Profile(){
		
	}

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
