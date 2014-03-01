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

	public long ID;
	public int Date;
	public long BIID;
	public String BIName;
	public int StartTime;
	public int EndTime;

	public DayTaskTimeInfo() {

	}

	public DayTaskTimeInfo(long id, int date, long biid, String biname,
			int startTime, int endTime) {
		this.ID = id;
		this.Date = date;
		this.BIID = biid;
		this.BIName = biname;
		this.StartTime = startTime;
		this.EndTime = endTime;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(DayTaskTable.DATE, Date);
		cv.put(DayTaskTable.BIID, BIID);
		cv.put(DayTaskTimeTable.START_TIME, StartTime);
		cv.put(DayTaskTimeTable.END_TIME, EndTime);
		return cv;
	}

	@Override
	public List<IStoreableItem> fromCursor(Cursor cursor) {
		List<IStoreableItem> daytask = new ArrayList<IStoreableItem>();
		try {

			int idxId = cursor.getColumnIndex(DayTaskTable._ID);
			int idxDate = cursor.getColumnIndex(DayTaskTable.DATE);
			int idxBIID = cursor.getColumnIndex(DayTaskTable.BIID);
			int idxStartTime = cursor.getColumnIndex(DayTaskTimeTable.START_TIME);
			int idxEndTime = cursor.getColumnIndex(DayTaskTimeTable.END_TIME);
			int idxBIName = cursor.getColumnIndex(BacklogItemTable.NAME);

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int id = cursor.getInt(idxId);
				int date = cursor.getInt(idxDate);
				long biid = cursor.getLong(idxBIID);
				String biname = cursor.getString(idxBIName);
				int startTime = cursor.getInt(idxStartTime);
				int endTime = cursor.getInt(idxEndTime);

				daytask.add(new DayTaskTimeInfo(id, date, biid, biname, startTime,
						endTime));
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
