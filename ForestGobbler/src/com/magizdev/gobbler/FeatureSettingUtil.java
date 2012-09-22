package com.magizdev.gobbler;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class FeatureSettingUtil {
	private Context context;
	SharedPreferences prefs;
	Editor editor;

	public FeatureSettingUtil(Context context) {
		this.context = context;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public List<Integer> getFeatures() {
		List<Integer> featureIds = new ArrayList<Integer>();

		featureIds.add(R.string.feature_rotate);
		featureIds.add(R.string.feature_gravity_down);
		featureIds.add(R.string.feature_gravity_right);
		featureIds.add(R.string.feature_gravity_up);
		featureIds.add(R.string.feature_gravity_left);

		return featureIds;
	}

	public String getFeatureName(int featureId) {
		return context.getResources().getString(featureId);
	}

	public int getFeatureRequirement(int featureId) {
		switch (featureId) {
		case R.string.feature_rotate:
			return 50;
		case R.string.feature_gravity_down:
			return 100;
		case R.string.feature_gravity_right:
			return 150;
		case R.string.feature_gravity_up:
			return 200;
		case R.string.feature_gravity_left:
			return 250;
		}
		return 0;
	}

	public void setFeature(int featureId, boolean enable) {
		editor = prefs.edit();
		editor.putBoolean(getFeatureName(featureId), enable);
		editor.commit();
	}

	public boolean getFeature(int featureId) {
		return prefs.getBoolean(getFeatureName(featureId), false);
	}
}
