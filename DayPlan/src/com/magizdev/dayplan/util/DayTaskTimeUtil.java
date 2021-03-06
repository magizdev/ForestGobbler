package com.magizdev.dayplan.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

		for (long biid : dayTasksTime.keySet()) {
			for (int dateid : dayTasksTime.get(biid).keySet()) {
				if (dayTasksTime.get(biid).get(dateid) > 0) {
					Date defaultEndTime = new Date();
					if (dateid < DayUtil.Today()) {
						Calendar calendar = DayUtil.toCalendar(DayUtil.Today());
						calendar.set(Calendar.HOUR_OF_DAY, 20);
						calendar.set(Calendar.MINUTE, 0);
						defaultEndTime = calendar.getTime();
					}

					int defaultEndMSOfDay = DayUtil.msOfDay(defaultEndTime);
					int defaultEffortForUnstopTask = defaultEndMSOfDay
							- dayTasksTime.get(biid).get(dateid);
					if (defaultEffortForUnstopTask > 0) {
						int span = dayTasksEffort.get(biid) == null ? 0
								: dayTasksEffort.get(biid);
						dayTasksEffort.put(biid, span
								+ defaultEffortForUnstopTask);
					}
				}

			}
		}

		List<PieChartData> result = new ArrayList<PieChartBuilder.PieChartData>();
		for (Long key : dayTasksEffort.keySet()) {
			result.add(new PieChartData(key, idToName.get(key), dayTasksEffort
					.get(key) / 1000 / 60));
		}

		return result;
	}

	public static HashMap<Integer, List<PieChartData>> computeBarData(
			List<DayTaskTimeInfo> input) {
		HashMap<Integer, HashMap<Long, Integer>> dayTasksTime = new HashMap<Integer, HashMap<Long, Integer>>();
		HashMap<Integer, HashMap<Long, Integer>> dayTasksEffort = new HashMap<Integer, HashMap<Long, Integer>>();
		HashMap<Long, String> idToName = new HashMap<Long, String>();
		for (int i = input.size() - 1; i > -1; i--) {

			DayTaskTimeInfo current = input.get(i);

			if (!idToName.containsKey(current.BIID)) {
				idToName.put(current.BIID, current.BIName);
			}

			int span = 0;
			if (dayTasksTime.containsKey(current.Date)) {
				if (dayTasksTime.get(current.Date).containsKey(current.BIID)) {
					if (dayTasksTime.get(current.Date).get(current.BIID) == 0) {
						dayTasksTime.get(current.Date).put(current.BIID,
								current.Time);
					} else {
						span = current.Time
								- dayTasksTime.get(current.Date).get(
										current.BIID);
						dayTasksTime.get(current.Date).put(current.BIID, 0);
						Integer totalEffort = dayTasksEffort.get(current.Date)
								.get(current.BIID);
						totalEffort += span;
						dayTasksEffort.get(current.Date).put(current.BIID,
								totalEffort);
					}
				} else {
					dayTasksTime.get(current.Date).put(current.BIID,
							current.Time);
					dayTasksEffort.get(current.Date).put(current.BIID, 0);
				}
			} else {
				HashMap<Long, Integer> first = new HashMap<Long, Integer>();
				first.put(current.BIID, current.Time);
				dayTasksTime.put(current.Date, first);

				HashMap<Long, Integer> second = new HashMap<Long, Integer>();
				second.put(current.BIID, 0);
				dayTasksEffort.put(current.Date, second);
			}
		}

		for (int dateid : dayTasksTime.keySet()) {
			for (long biid : dayTasksTime.get(dateid).keySet()) {
				if (dayTasksTime.get(dateid).get(biid) > 0) {
					Date defaultEndTime = new Date();
					if (dateid < DayUtil.Today()) {
						Calendar calendar = DayUtil.toCalendar(DayUtil.Today());
						calendar.set(Calendar.HOUR_OF_DAY, 20);
						calendar.set(Calendar.MINUTE, 0);
						defaultEndTime = calendar.getTime();
					}

					int defaultEndMSOfDay = DayUtil.msOfDay(defaultEndTime);
					int defaultEffortForUnstopTask = defaultEndMSOfDay
							- dayTasksTime.get(dateid).get(biid);
					if (defaultEffortForUnstopTask > 0) {
						int span = dayTasksEffort.get(dateid).get(biid);
						dayTasksEffort.get(dateid).put(biid,
								span + defaultEffortForUnstopTask);
					}
				}

			}
		}

		HashMap<Integer, List<PieChartData>> result = new HashMap<Integer, List<PieChartBuilder.PieChartData>>();
		for (Integer date : dayTasksEffort.keySet()) {
			List<PieChartData> oneDaysRecord = new ArrayList<PieChartBuilder.PieChartData>();
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
