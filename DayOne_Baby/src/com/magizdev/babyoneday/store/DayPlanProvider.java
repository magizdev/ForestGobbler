package com.magizdev.babyoneday.store;

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

import com.magizdev.babyoneday.R;
import com.magizdev.babyoneday.store.DayPlanMetaData.ActivityTable;
import com.magizdev.babyoneday.store.DayPlanMetaData.ActivityTypeTable;

public class DayPlanProvider extends ContentProvider {

	private static final UriMatcher uriMatcher;
	private static final int ACTIVITY_TYPE_COLLECTION_URI_INDICATOR = 1;
	private static final int ACTIVITY_TYPE_SINGLE_URI_INDICATOR = 2;
	private static final int ACTIVITY_COLLECTION_URI_INDICATOR = 3;
	private static final int ACTIVITY_SINGLE_URI_INDICATOR = 4;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(DayPlanMetaData.AUTHORITY, "activitytypes",
				ACTIVITY_TYPE_COLLECTION_URI_INDICATOR);
		uriMatcher.addURI(DayPlanMetaData.AUTHORITY, "activitytypes/#",
				ACTIVITY_TYPE_SINGLE_URI_INDICATOR);
		uriMatcher.addURI(DayPlanMetaData.AUTHORITY, "activities",
				ACTIVITY_COLLECTION_URI_INDICATOR);
		uriMatcher.addURI(DayPlanMetaData.AUTHORITY, "activities/#",
				ACTIVITY_SINGLE_URI_INDICATOR);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		private Context context;

		public DatabaseHelper(Context context) {
			super(context, DayPlanMetaData.DATABASE_NAME, null,
					DayPlanMetaData.DATABASE_VERSION);
			this.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// create activity type table;
			db.execSQL("CREATE TABLE " + ActivityTypeTable.TABLE_NAME + "("
					+ ActivityTypeTable._ID + " INTEGER PRIMARY KEY,"
					+ ActivityTypeTable.NAME + " TEXT,"
					+ ActivityTypeTable.TIME_TYPE + " INTEGER,"
					+ ActivityTypeTable.REMIND_DURATION + " INTEGER default 0,"
					+ ActivityTypeTable.REMIND_INTERVAL + " INTEGER default 0)");

			// create activity table;
			db.execSQL("CREATE TABLE " + ActivityTable.TABLE_NAME + "("
					+ ActivityTable._ID + " INTEGER PRIMARY KEY,"
					+ ActivityTable.DATE + " INTEGER,"
					+ ActivityTable.ACTIVITY_TYPE_ID + " INTEGER,"
					+ ActivityTable.START_TIME + " INTEGER,"
					+ ActivityTable.NOTE + " TEXT," + ActivityTable.DATA
					+ " INTEGER," + ActivityTable.END_TIME + " INTEGER);");

			db.execSQL("insert into " + ActivityTypeTable.TABLE_NAME + "("
					+ ActivityTypeTable.NAME + ") values ('"
					+ context.getResources().getString(R.string.weinai) + "');");

			db.execSQL("insert into " + ActivityTypeTable.TABLE_NAME + "("
					+ ActivityTypeTable.NAME + ") values ('"
					+ context.getResources().getString(R.string.shuijiao)
					+ "');");

			db.execSQL("insert into " + ActivityTypeTable.TABLE_NAME + "("
					+ ActivityTypeTable.NAME + ") values ('"
					+ context.getResources().getString(R.string.xiaobian)
					+ "');");

			db.execSQL("insert into " + ActivityTypeTable.TABLE_NAME + "("
					+ ActivityTypeTable.NAME + ") values ('"
					+ context.getResources().getString(R.string.dabian) + "');");

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
		case ACTIVITY_TYPE_COLLECTION_URI_INDICATOR:
			qb.setTables(ActivityTypeTable.TABLE_NAME);
			qb.setProjectionMap(ActivityTypeTable.projectionMap);
			tableDefaultSort = ActivityTypeTable.DEFAULT_SORT_ORDER;
			break;
		case ACTIVITY_TYPE_SINGLE_URI_INDICATOR:
			qb.setTables(ActivityTypeTable.TABLE_NAME);
			qb.setProjectionMap(ActivityTypeTable.projectionMap);
			qb.appendWhere(ActivityTypeTable._ID + "="
					+ uri.getPathSegments().get(1));
			tableDefaultSort = ActivityTypeTable.DEFAULT_SORT_ORDER;
			break;
		case ACTIVITY_COLLECTION_URI_INDICATOR:
			qb.setTables(ActivityTable.TABLE_NAME + " join "
					+ ActivityTypeTable.TABLE_NAME + " on ("
					+ ActivityTable.ACTIVITY_TYPE_ID + "="
					+ ActivityTypeTable.TABLE_NAME + "."
					+ ActivityTypeTable._ID + ")");
			qb.setProjectionMap(ActivityTable.projectionMap);
			tableDefaultSort = ActivityTable.DEFAULT_SORT_ORDER;
			break;
		case ACTIVITY_SINGLE_URI_INDICATOR:
			qb.setTables(ActivityTable.TABLE_NAME + " join "
					+ ActivityTypeTable.TABLE_NAME + " on ("
					+ ActivityTable.ACTIVITY_TYPE_ID + "="
					+ ActivityTypeTable.TABLE_NAME + "."
					+ ActivityTypeTable._ID + ")");
			qb.setProjectionMap(ActivityTable.projectionMap);
			qb.appendWhere(ActivityTable._ID + "="
					+ uri.getPathSegments().get(1));
			tableDefaultSort = ActivityTable.DEFAULT_SORT_ORDER;
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
		case ACTIVITY_TYPE_COLLECTION_URI_INDICATOR:
			return ActivityTypeTable.CONTENT_TYPE;
		case ACTIVITY_TYPE_SINGLE_URI_INDICATOR:
			return ActivityTypeTable.CONTENT_ITEM_TYPE;
		case ACTIVITY_COLLECTION_URI_INDICATOR:
			return ActivityTable.CONTENT_TYPE;
		case ACTIVITY_SINGLE_URI_INDICATOR:
			return ActivityTable.CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("Unknown Uri " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = uriMatcher.match(uri);
		if (uriType != ACTIVITY_TYPE_COLLECTION_URI_INDICATOR
				&& uriType != ACTIVITY_COLLECTION_URI_INDICATOR) {
			throw new IllegalArgumentException("Unknown Uri " + uri);
		}

		String tableName;
		String nullColumnHack;
		Uri contentUri;

		switch (uriType) {
		case ACTIVITY_TYPE_COLLECTION_URI_INDICATOR:
			tableName = ActivityTypeTable.TABLE_NAME;
			nullColumnHack = ActivityTypeTable.NAME;
			contentUri = ActivityTypeTable.CONTENT_URI;
			break;
		case ACTIVITY_COLLECTION_URI_INDICATOR:
			tableName = ActivityTable.TABLE_NAME;
			nullColumnHack = ActivityTable.ACTIVITY_TYPE_ID;
			contentUri = ActivityTable.CONTENT_URI;
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
		case ACTIVITY_TYPE_COLLECTION_URI_INDICATOR:
			tableName = ActivityTypeTable.TABLE_NAME;
			break;
		case ACTIVITY_TYPE_SINGLE_URI_INDICATOR:
			tableName = ActivityTypeTable.TABLE_NAME;
			rowId = uri.getPathSegments().get(1);
			break;
		case ACTIVITY_COLLECTION_URI_INDICATOR:
			tableName = ActivityTable.TABLE_NAME;
			break;
		case ACTIVITY_SINGLE_URI_INDICATOR:
			tableName = ActivityTable.TABLE_NAME;
			rowId = uri.getPathSegments().get(1);
			break;

		default:
			throw new IllegalArgumentException("Unknown Uri " + uri);
		}

		whereString = (rowId == "-1" ? "" : ActivityTable._ID + "=" + rowId)
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
		case ACTIVITY_TYPE_COLLECTION_URI_INDICATOR:
			tableName = ActivityTypeTable.TABLE_NAME;
			break;
		case ACTIVITY_TYPE_SINGLE_URI_INDICATOR:
			tableName = ActivityTypeTable.TABLE_NAME;
			rowId = uri.getPathSegments().get(1);
			break;
		case ACTIVITY_COLLECTION_URI_INDICATOR:
			tableName = ActivityTable.TABLE_NAME;
			break;
		case ACTIVITY_SINGLE_URI_INDICATOR:
			tableName = ActivityTable.TABLE_NAME;
			rowId = uri.getPathSegments().get(1);
			break;

		default:
			throw new IllegalArgumentException("Unknown Uri " + uri);
		}

		whereString = (rowId == "-1" ? "" : ActivityTable._ID + "=" + rowId)
				+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
						: "");

		count = db.update(tableName, values, whereString, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
