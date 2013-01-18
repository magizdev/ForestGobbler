package com.magizdev.easytask;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.magizdev.easytask.viewmodel.EasyTaskInfo;
import com.magizdev.easytask.viewmodel.EasyTaskUtil;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
//		Intent alarmIntent = new Intent(context, AlarmActivity.class);
//		alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(alarmIntent);
		
		long id = intent.getLongExtra("easyTaskId", 0l);
		EasyTaskUtil util = new EasyTaskUtil(context);
		EasyTaskInfo task =util.getTask(id);
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.clock_blue,
				task.Note, System.currentTimeMillis());

//		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
//				new Intent(context, AlarmActivity.class), 0);
//		String alarmString="Time is up!";
//		notification.setLatestEventInfo(context, "Tita", alarmString, contentIntent);
		notificationManager.notify(0, notification);
	}
}
