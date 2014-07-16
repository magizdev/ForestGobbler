package com.magizdev.babyoneday.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.activeandroid.query.Select;
import com.magizdev.babyoneday.viewmodel.ActivityInfo;

public class DayTaskTimeUtil {

	public DayTaskTimeUtil(Context context) {
	}

	public List<ActivityInfo> GetByDate(int date) {
		return new Select().from(ActivityInfo.class).where("date=" + date)
				.execute();
	}

	public List<ActivityInfo> GetByBacklog(long backlogId) {
		return new Select().from(ActivityInfo.class).where("type=" + backlogId)
				.execute();
	}

	public List<ActivityInfo> GetByDateRange(int startDate, int endDate) {
		if (startDate > endDate) {
			return new ArrayList<ActivityInfo>();
		} else {
			return new Select().from(ActivityInfo.class)
					.where("date >= " + startDate + " and date<" + endDate)
					.execute();
		}
	}
}
