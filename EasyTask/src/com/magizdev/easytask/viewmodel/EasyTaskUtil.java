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
		ContentResolver cr = context.getContentResolver();
		Uri uri = EasyTaskMetaData.TaskTableMetaData.CONTENT_URI;
		Cursor cursor = null;
		cursor = cr.query(uri, null, null, null,
				EasyTaskMetaData.TaskTableMetaData.START_DATE);
		List<EasyTaskInfo> tasks = CursorToTasks(cursor);
		return tasks;
	}

	public EasyTaskInfo getTask(long id) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(
				EasyTaskMetaData.TaskTableMetaData.CONTENT_URI,
				String.valueOf(id));
		Cursor cursor = null;
		EasyTaskInfo returnValue = null;
		try {
			cursor = cr.query(uri, null, null, null, null);
			List<EasyTaskInfo> tasks = CursorToTasks(cursor);
			returnValue = tasks.get(0);

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return returnValue;
	}

	public EasyTaskInfo getNextTask() {
		ContentResolver cr = context.getContentResolver();
		Uri uri = EasyTaskMetaData.TaskTableMetaData.CONTENT_URI;

		Cursor cursor = null;
		EasyTaskInfo returnValue = null;
		try {
			cursor = cr.query(uri, null,
					EasyTaskMetaData.TaskTableMetaData.START_DATE + ">?",
					new String[] { Long.toString(System.currentTimeMillis()) },
					EasyTaskMetaData.TaskTableMetaData.START_DATE);
			List<EasyTaskInfo> tasks = CursorToTasks(cursor);
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

	public long addTask(EasyTaskInfo task) {
		ContentValues cv = TaskToCV(task);
		ContentResolver cr = context.getContentResolver();
		Uri uri = EasyTaskMetaData.TaskTableMetaData.CONTENT_URI;
		Uri returned = cr.insert(uri, cv);
		return Long.parseLong(returned.getLastPathSegment());
	}

	public void updateTask(long id, EasyTaskInfo task) {
		ContentValues cv = TaskToCV(task);
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(
				EasyTaskMetaData.TaskTableMetaData.CONTENT_URI,
				String.valueOf(id));
		cr.update(uri, cv, null, null);
	}

	public void deleteTask(long id) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(
				EasyTaskMetaData.TaskTableMetaData.CONTENT_URI,
				String.valueOf(id));
		cr.delete(uri, null, null);
	}

	private ContentValues TaskToCV(EasyTaskInfo task) {
		ContentValues cv = new ContentValues();
		cv.put(TaskTableMetaData.TASK_NOTE, task.Note);
		cv.put(TaskTableMetaData.TASK_TITLE, task.Title);
		cv.put(TaskTableMetaData.CREATE_DATE, task.CreateDate.getTime());
		cv.put(TaskTableMetaData.START_DATE, task.StartDate.getTime());
		cv.put(TaskTableMetaData.SOURCE, task.Source);
		cv.put(TaskTableMetaData.SOURCE_ID, task.SourceId);
		return cv;
	}

	private List<EasyTaskInfo> CursorToTasks(Cursor cursor) {
		List<EasyTaskInfo> tasks = new ArrayList<EasyTaskInfo>();
		try {

			int idxId = cursor
					.getColumnIndex(EasyTaskMetaData.TaskTableMetaData._ID);
			int idxTitle = cursor
					.getColumnIndex(EasyTaskMetaData.TaskTableMetaData.TASK_TITLE);
			int idxNote = cursor
					.getColumnIndex(EasyTaskMetaData.TaskTableMetaData.TASK_NOTE);
			int idxCreateDate = cursor
					.getColumnIndex(EasyTaskMetaData.TaskTableMetaData.CREATE_DATE);
			int idxStartDate = cursor
					.getColumnIndex(EasyTaskMetaData.TaskTableMetaData.START_DATE);
			int idxSource = cursor
					.getColumnIndex(EasyTaskMetaData.TaskTableMetaData.SOURCE);
			int idxSourceId = cursor
					.getColumnIndex(EasyTaskMetaData.TaskTableMetaData.SOURCE_ID);

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int id = cursor.getInt(idxId);
				String title = cursor.getString(idxTitle);
				String note = cursor.getString(idxNote);
				String source = cursor.getString(idxSource);
				String sourceId = cursor.getString(idxSourceId);
				Date createDate = new Date(cursor.getLong(idxCreateDate));
				Date startDate = new Date(cursor.getLong(idxStartDate));
				tasks.add(new EasyTaskInfo(id, title, note, createDate,
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
