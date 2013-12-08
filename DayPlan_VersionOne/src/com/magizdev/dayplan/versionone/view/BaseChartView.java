package com.magizdev.dayplan.versionone.view;

import org.achartengine.GraphicalView;

import android.content.Context;

import com.magizdev.dayplan.versionone.util.INavigate;

public abstract class BaseChartView {
	protected INavigate navigate;
	protected Context context;

	protected static int[] COLORS = new int[] { 0xffB2C938, 0xff3BA9B8,
			0xffFF9910, 0xffC74C47, 0xff5B1A69, 0xffA83AAE, 0xffF981C5 };

	public BaseChartView(INavigate navigate, Context context) {
		this.navigate = navigate;
		this.context = context;

	}

	protected abstract GraphicalView GetChart();

}
