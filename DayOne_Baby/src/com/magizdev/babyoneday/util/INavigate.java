package com.magizdev.babyoneday.util;

import java.util.HashMap;
import java.util.List;

import com.magizdev.babyoneday.ChartFragment.PieChartData;


public interface INavigate {
	void Backword();
	void Forward();
	void SetPostion(int offset);
	String CurrentTitle();
	boolean IsLast();
	List<PieChartData> GetPieChartData();
	HashMap<Integer, List<PieChartData>> GetBarChartData();
}
