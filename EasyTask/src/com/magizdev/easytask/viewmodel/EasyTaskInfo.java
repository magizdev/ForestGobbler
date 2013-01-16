package com.magizdev.easytask.viewmodel;

import java.util.Date;

public class EasyTaskInfo {
	public int Id;
	public String Note;
	public Date CreateDate;
	public Date StartDate;

	public EasyTaskInfo(int id, String note, Date createDate, Date startDate) {
		this.Id = id;
		this.Note = note;
		this.CreateDate = createDate;
		this.StartDate = startDate;
	}
}
