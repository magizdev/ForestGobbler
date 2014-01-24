package com.magizdev.babyoneday.viewmodel;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.magizdev.babyoneday.store.DayPlanMetaData.ActivityTable;
import com.magizdev.babyoneday.store.DayPlanMetaData.ActivityTypeTable;
import com.magizdev.babyoneday.viewmodel.ActivityTypeInfo.TimeType;

public class ActivityInfo implements IStoreableItem {

	public long ID;
	public int Date;
	public long TypeID;
	public String Name;
	public int StartTime;
	public int EndTime;
	public TimeType timeType;
	public float Data;
	public String Note;

	public ActivityInfo() {

	}

	public ActivityInfo(long id, int date, long typeId, String name,
			int startTime, int endTime, TimeType timeType, float data, String note) {
		this.ID = id;
		this.Date = date;
		this.TypeID = typeId;
		this.Name = name;
		this.StartTime = startTime;
		this.EndTime = endTime;
		this.timeType = timeType;
		this.Data = data;
		this.Note = note;
	}

	@Override
	public ContentValues toContentValues() {

		ContentValues cv = new ContentValues();
		cv.put(ActivityTable.DATE, Date);
		cv.put(ActivityTable.ACTIVITY_TYPE_ID, TypeID);
		cv.put(ActivityTable.START_TIME, StartTime);
		cv.put(ActivityTable.END_TIME, EndTime);
		cv.put(ActivityTable.DATA, Data);
		cv.put(ActivityTable.NOTE, Note);
		return cv;
	}

	@Override
	public List<IStoreableItem> fromCursor(Cursor cursor) {
		List<IStoreableItem> activities = new ArrayList<IStoreableItem>();
		try {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				long id = cursor.getLong(cursor
						.getColumnIndex(ActivityTable._ID));
				int date = cursor.getInt(cursor
						.getColumnIndex(ActivityTable.DATE));
				long typeId = cursor.getLong(cursor
						.getColumnIndex(ActivityTable.ACTIVITY_TYPE_ID));
				String name = cursor.getString(cursor
						.getColumnIndex(ActivityTypeTable.NAME));

				TimeType timeType = cursor.getInt(cursor
						.getColumnIndex(ActivityTypeTable.TIME_TYPE)) == ActivityTypeTable.TIME_TYPE_DURATION ? TimeType.Duration
						: TimeType.Once;
				int startTime = cursor.getInt(cursor
						.getColumnIndex(ActivityTable.START_TIME));
				int endTime = cursor.getInt(cursor
						.getColumnIndex(ActivityTable.END_TIME));
				float data = cursor.getFloat(cursor
						.getColumnIndex(ActivityTable.DATA));
				String note = cursor.getString(cursor
						.getColumnIndex(ActivityTable.NOTE));

				activities.add(new ActivityInfo(id, date, typeId, name,
						startTime, endTime, timeType, data, note));
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return activities;
	}

	@Override
	public Uri contentUri() {
		return ActivityTable.CONTENT_URI;
	}

	@Override
	public String[] projection() {
		// TODO Auto-generated method stub
		return null;
	}

}
