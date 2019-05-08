package com.hangyi.zd.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;

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
import com.hangyi.zd.adapter.GroupAdapter;
import com.hangyi.zd.domain.GroupData;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ShipGroupActivity extends CommonActivity implements
OnItemClickListener {
	Data_loader data;
	private EditText startPort;
	private ListView mlistView;
	private List<GroupData> shipSCDataList = new ArrayList<GroupData>();
	private GroupAdapter smpAdapter;
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

	private void loadData() {
		
		String PHPSESSID = "";
        List<Cookie> list = GlobalApplication.getInstance().getCookies();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("PHPSESSID")) {
            	PHPSESSID = cookie.getValue();
            }
        }

		Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("PHPSESSID", PHPSESSID);
		
		data.getZd_JavaManageResult(new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("加载中", "请稍候...");
			}
			@Override
			public void onSuccess(String arg2) {
				dialog.dismiss();
				shipSCDataList.clear();
				
				if(arg2==null||"".equals(arg2)){
					Toast.makeText(ShipGroupActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();
					return;
				}
				
				Gson gson = new Gson();
				final HashMap<String, Object> rmap = gson.fromJson(
						arg2, new TypeToken<Map<String, Object>>() {
						}.getType());
				if (rmap.get("returnCode").equals("Success")) {
					
					List<Map<String, Object>> map = (List<Map<String, Object>>) rmap.get("groupDatas");
					for(Map<String, Object> m:map){
						GroupData gd = new GroupData(m);
						shipSCDataList.add(gd);
					}
				}else
					Toast.makeText(ShipGroupActivity.this, (String)rmap.get("message"), Toast.LENGTH_SHORT).show();
				
				handler.sendEmptyMessage(1);
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipGroupActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipGroupActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(ShipGroupActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipGroupActivity.this, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.groups, apiParams, "get");	
		}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("分组查看");
		setRight("保存", new OnClickListener() {

			@Override
			public void onClick(View v) {
				postSC();
			}

		});
	}
	
	private void postSC() {
		if(shipSCDataList.isEmpty())
			return;
		
		String PHPSESSID = "";
        List<Cookie> list = GlobalApplication.getInstance().getCookies();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("PHPSESSID")) {
            	PHPSESSID = cookie.getValue();
            }
        }
		
		Gson s = new Gson();
		String json = s.toJson(shipSCDataList);

		Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("groupDatas", json);
		apiParams.put("PHPSESSID", PHPSESSID);
		
		data.getZd_JavaManageResult(new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("加载中", "请稍候...");
			}
			@Override
			public void onSuccess(String arg2) {
				dialog.dismiss();
				
				Gson gson = new Gson();
				final HashMap<String, Object> rmap = gson.fromJson(
						arg2, new TypeToken<Map<String, Object>>() {
						}.getType());
				Toast.makeText(ShipGroupActivity.this, (String)rmap.get("message"), Toast.LENGTH_SHORT).show();
				
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipGroupActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipGroupActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(ShipGroupActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipGroupActivity.this, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.saveMobGroups, apiParams, "post");	
		
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
		smpAdapter = new GroupAdapter(this, shipSCDataList);
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
