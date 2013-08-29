package com.magizdev.dayplan.store;

import android.net.Uri;
import android.provider.BaseColumns;

public class BacklogItemMetaData {
	public static final String AUTHORITY = "com.magizdev.easytask";
	public static final String DATABASE_NAME = "easytask.db";
	public static final int DATABASE_VERSION = 2;
	public static final String TASK_TABLE_NAME_STRING = "tasks";

	private BacklogItemMetaData() {
	}

	public static final class TaskTableMetaData implements BaseColumns {
		private TaskTableMetaData() {
		}

		public static final String TABLE_NAME = "tasks";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/tasks");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.androidtask.task";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.androidtask.task";
		public static final String DEFAULT_SORT_ORDER = "create_date DESC";

		public static final String TASK_TITLE = "title";
		public static final String TASK_NOTE = "note";
		public static final String CREATE_DATE = "create_date";
		public static final String START_DATE = "start_date";
		public static final String SOURCE = "source";
		public static final String SOURCE_ID = "source_id";
	}
}
