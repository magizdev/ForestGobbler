package com.magizdev.easytask.store;

import android.net.Uri;
import android.provider.BaseColumns;

public class EasyTaskMetaData {
	public static final String AUTHORITY="com.magizdev.easytask";
	public static final String DATABASE_NAME="easytask.db";
	public static final int DATABASE_VERSION=1;
	public static final String TASK_TABLE_NAME_STRING="tasks";
	private EasyTaskMetaData(){}
	
	public static final class TaskTableMetaData implements BaseColumns{
		private TaskTableMetaData(){}
		
		public static final String TABLE_NAME="tasks";
		public static final Uri CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/tasks");
		public static final String CONTENT_TYPE="vnd.android.cursor.dir/vnd.androidtask.task";
		public static final String CONTENT_ITEM_TYPE="vnd.android.cursor.item/vnd.androidtask.task";
		public static final String DEFAULT_SORT_ORDER="create_date DESC";
		
		public static final String TASK_NOTE = "note";
		public static final String CREATE_DATE = "create_date";
		public static final String START_DATE = "start_date";
	}
}
