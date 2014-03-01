package com.magizdev.dayplan.versionone;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.versionone.util.DayUtil;
import com.magizdev.dayplan.versionone.viewmodel.BacklogItemInfo;
import com.magizdev.dayplan.versionone.viewmodel.StorageUtil;

public class BacklogEditActivity extends Activity implements OnDateSetListener {

	EditText txtTitle;
	EditText txtNote;
	EditText txtEstimate;
	Button dueDate;
	CheckBox completeCheckBox;
	CheckBox dueDateEnable;
	StorageUtil<BacklogItemInfo> util;
	Calendar calendar;
	long backlogId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backlog_edit);
		util = new StorageUtil<BacklogItemInfo>(this, new BacklogItemInfo());
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		txtTitle = (EditText) findViewById(R.id.txtTitle);
		calendar = Calendar.getInstance();
		txtNote = (EditText) findViewById(R.id.txtNote);
		txtEstimate = (EditText) findViewById(R.id.estimate);
		dueDate = (Button) findViewById(R.id.dueDate);
		dueDateEnable = (CheckBox) findViewById(R.id.checkBoxDueDate);
		dueDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerDialog dialog = new DatePickerDialog(
						BacklogEditActivity.this, BacklogEditActivity.this,
						calendar.get(Calendar.YEAR), calendar
								.get(Calendar.MONTH), calendar
								.get(Calendar.DAY_OF_MONTH));
				dialog.show();

			}
		});
		completeCheckBox = (CheckBox) findViewById(R.id.checkBoxCompleted);

	}

	@Override
	public void onResume() {
		super.onResume();

		Intent taskIntent = getIntent();
		backlogId = taskIntent.getLongExtra("backlogId", 0L);

		if (backlogId != 0) {
			BacklogItemInfo backlog = util.getSingle(backlogId);
			if (backlog.HasDueDate()) {
				calendar = DayUtil.toCalendar(backlog.DueDate);
				dueDate.setText(DayUtil.formatCalendar(calendar));
			}
			txtTitle.setText(backlog.Name);
			txtNote.setText(backlog.Description);
			txtEstimate.setText(backlog.Estimate + "");
			completeCheckBox.setChecked(backlog.Completed);
			dueDateEnable.setChecked(backlog.HasDueDate());
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_backlog_item_edit, menu);

		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_cancel:
			btnCancelClick();
			return true;
		case R.id.action_save:
			btnSaveClick();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void btnSaveClick() {
		int dueDate = dueDateEnable.isChecked() ? DayUtil.toDate(calendar.getTime()) : 0;
		BacklogItemInfo backlog = new BacklogItemInfo(0, txtTitle.getText()
				.toString(), txtNote.getText().toString(),
				completeCheckBox.isChecked(), Float.parseFloat(txtEstimate
						.getText().toString()), dueDate);
		util.update(backlogId, backlog);
		setResult(RESULT_OK);
		finish();
	}

	private void btnCancelClick() {
		setResult(RESULT_CANCELED);
		finish();
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
		dueDate.setText(DayUtil.formatCalendar(calendar));
	}

}
