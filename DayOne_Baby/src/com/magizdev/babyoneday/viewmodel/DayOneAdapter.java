package com.magizdev.babyoneday.viewmodel;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.magizdev.babyoneday.R;
import com.magizdev.babyoneday.util.ActivityUtil;
import com.magizdev.babyoneday.util.DayUtil;

public class DayOneAdapter extends BaseAdapter implements OnClickListener {
	Context context;

	ActivityUtil taskUtil;

	private List<ActivityInfo> activities;
	private int inEditModePosition;
	
	public DayOneAdapter(Context context) {
		this.context = context;
		ActivityInfo blank = new ActivityInfo();
		String whereStrings = "date=" + DayUtil.Today();
		taskUtil = new ActivityUtil(context);

		activities = new Select().from(ActivityInfo.class).where(whereStrings)
				.orderBy("startTime").execute();
		inEditModePosition = -1;
	}

	public void removeAt(int index) {
	}

	public void refresh() {
		String whereStrings = "date=" + DayUtil.Today();
		inEditModePosition = -1;
		activities = new Select().from(ActivityInfo.class).where(whereStrings)
				.orderBy("startTime").execute();
		notifyDataSetChanged();
	}

	public void refresh(Date date) {
		String whereStrings = "date=" + DayUtil.toDate(date);
		inEditModePosition = -1;
		activities = new Select().from(ActivityInfo.class).where(whereStrings)
				.orderBy("startTime").execute();
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
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater
					.inflate(R.layout.activity_item_display, null);
			viewHolder.activityType = (ImageView) convertView
					.findViewById(R.id.imageActivity);
			viewHolder.startTime = (TextView) convertView
					.findViewById(R.id.activityStartTime);
			viewHolder.endTime = (TextView) convertView
					.findViewById(R.id.activityEndTime);
			viewHolder.deletebuButton = (Button) convertView
					.findViewById(R.id.btnDelete);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		int image = R.drawable.ic_action_shuijiao;
		switch ((int) activityInfo.TypeID) {
		case 1:
			image = R.drawable.ic_action_weinai;
			break;
		case 2:
			image = R.drawable.ic_action_shuijiao;
			break;
		case 3:
			image = R.drawable.ic_action_xiaobian;
			break;
		case 4:
			image = R.drawable.ic_action_dabian;
			break;

		default:
			break;
		}
		viewHolder.activityType.setImageResource(image);

		String startTime = formatTime(activityInfo.StartTime);

		viewHolder.startTime.setText(startTime);
		if (activityInfo.timeType == ActivityInfo.TIME_DURATION) {
			viewHolder.endTime.setVisibility(View.GONE);
			viewHolder.startTime.setTextSize(15);
			if (activityInfo.EndTime > 0) {
				viewHolder.endTime.setVisibility(View.VISIBLE);
				viewHolder.endTime.setText(" - "
						+ formatTime(activityInfo.EndTime));
			} else {
				viewHolder.endTime.setText(" - "
						+ formatTime(activityInfo.StartTime));
			}
		} else {
			viewHolder.endTime.setVisibility(View.GONE);
			viewHolder.startTime.setTextSize(25);
		}
		viewHolder.deletebuButton.setVisibility(View.GONE);
		viewHolder.deletebuButton.setTag(activityInfo.getId());
		viewHolder.deletebuButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				long id = (Long) v.getTag();
				ActivityInfo tobeDeleted = ActivityInfo.load(
						ActivityInfo.class, id);
				if (tobeDeleted != null) {
					tobeDeleted.delete();
				}
				refresh();
			}
		});

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
			timeString = "00:" + String.format("%02d", time / 1000 / 60 % 60);
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
		public ImageView activityType;
		public TextView startTime;
		public TextView endTime;
		public Button deletebuButton;
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
			activityInfo.save();
			break;
		case R.id.activityDelete:
			activityInfo.delete();
			break;
		default:
			break;
		}
		refresh();
	}

}
