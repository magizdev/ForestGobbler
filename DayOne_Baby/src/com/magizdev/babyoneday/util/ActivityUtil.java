package com.magizdev.babyoneday.util;

import java.util.Date;
import java.util.List;

import android.content.Context;

import com.activeandroid.query.Select;
import com.magizdev.babyoneday.R;
import com.magizdev.babyoneday.viewmodel.ActivityInfo;

public class ActivityUtil {
	Context context;

	public ActivityUtil(Context context) {
		this.context = context;
	}

	public void StartActivity(long activityTypeId) {
		Date now = new Date();
		String activityName = "";

		switch ((int) activityTypeId) {
		case 1:
			activityName = context.getResources().getString(
					R.string.activity_breed);
			break;
		case 2:
			activityName = context.getResources().getString(
					R.string.activity_sleep);
			break;
		case 3:
			activityName = context.getResources().getString(
					R.string.activity_pee);
			break;
		case 4:
			activityName = context.getResources().getString(
					R.string.activity_poo);
			break;
		default:
			break;
		}
		ActivityInfo timeInfo = new ActivityInfo(-1, DayUtil.Today(),
				activityTypeId, activityName, DayUtil.msOfDay(now), 0,
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
