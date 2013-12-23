package com.magizdev.dayplan.versionone;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageButton;

import com.magizdev.dayplan.R;

public class ProgressDialog extends Dialog {

	private ImageButton cancelButton;

	public ProgressDialog(Context context) {
		super(context, R.style.dialog);
		this.setContentView(R.layout.dialog_view);

		cancelButton = (ImageButton) findViewById(R.id.cancel_btn);

		this.setCancelable(false);
	}

	public void setOnClickListener(
			android.view.View.OnClickListener onClickListener) {
		cancelButton.setOnClickListener(onClickListener);
	}
}
