package com.magizdev.easytask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.speech.RecognizerIntent;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.TasksRequestInitializer;
import com.google.api.services.tasks.model.Task;
import com.magizdev.easytask.util.AlarmUtil;
import com.magizdev.easytask.util.HeaderListView;
import com.magizdev.easytask.viewmodel.EasyTaskInfo;
import com.magizdev.easytask.viewmodel.EasyTaskUtil;
import com.magizdev.easytask.viewmodel.TaskListHeaderAdapter;

public class TaskListActivity extends Activity {
	private static final int DIALOG_ACCOUNTS = 0;
	private static final String AUTH_TOKEN_TYPE = "oauth2:https://www.googleapis.com/auth/tasks";
	protected static final int RESULT_SPEECH = 1;
	public static final int RESULT_EDIT = 2;
	private HeaderListView listView;
	private EasyTaskUtil util;
	private RelativeLayout inputArea;
	TaskListHeaderAdapter adapter;
	private long animDuration;
	private GestureDetectorCompat mDetector;
	private ImageButton btnSpeak;
	private EditText note;
	private AccountManager accountManager;
	private Account account;
	Tasks service;

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
							Intent editTaskIntent = new Intent(
									TaskListActivity.this,
									TaskEditActivity.class);
							editTaskIntent.putExtra("clickItemPosition", arg2);
							editTaskIntent.putExtra("easyTaskId", taskId);

							startActivityForResult(editTaskIntent, RESULT_EDIT);
						}
					}
				});

		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		ImageButton sendButton = (ImageButton) this.findViewById(R.id.addTask);
		note = (EditText) this.findViewById(R.id.taskInput);

		List<EasyTaskInfo> tasks = util.getTasks();
		int index = 0;
		for (EasyTaskInfo task : tasks) {
			if (task.StartDate.getTime() > System.currentTimeMillis()) {
				break;
			}
			index++;
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
						calendar.add(GregorianCalendar.MONTH, 1);
						dueDate = calendar.getTime();
					}
					EasyTaskInfo task = new EasyTaskInfo(0, ana
							.getFilteredString(), null, new Date(), dueDate,
							"local", null);
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
		accountManager = AccountManager.get(this);
		// showDialog(DIALOG_ACCOUNTS);
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
				// int position = result.getIntExtra("itemPosition", 0);
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

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ACCOUNTS:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select a Google account");

			final Account[] accounts = accountManager
					.getAccountsByType("com.google");
			final int size = accounts.length;
			String[] names = new String[size];
			for (int i = 0; i < size; i++) {
				names[i] = accounts[i].name;
			}
			builder.setItems(names, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					gotAccount(accounts[which]);
				}
			});
			return builder.create();
		}
		return null;
	}

	Runnable taskThread = new Runnable() {

		@Override
		public void run() {
			List<Task> tasks = null;
			try {
				com.google.api.services.tasks.Tasks.TasksOperations.List list = service
						.tasks().list("default");
				com.google.api.services.tasks.model.Tasks all = list.execute();
				tasks = all.getItems();
			} catch (IOException e) {
				Log.w("tasks", e.getMessage());
			}
			for (Task task : tasks) {
				Log.w("tasks", task.getNotes());
			}

		}
	};

	public void gotAccount(Account account) {
		this.account = account;
		accountManager.getAuthToken(account, AUTH_TOKEN_TYPE, null,
				TaskListActivity.this, new AccountManagerCallback<Bundle>() {
					public void run(AccountManagerFuture<Bundle> future) {
						try {
							// If the user has authorized your application
							// to
							// use the tasks API
							// a token is available.
							String token = future.getResult().getString(
									AccountManager.KEY_AUTHTOKEN);
							// Now you can use the Tasks API...
							GoogleCredential credential = new GoogleCredential();
							credential.setAccessToken(token);

							service = new Tasks.Builder(new NetHttpTransport(),
									new JacksonFactory(), credential)
									.setApplicationName("EasyTask")
									.setTasksRequestInitializer(
											new TasksRequestInitializer(
													"AIzaSyBszB5_MtTyGtKrN_mukAvHnhE6h6Ndt6w"))
									.build();

							new Thread(taskThread).start();
						} catch (OperationCanceledException e) {
							// TODO: The user has denied you access to the
							// API,
							// you should handle that
						} catch (Exception e) {
							Log.w("error", e.getMessage());
						}
					}

				}, null);

	}
}
