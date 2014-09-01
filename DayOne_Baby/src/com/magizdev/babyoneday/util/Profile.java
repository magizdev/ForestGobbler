package com.magizdev.babyoneday.util;

import java.util.Date;

import android.graphics.Bitmap;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "profile")
public class Profile extends Model {
	public static final String NAME = "NAME";
	public static final String GENDER = "GENDER";
	public static final String HEIGHT = "HEIGHT";
	public static final String WEIGHT = "WEIGHT";
	public static final String BIRTHDAY = "BIRTHDAY";
	public static final String AVATAR = "AVATAR";
	public static final int GENDER_BOY = 1;
	public static final int GENDER_GIRL = 2;

	private static Profile instance;

	@Column(name = "name")
	public String name;

	@Column(name = "gender")
	public int gender;

	@Column(name = "height")
	public float height;

	@Column(name = "weight")
	public float weight;

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
