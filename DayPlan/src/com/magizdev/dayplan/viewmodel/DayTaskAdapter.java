package com.magizdev.dayplan.viewmodel;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.store.DayPlanMetaData.DayTaskTable;
import com.magizdev.dayplan.util.DayTaskUtil;
import com.magizdev.dayplan.util.DayUtil;
import com.magizdev.dayplan.viewmodel.DayTaskTimeInfo.TimeType;

public class DayTaskAdapter extends BaseAdapter {
	Context context;
	StorageUtil<DayTaskInfo> storageUtil;
	List<DayTaskInfo> tasks;
	DayTaskUtil taskUtil;

	public DayTaskAdapter(Context context) {
		this.context = context;
		DayTaskInfo blank = new DayTaskInfo();
		storageUtil = new StorageUtil<DayTaskInfo>(context, blank);
		String whereStrings = DayTaskTable.DATE + "=" + DayUtil.Today();
		tasks = storageUtil.getCollection(whereStrings);
		taskUtil = new DayTaskUtil(context);
	}

	public void removeAt(int index) {
	}

	public void refresh() {
		String whereStrings = DayTaskTable.DATE + "=" + DayUtil.Today();
		tasks = storageUtil.getCollection(whereStrings);
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
		return tasks.get(position).ID;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.backlog_item_track, null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.tVName1);
			viewHolder.startButton = (ImageButton) convertView.findViewById(R.id.startButton);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		DayTaskInfo taskInfo = tasks.get(position);
		viewHolder.name.setText(taskInfo.BIName);
		final long biid = taskInfo.BIID;
		TimeType state = taskUtil.GetTaskState(biid);
		int imageId = state == TimeType.Start?android.R.drawable.ic_media_pause:android.R.drawable.ic_media_play;
		viewHolder.startButton.setImageResource(imageId);
		viewHolder.startButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TimeType state = taskUtil.GetTaskState(biid);
				if(state == TimeType.Start){
					taskUtil.StopTask(biid);
				}else {
					taskUtil.StartTask(biid);
				}
				DayTaskAdapter.this.notifyDataSetChanged();
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
		return tasks.size() == 0;
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
		public ImageButton startButton;

		// public Button deleteBtn;
		// public ImageButton notificationSetter;
	}

}
