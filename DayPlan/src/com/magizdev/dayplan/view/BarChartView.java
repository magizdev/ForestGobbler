package com.magizdev.dayplan.view;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Paint.Align;
import android.util.Log;

import com.magizdev.dayplan.PieChartBuilder.PieChartData;
import com.magizdev.dayplan.util.DayUtil;
import com.magizdev.dayplan.util.INavigate;

public class BarChartView extends BaseChartView {

	public BarChartView(INavigate navigate, Context context) {
		super(navigate, context);
	}

	private int seriesCount;
	private HashMap<Integer, List<PieChartData>> chartData;
	private int maxY;
	private int startDate;
	private int endDate;
	private XYMultipleSeriesDataset mBarDataset;
	private XYMultipleSeriesRenderer mBarRenderer;

	@Override
	protected GraphicalView GetChart() {
		mBarDataset = buildBarDataset();
		mBarRenderer = buildBarRenderer();
		return ChartFactory.getBarChartView(this.context, mBarDataset,
				mBarRenderer, BarChart.Type.DEFAULT);
	}

	private XYMultipleSeriesDataset buildBarDataset() {
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
					series.add(i++, data.data);
					maxY = data.data > maxY ? data.data : maxY;
				}
			}

		} else {
			for (Integer date : chartData.keySet()) {
				if (startDate > date) {
					startDate = date;
				}
				if (endDate < date) {
					endDate = date;
				}
			}
			for (Integer date : chartData.keySet()) {
				Log.w("a", date + "");
				for (PieChartData data : chartData.get(date)) {
					if (categoryMap.containsKey(data.biid)) {
						categoryMap.get(data.biid).add(date - startDate + 1,
								data.data);
						Log.w("c", data.data + "");
					} else {
						XYSeries series = new XYSeries(data.backlogName);
						for (Integer tmpDate = startDate; tmpDate <= endDate; tmpDate++) {
							series.add(tmpDate - startDate + 1, 0);
						}
						Log.w("b", data.data + "");
						series.add(date - startDate + 1, data.data);
						categoryMap.put(data.biid, series);
						seriesCount++;
					}

					maxY = data.data > maxY ? data.data : maxY;
				}
			}
			maxY *= 1.2;
		}

		for (XYSeries series : categoryMap.values()) {
			dataset.addSeries(series);
		}

		return dataset;

	}

	private XYMultipleSeriesRenderer buildBarRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setBarWidth(20);
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(0x00FAFAFA);
		renderer.setMarginsColor(0x00FAFAFA);
		renderer.setLegendTextSize(15);
		renderer.setChartTitle(navigate.CurrentTitle());
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
