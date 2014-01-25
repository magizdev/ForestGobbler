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
import android.widget.ImageButton;
import android.widget.ListView;

import com.magizdev.babyoneday.util.ActivityUtil;
import com.magizdev.babyoneday.viewmodel.DayOneAdapter;

public class DayOneRecordFragment extends Fragment implements OnClickListener {
	private ListView taskListView;
	private DayOneAdapter adapter;
	private ActivityUtil taskUtil;
	private ImageButton btnWeiNai;
	private ImageButton btnShuiJiao;
	private ImageButton btnXiaoBian;
	private ImageButton btnDaBian;
	private ImageButton btnNaiFen;
	private ImageButton btnTiWen;
	private ImageButton btnShenGao;
	private ImageButton btnTiZhong;
	private ImageButton btnJiYi;
	private long weinaiId = 1;
	private long shuijiaoId = 2;
	private long xiaobianId = 3;
	private long dabianId = 4;
	private long naifenId = 5;
	private long tiwenId = 6;
	private long shengaoId = 7;
	private long tizhongId = 8;
	private long jiyiId = 9;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			adapter.refresh();
		}
	};

	public DayOneRecordFragment() {
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
		taskUtil = new ActivityUtil(getActivity());

		btnWeiNai = (ImageButton) rootView.findViewById(R.id.btnWeiNai);
		btnShuiJiao = (ImageButton) rootView.findViewById(R.id.btnShuiJiao);
		btnXiaoBian = (ImageButton) rootView.findViewById(R.id.btnXiaoBian);
		btnDaBian = (ImageButton) rootView.findViewById(R.id.btnDaBian);
		btnNaiFen = (ImageButton) rootView.findViewById(R.id.btnNaiFen);
		btnTiWen = (ImageButton) rootView.findViewById(R.id.btnTiWen);
		btnShenGao = (ImageButton) rootView.findViewById(R.id.btnShenGao);
		btnTiZhong = (ImageButton) rootView.findViewById(R.id.btnTiZhong);
		btnJiYi = (ImageButton) rootView.findViewById(R.id.btnShiKe);

		btnWeiNai.setOnClickListener(this);

		btnShuiJiao.setOnClickListener(this);

		btnXiaoBian.setOnClickListener(this);

		btnDaBian.setOnClickListener(this);

		btnNaiFen.setOnClickListener(this);
		btnTiWen.setOnClickListener(this);
		btnShenGao.setOnClickListener(this);
		btnTiZhong.setOnClickListener(this);
		btnJiYi.setOnClickListener(this);
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
			if (taskUtil.IsActivityWaitingForStop(weinaiId)) {
				taskUtil.StopActivity(weinaiId);
			} else {
				taskUtil.StartActivity(weinaiId);
			}
			break;
		case R.id.btnShuiJiao:
			if (taskUtil.IsActivityWaitingForStop(shuijiaoId)) {
				taskUtil.StopActivity(shuijiaoId);
			} else {
				taskUtil.StartActivity(shuijiaoId);
			}
			break;
		case R.id.btnXiaoBian:
			taskUtil.StartActivity(xiaobianId);
			break;
		case R.id.btnDaBian:
			taskUtil.StartActivity(dabianId);
			break;
		case R.id.btnNaiFen:
			taskUtil.StartActivity(naifenId);
			break;
		case R.id.btnTiWen:
			taskUtil.StartActivity(tiwenId);
			break;
		case R.id.btnShenGao:
			taskUtil.StartActivity(shengaoId);
			break;
		case R.id.btnTiZhong:
			taskUtil.StartActivity(tizhongId);
			break;
		case R.id.btnShiKe:
			taskUtil.StartActivity(jiyiId);
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
