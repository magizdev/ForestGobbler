package com.magizdev.easytask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.magizdev.easytask.util.AlarmUtil;
import com.magizdev.easytask.viewmodel.EasyTaskInfo;
import com.magizdev.easytask.viewmodel.EasyTaskUtil;

public class TaskEditActivity extends Activity implements OnClickListener {
	private final static String DATE = "yyyy/MM/dd";
	private final static String TIME = "HH:mm";

	EditText txtTitle;
	EditText txtNote;
	TimePicker timePicker;
	DatePicker datePicker;
	RelativeLayout timeEdit;
	LinearLayout areaTime;
	Button btnDate;
	Button btnTime;
	RelativeLayout areaPicker;
	Button btnSave;
	Button btnCancel;
	EasyTaskUtil util;
	long easyTaskId;
	int mAnimationTime;

	// 0, collapsed
	// 1, expanded
	int pickerStatus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_edit);
		util = new EasyTaskUtil(this);
		txtTitle = (EditText) findViewById(R.id.txtTitle);
		txtNote = (EditText) findViewById(R.id.txtNote);
		timePicker = (TimePicker) findViewById(R.id.timePicker);
		timePicker.setIs24HourView(true);
		datePicker = (DatePicker) findViewById(R.id.datePicker);
		timeEdit = (RelativeLayout) findViewById(R.id.timeEdit);
		areaTime = (LinearLayout) findViewById(R.id.areaTime);
		btnDate = (Button) findViewById(R.id.btnDate);
		btnTime = (Button) findViewById(R.id.btnTime);
		areaPicker = (RelativeLayout) findViewById(R.id.areaPicker);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		mAnimationTime = btnDate.getContext().getResources()
				.getInteger(android.R.integer.config_shortAnimTime);

		btnDate.setOnClickListener(this);
		btnTime.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnSave.setOnClickListener(this);

		Date now = new Date(System.currentTimeMillis());
		datePicker.init(now.getYear(), now.getMonth(), now.getDay(),
				new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						btnDate.setText("" + year + "/" + (monthOfYear + 1)
								+ "/" + dayOfMonth);
					}
				});

		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				btnTime.setText("" + hourOfDay + ":" + minute);
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		Intent taskIntent = getIntent();
		easyTaskId = taskIntent.getLongExtra("easyTaskId", 0L);

		datePicker.setEnabled(false);
		timePicker.setEnabled(false);

		if (easyTaskId != 0) {
			EasyTaskInfo taskInfo = util.getTask(easyTaskId);
			Date startDate = taskInfo.StartDate;
			GregorianCalendar startDateCalendar = new GregorianCalendar();
			startDateCalendar.setTime(startDate);
			txtTitle.setText(taskInfo.Title + taskInfo.Source);
			txtNote.setText(taskInfo.Note);
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE);
			SimpleDateFormat timeFormat = new SimpleDateFormat(TIME);
			btnDate.setText(dateFormat.format(startDate));
			btnTime.setText(timeFormat.format(startDate));
			timePicker.setCurrentHour(startDateCalendar
					.get(GregorianCalendar.HOUR_OF_DAY));
			timePicker.setCurrentMinute(startDateCalendar
					.get(GregorianCalendar.MINUTE));
			datePicker.updateDate(
					startDateCalendar.get(GregorianCalendar.YEAR),
					startDateCalendar.get(GregorianCalendar.MONTH),
					startDateCalendar.get(GregorianCalendar.DAY_OF_MONTH));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_task_edit, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDate:
			toggleDateEdit();
			break;
		case R.id.btnCancel:
			btnCancelClick();
			break;
		case R.id.btnSave:
			btnSaveClick();
			break;
		case R.id.btnTime:
			toggleTimeEdit();
			break;
		default:
			break;
		}

	}

	private void toggleTimeEdit() {
		hideIme();
		if (pickerStatus == 0) {
			expandDateEdit();
		}
		showPicker(2);
	}

	private void btnSaveClick() {
		Date dueDate = new Date();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(dueDate);
		calendar.set(GregorianCalendar.YEAR, datePicker.getYear());
		calendar.set(GregorianCalendar.MONTH, datePicker.getMonth());
		calendar.set(GregorianCalendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
		calendar.set(GregorianCalendar.HOUR_OF_DAY, timePicker.getCurrentHour());
		calendar.set(GregorianCalendar.MINUTE, timePicker.getCurrentMinute());
		calendar.set(GregorianCalendar.SECOND, 0);
		EasyTaskInfo task = new EasyTaskInfo(0, txtNote.getText().toString(), null,
				new Date(), calendar.getTime(), "local", null);
		util.updateTask(easyTaskId, task);
		setResult(RESULT_OK);
		AlarmUtil.updateAlarm(this);
		finish();
	}

	private void btnCancelClick() {
		setResult(RESULT_CANCELED);
		finish();
	}

	private void toggleDateEdit() {
		hideIme();
		if (pickerStatus == 0) {
			expandDateEdit();
		}
		showPicker(1);
	}

	private float areaTimeOriginalY;
	private float areaPickerOriginalY;
	LayoutParams areaPickerOriginalLayoutParams;

	private void expandDateEdit() {
		areaTimeOriginalY = areaTime.getY();
		areaTime.animate().y(txtNote.getTranslationY())
				.setDuration(mAnimationTime);
		areaPickerOriginalY = areaPicker.getY();
		areaPicker.animate()
				.y(areaTime.getTranslationY() + btnDate.getMeasuredHeight())
				.setDuration(mAnimationTime);
		areaPickerOriginalLayoutParams = areaPicker.getLayoutParams();
		areaPicker.setLayoutParams(new RelativeLayout.LayoutParams(
				new LayoutParams(LayoutParams.MATCH_PARENT, timeEdit
						.getMeasuredHeight() - areaTime.getMeasuredHeight())));
		areaPicker.forceLayout();
		pickerStatus = 1;
	}

	private void collapseDateEdit() {
		showPicker(0);
		areaTime.animate().y(areaTimeOriginalY).setDuration(mAnimationTime);
		areaPicker.animate().y(areaPickerOriginalY).setDuration(mAnimationTime);
		areaPicker.setLayoutParams(new RelativeLayout.LayoutParams(
				areaPickerOriginalLayoutParams));
		pickerStatus = 0;
	}

	private void hideIme() {
		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	// /0, hide all.
	// /1, show date picker.
	// /2, show time picker.
	private void showPicker(int option) {
		switch (option) {
		case 0:
			datePicker.animate().alpha(0).setDuration(mAnimationTime);
			datePicker.setEnabled(false);
			timePicker.animate().alpha(0).setDuration(mAnimationTime);
			timePicker.setEnabled(false);
			break;
		case 1:
			timePicker.animate().alpha(0).setDuration(mAnimationTime);
			timePicker.setEnabled(false);
			datePicker.animate().alpha(1).setDuration(mAnimationTime);
			datePicker.setEnabled(true);
			datePicker.bringToFront();
			break;
		case 2:
			datePicker.animate().alpha(0).setDuration(mAnimationTime);
			datePicker.setEnabled(false);
			timePicker.animate().alpha(1).setDuration(mAnimationTime);
			timePicker.setEnabled(true);
			timePicker.bringToFront();
			break;
		default:
			break;
		}
	}

}
