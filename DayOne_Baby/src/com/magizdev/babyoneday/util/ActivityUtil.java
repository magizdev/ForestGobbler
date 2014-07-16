package com.magizdev.babyoneday.util;

import java.util.Date;
import java.util.List;

import android.content.Context;

import com.activeandroid.query.Select;
import com.magizdev.babyoneday.viewmodel.ActivityInfo;

public class ActivityUtil {

	public ActivityUtil(Context context) {
	}

	public void StartActivity(long activityTypeId) {
		Date now = new Date();
		ActivityInfo timeInfo = new ActivityInfo(-1, DayUtil.Today(),
				activityTypeId, null, DayUtil.msOfDay(now), 0,
				ActivityInfo.TIME_DURATION, 0, "");
		timeInfo.save();
	}

	public void StopActivity(long activityTypeId) {
		Date now = new Date();
		List<ActivityInfo> activities = new Select()
				.from(ActivityInfo.class)
				.where("type=" + activityTypeId + " and date="
						+ DayUtil.Today()).orderBy("startTime desc").execute();
		ActivityInfo activityInfo = activities.get(0);
		activityInfo.EndTime = DayUtil.msOfDay(now);
		activityInfo.Data = (activityInfo.EndTime - activityInfo.StartTime) / 60000;
		activityInfo.save();
	}

	public boolean IsActivityWaitingForStop(long activityTypeId) {
		List<ActivityInfo> activities = new Select()
				.from(ActivityInfo.class)
				.where("type=" + activityTypeId + " and date="
						+ DayUtil.Today()).orderBy("startTime desc").execute();
		if (activities.size() == 0) {
			return false;
		} else {
			ActivityInfo activityInfo = activities.get(0);
			return activityInfo.timeType == ActivityInfo.TIME_DURATION
					&& activityInfo.EndTime == 0;
		}
	}
}
