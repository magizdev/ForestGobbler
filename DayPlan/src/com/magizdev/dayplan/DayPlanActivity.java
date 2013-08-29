package com.magizdev.dayplan;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class DayPlanActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_plan);
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_task_edit, menu);
		return true;
	}

}
