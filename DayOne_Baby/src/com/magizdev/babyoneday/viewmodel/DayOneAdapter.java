package com.magizdev.babyoneday.viewmodel;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.magizdev.babyoneday.R;
import com.magizdev.babyoneday.store.DayPlanMetaData.DayTaskTable;
import com.magizdev.babyoneday.util.DayTaskUtil;
import com.magizdev.babyoneday.util.DayUtil;
import com.magizdev.babyoneday.viewmodel.DayTaskTimeInfo.TimeType;

public class DayOneAdapter extends BaseAdapter {
	Context context;
	StorageUtil<DayTaskInfo> storageUtil;
	DayTaskUtil taskUtil;
	private StorageUtil<DayTaskTimeInfo> timeUtil;
	private List<DayTaskTimeInfo> times;

	public DayOneAdapter(Context context) {
		this.context = context;
		DayTaskInfo blank = new DayTaskInfo();
		storageUtil = new StorageUtil<DayTaskInfo>(context, blank);
		String whereStrings = DayTaskTable.DATE + "=" + DayUtil.Today();
		taskUtil = new DayTaskUtil(context);

		timeUtil = new StorageUtil<DayTaskTimeInfo>(context,
				new DayTaskTimeInfo());
		times = timeUtil.getCollection(whereStrings);
		Collections.reverse(times);
	}

	public void removeAt(int index) {
	}

	public void refresh() {
		String whereStrings = DayTaskTable.DATE + "=" + DayUtil.Today();

		times = timeUtil.getCollection(whereStrings);
		Collections.reverse(times);
		notifyDataSetChanged();
	}

	public void refresh(Date date) {
		String whereStrings = DayTaskTable.DATE + "=" + DayUtil.toDate(date);

		times = timeUtil.getCollection(whereStrings);
		Collections.reverse(times);
		notifyDataSetChanged();
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
		return times.size();
	}

	public Object getItem(int position) {
		return times.get(position);

	}

	public long getItemId(int position) {
		return times.get(position).ID;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		DayTaskTimeInfo timeInfo = times.get(position);

		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.backlog_item_display, null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.tVName1);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.taskTimeTextView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.time.setText(formatTime(timeInfo.Time));
		TimeType state = timeInfo.timeType;
		String textString = timeInfo.BIName;
		if (state == TimeType.Start) {
			textString += " "
					+ context.getResources().getString(R.string.kaishi);
		} else if (state == TimeType.Stop) {
			textString += " "
					+ context.getResources().getString(R.string.jieshu);
		}
		viewHolder.name.setText(textString);
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	private String formatTime(int time) {
		String timeString = "";
		if (time / 1000 / 60 / 60 > 0) {
			timeString = time / 1000 / 60 / 60 + ":" + time / 1000 / 60 % 60;
		} else {
			timeString = "00 :" + time / 1000 / 60 % 60;
		}

		return timeString;
	}

	@Override
	public boolean isEmpty() {
		return times.size() == 0;
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
		public TextView name;
		public TextView time;

		// public Button deleteBtn;
		// public ImageButton notificationSetter;
	}

}
