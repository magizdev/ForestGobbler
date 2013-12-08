package com.magizdev.dayplan.versionone.viewmodel;

import com.magizdev.dayplan.versionone.viewmodel.DayTaskTimeInfo.TimeType;

public class TaskInfo {
	public long BIID;
	public TimeType State;
	
	public TaskInfo(long biid, TimeType state){
		this.BIID = biid;
		this.State = state;
	}
}
