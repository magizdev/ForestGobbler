package com.magizdev.babyoneday;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import com.magizdev.babyoneday.viewmodel.BacklogItemInfo;
import com.magizdev.babyoneday.viewmodel.StorageUtil;

public class BacklogEditActivity extends Activity {

	EditText txtTitle;
	EditText txtNote;
	CheckBox completeCheckBox;
	StorageUtil<BacklogItemInfo> util;
	long backlogId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backlog_edit);
		util = new StorageUtil<BacklogItemInfo>(this, new BacklogItemInfo());
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		txtTitle = (EditText) findViewById(R.id.txtTitle);
		txtNote = (EditText) findViewById(R.id.txtNote);
		completeCheckBox = (CheckBox) findViewById(R.id.checkBoxCompleted);

	}

	@Override
	public void onResume() {
		super.onResume();

		Intent taskIntent = getIntent();
		backlogId = taskIntent.getLongExtra("backlogId", 0L);

		if (backlogId != 0) {
			BacklogItemInfo backlog = util.getSingle(backlogId);
			txtTitle.setText(backlog.Name);
			txtNote.setText(backlog.Description);
			completeCheckBox.setChecked(backlog.Completed);
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
		BacklogItemInfo backlog = new BacklogItemInfo(0, txtTitle.getText()
				.toString(), txtNote.getText().toString(),
				completeCheckBox.isChecked());
		util.update(backlogId, backlog);
		setResult(RESULT_OK);
		finish();
	}

	private void btnCancelClick() {
		setResult(RESULT_CANCELED);
		finish();
	}

}
