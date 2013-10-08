package com.magizdev.dayplan.view;

import org.achartengine.GraphicalView;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.util.INavigate;

public abstract class BaseChartFragment extends Fragment {
	protected INavigate navigate;
	private TextView chartTitle;
	protected static int[] COLORS = new int[] { 0xffB2C938, 0xff3BA9B8,
			0xffFF9910, 0xffC74C47, 0xff5B1A69, 0xffA83AAE, 0xffF981C5 };
	private ViewPager pager;

	public void setPager(ViewPager pager) {
		this.pager = pager;
	}

	public void setDataSource(INavigate naviate) {
		this.navigate = naviate;
	}

	public BaseChartFragment() {

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

		Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner1);

		LinearLayout layout = (LinearLayout) rootView
				.findViewById(R.id.pieChart);
		layout.addView(GetChart(), new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		chartTitle.setText(navigate.CurrentTitle());
	}

	protected abstract GraphicalView GetChart();

}
