package com.magizdev.babyoneday.util;

import java.util.Date;

import android.graphics.Bitmap;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "profile")
public class Profile extends Model {

	private static Profile instance;

	@Column(name = "name")
	public String name;

	@Column(name = "gender")
	public int gender;

	@Column(name = "shengao")
	public float shengao;

	@Column(name = "tizhong")
	public float tizhong;

	@Column(name = "birthday")
	public int birthday;

	@Column(name = "avatar")
	public Bitmap pic;

	public Profile() {

	}

	public static Profile Instance() {
		if (instance == null) {
			instance = new Select().from(Profile.class).executeSingle();
		}
		return instance;
	}

	public int getDays() {
		return DayUtil.toDate(new Date()) - birthday;
	}
}
