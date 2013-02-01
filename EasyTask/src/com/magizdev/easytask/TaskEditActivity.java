package com.magizdev.easytask;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.magizdev.easytask.viewmodel.EasyTaskInfo;
import com.magizdev.easytask.viewmodel.EasyTaskUtil;

public class TaskEditActivity extends Activity implements OnClickListener {
	private final static String DATE = "MM/dd";
	private final static String TIME = "HH:mm";

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
	int itemPosition;
	int mAnimationTime;

	// 0, collapsed
	// 1, expanded
	int pickerStatus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_edit);
		Intent taskIntent = getIntent();
		easyTaskId = taskIntent.getLongExtra("easyTaskId", 0L);
		itemPosition = taskIntent.getIntExtra("clickItemPostion", 0);
		util = new EasyTaskUtil(this);
		txtNote = (EditText) findViewById(R.id.txtNote);
		timePicker = (TimePicker) findViewById(R.id.timePicker);
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

		if (easyTaskId != 0) {
			EasyTaskInfo taskInfo = util.getTask(easyTaskId);
			Date startDate = taskInfo.StartDate;
			txtNote.setText(taskInfo.Note);
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE);
			SimpleDateFormat timeFormat = new SimpleDateFormat(TIME);
			btnDate.setText(dateFormat.format(startDate));
			btnTime.setText(timeFormat.format(startDate));
			timePicker.setCurrentHour(startDate.getHours());
			timePicker.setCurrentMinute(startDate.getMinutes());
			datePicker.updateDate(startDate.getYear(), startDate.getMonth(),
					startDate.getDay());
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
		if(pickerStatus == 0){
			expandDateEdit();
		}
		showPicker(2);
	}

	private void btnSaveClick() {
		Date dueDate = new Date();
		// dueDate.setYear(datePicker.getYear());
		// dueDate.setMonth(datePicker.getMonth());
		// dueDate.setDate(datePicker.getDayOfMonth());
		// dueDate.setHours(timePicker.getCurrentHour());
		// dueDate.setMinutes(timePicker.getCurrentMinute());
		EasyTaskInfo task = new EasyTaskInfo(0, txtNote.getText().toString(),
				new Date(), dueDate);
		util.updateTask(easyTaskId, task);
		Intent result = new Intent();
		result.putExtra("itemPostion", itemPosition);
		TaskEditActivity.this.setResult(RESULT_OK, result);
		finish();
	}

	private void btnCancelClick() {
		TaskEditActivity.this.setResult(RESULT_CANCELED);
		finish();
	}

	private void toggleDateEdit() {
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
	}

	private void collapseDateEdit() {
		showPicker(0);
		areaTime.animate().y(areaTimeOriginalY).setDuration(mAnimationTime);
		areaPicker.animate().y(areaPickerOriginalY).setDuration(mAnimationTime);
		areaPicker.setLayoutParams(new RelativeLayout.LayoutParams(
				areaPickerOriginalLayoutParams));
	}

	// /0, hide all.
	// /1, show date picker.
	// /2, show time picker.
	private void showPicker(int option) {
		switch (option) {
		case 0:
			datePicker.animate().alpha(0).setDuration(mAnimationTime);
			timePicker.animate().alpha(0).setDuration(mAnimationTime);
			break;
		case 1:
			timePicker.animate().alpha(0).setDuration(mAnimationTime);
			datePicker.animate().alpha(1).setDuration(mAnimationTime);
			break;
		case 2:
			datePicker.animate().alpha(0).setDuration(mAnimationTime);
			timePicker.animate().alpha(1).setDuration(mAnimationTime);
			break;
		default:
			break;
		}
	}

}
