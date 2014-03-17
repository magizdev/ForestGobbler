package com.magizdev.dayplan.versionone.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NavigableMap;

import android.content.Context;

import com.magizdev.dayplan.versionone.model.BacklogItem;
import com.magizdev.dayplan.versionone.model.ChartData;
import com.magizdev.dayplan.versionone.model.Task;
import com.magizdev.dayplan.versionone.store.StorageUtil;
import com.magizdev.dayplan.versionone.store.DayPlanMetaData.BacklogItemTable;
import com.magizdev.dayplan.versionone.store.DayPlanMetaData.DayTaskTable;

public class BurndownNavigate implements INavigate {
	private StorageUtil<BacklogItem> storageUtil;
	private StorageUtil<Task> taskStorageUtil;
	private int current;
	private List<Long> ids;
	private List<String> names;

	public BurndownNavigate(Context context) {
		storageUtil = new StorageUtil<BacklogItem>(context,
				new BacklogItem());
		taskStorageUtil = new StorageUtil<Task>(context, new Task());
		List<BacklogItem> backlogItemInfos = storageUtil.getCollection(
				BacklogItemTable.ESTIMATE + " > 0", null);
		ids = new ArrayList<Long>();
		names = new ArrayList<String>();
		for (BacklogItem backlogItemInfo : backlogItemInfos) {
			ids.add(backlogItemInfo.Id);
			names.add(backlogItemInfo.Name);
		}
	}

	public List<String> getNames() {
		return names;
	}

	@Override
	public void Backword() {

	}

	@Override
	public void Forward() {

	}

	@Override
	public void SetPostion(int offset) {
		current = offset;
	}

	@Override
	public String CurrentTitle() {
		return names.get(current);
	}

	@Override
	public boolean IsLast() {
		return current == ids.size() - 1;
	}

	@Override
	public List<ChartData> GetPieChartData() {
		return null;
	}

	@Override
	public HashMap<Integer, List<ChartData>> GetBarChartData() {
		if(ids.size() == 0){
			return new HashMap<Integer, List<ChartData>>();
		}
		List<Task> result = taskStorageUtil.getCollection(
				DayTaskTable.BIID + " = " + ids.get(current), null);
		HashMap<Integer, List<ChartData>> temp = new HashMap<Integer, List<ChartData>>();
		for (Task dayTaskInfo : result) {
			ChartData pieChartData = new ChartData(dayTaskInfo.BIID,
					dayTaskInfo.BIName, dayTaskInfo.RemainEffort);
			List<ChartData> temp2 = new ArrayList<ChartData>();
			temp2.add(pieChartData);
			temp.put(dayTaskInfo.Date, temp2);
		}
		return temp;
	}

}
