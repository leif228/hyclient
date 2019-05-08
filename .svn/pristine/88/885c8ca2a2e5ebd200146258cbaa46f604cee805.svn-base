package com.hangyi.zd.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.CalendarUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.zd.adapter.HCShipAdapter;
import com.hangyi.zd.adapter.ShipAdapter;
import com.hangyi.zd.domain.ShipVoyageData;
import com.hangyi.zd.domain.ShipVoyageNodeData;
import com.hangyi.zd.domain.UserPowerData;
import com.hangyi.zd.domain.UserPowerShipData;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ShipListActivity extends CommonActivity implements
OnItemClickListener {
	Data_loader data;
	private static final int refrush_HOURTimes = 2;
	private EditText startPort;
	private ListView mlistView;
	private List<String> shipNames = new ArrayList<String>();
	private Map<String,List<ShipVoyageData>> shipNameListMap = new HashMap<String,List<ShipVoyageData>>();
	private HCShipAdapter smpAdapter;
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
//		SharedPreferences sp = ShipListActivity.this.getSharedPreferences("ShipVoyageData", Context.MODE_PRIVATE);
//		String time = sp.getString("loadTime", "");
//		if(!"".equals(time)){
//			Calendar loadTime = CalendarUtil.parseYYYY_MM_DD_HH_MM_SS(time);
//			Calendar currTime = Calendar.getInstance();
//			loadTime.add(Calendar.HOUR_OF_DAY, refrush_HOURTimes);  
//			if(currTime.getTimeInMillis()>loadTime.getTimeInMillis())
//				loadData();
//			else{
//				String data = sp.getString("data", "");
//				if(!data.equals("")){
//					Gson gson = new Gson();
//					shipNameListMap = gson.fromJson(
//							data, new TypeToken<Map<String, List<ShipVoyageData>>>() {
//							}.getType());
//					for(String key:shipNameListMap.keySet()){
//						shipNames.add(key);
//					}
//					smpAdapter.notifyDataSetChanged();
//				}else
//					loadData();
//			}
//		}else
			loadData();

	}

	private void loadData() {

		String PHPSESSID = "";
        List<Cookie> list = GlobalApplication.getInstance().getCookies();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("PHPSESSID")) {
            	PHPSESSID = cookie.getValue();
            }
        }
		Map<String, Object> apiParams = new HashMap<String, Object>();
		data.getZd_JavaManageResult(new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("加载中", "请稍候...");
			}
			@Override
			public void onSuccess(String arg2) {
				
				if(arg2==null||"".equals(arg2)){
					Toast.makeText(ShipListActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();
					return;
				}
				
				Gson gson = new Gson();
				final HashMap<String, Object> rmap = gson.fromJson(
						arg2, new TypeToken<Map<String, Object>>() {
						}.getType());
				if (rmap.get("returnCode").equals("Success")) {
					shipNameListMap.clear();
					
					List<Map<String, Object>> map = (List<Map<String, Object>>) rmap.get("shipVoyageDatas");
					for(Map<String, Object> m:map){
						ShipVoyageData s = new ShipVoyageData(m);
						List<ShipVoyageData> maplist =shipNameListMap.get(s.getShipName());
						if(maplist ==null){
							List<ShipVoyageData> nodes = new ArrayList<ShipVoyageData>();
							nodes.add(s);
							shipNameListMap.put(s.getShipName(), nodes);
						}else{
							maplist.add(s);
						}
					}
					for(String key:shipNameListMap.keySet()){
						shipNames.add(key);
					}
					
					SharedPreferences sp = ShipListActivity.this.getSharedPreferences(ApplicationConstants.ShipVoyageData_SharedPreferences, Context.MODE_PRIVATE);
					Editor editor = sp.edit();
					editor.putString("loadTime", CalendarUtil.toYYYY_MM_DD_HH_MM_SS(Calendar.getInstance()));
					editor.putString("data", new Gson().toJson(shipNameListMap));
					editor.commit();
					
					handler.sendEmptyMessage(1);
				} else {
					Toast.makeText(ShipListActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();
				}
				
				dialog.dismiss();
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipListActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipListActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(ShipListActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipListActivity.this, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.historyHC+PHPSESSID, apiParams, "get");	
		}

//	private void initData() {
//		SharedPreferences sp = getSharedPreferences("UserPowerData", Context.MODE_PRIVATE);
//		String object = sp.getString("UserPower", "");
//		
//		UserPowerData data = null;
//		if(!"".equals(object)){
//			Gson gson = new Gson();
//			data = gson.fromJson(object, new TypeToken<UserPowerData>() {}.getType());
//		}else
//			data = new UserPowerData();
//		
//		dataList.addAll(data.getUserPowerShipDatas());
//	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("历史航次船舶列表");
		/*setRight("分布图", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ShipListActivity.this,ShipFBActivity.class));
			}
		});*/
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
		smpAdapter = new HCShipAdapter(this, shipNames);
		mlistView.setAdapter(smpAdapter);
		mlistView.setOnItemClickListener(this);

	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		String map = (String) mlistView.getItemAtPosition(position);
		if(shipNameListMap!=null){
			List<ShipVoyageData> list = shipNameListMap.get(map);
			if(list!=null)
				startActivity(new Intent(ShipListActivity.this,ShipHCListActivity.class).putExtra("shipName", map));
		}
		
//		Intent intent = new Intent(ShipMoniterActivity.this,ShipLineListActivity.class);
//		// 绑定传输的船舶数据
//		intent.putExtra("shipId", map.get("shipId").toString());
//		intent.putExtra("shipName", map.get("shipName").toString());
//		intent.putExtra("mmsi", map.get("MMSI").toString());
//		startActivity(intent);
	}

}
