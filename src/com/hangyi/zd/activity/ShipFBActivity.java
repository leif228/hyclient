package com.hangyi.zd.activity;

import android.os.Bundle;
import android.widget.BaseAdapter;

import com.baidu.mapapi.SDKInitializer;
import com.eyunda.main.CommonListActivity;
import com.hy.client.R;

public class ShipFBActivity extends CommonListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.zd_ship_fb);
		
	}


	@Override
	protected void onStart() {
		super.onStart();
		setTitle("分布图");
		
		ContentFragment fragment = (ContentFragment) getFragmentManager().findFragmentById(R.id.fragment_content);
		fragment.hideSearch();
		fragment.hideDownBtn();

	};

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
