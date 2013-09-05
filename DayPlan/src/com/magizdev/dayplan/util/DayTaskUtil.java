package com.magizdev.dayplan.util;

import com.magizdev.dayplan.viewmodel.DayTaskInfo;
import com.magizdev.dayplan.viewmodel.StorageUtil;
import com.magizdev.dayplan.viewmodel.DayTaskInfo.TaskState;

import android.content.Context;

public class DayTaskUtil {
	private StorageUtil<DayTaskInfo> storage;

	public DayTaskUtil(Context context) {
		storage = new StorageUtil<DayTaskInfo>(context, new DayTaskInfo());
	}

	public void AddTask(long backlogId) {
		DayTaskInfo dayTask = new DayTaskInfo(-1, DayUtil.Today(), backlogId,
				null, TaskState.Stop);
		storage.add(dayTask);
	}
}
