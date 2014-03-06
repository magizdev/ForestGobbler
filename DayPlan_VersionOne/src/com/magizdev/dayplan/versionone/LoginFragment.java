package com.magizdev.dayplan.versionone;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.magizdev.dayplan.R;
import com.magizdev.dayplan.versionone.util.VersionOneProductUtil;
import com.magizdev.dayplan.versionone.util.VersionOneProductUtil.ProductInfo;
import com.magizdev.dayplan.versionone.util.VersionOneProductUtil.VersionOneLoginInfo;
import com.versionone.om.Project;
import com.versionone.om.V1Instance;
import com.versionone.om.filters.ProjectFilter;

public class LoginFragment extends MenuFragment {
	private ListView productList;
	private List<ProductInfo> products;
	private LoginTask loginTask;
	private EditText serverUrl;
	private EditText userName;
	private EditText password;
	private ProgressDialog progressDialog;
	private LinearLayout loginPart;
	private VersionOneProductUtil v1productUtil;
	private TextView currentV1Server;
	private TextView currentProduct;

	public LoginFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_login, container,
				false);
		v1productUtil = new VersionOneProductUtil(getActivity());

		productList = (ListView) rootView.findViewById(R.id.productList);
		Button loginButton = (Button) rootView.findViewById(R.id.btnlogin);
		serverUrl = (EditText) rootView.findViewById(R.id.v1host);
		userName = (EditText) rootView.findViewById(R.id.loginedituid);
		password = (EditText) rootView.findViewById(R.id.editpswrd);
		loginPart = (LinearLayout) rootView.findViewById(R.id.loginPart);
		currentV1Server = (TextView) rootView.findViewById(R.id.currentServer);
		currentProduct = (TextView) rootView.findViewById(R.id.currentProduct);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loginTask = new LoginTask();
				loginTask.execute();
			}
		});
		products = new ArrayList<ProductInfo>();

		productList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ProductInfo productInfo = (ProductInfo) productList
						.getAdapter().getItem(arg2);
				v1productUtil.setCurrentProduct(productInfo);
				currentProduct.setText(productInfo.toString());
			}
		});
		return rootView;
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
			VersionOneLoginInfo loginInfo = new VersionOneLoginInfo(server,
					username, passwords);

			try {
				V1Instance instance = new V1Instance(server, username,
						passwords);
				for (Project project1 : instance.getProjects()) {
					products.add(new ProductInfo(project1.getID().toString(),
							project1.getName()));
					for (Project project : project1.getChildProjects(
							new ProjectFilter(), true)) {
						products.add(new ProductInfo(
								project.getID().toString(), project.getName()));
					}
				}
			} catch (Exception e) {
				return false;
			}
			v1productUtil.setCurrentLogin(loginInfo);
			return true;

		}
	}

	public void displayProducts() {
		loginPart.setVisibility(View.GONE);
		productList.setVisibility(View.VISIBLE);
		ArrayAdapter<ProductInfo> adapter = new ArrayAdapter<ProductInfo>(getActivity(),
				R.layout.product_item, R.id.productName);
		adapter.addAll(products);
		productList.setAdapter(adapter);
		productList.invalidate();
		currentV1Server.setText(v1productUtil.getCurrentLogin().Url);
	}

	public void startProgress() {
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loginTask.cancel(true);
				progressDialog.dismiss();
			}
		});
		progressDialog.show();
	}

	public void stopProgress() {
		progressDialog.dismiss();
	}

	@Override
	public int optionMenuResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return false;
	}

}
