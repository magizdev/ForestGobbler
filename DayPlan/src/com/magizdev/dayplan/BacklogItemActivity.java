package com.magizdev.dayplan;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;

import com.magizdev.dayplan.util.DayTaskUtil;
import com.magizdev.dayplan.util.DayUtil;
import com.magizdev.dayplan.viewmodel.BacklogItemAdapter;
import com.magizdev.dayplan.viewmodel.BacklogItemInfo;
import com.magizdev.dayplan.viewmodel.StorageUtil;

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
		Switch showAll = (Switch) findViewById(R.id.switchShowAll);
		showAll.setChecked(false);
		showAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				BacklogItemAdapter adapter = (BacklogItemAdapter) listView
						.getAdapter();
				adapter.setShowAll(isChecked);
			}
		});

		dayTaskUtil = new DayTaskUtil(this);
		storageUtil = new StorageUtil<BacklogItemInfo>(this,
				new BacklogItemInfo());
		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (backlog.getText().toString().length() > 0) {
					BacklogItemInfo newItem = new BacklogItemInfo(-1, backlog
							.getText().toString(), null, false);
					storageUtil.add(newItem);
					adapter.refresh();
					adapter.notifyDataSetChanged();
					backlog.getText().clear();
				}
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				long backlogId = ((BacklogItemAdapter) listView.getAdapter())
						.getItemId(arg2);
				if (backlogId > -1) {
					Intent editTaskIntent = new Intent(
							BacklogItemActivity.this, BacklogEditActivity.class);
					editTaskIntent.putExtra("backlogId", backlogId);

					startActivityForResult(editTaskIntent, 1);
				}
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		List<Long> selectedBacklogs = dayTaskUtil.GetTasksByDate(DayUtil
				.Today());
		adapter = (BacklogItemAdapter) listView.getAdapter();
		if (adapter != null) {
			adapter.setSelected(selectedBacklogs);
		} else {
			adapter = new BacklogItemAdapter(this, selectedBacklogs);
			listView.setAdapter(adapter);
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
