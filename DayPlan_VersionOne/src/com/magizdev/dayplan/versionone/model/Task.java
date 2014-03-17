package com.magizdev.dayplan.versionone.model;

import java.util.ArrayList;
import java.util.List;

import com.magizdev.dayplan.versionone.store.IStoreableItem;
import com.magizdev.dayplan.versionone.store.DayPlanMetaData.BacklogItemTable;
import com.magizdev.dayplan.versionone.store.DayPlanMetaData.DayTaskTable;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class Task implements IStoreableItem {
	public enum TaskState {
		Running, Stop
	}

	public long ID;
	public int Date;
	public long BIID;
	public String BIName;
	public float TodayEffort;
	public float RemainEffort;
	public TaskState State;

	public Task() {

	}

	public Task(long id, int date, long biid, String biname,
			TaskState state, float todayEffort, float remainEffort) {
		this.ID = id;
		this.Date = date;
		this.BIID = biid;
		this.BIName = biname;
		this.State = state;
		this.TodayEffort = todayEffort;
		this.RemainEffort = remainEffort;
	}

	@Override
	public ContentValues toContentValues() {
		int state = DayTaskTable.STATE_PAUSEING;
		switch (this.State) {
		case Running:
			state = DayTaskTable.STATE_RUNNING;
			break;
		case Stop:
			state = DayTaskTable.STATE_PAUSEING;
		default:
			break;
		}

		ContentValues cv = new ContentValues();
		cv.put(DayTaskTable.DATE, Date);
		cv.put(DayTaskTable.BIID, BIID);
		cv.put(DayTaskTable.STATE, state);
		cv.put(DayTaskTable.EFFORT, TodayEffort);
		cv.put(DayTaskTable.REMAIN_ESTIMATE, RemainEffort);
		return cv;
	}

	@Override
	public List<IStoreableItem> fromCursor(Cursor cursor) {
		List<IStoreableItem> daytask = new ArrayList<IStoreableItem>();
		try {

			int idxId = cursor.getColumnIndexOrThrow(DayTaskTable._ID);
			int idxDate = cursor.getColumnIndex(DayTaskTable.DATE);
			int idxBIID = cursor.getColumnIndex(DayTaskTable.BIID);
			int idxState = cursor.getColumnIndex(DayTaskTable.STATE);
			int idxBIName = cursor.getColumnIndex(BacklogItemTable.NAME);

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int id = cursor.getInt(idxId);
				int date = cursor.getInt(idxDate);
				long biid = cursor.getLong(idxBIID);
				String biname = cursor.getString(idxBIName);
				int state = cursor.getInt(idxState);
				float effort = cursor.getFloat(cursor
						.getColumnIndex(DayTaskTable.EFFORT));
				float remainEstimate = cursor.getFloat(cursor
						.getColumnIndex(DayTaskTable.REMAIN_ESTIMATE));
				TaskState s = state == DayTaskTable.STATE_RUNNING ? TaskState.Running
						: TaskState.Stop;
				daytask.add(new Task(id, date, biid, biname, s, effort,
						remainEstimate));
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
		return DayTaskTable.CONTENT_URI;
	}

	@Override
	public String[] projection() {
		String[] projection = new String[5];
		projection[0] = DayTaskTable.TABLE_NAME + "." + DayTaskTable._ID;
		projection[1] = DayTaskTable.DATE;
		projection[2] = DayTaskTable.BIID;
		projection[3] = BacklogItemTable.NAME;
		projection[4] = DayTaskTable.STATE;

		return projection;
	}

}
