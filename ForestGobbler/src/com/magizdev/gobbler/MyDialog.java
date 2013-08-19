package com.magizdev.gobbler;

import java.io.BufferedWriter;
import java.io.File;
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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.magizdev.common.lib.Score;
import com.magizdev.gobbler.view.GameView;

public class MyDialog extends Dialog implements OnClickListener {

	private GameView gameview;
	private Context context;
	private Score score;
	private RatingBar ratingBar;
	private int rating;
	private String scoreString;
	private SDCardUtil sdCardUtil;

	public MyDialog(Context context, GameView gameview, String msg, Score score) {
		super(context, R.style.dialog);
		this.gameview = gameview;
		this.context = context;
		this.score = score;
		this.setContentView(R.layout.dialog_view);
		sdCardUtil = new SDCardUtil(context);
		TextView text_msg = (TextView) findViewById(R.id.text_message);
		TextView text_score = (TextView) findViewById(R.id.text_score);
		ImageButton btn_next = (ImageButton) findViewById(R.id.next_imgbtn);
		ImageButton btn_replay = (ImageButton) findViewById(R.id.replay_imgbtn);
		ImageButton btn_upload = (ImageButton) findViewById(R.id.upload_imgbtn);
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

		text_msg.setText(msg);
		scoreString = text_score.getText().toString()
				.replace("$", String.valueOf(this.score.getScore()));
		text_score.setText(scoreString);
		btn_next.setOnClickListener(this);
		btn_replay.setOnClickListener(this);
		btn_upload.setOnClickListener(this);

		this.setCancelable(false);
	}

	@Override
	public void onClick(View v) {
		int ratingPicId = 0;
		switch (this.rating) {
		case 0:
		case 1:
			ratingPicId = R.drawable.rating1;
			break;
		case 2:
			ratingPicId = R.drawable.rating2;
			break;
		case 3:
			ratingPicId = R.drawable.rating3;
			break;
		case 4:
			ratingPicId = R.drawable.rating4;
			break;
		case 5:
			ratingPicId = R.drawable.rating5;
			break;
		default:
			ratingPicId = R.drawable.rating1;
			break;
		}

		switch (v.getId()) {
		case R.id.upload_imgbtn:
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this.context);
			String userName = prefs.getString(SettingActivity.USERNAME_TAG,
					"Player");
			int intScore = score.getScore();
			int mode = 1;
			if (gameview.getMode() == GameView.HARD_MODE)
				mode = 2;
			if (gameview.getMode() == GameView.ENDLESS_MODE)
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
		case R.id.replay_imgbtn:
			this.dismiss();
			score.Clear();
			gameview.startPlay(gameview.getGameMode());
			break;
		case R.id.next_imgbtn:
			Drawable ratingPic = context.getResources()
					.getDrawable(ratingPicId);
			Bitmap bitmap = Bitmap.createBitmap(269, 141,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			ratingPic.setBounds(0, 0, 269, 141);
			ratingPic.draw(canvas);
			Paint paint = new Paint();
			paint.setColor(0xFF60ADFF);
			paint.setTextSize(20);
			canvas.drawText(scoreString, 135, 50, paint);
			boolean scoreFileSaved = sdCardUtil.saveBitmap(bitmap);

			Intent shareScoreIntent = new Intent(Intent.ACTION_SEND);
			String share = context.getResources().getString(R.string.share)
					.replace("$", String.valueOf(score.getScore()));
			shareScoreIntent.putExtra(Intent.EXTRA_TEXT, share);
			shareScoreIntent.setType("text/plain");
			if (scoreFileSaved) {
				shareScoreIntent.putExtra(Intent.EXTRA_STREAM, Uri
						.fromFile(new File(sdCardUtil.getImageFileFullName())));
				shareScoreIntent.setType("image/png");
			}
			context.startActivity(shareScoreIntent);
			break;
		}
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
