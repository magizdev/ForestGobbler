package com.magizdev.easytask.util;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ExpandableArrayAdapter<T> extends ArrayAdapter<T> {
	public ExpandableArrayAdapter(Context context, int resource,
			int textViewResourceId, List<T> objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}
	
	public ExpandableArrayAdapter(Context context, int resource,
			int textViewResourceId, T[] objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int arg0, View view, ViewGroup viewGroup){
		View convertView = super.getView(arg0, view, viewGroup);
		convertView.getLayoutParams().height = viewGroup.getHeight();
		return convertView;
	}
}
