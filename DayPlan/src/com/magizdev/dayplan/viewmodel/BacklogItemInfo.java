package com.magizdev.dayplan.viewmodel;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.magizdev.dayplan.store.DayPlanMetaData.BacklogItemTable;


public class BacklogItemInfo implements IStoreableItem{
	public long Id;
	public String Name;
	public String Description;
	
	public BacklogItemInfo(){
		
	}

	public BacklogItemInfo(long id, String name, String description) {
		this.Id = id;
		this.Name = name;
		this.Description = description;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(BacklogItemTable.NAME, Name);
		cv.put(BacklogItemTable.DESC, Description);
		return cv;
	}

	@Override
	public List<IStoreableItem> fromCursor(Cursor cursor) {
		List<IStoreableItem> backlogs = new ArrayList<IStoreableItem>();
		try {

			int idxId = cursor
					.getColumnIndex(BacklogItemTable._ID);
			int idxName = cursor
					.getColumnIndex(BacklogItemTable.NAME);
			int idxDesc = cursor
					.getColumnIndex(BacklogItemTable.DESC);

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int id = cursor.getInt(idxId);
				String name = cursor.getString(idxName);
				String desc = cursor.getString(idxDesc);
				backlogs.add(new BacklogItemInfo(id, name, desc));
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
}
