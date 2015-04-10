package com.magizdev.easytask.viewmodel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.magizdev.easytask.R;

public class TaskListAdapter extends BaseAdapter {

	private List<EasyTaskInfo> tasks;
	private LayoutInflater mInflater;
	private EasyTaskRepository repository;
	private Context context;

	public TaskListAdapter(Context context, ListView listView) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		repository = new EasyTaskRepository(context);
		tasks = repository.getTasks();
	}

	public void removeAt(int index) {
		EasyTaskInfo deleteTask = tasks.get(index);
		deleteTask.delete();
		tasks.remove(index);
	}

	public void refresh() {
		tasks = repository.getTasks();
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
		return tasks.get(position).getId();
	}

	@Override
	public boolean hasStableIds() {
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
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final long id = tasks.get(position).getId();
		final int tempPosition = position;
		holder.note.setText(tasks.get(position).Title);
		Date startDate = tasks.get(position).NotifyDate;
		if (startDate.getTime() > 0) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			holder.start_date.setText(format.format(startDate));
		} else {
			holder.notification.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return tasks.isEmpty();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	public class ViewHolder {
		public TextView note;
		public TextView start_date;
		public LinearLayout notification;
		public ImageButton notificationSetter;
	}

}
