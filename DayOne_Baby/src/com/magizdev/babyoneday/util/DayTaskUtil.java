package com.magizdev.babyoneday.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.magizdev.babyoneday.store.DayPlanMetaData.DayTaskTable;
import com.magizdev.babyoneday.store.DayPlanMetaData.ActivityTable;
import com.magizdev.babyoneday.viewmodel.DayTaskInfo;
import com.magizdev.babyoneday.viewmodel.DayTaskTimeInfo;
import com.magizdev.babyoneday.viewmodel.StorageUtil;
import com.magizdev.babyoneday.viewmodel.DayTaskInfo.TaskState;
import com.magizdev.babyoneday.viewmodel.DayTaskTimeInfo.TimeType;

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

	public List<Long> GetTasksByDate(int date) {
		List<DayTaskInfo> tasks = storage.getCollection("(" + DayTaskTable.DATE
				+ "=" + date + ")");
		List<Long> biIds = new ArrayList<Long>();
		for (DayTaskInfo task : tasks) {
			biIds.add(task.BIID);
		}
		return biIds;
	}
	
	public void ClearTasksByDate(int date){
		List<DayTaskInfo> tasks = storage.getCollection("(" + DayTaskTable.DATE
				+ "=" + date + ")");
		for (DayTaskInfo task : tasks) {
			storage.delete(task.ID);
		}
	}

	public void StartTask(long backlogId) {
		Date now = new Date();
		DayTaskTimeInfo timeInfo = new DayTaskTimeInfo(-1, DayUtil.Today(),
				backlogId, null, DayUtil.msOfDay(now), TimeType.Start);
		timeStorageUtil.add(timeInfo);
	}

	public void StopTask(long backlogId) {
		Date now = new Date();
		DayTaskTimeInfo timeInfo = new DayTaskTimeInfo(-1, DayUtil.Today(),
				backlogId, null, DayUtil.msOfDay(now), TimeType.Stop);
		timeStorageUtil.add(timeInfo);
	}
	
	public void TickTask(long backlogId){
		Date now = new Date();
		DayTaskTimeInfo timeInfo = new DayTaskTimeInfo(-1, DayUtil.Today(),
				backlogId, null, DayUtil.msOfDay(now), TimeType.Once);
		timeStorageUtil.add(timeInfo);
	}

	public TimeType GetTaskState(long backlogId) {
		List<DayTaskTimeInfo> times = timeStorageUtil.getCollection("("
				+ ActivityTable.DATE + "=" + DayUtil.Today() + " and "
				+ ActivityTable.BIID + "=" + backlogId + ")");
		if (times.size() == 0) {
			return TimeType.Stop;
		} else {
			DayTaskTimeInfo timeInfo = times.get(0);
			return timeInfo.timeType;
		}
	}
}
