package com.magizdev.easytask.viewmodel;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.magizdev.easytask.R;

public class TaskListAdapter extends BaseAdapter {
	private List<EasyTaskInfo> tasks;
	private LayoutInflater mInflater;
	private EasyTaskUtil util;

	public TaskListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		util = new EasyTaskUtil(context);
		tasks = util.getTasks();
	}

	public void removeAt(int index) {
		EasyTaskInfo deleteTask = tasks.get(index);
		util.deleteTask(deleteTask.Id);
		tasks.remove(index);
	}
	
	public void refresh(){
		tasks=util.getTasks();
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		super.registerDataSetObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		super.unregisterDataSetObserver(observer);
	}

	public int getCount() {
		return tasks.size();
	}

	public Object getItem(int position) {
		return tasks.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.task_item, null);
			holder.note = (TextView) convertView.findViewById(R.id.note);
			holder.start_date = (TextView) convertView
					.findViewById(R.id.start_date);
			holder.create_date = (TextView) convertView
					.findViewById(R.id.create_date);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.note.setText(tasks.get(position).Note);
		holder.create_date.setText(tasks.get(position).CreateDate
				.toLocaleString());
		holder.start_date.setText(tasks.get(position).StartDate
				.toLocaleString());
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return tasks.isEmpty();
	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	public class ViewHolder {
		public TextView note;
		public TextView create_date;
		public TextView start_date;
	}

}
