package com.magizdev.babyoneday;

import org.achartengine.GraphicalView;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.magizdev.babyoneday.util.ActivityUtil;
import com.magizdev.babyoneday.util.GrowthIndexUtil;
import com.magizdev.babyoneday.util.Profile;
import com.magizdev.babyoneday.view.ChartView;
import com.magizdev.babyoneday.viewmodel.DayOneAdapter;

public class DayOneRecordFragment extends Fragment implements OnClickListener {
	private static final int TAB_ACTIVITY = 1;
	private static final int TAB_HEIGHT = 2;
	private static final int TAB_WEIGHT = 3;
	private ListView taskListView;
	private DayOneAdapter adapter;
	private ActivityUtil taskUtil;
	private ImageButton btnWeiNai;
	private ImageButton btnShuiJiao;
	private ImageButton btnXiaoBian;
	private ImageButton btnDaBian;

	private Button btnHeight;
	private EditText heightInput;
	private EditText weightInput;
	private EditText dateInput;
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
	private RelativeLayout chartAreaHeight;
	private GraphicalView chartHeight;
	private ChartView heightChart;
	private RelativeLayout chartAreWeight;
	private ChartView weightChart;
	private GraphicalView chartWeight;
	private LinearLayout giInputLayout1;
	private LinearLayout giInputLayout2;

	public DayOneRecordFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_day_plan_new,
				container, false);

		tabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
		tabHost.setup();
		setupTabs();
		taskListView = (ListView) rootView.findViewById(R.id.listViewDayPlan);
		taskListView.setBackgroundResource(R.drawable.widget_bg);
		adapter = new DayOneAdapter(getActivity());
		taskListView.setAdapter(adapter);
		taskUtil = new ActivityUtil(getActivity());

		btnWeiNai = (ImageButton) rootView.findViewById(R.id.btnWeiNai);
		btnShuiJiao = (ImageButton) rootView.findViewById(R.id.btnShuiJiao);
		btnXiaoBian = (ImageButton) rootView.findViewById(R.id.btnXiaoBian);
		btnDaBian = (ImageButton) rootView.findViewById(R.id.btnDaBian);
		btnHeight = (Button) rootView.findViewById(R.id.btnHeight);
		heightInput = (EditText) rootView.findViewById(R.id.heightInput);
		weightInput = (EditText) rootView.findViewById(R.id.weightInput);
		dateInput = (EditText) rootView.findViewById(R.id.testDateInput);
		giInputLayout1 = (LinearLayout) rootView.findViewById(R.id.giInputArea);
		giInputLayout2 = (LinearLayout) rootView
				.findViewById(R.id.giInputArea2);

		chartAreaHeight = (RelativeLayout) rootView
				.findViewById(R.id.chartAreaHeight);
		heightChart = new ChartView(getActivity());
		chartHeight = heightChart.GetChart(Profile.Instance().gender,
				GrowthIndexUtil.GI_HEIGHT);
		chartAreaHeight.addView(chartHeight);

		chartAreWeight = (RelativeLayout) rootView
				.findViewById(R.id.chartAreaWeight);
		weightChart = new ChartView(getActivity());
		chartWeight = weightChart.GetChart(Profile.Instance().gender,
				GrowthIndexUtil.GI_WEIGHT);
		chartAreWeight.addView(chartWeight);

		btnWeiNai.setOnClickListener(this);

		btnShuiJiao.setOnClickListener(this);

		btnXiaoBian.setOnClickListener(this);

		btnDaBian.setOnClickListener(this);

		btnHeight.setOnClickListener(this);
		taskListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				View deleteView = arg1.findViewById(R.id.btnDelete);
				deleteView.setScaleX(0);
				deleteView.setVisibility(View.VISIBLE);
				deleteView.setPivotX(200);
				deleteView
						.animate()
						.setInterpolator(new OvershootInterpolator())
						.scaleX(1).start();
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
			float height = Float.parseFloat(heightInput.getText().toString());
			int date = Integer.parseInt(dateInput.getText().toString());
			GrowthIndexUtil.addRecord(GrowthIndexUtil.GI_HEIGHT, height,
					Profile.Instance().birthday + date);
			heightChart.addData(date, height);
			giInputLayout1.setPivotY(1000);
			giInputLayout1.animate().scaleY(0).setInterpolator(new DecelerateInterpolator()).start();
			break;
		case R.id.btnWeight:
			float weight = Float.parseFloat(weightInput.getText().toString());
			int date2 = Integer.parseInt(dateInput.getText().toString());
			GrowthIndexUtil.addRecord(GrowthIndexUtil.GI_WEIGHT, weight,
					Profile.Instance().birthday + date2);
			break;
		default:
			break;
		}
		adapter.refresh();

		taskListView.setSelection(adapter.getCount() - 1);
	}

	private void setupTabs() {
		tabHost.setup();
		tabHost.addTab(newTab(TAB_ACTIVITY));
		tabHost.addTab(newTab(TAB_HEIGHT));
		tabHost.addTab(newTab(TAB_WEIGHT));
	}

	private TabSpec newTab(int tag) {
		String label = "";
		TabSpec tabSpec = tabHost.newTabSpec(String.valueOf(tag));

		switch (tag) {
		case 1:
			label = getActivity().getResources().getString(
					R.string.tab_activity);
			tabSpec.setContent(R.id.tab1);
			break;
		case 2:
			label = getActivity().getResources().getString(R.string.tab_height);
			tabSpec.setContent(R.id.tab2);
			break;
		case 3:
			label = getActivity().getResources().getString(R.string.tab_weight);
			tabSpec.setContent(R.id.tab3);
			break;
		default:
			break;
		}
		tabSpec.setIndicator(label);
		return tabSpec;
	}

}
