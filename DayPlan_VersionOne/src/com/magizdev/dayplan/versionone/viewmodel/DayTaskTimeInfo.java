package com.magizdev.dayplan.versionone.viewmodel;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.magizdev.dayplan.versionone.store.DayPlanMetaData.BacklogItemTable;
import com.magizdev.dayplan.versionone.store.DayPlanMetaData.DayTaskTable;
import com.magizdev.dayplan.versionone.store.DayPlanMetaData.DayTaskTimeTable;

public class DayTaskTimeInfo implements IStoreableItem {
	public enum TimeType {
		Start, Stop
	}

	public long ID;
	public int Date;
	public long BIID;
	public String BIName;
	public int Time;
	public TimeType timeType;
	
	public DayTaskTimeInfo(){
		
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
		int timeTypeInt = DayTaskTimeTable.TIME_TYPE_START;
		switch (timeType) {
		case Start:
			timeTypeInt = DayTaskTimeTable.TIME_TYPE_START;
			break;
		case Stop:
			timeTypeInt = DayTaskTimeTable.TIME_TYPE_STOP;
			break;
		default:
			break;
		}

		ContentValues cv = new ContentValues();
		cv.put(DayTaskTable.DATE, Date);
		cv.put(DayTaskTable.BIID, BIID);
		cv.put(DayTaskTimeTable.TIME, Time);
		cv.put(DayTaskTimeTable.TIME_TYPE, timeTypeInt);
		return cv;
	}

	@Override
	public List<IStoreableItem> fromCursor(Cursor cursor) {
		List<IStoreableItem> daytask = new ArrayList<IStoreableItem>();
		try {

			int idxId = cursor.getColumnIndex(DayTaskTable._ID);
			int idxDate = cursor.getColumnIndex(DayTaskTable.DATE);
			int idxBIID = cursor.getColumnIndex(DayTaskTable.BIID);
			int idxTime = cursor.getColumnIndex(DayTaskTimeTable.TIME);
			int idxTimeType = cursor.getColumnIndex(DayTaskTimeTable.TIME_TYPE);
			int idxBIName = cursor.getColumnIndex(BacklogItemTable.NAME);

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int id = cursor.getInt(idxId);
				int date = cursor.getInt(idxDate);
				long biid = cursor.getLong(idxBIID);
				String biname = cursor.getString(idxBIName);
				int time = cursor.getInt(idxTime);
				int timeType = cursor.getInt(idxTimeType);

				TimeType timeType2 = timeType == DayTaskTimeTable.TIME_TYPE_START ? TimeType.Start
						: TimeType.Stop;
				daytask.add(new DayTaskTimeInfo(id, date, biid, biname, time, timeType2));
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
		return DayTaskTimeTable.CONTENT_URI;
	}

	@Override
	public String[] projection() {
		// TODO Auto-generated method stub
		return null;
	}

}
