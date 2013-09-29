package com.magizdev.dayplan.store;

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

import com.magizdev.dayplan.store.DayPlanMetaData.BacklogItemTable;
import com.magizdev.dayplan.store.DayPlanMetaData.DayTaskBurndownTable;
import com.magizdev.dayplan.store.DayPlanMetaData.DayTaskTable;
import com.magizdev.dayplan.store.DayPlanMetaData.DayTaskTimeTable;

public class DayPlanProvider extends ContentProvider {

	private static final UriMatcher uriMatcher;
	private static final int BACKLOG_ITEM_COLLECTION_URI_INDICATOR = 1;
	private static final int BACKLOG_ITEM_SINGLE_URI_INDICATOR = 2;
	private static final int DAY_TASK_COLLECTION_URI_INDICATOR = 3;
	private static final int DAY_TASK_SINGLE_URI_INDICATOR = 4;
	private static final int DAY_TASK_TIME_COLLECTION_URI_INDICATOR = 5;
	private static final int DAY_TASK_TIME_SINGLE_URI_INDICATOR = 6;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(DayPlanMetaData.AUTHORITY, "backlogs",
				BACKLOG_ITEM_COLLECTION_URI_INDICATOR);
		uriMatcher.addURI(DayPlanMetaData.AUTHORITY, "backlogs/#",
				BACKLOG_ITEM_SINGLE_URI_INDICATOR);
		uriMatcher.addURI(DayPlanMetaData.AUTHORITY, "daytasks",
				DAY_TASK_COLLECTION_URI_INDICATOR);
		uriMatcher.addURI(DayPlanMetaData.AUTHORITY, "daytasks/#",
				DAY_TASK_SINGLE_URI_INDICATOR);
		uriMatcher.addURI(DayPlanMetaData.AUTHORITY, "daytasktime",
				DAY_TASK_TIME_COLLECTION_URI_INDICATOR);
		uriMatcher.addURI(DayPlanMetaData.AUTHORITY, "daytasktime/#",
				DAY_TASK_TIME_SINGLE_URI_INDICATOR);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, DayPlanMetaData.DATABASE_NAME, null,
					DayPlanMetaData.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			// create backlog item table;
			db.execSQL("CREATE TABLE " + BacklogItemTable.TABLE_NAME + "("
					+ BacklogItemTable._ID + " INTEGER PRIMARY KEY,"
					+ BacklogItemTable.NAME + " TEXT," + BacklogItemTable.DESC
					+ " TEXT," + BacklogItemTable.STATE + " INTEGER,"
					+ BacklogItemTable.ESTIMATE + " INTEGER,"
					+ BacklogItemTable.REMAIN_ESTIMATE + " INTEGER,"
					+ BacklogItemTable.DUE_DATE + " INTEGER);");

			// create day task table;
			db.execSQL("CREATE TABLE " + DayTaskTable.TABLE_NAME + "("
					+ DayTaskTable._ID + " INTEGER PRIMARY KEY,"
					+ DayTaskTable.DATE + " INTEGER," + DayTaskTable.BIID
					+ " INTEGER," + DayTaskTable.STATE + " INTEGER);");

			// create day task time table;
			db.execSQL("CREATE TABLE " + DayTaskTimeTable.TABLE_NAME + "("
					+ DayTaskTimeTable._ID + " INTEGER PRIMARY KEY,"
					+ DayTaskTimeTable.DATE + " INTEGER,"
					+ DayTaskTimeTable.BIID + " INTEGER,"
					+ DayTaskTimeTable.TIME + " INTEGER,"
					+ DayTaskTimeTable.TIME_TYPE + " INTEGER);");
			
			// create day task effort table;
			db.execSQL("CREATE TABLE " + DayTaskBurndownTable.TABLE_NAME + "("
					+ DayTaskBurndownTable._ID + " INTEGER PRIMARY KEY,"
					+ DayTaskBurndownTable.DATE + " INTEGER,"
					+ DayTaskBurndownTable.BIID + " INTEGER,"
					+ DayTaskBurndownTable.EFFORT + " INTEGER);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	private DatabaseHelper dbHelper;

	@Override
	public boolean onCreate() {
		dbHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String tableDefaultSort = "";

		switch (uriMatcher.match(uri)) {
		case BACKLOG_ITEM_COLLECTION_URI_INDICATOR:
			qb.setTables(BacklogItemTable.TABLE_NAME);
			qb.setProjectionMap(BacklogItemTable.projectionMap);
			tableDefaultSort = BacklogItemTable.DEFAULT_SORT_ORDER;
			break;
		case BACKLOG_ITEM_SINGLE_URI_INDICATOR:
			qb.setTables(BacklogItemTable.TABLE_NAME);
			qb.setProjectionMap(BacklogItemTable.projectionMap);
			qb.appendWhere(BacklogItemTable._ID + "="
					+ uri.getPathSegments().get(1));
			tableDefaultSort = BacklogItemTable.DEFAULT_SORT_ORDER;
			break;
		case DAY_TASK_COLLECTION_URI_INDICATOR:
			qb.setTables(DayTaskTable.TABLE_NAME + " join "
					+ BacklogItemTable.TABLE_NAME + " on (" + DayTaskTable.BIID
					+ "=" + BacklogItemTable.TABLE_NAME + "."
					+ BacklogItemTable._ID + ")");
			qb.setProjectionMap(DayTaskTable.projectionMap);
			tableDefaultSort = DayTaskTable.DEFAULT_SORT_ORDER;

			int count = DayTaskTable.projectionMap.size();
			projection = new String[count];
			for (int i = 0; i < count; i++) {
				DayTaskTable.projectionMap.keySet().toArray(projection);
			}

			break;
		case DAY_TASK_SINGLE_URI_INDICATOR:
			qb.setTables(DayTaskTable.TABLE_NAME + " join "
					+ BacklogItemTable.TABLE_NAME + " on (" + DayTaskTable.BIID
					+ "=" + BacklogItemTable.TABLE_NAME + "."
					+ BacklogItemTable._ID + ")");
			qb.setProjectionMap(DayTaskTable.projectionMap);
			qb.appendWhere(DayTaskTable._ID + "="
					+ uri.getPathSegments().get(1));
			tableDefaultSort = DayTaskTable.DEFAULT_SORT_ORDER;
			break;
		case DAY_TASK_TIME_COLLECTION_URI_INDICATOR:
			qb.setTables(DayTaskTimeTable.TABLE_NAME + " join "
					+ BacklogItemTable.TABLE_NAME + " on ("
					+ DayTaskTimeTable.BIID + "=" + BacklogItemTable.TABLE_NAME
					+ "." + BacklogItemTable._ID + ")");
			qb.setProjectionMap(DayTaskTimeTable.projectionMap);
			tableDefaultSort = DayTaskTimeTable.DEFAULT_SORT_ORDER;
			break;
		case DAY_TASK_TIME_SINGLE_URI_INDICATOR:
			qb.setTables(DayTaskTimeTable.TABLE_NAME + " join "
					+ BacklogItemTable.TABLE_NAME + " on ("
					+ DayTaskTimeTable.BIID + "=" + BacklogItemTable.TABLE_NAME
					+ "." + BacklogItemTable._ID + ")");
			qb.setProjectionMap(DayTaskTimeTable.projectionMap);
			qb.appendWhere(DayTaskTimeTable._ID + "="
					+ uri.getPathSegments().get(1));
			tableDefaultSort = DayTaskTimeTable.DEFAULT_SORT_ORDER;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = tableDefaultSort;
		} else {
			orderBy = sortOrder;
		}
		// dbHelper = new DatabaseHelper(getContext());
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = qb.query(db, projection, selection, selectionArgs,
				null, null, orderBy);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case BACKLOG_ITEM_COLLECTION_URI_INDICATOR:
			return BacklogItemTable.CONTENT_TYPE;
		case BACKLOG_ITEM_SINGLE_URI_INDICATOR:
			return BacklogItemTable.CONTENT_ITEM_TYPE;
		case DAY_TASK_COLLECTION_URI_INDICATOR:
			return DayTaskTable.CONTENT_TYPE;
		case DAY_TASK_SINGLE_URI_INDICATOR:
			return DayTaskTable.CONTENT_ITEM_TYPE;
		case DAY_TASK_TIME_COLLECTION_URI_INDICATOR:
			return DayTaskTimeTable.CONTENT_TYPE;
		case DAY_TASK_TIME_SINGLE_URI_INDICATOR:
			return DayTaskTimeTable.CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("Unknown Uri " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = uriMatcher.match(uri);
		if (uriType != BACKLOG_ITEM_COLLECTION_URI_INDICATOR
				&& uriType != DAY_TASK_COLLECTION_URI_INDICATOR
				&& uriType != DAY_TASK_TIME_COLLECTION_URI_INDICATOR) {
			throw new IllegalArgumentException("Unknown Uri " + uri);
		}

		String tableName;
		String nullColumnHack;
		Uri contentUri;

		switch (uriType) {
		case BACKLOG_ITEM_COLLECTION_URI_INDICATOR:
			tableName = BacklogItemTable.TABLE_NAME;
			nullColumnHack = BacklogItemTable.NAME;
			contentUri = BacklogItemTable.CONTENT_URI;
			break;
		case DAY_TASK_COLLECTION_URI_INDICATOR:
			tableName = DayTaskTable.TABLE_NAME;
			nullColumnHack = DayTaskTable.BIID;
			contentUri = DayTaskTable.CONTENT_URI;
			break;
		case DAY_TASK_TIME_COLLECTION_URI_INDICATOR:
			tableName = DayTaskTimeTable.TABLE_NAME;
			nullColumnHack = DayTaskTimeTable.BIID;
			contentUri = DayTaskTimeTable.CONTENT_URI;
			break;

		default:
			throw new IllegalArgumentException("Unknown Uri " + uri);
		}

		ContentValues contentValues;
		if (values != null) {
			contentValues = new ContentValues(values);
		} else {
			contentValues = new ContentValues();
		}

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long rowId = db.insert(tableName, nullColumnHack, contentValues);
		Uri insertedUri = ContentUris.withAppendedId(contentUri, rowId);
		getContext().getContentResolver().notifyChange(insertedUri, null);
		return insertedUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count = 0;
		String tableName;
		String rowId = "-1";
		String whereString;
		switch (uriMatcher.match(uri)) {
		case BACKLOG_ITEM_COLLECTION_URI_INDICATOR:
			tableName = BacklogItemTable.TABLE_NAME;
			break;
		case BACKLOG_ITEM_SINGLE_URI_INDICATOR:
			tableName = BacklogItemTable.TABLE_NAME;
			rowId = uri.getPathSegments().get(1);
			break;
		case DAY_TASK_COLLECTION_URI_INDICATOR:
			tableName = DayTaskTable.TABLE_NAME;
			break;
		case DAY_TASK_SINGLE_URI_INDICATOR:
			tableName = DayTaskTable.TABLE_NAME;
			rowId = uri.getPathSegments().get(1);
			break;
		case DAY_TASK_TIME_COLLECTION_URI_INDICATOR:
			tableName = DayTaskTimeTable.TABLE_NAME;
			break;
		case DAY_TASK_TIME_SINGLE_URI_INDICATOR:
			tableName = DayTaskTimeTable.TABLE_NAME;
			rowId = uri.getPathSegments().get(1);
			break;

		default:
			throw new IllegalArgumentException("Unknown Uri " + uri);
		}

		whereString = (rowId == "-1" ? "" : DayTaskTable._ID + "=" + rowId)
				+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
						: "");

		count = db.delete(tableName, whereString, selectionArgs);

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count = 0;
		String tableName;
		String rowId = "-1";
		String whereString;
		switch (uriMatcher.match(uri)) {
		case BACKLOG_ITEM_COLLECTION_URI_INDICATOR:
			tableName = BacklogItemTable.TABLE_NAME;
			break;
		case BACKLOG_ITEM_SINGLE_URI_INDICATOR:
			tableName = BacklogItemTable.TABLE_NAME;
			rowId = uri.getPathSegments().get(1);
			break;
		case DAY_TASK_COLLECTION_URI_INDICATOR:
			tableName = DayTaskTable.TABLE_NAME;
			break;
		case DAY_TASK_SINGLE_URI_INDICATOR:
			tableName = DayTaskTable.TABLE_NAME;
			rowId = uri.getPathSegments().get(1);
			break;
		case DAY_TASK_TIME_COLLECTION_URI_INDICATOR:
			tableName = DayTaskTimeTable.TABLE_NAME;
			break;
		case DAY_TASK_TIME_SINGLE_URI_INDICATOR:
			tableName = DayTaskTimeTable.TABLE_NAME;
			rowId = uri.getPathSegments().get(1);
			break;

		default:
			throw new IllegalArgumentException("Unknown Uri " + uri);
		}

		whereString = (rowId == "-1" ? "" : DayTaskTable._ID + "=" + rowId)
				+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
						: "");

		count = db.update(tableName, values, whereString, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
