package com.magizdev.easytask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analyzer {
	static String TIMEPATTERN = "((0?[1-9]|1[012])(:[0-5]\\d)(\\ ?[aApP][mM]))|([01]\\d|2[0-3])(:[0-5]\\d)";
	static String DATEPATTERN = "(0?\\d|1[012])/(([12]|0?)\\d|3[01])";

	private boolean hasDate;
	private boolean hasTime;
	private Pattern timePattern;
	private Pattern datePattern;
	private Matcher timeMatcher;
	private Matcher dateMatcher;

	public boolean getHasDate() {
		return hasDate;
	}

	public boolean getHasTime() {
		return hasTime;
	}

	public Analyzer(String input) {
		timePattern = Pattern.compile(TIMEPATTERN);
		datePattern = Pattern.compile(DATEPATTERN);
		timeMatcher = timePattern.matcher(input);
		dateMatcher = datePattern.matcher(input);
		hasTime = timeMatcher.find();
	}

	public Date getDateTime() {
		Date dateTime = new Date(System.currentTimeMillis());
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(dateTime);
		calendar.set(GregorianCalendar.SECOND, 0);
		GregorianCalendar tempCalendar = new GregorianCalendar();
		if (dateMatcher.find()) {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd");
			try {
				Date time = format.parse(dateMatcher.group());
				tempCalendar.setTime(time);
				calendar.set(GregorianCalendar.MONTH,
						tempCalendar.get(GregorianCalendar.MONTH));
				calendar.set(GregorianCalendar.DAY_OF_MONTH,
						tempCalendar.get(GregorianCalendar.DAY_OF_MONTH));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (hasTime) {
			String timeString = timeMatcher.group();
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			try {
				Date time = format.parse(timeString);
				tempCalendar.setTime(time);
				calendar.set(GregorianCalendar.HOUR_OF_DAY,
						tempCalendar.get(GregorianCalendar.HOUR_OF_DAY));
				calendar.set(GregorianCalendar.MINUTE,
						tempCalendar.get(GregorianCalendar.MINUTE));
				calendar.set(GregorianCalendar.SECOND, 0);
				if (timeString.toLowerCase().endsWith("pm")) {
					calendar.add(GregorianCalendar.HOUR_OF_DAY, 12);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return calendar.getTime();
	}

	public String getFilteredString() {
		String noTime= timeMatcher.replaceFirst("");
		Matcher tempMatcher = datePattern.matcher(noTime);
		return tempMatcher.replaceFirst("").trim();
	}
}
