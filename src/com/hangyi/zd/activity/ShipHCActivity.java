package com.hangyi.zd.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.main.data.Image_loader;
import com.eyunda.main.reg.UpdateQA;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.part1.data.PartData_loader;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.user.AgentActivity;
import com.eyunda.third.chat.utils.LogUtil;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.loaders.SynData_loader;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.TAApplication;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ShipHCActivity extends CommonListActivity {
	private static final String LOGTAG = LogUtil
			.makeLogTag(ShipHCActivity.class);
	ImageView user_head;
	DialogUtil dialogUtil;
	RelativeLayout chang_user,modify_pw,user_logout;
	PartData_loader data;
	Image_loader loader;
	ProgressDialog dialog;
	ImageLoader mImageLoader;
	Data_loader data1;
	SynData_loader synDataLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zd_hc_information);
		chang_user = (RelativeLayout) findViewById(R.id.chang_user);
		modify_pw = (RelativeLayout) findViewById(R.id.modify_pw);
		user_logout = (RelativeLayout) findViewById(R.id.user_logout);
		
		synDataLoader = new SynData_loader();
		data1 = new Data_loader();
		user_head = (ImageView) findViewById(R.id.user_head);
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(GlobalApplication.getInstance()
				.getImageLoaderConfiguration());

		loader = new Image_loader(this, (TAApplication) getApplication());
		
		chang_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ShipHCActivity.this, ShipListActivity.class));
			}
		});
		modify_pw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(ShipHCActivity.this, ShipHCListIngActivity.class));
			}
		});

	}
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("航次管理");
	}

	
	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub
		
	}
}
