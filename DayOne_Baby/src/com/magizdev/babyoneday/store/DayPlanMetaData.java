package com.magizdev.babyoneday.store;

import java.util.HashMap;

import android.net.Uri;
import android.provider.BaseColumns;

public class DayPlanMetaData {
	public static final String AUTHORITY = "com.magizdev.babydayone";
	public static final String DATABASE_NAME = "babydayone.db";
	public static final int DATABASE_VERSION = 1;

	private DayPlanMetaData() {
	}

	public static final class ActivityTypeTable implements BaseColumns {
		private ActivityTypeTable() {
		}

		public static final String TABLE_NAME = "activityType";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/activityType");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.magizdev.babydayone.activityType";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.magizdev.babydayone.activityType";
		public static final String DEFAULT_SORT_ORDER = "name";

		public static final String NAME = "name";
		public static final String TIME_TYPE = "time_type";
		public static final String REMIND_INTERVAL = "remind_interval";
		public static final String REMIND_DURATION = "remind_duration";
		public static final int TIME_TYPE_DURATION = 0;
		public static final int TIME_TYPE_ONCE = 1;

		public static HashMap<String, String> projectionMap;

		static {
			projectionMap = new HashMap<String, String>();
			projectionMap.put(ActivityTypeTable._ID, ActivityTypeTable._ID);
			projectionMap.put(ActivityTypeTable.NAME, ActivityTypeTable.NAME);
			projectionMap.put(ActivityTypeTable.REMIND_INTERVAL,
					ActivityTypeTable.REMIND_INTERVAL);
		}
	}

	public static final class ActivityTable implements BaseColumns {
		private ActivityTable() {
		}

		public static final String TABLE_NAME = "activity";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/activity");

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.magizdev.babydayone.activity";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.magizdev.babydayone.activity";
		public static final String DEFAULT_SORT_ORDER = "time DESC";

		public static final String DATE = "date";
		public static final String ACTIVITY_TYPE_ID = "activity_type_id";
		public static final String NOTE = "note";
		public static final String DATA = "data";
		public static final String START_TIME = "start_time";
		public static final String END_TIME = "end_time";

		public static HashMap<String, String> projectionMap;

		static {
			projectionMap = new HashMap<String, String>();
			projectionMap.put(ActivityTable._ID, ActivityTable.TABLE_NAME + "."
					+ ActivityTable._ID);
			projectionMap.put(ActivityTable.DATE, ActivityTable.DATE);
			projectionMap.put(ActivityTable.ACTIVITY_TYPE_ID,
					ActivityTable.ACTIVITY_TYPE_ID);
			projectionMap.put(ActivityTypeTable.NAME, ActivityTypeTable.NAME);
			projectionMap.put(ActivityTable.START_TIME,
					ActivityTable.START_TIME);
			projectionMap.put(ActivityTable.END_TIME, ActivityTable.END_TIME);
			projectionMap.put(ActivityTable.NOTE, ActivityTable.NOTE);
			projectionMap.put(ActivityTable.DATA, ActivityTable.DATA);
			projectionMap.put(ActivityTypeTable.TIME_TYPE,
					ActivityTypeTable.TIME_TYPE);
		}
	}
}
