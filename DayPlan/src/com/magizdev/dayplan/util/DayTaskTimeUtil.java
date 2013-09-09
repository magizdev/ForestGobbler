package com.magizdev.dayplan.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

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
					+ ">=" + startDate + " and " + DayTaskTimeTable.DATE + "<="
					+ endDate + ")");
		}
	}
}
