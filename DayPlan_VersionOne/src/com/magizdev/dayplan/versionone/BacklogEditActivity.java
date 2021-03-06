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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.versionone.model.BacklogItem;
import com.magizdev.dayplan.versionone.store.StorageUtil;
import com.magizdev.dayplan.versionone.util.DayUtil;

public class BacklogEditActivity extends Activity implements OnDateSetListener {

	EditText txtTitle;
	EditText txtNote;
	EditText txtEstimate;
	Button dueDate;
	CheckBox completeCheckBox;
	CheckBox dueDateEnable;
	StorageUtil<BacklogItem> util;
	Calendar calendar;
	long backlogId;
	private TextView estimateLabel;
	private CheckBox estimateEnableBox;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backlog_edit);
		util = new StorageUtil<BacklogItem>(this, new BacklogItem());
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		txtTitle = (EditText) findViewById(R.id.txtTitle);
		calendar = Calendar.getInstance();
		txtNote = (EditText) findViewById(R.id.txtNote);
		txtEstimate = (EditText) findViewById(R.id.estimate);
		estimateLabel = (TextView) findViewById(R.id.backlog_estimate_unit_label);
		estimateEnableBox = (CheckBox) findViewById(R.id.checkBoxEstimate);
		estimateEnableBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						int visibility = isChecked ? View.VISIBLE
								: View.INVISIBLE;
						estimateLabel.setVisibility(visibility);
						txtEstimate.setVisibility(visibility);
					}
				});
		dueDate = (Button) findViewById(R.id.dueDate);
		dueDateEnable = (CheckBox) findViewById(R.id.checkBoxDueDate);
		dueDateEnable.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				int visibility = isChecked ? View.VISIBLE : View.INVISIBLE;
				dueDate.setVisibility(visibility);
			}
		});
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
			BacklogItem backlog = util.getSingle(backlogId);
			if (backlog.HasDueDate()) {
				calendar = DayUtil.toCalendar(backlog.DueDate);
				dueDate.setText(DayUtil.formatCalendar(calendar));
			} else {
				calendar = DayUtil.toCalendar(DayUtil.Today());
				dueDate.setText(DayUtil.formatCalendar(calendar));
			}
			txtTitle.setText(backlog.Name);
			txtNote.setText(backlog.Description);
			txtEstimate.setText(backlog.Estimate + "");
			completeCheckBox.setChecked(backlog.Completed);
			dueDateEnable.setChecked(!backlog.HasDueDate());
			dueDateEnable.setChecked(backlog.HasDueDate());
			estimateEnableBox.setChecked(!backlog.HasEstimate());
			estimateEnableBox.setChecked(backlog.HasEstimate());
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
		int dueDate = dueDateEnable.isChecked() ? DayUtil.toDate(calendar
				.getTime()) : 0;
		float estimate = estimateEnableBox.isChecked() ? Float
				.parseFloat(txtEstimate.getText().toString()) : 0;
		BacklogItem backlog = new BacklogItem(0, txtTitle.getText().toString(),
				txtNote.getText().toString(), completeCheckBox.isChecked(),
				estimate, dueDate);
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
