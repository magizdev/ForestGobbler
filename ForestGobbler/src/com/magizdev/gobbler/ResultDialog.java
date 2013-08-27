package com.magizdev.gobbler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.magizdev.common.lib.Score;
import com.magizdev.gobbler.view.GameView;

public class ResultDialog extends Dialog implements OnClickListener {

	private GameView gameview;
	private Context context;
	private Score score;
	private RatingBar ratingBar;
	private int rating;
	private String scoreString;

	public ResultDialog(Context context, GameView gameview, Score score) {
		super(context, R.style.dialog);
		this.gameview = gameview;
		this.context = context;
		this.score = score;
		this.setContentView(R.layout.dialog_view);
		TextView text_score = (TextView) findViewById(R.id.text_score);
		Button btn_replay = (Button) findViewById(R.id.replay_btn);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
		int rating = 4;
		if (gameview.getGameMode() == GameView.HARD_MODE) {
			rating = 5;
		}
		if (gameview.win() == false) {
			rating = 1;
		}
		float floatRating = rating * score.GetRating();
		this.rating = Math.round(floatRating);
		ratingBar.setRating(this.rating);

		DashboardUtil dashboardUtil = new DashboardUtil(context);
		dashboardUtil.insertHighScore(score.getScore());
		dashboardUtil.addStar(this.rating);

		scoreString = text_score.getText().toString()
				.replace("$", String.valueOf(this.score.getScore()));
		text_score.setText(scoreString);
		btn_replay.setOnClickListener(this);

		this.setCancelable(false);
	}

	@Override
	public void onClick(View v) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this.context);
		String userName = prefs.getString(SettingActivity.USERNAME_TAG,
				"Player");
		int intScore = score.getScore();
		int mode = 1;
		if (gameview.getGameMode() == GameView.HARD_MODE)
			mode = 2;
		if (gameview.getGameMode() == GameView.ENDLESS_MODE)
			mode = 3;
		String stringUrl = "http://gamerank.ap01.aws.af.cm/rankAdd";
		TelephonyManager tmManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imeiString = tmManager.getDeviceId();
		ConnectivityManager connMgr = (ConnectivityManager) this.context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			new UploadScoreTask().execute(stringUrl, userName,
					Integer.toString(mode), Integer.toString(intScore),
					imeiString);
		}
		this.dismiss();
		score.Clear();
		gameview.startPlay(gameview.getGameMode());
	}

	private class UploadScoreTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			// params comes from the execute() call: params[0] is the url.
			try {
				Log.w("a", "test");
				uploadScore(urls[0], urls[1], urls[2], urls[3], urls[4]);
				return "1";
			} catch (IOException e) {
				Log.w("exception", e.getMessage());
				return "Unable to retrieve web page. URL may be invalid.";
			}
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {

		}
	}

	private String getQuery(List<NameValuePair> params)
			throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}

	private void uploadScore(String myurl, String username, String mode,
			String score, String imei) throws IOException {
		InputStream is = null;
		// Only display the first 500 characters of the retrieved
		// web page content.
		int len = 5000;

		try {
			URL url = new URL(myurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("mode", mode));
			params.add(new BasicNameValuePair("score", score));
			params.add(new BasicNameValuePair("imei", imei));
			params.add(new BasicNameValuePair("game", "forestgobbler"));

			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					os, "UTF-8"));
			String body = getQuery(params);
			writer.write(getQuery(params));
			writer.close();
			os.close();
			// Starts the query
			conn.connect();
			int response = conn.getResponseCode();
			is = conn.getInputStream();

		} catch (Exception e) {
			Log.w("a", e.getMessage());
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
}
