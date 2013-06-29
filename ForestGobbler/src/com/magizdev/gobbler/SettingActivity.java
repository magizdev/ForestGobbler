package com.magizdev.gobbler;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class SettingActivity extends Activity {
	public static final String STAR_ACHIEVEMENT = "STAR_ACHIEVEMENT";
	public static final String USERNAME_TAG = "USER_NAME";

	private SharedPreferences prefs;
	private ListView featureList;
	private TextView highScore;
	private EditText userName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		featureList = (ListView) findViewById(R.id.listFeatures);
		FeatureListAdapter adapter= new FeatureListAdapter(this);
		featureList.setAdapter(adapter);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		
		setStarScore(R.id.starRecord, STAR_ACHIEVEMENT);
		userName = (EditText)findViewById(R.id.userName);
		if(prefs.getString(USERNAME_TAG, "") != ""){
			userName.setText(prefs.getString(USERNAME_TAG, ""));
		}
		
		userName.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if(arg1==EditorInfo.IME_ACTION_DONE){
					Editor editor = SettingActivity.this.prefs.edit();
					editor.putString(SettingActivity.this.USERNAME_TAG, userName.getText().toString());
					editor.commit();
				}
				return false;
			}
		});

	}
	
	public void setStarScore(int textViewId, String highScoreTag) {
		highScore = (TextView) findViewById(textViewId);
		int score = prefs.getInt(highScoreTag, 0);
		String scoreString = String.format("%1$04d", score);
		highScore.setText(scoreString);
	}
}