package com.magizdev.dayplan.versionone;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.versionone.viewmodel.DayTaskAdapter;
import com.versionone.om.Project;
import com.versionone.om.V1Instance;
import com.versionone.om.filters.ProjectFilter;

public class LoginActivity extends Activity {
	private ListView productList;
	private DayTaskAdapter adapter;
	private List<String> products;
	private LoginTask loginTask;
	private EditText serverUrl;
	private EditText userName;
	private EditText password;
	private ProgressDialog progressDialog;
	private LinearLayout loginPart;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		productList = (ListView) findViewById(R.id.productList);
		Button loginButton = (Button) findViewById(R.id.btnlogin);
		serverUrl = (EditText) findViewById(R.id.v1host);
		userName = (EditText) findViewById(R.id.loginedituid);
		password = (EditText) findViewById(R.id.editpswrd);
		loginPart = (LinearLayout) findViewById(R.id.loginPart);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loginTask = new LoginTask();
				loginTask.execute();
			}
		});
		products = new ArrayList<String>();
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.refresh();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private class LoginTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPostExecute(Boolean result) {
			stopProgress();
			if (result) {
				displayProducts();
			}
		}

		@Override
		protected void onPreExecute() {
			startProgress();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			String server = serverUrl.getText().toString();
			String username = userName.getText().toString();
			String passwords = password.getText().toString();

			try {
				V1Instance instance = new V1Instance(server, username,
						passwords);
				for (Project project1 : instance.getProjects()) {
					for (Project project : project1.getChildProjects(
							new ProjectFilter(), true)) {
						products.add(project.getName());
					}
				}
			} catch (Exception e) {
				return false;
			}
			return true;

		}
	}

	public void displayProducts() {
		loginPart.setVisibility(View.GONE);
		productList.setVisibility(View.VISIBLE);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.product_item, R.id.productName);
		productList.setAdapter(adapter);
	}

	public void startProgress() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loginTask.cancel(true);
			}
		});
	}

	public void stopProgress() {
		progressDialog.dismiss();
	}

}
