package com.magizdev.babyoneday.view;

import org.achartengine.GraphicalView;

import android.content.Context;

public abstract class BaseChartView {
	protected Context context;

	protected static int[] COLORS = new int[] { 0xffB2C938, 0xff3BA9B8,
			0xffFF9910, 0xffC74C47, 0xff5B1A69, 0xffA83AAE, 0xffF981C5 };

	public BaseChartView(Context context) {
		this.context = context;

	}

	public abstract GraphicalView GetChart(int gender, int indexType);

}
