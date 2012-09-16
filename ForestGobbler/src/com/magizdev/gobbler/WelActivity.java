package com.magizdev.gobbler;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class WelActivity extends Activity implements OnClickListener {

	private ImageButton btnPlayEasy;
	private ImageButton btnPlayHard;
	private ImageButton btnDashboard;

	private MediaPlayer player;
	private AdView adView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		LinearLayout adContainer = (LinearLayout) this
				.findViewById(R.id.adContainer);
		adView = new AdView(this, AdSize.BANNER, "a15042e99a83c4f");
		adContainer.addView(adView);
		AdRequest adRequest = new AdRequest();
		adView.loadAd(adRequest);

		btnPlayEasy = (ImageButton) findViewById(R.id.play_btn_easy);
		btnPlayHard = (ImageButton) findViewById(R.id.play_btn_hard);
		btnDashboard = (ImageButton) findViewById(R.id.dashboard_btn);

		btnPlayEasy.setOnClickListener(this);
		btnPlayHard.setOnClickListener(this);
		btnDashboard.setOnClickListener(this);
		

		Animation scale = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
		btnPlayEasy.startAnimation(scale);
		btnPlayHard.startAnimation(scale);
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
	protected void onDestroy() {
		adView.destroy();
		super.onDestroy();
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
		case R.id.dashboard_btn:
			gameIntent = new Intent(this, DashboardActivity.class);
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

}