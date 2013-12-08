package com.magizdev.dayplan.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.util.AnimationFactory;
import com.magizdev.dayplan.util.AnimationFactory.FlipDirection;
import com.magizdev.dayplan.util.INavigate;

public class DashboardFragment extends Fragment implements OnClickListener {
	private INavigate navigate;
	private ViewPager pager;

	public void setDataSource(INavigate naviate) {
		this.navigate = naviate;
	}

	public void setPager(ViewPager pager) {
		this.pager = pager;
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

		LinearLayout pieChartArea = (LinearLayout) rootView
				.findViewById(R.id.pieChartArea);
		LinearLayout barChartArea = (LinearLayout) rootView
				.findViewById(R.id.barChartArea);
		
		TextView title = (TextView)rootView.findViewById(R.id.reportTitle);
		title.setText(navigate.CurrentTitle());

		PieChartView pieChart = new PieChartView(navigate, this.getActivity());
		pieChartArea.addView(pieChart.GetChart());

		BarChartView barChart = new BarChartView(navigate, this.getActivity());
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
			int current = pager.getCurrentItem();
			pager.setCurrentItem(current - 1, true);
		} else if (v.getId() == R.id.btnRight) {
			int current = pager.getCurrentItem();
			if (current < pager.getAdapter().getCount() - 1) {
				pager.setCurrentItem(current + 1, true);
			}
		}
	}

}
