package com.magizdev.babyoneday.view;

import java.text.DecimalFormat;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Pair;

import com.magizdev.babyoneday.util.GrowthIndexUtil;

public class ChartView extends BaseChartView {

	public ChartView(Context context) {
		super(context);
	}

	private XYMultipleSeriesDataset mBarDataset;
	private XYMultipleSeriesRenderer mBarRenderer;
	private GraphicalView chart;
	int minY = 0;

	@Override
	public GraphicalView GetChart(int gender, int indexType) {
		mBarDataset = buildBarDataset(gender, indexType);
		mBarRenderer = buildBarRenderer();
		chart = ChartFactory.getLineChartView(this.context, mBarDataset,
				mBarRenderer);
		return chart;
	}

	private XYMultipleSeriesDataset buildBarDataset(int gender, int indexType) {

		List<Pair<Integer, Float>> lowBound = GrowthIndexUtil.getStandard(
				gender, indexType, GrowthIndexUtil.GI_LOW);
		List<Pair<Integer, Float>> highBound = GrowthIndexUtil.getStandard(
				gender, indexType, GrowthIndexUtil.GI_HEIGH);
		List<Pair<Integer, Float>> profile = GrowthIndexUtil
				.getProfile(indexType);
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		minY = Math.round(lowBound.get(0).second);
		XYSeries lowSeries = new XYSeries("low");
		for (Pair<Integer, Float> data : lowBound) {
			lowSeries.add(data.first * 30, data.second);
		}

		dataset.addSeries(lowSeries);

		XYSeries profileSeries = new XYSeries("profile");
		for(Pair<Integer, Float> data: profile){
			profileSeries.add(data.first, data.second);
		}
		dataset.addSeries(profileSeries);

		XYSeries highSeries = new XYSeries("High");
		for (Pair<Integer, Float> data : highBound) {
			highSeries.add(data.first * 30, data.second);
		}
		dataset.addSeries(highSeries);
		return dataset;
	}

	public void addData(int index, float value) {
		mBarDataset.getSeriesAt(1).add(index, value);
		chart.repaint();
	}

	private XYMultipleSeriesRenderer buildBarRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setLabelsTextSize(15);
		renderer.setBarWidth(20);
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(0x00FAFAFA);
		renderer.setMarginsColor(0x00FAFAFA);
		renderer.setLabelsColor(Color.BLUE);
		renderer.setAxesColor(Color.BLACK);
		renderer.setXLabelsColor(Color.BLACK);
		renderer.setYLabelsColor(0, Color.BLACK);
		renderer.setLegendTextSize(15);
		int length = COLORS.length;
		for (int i = 0; i < 3; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(COLORS[i % length]);
			r.setDisplayChartValues(true);
			r.setChartValuesTextSize(25);
			r.setChartValuesFormat(new DecimalFormat());

			renderer.addSeriesRenderer(r);
		}

		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setPanEnabled(false, false);
		renderer.setYAxisMin(0);
		renderer.setXLabels(0);
		renderer.setYAxisMin(minY);
		renderer.setXAxisMin(0);

		renderer.setZoomEnabled(false);
		renderer.setZoomRate(1f);
		renderer.setBarSpacing(1f);
		return renderer;
	}

}
