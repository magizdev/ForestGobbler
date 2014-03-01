package com.magizdev.dayplan.versionone.viewmodel;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.magizdev.dayplan.versionone.store.DayPlanMetaData.BacklogItemTable;

public class BacklogItemInfo implements IStoreableItem {
	public long Id;
	public String Name;
	public String Description;
	public boolean Selected;
	public float Estimate;
	public int DueDate;
	public boolean Completed;

	public boolean HasDueDate() {
		return DueDate > 0;
	}
	
	public boolean HasEstimate(){
		return Estimate > 0;
	}

	public BacklogItemInfo() {

	}

	public BacklogItemInfo(long id, String name, String description,
			boolean completed, float estimate, int dueDate) {
		this.Id = id;
		this.Name = name;
		this.Description = description;
		this.Completed = completed;
		this.Estimate = estimate;
		this.DueDate = dueDate;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(BacklogItemTable.NAME, Name);
		cv.put(BacklogItemTable.DESC, Description);
		cv.put(BacklogItemTable.STATE,
				Completed ? BacklogItemTable.STATE_COMPLETE
						: BacklogItemTable.STATE_ACTIVE);
		cv.put(BacklogItemTable.ESTIMATE, Estimate);
		cv.put(BacklogItemTable.DUE_DATE, DueDate);
		return cv;
	}

	@Override
	public List<IStoreableItem> fromCursor(Cursor cursor) {
		List<IStoreableItem> backlogs = new ArrayList<IStoreableItem>();
		try {

			int idxId = cursor.getColumnIndex(BacklogItemTable._ID);
			int idxName = cursor.getColumnIndex(BacklogItemTable.NAME);
			int idxDesc = cursor.getColumnIndex(BacklogItemTable.DESC);
			int idxState = cursor.getColumnIndex(BacklogItemTable.STATE);

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int id = cursor.getInt(idxId);
				String name = cursor.getString(idxName);
				String desc = cursor.getString(idxDesc);
				float estimate = cursor.getFloat(cursor
						.getColumnIndex(BacklogItemTable.ESTIMATE));
				int dueDate = cursor.getInt(cursor
						.getColumnIndex(BacklogItemTable.DUE_DATE));
				boolean completed = cursor.getInt(idxState) == BacklogItemTable.STATE_COMPLETE;
				backlogs.add(new BacklogItemInfo(id, name, desc, completed,
						estimate, dueDate));
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
		return BacklogItemTable.CONTENT_URI;
	}

	@Override
	public String[] projection() {
		return null;
	}
}
