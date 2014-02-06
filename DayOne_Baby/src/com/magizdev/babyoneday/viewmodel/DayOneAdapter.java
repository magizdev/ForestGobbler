package com.magizdev.babyoneday.viewmodel;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.magizdev.babyoneday.R;
import com.magizdev.babyoneday.store.DayPlanMetaData.ActivityTable;
import com.magizdev.babyoneday.util.ActivityUtil;
import com.magizdev.babyoneday.util.DayUtil;
import com.magizdev.babyoneday.viewmodel.ActivityTypeInfo.TimeType;

public class DayOneAdapter extends BaseAdapter implements OnClickListener {
	Context context;
	StorageUtil<ActivityInfo> storageUtil;
	ActivityUtil taskUtil;
	private StorageUtil<ActivityInfo> timeUtil;
	private List<ActivityInfo> activities;
	private int inEditModePosition;

	public DayOneAdapter(Context context) {
		this.context = context;
		ActivityInfo blank = new ActivityInfo();
		storageUtil = new StorageUtil<ActivityInfo>(context, blank);
		String whereStrings = ActivityTable.DATE + "=" + DayUtil.Today();
		taskUtil = new ActivityUtil(context);

		timeUtil = new StorageUtil<ActivityInfo>(context, new ActivityInfo());
		activities = timeUtil.getCollection(whereStrings);
		Collections.reverse(activities);
		inEditModePosition = -1;
	}

	public void removeAt(int index) {
	}

	public void refresh() {
		String whereStrings = ActivityTable.DATE + "=" + DayUtil.Today();
		inEditModePosition = -1;
		activities = timeUtil.getCollection(whereStrings);
		Collections.reverse(activities);
		notifyDataSetChanged();
	}

	public void refresh(Date date) {
		String whereStrings = ActivityTable.DATE + "=" + DayUtil.toDate(date);
		inEditModePosition = -1;
		activities = timeUtil.getCollection(whereStrings);
		Collections.reverse(activities);
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
		return activities.size();
	}

	public Object getItem(int position) {
		return activities.get(position);

	}

	public long getItemId(int position) {
		return activities.get(position).ID;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	public void setEditMode(int position) {
		inEditModePosition = position;
		notifyDataSetChanged();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		ActivityInfo activityInfo = activities.get(position);
		if (position == inEditModePosition) {

			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.activity_item_edit, null);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.activityName);
			viewHolder.startTime = (TextView) convertView
					.findViewById(R.id.activityStartTime);
			viewHolder.endTime = (TextView) convertView
					.findViewById(R.id.activityEndTime);
			viewHolder.dataEdit = (EditText) convertView
					.findViewById(R.id.activityDataEdit);
			viewHolder.noteEdit = (EditText) convertView
					.findViewById(R.id.activityNoteEdit);
			viewHolder.cancelButton = (Button) convertView
					.findViewById(R.id.activityCancel);
			viewHolder.deletebuButton = (Button) convertView
					.findViewById(R.id.activityDelete);
			viewHolder.saveButton = (Button) convertView
					.findViewById(R.id.activitySave);
			viewHolder.cancelButton.setOnClickListener(this);
			viewHolder.deletebuButton.setOnClickListener(this);
			viewHolder.saveButton.setOnClickListener(this);
			viewHolder.saveButton.setTag(convertView);

			String startTime = formatTime(activityInfo.StartTime);

			viewHolder.startTime.setText(startTime);
			if (activityInfo.timeType == TimeType.Duration) {
				viewHolder.endTime.setVisibility(View.VISIBLE);
				viewHolder.startTime.setTextSize(15);
				if (activityInfo.EndTime > 0) {
					viewHolder.endTime.setText("-- "
							+ formatTime(activityInfo.EndTime));
				} else {
					viewHolder.endTime.setText("--");
				}
			} else {
				viewHolder.endTime.setVisibility(View.GONE);
				viewHolder.startTime.setTextSize(25);
			}
			viewHolder.name.setText(activityInfo.Name);
			viewHolder.dataEdit.setText(activityInfo.Data + "");
			viewHolder.noteEdit.setText(activityInfo.Note);
		} else {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.activity_item_display, null);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.activityName);
			viewHolder.startTime = (TextView) convertView
					.findViewById(R.id.activityStartTime);
			viewHolder.endTime = (TextView) convertView
					.findViewById(R.id.activityEndTime);
			viewHolder.data = (TextView) convertView
					.findViewById(R.id.activityData);
			viewHolder.note = (TextView) convertView
					.findViewById(R.id.activityNote);
			convertView.setTag(viewHolder);

			String startTime = formatTime(activityInfo.StartTime);

			viewHolder.startTime.setText(startTime);
			if (activityInfo.timeType == TimeType.Duration) {
				viewHolder.endTime.setVisibility(View.VISIBLE);
				viewHolder.startTime.setTextSize(15);
				if (activityInfo.EndTime > 0) {
					viewHolder.endTime.setText("-- "
							+ formatTime(activityInfo.EndTime));
				} else {
					viewHolder.endTime.setText("--");
				}
			} else {
				viewHolder.endTime.setVisibility(View.GONE);
				viewHolder.startTime.setTextSize(25);
			}
			viewHolder.name.setText(activityInfo.Name);
			if (activityInfo.Data > 0) {
				viewHolder.data.setText(activityInfo.Data + "");
			}
			viewHolder.note.setText(activityInfo.Note);
		}

		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	private String formatTime(int time) {
		String timeString = "";
		if (time / 1000 / 60 / 60 > 0) {
			timeString = time / 1000 / 60 / 60 + ":"
					+ String.format("%02d", time / 1000 / 60 % 60);
		} else {
			timeString = "00:" + String.format("%2d", time / 1000 / 60 % 60);
		}

		return timeString;
	}

	@Override
	public boolean isEmpty() {
		return activities.size() == 0;
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
		public TextView startTime;
		public TextView endTime;
		public TextView data;
		public EditText dataEdit;
		public TextView note;
		public EditText noteEdit;
		public Button deletebuButton;
		public Button cancelButton;
		public Button saveButton;
	}

	@Override
	public void onClick(View v) {
		ActivityInfo activityInfo = activities.get(inEditModePosition);
		switch (v.getId()) {
		case R.id.activityCancel:

			break;
		case R.id.activitySave:
			View activityItemView = (View) v.getTag();
			EditText note = (EditText) activityItemView
					.findViewById(R.id.activityNoteEdit);
			EditText data = (EditText) activityItemView
					.findViewById(R.id.activityDataEdit);
			activityInfo.Note = note.getText().toString();
			activityInfo.Data = Float.parseFloat(data.getText().toString());
			timeUtil.update(activityInfo.ID, activityInfo);
			break;
		case R.id.activityDelete:
			timeUtil.delete(activityInfo.ID);
			break;
		default:
			break;
		}
		refresh();
	}

}
