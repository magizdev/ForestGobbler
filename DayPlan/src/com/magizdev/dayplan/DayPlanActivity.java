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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_plan);
		
		ListView taskListView = (ListView)findViewById(R.id.listViewDayPlan);
		DayTaskAdapter adapter = new DayTaskAdapter(this);
		taskListView.setAdapter(adapter);

	}

	@Override
	public void onResume() {
		super.onResume();
		

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
	    switch (item.getItemId()) {
	        case R.id.action_pickup:
	            Intent intent = new Intent();
	            intent.setClass(this, BacklogItemActivity.class);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
