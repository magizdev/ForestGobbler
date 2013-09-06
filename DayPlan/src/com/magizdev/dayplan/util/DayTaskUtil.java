package com.magizdev.dayplan.util;

import java.util.Date;
import java.util.List;

import com.magizdev.dayplan.store.DayPlanMetaData.DayTaskTimeTable;
import com.magizdev.dayplan.viewmodel.DayTaskInfo;
import com.magizdev.dayplan.viewmodel.DayTaskTimeInfo;
import com.magizdev.dayplan.viewmodel.DayTaskTimeInfo.TimeType;
import com.magizdev.dayplan.viewmodel.StorageUtil;
import com.magizdev.dayplan.viewmodel.DayTaskInfo.TaskState;

import android.content.Context;

public class DayTaskUtil {
	private StorageUtil<DayTaskInfo> storage;
	private StorageUtil<DayTaskTimeInfo> timeStorageUtil;

	public DayTaskUtil(Context context) {
		storage = new StorageUtil<DayTaskInfo>(context, new DayTaskInfo());
		timeStorageUtil = new StorageUtil<DayTaskTimeInfo>(context,
				new DayTaskTimeInfo());
	}

	public void AddTask(long backlogId) {
		DayTaskInfo dayTask = new DayTaskInfo(-1, DayUtil.Today(), backlogId,
				null, TaskState.Stop);
		storage.add(dayTask);
	}

	public void StartTask(long backlogId) {
		Date now = new Date();
		DayTaskTimeInfo timeInfo = new DayTaskTimeInfo(-1, DayUtil.Today(),
				backlogId, null, now.getTime(), TimeType.Start);
		timeStorageUtil.add(timeInfo);
	}

	public void StopTask(long backlogId) {
		Date now = new Date();
		DayTaskTimeInfo timeInfo = new DayTaskTimeInfo(-1, DayUtil.Today(),
				backlogId, null, now.getTime(), TimeType.Stop);
		timeStorageUtil.add(timeInfo);
	}

	public TimeType GetTaskState(long backlogId) {
		List<DayTaskTimeInfo> times = timeStorageUtil.getCollection("(" + DayTaskTimeTable.DATE + "="
				+ DayUtil.Today() + " and " + DayTaskTimeTable.BIID + "="
				+ backlogId + ")");
		if(times.size() == 0){
			return TimeType.Stop;
		} else {
			DayTaskTimeInfo timeInfo = times.get(0);
			return timeInfo.timeType;
		}
	}
}
