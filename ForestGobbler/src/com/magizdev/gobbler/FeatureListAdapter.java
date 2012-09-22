package com.magizdev.gobbler;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

public class FeatureListAdapter extends BaseAdapter {
	private List<Integer> featureIds;
	private LayoutInflater mInflater;
	private FeatureSettingUtil featureUtil;
	private DashboardUtil dashboardUtil;

	public FeatureListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		featureUtil = new FeatureSettingUtil(context);
		featureIds = featureUtil.getFeatures();
		dashboardUtil = new DashboardUtil(context);
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		return featureIds.size();
	}

	@Override
	public Object getItem(int position) {
		return featureIds.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.featurelist, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.toggle = (ToggleButton) convertView
					.findViewById(R.id.switch1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		int id = featureIds.get(position);

		holder.title.setText(featureUtil.getFeatureName(id));
		holder.toggle.setTag(id);
		holder.toggle.setChecked(featureUtil.getFeature(id));

		if (featureUtil.getFeatureRequirement(id) <= 500) {
			holder.toggle.setEnabled(true);
		} else {
			holder.toggle.setEnabled(false);
		}

		holder.toggle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToggleButton button = (ToggleButton) v;
				int id = (Integer) v.getTag();
				featureUtil.setFeature(id, button.isChecked());
			}
		});
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	public class ViewHolder {
		public TextView title;
		public ToggleButton toggle;
	}

}
