package com.magizdev.babyoneday;

import java.util.Calendar;

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

import com.activeandroid.query.Select;
import com.magizdev.babyoneday.util.DayUtil;
import com.magizdev.babyoneday.util.Profile;

public class DayOneProfileFragment extends Fragment implements
		OnDateSetListener {
	protected static final int IMAGE_REQUEST_CODE = 1;
	private static final int RESIZE_REQUEST_CODE = 2;
	private View rootView;
	private EditText name;
	private RadioButton gender_boy;
	private RadioButton gender_girl;
	private EditText shengao;
	private EditText tizhong;
	private ImageButton pic;
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
		name = (EditText) rootView.findViewById(R.id.profile_name);
		gender_boy = (RadioButton) rootView
				.findViewById(R.id.profile_gender_boy);
		gender_girl = (RadioButton) rootView
				.findViewById(R.id.profile_gentder_girl);
		shengao = (EditText) rootView.findViewById(R.id.profile_iniShengao);
		tizhong = (EditText) rootView.findViewById(R.id.profile_iniTizhong);
		update = (Button) rootView.findViewById(R.id.profile_update);
		birthday = (Button) rootView.findViewById(R.id.profile_birthday);
		profile = new Select().from(Profile.class).executeSingle();
		if(profile == null){
			profile = new Profile();
		}
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
				profile.save();
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
		if(profile.pic != null){
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
        intent.putExtra("outputX", 150);  
        intent.putExtra("outputY", 150);  
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