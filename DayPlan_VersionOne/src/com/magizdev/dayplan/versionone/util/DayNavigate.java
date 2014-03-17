package com.magizdev.dayplan.versionone.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.magizdev.dayplan.versionone.model.ChartData;
import com.magizdev.dayplan.versionone.model.TaskTimeRecord;

public class DayNavigate implements INavigate {
	private int current;
	private int today;
	private DayTaskTimeUtil util;

	public DayNavigate(Context context) {
		util = new DayTaskTimeUtil(context);
		current = DayUtil.Today();
		today = current;
	}

	@Override
	public void Backword() {
		current--;
	}

	@Override
	public void Forward() {
		if (!IsLast()) {
			current++;
		}
	}

	@Override
	public String CurrentTitle() {
		Calendar calendar = DayUtil.toCalendar(current);
		return calendar.get(Calendar.YEAR) + "-"
				+ (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	public boolean IsLast() {
		return current == today;
	}

	@Override
	public List<ChartData> GetPieChartData() {
		List<TaskTimeRecord> data = util.GetByDate(current);

		List<ChartData> chartDatas = DayTaskTimeUtil.compute(data);
		return chartDatas;
	}

	@Override
	public HashMap<Integer, List<ChartData>> GetBarChartData() {
		List<TaskTimeRecord> data = util.GetByDate(current);

		HashMap<Integer, List<ChartData>> chartDatas = DayTaskTimeUtil
				.computeBarData(data);
		return chartDatas;
	}

	@Override
	public void SetPostion(int offset) {
		current = today + offset;
	}

}
