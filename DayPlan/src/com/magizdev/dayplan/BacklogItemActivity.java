package com.magizdev.dayplan;

import com.magizdev.dayplan.util.DayTaskUtil;
import com.magizdev.dayplan.viewmodel.BacklogItemAdapter;
import com.magizdev.dayplan.viewmodel.BacklogItemInfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

public class BacklogItemActivity extends Activity {
	private DayTaskUtil dayTaskUtil;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backlog_item);
		
		ListView listView = (ListView) findViewById(R.id.listView1);
		BacklogItemAdapter adapter = new BacklogItemAdapter(this);
		listView.setAdapter(adapter);
		dayTaskUtil = new DayTaskUtil(this);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_backlog_item, menu);

		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_pickup:
	            addTasks();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void addTasks(){
		ListView listView = (ListView) findViewById(R.id.listView1);
		ListAdapter adapter = listView.getAdapter();
		int count = adapter.getCount() - 1;
		for(int i=0; i<count; i++){
			BacklogItemInfo backlogItemInfo = (BacklogItemInfo)adapter.getItem(i);
			if(backlogItemInfo.Selected){
				dayTaskUtil.AddTask(backlogItemInfo.Id);
			}
		}
		finish();
	}

}
