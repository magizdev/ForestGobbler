package com.magizdev.dayplan.store;

import java.util.HashMap;

import android.net.Uri;
import android.provider.BaseColumns;

public class DayPlanMetaData {
	public static final String AUTHORITY = "com.magizdev.dayplan";
	public static final String DATABASE_NAME = "dayplan.db";
	public static final int DATABASE_VERSION = 1;

	private DayPlanMetaData() {
	}

	public static final class BacklogItemTable implements BaseColumns {
		private BacklogItemTable() {
		}

		public static final String TABLE_NAME = "backlog";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/backlogs");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.magizdev.dayplan.backlogitem";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.magizdev.dayplan.backlogitem";
		public static final String DEFAULT_SORT_ORDER = "name";

		public static final String NAME = "name";
		public static final String DESC = "desc";
		public static final String STATE = "bi_state";
		public static final int STATE_ACTIVE = 0;
		public static final int STATE_COMPLETE = 1;
		
		public static HashMap<String, String> projectionMap;

		static {
			projectionMap = new HashMap<String, String>();
			projectionMap.put(BacklogItemTable._ID, BacklogItemTable._ID);
			projectionMap.put(BacklogItemTable.NAME, BacklogItemTable.NAME);
			projectionMap.put(BacklogItemTable.DESC, BacklogItemTable.DESC);
			projectionMap.put(BacklogItemTable.STATE, BacklogItemTable.STATE);
		}
	}

	public static final class DayTaskTable implements BaseColumns {
		private DayTaskTable() {
		}

		public static final String TABLE_NAME = "daytask";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/daytasks");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.magizdev.dayplan.daytask";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.magizdev.dayplan.daytask";
		public static final String DEFAULT_SORT_ORDER = "date";

		public static final String DATE = "date";
		public static final String BIID = "backlogid";
		public static final String STATE = "state";
		public static final int STATE_RUNNING = 0;
		public static final int STATE_PAUSEING = 1;

		public static HashMap<String, String> projectionMap;

		static {
			projectionMap = new HashMap<String, String>();
			projectionMap.put(DayTaskTable._ID, DayTaskTable.TABLE_NAME + "." + DayTaskTable._ID);
			projectionMap.put(DayTaskTable.DATE, DayTaskTable.DATE);
			projectionMap.put(DayTaskTable.BIID, DayTaskTable.BIID);
			projectionMap.put(BacklogItemTable.NAME, BacklogItemTable.NAME);
			projectionMap.put(DayTaskTable.STATE, DayTaskTable.STATE);
		}
	}

	public static final class DayTaskTimeTable implements BaseColumns {
		private DayTaskTimeTable() {
		}

		public static final String TABLE_NAME = "daytasktime";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/daytasktime");

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.magizdev.dayplan.daytasktime";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.magizdev.dayplan.daytasktime";
		public static final String DEFAULT_SORT_ORDER = "date";

		public static final String DATE = "date";
		public static final String BIID = "backlogid";
		public static final String TIME = "time";
		public static final String TIME_TYPE = "time_type";

		public static final int TIME_TYPE_START = 0;
		public static final int TIME_TYPE_STOP = 1;
		
		public static HashMap<String, String> projectionMap;

		static {
			projectionMap = new HashMap<String, String>();
			projectionMap.put(DayTaskTimeTable._ID, DayTaskTimeTable._ID);
			projectionMap.put(DayTaskTimeTable.DATE, DayTaskTimeTable.DATE);
			projectionMap.put(DayTaskTimeTable.BIID, DayTaskTimeTable.BIID);
			projectionMap.put(BacklogItemTable.NAME, BacklogItemTable.NAME);
			projectionMap.put(DayTaskTimeTable.TIME, DayTaskTimeTable.TIME);
			projectionMap.put(DayTaskTimeTable.TIME_TYPE, DayTaskTimeTable.TIME_TYPE);
		}
	}
}
