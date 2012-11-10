package com.magizdev.gobbler;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class DashboardActivity extends Activity {
	public static final String HIGH_SCORE_1 = "HIGH_SCORE_1";
	public static final String HIGH_SCORE_2 = "HIGH_SCORE_2";
	public static final String HIGH_SCORE_3 = "HIGH_SCORE_3";
	public static final String HIGH_SCORE_4 = "HIGH_SCORE_4";
	public static final String HIGH_SCORE_5 = "HIGH_SCORE_5";
	public static final String STAR_ACHIEVEMENT = "STAR_ACHIEVEMENT";

	private SharedPreferences prefs;
	private TextView highScore;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		setHighScore(R.id.highScore1, HIGH_SCORE_1, 1);
		setHighScore(R.id.highScore2, HIGH_SCORE_2, 2);
		setHighScore(R.id.highScore3, HIGH_SCORE_3, 3);
		setHighScore(R.id.highScore4, HIGH_SCORE_4, 4);
		setHighScore(R.id.highScore5, HIGH_SCORE_5, 5);
	}

	public void setHighScore(int textViewId, String highScoreTag, int index) {
		highScore = (TextView) findViewById(textViewId);
		int score = prefs.getInt(highScoreTag, 0);
		String scoreString = String.format("%1d.    %2$06d", index, score);
		highScore.setText(scoreString);
	}
}