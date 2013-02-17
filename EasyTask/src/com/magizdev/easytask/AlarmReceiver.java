package com.magizdev.easytask;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

import com.magizdev.easytask.viewmodel.EasyTaskInfo;
import com.magizdev.easytask.viewmodel.EasyTaskUtil;

public class AlarmReceiver extends BroadcastReceiver {
	private Context context;
	private EasyTaskUtil util;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		long id = intent.getLongExtra("easyTaskId", 0l);
		util = new EasyTaskUtil(context);
		EasyTaskInfo task = util.getTask(id);
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification.Builder(context)
				.setContentTitle(task.Note)
				.setSound(
						RingtoneManager
								.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setVibrate(new long[] { 500 })
				.setSmallIcon(R.drawable.clock_blue).getNotification();

		notificationManager.notify(0, notification);
		sendAlarm();
	}
	
	private void sendAlarm() {
		EasyTaskInfo nextEasyTaskInfo = util.getNextTask();
		if (nextEasyTaskInfo != null) {
			Intent intent = new Intent(context, AlarmReceiver.class);
			intent.putExtra("easyTaskId", nextEasyTaskInfo.Id);
			PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent,
					PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					nextEasyTaskInfo.StartDate.getTime(), pIntent);
		}
	}
}
