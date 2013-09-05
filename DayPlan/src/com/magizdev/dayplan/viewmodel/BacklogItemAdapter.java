package com.magizdev.dayplan.viewmodel;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.magizdev.dayplan.R;

public class BacklogItemAdapter extends BaseAdapter {
	Context context;
	StorageUtil<BacklogItemInfo> storageUtil;
	List<BacklogItemInfo> backlogs;

	public BacklogItemAdapter(Context context) {
		this.context = context;
		BacklogItemInfo blank = new BacklogItemInfo();
		storageUtil = new StorageUtil<BacklogItemInfo>(context, blank);
		String[] whereStrings = new String[0];
		backlogs = storageUtil.getCollection(whereStrings);
	}

	public void removeAt(int index) {
	}

	public void refresh() {
		String[] whereStrings = new String[0];
		backlogs = storageUtil.getCollection(whereStrings);
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
		return backlogs.size() + 1;
	}

	public Object getItem(int position) {
		if (position < backlogs.size()) {
			return backlogs.get(position);
		} else {
			return null;
		}
	}

	public long getItemId(int position) {
		if (position < backlogs.size()) {
			return backlogs.get(position).Id;
		} else {
			return -1;
		}
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		final int finalPosition = position;
		if (position < backlogs.size()) {
			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.backlog_item, null);
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.tVName);
				viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.checkBox1);
				viewHolder.checkBox.setChecked(backlogs.get(position).Selected);
				viewHolder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
						backlogs.get(finalPosition).Selected = arg1;
					}
				});
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			BacklogItemInfo backlog = backlogs.get(position);
			viewHolder.name.setText(backlog.Name);

			return convertView;
		} else {
			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.backlog_item_add, null);
				viewHolder.etName = (EditText) convertView
						.findViewById(R.id.eTName);
				viewHolder.addButton = (Button) convertView
						.findViewById(R.id.btnAdd);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.addButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					BacklogItemInfo newItem = new BacklogItemInfo(-1,
							viewHolder.etName.getText().toString(), null);
					storageUtil.add(newItem);
					BacklogItemAdapter.this.notifyDataSetChanged();
				}
			});

			return convertView;
		}
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
		public EditText etName;
		public Button addButton;
		public CheckBox checkBox;
		// public Button deleteBtn;
		// public ImageButton notificationSetter;
	}

}
