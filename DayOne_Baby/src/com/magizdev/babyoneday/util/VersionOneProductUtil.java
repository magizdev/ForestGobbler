package com.magizdev.babyoneday.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class VersionOneProductUtil {
	public static class VersionOneLoginInfo {
		public String Url;
		public String Username;
		public String Token;

		public VersionOneLoginInfo(String url, String username, String token) {
			Url = url;
			Username = username;
			Token = token;
		}

	}

	public static class ProductInfo {
		public String ProductId;
		public String ProductName;

		public ProductInfo(String id, String name) {
			ProductId = id;
			ProductName = name;
		}

		@Override
		public String toString() {
			return ProductName;
		}
	}

	private Context context;

	public VersionOneProductUtil(Context context) {
		this.context = context;
	}

	public VersionOneLoginInfo getCurrentLogin() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		VersionOneLoginInfo currentLogin = new VersionOneLoginInfo(
				pref.getString("Url", ""), pref.getString("Username", ""),
				pref.getString("Token", ""));

		if (currentLogin.Url == "") {
			return null;
		} else {
			return currentLogin;
		}
	}

	public void setCurrentLogin(VersionOneLoginInfo loginInfo) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = pref.edit();
		editor.putString("Url", loginInfo.Url);
		editor.putString("Username", loginInfo.Username);
		editor.putString("Token", loginInfo.Token);
		editor.putString("ProductId", "");
		editor.commit();
	}

	public ProductInfo getCurrentProduct() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		ProductInfo productInfo = new ProductInfo(pref.getString("ProductId",
				""), pref.getString("ProductName", ""));
		if (productInfo.ProductId == "") {
			return null;
		} else {
			return productInfo;
		}
	}

	public void setCurrentProduct(ProductInfo productInfo) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = pref.edit();
		editor.putString("ProductId", productInfo.ProductId);
		editor.putString("ProductName", productInfo.ProductName);
		editor.commit();
	}
}
