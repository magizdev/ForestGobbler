package com.magizdev.dayplan.view;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import com.magizdev.dayplan.PieChartBuilder.PieChartData;

public class PieChartFragment extends BaseChartFragment {
	private DefaultRenderer mPieRenderer;
	private CategorySeries mPieSeries;

	@Override
	protected GraphicalView GetChart() {
		mPieRenderer = new DefaultRenderer();
		mPieSeries = new CategorySeries("");
		mPieRenderer.setStartAngle(180);
		mPieRenderer.setDisplayValues(true);
		mPieRenderer.setLegendTextSize(30);
		mPieRenderer.setLabelsTextSize(30);

		for (PieChartData pieChartData : navigate.GetPieChartData()) {
			mPieSeries.add(pieChartData.backlogName, pieChartData.data);
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(COLORS[(mPieSeries.getItemCount() - 1)
					% COLORS.length]);
			renderer.setChartValuesTextSize(40);
			mPieRenderer.addSeriesRenderer(renderer);

		}

		return ChartFactory.getPieChartView(this.getActivity(), mPieSeries,
				mPieRenderer);

	}

}
