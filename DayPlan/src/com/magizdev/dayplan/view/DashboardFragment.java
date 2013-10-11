package com.magizdev.dayplan.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

		Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner1);

		ArrayAdapter<CharSequence> mAdapter = ArrayAdapter.createFromResource(
				this.getActivity(), R.array.rangs,
				android.R.layout.simple_spinner_dropdown_item);

		/*
		 * Attach the mLocalAdapter to the spinner.
		 */

		spinner.setAdapter(mAdapter);

		OnItemSelectedListener spinnerListener = new myOnItemSelectedListener(
				this.getActivity(), mAdapter);

		/*
		 * Attach the listener to the Spinner.
		 */

		spinner.setOnItemSelectedListener(spinnerListener);

		LinearLayout pieChartArea = (LinearLayout) rootView
				.findViewById(R.id.pieChartArea);
		LinearLayout barChartArea = (LinearLayout) rootView
				.findViewById(R.id.barChartArea);

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
				// This is all you need to do to 3D flip
				AnimationFactory.flipTransition(viewAnimator,
						FlipDirection.LEFT_RIGHT);
			}

		});

		flipChart2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// This is all you need to do to 3D flip
				AnimationFactory.flipTransition(viewAnimator,
						FlipDirection.LEFT_RIGHT);
			}

		});

		return rootView;
	}

	public class myOnItemSelectedListener implements OnItemSelectedListener {

		/*
		 * provide local instances of the mLocalAdapter and the mLocalContext
		 */

		ArrayAdapter<CharSequence> mLocalAdapter;
		Activity mLocalContext;

		/**
		 * Constructor
		 * 
		 * @param c
		 *            - The activity that displays the Spinner.
		 * @param ad
		 *            - The Adapter view that controls the Spinner. Instantiate
		 *            a new listener object.
		 */
		public myOnItemSelectedListener(Activity c,
				ArrayAdapter<CharSequence> ad) {

			this.mLocalContext = c;
			this.mLocalAdapter = ad;

		}

		/**
		 * When the user selects an item in the spinner, this method is invoked
		 * by the callback chain. Android calls the item selected listener for
		 * the spinner, which invokes the onItemSelected method.
		 * 
		 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView,
		 *      android.view.View, int, long)
		 * @param parent
		 *            - the AdapterView for this listener
		 * @param v
		 *            - the View for this listener
		 * @param pos
		 *            - the 0-based position of the selection in the
		 *            mLocalAdapter
		 * @param row
		 *            - the 0-based row number of the selection in the View
		 */
		public void onItemSelected(AdapterView<?> parent, View v, int pos,
				long row) {

			// SpinnerActivity.this.mPos = pos;
			// SpinnerActivity.this.mSelection = parent.getItemAtPosition(pos)
			// .toString();
			// /*
			// * Set the value of the text field in the UI
			// */
			// TextView resultText = (TextView)
			// findViewById(R.id.SpinnerResult);
			// resultText.setText(SpinnerActivity.this.mSelection);
		}

		/**
		 * The definition of OnItemSelectedListener requires an override of
		 * onNothingSelected(), even though this implementation does not use it.
		 * 
		 * @param parent
		 *            - The View for this Listener
		 */
		public void onNothingSelected(AdapterView<?> parent) {

			// do nothing

		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnLeft) {
			int current = pager.getCurrentItem();
			pager.setCurrentItem(current - 1, true);
		} else if (v.getId() == R.id.btnRight) {
			int current = pager.getCurrentItem();
			if (current < pager.getChildCount() - 1) {
				pager.setCurrentItem(current + 1, true);
			}
		}
	}

}
