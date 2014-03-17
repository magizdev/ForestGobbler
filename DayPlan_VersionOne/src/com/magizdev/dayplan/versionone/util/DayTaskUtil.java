package com.magizdev.dayplan.versionone.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.R.bool;
import android.content.Context;

import com.magizdev.dayplan.versionone.model.BacklogItem;
import com.magizdev.dayplan.versionone.model.Task;
import com.magizdev.dayplan.versionone.model.TaskTimeRecord;
import com.magizdev.dayplan.versionone.model.Task.TaskState;
import com.magizdev.dayplan.versionone.store.StorageUtil;
import com.magizdev.dayplan.versionone.store.DayPlanMetaData.DayTaskTable;
import com.magizdev.dayplan.versionone.store.DayPlanMetaData.DayTaskTimeTable;

public class DayTaskUtil {
	private StorageUtil<Task> storage;
	private StorageUtil<TaskTimeRecord> timeStorageUtil;
	private StorageUtil<BacklogItem> backlogStorage;
	private long currentRunningTask;

	public DayTaskUtil(Context context) {
		storage = new StorageUtil<Task>(context, new Task());
		timeStorageUtil = new StorageUtil<TaskTimeRecord>(context,
				new TaskTimeRecord());
		backlogStorage = new StorageUtil<BacklogItem>(context,
				new BacklogItem());
		currentRunningTask = -1;
		List<Long> todaysTasks = GetTasksByDate(DayUtil.Today());
		for (Long id : todaysTasks) {
			if (IsTaskWaitingForStop(id)) {
				currentRunningTask = id;
				break;
			}
		}
	}

	public void AddTask(long backlogId) {
		Task dayTask = new Task(-1, DayUtil.Today(), backlogId,
				null, TaskState.Stop, 0, -1);
		storage.add(dayTask);
	}

	public float GetTaskRemainEstimate(long backlogId, boolean excludToday) {
		if (excludToday) {
			List<Task> tasks = storage.getCollection("("
					+ DayTaskTable.DATE + "<" + DayUtil.Today() + " and "
					+ DayTaskTable.BIID + " = " + backlogId + ")",
					DayTaskTable.DATE + " DESC");
			if (tasks.size() > 0) {
				return tasks.get(0).RemainEffort;
			}

			return backlogStorage.getSingle(backlogId).Estimate;
		} else {
			List<Task> tasks = storage.getCollection("("
					+ DayTaskTable.DATE + "<=" + DayUtil.Today() + " and "
					+ DayTaskTable.BIID + " = " + backlogId + ")",
					DayTaskTable.DATE + " DESC");
			if (tasks.size() > 0 && tasks.get(0).RemainEffort != -1) {
				return tasks.get(0).RemainEffort;
			} else if (tasks.size() > 1 && tasks.get(0).RemainEffort == -1) {
				return tasks.get(1).RemainEffort;
			}

			return backlogStorage.getSingle(backlogId).Estimate;
		}
	}

	public List<Long> GetTasksByDate(int date) {
		List<Task> tasks = storage.getCollection("(" + DayTaskTable.DATE
				+ "=" + date + ")", null);
		List<Long> biIds = new ArrayList<Long>();
		for (Task task : tasks) {
			biIds.add(task.BIID);
		}
		return biIds;
	}

	public void CloseDate(int date) {
		if (date < DayUtil.Today()) {
			List<Long> biids = GetTasksByDate(date);

		}
	}

	public void ClearTasksByDate(int date) {
		List<Task> tasks = storage.getCollection("(" + DayTaskTable.DATE
				+ "=" + date + ")", null);
		for (Task task : tasks) {
			storage.delete(task.ID);
		}
	}

	public void StartTask(long backlogId) {
		if (currentRunningTask > 0) {
			StopTask(currentRunningTask);
		}
		currentRunningTask = backlogId;
		Date now = new Date();
		TaskTimeRecord timeInfo = new TaskTimeRecord(-1, DayUtil.Today(),
				backlogId, null, DayUtil.msOfDay(now), 0);
		timeStorageUtil.add(timeInfo);
	}

	public void StopTask(long backlogId) {
		Date now = new Date();
		List<TaskTimeRecord> times = timeStorageUtil.getCollection("("
				+ DayTaskTimeTable.DATE + "=" + DayUtil.Today() + " and "
				+ DayTaskTimeTable.BIID + "=" + backlogId + ")", null);
		TaskTimeRecord timeInfo = times.get(0);
		timeInfo.EndTime = DayUtil.msOfDay(now);

		timeStorageUtil.update(timeInfo.ID, timeInfo);
	}

	public boolean IsTaskWaitingForStop(long backlogId) {
		List<TaskTimeRecord> times = timeStorageUtil.getCollection("("
				+ DayTaskTimeTable.DATE + "=" + DayUtil.Today() + " and "
				+ DayTaskTimeTable.BIID + "=" + backlogId + ")", null);
		if (times.size() == 0) {
			return false;
		} else {
			TaskTimeRecord timeInfo = times.get(0);
			return timeInfo.EndTime == 0;
		}
	}
}
