package com.magizdev.dayplan;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.magizdev.dayplan.util.DayNavigate;
import com.magizdev.dayplan.util.INavigate;
import com.magizdev.dayplan.util.WeekNavigate;

public class PieChartBuilder extends Activity {

	private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE,
			Color.MAGENTA, Color.CYAN };

	private CategorySeries mSeries = new CategorySeries("");

	private DefaultRenderer mRenderer = new DefaultRenderer();

	private GraphicalView mChartView;
	private TextView chartTitle;
	private INavigate navigate;

	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
		super.onRestoreInstanceState(savedState);
		mSeries = (CategorySeries) savedState.getSerializable("current_series");
		mRenderer = (DefaultRenderer) savedState
				.getSerializable("current_renderer");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("current_series", mSeries);
		outState.putSerializable("current_renderer", mRenderer);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xy_chart);
		ImageButton backButton = (ImageButton) findViewById(R.id.btnLeft);
		ImageButton forwardButton = (ImageButton) findViewById(R.id.btnRight);
		chartTitle = (TextView) findViewById(R.id.chartTitle);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				navigate.Backword();

				refresh();
			}
		});

		forwardButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!navigate.IsLast()) {
					navigate.Forward();

					refresh();
				}
			}
		});
		navigate = new DayNavigate(this);

		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setStartAngle(180);
		mRenderer.setDisplayValues(true);
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);

		ArrayAdapter<CharSequence> mAdapter = ArrayAdapter.createFromResource(
				this, R.array.rangs,
				android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(mAdapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					navigate = new DayNavigate(PieChartBuilder.this);

					refresh();
				} else {
					navigate = new WeekNavigate(PieChartBuilder.this);

					refresh();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		refresh();
		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
			mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
			mRenderer.setClickEnabled(true);
			mChartView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SeriesSelection seriesSelection = mChartView
							.getCurrentSeriesAndPoint();
					if (seriesSelection == null) {
						Toast.makeText(PieChartBuilder.this,
								"No chart element selected", Toast.LENGTH_SHORT)
								.show();
					} else {
						for (int i = 0; i < mSeries.getItemCount(); i++) {
							mRenderer.getSeriesRendererAt(i).setHighlighted(
									i == seriesSelection.getPointIndex());
						}
						mChartView.repaint();
						Toast.makeText(
								PieChartBuilder.this,
								"Chart data point index "
										+ seriesSelection.getPointIndex()
										+ " selected" + " point value="
										+ seriesSelection.getValue(),
								Toast.LENGTH_SHORT).show();
					}
				}
			});
			layout.addView(mChartView, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			mChartView.repaint();
		}
	}

	private void refresh() {
		List<PieChartData> data = navigate.GetPieChartData();
		mSeries.clear();
		mRenderer.removeAllRenderers();
		for (PieChartData pieChartData : data) {
			mSeries.add(pieChartData.backlogName, pieChartData.data);
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(COLORS[(mSeries.getItemCount() - 1)
					% COLORS.length]);
			renderer.setChartValuesTextSize(40);
			mRenderer.addSeriesRenderer(renderer);
		}
		mRenderer.setChartTitle(navigate.CurrentTitle());
		chartTitle.setText(navigate.CurrentTitle());
		if (mChartView != null) {
			mChartView.repaint();
		}
	}

	public static class PieChartData {
		public String backlogName;
		public int data;

		public PieChartData(String backlogName, int data) {
			this.backlogName = backlogName;
			this.data = data;
		}
	}
}
