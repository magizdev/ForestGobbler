package com.magizdev.dayplan.view;

import org.achartengine.GraphicalView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.util.INavigate;

public abstract class BaseChartFragment extends Fragment {
	protected INavigate navigate;
	private TextView chartTitle;
	private OnClickListener onClickListener;
	protected static int[] COLORS = new int[] { 0xffB2C938, 0xff3BA9B8,
			0xffFF9910, 0xffC74C47, 0xff5B1A69, 0xffA83AAE, 0xffF981C5 };

	public void setDataSource(INavigate naviate) {
		this.navigate = naviate;
	}
	
	public void setOnClick(OnClickListener onClickListener){
		this.onClickListener = onClickListener;
	}

	public BaseChartFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_dashboard, container, false);


		buildChart(rootView);

		return rootView;
	}

	private void buildChart(ViewGroup rootView) {
		chartTitle = (TextView) rootView.findViewById(R.id.chartTitle);
		chartTitle.setText(navigate.CurrentTitle());

		LinearLayout chartArea = (LinearLayout) rootView
				.findViewById(R.id.chartArea);
		chartArea.addView(GetChart(), new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	protected abstract GraphicalView GetChart();

}
