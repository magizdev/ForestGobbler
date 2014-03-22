package com.magizdev.dayplan.versionone.store;

import java.security.PublicKey;
import java.util.HashMap;

import android.net.Uri;
import android.provider.BaseColumns;

public class DayPlanMetaData {
	public static final String AUTHORITY = "com.magizdev.dayplan";
	public static final String DATABASE_NAME = "dayplan.db";
	public static final int DATABASE_VERSION = 1;

	private DayPlanMetaData() {
	}

	public static final class VersionOneServerTable implements BaseColumns {
		private VersionOneServerTable() {

		}

		public static final String TABLE_NAME = "versionOneServer";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/versiononeservers");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.magizdev.dayplan.versionone.server";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.magizdev.dayplan.versionone.server";
		public static final String DEFAULT_SORT_ORDER = "url";

		public static final String URL = "url";
		public static final String USERNAME = "username";
		public static final String TOKEN = "token";
		public static final String ISCURRENT = "iscurrent";

		public static HashMap<String, String> projectionMap;

		static {
			projectionMap = new HashMap<String, String>();
			projectionMap.put(VersionOneServerTable._ID,
					VersionOneServerTable._ID);
			projectionMap.put(VersionOneServerTable.URL,
					VersionOneServerTable.URL);
			projectionMap.put(VersionOneServerTable.USERNAME,
					VersionOneServerTable.USERNAME);
			projectionMap.put(VersionOneServerTable.TOKEN,
					VersionOneServerTable.TOKEN);
			projectionMap.put(VersionOneServerTable.ISCURRENT,
					VersionOneServerTable.ISCURRENT);
		}
	}

	public static final class BacklogItemTable implements BaseColumns {
		private BacklogItemTable() {
		}

		public static final String TABLE_NAME = "backlog";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/backlogs");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.magizdev.dayplan.versionone.backlogitem";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.magizdev.dayplan.versionone.backlogitem";
		public static final String DEFAULT_SORT_ORDER = "name";

		public static final String NAME = "name";
		public static final String DESC = "desc";
		public static final String STATE = "bi_state";
		public static final String ESTIMATE = "estimate";
		// public static final String REMAIN_ESTIMATE = "remain_estimate";
		public static final String DUE_DATE = "due_date";
		public static final String SERVERID = "server_id";
		public static final String SERVER_SIDE_ID  = "server_side_id";
		public static final int STATE_ACTIVE = 0;
		public static final int STATE_COMPLETE = 1;

		public static HashMap<String, String> projectionMap;

		static {
			projectionMap = new HashMap<String, String>();
			projectionMap.put(BacklogItemTable._ID, BacklogItemTable._ID);
			projectionMap.put(BacklogItemTable.NAME, BacklogItemTable.NAME);
			projectionMap.put(BacklogItemTable.DESC, BacklogItemTable.DESC);
			projectionMap.put(BacklogItemTable.STATE, BacklogItemTable.STATE);
			projectionMap.put(BacklogItemTable.SERVERID,
					BacklogItemTable.SERVERID);
			projectionMap.put(BacklogItemTable.ESTIMATE,
					BacklogItemTable.ESTIMATE);
			projectionMap.put(BacklogItemTable.DUE_DATE,
					BacklogItemTable.DUE_DATE);
		}
	}

	public static final class DayTaskTable implements BaseColumns {
		private DayTaskTable() {
		}

		public static final String TABLE_NAME = "daytask";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/daytasks");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.magizdev.dayplan.versionone.daytask";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.magizdev.dayplan.versionone.daytask";
		public static final String DEFAULT_SORT_ORDER = "date";

		public static final String DATE = "date";
		public static final String BIID = "backlogid";
		public static final String STATE = "state";
		public static final String EFFORT = "effort";
		public static final String REMAIN_ESTIMATE = "remain_estimate";
		public static final int STATE_RUNNING = 0;
		public static final int STATE_PAUSEING = 1;

		public static HashMap<String, String> projectionMap;

		static {
			projectionMap = new HashMap<String, String>();
			projectionMap.put(DayTaskTable._ID, DayTaskTable.TABLE_NAME + "."
					+ DayTaskTable._ID);
			projectionMap.put(DayTaskTable.DATE, DayTaskTable.DATE);
			projectionMap.put(DayTaskTable.BIID, DayTaskTable.BIID);
			projectionMap.put(BacklogItemTable.NAME, BacklogItemTable.NAME);
			projectionMap.put(DayTaskTable.STATE, DayTaskTable.STATE);
			projectionMap.put(DayTaskTable.EFFORT, DayTaskTable.EFFORT);
			projectionMap.put(DayTaskTable.REMAIN_ESTIMATE,
					DayTaskTable.REMAIN_ESTIMATE);
			projectionMap.put(BacklogItemTable.ESTIMATE, BacklogItemTable.ESTIMATE);
		}
	}

	public static final class DayTaskTimeTable implements BaseColumns {
		private DayTaskTimeTable() {
		}

		public static final String TABLE_NAME = "daytasktime";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/daytasktime");

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.magizdev.dayplan.versionone.daytasktime";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.magizdev.dayplan.versionone.daytasktime";
		public static final String DEFAULT_SORT_ORDER = "start_time DESC";

		public static final String DATE = "date";
		public static final String BIID = "backlogid";
		public static final String START_TIME = "start_time";
		public static final String END_TIME = "end_time";

		public static HashMap<String, String> projectionMap;

		static {
			projectionMap = new HashMap<String, String>();
			projectionMap.put(DayTaskTimeTable._ID, DayTaskTimeTable.TABLE_NAME
					+ "." + DayTaskTimeTable._ID);
			projectionMap.put(DayTaskTimeTable.DATE, DayTaskTimeTable.DATE);
			projectionMap.put(DayTaskTimeTable.BIID, DayTaskTimeTable.BIID);
			projectionMap.put(BacklogItemTable.NAME, BacklogItemTable.NAME);
			projectionMap.put(DayTaskTimeTable.START_TIME,
					DayTaskTimeTable.START_TIME);
			projectionMap.put(DayTaskTimeTable.END_TIME,
					DayTaskTimeTable.END_TIME);
		}
	}

}
