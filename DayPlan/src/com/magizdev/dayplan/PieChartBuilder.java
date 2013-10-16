package com.magizdev.dayplan;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.magizdev.dayplan.util.InAppNavigation;
import com.magizdev.dayplan.view.DashboardFragmentAdapter;
import com.magizdev.dayplan.view.ZoomOutPageTransformer;

public class PieChartBuilder extends FragmentActivity {

	private ViewPager pager;
	private InAppNavigation navigation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);

		pager = (ViewPager) findViewById(R.id.pager);
		LinearLayout adContainer = (LinearLayout) this
				.findViewById(R.id.adContainer);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		ArrayAdapter<CharSequence> mAdapter = ArrayAdapter.createFromResource(
				this, R.array.Pages,
				android.R.layout.simple_spinner_dropdown_item);

		navigation = new InAppNavigation(this);
		actionBar.setListNavigationCallbacks(mAdapter, navigation);
		navigation.setEnable(false);
		actionBar.setSelectedNavigationItem(2);

		AdView adView = new AdView(this, AdSize.BANNER, "a1520fa1c2a3c8f");
		adContainer.addView(adView);
		AdRequest adRequest = new AdRequest();
		adView.loadAd(adRequest);

		DashboardFragmentAdapter pagerAdapter = new DashboardFragmentAdapter(
				getSupportFragmentManager(), this, pager, 1);
		pager.setAdapter(pagerAdapter);
		pager.setPageTransformer(true, new ZoomOutPageTransformer());
		pager.setCurrentItem(pagerAdapter.getCount() - 1);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		navigation.setEnable(true);
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
			pagerAdapter = new DashboardFragmentAdapter(getSupportFragmentManager(),
					this, pager, 1);
			pager.setAdapter(pagerAdapter);
			pager.setPageTransformer(true, new ZoomOutPageTransformer());
			pager.setCurrentItem(pagerAdapter.getCount() - 1);
			return true;
		case R.id.action_weekly:
			pagerAdapter = new DashboardFragmentAdapter(getSupportFragmentManager(),
					this, pager, 0);
			pager.setAdapter(pagerAdapter);
			pager.setPageTransformer(true, new ZoomOutPageTransformer());
			pager.setCurrentItem(pagerAdapter.getCount() - 1);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
