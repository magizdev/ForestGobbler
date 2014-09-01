package com.magizdev.babyoneday;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.magizdev.babyoneday.util.DayUtil;
import com.magizdev.babyoneday.util.GrowthIndexUtil;
import com.magizdev.babyoneday.util.Profile;
import com.magizdev.babyoneday.viewmodel.GrowthIndexInfo;

public class DayOneProfileFragment extends Fragment implements
		OnDateSetListener {
	protected static final int IMAGE_REQUEST_CODE = 1;
	private static final int RESIZE_REQUEST_CODE = 2;
	private View rootView;
	private TextView name;
	private TextView gender;
	private TextView shengao;
	private TextView tizhong;
	private ImageButton pic;
	private TextView birthday;
	private Calendar calendar;
	private Profile profile;
	private String birthdayString;
	private int oldBirthday;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.activity_profile, container, false);

		pic = (ImageButton) rootView.findViewById(R.id.profile_pic);
		pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
				galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
				galleryIntent.setType("image/*");
				startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
			}
		});
		name = (TextView) rootView.findViewById(R.id.profile_name);
		gender = (TextView) rootView.findViewById(R.id.profile_gender);
		shengao = (TextView) rootView.findViewById(R.id.profile_iniShengao);
		tizhong = (TextView) rootView.findViewById(R.id.profile_iniTizhong);
		birthday = (TextView) rootView.findViewById(R.id.profile_birthday);

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

		profile = new Select().from(Profile.class).executeSingle();
		if (profile == null) {
			profile = new Profile();
			profile.birthday = DayUtil.Today();
		}
		oldBirthday = profile.birthday;
		calendar = DayUtil.toCalendar(profile.birthday);

		name.setText(profile.name);
		if (profile.height > 0) {
			shengao.setText(profile.height + "");
		}
		if (profile.weight > 0) {
			tizhong.setText(profile.weight + "");
		}
		if (profile.gender == Profile.GENDER_BOY) {
			gender.setText(R.string.gender_boy);
		} else {
			gender.setText(R.string.gender_girl);
		}
		if (profile.pic != null) {
			pic.setImageDrawable(new BitmapDrawable(profile.pic));
		}
		birthdayString = getActivity().getResources().getString(
				R.string.profile_birthday);
		birthday.setText(birthdayString + ":"
				+ DayUtil.formatCalendar(calendar));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		} else {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				resizeImage(data.getData());
				break;
			case RESIZE_REQUEST_CODE:
				if (data != null) {
					showResizeImage(data);
				}
				break;
			}
		}
	}

	private void showResizeImage(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			profile.pic = photo;
			Drawable drawable = new BitmapDrawable(photo);
			pic.setImageDrawable(drawable);
		}
	}

	public void resizeImage(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 450);
		intent.putExtra("outputY", 450);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESIZE_REQUEST_CODE);
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