package com.eyunda.third;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.eyunda.main.SplashActivity;

public class MyActivityDelegate {
	Activity ac;

	public MyActivityDelegate(Activity activity) {
		ac = activity;
	}

	public void onCreate(Bundle savedInstanceState) {
		Toast.makeText(ac.getBaseContext(), "程序onCreate", Toast.LENGTH_LONG)
				.show();
		System.out.println("MyActivityDelegate内  activity:"+ ac.getLocalClassName());

		if (isNetworkConnected(ac)) {

			String sessionId = GlobalApplication.getInstance().getUserData().getSessionId();
			if (sessionId.equals("")) {
				System.out.println("MyActivityDelegate内 sessionId丢失了");
				String localClassName = ac.getLocalClassName();

				NeedSessionIdCode needSessionIdCode = NeedSessionIdCode.getByClassPath(localClassName);
					if (needSessionIdCode!=null) {
						// 从SplashActivity自动登入
						Intent intent = new Intent(ac.getApplication(),
								SplashActivity.class).putExtra("gotoActivity",needSessionIdCode.getGotoPath());
						ac.startActivity(intent);
						ac.finish();
					}
			}
		} else {
			Toast.makeText(ac.getBaseContext(), "网络连接不上", Toast.LENGTH_LONG)
					.show();
		}

	}

	public void onDestroy() {
	}

	public void onResume() {
		//Toast.makeText(ac.getBaseContext(), "程序回来了", Toast.LENGTH_LONG).show();
	}

	public void onRestart() {
		Toast.makeText(ac.getBaseContext(), "程序重启了", Toast.LENGTH_LONG).show();
		if (!isNetworkConnected(ac)) {
			Toast.makeText(ac.getBaseContext(), "网络连接不上", Toast.LENGTH_LONG)
					.show();
		}
	}

	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
}
