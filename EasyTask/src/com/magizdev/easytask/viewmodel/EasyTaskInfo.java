package com.magizdev.easytask.viewmodel;

import java.util.Date;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "tasks")
public class EasyTaskInfo extends Model {
	public static final int STATUS_DEFAULT = 0;
	public static final int STATUS_COMPLETE = 1;
	public static final int STATUS_CANCEL = 2;

	@Column(name = "title")
	public String Title;

	@Column(name = "voice")
	public String Voice;

	@Column(name = "createDate")
	public Date CreateDate;

	@Column(name = "notifyDate")
	public Date NotifyDate;

	@Column(name = "enableNotify")
	public boolean EnableNotify;

	@Column(name = "status")
	public int Status;
	
	public EasyTaskInfo(){
		super();
	}

	public EasyTaskInfo(String title, String voice, Date createDate,
			Date notifyDate, Boolean enableNotify, int status) {
		super();
		this.Title = title;
		this.Voice = voice;
		this.CreateDate = createDate;
		this.NotifyDate = notifyDate;
		this.EnableNotify = enableNotify;
		this.Status = status;
	}
}
