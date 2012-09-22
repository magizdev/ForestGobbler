package com.magizdev.gobbler;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
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
	private ListView featureList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);

		featureList = (ListView) findViewById(R.id.listFeatures);
		FeatureListAdapter adapter= new FeatureListAdapter(this);
		featureList.setAdapter(adapter);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		setHighScore(R.id.highScore1, HIGH_SCORE_1, 1);
		setHighScore(R.id.highScore2, HIGH_SCORE_2, 2);
		setHighScore(R.id.highScore3, HIGH_SCORE_3, 3);
		setHighScore(R.id.highScore4, HIGH_SCORE_4, 4);
		setHighScore(R.id.highScore5, HIGH_SCORE_5, 5);
		
		setStarScore(R.id.starRecord, STAR_ACHIEVEMENT);

	}

	public void setHighScore(int textViewId, String highScoreTag, int index) {
		highScore = (TextView) findViewById(textViewId);
		int score = prefs.getInt(highScoreTag, 0);
		String scoreString = String.format("%1d.    %2$06d", index, score);
		highScore.setText(scoreString);
	}
	
	public void setStarScore(int textViewId, String highScoreTag) {
		highScore = (TextView) findViewById(textViewId);
		int score = prefs.getInt(highScoreTag, 0);
		String scoreString = String.format("%1$04d", score);
		highScore.setText(scoreString);
	}
}