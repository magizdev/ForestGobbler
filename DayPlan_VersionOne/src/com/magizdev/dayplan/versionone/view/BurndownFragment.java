package com.magizdev.dayplan.versionone.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.versionone.util.AnimationFactory;
import com.magizdev.dayplan.versionone.util.AnimationFactory.FlipDirection;
import com.magizdev.dayplan.versionone.util.BurndownNavigate;
import com.magizdev.dayplan.versionone.util.INavigate;

public class BurndownFragment extends Fragment implements OnClickListener {
	private BurndownNavigate navigate;
	private TextView title;
	private LinearLayout pieChartArea;
	private LinearLayout barChartArea;
	private Spinner backlogsDropdown;
	private BurndownChartView chart;

	public void setDataSource(INavigate naviate) {
		this.navigate = (BurndownNavigate) naviate;
	}

	public BurndownFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_report_burndown, container, false);

//		ImageButton backButton = (ImageButton) rootView
//				.findViewById(R.id.btnLeft);
//		ImageButton forwardButton = (ImageButton) rootView
//				.findViewById(R.id.btnRight);
//		ImageButton flipChart = (ImageButton) rootView
//				.findViewById(R.id.flipButton1);
//		ImageButton flipChart2 = (ImageButton) rootView
//				.findViewById(R.id.flipButton2);

		pieChartArea = (LinearLayout) rootView.findViewById(R.id.pieChartArea);
//		barChartArea = (LinearLayout) rootView.findViewById(R.id.barChartArea);

//		title = (TextView) rootView.findViewById(R.id.reportTitle);
//		title.setText(navigate.CurrentTitle());
		
		chart = new BurndownChartView(navigate, getActivity());
		pieChartArea.addView(chart.GetChart());

//		PieChartView pieChart = new PieChartView(navigate, this.getActivity());
//		pieChartArea.addView(pieChart.GetChart());
//
//		BarChartView barChart = new BarChartView(navigate, this.getActivity());
//		barChartArea.addView(barChart.GetChart());

		backlogsDropdown = (Spinner) rootView
				.findViewById(R.id.dropdownBacklogs);

		final ViewAnimator viewAnimator = (ViewAnimator) rootView
				.findViewById(R.id.viewFlipper);

//		backButton.setOnClickListener(this);
//		forwardButton.setOnClickListener(this);
//		flipChart.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				AnimationFactory.flipTransition(viewAnimator,
//						FlipDirection.LEFT_RIGHT);
//			}
//
//		});
//
//		flipChart2.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				AnimationFactory.flipTransition(viewAnimator,
//						FlipDirection.LEFT_RIGHT);
//			}
//
//		});

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		backlogsDropdown.setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, navigate.getNames()));
		backlogsDropdown.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				navigate.SetPostion(position);
				pieChartArea.removeAllViews();
				pieChartArea.addView(chart.GetChart());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
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
		pieChartArea.addView(new PieChartView(navigate, getActivity())
				.GetChart());
		barChartArea.removeAllViews();
		barChartArea.addView(new BarChartView(navigate, getActivity())
				.GetChart());
	}

}
