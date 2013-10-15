/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.magizdev.dayplan.widget;

import java.util.List;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.store.DayPlanMetaData.DayTaskTable;
import com.magizdev.dayplan.util.DayUtil;
import com.magizdev.dayplan.viewmodel.DayTaskInfo;
import com.magizdev.dayplan.viewmodel.DayTaskInfo.TaskState;
import com.magizdev.dayplan.viewmodel.StorageUtil;

/**
 * This is the service that provides the factory to be bound to the collection
 * service.
 */
public class DayPlanWidgetService extends RemoteViewsService {
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
	}
}

/**
 * This is the factory that will provide data to the collection widget.
 */
class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
	private Context mContext;
	private List<DayTaskInfo> allTasks;
	private int mAppWidgetId;

	public StackRemoteViewsFactory(Context context, Intent intent) {
		mContext = context;
		mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
	}

	public void onCreate() {
		// Since we reload the cursor in onDataSetChanged() which gets called
		// immediately after
		// onCreate(), we do nothing here.
	}

	public void onDestroy() {
	}

	public int getCount() {
		return allTasks.size();
	}

	public RemoteViews getViewAt(int position) {
		// Get the data for this position from the content provider
		Log.w("b", "here");
		Log.w("b", position + "");
		DayTaskInfo taskInfo = allTasks.get(position);

		final int itemId = R.layout.widget_item;
		RemoteViews rv = new RemoteViews(mContext.getPackageName(), itemId);
		rv.setTextViewText(R.id.tVName1_remote, taskInfo.BIName);
		rv.setImageViewResource(
				R.id.startButton_remote,
				taskInfo.State == TaskState.Running ? android.R.drawable.ic_media_pause
						: android.R.drawable.ic_media_play);

		rv.setViewVisibility(R.id.taskStatus_remote,
				taskInfo.State == TaskState.Running ? View.VISIBLE
						: View.INVISIBLE);
		// Set the click intent so that we can handle it and show a toast
		// message
		// final Intent fillInIntent = new Intent();
		// final Bundle extras = new Bundle();
		// extras.putString(WeatherWidgetProvider.EXTRA_DAY_ID, day);
		// fillInIntent.putExtras(extras);
		// rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);
		Log.w("b", "there");

		return rv;
	}

	public RemoteViews getLoadingView() {
		// We aren't going to return a default loading view in this sample
		return null;
	}

	public int getViewTypeCount() {
		// Technically, we have two types of views (the dark and light
		// background views)
		return 2;
	}

	public long getItemId(int position) {
		return allTasks.get(position).BIID;
	}

	public boolean hasStableIds() {
		return true;
	}

	public void onDataSetChanged() {
		DayTaskInfo blank = new DayTaskInfo();
		StorageUtil<DayTaskInfo> storageUtil = new StorageUtil<DayTaskInfo>(
				mContext, blank);
		String whereStrings = DayTaskTable.DATE + "=" + DayUtil.Today();
		allTasks = storageUtil.getCollection(whereStrings);
	}
}
