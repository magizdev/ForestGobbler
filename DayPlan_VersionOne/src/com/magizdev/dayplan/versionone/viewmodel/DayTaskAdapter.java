package com.magizdev.dayplan.versionone.viewmodel;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.versionone.PieChartData;
import com.magizdev.dayplan.versionone.store.DayPlanMetaData.DayTaskTable;
import com.magizdev.dayplan.versionone.util.DayTaskTimeUtil;
import com.magizdev.dayplan.versionone.util.DayTaskUtil;
import com.magizdev.dayplan.versionone.util.DayUtil;

public class DayTaskAdapter extends BaseAdapter {
	Context context;
	StorageUtil<DayTaskInfo> storageUtil;
	List<DayTaskInfo> tasks;
	DayTaskUtil taskUtil;
	List<PieChartData> taskTimes;
	HashMap<Long, Integer> taskTimeHash;
	private boolean inEditMode;
	private StorageUtil<DayTaskTimeInfo> timeUtil;
	private DayTaskUtil dayTaskUtil;

	public DayTaskAdapter(Context context) {
		this.context = context;
		dayTaskUtil = new DayTaskUtil(context);
		DayTaskInfo blank = new DayTaskInfo();
		storageUtil = new StorageUtil<DayTaskInfo>(context, blank);
		String whereStrings = DayTaskTable.DATE + "=" + DayUtil.Today();
		tasks = storageUtil.getCollection(whereStrings, null);
		taskUtil = new DayTaskUtil(context);
		fillRemainEstimate();
		timeUtil = new StorageUtil<DayTaskTimeInfo>(context,
				new DayTaskTimeInfo());
		List<DayTaskTimeInfo> times = timeUtil
				.getCollection(whereStrings, null);
		taskTimes = DayTaskTimeUtil.compute(times);
		taskTimeHash = new HashMap<Long, Integer>();
		for (PieChartData taskTime : taskTimes) {
			taskTimeHash.put(taskTime.biid, taskTime.data);
		}
	}
	
	private void fillRemainEstimate(){
		for(DayTaskInfo task: tasks){
			task.RemainEffort = dayTaskUtil.GetTaskRemainEstimate(task.BIID);
		}
	}

	public void setEditMode(boolean mode) {
		this.inEditMode = mode;
		notifyDataSetChanged();
	}
	
	public void save(){
		for (DayTaskInfo task : tasks) {
			storageUtil.update(task.ID, task);
		}
	}

	public void removeAt(int index) {
	}

	public void refresh() {
		String whereStrings = DayTaskTable.DATE + "=" + DayUtil.Today();
		tasks = storageUtil.getCollection(whereStrings, null);
		fillRemainEstimate();
		List<DayTaskTimeInfo> times = timeUtil
				.getCollection(whereStrings, null);
		taskTimes = DayTaskTimeUtil.compute(times);
		taskTimeHash = new HashMap<Long, Integer>();
		for (PieChartData taskTime : taskTimes) {
			taskTimeHash.put(taskTime.biid, taskTime.data);
		}
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
		final DayTaskInfo taskInfo = tasks.get(position);

		if (inEditMode) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.backlog_item_close, null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.tVName1);
			viewHolder.effort = (TextView) convertView
					.findViewById(R.id.effort);
			viewHolder.remainEstimate = (EditText) convertView
					.findViewById(R.id.remainEstimate);
			int intEffort = 0;
			if(taskTimeHash.containsKey(taskInfo.BIID)){
				intEffort = taskTimeHash.get(taskInfo.BIID);
			}
			viewHolder.effort.setText(DayUtil.formatTime(intEffort / 1000));
			float estimate = dayTaskUtil.GetTaskRemainEstimate(taskInfo.BIID);
			viewHolder.remainEstimate.setText(String.format("%2.2f", estimate
					- intEffort / 1000 / 60 / 60 ));
			final int finalPosition = position;
			viewHolder.updateButton = (ImageButton)convertView.findViewById(R.id.updateRemainEstimate);
			viewHolder.updateButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					taskInfo.RemainEffort = Float.parseFloat(viewHolder.remainEstimate.getText().toString());
					storageUtil.update(taskInfo.ID, taskInfo);
				}
			});
			viewHolder.remainEstimate.setOnEditorActionListener(new OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					tasks.get(finalPosition).RemainEffort = Float.parseFloat(v.getText().toString());
					Log.w("test", actionId+"");
					return false;
				}
			});

		} else {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.backlog_item_track, null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.tVName1);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.taskTimeTextView);
			viewHolder.startButton = (ImageButton) convertView
					.findViewById(R.id.startButton);
			viewHolder.progress = (ProgressBar) convertView
					.findViewById(R.id.taskStatus);
			viewHolder.startButton.setTag(taskInfo);
			convertView.setTag(viewHolder);
			viewHolder.startButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					DayTaskInfo taskInfo = (DayTaskInfo) v.getTag();
					boolean waitStop = taskUtil
							.IsTaskWaitingForStop(taskInfo.BIID);
					if (waitStop) {
						taskUtil.StopTask(taskInfo.BIID);
						refresh();
					} else {
						taskUtil.StartTask(taskInfo.BIID);
						refresh();
					}
					DayTaskAdapter.this.notifyDataSetChanged();
				}
			});
			if (taskTimeHash.containsKey(taskInfo.BIID)) {
				viewHolder.time.setText(DayUtil.formatTime(taskTimeHash
						.get(taskInfo.BIID)));
			} else {
				viewHolder.time.setText("00:00");
			}
			final long biid = taskInfo.BIID;
			boolean waitStop = taskUtil.IsTaskWaitingForStop(biid);
			int imageId = waitStop ? android.R.drawable.ic_media_pause
					: android.R.drawable.ic_media_play;
			viewHolder.startButton.setImageResource(imageId);
			int visibility = waitStop ? View.VISIBLE : View.GONE;
			viewHolder.progress.setVisibility(visibility);
		}
		viewHolder.name.setText(taskInfo.BIName);
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
		public TextView time;
		public ImageButton startButton;
		public ProgressBar progress;
		public TextView effort;
		public EditText remainEstimate;
		public ImageButton updateButton;
		// public Button deleteBtn;
		// public ImageButton notificationSetter;
	}

}
