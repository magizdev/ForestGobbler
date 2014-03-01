package com.magizdev.dayplan.versionone;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.magizdev.dayplan.R;

public class DrawerAdapter extends BaseAdapter {
	private Context context;
	private List<String> labels;
	private int selectedIndex;

	public DrawerAdapter(Context context) {
		this.context = context;
		labels = Arrays.asList(context.getResources().getStringArray(
				R.array.Pages));
		selectedIndex = 0;

	}

	public void select(int index) {
		selectedIndex = index;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return labels.size();
	}

	@Override
	public Object getItem(int position) {
		return labels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.product_item, null);
			viewHolder.label = (TextView) convertView
					.findViewById(R.id.productName);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.label.setText(labels.get(position));
		if (position == selectedIndex) {
			viewHolder.label
					.setBackgroundResource(R.drawable.list_item_focused);
		} else {
			viewHolder.label.setBackgroundColor(Color.TRANSPARENT);
		}
		return convertView;
	}

	class ViewHolder {
		public TextView label;
	}

}
