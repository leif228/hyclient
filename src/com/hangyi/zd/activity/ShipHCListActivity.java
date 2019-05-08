package com.hangyi.zd.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.NewPageHomeMainActivity;
import com.eyunda.third.activities.ship.ShipLineListActivity;
import com.eyunda.third.domain.location.ShipCooordData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.tools.ParseJson;
import com.hangyi.zd.adapter.ShipHCListAdapter;
import com.hangyi.zd.adapter.ShipSCAdapter;
import com.hangyi.zd.domain.ShipVoyageData;
import com.hangyi.zd.domain.UserPowerData;
import com.hangyi.zd.domain.UserPowerShipData;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ShipHCListActivity extends CommonActivity implements
OnItemClickListener {
	Data_loader data;
	private EditText startPort;
	private ListView mlistView;
	private ShipHCListAdapter smpAdapter;
	private String shipName;
	private List<ShipVoyageData> svds = new ArrayList<ShipVoyageData>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zd_part_home);
		data = new Data_loader();
		dialogUtil = new DialogUtil(this);
		
		initView();
		
		LinearLayout searchContainer = (LinearLayout) findViewById(R.id.searchContainer);// 搜索按钮
		searchContainer.setVisibility(View.GONE);
		shipName = getIntent().getStringExtra("shipName");
		if(shipName!=null){
			SharedPreferences sp = ShipHCListActivity.this.getSharedPreferences(ApplicationConstants.ShipVoyageData_SharedPreferences, Context.MODE_PRIVATE);
			String data = sp.getString("data", "");
			if(!data.equals("")){
				Gson gson = new Gson();
				Map<String,List<ShipVoyageData>> shipNameListMap = gson.fromJson(
						data, new TypeToken<Map<String, List<ShipVoyageData>>>() {
						}.getType());
				for(String key:shipNameListMap.keySet()){
					if(key.equals(shipName)){
						List<ShipVoyageData> ss = shipNameListMap.get(key);
						for(int i=ss.size()-1;i>=0;i--){
							svds.add(ss.get(i));
						}
					}
				}
//				smpAdapter.notifyDataSetChanged();
			}
		}

		mlistView =  (ListView)findViewById(R.id.mlistView);
		smpAdapter = new ShipHCListAdapter(this, svds);
		mlistView.setAdapter(smpAdapter);
		mlistView.setOnItemClickListener(this);
		
//		initData();

	}

//	private void initData() {
//		if(userPowerShipData == null) 
//			return;
//		
//		Map<String, Object> apiParams = new HashMap<String, Object>();
//		
//		data.getZd_ApiResult(new AsyncHttpResponseHandler() {
//			@Override
//			public void onStart() {
//				super.onStart();
//				dialog = dialogUtil.loading("加载中", "请稍候...");
//			}
//			@Override
//			public void onSuccess(String arg2) {
//				super.onSuccess(arg2);
//				dialog.dismiss();
//				
//								
//			}
//			@Override
//			public void onFailure(Throwable error, String content) {
//				super.onFailure(error, content);
//				dialog.dismiss();
//				if (content != null && content.equals("can't resolve host"))
//					Toast.makeText(ShipHCListActivity.this, "网络连接异常",
//							Toast.LENGTH_LONG).show();
//				else if (content != null && content.equals("socket time out")) {
//					Toast.makeText(ShipHCListActivity.this, "连接服务器超时",
//							Toast.LENGTH_LONG).show();
//				} else if (content != null) {
//					Toast.makeText(ShipHCListActivity.this, content,
//							Toast.LENGTH_LONG).show();
//				} else
//					Toast.makeText(ShipHCListActivity.this, "未知异常",
//							Toast.LENGTH_LONG).show();
//
//			}
//		}, "/clientapi/?Function=6&Style=json&ShipID="+userPowerShipData.getShipID(), apiParams, "get");
//	
//	
//	}

	@Override
	protected void onStart() {
		super.onStart();
		if(shipName!=null)
			setTitle(shipName+"航次列表");
		else
			setTitle("航次列表");
		setRight("关闭", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initView() {
		startPort = (EditText) findViewById(R.id.startPort);
		LinearLayout search_btn = (LinearLayout) findViewById(R.id.search_btn);// 搜索按钮

		search_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String key = startPort.getText().toString();
				if (TextUtils.isEmpty(key)) {

					Toast.makeText(context, "请输入搜索船名", Toast.LENGTH_SHORT)
							.show();
				} else {

					// TODO 显视搜索船舶结果
				}
			}
		});

	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		ShipVoyageData map = (ShipVoyageData) mlistView.getItemAtPosition(position);
		Intent intent = new Intent(ShipHCListActivity.this,ShipHCNodeActivity.class);
		intent.putExtra("data", map);
		// 绑定传输的船舶数据
//		intent.putExtra("shipId", map.get("shipId").toString());
//		intent.putExtra("shipName", map.get("shipName").toString());
//		intent.putExtra("mmsi", map.get("MMSI").toString());
		startActivity(intent);
	}

}
