package com.magizdev.easytask.viewmodel;

import java.util.Calendar;
import java.util.Date;

public class EasyTaskInfo {
	public long Id;
	public String Title;
	public String Note;
	public Date CreateDate;
	public Date StartDate;
	public String Source;
	public String SourceId;

	public boolean getEnableNotification() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(StartDate);
		int year = calendar.get(Calendar.YEAR);
		return year > 2000;
	}
	
	public void disableNotification(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(StartDate);
		calendar.set(Calendar.YEAR, 1999);
		StartDate = calendar.getTime();
	}

	public EasyTaskInfo(long id, String title, String note, Date createDate,
			Date startDate, String source, String sourceId) {
		this.Id = id;
		this.Title = title;
		this.Note = note;
		this.CreateDate = createDate;
		this.StartDate = startDate;
		this.Source = source;
		this.SourceId = sourceId;
	}
}
