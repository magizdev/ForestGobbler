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
	private static final int STATE_REGULAR_CELL = 1;
	private static final int STATE_SECTIONED_CELL_PAST = 2;
	private static final int STATE_SECTIONED_CELL_INCOMING = 3;
	private static final int STATE_SECTIONED_CELL_FUTURE = 4;

	private List<EasyTaskInfo> tasks;
	private LayoutInflater mInflater;
	private EasyTaskUtil util;
	private ListView listView;
	private Context context;
	private Handler uihHandler;
	private int[] mCellState;

	public TaskListAdapter(Context context, ListView listView, Handler uiHandler) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		util = new EasyTaskUtil(context);
		tasks = util.getTasks();
		this.listView = listView;
		this.uihHandler = uiHandler;
		mCellState = new int[tasks.size()];
	}

	public void removeAt(int index) {
		EasyTaskInfo deleteTask = tasks.get(index);
		util.deleteTask(deleteTask.Id);
		tasks.remove(index);
	}

	public void refresh() {
		tasks = util.getTasks();
		mCellState = new int[tasks.size()];
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
		return false;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Date today = new Date();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(today);
		calendar.add(GregorianCalendar.DAY_OF_YEAR, 7);
		Date weekAfter = calendar.getTime();
		boolean needSeparator = false;

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
		final int tempPosition = position;
		holder.note.setText(tasks.get(position).Title);
		Date startDate = tasks.get(position).StartDate;
		if (startDate.getTime() > 0) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			holder.start_date.setText(format.format(startDate));
		} else {
			holder.notification.setVisibility(View.INVISIBLE);
		}
		switch (mCellState[position]) {
		case STATE_SECTIONED_CELL_PAST:
		case STATE_SECTIONED_CELL_INCOMING:
		case STATE_SECTIONED_CELL_FUTURE:
			needSeparator = true;
			break;

		case STATE_REGULAR_CELL:
			needSeparator = false;
			break;

		default:
			// A separator is needed if it's the first itemview of the
			// ListView or if the group of the current cell is different
			// from the previous itemview.
			if (position == 0) {
				needSeparator = true;
				if (startDate.before(today)) {
					mCellState[position] = STATE_SECTIONED_CELL_PAST;
				} else if (startDate.before(weekAfter)) {
					mCellState[position] = STATE_SECTIONED_CELL_INCOMING;
				} else {
					mCellState[position] = STATE_SECTIONED_CELL_FUTURE;
				}
			} else {
				Date previousTaskDate = tasks.get(position - 1).StartDate;

				if (today.after(previousTaskDate) && today.before(startDate)) {
					mCellState[position] = STATE_SECTIONED_CELL_INCOMING;
					needSeparator = true;
				}else if (weekAfter.after(previousTaskDate)
						&& weekAfter.before(startDate)) {
					mCellState[position] = STATE_SECTIONED_CELL_FUTURE;
					needSeparator = true;
				}else {
					needSeparator = false;
					mCellState[position] = STATE_REGULAR_CELL;
				}
			}
			break;
		}
		
		holder.deleteBtn.setPivotX(0);
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
						TaskListAdapter.this.refresh();
						TaskListAdapter.this.notifyDataSetChanged();
					}
				});
				uihHandler.removeMessages(tempPosition);
				uihHandler.sendEmptyMessage(tempPosition);
				listView.getChildAt(
						tempPosition - listView.getFirstVisiblePosition())
						.startAnimation(anim);
			}
		});

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
		public Button deleteBtn;
		public ImageButton notificationSetter;
	}

}
