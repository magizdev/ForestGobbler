package com.magizdev.dayplan.util;

import android.app.ActionBar.OnNavigationListener;
import android.content.Context;
import android.content.Intent;

import com.magizdev.dayplan.BacklogItemActivity;
import com.magizdev.dayplan.DayPlanActivity;
import com.magizdev.dayplan.PieChartBuilder;

public class InAppNavigation implements OnNavigationListener {
	private Context context;
	private boolean enable;

	public InAppNavigation(Context context) {
		this.context = context;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		if (enable) {
			Intent intent = new Intent();
			switch ((int) itemId) {
			case 0:
				intent.setClass(context, DayPlanActivity.class);
				context.startActivity(intent);
				return true;
			case 1:
				intent.setClass(context, BacklogItemActivity.class);
				context.startActivity(intent);
				return true;
			case 2:
				intent.setClass(context, PieChartBuilder.class);
				context.startActivity(intent);
				return true;
			default:
			}
		}
		return true;
	}

}
