package com.magizdev.dayplan.versionone.store;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public interface IStoreableItem {
	ContentValues toContentValues();
	List<IStoreableItem> fromCursor(Cursor cursor);
	Uri contentUri();
	String[] projection();
}
