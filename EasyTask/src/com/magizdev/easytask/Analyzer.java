package com.magizdev.easytask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analyzer {
	static String TIMEPATTERN = "((0?[1-9]|1[012])(:[0-5]\\d)(\\ ?[aApP][mM]))|([01]\\d|2[0-3])(:[0-5]\\d)";
	static String DATEPATTERN = "(0?\\d|1[012])/((0?|[12])\\d|3[01])";

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
		if(dateMatcher.find()){
			SimpleDateFormat format = new SimpleDateFormat("MM/DD");
			try {
				Date time = format.parse(dateMatcher.group());
				dateTime.setDate(time.getDate());
				dateTime.setMonth(time.getMonth());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (hasTime) {
			String timeString = timeMatcher.group();
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			try {
				Date time = format.parse(timeString);
				dateTime.setHours(time.getHours());
				dateTime.setMinutes(time.getMinutes());
				if(timeString.toLowerCase().endsWith("pm")){
					dateTime.setHours(time.getHours() + 12);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return dateTime;
	}
	
	public String getFilteredString(){
		return timeMatcher.replaceFirst("");
	}
}
