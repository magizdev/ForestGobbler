package com.magizdev.babyoneday.util;

import java.util.Date;
import java.util.List;

import android.content.Context;

import com.magizdev.babyoneday.store.DayPlanMetaData.ActivityTable;
import com.magizdev.babyoneday.viewmodel.ActivityInfo;
import com.magizdev.babyoneday.viewmodel.StorageUtil;

public class ActivityUtil {
	private StorageUtil<ActivityInfo> timeStorageUtil;

	public ActivityUtil(Context context) {

		timeStorageUtil = new StorageUtil<ActivityInfo>(context,
				new ActivityInfo());
	}

	public void StartActivity(long activityTypeId) {
		Date now = new Date();
		ActivityInfo timeInfo = new ActivityInfo(
				-1,
				DayUtil.Today(),
				activityTypeId,
				null,
				DayUtil.msOfDay(now),
				0,
				com.magizdev.babyoneday.viewmodel.ActivityTypeInfo.TimeType.Duration,
				0, "");
		timeStorageUtil.add(timeInfo);
	}

	public void StopActivity(long activityTypeId) {
		Date now = new Date();
		List<ActivityInfo> activities = timeStorageUtil.getCollection("("
				+ ActivityTable.DATE + "=" + DayUtil.Today() + " and "
				+ ActivityTable.ACTIVITY_TYPE_ID + "=" + activityTypeId + ")");
		ActivityInfo activityInfo = activities.get(0);
		activityInfo.EndTime = DayUtil.msOfDay(now);
		timeStorageUtil.update(activityInfo.ID, activityInfo);
	}

	public boolean IsActivityWaitingForStop(long activityTypeId) {
		List<ActivityInfo> times = timeStorageUtil.getCollection("("
				+ ActivityTable.DATE + "=" + DayUtil.Today() + " and "
				+ ActivityTable.ACTIVITY_TYPE_ID + "=" + activityTypeId + ")");
		if (times.size() == 0) {
			return false;
		} else {
			ActivityInfo activityInfo = times.get(0);
			return activityInfo.timeType == com.magizdev.babyoneday.viewmodel.ActivityTypeInfo.TimeType.Duration
					&& activityInfo.EndTime == 0;
		}
	}
}
