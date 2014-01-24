package com.magizdev.babyoneday;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import com.magizdev.babyoneday.util.ActivityUtil;
import com.magizdev.babyoneday.viewmodel.DayOneAdapter;

public class DayOneViewFragment extends Fragment implements OnDateSetListener {
	private ListView taskListView;
	private DayOneAdapter adapter;
	private ActivityUtil taskUtil;
	private Button btnDatePicker;

	public DayOneViewFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_day_view, container,
				false);

		taskListView = (ListView) rootView.findViewById(R.id.listViewDayView);
		taskListView.setBackgroundResource(R.drawable.widget_bg);
		adapter = new DayOneAdapter(getActivity());
		taskListView.setAdapter(adapter);

		btnDatePicker = (Button) rootView.findViewById(R.id.datePickerBtn);
		Date today = new Date();
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		btnDatePicker.setText(calendar.get(Calendar.YEAR) + "/"
				+ (calendar.get(Calendar.MONTH) + 1) + "/"
				+ calendar.get(Calendar.DAY_OF_MONTH));
		btnDatePicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerDialog dialog = new DatePickerDialog(getActivity(),
						DayOneViewFragment.this, calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH), calendar
								.get(Calendar.DAY_OF_MONTH));
				dialog.show();
			}
		});
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.refresh();
		// handler.sendEmptyMessageDelayed(0, 60000);
	}

	@Override
	public void onPause() {
		super.onPause();
		// handler.removeMessages(0);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, monthOfYear);
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		btnDatePicker
				.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
		adapter.refresh(calendar.getTime());
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.activity_day_plan, menu);
	//
	// return super.onCreateOptionsMenu(menu);
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle presses on the action bar items
	// Intent intent = new Intent();
	// switch (item.getItemId()) {
	// case R.id.action_mark:
	// intent.setClass(this, BacklogItemActivity.class);
	// startActivity(intent);
	// return true;
	// case R.id.action_report:
	// intent.setClass(this, PieChartBuilder.class);
	// startActivity(intent);
	// return true;
	// default:
	// return super.onOptionsItemSelected(item);
	// }
	// }

}
