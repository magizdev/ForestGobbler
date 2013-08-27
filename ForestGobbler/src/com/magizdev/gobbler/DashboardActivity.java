package com.magizdev.gobbler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.magizdev.gobbler.common.RankInfo;
import com.magizdev.gobbler.common.RankListAdapter;

public class DashboardActivity extends Activity implements OnClickListener {
	public static final String SCORE_SERVER = "http://gamerank.ap01.aws.af.cm/";
	public static final String HIGH_SCORE_1 = "HIGH_SCORE_1";
	public static final String HIGH_SCORE_2 = "HIGH_SCORE_2";
	public static final String HIGH_SCORE_3 = "HIGH_SCORE_3";
	public static final String HIGH_SCORE_4 = "HIGH_SCORE_4";
	public static final String HIGH_SCORE_5 = "HIGH_SCORE_5";
	public static final String STAR_ACHIEVEMENT = "STAR_ACHIEVEMENT";

	private SharedPreferences prefs;
	private TextView myRank;	
	private ListView rankListView;
	private ProgressBar networkProgress;
	private Handler uiHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				startDownload();
				break;
			case 1:
				finishDownload();
				break;
			default:
				displayLocalDashboard();
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		rankListView = (ListView) findViewById(R.id.rankList);
		myRank = (TextView)findViewById(R.id.myRank);
		rankListView.setCacheColorHint(android.R.color.transparent);
		networkProgress = (ProgressBar) findViewById(R.id.networkProgress);
		String stringUrl = SCORE_SERVER + "rankList?game=forestgobbler&mode=1";
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			new DownloadWebpageTask().execute(stringUrl);
			uiHandler.sendEmptyMessage(0);
		} else {
			displayLocalDashboard();
		}

		Button rankEasy = (Button) findViewById(R.id.rank_btn_easy);
		Button rankHard = (Button) findViewById(R.id.rank_btn_hard);
		Button rankEndless = (Button) findViewById(R.id.rank_btn_endless);

		rankEasy.setOnClickListener(this);
		rankHard.setOnClickListener(this);
		rankEndless.setOnClickListener(this);
	}

	protected void finishDownload() {
		networkProgress.setVisibility(View.GONE);
		rankListView.setVisibility(View.VISIBLE);
	}

	protected void startDownload() {
		networkProgress.setVisibility(View.VISIBLE);
		rankListView.setVisibility(View.GONE);
	}

	private void displayLocalDashboard() {
		networkProgress.setVisibility(View.GONE);
		rankListView.setVisibility(View.VISIBLE);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		List<RankInfo> ranks = new ArrayList<RankInfo>();
		for (int i = 1; i < 6; i++) {
			int score = prefs.getInt("HIGH_SCORE_" + i, -1);
			if (score > 0) {
				RankInfo rank = new RankInfo();
				rank.Rank = i;
				rank.UserName = "";
				rank.Score = score;
				ranks.add(rank);
			}
		}
		RankListAdapter adapter = new RankListAdapter(DashboardActivity.this,
				ranks);
		rankListView.setAdapter(adapter);
	}

	private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			// params comes from the execute() call: params[0] is the url.
			try {
				return downloadUrl(urls[0]);
			} catch (IOException e) {
				uiHandler.sendEmptyMessage(-1);
				return "";
			}
			catch (Exception e) {
				Log.w("a",e.getMessage());
				return "";
			}
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			JSONObject jsonRanks;
			try {
				String imei = ((TelephonyManager)DashboardActivity.this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
				int myRank = -1;
				jsonRanks = (JSONObject) new JSONTokener(result).nextValue();
				JSONArray ranksArray = jsonRanks.getJSONArray("ranks");
				List<RankInfo> ranks = new ArrayList<RankInfo>();
				for (int i = 0; i < ranksArray.length(); i++) {
					RankInfo rank = new RankInfo();
					rank.Rank = i + 1;
					rank.UserName = ranksArray.getJSONObject(i).getString(
							"username");
					rank.Score = ranksArray.getJSONObject(i).getInt("score");
					String theImei = ranksArray.getJSONObject(i).getString("imei");
					if(imei.equals(theImei)){
						myRank = i + 1;
					}
					ranks.add(rank);
				}
				
				if(myRank == -1){
					DashboardActivity.this.myRank.setText("No rank");
				}else {
					DashboardActivity.this.myRank.setText("My Rank:"+myRank);
				}
				RankListAdapter adapter = new RankListAdapter(
						DashboardActivity.this, ranks);
				DashboardActivity.this.rankListView.setAdapter(adapter);
				uiHandler.sendEmptyMessage(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				uiHandler.sendEmptyMessage(-1);
			}
		}
	}

	private String downloadUrl(String myurl) throws IOException {
		InputStream is = null;
		// Only display the first 500 characters of the retrieved
		// web page content.
		int len = 5000;

		try {
			URL url = new URL(myurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			int response = conn.getResponseCode();
			is = conn.getInputStream();

			// Convert the InputStream into a string
			String contentAsString = readIt(is, len);
			return contentAsString;

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	public String readIt(InputStream stream, int len) throws IOException,
			UnsupportedEncodingException {
		Reader reader = null;
		reader = new InputStreamReader(stream, "UTF-8");
		char[] buffer = new char[len];
		reader.read(buffer);
		return new String(buffer);
	}

	@Override
	public void onClick(View v) {
		String stringUrl = SCORE_SERVER + "rankList?game=forestgobbler&mode=1";
		switch (v.getId()) {
		case R.id.rank_btn_easy:
			stringUrl = SCORE_SERVER + "rankList?game=forestgobbler&mode=1";
			break;
		case R.id.rank_btn_hard:
			stringUrl = SCORE_SERVER + "rankList?game=forestgobbler&mode=2";
			break;
		case R.id.rank_btn_endless:
			stringUrl = SCORE_SERVER + "rankList?game=forestgobbler&mode=3";
			break;
		default:
			break;
		}
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			new DownloadWebpageTask().execute(stringUrl);
			uiHandler.sendEmptyMessage(0);
		}
	}
}