package com.magizdev.dayplan.util;

import java.util.List;

import com.magizdev.dayplan.PieChartBuilder.PieChartData;

public interface INavigate {
	void Backword();
	void Forward();
	String CurrentTitle();
	boolean IsLast();
	List<PieChartData> GetPieChartData();
}
