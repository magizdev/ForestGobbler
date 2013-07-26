package com.magizdev.gobbler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class WelActivity extends Activity implements OnClickListener {
	public final static float[] BT_SELECTED = new float[] { 2, 0, 0, 0, 2, 0,
			2, 0, 0, 2, 0, 0, 2, 0, 2, 0, 0, 0, 1, 0 };
	public final static float[] BT_NOT_SELECTED = new float[] { 1, 0, 0, 0, 0,
			0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 };

	private Button btnPlayEasy;
	private Button btnPlayHard;
	private Button btnPlayEndless;
	private Button btnDashboard;
	private Button btnSetting;

	private MediaPlayer player;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		btnPlayEasy = (Button) findViewById(R.id.play_btn_easy);
		btnPlayHard = (Button) findViewById(R.id.play_btn_hard);
		btnDashboard = (Button) findViewById(R.id.dashboard_btn);
		btnPlayEndless = (Button) findViewById(R.id.play_btn_endless);
		btnSetting = (Button) findViewById(R.id.setting_btn);

		btnPlayEasy.setOnClickListener(this);
		btnPlayEasy.setOnTouchListener(buttonOnTouchListener);
		btnPlayHard.setOnClickListener(this);
		btnPlayHard.setOnTouchListener(buttonOnTouchListener);
		btnDashboard.setOnClickListener(this);
		btnDashboard.setOnTouchListener(buttonOnTouchListener);
		btnPlayEndless.setOnClickListener(this);
		btnPlayEndless.setOnTouchListener(buttonOnTouchListener);
		btnSetting.setOnClickListener(this);
		btnSetting.setOnTouchListener(buttonOnTouchListener);

		// Animation scale = AnimationUtils.loadAnimation(this,
		// R.anim.scale_anim);
		// btnPlayEasy.startAnimation(scale);
		// btnPlayHard.startAnimation(scale);
		player = MediaPlayer.create(this, R.raw.bg);
		player.setLooping(true);
		player.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		player.pause();
	}

	@Override
	public void onClick(View v) {
		Intent gameIntent = new Intent(this, GameActivity.class);

		Animation flyOut = AnimationUtils.loadAnimation(this,
				android.R.anim.slide_out_right);

		switch (v.getId()) {
		case R.id.play_btn_hard:
			gameIntent.putExtra(GameActivity.GAME_MODE_TAG,
					GameActivity.GAME_MODE_HARD);
			btnPlayHard.startAnimation(flyOut);
			break;
		case R.id.play_btn_easy:
			gameIntent.putExtra(GameActivity.GAME_MODE_TAG,
					GameActivity.GAME_MODE_EASY);
			btnPlayEasy.startAnimation(flyOut);
			break;
		case R.id.play_btn_endless:
			gameIntent.putExtra(GameActivity.GAME_MODE_TAG,
					GameActivity.GAME_MODE_ENDLESS);
			btnPlayEndless.startAnimation(flyOut);
			break;
		case R.id.dashboard_btn:
			gameIntent = new Intent(this, DashboardActivity.class);
			btnDashboard.startAnimation(flyOut);
			break;
		case R.id.setting_btn:
			gameIntent = new Intent(this, SettingActivity.class);
			btnSetting.startAnimation(flyOut);
			break;
		}

		player.pause();

		startActivity(gameIntent);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	@Override
	public void onResume() {
		super.onResume();
		player.start();

	}

	public final static OnTouchListener buttonOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.getBackground().setColorFilter(
						new ColorMatrixColorFilter(BT_SELECTED));
				v.setBackgroundDrawable(v.getBackground());
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.getBackground().setColorFilter(
						new ColorMatrixColorFilter(BT_NOT_SELECTED));
				v.setBackgroundDrawable(v.getBackground());
			}
			return false;
		}

	};

	public void quit() {
		this.finish();
	}

}