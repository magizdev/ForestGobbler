package com.magizdev.dayplan.versionone;

import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.versionone.util.DayTaskUtil;
import com.magizdev.dayplan.versionone.util.DayUtil;
import com.magizdev.dayplan.versionone.viewmodel.BacklogItemAdapter;
import com.magizdev.dayplan.versionone.viewmodel.BacklogItemInfo;
import com.magizdev.dayplan.versionone.viewmodel.StorageUtil;

public class BacklogItemFragment extends MenuFragment {
	private DayTaskUtil dayTaskUtil;
	private ListView listView;
	private EditText backlog;
	private StorageUtil<BacklogItemInfo> storageUtil;
	private BacklogItemAdapter adapter;
	private IJumpable jumpable;
	private Switch showAll;

	public BacklogItemFragment() {
	}

	public void setJumpable(IJumpable jumpable) {
		this.jumpable = jumpable;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container,
				savedInstanceState);

		listView = (ListView) rootView.findViewById(R.id.listViewBacklog);
		backlog = (EditText) rootView.findViewById(R.id.editTextBacklog);
		ImageButton addButton = (ImageButton) rootView
				.findViewById(R.id.btnAddBacklog);
		showAll = (Switch) rootView.findViewById(R.id.switchShowAll);
		showAll.setChecked(false);
		showAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (adapter == null) {
					List<Long> selectedBacklogs = dayTaskUtil
							.GetTasksByDate(DayUtil.Today());
					adapter = new BacklogItemAdapter(getActivity(),
							selectedBacklogs);
					listView.setAdapter(adapter);
				}
				adapter.setShowAll(isChecked);
				adapter.notifyDataSetChanged();
			}
		});

		dayTaskUtil = new DayTaskUtil(getActivity());
		storageUtil = new StorageUtil<BacklogItemInfo>(getActivity(),
				new BacklogItemInfo());
		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (backlog.getText().toString().length() > 0) {
					BacklogItemInfo newItem = new BacklogItemInfo(-1, backlog
							.getText().toString(), null, false, -1, -1);
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
					Intent editTaskIntent = new Intent(getActivity(),
							BacklogEditActivity.class);
					editTaskIntent.putExtra("backlogId", backlogId);

					startActivityForResult(editTaskIntent, 1);
				}
			}
		});

		return rootView;
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
			adapter = new BacklogItemAdapter(getActivity(), selectedBacklogs);
			listView.setAdapter(adapter);
		}
		adapter.setShowAll(showAll.isChecked());
		listView.invalidate();
	}

	public void addTasks() {
		List<Long> ids = dayTaskUtil.GetTasksByDate(DayUtil.Today());
		dayTaskUtil.ClearTasksByDate(DayUtil.Today());
		ListAdapter adapter = listView.getAdapter();
		int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			BacklogItemInfo backlogItemInfo = (BacklogItemInfo) adapter
					.getItem(i);
			if (backlogItemInfo.Selected) {
				dayTaskUtil.AddTask(backlogItemInfo.Id);
			} else {
				if (ids.contains(backlogItemInfo.Id)
						&& dayTaskUtil.IsTaskWaitingForStop(backlogItemInfo.Id)) {
					dayTaskUtil.StopTask(backlogItemInfo.Id);
				}
			}
		}
	}

	@Override
	public int optionMenuResource() {
		return R.menu.activity_backlog_item;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_pickup:
			addTasks();
			jumpable.jumpTo(0);
			break;

		default:
			break;
		}
		return false;
	}

	@Override
	public int layoutResource() {
		return R.layout.activity_backlog_item;
	}
}
