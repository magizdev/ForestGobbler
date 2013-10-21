package com.magizdev.dayplan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.magizdev.dayplan.viewmodel.DayTaskAdapter;

public class DayPlanActivity extends NavigationBaseActivity {
	private ListView taskListView;
	private DayTaskAdapter adapter;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			adapter.refresh();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_plan);

		taskListView = (ListView) findViewById(R.id.listViewDayPlan);
		taskListView.setBackgroundResource(R.drawable.widget_bg);
		adapter = new DayTaskAdapter(this);
		taskListView.setAdapter(adapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.refresh();
		handler.sendEmptyMessageDelayed(0, 60000);
	}

	@Override
	public void onPause() {
		super.onPause();
		handler.removeMessages(0);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.activity_day_plan, menu);
//
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle presses on the action bar items
//		Intent intent = new Intent();
//		switch (item.getItemId()) {
//		case R.id.action_mark:
//			intent.setClass(this, BacklogItemActivity.class);
//			startActivity(intent);
//			return true;
//		case R.id.action_report:
//			intent.setClass(this, PieChartBuilder.class);
//			startActivity(intent);
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}

	@Override
	protected int getMyPostion() {
		return 0;
	}

}
