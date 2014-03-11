package com.magizdev.dayplan.versionone.view;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

import com.magizdev.dayplan.versionone.PieChartData;
import com.magizdev.dayplan.versionone.util.DayUtil;
import com.magizdev.dayplan.versionone.util.INavigate;

public class BurndownChartView extends BaseChartView {

	private int seriesCount;
	private float maxY;
	private HashMap<Integer, List<PieChartData>> chartData;
	private int startDate;
	private int endDate;

	public BurndownChartView(INavigate navigate, Context context) {
		super(navigate, context);
	}

	@Override
	protected GraphicalView GetChart() {

		XYMultipleSeriesDataset dataset = buildDataset();
		XYMultipleSeriesRenderer renderer = buildRenderer();

		return ChartFactory.getLineChartView(context, dataset, renderer);
	}

	private XYMultipleSeriesDataset buildDataset() {
		seriesCount = 0;
		chartData = navigate.GetBarChartData();

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		HashMap<Long, XYSeries> categoryMap = new HashMap<Long, XYSeries>();
		maxY = 0;

		startDate = DayUtil.toDate(new Date());
		endDate = DayUtil.toDate(new Date(0));

		if (chartData.size() == 1) {
			seriesCount = 1;
			int i = 1;
			XYSeries series = new XYSeries("");
			categoryMap.put(1L, series);
			for (Integer j : chartData.keySet()) {
				for (PieChartData data : chartData.get(j)) {
					series.add(i++, data.fdata);
					maxY = data.fdata > maxY ? data.fdata : maxY;
				}
			}

		}

		for (XYSeries series : categoryMap.values()) {
			dataset.addSeries(series);
		}

		return dataset;

	}

	private XYMultipleSeriesRenderer buildRenderer() {
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
		for (int i = 0; i < seriesCount; i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(COLORS[i % length]);
			r.setDisplayChartValues(true);
			if (seriesCount == 1) {
				r.setShowLegendItem(false);
			}
			renderer.addSeriesRenderer(r);
		}

		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setPanEnabled(false, false);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(maxY);
		renderer.setXLabels(0);
		renderer.setXAxisMin(0);
		if (chartData.size() == 1) {
			for (Integer i : chartData.keySet()) {
				List<PieChartData> datas = chartData.get(i);
				renderer.setXAxisMax(datas.size() + 2);

				for (int j = 1; j < datas.size() + 1; j++) {
					renderer.removeXTextLabel(j);
					renderer.addXTextLabel(j, datas.get(j - 1).backlogName);
				}
			}
		} else {
			renderer.setXAxisMax(endDate - startDate + 2);

			for (int i = 1; i < endDate - startDate + 2; i++) {
				Calendar calendar = DayUtil.toCalendar(startDate + i - 1);
				String title = (calendar.get(Calendar.MONTH) + 1) + "/"
						+ calendar.get(Calendar.DAY_OF_MONTH);
				renderer.addXTextLabel(i, title);
			}
		}

		renderer.setZoomEnabled(false);
		renderer.setZoomRate(1f);
		renderer.setBarSpacing(1f);
		return renderer;
	}

}
