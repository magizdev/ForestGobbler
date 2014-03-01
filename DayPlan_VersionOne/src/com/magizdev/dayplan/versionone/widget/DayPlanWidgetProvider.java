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

package com.magizdev.dayplan.versionone.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.RemoteViews;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.versionone.DrawerActivity;
import com.magizdev.dayplan.versionone.store.DayPlanMetaData;
import com.magizdev.dayplan.versionone.util.DayTaskUtil;

/**
 * Our data observer just notifies an update for all weather widgets when it
 * detects a change.
 */
class WeatherDataProviderObserver extends ContentObserver {
	private AppWidgetManager mAppWidgetManager;
	private ComponentName mComponentName;

	WeatherDataProviderObserver(AppWidgetManager mgr, ComponentName cn,
			Handler h) {
		super(h);
		mAppWidgetManager = mgr;
		mComponentName = cn;
	}

	@Override
	public void onChange(boolean selfChange) {
		// The data has changed, so notify the widget that the collection view
		// needs to be updated.
		// In response, the factory's onDataSetChanged() will be called which
		// will requery the
		// cursor for the new data.
		mAppWidgetManager.notifyAppWidgetViewDataChanged(
				mAppWidgetManager.getAppWidgetIds(mComponentName),
				R.id.task_list);
	}
}

/**
 * The weather widget's AppWidgetProvider.
 */
public class DayPlanWidgetProvider extends AppWidgetProvider {
	public static String CLICK_ACTION = "com.magizdev.dayplan.widget.CLICK";
	public static String EMPTY_VIEW_CLICK_ACTION = "com.magizdev.dayplan.widget.EMPTY_VIEW_CLICK";
	public static String REFRESH_ACTION = "com.magizdev.dayplan.widget.REFRESH";
	public static String AUTO_REFRESH_ACTION = "com.magizdev.dayplan.widget.AUTO_REFRESH";
	public static String REPORT_ACTION = "com.magizdev.dayplan.widget.REPORT";
	public static String EXTRA_BI_ID = "com.magizdev.dayplan.widget.biid";

	private static HandlerThread sWorkerThread;
	private static Handler sWorkerQueue;
	private static WeatherDataProviderObserver sDataObserver;

	private boolean mIsLargeLayout = true;
	private PendingIntent refreshPendingIntent;

	public DayPlanWidgetProvider() {
		// Start the worker thread
		sWorkerThread = new HandlerThread("WeatherWidgetProvider-worker");
		sWorkerThread.start();
		sWorkerQueue = new Handler(sWorkerThread.getLooper());
	}

	// XXX: clear the worker queue if we are destroyed?

	@Override
	public void onEnabled(Context context) {
		// Register for external updates to the data to trigger an update of the
		// widget. When using
		// content providers, the data is often updated via a background
		// service, or in response to
		// user interaction in the main app. To ensure that the widget always
		// reflects the current
		// state of the data, we must listen for changes and update ourselves
		// accordingly.
		final ContentResolver r = context.getContentResolver();
		if (sDataObserver == null) {
			final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
			final ComponentName cn = new ComponentName(context,
					DayPlanWidgetProvider.class);
			sDataObserver = new WeatherDataProviderObserver(mgr, cn,
					sWorkerQueue);
			r.registerContentObserver(DayPlanMetaData.DayTaskTable.CONTENT_URI,
					true, sDataObserver);
		}
	}

	@Override
	public void onReceive(Context ctx, Intent intent) {
		final String action = intent.getAction();
		final Context context = ctx;
		if (action.equals(REFRESH_ACTION)) {
			sWorkerQueue.removeMessages(0);
			sWorkerQueue.post(new Runnable() {
				@Override
				public void run() {
					final AppWidgetManager mgr = AppWidgetManager
							.getInstance(context);
					final ComponentName cn = new ComponentName(context,
							DayPlanWidgetProvider.class);
					mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn),
							R.id.task_list);
				}
			});

			final int appWidgetId = intent.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		} else if (action.equals(CLICK_ACTION)) {
			// Show a toast
			final int appWidgetId = intent.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			final Long biid = intent.getLongExtra(EXTRA_BI_ID, 0L);
			DayTaskUtil util = new DayTaskUtil(ctx);
			boolean waitStop = util.IsTaskWaitingForStop(biid);
			if (waitStop) {
				util.StopTask(biid);
			} else {
				util.StartTask(biid);
			}
			sWorkerQueue.removeMessages(0);
			sWorkerQueue.post(new Runnable() {
				@Override
				public void run() {
					final AppWidgetManager mgr = AppWidgetManager
							.getInstance(context);
					final ComponentName cn = new ComponentName(context,
							DayPlanWidgetProvider.class);
					mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn),
							R.id.task_list);
				}
			});
		} else if (action.equals(EMPTY_VIEW_CLICK_ACTION)) {
			Intent chooseTaskIntent = new Intent(ctx, DrawerActivity.class);
			chooseTaskIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ctx.startActivity(chooseTaskIntent);
		} else if (action.equals(REPORT_ACTION)) {
			Intent chooseTaskIntent = new Intent(ctx, DrawerActivity.class);
			chooseTaskIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ctx.startActivity(chooseTaskIntent);
		} else if (action.equals(AUTO_REFRESH_ACTION)) {
			if (sDataObserver == null) {
				final AppWidgetManager mgr = AppWidgetManager
						.getInstance(context);
				final ComponentName cn = new ComponentName(context,
						DayPlanWidgetProvider.class);
				sDataObserver = new WeatherDataProviderObserver(mgr, cn,
						sWorkerQueue);
			}
			sDataObserver.dispatchChange(true);
		}

		super.onReceive(ctx, intent);
	}

	private RemoteViews buildLayout(Context context, int appWidgetId,
			boolean largeLayout) {
		RemoteViews rv;

		// Specify the service to provide data for the collection widget. Note
		// that we need to
		// embed the appWidgetId via the data otherwise it will be ignored.
		final Intent intent = new Intent(context, DayPlanWidgetService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		rv.setRemoteAdapter(appWidgetId, R.id.task_list, intent);

		// Set the empty view to be displayed if the collection is empty. It
		// must be a sibling
		// view of the collection view.
		rv.setEmptyView(R.id.task_list, R.id.empty_view);
		// Bind the click intent for the refresh button on the widget
		final Intent emptyViewClickIntent = new Intent(context,
				DayPlanWidgetProvider.class);
		emptyViewClickIntent
				.setAction(DayPlanWidgetProvider.EMPTY_VIEW_CLICK_ACTION);
		final PendingIntent emptyViewClickPendingIntent = PendingIntent
				.getBroadcast(context, 0, emptyViewClickIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setOnClickPendingIntent(R.id.empty_view, emptyViewClickPendingIntent);

		// Bind a click listener template for the contents of the weather list.
		// Note that we
		// need to update the intent's data if we set an extra, since the extras
		// will be
		// ignored otherwise.
		final Intent onClickIntent = new Intent(context,
				DayPlanWidgetProvider.class);
		onClickIntent.setAction(DayPlanWidgetProvider.CLICK_ACTION);
		onClickIntent
				.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		onClickIntent.setData(Uri.parse(onClickIntent
				.toUri(Intent.URI_INTENT_SCHEME)));
		final PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(
				context, 0, onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setPendingIntentTemplate(R.id.task_list, onClickPendingIntent);

		// Bind the click intent for the refresh button on the widget
		final Intent refreshIntent = new Intent(context,
				DayPlanWidgetProvider.class);
		refreshIntent.setAction(DayPlanWidgetProvider.REFRESH_ACTION);
		final PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(
				context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setOnClickPendingIntent(R.id.refresh, refreshPendingIntent);

		// Bind the click intent for the refresh button on the widget
		final Intent reportIntent = new Intent(context,
				DayPlanWidgetProvider.class);
		reportIntent.setAction(DayPlanWidgetProvider.REPORT_ACTION);
		final PendingIntent reportPendingIntent = PendingIntent.getBroadcast(
				context, 0, reportIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setOnClickPendingIntent(R.id.report, reportPendingIntent);

		return rv;
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// Update each of the widgets with the remote adapter
		for (int i = 0; i < appWidgetIds.length; ++i) {
			RemoteViews layout = buildLayout(context, appWidgetIds[i],
					mIsLargeLayout);
			appWidgetManager.updateAppWidget(appWidgetIds[i], layout);
		}

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		if (refreshPendingIntent == null) {
			final Intent refreshIntent = new Intent(context,
					DayPlanWidgetProvider.class);
			refreshIntent.setAction(DayPlanWidgetProvider.AUTO_REFRESH_ACTION);
			refreshPendingIntent = PendingIntent.getBroadcast(context, 0,
					refreshIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		}
		alarmManager.setRepeating(AlarmManager.RTC, 60000, 90000,
				refreshPendingIntent);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		if (refreshPendingIntent == null) {
			final Intent refreshIntent = new Intent(context,
					DayPlanWidgetProvider.class);
			refreshIntent.setAction(DayPlanWidgetProvider.AUTO_REFRESH_ACTION);
			refreshPendingIntent = PendingIntent.getBroadcast(context, 0,
					refreshIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		}
		alarmManager.cancel(refreshPendingIntent);
		super.onDisabled(context);
	}
}