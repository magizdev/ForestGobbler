package com.magizdev.dayplan.view;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.magizdev.dayplan.PieChartBuilder.PieChartData;
import com.magizdev.dayplan.R;
import com.magizdev.dayplan.util.DayUtil;
import com.magizdev.dayplan.util.INavigate;

public class DashboardFragment extends Fragment {
	private INavigate navigate;
	private int seriesCount;
	private HashMap<Integer, List<PieChartData>> chartData;
	private int maxY;
	private int startDate;
	private int endDate;
	private DefaultRenderer mPieRenderer = new DefaultRenderer();
	private GraphicalView mPieChartView;
	private CategorySeries mPieSeries = new CategorySeries("");
	private TextView chartTitle;
	private XYMultipleSeriesDataset mBarDataset;
	private XYMultipleSeriesRenderer mBarRenderer;
	private GraphicalView mBarChartView;
	private static int[] COLORS = new int[] { 0xffB2C938, 0xff3BA9B8,
			0xffFF9910, 0xffC74C47, 0xff5B1A69, 0xffA83AAE, 0xffF981C5 };
	private ViewPager pager;

	public void setPager(ViewPager pager) {
		this.pager = pager;
	}

	public void setDataSource(INavigate naviate) {
		this.navigate = naviate;
	}

	public DashboardFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_dashboard, container, false);

		buildChart(rootView);

		return rootView;
	}

	private void buildChart(ViewGroup rootView) {
		ImageButton backButton = (ImageButton) rootView
				.findViewById(R.id.btnLeft);
		ImageButton forwardButton = (ImageButton) rootView
				.findViewById(R.id.btnRight);
		chartTitle = (TextView) rootView.findViewById(R.id.chartTitle);
		LinearLayout barLayout = (LinearLayout) rootView
				.findViewById(R.id.barChart);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int current = pager.getCurrentItem();
				pager.setCurrentItem(current - 1, true);
			}
		});

		forwardButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!navigate.IsLast()) {
					int current = pager.getCurrentItem();
					pager.setCurrentItem(current + 1, true);
				}
			}
		});

		mPieRenderer.setStartAngle(180);
		mPieRenderer.setDisplayValues(true);
		mPieRenderer.setLegendTextSize(30);
		mPieRenderer.setLabelsTextSize(30);
		Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner1);

		// ArrayAdapter<CharSequence> mAdapter =
		// ArrayAdapter.createFromResource(
		// this, R.array.rangs,
		// android.R.layout.simple_spinner_dropdown_item);
		//
		// spinner.setAdapter(mAdapter);
		// spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View view,
		// int position, long id) {
		// if (position == 0) {
		// navigate = new DayNavigate(PieChartBuilder.this);
		//
		// refresh();
		// } else {
		// navigate = new WeekNavigate(PieChartBuilder.this);
		//
		// refresh();
		// }
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> parent) {
		//
		// }
		// });

		LinearLayout layout = (LinearLayout) rootView
				.findViewById(R.id.pieChart);
		mPieChartView = ChartFactory.getPieChartView(this.getActivity(),
				mPieSeries, mPieRenderer);
		layout.addView(mPieChartView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		for (PieChartData pieChartData : navigate.GetPieChartData()) {
			mPieSeries.add(pieChartData.backlogName, pieChartData.data);
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(COLORS[(mPieSeries.getItemCount() - 1)
					% COLORS.length]);
			renderer.setChartValuesTextSize(40);
			mPieRenderer.addSeriesRenderer(renderer);
		}
		mPieChartView.repaint();

		chartTitle.setText(navigate.CurrentTitle());

		mBarDataset = buildBarDataset();
		mBarRenderer = buildBarRenderer();
		mBarChartView = ChartFactory.getBarChartView(this.getActivity(),
				mBarDataset, mBarRenderer, BarChart.Type.DEFAULT);

		barLayout.addView(mBarChartView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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
