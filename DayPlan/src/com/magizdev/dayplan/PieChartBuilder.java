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
import com.magizdev.dayplan.view.ZoomOutPageTransformer;

public class PieChartBuilder extends FragmentActivity {

	private ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);

		pager = (ViewPager) findViewById(R.id.pager);
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);

		DashboardFragmentAdapter pagerAdapter = new DashboardFragmentAdapter(
				getSupportFragmentManager(), this, pager);
		pager.setAdapter(pagerAdapter);
		pager.setPageTransformer(true, new ZoomOutPageTransformer());
		pager.setOffscreenPageLimit(0);
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
