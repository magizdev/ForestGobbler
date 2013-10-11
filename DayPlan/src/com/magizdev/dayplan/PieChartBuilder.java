package com.magizdev.dayplan;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.magizdev.dayplan.view.DashboardFragmentAdapter;
import com.magizdev.dayplan.view.ZoomOutPageTransformer;

public class PieChartBuilder extends FragmentActivity implements
		OnItemSelectedListener {

	private ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);

		pager = (ViewPager) findViewById(R.id.pager);

		DashboardFragmentAdapter pagerAdapter = new DashboardFragmentAdapter(
				getSupportFragmentManager(), this, pager, 1, this);
		pager.setAdapter(pagerAdapter);
		pager.setPageTransformer(true, new ZoomOutPageTransformer());
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_dashboard, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		DashboardFragmentAdapter pagerAdapter;
		switch (item.getItemId()) {
		case R.id.action_daily:
			pagerAdapter = new DashboardFragmentAdapter(
					getSupportFragmentManager(), this, pager, 0, this);
			pager.setAdapter(pagerAdapter);
			pager.setPageTransformer(true, new ZoomOutPageTransformer());
			pager.setCurrentItem(pagerAdapter.getCount() - 1);
			return true;
		case R.id.action_weekly:
			pagerAdapter = new DashboardFragmentAdapter(
					getSupportFragmentManager(), this, pager, 1, this);
			pager.setAdapter(pagerAdapter);
			pager.setPageTransformer(true, new ZoomOutPageTransformer());
			pager.setCurrentItem(pagerAdapter.getCount() - 1);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (arg2 == 0) {
			DashboardFragmentAdapter pagerAdapter = new DashboardFragmentAdapter(
					getSupportFragmentManager(), this, pager, 0, this);
			pager.setAdapter(pagerAdapter);
			pager.setPageTransformer(true, new ZoomOutPageTransformer());
			pager.setCurrentItem(pagerAdapter.getCount() - 1);
		} else {
			DashboardFragmentAdapter pagerAdapter = new DashboardFragmentAdapter(
					getSupportFragmentManager(), this, pager, 1, this);
			pager.setAdapter(pagerAdapter);
			pager.setPageTransformer(true, new ZoomOutPageTransformer());
			pager.setCurrentItem(pagerAdapter.getCount() - 1);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

}
