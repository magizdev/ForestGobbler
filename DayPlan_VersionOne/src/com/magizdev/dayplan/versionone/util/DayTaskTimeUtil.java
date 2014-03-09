package com.magizdev.dayplan.versionone.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.magizdev.dayplan.versionone.PieChartData;
import com.magizdev.dayplan.versionone.store.DayPlanMetaData.DayTaskTimeTable;
import com.magizdev.dayplan.versionone.viewmodel.DayTaskTimeInfo;
import com.magizdev.dayplan.versionone.viewmodel.StorageUtil;

public class DayTaskTimeUtil {
	private StorageUtil<DayTaskTimeInfo> timeStorageUtil;

	public DayTaskTimeUtil(Context context) {
		timeStorageUtil = new StorageUtil<DayTaskTimeInfo>(context,
				new DayTaskTimeInfo());
	}

	public List<DayTaskTimeInfo> GetByDate(int date) {
		return timeStorageUtil.getCollection("(" + DayTaskTimeTable.DATE + "="
				+ date + ")", null);
	}

	public List<DayTaskTimeInfo> GetByBacklog(long backlogId) {
		return timeStorageUtil.getCollection("(" + DayTaskTimeTable.BIID + "="
				+ backlogId + ")", null);
	}

	public List<DayTaskTimeInfo> GetByDateRange(int startDate, int endDate) {
		if (startDate > endDate) {
			return new ArrayList<DayTaskTimeInfo>();
		} else {
			return timeStorageUtil.getCollection("(" + DayTaskTimeTable.DATE
					+ ">=" + startDate + " and " + DayTaskTimeTable.DATE + "<"
					+ endDate + ")", null);
		}
	}

	public static List<PieChartData> compute(List<DayTaskTimeInfo> input) {
		HashMap<Long, Integer> dayTasksEffort = new HashMap<Long, Integer>();
		HashMap<Long, String> idToName = new HashMap<Long, String>();
		for (int i = input.size() - 1; i > -1; i--) {

			DayTaskTimeInfo current = input.get(i);

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
			}
		}

		List<PieChartData> result = new ArrayList<PieChartData>();
		for (Long key : dayTasksEffort.keySet()) {
			result.add(new PieChartData(key, idToName.get(key), dayTasksEffort
					.get(key) / 1000 / 60));
		}

		return result;
	}

	public static HashMap<Integer, List<PieChartData>> computeBarData(
			List<DayTaskTimeInfo> input) {
		HashMap<Integer, HashMap<Long, Integer>> dayTasksEffort = new HashMap<Integer, HashMap<Long, Integer>>();
		HashMap<Long, String> idToName = new HashMap<Long, String>();
		for (int i = input.size() - 1; i > -1; i--) {

			DayTaskTimeInfo current = input.get(i);
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

		HashMap<Integer, List<PieChartData>> result = new HashMap<Integer, List<PieChartData>>();
		for (Integer date : dayTasksEffort.keySet()) {
			List<PieChartData> oneDaysRecord = new ArrayList<PieChartData>();
			for (Long biid : dayTasksEffort.get(date).keySet()) {
				PieChartData oneRecord = new PieChartData(biid,
						idToName.get(biid),
						dayTasksEffort.get(date).get(biid) / 1000 / 60);
				oneDaysRecord.add(oneRecord);
			}
			result.put(date, oneDaysRecord);
		}

		return result;
	}
}
