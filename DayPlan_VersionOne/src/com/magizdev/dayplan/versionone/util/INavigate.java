package com.magizdev.dayplan.versionone.util;

import java.util.HashMap;
import java.util.List;

import com.magizdev.dayplan.versionone.model.ChartData;


public interface INavigate {
	void Backword();
	void Forward();
	void SetPostion(int offset);
	String CurrentTitle();
	boolean IsLast();
	List<ChartData> GetPieChartData();
	HashMap<Integer, List<ChartData>> GetBarChartData();
}
