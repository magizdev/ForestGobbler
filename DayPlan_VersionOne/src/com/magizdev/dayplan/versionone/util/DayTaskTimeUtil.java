package com.magizdev.dayplan.versionone.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
import android.content.Context;

import com.magizdev.dayplan.versionone.model.ChartData;
import com.magizdev.dayplan.versionone.model.TaskTimeRecord;
import com.magizdev.dayplan.versionone.store.StorageUtil;
import com.magizdev.dayplan.versionone.store.DayPlanMetaData.DayTaskTimeTable;

public class DayTaskTimeUtil {
	private StorageUtil<TaskTimeRecord> timeStorageUtil;

	public DayTaskTimeUtil(Context context) {
		timeStorageUtil = new StorageUtil<TaskTimeRecord>(context,
				new TaskTimeRecord());
	}

	public List<TaskTimeRecord> GetByDate(int date) {
		return timeStorageUtil.getCollection("(" + DayTaskTimeTable.DATE + "="
				+ date + ")", null);
	}

	public int getEffortInMs(int date, long biid) {
		List<TaskTimeRecord> allRecords = timeStorageUtil.getCollection("("
				+ DayTaskTimeTable.DATE + "=" + date + " and "
				+ DayTaskTimeTable.BIID + "=" + biid + ")", null);
		int effort = 0;
		for (TaskTimeRecord record : allRecords) {
			if (record.EndTime > 0) {
				effort += record.EndTime - record.StartTime;
			}
		}
		return effort;
	}

	public List<TaskTimeRecord> GetByBacklog(long backlogId) {
		return timeStorageUtil.getCollection("(" + DayTaskTimeTable.BIID + "="
				+ backlogId + ")", null);
	}

	public List<TaskTimeRecord> GetByDateRange(int startDate, int endDate) {
		if (startDate > endDate) {
			return new ArrayList<TaskTimeRecord>();
		} else {
			return timeStorageUtil.getCollection("(" + DayTaskTimeTable.DATE
					+ ">=" + startDate + " and " + DayTaskTimeTable.DATE + "<"
					+ endDate + ")", null);
		}
	}

	public static List<ChartData> compute(List<TaskTimeRecord> input) {
		HashMap<Long, Integer> dayTasksEffort = new HashMap<Long, Integer>();
		HashMap<Long, String> idToName = new HashMap<Long, String>();
		for (int i = input.size() - 1; i > -1; i--) {

			TaskTimeRecord current = input.get(i);

			if (!idToName.containsKey(current.BIID)) {
				idToName.put(current.BIID, current.BIName);
			}

			int accumulate = 0;
			if (dayTasksEffort.containsKey(current.BIID)) {
				accumulate = dayTasksEffort.get(current.BIID);
			}
			if (current.EndTime > 0) {
				dayTasksEffort.put(current.BIID, accumulate + current.EndTime
						- current.StartTime);
			} else if (current.Date == DayUtil.Today()) {
				dayTasksEffort.put(current.BIID,
						accumulate + DayUtil.msOfDay(new Date())
								- current.StartTime);
			}
		}

		List<ChartData> result = new ArrayList<ChartData>();
		for (Long key : dayTasksEffort.keySet()) {
			result.add(new ChartData(key, idToName.get(key), dayTasksEffort
					.get(key) / 1000 / 60));
		}

		return result;
	}

	public static HashMap<Integer, List<ChartData>> computeBarData(
			List<TaskTimeRecord> input) {
		HashMap<Integer, HashMap<Long, Integer>> dayTasksEffort = new HashMap<Integer, HashMap<Long, Integer>>();
		HashMap<Long, String> idToName = new HashMap<Long, String>();
		for (int i = input.size() - 1; i > -1; i--) {

			TaskTimeRecord current = input.get(i);
			if (current.EndTime == 0) {
				continue;
			}

			if (!idToName.containsKey(current.BIID)) {
				idToName.put(current.BIID, current.BIName);
			}

			if (dayTasksEffort.containsKey(current.Date)) {
				int accumulate = 0;
				if (dayTasksEffort.get(current.Date).containsKey(current.BIID)) {
					accumulate = dayTasksEffort.get(current.Date).get(
							current.BIID);
				}
				dayTasksEffort.get(current.Date).put(current.BIID,
						accumulate + current.EndTime - current.StartTime);

			} else {
				HashMap<Long, Integer> result = new HashMap<Long, Integer>();
				result.put(current.BIID, current.EndTime - current.StartTime);
				dayTasksEffort.put(current.Date, result);
			}

		}

		HashMap<Integer, List<ChartData>> result = new HashMap<Integer, List<ChartData>>();
		for (Integer date : dayTasksEffort.keySet()) {
			List<ChartData> oneDaysRecord = new ArrayList<ChartData>();
			for (Long biid : dayTasksEffort.get(date).keySet()) {
				ChartData oneRecord = new ChartData(biid,
						idToName.get(biid),
						dayTasksEffort.get(date).get(biid) / 1000 / 60);
				oneDaysRecord.add(oneRecord);
			}
			result.put(date, oneDaysRecord);
		}

		return result;
	}
}
