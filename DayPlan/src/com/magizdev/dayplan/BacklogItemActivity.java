package com.magizdev.dayplan;

import java.util.List;

import com.magizdev.dayplan.util.DayTaskUtil;
import com.magizdev.dayplan.util.DayUtil;
import com.magizdev.dayplan.viewmodel.BacklogItemAdapter;
import com.magizdev.dayplan.viewmodel.BacklogItemInfo;
import com.magizdev.dayplan.viewmodel.StorageUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

public class BacklogItemActivity extends Activity {
	private DayTaskUtil dayTaskUtil;
	private ListView listView;
	private EditText backlog;
	private StorageUtil<BacklogItemInfo> storageUtil;
	private BacklogItemAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backlog_item);

		listView = (ListView) findViewById(R.id.listViewBacklog);
		backlog = (EditText) findViewById(R.id.editTextBacklog);
		ImageButton addButton = (ImageButton) findViewById(R.id.btnAddBacklog);
		adapter = new BacklogItemAdapter(this);
		listView.setAdapter(adapter);
		dayTaskUtil = new DayTaskUtil(this);
		storageUtil = new StorageUtil<BacklogItemInfo>(this,
				new BacklogItemInfo());
		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				BacklogItemInfo newItem = new BacklogItemInfo(-1, backlog
						.getText().toString(), null);
				storageUtil.add(newItem);
				adapter.refresh();
				adapter.notifyDataSetChanged();
				backlog.getText().clear();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		List<Long> selectedBacklogs = dayTaskUtil.GetTasksByDate(DayUtil
				.Today());
		ListAdapter adapter = listView.getAdapter();
		int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			BacklogItemInfo backlogItemInfo = (BacklogItemInfo) adapter
					.getItem(i);
			if (selectedBacklogs.contains(backlogItemInfo.Id)) {
				backlogItemInfo.Selected = true;
			} else {
				backlogItemInfo.Selected = false;
			}
		}
		listView.invalidate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_backlog_item, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_pickup:
			addTasks();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void addTasks() {
		dayTaskUtil.ClearTasksByDate(DayUtil.Today());
		ListAdapter adapter = listView.getAdapter();
		int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			BacklogItemInfo backlogItemInfo = (BacklogItemInfo) adapter
					.getItem(i);
			if (backlogItemInfo.Selected) {
				dayTaskUtil.AddTask(backlogItemInfo.Id);
			}
		}
		finish();
	}
}
