package com.magizdev.easytask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.v4.view.GestureDetectorCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.magizdev.common.view.ArrayWheelAdapter;
import com.magizdev.common.view.NumericWheelAdapter;
import com.magizdev.common.view.WheelView;
import com.magizdev.easytask.util.AlarmUtil;
import com.magizdev.easytask.util.HeaderListView;
import com.magizdev.easytask.viewmodel.EasyTaskInfo;
import com.magizdev.easytask.viewmodel.EasyTaskUtil;
import com.magizdev.easytask.viewmodel.TaskListHeaderAdapter;

public class TaskListActivity extends Activity {
	protected static final int RESULT_SPEECH = 1;
	public static final int RESULT_EDIT = 2;
	private HeaderListView listView;
	private EasyTaskUtil util;
	private RelativeLayout inputArea;
	private TaskListHeaderAdapter adapter;
	private long animDuration;
	private GestureDetectorCompat mDetector;
	private ImageButton btnSpeak;
	private EditText note;
	private Dialog timePicker;

	private Handler uiHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int first = listView.getListView().getFirstVisiblePosition();
			int last = listView.getListView().getLastVisiblePosition();
			if (msg.what >= first && msg.what <= last) {
				View selectedItem = listView.getListView().getChildAt(
						msg.what
								- listView.getListView()
										.getFirstVisiblePosition());
				final Button deleteButton = (Button) selectedItem
						.findViewById(R.id.deleteBtn);
				deleteButton.animate().scaleX(0).setDuration(animDuration)
						.setListener(new AnimatorListener() {

							@Override
							public void onAnimationStart(Animator animation) {
							}

							@Override
							public void onAnimationRepeat(Animator animation) {
							}

							@Override
							public void onAnimationEnd(Animator animation) {
								deleteButton.setVisibility(View.GONE);
								deleteButton.setScaleX(1);
							}

							@Override
							public void onAnimationCancel(Animator animation) {
							}
						}).start();
				uiHandler.removeMessages(msg.what);
			} else {
				uiHandler.removeMessages(msg.what);
				uiHandler.sendEmptyMessageDelayed(msg.what, 2000);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);
		animDuration = getResources().getInteger(
				android.R.integer.config_shortAnimTime);
		LinearLayout adContainer = (LinearLayout) this
				.findViewById(R.id.adContainer);
		AdView adView = new AdView(this, AdSize.BANNER, "a1520fa1c2a3c8f");
		adContainer.addView(adView);
		AdRequest adRequest = new AdRequest();
		adView.loadAd(adRequest);

		listView = (HeaderListView) this.findViewById(R.id.taskList);
		inputArea = (RelativeLayout) this.findViewById(R.id.inputArea);
		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
		util = new EasyTaskUtil(this);
		adapter = new TaskListHeaderAdapter(this, listView, uiHandler);
		listView.setAdapter(adapter);
		mDetector = new GestureDetectorCompat(this, new EasyGestureListener(
				listView.getListView(), uiHandler, animDuration));
		listView.getListView().setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mDetector.onTouchEvent(event);
			}
		});

		listView.getListView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						long taskId = ((TaskListHeaderAdapter) listView
								.getListView().getAdapter()).getTaskId(arg2);
						if (taskId > -1) {
							EasyTaskInfo taskInfo = util.getTask(taskId);
							timePicker.setTitle(taskInfo.Title);
							timePicker.show();
//							Intent editTaskIntent = new Intent(
//									TaskListActivity.this,
//									TaskEditActivity.class);
//							editTaskIntent.putExtra("clickItemPosition", arg2);
//							editTaskIntent.putExtra("easyTaskId", taskId);
//
//							startActivityForResult(editTaskIntent, RESULT_EDIT);
						}
					}
				});

		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		ImageButton sendButton = (ImageButton) this.findViewById(R.id.addTask);
		note = (EditText) this.findViewById(R.id.taskInput);

		sendButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				String noteString = note.getText().toString();
				if (!noteString.isEmpty()) {
					Analyzer ana = new Analyzer(noteString);
					Date dueDate = new Date(System.currentTimeMillis());
					if (ana.getHasTime()) {
						dueDate = ana.getDateTime();
					} else {
						GregorianCalendar calendar = new GregorianCalendar();
						calendar.setTime(dueDate);
						calendar.add(GregorianCalendar.MONTH, 1);
						dueDate = calendar.getTime();
					}
					EasyTaskInfo task = new EasyTaskInfo(0, ana
							.getFilteredString(), null, new Date(), dueDate,
							"local", null);
					util.addTask(task);
					if (ana.getHasTime()) {
						AlarmUtil.updateAlarm(TaskListActivity.this);
					}
					note.getText().clear();
					adapter.refresh();
					adapter.notifyDataSetChanged();
					imm.hideSoftInputFromWindow(TaskListActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}

		});

		btnSpeak.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

				try {
					startActivityForResult(intent, RESULT_SPEECH);
				} catch (ActivityNotFoundException a) {
					Toast t = Toast.makeText(getApplicationContext(),
							"Your device doesn't support Speech to Text",
							Toast.LENGTH_SHORT);
					t.show();
				}
			}
		});

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
				adapter.refresh();
				adapter.notifyDataSetChanged();
			}
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		inputArea.requestFocus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_task_list, menu);
		return true;
	}

	private Dialog iniDateTimePicker() {
		Dialog dialog = new Dialog(this);
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		List<String> incomingDays = new ArrayList<String>();
		incomingDays.add("Today");
		for (int i = 1; i < 7; i++) {
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			incomingDays.add(calendar.get(Calendar.YEAR) + "/"
					+ calendar.get(Calendar.MONTH) + "/"
					+ calendar.get(Calendar.DAY_OF_MONTH));
		}

		String[] arrStrings = new String[incomingDays.size()];
		incomingDays.toArray(arrStrings);

		ArrayWheelAdapter<String> dateAdapter = new ArrayWheelAdapter<String>(
				arrStrings, incomingDays.size());
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(
				com.magizdev.common.lib.R.layout.time_layout, null);

		final WheelView wv_date = (WheelView) view
				.findViewById(com.magizdev.common.lib.R.id.date);
		wv_date.setAdapter(dateAdapter);
		wv_date.setCyclic(false);
		wv_date.setCurrentItem(0);

		final WheelView wv_hours = (WheelView) view
				.findViewById(com.magizdev.common.lib.R.id.hour);
		wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
		wv_hours.setCyclic(true);
		wv_hours.setCurrentItem(hour);

		final WheelView wv_mins = (WheelView) view
				.findViewById(com.magizdev.common.lib.R.id.mins);
		wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		wv_mins.setCyclic(true);
		wv_mins.setCurrentItem(minute);

		int textSize = 15;

		wv_date.TEXT_SIZE = textSize;
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;
		
		Button btn_sure = (Button) view.findViewById(R.id.btn_datetime_sure);
		Button btn_cancel = (Button) view
				.findViewById(R.id.btn_datetime_cancel);
		
		btn_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				note.setText(wv_date.getCurrentItem());
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
