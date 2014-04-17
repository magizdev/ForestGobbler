package com.magizdev.dayplan.versionone.view;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;

import com.magizdev.dayplan.versionone.model.ChartData;
import com.magizdev.dayplan.versionone.util.INavigate;

public class PieChart extends BaseChart {
	public PieChart(Context context) {
		super(context);
	}

	private DefaultRenderer mPieRenderer;
	private CategorySeries mPieSeries;

	@Override
	public GraphicalView GetChart(INavigate navigate) {
		mPieRenderer = new DefaultRenderer();
		mPieSeries = new CategorySeries("");
		mPieRenderer.setStartAngle(180);
		mPieRenderer.setDisplayValues(true);
		mPieRenderer.setLegendTextSize(30);
		mPieRenderer.setLabelsTextSize(30);
		mPieRenderer.setLabelsColor(Color.BLUE);

		for (ChartData pieChartData : navigate.GetPieChartData()) {
			mPieSeries.add(pieChartData.backlogName, pieChartData.data);
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(COLORS[(mPieSeries.getItemCount() - 1)
					% COLORS.length]);
			renderer.setChartValuesTextSize(40);
			mPieRenderer.addSeriesRenderer(renderer);

		}

		return ChartFactory.getPieChartView(this.context, mPieSeries,
				mPieRenderer);

	}

}
