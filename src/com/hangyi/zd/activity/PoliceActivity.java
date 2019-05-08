package com.hangyi.zd.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
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
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.CalendarUtil;
import com.hangyi.tools.ParseJson;
import com.hangyi.zd.adapter.PoliceAdapter;
import com.hangyi.zd.domain.PoliceData;
import com.hangyi.zd.domain.ShipVoyageNodeData;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class PoliceActivity extends CommonActivity implements
OnItemClickListener {
	Data_loader dataLoader;
	private EditText startPort;
	private ListView mlistView;
	private PoliceAdapter smpAdapter;
	public static final int offMonths = -1;
	Thread loadDataThread = null;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				dialog = dialogUtil.loading("加载中", "请稍候...");
				break;
			case 1:
				dialog.dismiss();
				smpAdapter.notifyDataSetChanged();
				break;
			case 2:
				dialog.dismiss();
				Toast.makeText(PoliceActivity.this, "数据异常，请稍后再试！",
						Toast.LENGTH_LONG).show();
				break;
			case 3:
				dialog.dismiss();
				Toast.makeText(PoliceActivity.this, "数据解析出错",
						Toast.LENGTH_LONG).show();
				break;

			default:
				break;
			}
		}
	};
	String startTime="",endTime="";
	List<PoliceData> pds = new ArrayList<PoliceData>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zd_part_home);
		dataLoader = new Data_loader();
		
		LinearLayout searchContainer = (LinearLayout) findViewById(R.id.searchContainer);// 搜索按钮
		searchContainer.setVisibility(View.GONE);
		
		Calendar now = Calendar.getInstance();
		startTime = CalendarUtil.toYYYY_MM_DD_HH_MM_SS(CalendarUtil.addMonths(now, offMonths));
		endTime = CalendarUtil.toYYYY_MM_DD_HH_MM_SS(now);
		
		initView();
	}

	private void loadData() {
		Map<String, Object> apiParams = new HashMap<String, Object>();
		dataLoader.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				handler.sendEmptyMessage(0);
//				dialog = dialogUtil.loading("加载中", "请稍候...");
			}
			@Override
			public void onSuccess(String arg2) {
				try {
					pds.clear();
					List<PoliceData> list = ParseJson.parserPolice(arg2);
					// 按时间排序从大到小
					Collections.sort(list, new Comparator<PoliceData>() {

						@Override
						public int compare(PoliceData lhs, PoliceData rhs) {
							return CalendarUtil.parseYYYY_MM_DD_HH_MM_SS(rhs.getEventTime()).compareTo(
									CalendarUtil.parseYYYY_MM_DD_HH_MM_SS(lhs.getEventTime()));
						}
					});
					// 按未读－已读排序
					List<PoliceData> noReadList = new ArrayList<PoliceData>();
					List<PoliceData> readList = new ArrayList<PoliceData>();
					for(PoliceData p:list){
						if(p.isbRead())
							readList.add(p);
						else
							noReadList.add(p);
					}
					noReadList.addAll(readList);
					
					pds.addAll(noReadList);
					
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					handler.sendEmptyMessage(3);
					
				}
				
//				dialog.dismiss();
//				loadDataThread = null;
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				handler.sendEmptyMessage(2);
				
//				dialog.dismiss();
//				if (content != null && content.equals("can't resolve host"))
//					Toast.makeText(PoliceActivity.this, "数据异常，请稍后再试！",
//							Toast.LENGTH_LONG).show();
//				else if (content != null && content.equals("socket time out")) {
//					Toast.makeText(PoliceActivity.this, "连接服务器超时",
//							Toast.LENGTH_LONG).show();
//				} else if (content != null) {
//					Toast.makeText(PoliceActivity.this, content,
//							Toast.LENGTH_LONG).show();
//				} else
//					Toast.makeText(PoliceActivity.this, "未知异常",
//							Toast.LENGTH_LONG).show();
					
//					loadDataThread = null;
			}
		}, ApplicationUrls.policeList+NewContentFragment.getUrlTimeStr(startTime)+"&EndTime="+NewContentFragment.getUrlTimeStr(endTime), apiParams, "get");	
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
		setTitle("报警");
		
		loadDataThread = new Thread(new Runnable() {
			@Override
			public void run() {
				loadData();
			}
		});
		loadDataThread.setDaemon(true);
		loadDataThread.start();
		
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
		smpAdapter = new PoliceAdapter(this, pds);
		mlistView.setAdapter(smpAdapter);
		mlistView.setOnItemClickListener(this);
 
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		PoliceData map = (PoliceData) mlistView.getItemAtPosition(position);
		startActivity(new Intent(PoliceActivity.this,ShipPoliceActivity.class).putExtra("msgNid", map.getnID()));
		
//		postReadFlag(map);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(loadDataThread != null){
			loadDataThread.interrupt();
			loadDataThread = null;
		}
	}

	private void postReadFlag(final PoliceData map) {
		
		Map<String, Object> apiParams = new HashMap<String, Object>();
		dataLoader.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("数据加载中", "请稍候...");
			}
			@Override
			public void onSuccess(final String arg2) {
				super.onSuccess(arg2);
				
//				int imgSize = 0; 
//				
//				SharedPreferences sp = getSharedPreferences("UserPowerData", Context.MODE_PRIVATE);
//				String object = sp.getString("UserPower", "");
//				
//				Gson gson = new Gson();
//				UserPowerData data = gson.fromJson(object, new TypeToken<UserPowerData>() {}.getType());
//				
//				flag:
//					for(UserPowerShipData upsd:data.getUserPowerShipDatas()){
//						if((upsd.getShipID()).equals("0x"+map.getShipID())){
//							for(ShipModelData smd:upsd.getShipModels()){
//								if(smd.getModel() == ShipModelCode.five){
//									imgSize = smd.getModelNolist().size() ;
//									break flag;
//								}
//							}
//						}
//					}
//				NodeCode nc = NodeCode.getByN(Integer.valueOf(map.getEventType()));
				
				startActivity(new Intent(PoliceActivity.this,ShipPoliceActivity.class)
//				.putExtra("shipName", map.getShipName())
//				.putExtra("shipId", map.getShipID())
//				.putExtra("imgSize", imgSize)
//				.putExtra("policeType", nc!=null?nc.getDescription():"未知报警")
//				.putExtra("hcNum", map.getVorageNumber())
//				.putExtra("start", map.getStart())
//				.putExtra("end", map.getEnd())
//				.putExtra("time", map.getEventTime())
				.putExtra("msgNid", map.getnID()));
				
				dialog.dismiss();
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

				dialog.dismiss();
//				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(PoliceActivity.this, "数据异常，请稍后再试！",
							Toast.LENGTH_LONG).show();
//				else if (content != null && content.equals("socket time out")) {
//					Toast.makeText(PoliceActivity.this, "连接服务器超时",
//							Toast.LENGTH_LONG).show();
//				} else if (content != null) {
//					Toast.makeText(PoliceActivity.this, "失败 ",
//							Toast.LENGTH_LONG).show();
//				} else 
//					Toast.makeText(PoliceActivity.this, "未知异常",
//							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.policeReadFlag+map.getnID(), apiParams, "get");
	
	}

}
