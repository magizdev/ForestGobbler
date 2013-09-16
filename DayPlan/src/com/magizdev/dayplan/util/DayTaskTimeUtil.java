package com.magizdev.dayplan.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.magizdev.dayplan.PieChartBuilder;
import com.magizdev.dayplan.PieChartBuilder.PieChartData;
import com.magizdev.dayplan.store.DayPlanMetaData.DayTaskTimeTable;
import com.magizdev.dayplan.viewmodel.DayTaskTimeInfo;
import com.magizdev.dayplan.viewmodel.StorageUtil;

public class DayTaskTimeUtil {
	private StorageUtil<DayTaskTimeInfo> timeStorageUtil;

	public DayTaskTimeUtil(Context context) {
		timeStorageUtil = new StorageUtil<DayTaskTimeInfo>(context,
				new DayTaskTimeInfo());
	}

	public List<DayTaskTimeInfo> GetByDate(int date) {
		return timeStorageUtil.getCollection("(" + DayTaskTimeTable.DATE + "="
				+ date + ")");
	}

	public List<DayTaskTimeInfo> GetByBacklog(long backlogId) {
		return timeStorageUtil.getCollection("(" + DayTaskTimeTable.BIID + "="
				+ backlogId + ")");
	}

	public List<DayTaskTimeInfo> GetByDateRange(int startDate, int endDate) {
		if (startDate > endDate) {
			return new ArrayList<DayTaskTimeInfo>();
		} else {
			return timeStorageUtil.getCollection("(" + DayTaskTimeTable.DATE
					+ ">=" + startDate + " and " + DayTaskTimeTable.DATE + "<"
					+ endDate + ")");
		}
	}

	public static List<PieChartData> compute(List<DayTaskTimeInfo> input) {
		Integer startMark = 0;
		Integer endMark = 1;
		HashMap<Long, HashMap<Integer, Integer>> dayTasksTime = new HashMap<Long, HashMap<Integer, Integer>>();
		HashMap<Long, Integer> dayTasksEffort = new HashMap<Long, Integer>();
		HashMap<Long, String> idToName = new HashMap<Long, String>();
		for (int i = input.size() - 1; i > -1; i--) {

			DayTaskTimeInfo current = input.get(i);

			if (!idToName.containsKey(current.BIID)) {
				idToName.put(current.BIID, current.BIName);
			}

			int span = 0;
			if (dayTasksTime.containsKey(current.BIID)) {
				if (dayTasksTime.get(current.BIID).containsKey(current.Date)) {
					if (dayTasksTime.get(current.BIID).get(current.Date) == 0) {
						dayTasksTime.get(current.BIID).put(current.Date,
								current.Time);
					} else {
						span = current.Time
								- dayTasksTime.get(current.BIID).get(
										current.Date);
						dayTasksTime.get(current.BIID).put(current.Date, 0);
						if (dayTasksEffort.containsKey(current.BIID)) {
							span = span + dayTasksEffort.get(current.BIID);
							dayTasksEffort.put(current.BIID, span);
						} else {
							dayTasksEffort.put(current.BIID, span);
						}
					}
				} else {
					dayTasksTime.get(current.BIID).put(current.Date,
							current.Time);
				}
			} else {
				HashMap<Integer, Integer> first = new HashMap<Integer, Integer>();
				first.put(current.Date, current.Time);
				dayTasksTime.put(current.BIID, first);
			}
		}

		List<PieChartData> result = new ArrayList<PieChartBuilder.PieChartData>();
		for (Long key : idToName.keySet()) {
			result.add(new PieChartData(key, idToName.get(key), dayTasksEffort
					.get(key)));
		}

		return result;
	}
}
