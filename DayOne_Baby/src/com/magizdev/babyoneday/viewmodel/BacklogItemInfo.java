package com.magizdev.babyoneday.viewmodel;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.magizdev.babyoneday.store.DayPlanMetaData.ActivityTypeTable;

public class BacklogItemInfo implements IStoreableItem {
	public long Id;
	public String Name;
	public String Description;
	public boolean Selected;
	public boolean Completed;

	public BacklogItemInfo() {

	}

	public BacklogItemInfo(long id, String name, String description,
			boolean completed) {
		this.Id = id;
		this.Name = name;
		this.Description = description;
		this.Completed = completed;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(ActivityTypeTable.NAME, Name);
		cv.put(ActivityTypeTable.DESC, Description);
		cv.put(ActivityTypeTable.STATE,
				Completed ? ActivityTypeTable.STATE_COMPLETE
						: ActivityTypeTable.STATE_ACTIVE);
		return cv;
	}

	@Override
	public List<IStoreableItem> fromCursor(Cursor cursor) {
		List<IStoreableItem> backlogs = new ArrayList<IStoreableItem>();
		try {

			int idxId = cursor.getColumnIndex(ActivityTypeTable._ID);
			int idxName = cursor.getColumnIndex(ActivityTypeTable.NAME);
			int idxDesc = cursor.getColumnIndex(ActivityTypeTable.DESC);
			int idxState = cursor.getColumnIndex(ActivityTypeTable.STATE);

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int id = cursor.getInt(idxId);
				String name = cursor.getString(idxName);
				String desc = cursor.getString(idxDesc);
				boolean completed = cursor.getInt(idxState) == ActivityTypeTable.STATE_COMPLETE;
				backlogs.add(new BacklogItemInfo(id, name, desc, completed));
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return backlogs;
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
