package com.eyunda.main.home;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;




import com.google.zxing.client.android.CaptureActivity;
import com.hy.client.R;
import com.eyunda.main.Config;
import com.eyunda.main.SplashActivity;
import com.eyunda.main.data.Image_loader;
import com.eyunda.main.json.DataConvert;
import com.eyunda.main.localdata.LocalData;
import com.eyunda.main.reg.UserInfoActivity;
import com.eyunda.main.util.FavorUtil;
import com.eyunda.main.util.PlugUtil;
import com.eyunda.main.view.DialogUtil;
import com.ta.TAApplication;
import com.ta.util.http.AsyncHttpResponseHandler;

public class LoginActivity extends HomeActivity {
	LinearLayout scroll_but1, home_left_lay1, home_left_lay2, home_left_lay3,
			home_left_lay4, home_left_lay5, home_left_lay6;
	TextView favor_count;

	Image_loader   imageFetcher;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scroll_but1 = (LinearLayout) findViewById(R.id.scroll_but_layout);
		home_left_lay1 = (LinearLayout) findViewById(R.id.home_left_lay1);
		home_left_lay2 = (LinearLayout) findViewById(R.id.home_left_lay2);
		home_left_lay3 = (LinearLayout) findViewById(R.id.home_left_lay3);
		home_left_lay4 = (LinearLayout) findViewById(R.id.home_left_lay4);
		home_left_lay5 = (LinearLayout) findViewById(R.id.home_left_lay5);
		home_left_lay6 = (LinearLayout) findViewById(R.id.home_left_lay6);

		login_type = (TextView) findViewById(R.id.login_type);
		login_name = (TextView) findViewById(R.id.login_name);
		userHead = (ImageView) findViewById(R.id.userHead);

		dialogUtil = new DialogUtil(this);
		scroll_but1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Config.IFLOGIN)
					startActivity(new Intent(LoginActivity.this,
							UserInfoActivity.class));
				else
					startActivity(new Intent(LoginActivity.this,
							com.eyunda.main.reg.LoginActivity.class));
			}
		});
		home_left_lay1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		home_left_lay2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		home_left_lay4.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialogUtil.showDialogFromConfig("提示", "是否退出？", new Handler() {

					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						logout();
					}
				});
			}
		});
		home_left_lay5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this,
						UserInfoActivity.class));
			}
		});

		home_left_lay6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// dialogUtil.showQuitDialogFromConfig("提示", "是否退出？");
				dialogUtil.showDialogFromConfig("提示", "是否退出？", new Handler() {

					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						logout();
					}
				});
			}
		});
		imageFetcher=new Image_loader(LoginActivity.this	, (TAApplication) getApplication());
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (Config.IFLOGIN) {
			home_left_lay1.setVisibility(View.VISIBLE);
			home_left_lay2.setVisibility(View.VISIBLE);
			home_left_lay3.setVisibility(View.GONE);
			home_left_lay4.setVisibility(View.VISIBLE);
			home_left_lay5.setVisibility(View.GONE);
			home_left_lay6.setVisibility(View.VISIBLE);
			login_name.setText(Config.LOGINNAME);
			login_type.setText("已登录");

			data.myfavorCount(new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String arg0) {
					super.onSuccess(arg0);
					Map<String,String> tm =DataConvert.toMap(arg0);
					favor_count.setText(tm.get("favorCount"));
					
				}
			}, Config.USERID);
			
			String headString =LocalData.get((TAApplication) getApplication()).getUserHead();
			if(headString!=null)
			imageFetcher.load_normal_Img(headString, userHead);
			if (FavorUtil.ifNull())
			loadAllFavors(Config.USERID);

		} else {
			home_left_lay1.setVisibility(View.GONE);
			home_left_lay2.setVisibility(View.GONE);
			home_left_lay3.setVisibility(View.GONE);
			home_left_lay4.setVisibility(View.VISIBLE);
			home_left_lay5.setVisibility(View.GONE);
			home_left_lay6.setVisibility(View.GONE);
		}

	}

	public void logout() {
		Config.IFLOGIN = false;
		Config.LOGINNAME = "";
		Config.USERID = "";

		home_include2.setVisibility(View.VISIBLE);
		home_include1.setVisibility(View.GONE);

		home_left_lay1.setVisibility(View.GONE);
		home_left_lay2.setVisibility(View.GONE);
		home_left_lay3.setVisibility(View.GONE);
		//home_left_lay4.setVisibility(View.VISIBLE);
		home_left_lay4.setVisibility(View.GONE);
		home_left_lay5.setVisibility(View.GONE);
		home_left_lay6.setVisibility(View.GONE);
		login_name.setText("登录/注册");
		login_type.setText("登录后，推荐更多精彩");
		 LocalData.get((TAApplication) getApplication()).delUser();
		 dialogUtil.sysExit();

	}
	
	private void loadAllFavors(String userid) {
		FavorUtil.clear();
			data.getAllFavors(new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String arg0) {
					super.onSuccess(arg0);
					List<Map<String, String>> list = DataConvert.toArrayList(arg0);
					if (list != null) {
						for (Map<String, String> tm : list) {
							FavorUtil.addFavor(tm.get("cid") );
						}
					}
				}
			}, userid);
			PlugUtil.ALLPLUG.clear();
			data.myPlugin(new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String arg0) {
					super.onSuccess(arg0);
					List<Map<String, String>> list = DataConvert.toArrayList(arg0);
					if (list != null) {
						for (Map<String, String> tm : list) {
							PlugUtil.ALLPLUG.put(tm.get("id"), "");
						}
					}
				}
			}, userid);
		}

}
