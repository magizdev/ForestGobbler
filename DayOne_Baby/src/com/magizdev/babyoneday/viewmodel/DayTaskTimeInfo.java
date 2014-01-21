package com.magizdev.babyoneday.viewmodel;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.magizdev.babyoneday.store.DayPlanMetaData.ActivityTypeTable;
import com.magizdev.babyoneday.store.DayPlanMetaData.DayTaskTable;
import com.magizdev.babyoneday.store.DayPlanMetaData.ActivityTable;

public class DayTaskTimeInfo implements IStoreableItem {
	public enum TimeType {
		Start, Stop, Once
	}

	public long ID;
	public int Date;
	public long BIID;
	public String BIName;
	public int Time;
	public TimeType timeType;

	public DayTaskTimeInfo() {

	}

	public DayTaskTimeInfo(long id, int date, long biid, String biname,
			int time, TimeType timeType) {
		this.ID = id;
		this.Date = date;
		this.BIID = biid;
		this.BIName = biname;
		this.Time = time;
		this.timeType = timeType;
	}

	@Override
	public ContentValues toContentValues() {
		int timeTypeInt = ActivityTable.TIME_TYPE_START;
		switch (timeType) {
		case Start:
			timeTypeInt = ActivityTable.TIME_TYPE_START;
			break;
		case Stop:
			timeTypeInt = ActivityTable.TIME_TYPE_STOP;
			break;
		case Once:
			timeTypeInt = ActivityTable.TIME_TYPE_ONCE;
			break;
		default:
			break;
		}

		ContentValues cv = new ContentValues();
		cv.put(DayTaskTable.DATE, Date);
		cv.put(DayTaskTable.BIID, BIID);
		cv.put(ActivityTable.TIME, Time);
		cv.put(ActivityTable.TIME_TYPE, timeTypeInt);
		return cv;
	}

	@Override
	public List<IStoreableItem> fromCursor(Cursor cursor) {
		List<IStoreableItem> daytask = new ArrayList<IStoreableItem>();
		try {

			int idxId = cursor.getColumnIndex(DayTaskTable._ID);
			int idxDate = cursor.getColumnIndex(DayTaskTable.DATE);
			int idxBIID = cursor.getColumnIndex(DayTaskTable.BIID);
			int idxTime = cursor.getColumnIndex(ActivityTable.TIME);
			int idxTimeType = cursor.getColumnIndex(ActivityTable.TIME_TYPE);
			int idxBIName = cursor.getColumnIndex(ActivityTypeTable.NAME);

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int id = cursor.getInt(idxId);
				int date = cursor.getInt(idxDate);
				long biid = cursor.getLong(idxBIID);
				String biname = cursor.getString(idxBIName);
				int time = cursor.getInt(idxTime);
				int timeType = cursor.getInt(idxTimeType);

				TimeType timeType2;
				if (timeType == ActivityTable.TIME_TYPE_START) {
					timeType2 = TimeType.Start;
				} else if (timeType == ActivityTable.TIME_TYPE_STOP) {
					timeType2 = TimeType.Stop;
				} else {
					timeType2 = TimeType.Once;
				}
				daytask.add(new DayTaskTimeInfo(id, date, biid, biname, time,
						timeType2));
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return daytask;
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
