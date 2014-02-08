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
import android.widget.ImageButton;
import android.widget.ListView;

import com.magizdev.babyoneday.util.ActivityUtil;
import com.magizdev.babyoneday.viewmodel.DayOneAdapter;

public class RawDataViewFragment extends Fragment implements OnDateSetListener {
	private ListView taskListView;
	private DayOneAdapter adapter;
	private ActivityUtil taskUtil;
	private Button btnDatePicker;
	private Calendar calendar;

	public RawDataViewFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_raw_data_view,
				container, false);

		taskListView = (ListView) rootView.findViewById(R.id.listViewDayView);
		taskListView.setBackgroundResource(R.drawable.widget_bg);
		adapter = new DayOneAdapter(getActivity());
		taskListView.setAdapter(adapter);

		btnDatePicker = (Button) rootView.findViewById(R.id.datePickerBtn);
		ImageButton backBtn = (ImageButton) rootView.findViewById(R.id.backBtn);
		ImageButton forwardBtn = (ImageButton) rootView
				.findViewById(R.id.forwardBtn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				calendar.add(Calendar.DAY_OF_YEAR, -1);
				refreshView();
			}
		});
		forwardBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				refreshView();
			}
		});
		Date today = new Date();
		calendar = Calendar.getInstance();
		calendar.setTime(today);
		refreshView();
		btnDatePicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerDialog dialog = new DatePickerDialog(getActivity(),
						RawDataViewFragment.this, calendar.get(Calendar.YEAR),
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
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {

		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, monthOfYear);
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		refreshView();
	}

	private void refreshView() {
		btnDatePicker.setText(calendar.get(Calendar.YEAR) + "/"
				+ (calendar.get(Calendar.MONTH) + 1) + "/"
				+ calendar.get(Calendar.DAY_OF_MONTH));
		adapter.refresh(calendar.getTime());
	}
}
