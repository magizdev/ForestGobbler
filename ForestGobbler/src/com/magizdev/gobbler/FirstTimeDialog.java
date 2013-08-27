package com.magizdev.gobbler;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FirstTimeDialog extends Dialog implements OnClickListener {

	private EditText userName;
	private Button okBtn;
	private SharedPreferences pref;

	public FirstTimeDialog(Context context) {
		super(context, R.style.dialog);
		this.setContentView(R.layout.first_time_dialog);
		userName = (EditText) findViewById(R.id.user_name);
		okBtn = (Button) findViewById(R.id.user_name_ok);
		pref = PreferenceManager.getDefaultSharedPreferences(context);
		String username = pref.getString("USER_NAME", "0");
		if (username != "0") {
			userName.setText(username);
		}
		okBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String username=userName.getText().toString();
		if(username.length() > 0){
			Editor editor = pref.edit();
			editor.putString("USER_NAME", username);
			editor.commit();
		}
	}
}
