package com.magizdev.dayplan.store;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.magizdev.dayplan.store.BacklogItemMetaData.TaskTableMetaData;

public class BacklogItemProvider extends ContentProvider {
	private static final String TAG = "EasyTask";

	private static HashMap<String, String> sTaskProjectionMap;

	static {
		sTaskProjectionMap = new HashMap<String, String>();
		sTaskProjectionMap.put(TaskTableMetaData._ID, TaskTableMetaData._ID);
		sTaskProjectionMap.put(TaskTableMetaData.TASK_TITLE,
				TaskTableMetaData.TASK_TITLE);
		sTaskProjectionMap.put(TaskTableMetaData.TASK_NOTE,
				TaskTableMetaData.TASK_NOTE);
		sTaskProjectionMap.put(TaskTableMetaData.CREATE_DATE,
				TaskTableMetaData.CREATE_DATE);
		sTaskProjectionMap.put(TaskTableMetaData.START_DATE,
				TaskTableMetaData.START_DATE);
		sTaskProjectionMap.put(TaskTableMetaData.SOURCE,
				TaskTableMetaData.SOURCE);
		sTaskProjectionMap.put(TaskTableMetaData.SOURCE_ID,
				TaskTableMetaData.SOURCE_ID);

	}

	private static final UriMatcher sUriMatcher;
	private static final int INCOMING_TASK_COLLECTION_URI_INDICATOR = 1;
	private static final int INCOMING_SINGLE_TASK_URI_INDICATOR = 2;
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(BacklogItemMetaData.AUTHORITY, "tasks",
				INCOMING_TASK_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(BacklogItemMetaData.AUTHORITY, "tasks/#",
				INCOMING_SINGLE_TASK_URI_INDICATOR);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, BacklogItemMetaData.DATABASE_NAME, null,
					BacklogItemMetaData.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "inner oncreate called");
			db.execSQL("CREATE TABLE "
					+ BacklogItemMetaData.TASK_TABLE_NAME_STRING + "("
					+ BacklogItemMetaData.TaskTableMetaData._ID
					+ " INTEGER PRIMARY KEY,"
					+ BacklogItemMetaData.TaskTableMetaData.TASK_TITLE + " TEXT,"
					+ BacklogItemMetaData.TaskTableMetaData.TASK_NOTE + " TEXT,"
					+ BacklogItemMetaData.TaskTableMetaData.CREATE_DATE
					+ " INTEGER,"
					+ BacklogItemMetaData.TaskTableMetaData.START_DATE
					+ " INTEGER," + BacklogItemMetaData.TaskTableMetaData.SOURCE
					+ " TEXT," + BacklogItemMetaData.TaskTableMetaData.SOURCE_ID
					+ " TEXT);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "inner onupgrade called");
			if (oldVersion == 1 && newVersion == 2) {
				db.execSQL("alter table "
						+ BacklogItemMetaData.TASK_TABLE_NAME_STRING
						+ " add column "
						+ BacklogItemMetaData.TaskTableMetaData.TASK_TITLE
						+ " TEXT");
				db.execSQL("alter table "
						+ BacklogItemMetaData.TASK_TABLE_NAME_STRING
						+ " add column "
						+ BacklogItemMetaData.TaskTableMetaData.SOURCE + " TEXT");
				db.execSQL("alter table "
						+ BacklogItemMetaData.TASK_TABLE_NAME_STRING
						+ " add column "
						+ BacklogItemMetaData.TaskTableMetaData.SOURCE_ID
						+ " TEXT");
				db.execSQL("update " + BacklogItemMetaData.TASK_TABLE_NAME_STRING
						+ " set "
						+ BacklogItemMetaData.TaskTableMetaData.TASK_TITLE + "="
						+ BacklogItemMetaData.TaskTableMetaData.TASK_NOTE);
				db.execSQL("update " + BacklogItemMetaData.TASK_TABLE_NAME_STRING
						+ " set " + BacklogItemMetaData.TaskTableMetaData.SOURCE
						+ "='local'");
			} else {
				db.execSQL("DROP TABLE IF EXISTS "
						+ BacklogItemMetaData.TASK_TABLE_NAME_STRING);
				onCreate(db);
			}
		}
	}

	private DatabaseHelper mOpenHelper;

	@Override
	public boolean onCreate() {
		Log.d(TAG, "main onCreate called");
		mOpenHelper = new DatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (sUriMatcher.match(uri)) {
		case INCOMING_TASK_COLLECTION_URI_INDICATOR:
			qb.setTables(BacklogItemMetaData.TaskTableMetaData.TABLE_NAME);
			qb.setProjectionMap(sTaskProjectionMap);
			break;
		case INCOMING_SINGLE_TASK_URI_INDICATOR:
			qb.setTables(BacklogItemMetaData.TaskTableMetaData.TABLE_NAME);
			qb.setProjectionMap(sTaskProjectionMap);
			qb.appendWhere(TaskTableMetaData._ID + "="
					+ uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = TaskTableMetaData.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor cursor = qb.query(db, projection, selection, selectionArgs,
				null, null, orderBy);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case INCOMING_TASK_COLLECTION_URI_INDICATOR:

			return TaskTableMetaData.CONTENT_TYPE;
		case INCOMING_SINGLE_TASK_URI_INDICATOR:
			return TaskTableMetaData.CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("Unknown Uri " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (sUriMatcher.match(uri) != INCOMING_TASK_COLLECTION_URI_INDICATOR) {
			throw new IllegalArgumentException("Unknown Uri " + uri);
		}

		ContentValues contentValues;
		if (values != null) {
			contentValues = new ContentValues(values);
		} else {
			contentValues = new ContentValues();
		}

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(TaskTableMetaData.TABLE_NAME,
				TaskTableMetaData.TASK_NOTE, contentValues);
		Uri insertedUri = ContentUris.withAppendedId(
				TaskTableMetaData.CONTENT_URI, rowId);
		getContext().getContentResolver().notifyChange(insertedUri, null);
		return insertedUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case INCOMING_TASK_COLLECTION_URI_INDICATOR:
			count = db.delete(TaskTableMetaData.TABLE_NAME, selection,
					selectionArgs);
			break;

		case INCOMING_SINGLE_TASK_URI_INDICATOR:
			String rowId = uri.getPathSegments().get(1);
			count = db.delete(
					TaskTableMetaData.TABLE_NAME,
					TaskTableMetaData._ID
							+ "="
							+ rowId
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case INCOMING_TASK_COLLECTION_URI_INDICATOR:
			count = db.update(TaskTableMetaData.TABLE_NAME, values, selection,
					selectionArgs);
			break;

		case INCOMING_SINGLE_TASK_URI_INDICATOR:
			String rowId = uri.getPathSegments().get(1);
			count = db.update(
					TaskTableMetaData.TABLE_NAME,
					values,
					TaskTableMetaData._ID
							+ "="
							+ rowId
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
