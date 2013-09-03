package com.magizdev.dayplan.viewmodel;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class StorageUtil<T extends IStoreableItem> {
	private Context context;
	private T data;

	public StorageUtil(Context context, T data) {
		this.context = context;
		this.data = data;
	}

	public List<T> getCollection() {
		ContentResolver cr = context.getContentResolver();
		Uri uri = data.contentUri();
		Cursor cursor = null;
		cursor = cr.query(uri, null, null, null, null);
		List<IStoreableItem> results = data.fromCursor(cursor);
		List<T> typedResult = new ArrayList<T>();
		for (IStoreableItem item : results) {
			typedResult.add((T) item);
		}
		return typedResult;
	}

	public T getSingle(long id) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(data.contentUri(), String.valueOf(id));
		Cursor cursor = null;
		T returnValue = null;
		try {
			cursor = cr.query(uri, null, null, null, null);
			List<T> results = getCollection();
			returnValue = results.get(0);

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return returnValue;
	}

	public long add(T item) {
		ContentValues cv = item.toContentValues();
		ContentResolver cr = context.getContentResolver();
		Uri uri = item.contentUri();
		Uri returned = cr.insert(uri, cv);
		return Long.parseLong(returned.getLastPathSegment());
	}

	public void update(long id, T item) {
		ContentValues cv = item.toContentValues();
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(item.contentUri(), String.valueOf(id));
		cr.update(uri, cv, null, null);
	}

	public void delete(long id) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(data.contentUri(), String.valueOf(id));
		cr.delete(uri, null, null);
	}
}
