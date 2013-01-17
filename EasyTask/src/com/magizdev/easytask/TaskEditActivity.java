package com.magizdev.easytask;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.magizdev.easytask.viewmodel.EasyTaskInfo;
import com.magizdev.easytask.viewmodel.EasyTaskUtil;

public class TaskEditActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        Intent taskIntent = getIntent();
        final long easyTaskId = taskIntent.getLongExtra("easyTaskId", 0L);
        final int itemPosition = taskIntent.getIntExtra("clickItemPostion", 0);
        final EasyTaskUtil util = new EasyTaskUtil(this);
        final EditText noteEditText = (EditText)findViewById(R.id.editText1);
        final TimePicker timePicker = (TimePicker)findViewById(R.id.timePicker1);
        final DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker1);
        Button doneBtn = (Button)findViewById(R.id.doneBtn);
        Button cancelBtn = (Button)findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				TaskEditActivity.this.setResult(RESULT_CANCELED);
				finish();
			}});
        if(easyTaskId != 0){
        	EasyTaskInfo taskInfo = util.getTask(easyTaskId);
        	Date startDate = taskInfo.StartDate;
        	noteEditText.setText(taskInfo.Note);
        	timePicker.setCurrentHour(startDate.getHours());
        	timePicker.setCurrentMinute(startDate.getMinutes());
        	datePicker.updateDate(startDate.getYear(), startDate.getMonth(), startDate.getDay());
        	doneBtn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Date dueDate = new Date();
					dueDate.setYear(datePicker.getYear());
					dueDate.setMonth(datePicker.getMonth());
					dueDate.setDate(datePicker.getDayOfMonth());
					dueDate.setHours(timePicker.getCurrentHour());
					dueDate.setMinutes(timePicker.getCurrentMinute());
					EasyTaskInfo task = new EasyTaskInfo(0, noteEditText.getText().toString(), new Date(), dueDate);
					util.updateTask(easyTaskId, task);
					Intent result = new Intent();
					result.putExtra("itemPostion", itemPosition);
					TaskEditActivity.this.setResult(RESULT_OK, result);
					finish();
				}});
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_task_edit, menu);
        return true;
    }

}
