package com.magizdev.babyoneday;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.magizdev.babyoneday.util.DayTaskUtil;
import com.magizdev.babyoneday.viewmodel.DayOneAdapter;
import com.magizdev.babyoneday.viewmodel.DayTaskTimeInfo.TimeType;

public class DayPlanFragment extends Fragment implements OnClickListener {
	private ListView taskListView;
	private DayOneAdapter adapter;
	private DayTaskUtil taskUtil;
	private ImageButton btnWeiNai;
	private ImageButton btnShuiJiao;
	private ImageButton btnXiaoBian;
	private ImageButton btnDaBian;
	private long weinaiId = 1;
	private long shuijiaoId = 2;
	private long xiaobianId = 3;
	private long dabianId = 4;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			adapter.refresh();
		}
	};

	public DayPlanFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_day_plan, container,
				false);

		taskListView = (ListView) rootView.findViewById(R.id.listViewDayPlan);
		taskListView.setBackgroundResource(R.drawable.widget_bg);
		adapter = new DayOneAdapter(getActivity());
		taskListView.setAdapter(adapter);
		taskUtil = new DayTaskUtil(getActivity());

		btnWeiNai = (ImageButton) rootView.findViewById(R.id.btnWeiNai);
		btnShuiJiao = (ImageButton) rootView.findViewById(R.id.btnShuiJiao);
		btnXiaoBian = (ImageButton) rootView.findViewById(R.id.btnXiaoBian);
		btnDaBian = (ImageButton) rootView.findViewById(R.id.btnDaBian);

		btnWeiNai.setOnClickListener(this);

		btnShuiJiao.setOnClickListener(this);

		btnXiaoBian.setOnClickListener(this);

		btnDaBian.setOnClickListener(this);
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
		TimeType timeType;
		switch (v.getId()) {
		case R.id.btnWeiNai:
			timeType = taskUtil.GetTaskState(weinaiId);
			if (timeType == TimeType.Stop) {
				taskUtil.StartTask(weinaiId);
			} else {
				taskUtil.StopTask(weinaiId);
			}
			break;
		case R.id.btnShuiJiao:
			timeType = taskUtil.GetTaskState(shuijiaoId);
			if (timeType == TimeType.Stop) {
				taskUtil.StartTask(shuijiaoId);
			} else {
				taskUtil.StopTask(shuijiaoId);
			}
			break;
		case R.id.btnXiaoBian:
			taskUtil.TickTask(xiaobianId);
			break;
		case R.id.btnDaBian:
			taskUtil.TickTask(dabianId);
			break;
		default:
			break;
		}
		adapter.refresh();

		taskListView.setSelection(adapter.getCount() - 1);
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
