package com.magizdev.easytask;

import java.util.Date;

import com.magizdev.easytask.viewmodel.EasyTaskInfo;
import com.magizdev.easytask.viewmodel.EasyTaskUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.support.v4.app.NavUtils;

public class TaskEditActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        Intent taskIntent = getIntent();
        int easyTaskId = taskIntent.getIntExtra("easyTaskId", 0);
        EasyTaskUtil util = new EasyTaskUtil(this);
        EditText noteEditText = (EditText)findViewById(R.id.editText1);
        TimePicker timePicker = (TimePicker)findViewById(R.id.timePicker1);
        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker1);
        if(easyTaskId != 0){
        	EasyTaskInfo taskInfo = util.getTask(easyTaskId);
        	Date startDate = taskInfo.StartDate;
        	noteEditText.setText(taskInfo.Note);
        	timePicker.setCurrentHour(startDate.getHours());
        	timePicker.setCurrentMinute(startDate.getMinutes());
        	datePicker.updateDate(startDate.getYear(), startDate.getMonth(), startDate.getDay());
        }
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task_edit, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
