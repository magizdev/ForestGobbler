package com.magizdev.dayplan;

import com.magizdev.dayplan.viewmodel.BacklogItemAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class BacklogItemActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backlog_item);
		
		ListView listView = (ListView)findViewById(R.id.listView1);
		BacklogItemAdapter adapter = new BacklogItemAdapter(this);
		listView.setAdapter(adapter);
	}


	@Override
	public void onResume() {
		super.onResume();
	}

}
