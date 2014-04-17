package com.magizdev.dayplan.versionone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.versionone.util.AnimationFactory;
import com.magizdev.dayplan.versionone.util.AnimationFactory.FlipDirection;
import com.magizdev.dayplan.versionone.util.BurndownNavigate;
import com.magizdev.dayplan.versionone.util.DayNavigate;
import com.magizdev.dayplan.versionone.util.INavigate;
import com.magizdev.dayplan.versionone.util.WeekNavigate;
import com.magizdev.dayplan.versionone.view.BarChart;
import com.magizdev.dayplan.versionone.view.BurndownChart;
import com.magizdev.dayplan.versionone.view.PieChart;

public class ReportFragment extends MenuFragment implements OnClickListener {
	private final static String TAB_DAILY = "view_daily";
	private final static String TAB_WEEKLY = "view_weekly";
	private final static String TAB_BURNDOWN = "view_burndown";

	private TabHost tabHost;
	private View rootView;
	private TextView title;
	private RelativeLayout burndownNavigateArea;
	private RelativeLayout chartNavigateArea;
	private LinearLayout pieChartArea;
	private LinearLayout barChartArea;
	private LinearLayout burndownChartArea;
	private INavigate navigate;
	private Spinner backlogsDropdown;
	private BurndownChart chart;
	private int tabIndex;
	private ViewAnimator viewAnimator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = super.onCreateView(inflater, container, savedInstanceState);
		tabIndex = 0;
		tabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
		setupTabs();

		ImageButton backButton = (ImageButton) rootView
				.findViewById(R.id.btnLeft);
		ImageButton forwardButton = (ImageButton) rootView
				.findViewById(R.id.btnRight);
		ImageButton flipChart = (ImageButton) rootView
				.findViewById(R.id.flipButton1);
		ImageButton flipChart2 = (ImageButton) rootView
				.findViewById(R.id.flipButton2);

		pieChartArea = (LinearLayout) rootView.findViewById(R.id.pieChartArea);
		barChartArea = (LinearLayout) rootView.findViewById(R.id.barChartArea);
		burndownChartArea = (LinearLayout) rootView.findViewById(R.id.burndownChartArea);

		burndownNavigateArea = (RelativeLayout) rootView
				.findViewById(R.id.burndownNavigateArea);
		chartNavigateArea = (RelativeLayout) rootView
				.findViewById(R.id.chartNavigateArea);

		backlogsDropdown = (Spinner) rootView
				.findViewById(R.id.dropdownBacklogs);

		title = (TextView) rootView.findViewById(R.id.reportTitle);

		viewAnimator = (ViewAnimator) rootView
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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				if (tabId == TAB_DAILY) {
					tabIndex = 0;
				} else if (tabId == TAB_WEEKLY) {
					tabIndex = 1;
				} else if (tabId == TAB_BURNDOWN) {
					tabIndex = 2;
				}

				tabHost.setCurrentTab(tabIndex);
				updateTab(tabIndex);
			}
		});
		tabHost.setCurrentTab(tabIndex);
		updateTab(tabIndex);
	}

	@Override
	public void onResume() {
		super.onResume();
		tabHost.setCurrentTab(tabIndex);
		updateTab(tabIndex);
	}

	private void setupTabs() {
		tabHost.setup();
		tabHost.addTab(newTab(TAB_DAILY, R.string.tab_label_daily));
		tabHost.addTab(newTab(TAB_WEEKLY, R.string.tab_label_weekly));
		tabHost.addTab(newTab(TAB_BURNDOWN, R.string.tab_label_burndown));
	}

	private TabSpec newTab(String tag, int labelId) {
		String label = getActivity().getResources().getString(labelId);
		TabSpec tabSpec = tabHost.newTabSpec(tag);
		tabSpec.setIndicator(label);
		tabSpec.setContent(android.R.id.tabcontent);
		return tabSpec;
	}

	private void updateTab(int tabIndex) {
		chartNavigateArea.setVisibility(View.VISIBLE);
		burndownNavigateArea.setVisibility(View.INVISIBLE);
		viewAnimator.setVisibility(View.VISIBLE);
		burndownChartArea.setVisibility(View.INVISIBLE);
		switch (tabIndex) {
		case 0:
			navigate = new DayNavigate(getActivity());
			drawChart();
			break;
		case 1:
			navigate = new WeekNavigate(getActivity());
			drawChart();
			break;
		case 2:
			navigate = new BurndownNavigate(getActivity());
			chart = new BurndownChart(getActivity());
			viewAnimator.setVisibility(View.INVISIBLE);
			chartNavigateArea.setVisibility(View.INVISIBLE);
			burndownNavigateArea.setVisibility(View.VISIBLE);
			burndownChartArea.setVisibility(View.VISIBLE);
			backlogsDropdown.setAdapter(new ArrayAdapter<String>(getActivity(),
					R.layout.spinner_item, ((BurndownNavigate) navigate)
							.getNames()));
			backlogsDropdown
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							navigate.SetPostion(position);
							burndownChartArea.removeAllViews();
							burndownChartArea.addView(chart.GetChart(navigate));
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub

						}
					});
			break;
		default:
			break;
		}
		
	}

	@Override
	public int optionMenuResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int layoutResource() {
		return R.layout.fragement_report_container;
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
		pieChartArea.addView(new PieChart(getActivity()).GetChart(navigate));
		barChartArea.removeAllViews();
		barChartArea.addView(new BarChart(getActivity()).GetChart(navigate));
	}

	private void drawChart() {
		title.setText(navigate.CurrentTitle());
		if (tabIndex != 2) {
			pieChartArea.removeAllViews();
			pieChartArea
					.addView(new PieChart(getActivity()).GetChart(navigate));
		}
		barChartArea.removeAllViews();
		barChartArea.addView(new BarChart(getActivity()).GetChart(navigate));
	}
}