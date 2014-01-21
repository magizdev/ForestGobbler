package com.magizdev.babyoneday;

import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.magizdev.babyoneday.util.DayTaskUtil;
import com.magizdev.babyoneday.util.DayUtil;
import com.magizdev.babyoneday.viewmodel.BacklogItemAdapter;
import com.magizdev.babyoneday.viewmodel.BacklogItemInfo;
import com.magizdev.babyoneday.viewmodel.DayTaskTimeInfo.TimeType;
import com.magizdev.babyoneday.viewmodel.StorageUtil;

public class BacklogItemFragment extends Fragment {
	private DayTaskUtil dayTaskUtil;
	private ListView listView;
	private EditText backlog;
	private StorageUtil<BacklogItemInfo> storageUtil;
	private BacklogItemAdapter adapter;
	
	public BacklogItemFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_backlog_item, container, false);

		listView = (ListView) rootView.findViewById(R.id.listViewBacklog);
		backlog = (EditText) rootView.findViewById(R.id.editTextBacklog);
		ImageButton addButton = (ImageButton) rootView.findViewById(R.id.btnAddBacklog);
		Switch showAll = (Switch) rootView.findViewById(R.id.switchShowAll);
		showAll.setChecked(false);
		showAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				BacklogItemAdapter adapter = (BacklogItemAdapter) listView
						.getAdapter();
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
							getActivity(), BacklogEditActivity.class);
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
		listView.invalidate();
	}


	private void addTasks() {
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
						&& dayTaskUtil.GetTaskState(backlogItemInfo.Id) == TimeType.Start) {
					dayTaskUtil.StopTask(backlogItemInfo.Id);
				}
			}
		}
		getActivity().finish();
	}
}
