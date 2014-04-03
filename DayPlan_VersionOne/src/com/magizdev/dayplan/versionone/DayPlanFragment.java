package com.magizdev.dayplan.versionone;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.versionone.viewmodel.DayTaskAdapter;

public class DayPlanFragment extends MenuFragment {
	private ListView taskListView;
	private DayTaskAdapter adapter;
	private boolean inEditMode;
	private IJumpable jumpable;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			adapter.refresh();
			handler.sendEmptyMessageDelayed(0, 30000);
		}
	};
	
	public void setJumpable(IJumpable jumpable){
		this.jumpable = jumpable;
	}

	public DayPlanFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = super.onCreateView(inflater, container, savedInstanceState);

		taskListView = (ListView) rootView.findViewById(R.id.listViewDayPlan);
		taskListView.setBackgroundResource(R.drawable.widget_bg);
		Button emptyView = (Button)rootView.findViewById(R.id.listViewDayPlanEmpty);
		taskListView.setEmptyView(emptyView);
		emptyView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				jumpable.jumpTo(1);
			}
		});
		adapter = new DayTaskAdapter(getActivity());
		taskListView.setAdapter(adapter);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.setEditMode(inEditMode);
		adapter.refresh();
		handler.sendEmptyMessageDelayed(0, 30000);
	}

	@Override
	public void onPause() {
		super.onPause();
		handler.removeMessages(0);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.activity_day_plan, menu);
	//
	// return super.onCreateOptionsMenu(menu);
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle presses on the action bar items
	// Intent intent = new Intent();
	// switch (item.getItemId()) {
	// case R.id.action_mark:
	// intent.setClass(this, BacklogItemActivity.class);
	// startActivity(intent);
	// return true;
	// case R.id.action_report:
	// intent.setClass(this, PieChartBuilder.class);
	// startActivity(intent);
	// return true;
	// default:
	// return super.onOptionsItemSelected(item);
	// }
	// }

	@Override
	public int optionMenuResource() {
		if (inEditMode) {
			return R.menu.activity_day_plan_close;
		} else {
			return R.menu.activity_backlog_item_plan;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_pickup:
			jumpable.jumpTo(1);
			break;
		case R.id.action_finish:
			this.inEditMode = true;
			adapter.refresh();
			adapter.setEditMode(inEditMode);
			getActivity().invalidateOptionsMenu();
			break;
		case R.id.action_cancel:
			this.inEditMode = false;
			adapter.setEditMode(inEditMode);
			getActivity().invalidateOptionsMenu();
			break;
//		case R.id.action_save:
//			this.inEditMode = false;
//			adapter.save();
//			adapter.setEditMode(inEditMode);
//			getActivity().invalidateOptionsMenu();
		default:
			break;
		}
		return false;
	}

	@Override
	public int layoutResource() {
		return R.layout.activity_day_plan;
	}

}
