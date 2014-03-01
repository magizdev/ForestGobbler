package com.magizdev.dayplan.versionone.util;

import java.util.Calendar;
import java.util.Date;

import android.R.integer;

public class DayUtil {
	public static int toDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR) * 1000
				+ calendar.get(Calendar.DAY_OF_YEAR);
	}

	public static int minOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY) * 60
				+ calendar.get(Calendar.MINUTE);
	}

	public static int msOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000
				+ calendar.get(Calendar.MINUTE) * 60 * 1000
				+ calendar.get(Calendar.SECOND) * 1000
				+ calendar.get(Calendar.MILLISECOND);
	}

	public static int Today() {
		return toDate(new Date());
	}

	public static Calendar toCalendar(int date) {
		Calendar calendar = Calendar.getInstance();
		int year = date / 1000;
		int dayinyear = date % 1000;
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_YEAR, dayinyear);
		return calendar;
	}

	public static String formatCalendar(Calendar calendar) {
		return calendar.get(Calendar.YEAR) + "-"
				+ (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH);
	}

	public static String formatTime(int time) {
		String timeString = "";
		int hour = time / 60;
		int min = time % 60;
		timeString = String.format("%02d", hour) + ":"
				+ String.format("%02d", min);

		return timeString;
	}
}
