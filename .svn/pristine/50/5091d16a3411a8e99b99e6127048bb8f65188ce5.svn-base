package com.eyunda.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;

public class NetWorkUtil {

	public static final int STATE_DISCONNECT = 0;
	public static final int STATE_WIFI = 1;
	public static final int STATE_MOBILE = 2;


	public static boolean isNetWorkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] nis = cm.getAllNetworkInfo();
		if (nis != null) {
			for (NetworkInfo ni : nis) {
				if (ni != null) {
					if (ni.isConnected()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public static boolean isWifiConnected(Context context) {
		WifiManager wifiMgr = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		boolean isWifiEnable = wifiMgr.isWifiEnabled();

		return isWifiEnable;
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}

		return false;
	}



	public static int getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// Wifi
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return STATE_WIFI;
		}

		// 3G
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return STATE_MOBILE;
		}
		return STATE_DISCONNECT;
	}
}
