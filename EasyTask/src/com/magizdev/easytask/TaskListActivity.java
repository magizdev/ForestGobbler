package com.magizdev.easytask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


//import com.baidu.mobads.AdView;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.magizdev.common.view.NumericWheelAdapter;
import com.magizdev.common.view.OnWheelChangedListener;
import com.magizdev.common.view.WheelView;
import com.magizdev.easytask.util.AlarmUtil;
import com.magizdev.easytask.util.TaskMenuCreator;
import com.magizdev.easytask.viewmodel.EasyTaskInfo;
import com.magizdev.easytask.viewmodel.EasyTaskRepository;
import com.magizdev.easytask.viewmodel.ITaskClick;
import com.magizdev.easytask.viewmodel.TaskListAdapter;

public class TaskListActivity extends Activity implements ITaskClick {
	protected static final int RESULT_SPEECH = 1;
	public static final int RESULT_EDIT = 2;
	
	private EasyTaskRepository util;
	private RelativeLayout inputArea;
	private TaskListAdapter adapter;
	private long animDuration;
	private ImageButton btnSpeak;
	private EditText note;
	private Dialog timePicker;
	private CheckBox enableNotification;
	private long currentTaskId = -1;
	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;
	private WheelView wv_hours;
	private WheelView wv_mins;

	private static int START_YEAR = 1990, END_YEAR = 2100;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);
		
//		final String[] array = {"Expired", "Incoming", "Unscheculed"};
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.task_list_row, R.id.title, array);
        final SwipeMenuListView taskList = (SwipeMenuListView) findViewById(R.id.taskList);
        
        TaskListAdapter adapter = new TaskListAdapter(this, taskList);
        FloatingActionButton addTextTask = (FloatingActionButton)findViewById(R.id.add_text_task);
        FloatingActionButton addVoiceTask = (FloatingActionButton)findViewById(R.id.add_voice_task);
        
        addTextTask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EasyTaskInfo task = new EasyTaskInfo("aaa", "test", new Date(), new Date(), false, EasyTaskInfo.STATUS_DEFAULT);
				task.save();
			}
		});
        taskList.setAdapter(adapter);
        
        TaskMenuCreator menuCreator = new TaskMenuCreator(this);
        taskList.setMenuCreator(menuCreator);
        
		//new SplashAd(this, splashAd, adListener, SplashType.REAL_TIME);
		animDuration = getResources().getInteger(
				android.R.integer.config_shortAnimTime);
		LinearLayout adContainer = (LinearLayout) this
				.findViewById(R.id.adContainer);
		
//		AdView adView = new AdView(this);
//		adContainer.addView(adView);


		util = new EasyTaskRepository(this);

		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


		timePicker = iniDateTimePicker();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent result) {
		super.onActivityResult(requestCode, resultCode, result);

		switch (requestCode) {
		case RESULT_SPEECH:
			if (resultCode == RESULT_OK && null != result) {
				note.getText().clear();

				ArrayList<String> text = result
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				note.setText(text.get(0));
			}
			break;
		case RESULT_EDIT:
			if (resultCode == RESULT_OK) {
//				adapter.refresh();
//				adapter.notifyDataSetChanged();
			}
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void OnClick(long taskId) {
		currentTaskId = taskId;
		EasyTaskInfo taskInfo = util.getTask(taskId);
		if (taskInfo.EnableNotify) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(taskInfo.NotifyDate);
			wv_year.setCurrentItem(calendar.get(Calendar.YEAR) - START_YEAR);
			wv_month.setCurrentItem(calendar.get(Calendar.MONTH));
			wv_day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
			wv_hours.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
			wv_mins.setCurrentItem(calendar.get(Calendar.MINUTE));
		}
		timePicker.setTitle(taskInfo.Title);
		enableNotification.setChecked(taskInfo.EnableNotify);
		timePicker.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_task_list, menu);
		return true;
	}

	private Dialog iniDateTimePicker() {
		Dialog dialog = new Dialog(this);
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };
		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(
				R.layout.time_layout, null);

		wv_year = (WheelView) view.findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));
		wv_year.setCyclic(true);
		wv_year.setLabel("Y");
		wv_year.setCurrentItem(year - START_YEAR);

		wv_month = (WheelView) view.findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setCyclic(true);
		wv_month.setLabel("M");
		wv_month.setCurrentItem(month);

		wv_day = (WheelView) view.findViewById(R.id.day);
		wv_day.setCyclic(true);

		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
		} else {

			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
		}
		wv_day.setLabel("D");
		wv_day.setCurrentItem(day - 1);

		wv_hours = (WheelView) view
				.findViewById(R.id.hour);
		wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
		wv_hours.setCyclic(true);
		wv_hours.setCurrentItem(hour);

		wv_mins = (WheelView) view
				.findViewById(R.id.mins);
		wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		wv_mins.setCyclic(true);
		wv_mins.setCurrentItem(minute);

		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				if (list_big
						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);
		int textSize = 15;

		wv_day.TEXT_SIZE = textSize;
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;

		Button btn_sure = (Button) view.findViewById(R.id.btn_datetime_sure);
		Button btn_cancel = (Button) view
				.findViewById(R.id.btn_datetime_cancel);
		enableNotification = (CheckBox) view
				.findViewById(R.id.checkNotification);
		enableNotification
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						wv_day.setEnabled(isChecked);
						wv_hours.setEnabled(isChecked);
						wv_mins.setEnabled(isChecked);

					}
				});

		btn_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int year = wv_year.getCurrentItem() + START_YEAR;
				int month = wv_month.getCurrentItem();
				int day = wv_day.getCurrentItem() + 1;
				int hour = wv_hours.getCurrentItem();
				int minutes = wv_mins.getCurrentItem();
				EasyTaskInfo taskInfo = util.getTask(currentTaskId);
				if (enableNotification.isChecked() == false) {
					taskInfo.EnableNotify = false;
				} else {
					Calendar newDate = Calendar.getInstance();
					newDate.setTime(new Date());
					newDate.set(Calendar.YEAR, year);
					newDate.set(Calendar.MONTH, month);
					newDate.set(Calendar.DAY_OF_MONTH, day);
					newDate.set(Calendar.HOUR_OF_DAY, hour);
					newDate.set(Calendar.MINUTE, minutes);
					taskInfo.NotifyDate = newDate.getTime();
				}
				taskInfo.save();
				AlarmUtil.updateAlarm(TaskListActivity.this);
//				TaskListActivity.this.adapter.refresh();
//				TaskListActivity.this.adapter.notifyDataSetChanged();
				TaskListActivity.this.timePicker.dismiss();
			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TaskListActivity.this.timePicker.dismiss();
			}
		});

		dialog.setContentView(view);
		return dialog;
	}
}
