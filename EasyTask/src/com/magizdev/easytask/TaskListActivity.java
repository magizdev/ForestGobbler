package com.magizdev.easytask;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GestureDetectorCompat;
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
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.magizdev.easytask.util.AlarmUtil;
import com.magizdev.easytask.viewmodel.EasyTaskInfo;
import com.magizdev.easytask.viewmodel.EasyTaskUtil;
import com.magizdev.easytask.viewmodel.TaskListAdapter;

public class TaskListActivity extends Activity {
	private ListView listView;
	private EasyTaskUtil util;
	private RelativeLayout inputArea;
	TaskListAdapter adapter;
	private long animDuration;
	private GestureDetectorCompat mDetector;

	private Handler uiHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			View selectedItem = listView.getChildAt(msg.what);
			final Button deleteButton = (Button) selectedItem
					.findViewById(R.id.deleteBtn);
			deleteButton.animate().scaleX(0).setDuration(animDuration)
					.setListener(new AnimatorListener() {

						@Override
						public void onAnimationStart(Animator animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationRepeat(Animator animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animator animation) {
							deleteButton.setVisibility(View.GONE);
							deleteButton.setScaleX(1);
						}

						@Override
						public void onAnimationCancel(Animator animation) {
							// TODO Auto-generated method stub

						}
					}).start();
			uiHandler.removeMessages(msg.what);
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
		AdView adView = new AdView(this, AdSize.BANNER, "");
		adContainer.addView(adView);
		AdRequest adRequest = new AdRequest();
		adView.loadAd(adRequest);

		listView = (ListView) this.findViewById(R.id.taskList);
		inputArea = (RelativeLayout) this.findViewById(R.id.inputArea);
		util = new EasyTaskUtil(this);
		adapter = new TaskListAdapter(this, listView, uiHandler);
		listView.setAdapter(adapter);
		mDetector = new GestureDetectorCompat(this, new EasyGestureListener(
				listView, uiHandler, animDuration));
		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mDetector.onTouchEvent(event);
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent editTaskIntent = new Intent(TaskListActivity.this,
						TaskEditActivity.class);
				editTaskIntent.putExtra("clickItemPosition", arg2);
				editTaskIntent.putExtra("easyTaskId", listView.getAdapter()
						.getItemId(arg2));
				startActivityForResult(editTaskIntent, 22);
			}
		});

		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		ImageButton sendButton = (ImageButton) this.findViewById(R.id.addTask);
		final EditText note = (EditText) this.findViewById(R.id.taskInput);

		List<EasyTaskInfo> tasks = util.getTasks();
		int index = 0;
		for (EasyTaskInfo task : tasks) {
			if (task.StartDate.getTime() > System.currentTimeMillis()) {
				break;
			}
			index++;
		}
		if (index > 2) {
			listView.smoothScrollToPositionFromTop(index - 2, 0);
		}

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
						calendar.add(GregorianCalendar.YEAR, 1);
						dueDate = calendar.getTime();
					}
					EasyTaskInfo task = new EasyTaskInfo(0, ana
							.getFilteredString(), new Date(), dueDate);
					long id = util.addTask(task);
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
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent result) {
		if (resultCode == RESULT_OK) {
			// int position = result.getIntExtra("itemPosition", 0);
			adapter.refresh();
			adapter.notifyDataSetChanged();
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
}
