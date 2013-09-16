package com.magizdev.dayplan.viewmodel;

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
import android.widget.TextView;

import com.magizdev.dayplan.R;

public class BacklogItemAdapter extends BaseAdapter {
	Context context;
	StorageUtil<BacklogItemInfo> storageUtil;
	List<BacklogItemInfo> backlogs;
	List<Long> selectedIds;

	public BacklogItemAdapter(Context context, List<Long> selectedIds) {
		this.context = context;
		BacklogItemInfo blank = new BacklogItemInfo();
		this.selectedIds = selectedIds;
		storageUtil = new StorageUtil<BacklogItemInfo>(context, blank);
		backlogs = storageUtil.getCollection(null);
		for (int i = 0; i < backlogs.size(); i++) {
			BacklogItemInfo backlogItemInfo = backlogs.get(i);
			if (selectedIds.contains(backlogItemInfo.Id)) {
				backlogItemInfo.Selected = true;
			} else {
				backlogItemInfo.Selected = false;
			}
		}
	}

	public void removeAt(int index) {
	}

	public void refresh() {
		backlogs = storageUtil.getCollection(null);
		for (int i = 0; i < backlogs.size(); i++) {
			BacklogItemInfo backlogItemInfo = backlogs.get(i);
			if (selectedIds.contains(backlogItemInfo.Id)) {
				backlogItemInfo.Selected = true;
			} else {
				backlogItemInfo.Selected = false;
			}
		}
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
			viewHolder.checkBox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							BacklogItemInfo backlogItemInfo = (BacklogItemInfo) buttonView
									.getTag();
							backlogItemInfo.Selected = isChecked;
						}
					});

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		BacklogItemInfo backlog = backlogs.get(position);
		viewHolder.name.setText(backlog.Name);
		viewHolder.checkBox.setChecked(backlogs.get(position).Selected);
		viewHolder.checkBox.setTag(backlogs.get(position));

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

	public class ViewHolder {
		public TextView name;
		public CheckBox checkBox;
		// public Button deleteBtn;
		// public ImageButton notificationSetter;
	}

}
