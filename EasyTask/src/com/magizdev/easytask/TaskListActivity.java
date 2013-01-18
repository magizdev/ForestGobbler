package com.magizdev.easytask;

import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.magizdev.easytask.viewmodel.EasyTaskInfo;
import com.magizdev.easytask.viewmodel.EasyTaskUtil;
import com.magizdev.easytask.viewmodel.TaskListAdapter;

public class TaskListActivity extends Activity {
	private ListView listView;
	private EasyTaskUtil util;
	private RelativeLayout inputArea;
	TaskListAdapter adapter;
	private AlarmManager alarmManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_task_list);
		listView = (ListView) this.findViewById(R.id.taskList);
		inputArea = (RelativeLayout) this.findViewById(R.id.inputArea);
		util = new EasyTaskUtil(this);
		adapter = new TaskListAdapter(this);
		listView.setAdapter(adapter);
		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
				listView,
				new SwipeDismissListViewTouchListener.OnDismissCallback() {
					@Override
					public void onDismiss(ListView listView,
							int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							adapter.removeAt(position);
						}
						adapter.notifyDataSetChanged();
					}
				});
		listView.setOnTouchListener(touchListener);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent editTaskIntent = new Intent(TaskListActivity.this, TaskEditActivity.class);
				editTaskIntent.putExtra("clickItemPosition", arg2);
				editTaskIntent.putExtra("easyTaskId", listView.getAdapter().getItemId(arg2));
				startActivityForResult(editTaskIntent, 22);
			}
		});
		listView.setOnScrollListener(touchListener.makeScrollListener());
		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		ImageButton sendButton = (ImageButton) this.findViewById(R.id.addTask);
		final EditText note = (EditText) this.findViewById(R.id.taskInput);
		sendButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				String noteString = note.getText().toString();
				if (!noteString.isEmpty()) {
					Analyzer ana = new Analyzer(noteString);
					Date dueDate = new Date();
					if (ana.getHasTime()) {
						dueDate = ana.getDateTime();
					}
					EasyTaskInfo task = new EasyTaskInfo(0, ana.getFilteredString(),
							new Date(), dueDate);
					long id = util.addTask(task);
					if(ana.getHasTime()){
						sendAlarm(id, dueDate);
					}
					note.getText().clear();
					adapter.refresh();
					adapter.notifyDataSetChanged();
					imm.hideSoftInputFromWindow(TaskListActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}

		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent result){
		if(resultCode == RESULT_OK){
			//int position = result.getIntExtra("itemPosition", 0);
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
		//getMenuInflater().inflate(R.menu.activity_task_list, menu);
		return true;
	}
	
	private void sendAlarm(long id, Date dueDate) {
		Intent intent = new Intent(this, AlarmReceiver.class);
		intent.putExtra("easyTaskId", id);
		PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager
				.set(AlarmManager.RTC_WAKEUP, dueDate.getTime(), pIntent);
	}

}
