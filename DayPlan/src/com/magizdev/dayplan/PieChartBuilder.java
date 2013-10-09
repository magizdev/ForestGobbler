package com.magizdev.dayplan;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.magizdev.dayplan.util.INavigate;
import com.magizdev.dayplan.util.WeekNavigate;
import com.magizdev.dayplan.view.DashboardFragmentAdapter;
import com.magizdev.dayplan.view.DepthPageTransformer;

public class PieChartBuilder extends FragmentActivity {

	private INavigate navigate;
	private ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		ImageButton backButton = (ImageButton) findViewById(R.id.btnLeft);
		ImageButton forwardButton = (ImageButton) findViewById(R.id.btnRight);

		pager = (ViewPager) findViewById(R.id.pager);
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

		Spinner spinner = (Spinner) findViewById(R.id.spinner1);

		navigate = new WeekNavigate(this);
		DashboardFragmentAdapter pagerAdapter = new DashboardFragmentAdapter(
				getSupportFragmentManager(), this);
		pager.setAdapter(pagerAdapter);
		pager.setPageTransformer(true, new DepthPageTransformer());
		pager.setCurrentItem(pagerAdapter.getCount() - 1);
	}

	public static class PieChartData {
		public long biid;
		public String backlogName;
		public int data;

		public PieChartData(long biid, String backlogName, int data) {
			this.biid = biid;
			this.backlogName = backlogName;
			this.data = data;
		}
	}

}
