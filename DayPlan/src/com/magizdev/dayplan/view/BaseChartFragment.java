package com.magizdev.dayplan.view;

import org.achartengine.GraphicalView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.util.INavigate;

public abstract class BaseChartFragment extends Fragment {
	protected INavigate navigate;

	private GraphicalView graphicalView;
	protected static int[] COLORS = new int[] { 0xffB2C938, 0xff3BA9B8,
			0xffFF9910, 0xffC74C47, 0xff5B1A69, 0xffA83AAE, 0xffF981C5 };
	private ViewGroup rootView;
	private LinearLayout chartArea;

	public void setDataSource(INavigate naviate) {
		this.navigate = naviate;
	}

	public BaseChartFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_chart,
				container, false);

		Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner1);

		Log.w("BaseChartFragment", "onCreateView");
		chartArea = (LinearLayout) rootView.findViewById(R.id.chartArea);

		buildChart();

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.w("BaseChartFragment", "onResume");
		buildChart();
		graphicalView.repaint();
	}

	@Override
	public void onStart() {
		super.onStart();
		buildChart();
		Log.w("BaseChartFragement", "onStart");
	}

	private void buildChart() {

		graphicalView = GetChart();
		chartArea.removeAllViews();
		chartArea.addView(graphicalView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	protected abstract GraphicalView GetChart();

}
