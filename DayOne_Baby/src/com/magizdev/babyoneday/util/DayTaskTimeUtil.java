package com.magizdev.babyoneday.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.magizdev.babyoneday.store.DayPlanMetaData.ActivityTable;
import com.magizdev.babyoneday.viewmodel.ActivityInfo;
import com.magizdev.babyoneday.viewmodel.StorageUtil;

public class DayTaskTimeUtil {
	private StorageUtil<ActivityInfo> timeStorageUtil;

	public DayTaskTimeUtil(Context context) {
		timeStorageUtil = new StorageUtil<ActivityInfo>(context,
				new ActivityInfo());
	}

	public List<ActivityInfo> GetByDate(int date) {
		return timeStorageUtil.getCollection("(" + ActivityTable.DATE + "="
				+ date + ")");
	}

	public List<ActivityInfo> GetByBacklog(long backlogId) {
		return timeStorageUtil.getCollection("(" + ActivityTable.ACTIVITY_TYPE_ID + "="
				+ backlogId + ")");
	}

	public List<ActivityInfo> GetByDateRange(int startDate, int endDate) {
		if (startDate > endDate) {
			return new ArrayList<ActivityInfo>();
		} else {
			return timeStorageUtil.getCollection("(" + ActivityTable.DATE
					+ ">=" + startDate + " and " + ActivityTable.DATE + "<"
					+ endDate + ")");
		}
	}
}
