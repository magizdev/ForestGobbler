package com.magizdev.easytask;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

import com.magizdev.easytask.util.AlarmUtil;
import com.magizdev.easytask.viewmodel.EasyTaskInfo;
import com.magizdev.easytask.viewmodel.EasyTaskRepository;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		long id = intent.getLongExtra("easyTaskId", 0l);
		EasyTaskRepository util = new EasyTaskRepository(context);
		EasyTaskInfo task = util.getTask(id);
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification.Builder(context)
				.setContentTitle(task.Title)
				.setSound(
						RingtoneManager
								.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setVibrate(new long[] { 500, 800 })
				.setSmallIcon(R.drawable.clock_blue).getNotification();

		notificationManager.notify(0, notification);
		AlarmUtil.updateAlarm(context);
	}
}
