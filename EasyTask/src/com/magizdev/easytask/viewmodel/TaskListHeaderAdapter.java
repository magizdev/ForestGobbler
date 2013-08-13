package com.magizdev.easytask.viewmodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magizdev.easytask.R;
import com.magizdev.easytask.util.HeaderListView;
import com.magizdev.easytask.util.SectionAdapter;

public class TaskListHeaderAdapter extends SectionAdapter {
	private List<EasyTaskInfo> tasksBefore;
	private List<EasyTaskInfo> tasksIncoming;
	private List<EasyTaskInfo> tasksFuture;
	private LayoutInflater mInflater;
	private EasyTaskUtil util;
	private HeaderListView listView;
	private Context context;
	private Handler uihHandler;

	public TaskListHeaderAdapter(Context context, HeaderListView listView,
			Handler uiHandler) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		util = new EasyTaskUtil(context);
		this.tasksBefore = new ArrayList<EasyTaskInfo>();
		this.tasksIncoming = new ArrayList<EasyTaskInfo>();
		this.tasksFuture = new ArrayList<EasyTaskInfo>();
		List<EasyTaskInfo> tasks = util.getTasks();
		spliteTasks(tasks);
		this.listView = listView;
		this.uihHandler = uiHandler;
	}

	private void spliteTasks(List<EasyTaskInfo> tasks) {
		tasksBefore.clear();
		tasksFuture.clear();
		tasksIncoming.clear();

		Date now = new Date();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(now);
		calendar.add(GregorianCalendar.DAY_OF_YEAR, 7);
		Date weekAfter = calendar.getTime();

		for (EasyTaskInfo task : tasks) {
			if (task.StartDate.before(now)) {
				tasksBefore.add(task);
			} else if (task.StartDate.before(weekAfter)) {
				tasksIncoming.add(task);
			} else {
				tasksFuture.add(task);
			}
		}

	}

	public void refresh() {
		List<EasyTaskInfo> tasks = util.getTasks();
		spliteTasks(tasks);
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		super.registerDataSetObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		super.unregisterDataSetObserver(observer);
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return tasksBefore.isEmpty() && tasksIncoming.isEmpty()
				&& tasksFuture.isEmpty();
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
		public TextView separator;
		public TextView note;
		public TextView start_date;
		public LinearLayout notification;
		public Button deleteBtn;
	}
	
	public long getTaskId(int position) {
		int section = getSection(position);
		int row = getRowInSection(position);
		if (row >= 0) {
			EasyTaskInfo taskInfo = (EasyTaskInfo) getRowItem(section, row);
			return taskInfo.Id;
		} else {
			return -1;
		}
	}

	@Override
	public int numberOfSections() {
		return 3;
	}

	@Override
	public int numberOfRows(int section) {
		int retValue = 0;
		switch (section) {
		case 0:
			retValue = tasksBefore.size();
			break;
		case 1:
			retValue = tasksIncoming.size();
			break;
		case 2:
			retValue = tasksFuture.size();
			break;
		default:
			break;
		}
		return retValue;
	}

	@Override
	public View getRowView(int section, int row, View convertView,
			ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.task_item, null);
		}

		ViewHolder holder = new ViewHolder();
		holder.note = (TextView) convertView.findViewById(R.id.note);
		holder.start_date = (TextView) convertView
				.findViewById(R.id.start_date);
		holder.notification = (LinearLayout) convertView
				.findViewById(R.id.notification);
		holder.deleteBtn = (Button) convertView.findViewById(R.id.deleteBtn);

		EasyTaskInfo task = (EasyTaskInfo) getRowItem(section, row);

		holder.note.setText(task.Title);
		Date startDate = task.StartDate;
		if (startDate.getTime() > 0) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			holder.start_date.setText(format.format(startDate));
		} else {
			holder.notification.setVisibility(View.INVISIBLE);
		}
		final long id = task.Id;
		final int tempPosition = getPosition(section, row);
		holder.deleteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Animation anim = AnimationUtils.loadAnimation(context,
						android.R.anim.slide_out_right);
				anim.setDuration(500);
				anim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						util.deleteTask(id);
						TaskListHeaderAdapter.this.refresh();
						TaskListHeaderAdapter.this.notifyDataSetChanged();
					}
				});
				uihHandler.removeMessages(tempPosition);
				uihHandler.sendEmptyMessage(tempPosition);
				listView.getListView()
						.getChildAt(
								tempPosition
										- listView.getListView()
												.getFirstVisiblePosition())
						.startAnimation(anim);
			}
		});

		return convertView;
	}

	private int getPosition(int secion, int row) {
		int position = 0;
		for (int i = 0; i < secion; i++) {
			position += numberOfRows(i) + 1;
		}

		position += 1 + row;
		return position;
	}

	@Override
	public Object getRowItem(int section, int row) {
		Object retValue = null;
		switch (section) {
		case 0:
			retValue = tasksBefore.get(row);
			break;
		case 1:
			retValue = tasksIncoming.get(row);
			break;
		case 2:
			retValue = tasksFuture.get(row);
			break;
		default:
			break;
		}
		return retValue;
	}

	@Override
	public boolean hasSectionHeaderView(int section) {
		if(section < 0 || section >= numberOfSections()){
			return false;
		}
		return true;
	}

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {

		if (convertView == null) {
			convertView = (TextView) mInflater.inflate(
					this.context.getResources().getLayout(
							android.R.layout.simple_list_item_1), null);
		}

		switch (section) {
		case 0:
			((TextView) convertView).setText("Past");
			convertView.setBackgroundColor(this.context.getResources().getColor(
					android.R.color.holo_orange_light));
			break;
		case 1:
			((TextView) convertView).setText("Incoming");
			convertView.setBackgroundColor(this.context.getResources().getColor(
					android.R.color.holo_blue_light));
			break;
		case 2:
			((TextView) convertView).setText("Future");
			convertView.setBackgroundColor(this.context.getResources().getColor(
					android.R.color.holo_green_light));
			break;
		case 3:
			break;
		}
		
		convertView.setAlpha(0.7f);
		((TextView)convertView).setTextSize(20);
		return convertView;
	}

	@Override
	public int getSectionHeaderViewTypeCount() {
		return 1;
	}

}
