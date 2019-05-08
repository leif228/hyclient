package com.hangyi.zd.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.tools.ParseJson;
import com.hangyi.tools.Util;
import com.hangyi.zd.adapter.SCAdapter;
import com.hangyi.zd.domain.ShipSCData;
import com.hangyi.zd.domain.UserPowerData;
import com.hangyi.zd.domain.UserPowerShipData;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ShipSCActivity extends CommonActivity implements
OnItemClickListener {
	Data_loader data;
	private EditText startPort;
	private ListView mlistView;
	private List<UserPowerShipData> userPowerShipDataList = new ArrayList<UserPowerShipData>();
	private List<ShipSCData> shipSCDataList = new ArrayList<ShipSCData>();
	private SCAdapter smpAdapter;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {// 初始化数据完成
				smpAdapter.notifyDataSetChanged();
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zd_part_home);
		data = new Data_loader();
		dialogUtil = new DialogUtil(this);
		LinearLayout searchContainer = (LinearLayout) findViewById(R.id.searchContainer);// 搜索按钮
		searchContainer.setVisibility(View.GONE);
		initView();
		loadData();
	}

	private void initData() {
		SharedPreferences sp = getSharedPreferences("UserPowerData",
				Context.MODE_PRIVATE);
		String object = sp.getString("UserPower", "");

		UserPowerData data = null;
		if (!"".equals(object)) {
			Gson gson = new Gson();
			data = gson.fromJson(object, new TypeToken<UserPowerData>() {
			}.getType());
		} else
			data = new UserPowerData();

		userPowerShipDataList.addAll(data.getUserPowerShipDatas());
		for(UserPowerShipData u:userPowerShipDataList){
			ShipSCData s = new ShipSCData();
			s.setShipID(u.getShipID());
			s.setShipName(u.getShipName());
			s.setFocus("1");
			shipSCDataList.add(s);
		}
	}

	private void loadData() {

		Map<String, Object> apiParams = new HashMap<String, Object>();
		data.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("加载中", "请稍候...");
			}
			@Override
			public void onSuccess(String arg2) {
				shipSCDataList.clear();
				
				if(arg2.contains("[]")){
					initData();
				}else{
					try {
						List<ShipSCData> list = ParseJson.parseShipSC(arg2);
						shipSCDataList.addAll(list);
					} catch (Exception e) {
						Toast.makeText(ShipSCActivity.this, "数据解析出错！",
								Toast.LENGTH_LONG).show();
					}
				}
				
				handler.sendEmptyMessage(1);
				dialog.dismiss();
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipSCActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipSCActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(ShipSCActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipSCActivity.this, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.shipSCGet, apiParams, "get");	
		}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("收藏船舶");
		setRight("保存", new OnClickListener() {

			@Override
			public void onClick(View v) {
				postSC();
			}

		});
	}
	
	private void postSC() {
		List<ShipSCData> tempList = new ArrayList<ShipSCData>();
		for(ShipSCData ss:shipSCDataList){
			ShipSCData temps = new ShipSCData();
			temps.setShipID(ss.getShipID());
			temps.setShipName(ss.getShipName());
			temps.setFocus(ss.getFocus());
			
			tempList.add(temps);
		}
		
		String json = "";
		for(ShipSCData s:tempList){
			s.setShipName(Util.chinaToUnicode(s.getShipName()));
			if(s.getShipID().startsWith("0x"))
				s.setShipID(String.valueOf(Util.HexToInt(s.getShipID())));
		}
		
		Gson s = new Gson();
		Map<String,List<ShipSCData>> map = new HashMap<String, List<ShipSCData>>();
		map.put("Data", tempList);
		json = s.toJson(map);

		Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("jsonstr", json);
		data.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("加载中", "请稍候...");
			}
			@Override
			public void onSuccess(String arg2) {
				
				if("0".equals(arg2)){
					Toast.makeText(ShipSCActivity.this, "收藏成功！", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(ShipSCActivity.this, "收藏失败！", Toast.LENGTH_SHORT).show();
				}
				
				dialog.dismiss();
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipSCActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipSCActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(ShipSCActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipSCActivity.this, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.shipSCPost, apiParams, "post");	
		
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
		
		mlistView =  (ListView)findViewById(R.id.mlistView);
		smpAdapter = new SCAdapter(this, shipSCDataList);
		mlistView.setAdapter(smpAdapter);
//		mlistView.setOnItemClickListener(this);

	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

//		HashMap<String, Object> map = (HashMap<String, Object>) mlistView.getItemAtPosition(position);
//		Intent intent = new Intent(ShipMoniterActivity.this,ShipLineListActivity.class);
//		// 绑定传输的船舶数据
//		intent.putExtra("shipId", map.get("shipId").toString());
//		intent.putExtra("shipName", map.get("shipName").toString());
//		intent.putExtra("mmsi", map.get("MMSI").toString());
//		startActivity(intent);
	}

}
