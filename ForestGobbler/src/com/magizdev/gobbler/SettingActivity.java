package com.magizdev.gobbler;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.TextView;

public class SettingActivity extends Activity {
	public static final String STAR_ACHIEVEMENT = "STAR_ACHIEVEMENT";

	private SharedPreferences prefs;
	private ListView featureList;
	private TextView highScore;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		featureList = (ListView) findViewById(R.id.listFeatures);
		FeatureListAdapter adapter= new FeatureListAdapter(this);
		featureList.setAdapter(adapter);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		setStarScore(R.id.starRecord, STAR_ACHIEVEMENT);

	}
	
	public void setStarScore(int textViewId, String highScoreTag) {
		highScore = (TextView) findViewById(textViewId);
		int score = prefs.getInt(highScoreTag, 0);
		String scoreString = String.format("%1$04d", score);
		highScore.setText(scoreString);
	}
}