package com.magizdev.easytask.viewmodel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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

	public void refreshAt(int index) {
		EasyTaskInfo refreshTask = util.getTask(this.getItemId(index));
	}

	public void refresh() {
		tasks = util.getTasks();
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
		return tasks.get(position).Id;
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
			holder.notification = (LinearLayout) convertView
					.findViewById(R.id.notification);
			holder.deleteBtn = (Button) convertView
					.findViewById(R.id.deleteBtn);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final long id = tasks.get(position).Id;
		holder.note.setText(tasks.get(position).Note);
		Date startDate = tasks.get(position).StartDate;
		if (startDate.getTime() > 0) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			holder.start_date.setText(format.format(startDate));
		} else {
			holder.notification.setVisibility(View.INVISIBLE);
		}
		holder.deleteBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				util.deleteTask(id);
				TaskListAdapter.this.refresh();
				TaskListAdapter.this.notifyDataSetChanged();
			}
		});
		
		if (startDate.getTime() < System.currentTimeMillis()) {
			holder.note.setBackgroundColor(0xFFA0A0A0);
			holder.notification.setBackgroundColor(0xFFA0A0A0);
			holder.start_date.setBackgroundColor(0xFFA0A0A0);
		} else {
			holder.note.setBackgroundColor(0xFFFFFFFF);
			holder.notification.setBackgroundColor(0xFFFFFFFF);
			holder.start_date.setBackgroundColor(0xFFFFFFFF);
		}
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
		return true;
	}

	public class ViewHolder {
		public TextView note;
		public TextView start_date;
		public LinearLayout notification;
		public Button deleteBtn;
	}

}
