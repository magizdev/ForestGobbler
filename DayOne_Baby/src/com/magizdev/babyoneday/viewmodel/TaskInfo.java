package com.magizdev.babyoneday.viewmodel;

import com.magizdev.babyoneday.viewmodel.DayTaskTimeInfo.TimeType;

public class TaskInfo {
	public long BIID;
	public TimeType State;
	
	public TaskInfo(long biid, TimeType state){
		this.BIID = biid;
		this.State = state;
	}
}
