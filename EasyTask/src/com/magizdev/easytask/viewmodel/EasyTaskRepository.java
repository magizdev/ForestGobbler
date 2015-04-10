package com.magizdev.easytask.viewmodel;

import java.util.Date;
import java.util.List;

import android.content.Context;

import com.activeandroid.query.Select;

public class EasyTaskRepository {
	private Context context;

	public EasyTaskRepository(Context context) {
		this.context = context;
	}

	public List<EasyTaskInfo> getTasks() {
		List<EasyTaskInfo> tasks = new Select().from(EasyTaskInfo.class)
				.execute();
		return tasks;
	}

	public EasyTaskInfo getTask(long id) {
		return EasyTaskInfo.load(EasyTaskInfo.class, id);
	}

	public EasyTaskInfo getNextTask() {
		Long now = new Date().getTime();

		EasyTaskInfo nextTaskInfo = new Select().from(EasyTaskInfo.class)
				.where("notifyDate > ?", now).orderBy("notifyDate")
				.executeSingle();
		return nextTaskInfo;
	}

	public void deleteTask(long id) {
		EasyTaskInfo task = EasyTaskInfo.load(EasyTaskInfo.class, id);
		if (task != null) {
			task.delete();
		}
	}
}
