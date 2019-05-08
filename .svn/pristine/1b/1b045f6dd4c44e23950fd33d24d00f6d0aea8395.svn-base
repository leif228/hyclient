package com.eyunda.third.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eyunda.main.data.Image_loader;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.order.PayActivity;
import com.eyunda.third.activities.user.UserInfoActivity;
import com.hangyi.zd.activity.AccountInfoActivity;
import com.hangyi.zd.activity.LSShipListActivity;
import com.hangyi.zd.activity.LoginActivity;
import com.hangyi.zd.activity.ShipHCActivity;
import com.hangyi.zd.activity.ShipLSActivity;
import com.hangyi.zd.activity.ShipSCActivity;
import com.hangyi.zd.activity.ZdShipDynamicActivity;
import com.hangyi.zd.activity.gridviewpage.AllAppListActivity;
import com.hangyi.zd.widge.MainActivity;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.TAApplication;

public class MenuActivity extends NewPageHomeMainActivity {
	LinearLayout scroll_but1, home_left_lay1, home_left_lay2, home_left_lay3,
			home_left_lay4, home_left_lay5, home_left_lay6, home_left_ship,
			home_search_boat, home_left_cargo,my_boat_dt,my_boat_jk,home_left_cabin;

	Image_loader imageFetcher;
	ImageLoader mImageLoader;
	String headImgUrl;

	SharedPreferences sp;
	String bindingCode;

	private LinearLayout home_left_account;
	private LinearLayout home_left_history;
	private LinearLayout home_left_hcmanage;
	private LinearLayout home_left_jt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		scroll_but1 = (LinearLayout) findViewById(R.id.scroll_but_layout);
//		home_search_boat = (LinearLayout) findViewById(R.id.home_left_search);
//		
//		home_left_account = (LinearLayout) findViewById(R.id.home_left_account);
//		home_left_ship = (LinearLayout) findViewById(R.id.home_left_ship);
//		home_left_history = (LinearLayout) findViewById(R.id.home_left_history);
//		home_left_hcmanage = (LinearLayout) findViewById(R.id.home_left_hcmanage);
//		home_left_jt = (LinearLayout) findViewById(R.id.home_left_jt);
//		
//		login_name = (TextView) findViewById(R.id.login_name);
//		userHead = (ImageView) findViewById(R.id.userHead);
//		mImageLoader = ImageLoader.getInstance();
//		mImageLoader.init(GlobalApplication.getInstance()
//				.getImageLoaderConfiguration());
//
//		home_left_account.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//					startActivity(new Intent(MenuActivity.this,
//							AccountInfoActivity.class));// 导向个人中心
//			}
//		});
//		home_left_ship.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(MenuActivity.this,
//						ShipSCActivity.class));// 导向个人中心
//			}
//		});
//		home_left_history.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(MenuActivity.this,
//						LSShipListActivity.class));
//			}
//		});
//		home_left_hcmanage.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(MenuActivity.this,
//						ShipHCActivity.class));// 导向个人中心
//			}
//		});
//		home_left_jt.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(MenuActivity.this,
//						AllAppListActivity.class));// 导向个人中心
//			}
//		});
//		sp = this.getSharedPreferences("UserInfoConfig", Context.MODE_PRIVATE);
//		
//		String userName = sp.getString("UserName", "");
//		if("".equals(userName)) {
//			startActivity(new Intent(this,LoginActivity.class));
//			finish();
//		} 
		
	}

	@Override
	protected void onStart() {
		super.onStart();
//		String sUserName = sp.getString("UserName", "");
//		login_name.setText(sUserName);
	}

	public void logout() {
		dialogUtil.sysExit();
	}

}
