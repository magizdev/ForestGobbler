package com.magizdev.gobbler;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.magizdev.common.util.DisplayOnce;
import com.magizdev.common.util.IDisplayOnce;

public class WelActivity extends Activity implements OnClickListener, IDisplayOnce {

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

		btnPlayHard.setOnClickListener(this);
		btnDashboard.setOnClickListener(this);
		btnPlayEndless.setOnClickListener(this);
		btnSetting.setOnClickListener(this);
		
		DisplayOnce displayOnce = new DisplayOnce(this, this);
		displayOnce.Check();

		player = MediaPlayer.create(this, R.raw.bg);
		player.setLooping(true);
		player.start();
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		int dayofyear = calendar.get(Calendar.DAY_OF_YEAR);
		int lastplay = preferences.getInt("lastplay", 0);
		if(lastplay != dayofyear){
			Editor editor = preferences.edit();
			editor.putInt("lastplay", dayofyear);
			editor.putInt("highscore", 0);
			editor.commit();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		player.pause();
	}

	@Override
	public void onClick(View v) {
		Intent gameIntent = new Intent(this, GameActivity.class);

		switch (v.getId()) {
		case R.id.play_btn_hard:
			gameIntent.putExtra(GameActivity.GAME_MODE_TAG,
					GameActivity.GAME_MODE_HARD);
			break;
		case R.id.play_btn_easy:
			gameIntent.putExtra(GameActivity.GAME_MODE_TAG,
					GameActivity.GAME_MODE_EASY);
			break;
		case R.id.play_btn_endless:
			gameIntent.putExtra(GameActivity.GAME_MODE_TAG,
					GameActivity.GAME_MODE_ENDLESS);
			break;
		case R.id.dashboard_btn:
			gameIntent = new Intent(this, DashboardActivity.class);
			break;
		case R.id.setting_btn:
			gameIntent = new Intent(this, SettingActivity.class);
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

	public void quit() {
		this.finish();
	}

	@Override
	public void OnFirstTime() {
		Dialog firstDialog = new FirstTimeDialog(this);
		firstDialog.show();
	}

}