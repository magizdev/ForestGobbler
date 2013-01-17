package com.magizdev.easytask.viewmodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.magizdev.easytask.store.EasyTaskMetaData;
import com.magizdev.easytask.store.EasyTaskMetaData.TaskTableMetaData;

public class EasyTaskUtil {
	private Context context;

	public EasyTaskUtil(Context context) {
		this.context = context;
	}

	public List<EasyTaskInfo> getTasks() {
		List<EasyTaskInfo> tasks = new ArrayList<EasyTaskInfo>();
		ContentResolver cr = context.getContentResolver();
		Uri uri = EasyTaskMetaData.TaskTableMetaData.CONTENT_URI;
		Cursor cursor = null;
		try {
			cursor = cr.query(uri, null, null, null, null);
			int idxId = cursor
					.getColumnIndex(EasyTaskMetaData.TaskTableMetaData._ID);
			int idxNote = cursor
					.getColumnIndex(EasyTaskMetaData.TaskTableMetaData.TASK_NOTE);
			int idxCreateDate = cursor
					.getColumnIndex(EasyTaskMetaData.TaskTableMetaData.CREATE_DATE);
			int idxStartDate = cursor
					.getColumnIndex(EasyTaskMetaData.TaskTableMetaData.START_DATE);

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int id = cursor.getInt(idxId);
				String note = cursor.getString(idxNote);
				Date createDate = new Date(cursor.getLong(idxCreateDate));
				Date startDate = new Date(cursor.getLong(idxStartDate));
				tasks.add(new EasyTaskInfo(id, note, createDate, startDate));
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return tasks;
	}

	public EasyTaskInfo getTask(int id) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(
				EasyTaskMetaData.TaskTableMetaData.CONTENT_URI,
				String.valueOf(id));
		Cursor cursor = null;
		EasyTaskInfo returnValue = null;
		try {
			cursor = cr.query(uri, null, null, null, null);
			int idxId = cursor
					.getColumnIndex(EasyTaskMetaData.TaskTableMetaData._ID);
			int idxNote = cursor
					.getColumnIndex(EasyTaskMetaData.TaskTableMetaData.TASK_NOTE);
			int idxCreateDate = cursor
					.getColumnIndex(EasyTaskMetaData.TaskTableMetaData.CREATE_DATE);
			int idxStartDate = cursor
					.getColumnIndex(EasyTaskMetaData.TaskTableMetaData.START_DATE);

			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				String note = cursor.getString(idxNote);
				Date createDate = new Date(cursor.getLong(idxCreateDate));
				Date startDate = new Date(cursor.getLong(idxStartDate));
				returnValue = new EasyTaskInfo(id, note, createDate, startDate);
			}

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return returnValue;
	}

	public void addTask(EasyTaskInfo task) {
		ContentValues cv = new ContentValues();
		cv.put(TaskTableMetaData.TASK_NOTE, task.Note);
		cv.put(TaskTableMetaData.CREATE_DATE, task.CreateDate.getTime());
		cv.put(TaskTableMetaData.START_DATE, task.StartDate.getTime());
		ContentResolver cr = context.getContentResolver();
		Uri uri = EasyTaskMetaData.TaskTableMetaData.CONTENT_URI;
		cr.insert(uri, cv);
	}

	public void deleteTask(int id) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(
				EasyTaskMetaData.TaskTableMetaData.CONTENT_URI,
				String.valueOf(id));
		cr.delete(uri, null, null);
	}
}
