package com.magizdev.babyoneday.viewmodel;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.magizdev.babyoneday.store.DayPlanMetaData.ActivityTypeTable;

public class ActivityTypeInfo implements IStoreableItem {
	public long Id;
	public String Name;
	public TimeType activityTimeType;
	public int RemindDuration;
	public int RemindInterval;
	public boolean RemindDurationEnable;
	public boolean RemindIntervalEnable;

	public ActivityTypeInfo() {

	}

	public static enum TimeType {
		Duration, Once
	}

	public ActivityTypeInfo(long id, String name, TimeType timeType,
			int remindDuration, int remindInterval,
			boolean remindDurationEnable, boolean remindIntervalEnable) {
		this.Id = id;
		this.Name = name;
		this.activityTimeType = timeType;
		this.RemindDuration = remindDuration;
		this.RemindInterval = remindInterval;
		this.RemindDurationEnable = remindDurationEnable;
		this.RemindIntervalEnable = remindIntervalEnable;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(ActivityTypeTable.NAME, Name);
		cv.put(ActivityTypeTable.TIME_TYPE,
				activityTimeType == activityTimeType.Duration ? ActivityTypeTable.TIME_TYPE_DURATION
						: ActivityTypeTable.TIME_TYPE_ONCE);
		cv.put(ActivityTypeTable.REMIND_DURATION, RemindDuration);
		cv.put(ActivityTypeTable.REMIND_INTERVAL, RemindInterval);
		cv.put(ActivityTypeTable.REMIND_DURATION_ENABLE,
				RemindDurationEnable ? 1 : 0);
		cv.put(ActivityTypeTable.REMIND_INTERVAL_ENABLE,
				RemindIntervalEnable ? 1 : 0);

		return cv;
	}

	@Override
	public List<IStoreableItem> fromCursor(Cursor cursor) {
		List<IStoreableItem> activityTypes = new ArrayList<IStoreableItem>();
		try {

			int idxId = cursor.getColumnIndex(ActivityTypeTable._ID);

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int id = cursor.getInt(idxId);
				String name = cursor.getString(cursor
						.getColumnIndex(ActivityTypeTable.NAME));
				TimeType timeType = cursor.getInt(cursor
						.getColumnIndex(ActivityTypeTable.TIME_TYPE)) == ActivityTypeTable.TIME_TYPE_DURATION ? activityTimeType.Duration
						: activityTimeType.Once;
				int remindDuration = cursor.getInt(cursor
						.getColumnIndex(ActivityTypeTable.REMIND_DURATION));
				int remindInterval = cursor.getInt(cursor
						.getColumnIndex(ActivityTypeTable.REMIND_INTERVAL));
				boolean remindDurationEnable = cursor
						.getInt(cursor
								.getColumnIndex(ActivityTypeTable.REMIND_DURATION_ENABLE)) == 1 ? true
						: false;
				boolean remindIntervalEnable = cursor
						.getInt(cursor
								.getColumnIndex(ActivityTypeTable.REMIND_INTERVAL_ENABLE)) == 1 ? true
						: false;
				activityTypes.add(new ActivityTypeInfo(id, name, timeType,
						remindDuration, remindInterval, remindDurationEnable,
						remindIntervalEnable));
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return activityTypes;
	}

	@Override
	public Uri contentUri() {
		return ActivityTypeTable.CONTENT_URI;
	}

	@Override
	public String[] projection() {
		return null;
	}
}
