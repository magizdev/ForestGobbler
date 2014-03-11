package com.magizdev.dayplan.versionone.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NavigableMap;

import android.content.Context;

import com.magizdev.dayplan.versionone.PieChartData;
import com.magizdev.dayplan.versionone.store.DayPlanMetaData.BacklogItemTable;
import com.magizdev.dayplan.versionone.store.DayPlanMetaData.DayTaskTable;
import com.magizdev.dayplan.versionone.viewmodel.BacklogItemInfo;
import com.magizdev.dayplan.versionone.viewmodel.DayTaskInfo;
import com.magizdev.dayplan.versionone.viewmodel.StorageUtil;

public class BurndownNavigate implements INavigate {
	private StorageUtil<BacklogItemInfo> storageUtil;
	private StorageUtil<DayTaskInfo> taskStorageUtil;
	private int current;
	private List<Long> ids;
	private List<String> names;

	public BurndownNavigate(Context context) {
		storageUtil = new StorageUtil<BacklogItemInfo>(context,
				new BacklogItemInfo());
		taskStorageUtil = new StorageUtil<DayTaskInfo>(context, new DayTaskInfo());
		List<BacklogItemInfo> backlogItemInfos = storageUtil.getCollection(
				BacklogItemTable.ESTIMATE + " > 0", null);
		ids = new ArrayList<Long>();
		names = new ArrayList<String>();
		for (BacklogItemInfo backlogItemInfo : backlogItemInfos) {
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
	public List<PieChartData> GetPieChartData() {
		return null;
	}

	@Override
	public HashMap<Integer, List<PieChartData>> GetBarChartData() {
		List<DayTaskInfo> result = taskStorageUtil.getCollection(
				DayTaskTable.BIID + " = " + ids.get(current), null);
		HashMap<Integer, List<PieChartData>> temp = new HashMap<Integer, List<PieChartData>>();
		for (DayTaskInfo dayTaskInfo : result) {
			PieChartData pieChartData = new PieChartData(dayTaskInfo.BIID,
					dayTaskInfo.BIName, dayTaskInfo.RemainEffort);
			List<PieChartData> temp2 = new ArrayList<PieChartData>();
			temp2.add(pieChartData);
			temp.put(dayTaskInfo.Date, temp2);
		}
		return temp;
	}

}
