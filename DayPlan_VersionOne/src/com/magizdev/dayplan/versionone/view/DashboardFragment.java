package com.magizdev.dayplan.versionone.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.versionone.util.AnimationFactory;
import com.magizdev.dayplan.versionone.util.AnimationFactory.FlipDirection;
import com.magizdev.dayplan.versionone.util.INavigate;

public class DashboardFragment extends Fragment implements OnClickListener {
	private INavigate navigate;
	private TextView title;
	private LinearLayout pieChartArea;
	private LinearLayout barChartArea;

	public void setDataSource(INavigate naviate) {
		this.navigate = naviate;
	}

	public DashboardFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_report, container, false);

		ImageButton backButton = (ImageButton) rootView
				.findViewById(R.id.btnLeft);
		ImageButton forwardButton = (ImageButton) rootView
				.findViewById(R.id.btnRight);
		ImageButton flipChart = (ImageButton) rootView
				.findViewById(R.id.flipButton1);
		ImageButton flipChart2 = (ImageButton) rootView
				.findViewById(R.id.flipButton2);

		pieChartArea = (LinearLayout) rootView
				.findViewById(R.id.pieChartArea);
		barChartArea = (LinearLayout) rootView
				.findViewById(R.id.barChartArea);

		title = (TextView) rootView.findViewById(R.id.reportTitle);
		title.setText(navigate.CurrentTitle());

		PieChart pieChart = new PieChart(navigate, this.getActivity());
		pieChartArea.addView(pieChart.GetChart());

		BarChart barChart = new BarChart(navigate, this.getActivity());
		barChartArea.addView(barChart.GetChart());

		final ViewAnimator viewAnimator = (ViewAnimator) rootView
				.findViewById(R.id.viewFlipper);

		backButton.setOnClickListener(this);
		forwardButton.setOnClickListener(this);
		flipChart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AnimationFactory.flipTransition(viewAnimator,
						FlipDirection.LEFT_RIGHT);
			}

		});

		flipChart2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AnimationFactory.flipTransition(viewAnimator,
						FlipDirection.LEFT_RIGHT);
			}

		});

		return rootView;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnLeft) {
			navigate.Backword();
		} else if (v.getId() == R.id.btnRight) {
			if (!navigate.IsLast()) {
				navigate.Forward();
			}
		}
		
		title.setText(navigate.CurrentTitle());
		pieChartArea.removeAllViews();
		pieChartArea.addView(new PieChart(navigate, getActivity()).GetChart());
		barChartArea.removeAllViews();
		barChartArea.addView(new BarChart(navigate, getActivity()).GetChart());
	}

}
