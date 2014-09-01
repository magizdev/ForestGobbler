package com.magizdev.babyoneday.viewmodel;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.magizdev.babyoneday.R;
import com.magizdev.babyoneday.util.Profile;

public class DrawerAdapter extends BaseAdapter {
	Context context;
	String[] pageTitles;

	public DrawerAdapter(Context context, String[] pageTitles) {
		this.context = context;
		this.pageTitles = pageTitles;
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
		return pageTitles.length + 1; // +1 header section.
	}

	public Object getItem(int position) {
		if (position == 0) {
			return null;
		} else {
			return pageTitles[position - 1];
		}

	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			if (position == 0) {
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.drawer_header, null);
				viewHolder.icon = (ImageView) convertView
						.findViewById(R.id.drawerHeaderPic);
				if (Profile.Instance() != null) {
					viewHolder.icon.setImageBitmap(Profile.Instance().pic);
				}
			} else {
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.drawer_list_item, null);
				viewHolder.icon = (ImageView) convertView
						.findViewById(R.id.drawerListItemIcon);
				viewHolder.title = (TextView) convertView
						.findViewById(R.id.drawerListItemName);
				switch (position) {
				case 1:
					viewHolder.icon.setImageResource(R.drawable.ic_action_dabian);
					break;
				case 2:
					break;
				case 3:
					break;
				default:
					break;
				}
				viewHolder.title.setText(pageTitles[position - 1]);
			}
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}


	@Override
	public boolean isEmpty() {
		return false;
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
		public ImageView icon;
		public TextView title;
	}
}
