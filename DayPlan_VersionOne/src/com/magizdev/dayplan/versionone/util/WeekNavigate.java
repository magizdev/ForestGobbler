package com.magizdev.dayplan.versionone.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.magizdev.dayplan.versionone.PieChartBuilder.PieChartData;
import com.magizdev.dayplan.versionone.viewmodel.DayTaskTimeInfo;

public class WeekNavigate implements INavigate {
	private int currentWeekOfYear;
	private int currentWeekStartDay;
	private int nowWeekOfYear;
	private int nowWeekStartDay;
	private int today;
	private DayTaskTimeUtil util;

	private int currentWeekEndDay() {
		if (currentWeekOfYear == nowWeekOfYear) {
			return today + 1;
		} else {
			return currentWeekStartDay + 7;
		}
	}

	public WeekNavigate(Context context) {
		util = new DayTaskTimeUtil(context);
		today = DayUtil.Today();
		Calendar cToday = DayUtil.toCalendar(today);
		nowWeekOfYear = cToday.get(Calendar.WEEK_OF_YEAR);
		currentWeekOfYear = nowWeekOfYear;
		cToday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		currentWeekStartDay = DayUtil.toDate(cToday.getTime());
		nowWeekStartDay = currentWeekStartDay;
	}

	@Override
	public void Backword() {
		currentWeekOfYear--;
		currentWeekStartDay -= 7;
	}

	@Override
	public void Forward() {
		if (!IsLast()) {
			currentWeekOfYear++;
			currentWeekStartDay += 7;
		}

	}

	@Override
	public String CurrentTitle() {
		Calendar startDay = DayUtil.toCalendar(currentWeekStartDay);
		Calendar endDay = DayUtil.toCalendar(currentWeekEndDay() - 1);
		return startDay.get(Calendar.YEAR) + "-"
				+ (startDay.get(Calendar.MONTH) + 1) + "-"
				+ startDay.get(Calendar.DAY_OF_MONTH) + " -- "
				+ endDay.get(Calendar.YEAR) + "-"
				+ (endDay.get(Calendar.MONTH) + 1) + "-"
				+ endDay.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	public boolean IsLast() {
		return currentWeekOfYear == nowWeekOfYear;
	}

	@Override
	public List<PieChartData> GetPieChartData() {
		List<DayTaskTimeInfo> data = util.GetByDateRange(currentWeekStartDay,
				currentWeekEndDay());
		return DayTaskTimeUtil.compute(data);
	}

	@Override
	public HashMap<Integer, List<PieChartData>> GetBarChartData() {
		List<DayTaskTimeInfo> data = util.GetByDateRange(currentWeekStartDay,
				currentWeekEndDay());

		HashMap<Integer, List<PieChartData>> chartDatas = DayTaskTimeUtil
				.computeBarData(data);
		return chartDatas;
	}

	@Override
	public void SetPostion(int offset) {
		currentWeekOfYear = nowWeekOfYear + offset;
		currentWeekStartDay = nowWeekStartDay + offset * 7;

	}
}
