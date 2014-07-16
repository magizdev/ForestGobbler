package com.magizdev.babyoneday.viewmodel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name="Activity")
public class ActivityInfo extends Model {
	public static final int AC_BREED=1;
	public static final int AC_SLEEP=2;
	public static final int AC_PEE=3;
	public static final int AC_POO=4;
	
	public static final int TIME_DURATION=1;
	public static final int TIME_ONETIME=2;

	public long ID;
	@Column(name="date")
	public int Date;
	@Column(name="type")
	public long TypeID;
	@Column(name="name")
	public String Name;
	@Column(name="startTime")
	public int StartTime;
	@Column(name="endTime")
	public int EndTime;
	@Column(name="timeType")
	public int timeType;
	public float Data;
	public String Note;

	public ActivityInfo() {

	}

	public ActivityInfo(long id, int date, long typeId, String name,
			int startTime, int endTime, int timeType, float data, String note) {
		this.ID = id;
		this.Date = date;
		this.TypeID = typeId;
		this.Name = name;
		this.StartTime = startTime;
		this.EndTime = endTime;
		this.timeType = timeType;
		this.Data = data;
		this.Note = note;
	}
}
