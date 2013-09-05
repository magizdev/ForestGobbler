package com.magizdev.dayplan.util;

import java.util.Calendar;
import java.util.Date;


public class DayUtil {
	public static long Today(){
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		return calendar.get(Calendar.YEAR)*1000 + calendar.get(Calendar.DAY_OF_YEAR);
	}
}
