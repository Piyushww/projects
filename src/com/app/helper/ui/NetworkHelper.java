package com.app.helper.ui;

import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkHelper {

	public static boolean isOnline(Context cxt) {
		ConnectivityManager cm = (ConnectivityManager) cxt
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting() ) {
			
			Log.d("", "mode is online");
			return true;
		}
		return false;
	}

	public static boolean canHit() {

		try {
			URL url = new URL("http://www.google.com/");
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setConnectTimeout(2000);
			urlConnection.connect();
			urlConnection.disconnect();
			return true;
		} catch (Exception e) {
			Log.d("","exception "+e.getMessage());
			return false;
		} 

	}

	public static void exitIfOffline(Activity act) {
		if (!isOnline(act.getApplicationContext())) {
			Log.d("","no net connection ");
		}
	}

}





















