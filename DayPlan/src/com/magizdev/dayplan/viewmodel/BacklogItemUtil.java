package com.magizdev.dayplan.viewmodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.magizdev.dayplan.store.BacklogItemMetaData;
import com.magizdev.dayplan.store.BacklogItemMetaData.TaskTableMetaData;

public class BacklogItemUtil {
	private Context context;

	public BacklogItemUtil(Context context) {
		this.context = context;
	}

	public List<BacklogItemInfo> getTasks() {
		ContentResolver cr = context.getContentResolver();
		Uri uri = BacklogItemMetaData.TaskTableMetaData.CONTENT_URI;
		Cursor cursor = null;
		cursor = cr.query(uri, null, null, null,
				BacklogItemMetaData.TaskTableMetaData.START_DATE);
		List<BacklogItemInfo> tasks = CursorToTasks(cursor);
		return tasks;
	}

	public BacklogItemInfo getTask(long id) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(
				BacklogItemMetaData.TaskTableMetaData.CONTENT_URI,
				String.valueOf(id));
		Cursor cursor = null;
		BacklogItemInfo returnValue = null;
		try {
			cursor = cr.query(uri, null, null, null, null);
			List<BacklogItemInfo> tasks = CursorToTasks(cursor);
			returnValue = tasks.get(0);

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return returnValue;
	}

	public BacklogItemInfo getNextTask() {
		ContentResolver cr = context.getContentResolver();
		Uri uri = BacklogItemMetaData.TaskTableMetaData.CONTENT_URI;

		Cursor cursor = null;
		BacklogItemInfo returnValue = null;
		try {
			cursor = cr.query(uri, null,
					BacklogItemMetaData.TaskTableMetaData.START_DATE + ">?",
					new String[] { Long.toString(System.currentTimeMillis()) },
					BacklogItemMetaData.TaskTableMetaData.START_DATE);
			List<BacklogItemInfo> tasks = CursorToTasks(cursor);
			if (tasks.size() > 0) {
				returnValue = tasks.get(0);
			} else {
				return null;
			}

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return returnValue;
	}

	public long addTask(BacklogItemInfo task) {
		ContentValues cv = TaskToCV(task);
		ContentResolver cr = context.getContentResolver();
		Uri uri = BacklogItemMetaData.TaskTableMetaData.CONTENT_URI;
		Uri returned = cr.insert(uri, cv);
		return Long.parseLong(returned.getLastPathSegment());
	}

	public void updateTask(long id, BacklogItemInfo task) {
		ContentValues cv = TaskToCV(task);
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(
				BacklogItemMetaData.TaskTableMetaData.CONTENT_URI,
				String.valueOf(id));
		cr.update(uri, cv, null, null);
	}

	public void deleteTask(long id) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(
				BacklogItemMetaData.TaskTableMetaData.CONTENT_URI,
				String.valueOf(id));
		cr.delete(uri, null, null);
	}

	private ContentValues TaskToCV(BacklogItemInfo task) {
		ContentValues cv = new ContentValues();
		cv.put(TaskTableMetaData.TASK_NOTE, task.Note);
		cv.put(TaskTableMetaData.TASK_TITLE, task.Title);
		cv.put(TaskTableMetaData.CREATE_DATE, task.CreateDate.getTime());
		cv.put(TaskTableMetaData.START_DATE, task.StartDate.getTime());
		cv.put(TaskTableMetaData.SOURCE, task.Source);
		cv.put(TaskTableMetaData.SOURCE_ID, task.SourceId);
		return cv;
	}

	private List<BacklogItemInfo> CursorToTasks(Cursor cursor) {
		List<BacklogItemInfo> tasks = new ArrayList<BacklogItemInfo>();
		try {

			int idxId = cursor
					.getColumnIndex(BacklogItemMetaData.TaskTableMetaData._ID);
			int idxTitle = cursor
					.getColumnIndex(BacklogItemMetaData.TaskTableMetaData.TASK_TITLE);
			int idxNote = cursor
					.getColumnIndex(BacklogItemMetaData.TaskTableMetaData.TASK_NOTE);
			int idxCreateDate = cursor
					.getColumnIndex(BacklogItemMetaData.TaskTableMetaData.CREATE_DATE);
			int idxStartDate = cursor
					.getColumnIndex(BacklogItemMetaData.TaskTableMetaData.START_DATE);
			int idxSource = cursor
					.getColumnIndex(BacklogItemMetaData.TaskTableMetaData.SOURCE);
			int idxSourceId = cursor
					.getColumnIndex(BacklogItemMetaData.TaskTableMetaData.SOURCE_ID);

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int id = cursor.getInt(idxId);
				String title = cursor.getString(idxTitle);
				String note = cursor.getString(idxNote);
				String source = cursor.getString(idxSource);
				String sourceId = cursor.getString(idxSourceId);
				Date createDate = new Date(cursor.getLong(idxCreateDate));
				Date startDate = new Date(cursor.getLong(idxStartDate));
				tasks.add(new BacklogItemInfo(id, title, note, createDate,
						startDate, source, sourceId));
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return tasks;
	}
}
