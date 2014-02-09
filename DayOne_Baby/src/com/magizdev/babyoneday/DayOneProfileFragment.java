package com.magizdev.babyoneday;

import java.util.Calendar;

import com.magizdev.babyoneday.util.DayUtil;
import com.magizdev.babyoneday.util.Profile;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class DayOneProfileFragment extends Fragment implements
		OnDateSetListener {
	private View rootView;
	private EditText name;
	private RadioButton gender_boy;
	private RadioButton gender_girl;
	private EditText shengao;
	private EditText tizhong;
	private Button update;
	private Button birthday;
	private Calendar calendar;
	private Profile profile;
	private String birthdayString;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.activity_profile, container, false);

		name = (EditText) rootView.findViewById(R.id.profile_name);
		gender_boy = (RadioButton) rootView
				.findViewById(R.id.profile_gender_boy);
		gender_girl = (RadioButton) rootView
				.findViewById(R.id.profile_gentder_girl);
		shengao = (EditText) rootView.findViewById(R.id.profile_iniShengao);
		tizhong = (EditText) rootView.findViewById(R.id.profile_iniTizhong);
		update = (Button) rootView.findViewById(R.id.profile_update);
		birthday = (Button) rootView.findViewById(R.id.profile_birthday);
		profile = new Profile(getActivity());
		calendar = DayUtil.toCalendar(profile.birthday);

		birthday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerDialog dialog = new DatePickerDialog(getActivity(),
						DayOneProfileFragment.this,
						calendar.get(Calendar.YEAR), calendar
								.get(Calendar.MONTH), calendar
								.get(Calendar.DAY_OF_MONTH));
				dialog.show();
			}
		});

		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				profile.name = name.getText().toString();
				profile.shengao = Float
						.parseFloat(shengao.getText().toString());
				profile.tizhong = Float
						.parseFloat(tizhong.getText().toString());
				profile.birthday = DayUtil.toDate(calendar.getTime());
				profile.gender = gender_boy.isChecked() ? 0 : 1;
				profile.Update();
			}
		});

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

	}

	@Override
	public void onResume() {
		super.onResume();
		
		name.setText(profile.name);
		if (profile.shengao > 0) {
			shengao.setText(profile.shengao + "");
		}
		if (profile.tizhong > 0) {
			tizhong.setText(profile.tizhong + "");
		}
		if (profile.gender == 0) {
			gender_boy.setChecked(true);
		} else {
			gender_girl.setChecked(true);
		}
		birthdayString = getActivity().getResources().getString(
				R.string.profile_birthday);
		birthday.setText(birthdayString + ":"
				+ DayUtil.formatCalendar(calendar));
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, monthOfYear);
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		birthday.setText(birthdayString + ":"
				+ DayUtil.formatCalendar(calendar));
	}
}