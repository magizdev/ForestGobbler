/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.magizdev.babyoneday.profilewizard;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.wizardpager.wizard.ui.PageFragmentCallbacks;
import com.magizdev.babyoneday.R;
import com.magizdev.babyoneday.util.DayUtil;
import com.magizdev.babyoneday.util.Profile;

public class BirthDataFragment extends Fragment implements OnDateSetListener {
	private static final String ARG_KEY = "key";

	private PageFragmentCallbacks mCallbacks;
	private String mKey;
	private BirthDataPage mPage;
	private TextView mHeightView;
	private TextView mWeightView;
	private ImageButton mBirthdayBtn;
	private TextView mBirthday;
	private Calendar calendar;

	private String birthdayString;

	public static BirthDataFragment create(String key) {
		Bundle args = new Bundle();
		args.putString(ARG_KEY, key);

		BirthDataFragment fragment = new BirthDataFragment();
		fragment.setArguments(args);
		return fragment;
	}

	public BirthDataFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		mKey = args.getString(ARG_KEY);
		mPage = (BirthDataPage) mCallbacks.onGetPage(mKey);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater
				.inflate(
						com.magizdev.babyoneday.R.layout.fragment_profilewizard_birthdata,
						container, false);
		((TextView) rootView.findViewById(android.R.id.title)).setText(mPage
				.getTitle());

		mHeightView = ((TextView) rootView
				.findViewById(com.magizdev.babyoneday.R.id.profile_height_edittext));
		mHeightView.setText(Float.toString(mPage.getData().getFloat(
				Profile.HEIGHT)));

		mWeightView = (TextView) rootView
				.findViewById(com.magizdev.babyoneday.R.id.profile_weight_edittext);
		mWeightView.setText(Float.toString(mPage.getData().getFloat(
				Profile.WEIGHT)));

		mBirthday = (TextView)rootView.findViewById(R.id.profile_birthday);
		mBirthdayBtn = (ImageButton) rootView
				.findViewById(com.magizdev.babyoneday.R.id.profile_birthday_set);
		int intBirthday = mPage.getData().getInt(Profile.BIRTHDAY);
		calendar = DayUtil.toCalendar(intBirthday);

		mBirthday.setText(birthdayString + ":"
				+ DayUtil.formatCalendar(calendar));

		mBirthdayBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerDialog dialog = new DatePickerDialog(getActivity(),
						BirthDataFragment.this, calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH), calendar
								.get(Calendar.DAY_OF_MONTH));
				dialog.show();
			}
		});
		birthdayString = getActivity().getResources().getString(
				com.magizdev.babyoneday.R.string.profile_birthday);

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (!(activity instanceof PageFragmentCallbacks)) {
			throw new ClassCastException(
					"Activity must implement PageFragmentCallbacks");
		}

		mCallbacks = (PageFragmentCallbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mHeightView.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i,
					int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1,
					int i2) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				float height = 0;
				try {
					height = Float.parseFloat(editable.toString());
				} catch (Exception e) {
				}
				mPage.getData().putFloat(Profile.HEIGHT,
						(editable != null) ? height : 0);
				mPage.notifyDataChanged();
			}
		});

		mWeightView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				float weight = 0;
				try {
					weight = Float.parseFloat(arg0.toString());
				} catch (Exception e) {

				}
				mPage.getData().putFloat(Profile.WEIGHT,
						(arg0 != null) ? weight : 0);
				mPage.notifyDataChanged();
			}
		});
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);

		// In a future update to the support library, this should override
		// setUserVisibleHint
		// instead of setMenuVisibility.
		if (mHeightView != null) {
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (!menuVisible) {
				imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
			}
		}
	}

	@Override
	public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
		calendar.set(Calendar.YEAR, arg1);
		calendar.set(Calendar.MONTH, arg2);
		calendar.set(Calendar.DAY_OF_MONTH, arg3);
		mBirthday.setText(birthdayString + ":"
				+ DayUtil.formatCalendar(calendar));

		mPage.getData().putInt(Profile.BIRTHDAY,
				DayUtil.toDate(calendar.getTime()));
		mPage.notifyDataChanged();

	}
}
