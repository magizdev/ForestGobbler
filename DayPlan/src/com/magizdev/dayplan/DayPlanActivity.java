package com.magizdev.dayplan;

import com.magizdev.dayplan.viewmodel.DayTaskAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

public class DayPlanActivity extends Activity {
	private ListView taskListView;
	private DayTaskAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_plan);

		taskListView = (ListView) findViewById(R.id.listViewDayPlan);
		adapter = new DayTaskAdapter(this);
		taskListView.setAdapter(adapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.refresh();
		taskListView.invalidateViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_day_plan, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		Intent intent = new Intent();
		switch (item.getItemId()) {
		case R.id.action_mark:
			intent.setClass(this, BacklogItemActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_report:
			intent.setClass(this, PieChartBuilder.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
