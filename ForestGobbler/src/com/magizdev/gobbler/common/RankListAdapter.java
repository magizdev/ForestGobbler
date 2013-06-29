package com.magizdev.gobbler.common;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.magizdev.gobbler.R;

public class RankListAdapter extends BaseAdapter {
	private List<RankInfo> ranks;
	private LayoutInflater mInflater;

	public RankListAdapter(Context context, List<RankInfo> ranks) {
		mInflater = LayoutInflater.from(context);
		this.ranks=ranks; 

	}

	public void removeAt(int index) {

	}

	public void refreshAt(int index) {
		
	}

	public void refresh() {
		
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
		return ranks.size();
	}

	public Object getItem(int position) {
		return ranks.get(position);
	}


	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.rank_item, null);
			holder.rank = (TextView) convertView.findViewById(R.id.rank);
			holder.username = (TextView) convertView.findViewById(R.id.username);
			holder.score = (TextView) convertView.findViewById(R.id.score);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.rank.setText(String.valueOf(ranks.get(position).Rank));
		holder.username.setText(ranks.get(position).UserName);
		holder.score.setText(String.valueOf(ranks.get(position).Score));
		
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return ranks.isEmpty();
	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return true;
	}

	public class ViewHolder {
		public TextView rank;
		public TextView username;
		public TextView score;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
