package com.hangyi.zd.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;

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
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.zd.adapter.ShipSCingAdapter;
import com.hangyi.zd.domain.ShipVoyageData;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ShipHCListIngActivity extends CommonActivity implements
OnItemClickListener {
	Data_loader data;
	private EditText startPort;
	private ListView mlistView;
	private ArrayList<ShipVoyageData> dataList =new ArrayList<ShipVoyageData>();
	private ShipSCingAdapter smpAdapter;
	private Map<String,List<ShipVoyageData>> shipNameListMap = new HashMap<String,List<ShipVoyageData>>();
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {// 初始化数据完成
				smpAdapter.notifyDataSetChanged();
				
				if(shipName!=null)
					for(int i=0;i<dataList.size();i++){
						if(shipName.equals(dataList.get(i).getShipName())){
							mlistView.setSelection(i);
							break;
						}
					}
			}
		}
	};
	private String shipName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zd_part_home);
		data = new Data_loader();
		dialogUtil = new DialogUtil(this);
		
		shipName = getIntent().getStringExtra("shipName");
		
		LinearLayout searchContainer = (LinearLayout) findViewById(R.id.searchContainer);// 搜索按钮
		searchContainer.setVisibility(View.GONE);
		
		initView();
		loadData();
//		initData();

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
					Toast.makeText(ShipHCListIngActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();
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
						dataList.add(shipNameListMap.get(key).get(0));
					}
					
					handler.sendEmptyMessage(1);
				} else {
					Toast.makeText(ShipHCListIngActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();
				}
				
				dialog.dismiss();
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipHCListIngActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipHCListIngActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(ShipHCListIngActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipHCListIngActivity.this, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.excuteHC+PHPSESSID, apiParams, "get");	
		}


//	private void initData() {
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
//					Toast.makeText(ShipHCListIngActivity.this, "网络连接异常",
//							Toast.LENGTH_LONG).show();
//				else if (content != null && content.equals("socket time out")) {
//					Toast.makeText(ShipHCListIngActivity.this, "连接服务器超时",
//							Toast.LENGTH_LONG).show();
//				} else if (content != null) {
//					Toast.makeText(ShipHCListIngActivity.this, content,
//							Toast.LENGTH_LONG).show();
//				} else
//					Toast.makeText(ShipHCListIngActivity.this, "未知异常",
//							Toast.LENGTH_LONG).show();
//
//			}
//		}, "/clientapi/?Function=5&Style=json", apiParams, "get");
//	
//	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("当前航次列表");
		/*setRight("分布图", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ShipHCListIngActivity.this,ShipFBActivity.class));
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
		smpAdapter = new ShipSCingAdapter(this, dataList);
		mlistView.setAdapter(smpAdapter);
		mlistView.setSelected(true);
		mlistView.setOnItemClickListener(this);

	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		ShipVoyageData map = (ShipVoyageData) mlistView.getItemAtPosition(position);
		Intent intent = new Intent(ShipHCListIngActivity.this,ShipHCNodeActivity.class);
		intent.putExtra("data", map);
		// 绑定传输的船舶数据
//		intent.putExtra("shipId", map.get("shipId").toString());
//		intent.putExtra("shipName", map.get("shipName").toString());
//		intent.putExtra("mmsi", map.get("MMSI").toString());
		startActivity(intent);
	}

}
