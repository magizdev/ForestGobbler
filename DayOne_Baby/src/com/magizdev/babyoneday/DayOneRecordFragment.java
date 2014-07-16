package com.magizdev.babyoneday;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.magizdev.babyoneday.util.ActivityUtil;
import com.magizdev.babyoneday.viewmodel.DayOneAdapter;

public class DayOneRecordFragment extends Fragment implements OnClickListener {
	private static final int TAB_ACTIVITY = 1;
	private static final int TAB_GROWTHINDEX = 2;
	private ListView taskListView;
	private DayOneAdapter adapter;
	private ActivityUtil taskUtil;
	private Button btnWeiNai;
	private Button btnShuiJiao;
	private Button btnXiaoBian;
	private Button btnDaBian;
	private Button btnNaiFen;
	private Button btnTiWen;
	private Button btnHeight;
	private Button btnWeight;
	private Button btnHeadGirth;
	private long breadId = 1;
	private long sleepId = 2;
	private long peeId = 3;
	private long pooId = 4;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			adapter.refresh();
		}
	};
	private TabHost tabHost;

	public DayOneRecordFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_day_plan_new, container,
				false);

		tabHost = (TabHost)rootView.findViewById(android.R.id.tabhost);
		tabHost.setup();
		setupTabs();
		taskListView = (ListView) rootView.findViewById(R.id.listViewDayPlan);
		taskListView.setBackgroundResource(R.drawable.widget_bg);
		adapter = new DayOneAdapter(getActivity());
		taskListView.setAdapter(adapter);
		taskUtil = new ActivityUtil(getActivity());

		btnWeiNai = (Button) rootView.findViewById(R.id.btnWeiNai);
		btnShuiJiao = (Button) rootView.findViewById(R.id.btnShuiJiao);
		btnXiaoBian = (Button) rootView.findViewById(R.id.btnXiaoBian);
		btnDaBian = (Button) rootView.findViewById(R.id.btnDaBian);
		btnHeight = (Button) rootView.findViewById(R.id.btnHeight);
		btnWeight = (Button) rootView.findViewById(R.id.btnWeight);
		btnHeadGirth = (Button) rootView.findViewById(R.id.btnHeadGirth);

		btnWeiNai.setOnClickListener(this);

		btnShuiJiao.setOnClickListener(this);

		btnXiaoBian.setOnClickListener(this);

		btnDaBian.setOnClickListener(this);

		btnHeight.setOnClickListener(this);
		btnWeight.setOnClickListener(this);
		btnHeadGirth.setOnClickListener(this);
		taskListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				adapter.setEditMode(arg2);
				return false;
			}
		});
		return rootView;
	}
	
	

	@Override
	public void onResume() {
		super.onResume();
		adapter.refresh();
		// handler.sendEmptyMessageDelayed(0, 60000);
	}

	@Override
	public void onPause() {
		super.onPause();
		// handler.removeMessages(0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnWeiNai:
			if (taskUtil.IsActivityWaitingForStop(breadId)) {
				taskUtil.StopActivity(breadId);
			} else {
				taskUtil.StartActivity(breadId);
			}
			break;
		case R.id.btnShuiJiao:
			if (taskUtil.IsActivityWaitingForStop(sleepId)) {
				taskUtil.StopActivity(sleepId);
			} else {
				taskUtil.StartActivity(sleepId);
			}
			break;
		case R.id.btnXiaoBian:
			taskUtil.StartActivity(peeId);
			break;
		case R.id.btnDaBian:
			taskUtil.StartActivity(pooId);
			break;
		case R.id.btnHeight:
			
			break;
		case R.id.btnWeight:
			
			break;
		case R.id.btnHeadGirth:
			
			break;
		default:
			break;
		}
		adapter.refresh();

		taskListView.setSelection(adapter.getCount() - 1);
	}

	private void setupTabs() {
		tabHost.setup();
		tabHost.addTab(newTab(TAB_ACTIVITY, "tab1"));
		tabHost.addTab(newTab(TAB_GROWTHINDEX, "tab2"));
	}
	
	private TabSpec newTab(int tag, String label) {
		TabSpec tabSpec = tabHost.newTabSpec(String.valueOf(tag));
		tabSpec.setIndicator(label);
		switch (tag) {
		case 1:
			tabSpec.setContent(R.id.tab1);
			break;
		case 2:
			tabSpec.setContent(R.id.tab2);
			break;
		default:
			break;
		}
		return tabSpec;
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.activity_day_plan, menu);
	//
	// return super.onCreateOptionsMenu(menu);
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle presses on the action bar items
	// Intent intent = new Intent();
	// switch (item.getItemId()) {
	// case R.id.action_mark:
	// intent.setClass(this, BacklogItemActivity.class);
	// startActivity(intent);
	// return true;
	// case R.id.action_report:
	// intent.setClass(this, PieChartBuilder.class);
	// startActivity(intent);
	// return true;
	// default:
	// return super.onOptionsItemSelected(item);
	// }
	// }

}
