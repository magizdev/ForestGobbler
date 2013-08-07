package com.magizdev.easytask.viewmodel;

import java.util.Date;

public class EasyTaskInfo {
	public long Id;
	public String Title;
	public String Note;
	public Date CreateDate;
	public Date StartDate;
	public String Source;
	public String SourceId;

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
