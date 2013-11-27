package com.magizdev.gobbler;

import me.kiip.sdk.Kiip;
import me.kiip.sdk.Poptart;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.ads.AdView;
import com.kyview.AdViewLayout;
import com.magizdev.common.lib.Score;
import com.magizdev.common.view.ScoreBoard;
import com.magizdev.gobbler.kiip.KiipActivityBase;
import com.magizdev.gobbler.view.GameView;
import com.magizdev.gobbler.view.OnScoreListener;
import com.magizdev.gobbler.view.OnStateListener;
import com.magizdev.gobbler.view.OnTimerListener;
import com.magizdev.gobbler.view.OnToolsChangeListener;

public class GameActivity extends KiipActivityBase implements OnClickListener,
		OnTimerListener, OnStateListener, OnToolsChangeListener,
		OnScoreListener {

	public static final String GAME_MODE_TAG = "GAME_MODE_TAG";
	public static final int GAME_MODE_EASY = 1;
	public static final int GAME_MODE_HARD = 2;
	public static final int GAME_MODE_ENDLESS = 3;

	private ImageButton btnRefresh;
	private ImageButton btnTip;
	private GameView gameView;
	private ProgressBar progress;
	private static ResultDialog dialog;
	private TextView textRefreshNum;
	private TextView textTipNum;
	private ScoreBoard scoreBoard;

	private Score score;
	private AdView adView;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dialog = new ResultDialog(GameActivity.this, gameView, score);
			dialog.show();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent gameIntent = getIntent();
		int gameMode = gameIntent.getIntExtra(GAME_MODE_TAG, 1);
		setContentView(R.layout.game);

		LinearLayout adContainer = (LinearLayout) this
				.findViewById(R.id.adContainer);

		// AdViewTargeting.setUpdateMode(UpdateMode.EVERYTIME);
		// AdViewTargeting.setRunMode(RunMode.TEST);
		AdViewLayout adViewLayout = new AdViewLayout(this,
				"SDK201321270908095vgowmnz6v5pxht");
		adContainer.addView(adViewLayout);
		adContainer.invalidate();
		// adView = new AdView(this, AdSize.BANNER, "");
		// adContainer.addView(adView);
		// AdRequest adRequest = new AdRequest();
		// adView.loadAd(adRequest);

		btnRefresh = (ImageButton) findViewById(R.id.refresh_btn);
		btnTip = (ImageButton) findViewById(R.id.tip_btn);
		gameView = (GameView) findViewById(R.id.game_view);
		progress = (ProgressBar) findViewById(R.id.timer);
		textRefreshNum = (TextView) findViewById(R.id.text_refresh_num);
		textTipNum = (TextView) findViewById(R.id.text_tip_num);
		scoreBoard = (ScoreBoard) findViewById(R.id.scoreBoard);
		progress.setMax(gameView.getTotalTime());

		btnRefresh.setOnClickListener(this);
		btnTip.setOnClickListener(this);
		gameView.setOnTimerListener(this);
		gameView.setOnStateListener(this);
		gameView.setOnToolsChangedListener(this);
		gameView.setOnScoreListener(this);
		GameView.initSound(this);
		score = new Score();
		score.setScoreListener(scoreBoard);

		if (gameMode == GAME_MODE_EASY) {
			gameView.startPlay(GameView.EASY_MODE);
		} else if (gameMode == GAME_MODE_HARD) {
			gameView.startPlay(GameView.HARD_MODE);
		} else {
			gameView.startPlay(GameView.ENDLESS_MODE);
			progress.setMax(10);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (gameView.getMode() != GameView.WIN
				&& gameView.getMode() != GameView.LOSE) {
			gameView.setMode(GameView.PAUSE);
		} else {
			gameView.player.pause();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		gameView.setMode(GameView.QUIT);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.refresh_btn:
			Animation shake01 = AnimationUtils
					.loadAnimation(this, R.anim.shake);
			btnRefresh.startAnimation(shake01);
			gameView.refreshChange();
			break;
		case R.id.tip_btn:
			Animation shake02 = AnimationUtils
					.loadAnimation(this, R.anim.shake);
			btnTip.startAnimation(shake02);
			gameView.autoClear();
			break;
		}
	}

	@Override
	public void onTimer(int leftTime) {
		progress.setProgress(leftTime);
	}

	@Override
	public void OnStateChanged(int StateMode) {
		switch (StateMode) {
		case GameView.WIN:
			// handler.sendEmptyMessage(0);
			Kiip.getInstance().saveMoment("Win", new Kiip.Callback() {

				@Override
				public void onFinished(Kiip arg0, Poptart arg1) {
					onPoptart(arg1);
				}

				@Override
				public void onFailed(Kiip arg0, Exception arg1) {
					// TODO Auto-generated method stub

				}
			});
			break;
		case GameView.LOSE:
			handler.sendEmptyMessage(1);
			break;
		case GameView.PAUSE:
			gameView.player.pause();
			gameView.stopTimer();
			break;
		case GameView.QUIT:
			gameView.player.release();
			gameView.stopTimer();
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (gameView.getMode() == GameView.PAUSE) {
			gameView.player.start();
			gameView.startTimer();
		} else {
			gameView.player.start();
		}

	}

	@Override
	public void onRefreshChanged(int count) {
		textRefreshNum.setText("" + gameView.getRefreshNum());
	}

	@Override
	public void onTipChanged(int count) {
		textTipNum.setText("" + gameView.getTipNum());
	}

	public void quit() {
		this.finish();
	}

	@Override
	public void OnScore() {
		score.Add();
	}
}