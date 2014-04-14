package com.magizdev.dayplan.versionone.viewmodel;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.versionone.model.BacklogItem;
import com.magizdev.dayplan.versionone.store.DayPlanMetaData.BacklogItemTable;
import com.magizdev.dayplan.versionone.store.StorageUtil;
import com.magizdev.dayplan.versionone.util.BacklogComparator;
import com.magizdev.dayplan.versionone.util.DayTaskUtil;
import com.magizdev.dayplan.versionone.util.DayUtil;

public class BacklogItemAdapter extends BaseAdapter {
	private static String conditionActiveOnly = "(" + BacklogItemTable.STATE
			+ "=" + BacklogItemTable.STATE_ACTIVE + ")";
	private static String conditionAll = "(1=1)";
	Context context;
	StorageUtil<BacklogItem> storageUtil;
	DayTaskUtil taskUtil;
	int completedCount;
	List<BacklogItem> backlogs;
	List<Long> selectedIds;
	private String condition;

	public BacklogItemAdapter(Context context, List<Long> selectedIds) {
		this.context = context;
		taskUtil = new DayTaskUtil(context);
		BacklogItem blank = new BacklogItem();
		this.selectedIds = selectedIds;
		storageUtil = new StorageUtil<BacklogItem>(context, blank);
		condition = conditionActiveOnly;
		backlogs = storageUtil.getCollection(condition, null);
		for (int i = 0; i < backlogs.size(); i++) {
			BacklogItem backlogItemInfo = backlogs.get(i);
			if (selectedIds.contains(backlogItemInfo.Id)) {
				backlogItemInfo.Selected = true;
			} else {
				backlogItemInfo.Selected = false;
			}
			if (backlogItemInfo.HasEstimate()) {
				backlogItemInfo.RemainEstimate = taskUtil
						.GetTaskRemainEstimate(backlogItemInfo.Id,
								DayUtil.Today() + 1);
			}
		}
		Collections.sort(backlogs, new BacklogComparator());
	}

	public void removeAt(int index) {
	}

	public void setShowAll(Boolean showAll) {
		if (showAll) {
			condition = conditionAll;
		} else {
			condition = conditionActiveOnly;
		}
		refresh();
	}

	public void setSelected(List<Long> selected) {
		this.selectedIds = selected;
	}

	public void refresh() {
		backlogs = storageUtil.getCollection(condition, null);
		for (int i = 0; i < backlogs.size(); i++) {
			BacklogItem backlogItemInfo = backlogs.get(i);
			if (selectedIds.contains(backlogItemInfo.Id)) {
				backlogItemInfo.Selected = true;
			} else {
				backlogItemInfo.Selected = false;
			}
			if (backlogItemInfo.HasEstimate()) {
				backlogItemInfo.RemainEstimate = taskUtil
						.GetTaskRemainEstimate(backlogItemInfo.Id,
								DayUtil.Today() + 1);
			}
		}
		Collections.sort(backlogs, new BacklogComparator());
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
		return backlogs.size();
	}

	public Object getItem(int position) {

		return backlogs.get(position);

	}

	public long getItemId(int position) {
		return backlogs.get(position).Id;
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
			convertView = inflater.inflate(R.layout.backlog_item, null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.tVName);
			viewHolder.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBox1);

			viewHolder.stateIcon = (ImageView) convertView
					.findViewById(R.id.stateIcon);
			viewHolder.stateText = (TextView) convertView
					.findViewById(R.id.stateText);
			viewHolder.dueDateArea = (LinearLayout) convertView.findViewById(R.id.remainEffortArea);

			viewHolder.checkBox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							BacklogItem backlogItemInfo = (BacklogItem) buttonView
									.getTag();
							backlogItemInfo.Selected = isChecked;
						}
					});

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		BacklogItem backlog = backlogs.get(position);
		viewHolder.name.setText(backlog.Name);
		viewHolder.checkBox.setTag(backlogs.get(position));
		viewHolder.checkBox.setChecked(backlogs.get(position).Selected);
		if (backlog.HasDueDate()) {
			viewHolder.dueDateArea.setVisibility(View.VISIBLE);
			viewHolder.stateIcon
					.setImageResource(getBacklogStateIconResource(backlog));
			viewHolder.stateText.setText(getBacklogStateText(backlog));
		} else {
			viewHolder.dueDateArea.setVisibility(View.GONE);
		}

		return convertView;

	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return backlogs.size() == 0;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	private int getBacklogStateIconResource(BacklogItem backlogItem) {
		int stateResource = R.drawable.green;
		if (backlogItem.HasDueDate()) {
			int remainDate = backlogItem.DueDate - DayUtil.Today();
			if (remainDate <= 0) {
				stateResource = R.drawable.red;
			} else if (remainDate < 7) {
				stateResource = R.drawable.yellow;
			}
		}

		if (backlogItem.HasEstimate() && backlogItem.HasDueDate()
				&& backlogItem.DueDate > DayUtil.Today()) {
			int remainDate = backlogItem.DueDate - DayUtil.Today();
			double avg = backlogItem.RemainEstimate / remainDate;
			if (avg > 4 && stateResource == R.drawable.green) {
				stateResource = R.drawable.yellow;
			} else if (avg > 8) {
				stateResource = R.drawable.red;
			}
		}
		return stateResource;
	}

	private String getBacklogStateText(BacklogItem backlogItem) {
		String state = "";
		if (backlogItem.HasDueDate()) {
			state += DayUtil.formatCalendar(DayUtil
					.toCalendar(backlogItem.DueDate));
			state += " (" + (backlogItem.DueDate - DayUtil.Today()) + ") ";
		}
		if (backlogItem.HasEstimate()) {
			state += String.format("%.2f", backlogItem.RemainEstimate) + " ";
			if (backlogItem.HasDueDate()
					&& backlogItem.DueDate > DayUtil.Today()) {
				state += "("
						+ String.format("%.2f", (backlogItem.RemainEstimate / (backlogItem.DueDate - DayUtil
								.Today()))) + ")";
			}
		}

		return state;
	}

	class ViewHolder {
		public TextView name;
		public CheckBox checkBox;
		public LinearLayout dueDateArea;
		public ImageView stateIcon;
		public TextView stateText;
	}

}
