package com.magizdev.dayplan.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.magizdev.dayplan.PieChartBuilder;
import com.magizdev.dayplan.PieChartBuilder.PieChartData;
import com.magizdev.dayplan.viewmodel.DayTaskTimeInfo;

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
		return calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH)
				+ "-" + calendar.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	public boolean IsLast() {
		return current == today;
	}

	@Override
	public List<PieChartData> GetPieChartData() {
		List<DayTaskTimeInfo> data = util.GetByDate(DayUtil.Today());

		List<PieChartData> chartDatas = compute(data);
		return chartDatas;
	}
	
	private List<PieChartData> compute(List<DayTaskTimeInfo> input) {
		HashMap<String, Integer> data = new HashMap<String, Integer>();
		HashMap<String, Integer> couter = new HashMap<String, Integer>();
		for (int i = input.size() - 1; i > -1; i--) {
			DayTaskTimeInfo current = input.get(i);
			int span = 0;
			if (couter.containsKey(current.BIName)) {
				span = current.Time - couter.get(current.BIName);
				couter.remove(current.BIName);
			} else {
				couter.put(current.BIName, current.Time);
			}

			if (span > 0) {
				if (data.containsKey(current.BIName)) {
					span = data.get(current.BIName) + span;
					data.put(current.BIName, span);
				} else {
					data.put(current.BIName, span);
				}
			}
		}

		List<PieChartData> result = new ArrayList<PieChartBuilder.PieChartData>();
		for (String key : data.keySet()) {
			result.add(new PieChartData(key, data.get(key)));
		}

		return result;
	}

}
