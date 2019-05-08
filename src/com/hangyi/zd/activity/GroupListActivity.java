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
import com.eyunda.third.activities.NewPageHomeMainActivity;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.CalendarUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.zd.activity.HXDownPopupWindow.GroupResultListener;
import com.hangyi.zd.adapter.GroupListAdapter;
import com.hangyi.zd.adapter.HCShipAdapter;
import com.hangyi.zd.adapter.ShipAdapter;
import com.hangyi.zd.domain.GroupCode;
import com.hangyi.zd.domain.GroupData;
import com.hangyi.zd.domain.MapPortData;
import com.hangyi.zd.domain.ShipVoyageData;
import com.hangyi.zd.domain.ShipVoyageNodeData;
import com.hangyi.zd.domain.UserPowerData;
import com.hangyi.zd.domain.UserPowerShipData;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class GroupListActivity extends CommonActivity implements
OnItemClickListener {
	Data_loader data;
	private EditText startPort;
	private ListView mlistView;
	private List<GroupData> gs = new ArrayList<GroupData>();
	List<GroupData> totalShipCooordDatas = new ArrayList<GroupData>();
	private GroupListAdapter smpAdapter;
	private String gType = GroupCode.gport.getDescription();
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
		setContentView(R.layout.zd_grouplist);
		data = new Data_loader();
		dialogUtil = new DialogUtil(this);
		
		LinearLayout searchContainer = (LinearLayout) findViewById(R.id.searchContainer);// 搜索按钮
		searchContainer.setVisibility(View.GONE);
		
		if(getIntent().getStringExtra("gType")!=null)
			gType = getIntent().getStringExtra("gType");
		
		initView();
		initData();
	}

	private void initData() {
		gs.clear();
		totalShipCooordDatas.clear();
		
		if(gType.equals(GroupCode.gport.getDescription())){
			List<GroupData> list = getPort();
			if(list.size()>0){
				gs.addAll(list);
				handler.sendEmptyMessage(1);
			}
		}else if(gType.equals(GroupCode.gkehu.getDescription())){
			getHx();
		}else if(gType.equals(GroupCode.gship.getDescription())){
			getHx();
		}
	}
	
	private List<GroupData> getPort(){
		List<GroupData> shipCooordDatas = new ArrayList<GroupData>();
		
		SharedPreferences sp = context.getSharedPreferences(ApplicationConstants.MapPortData_SharedPreferences, Context.MODE_PRIVATE);
		String port = sp.getString("MapPort", "");

		Gson gson = new Gson();
		List<MapPortData> rmap = gson.fromJson((String) port,
				new TypeToken<List<MapPortData>>() {
				}.getType());
		if(rmap!=null&&rmap.size()>0){
			for(MapPortData m:rmap){
				GroupData g = new GroupData();
				g.setGroupName(m.getPort_name());
				g.setTempx(m.getX());
				g.setTempy(m.getY());
				g.setPort(true);
				
				shipCooordDatas.add(g);
			}
		}else{
			Toast.makeText(context, "码头数据加载失败！", Toast.LENGTH_LONG).show();
		}
		
		return shipCooordDatas;
	}
	
	private void getHx(){
		
		String PHPSESSID = "";
        List<Cookie> list = GlobalApplication.getInstance().getCookies();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("PHPSESSID")) {
            	PHPSESSID = cookie.getValue();
            	break;
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
				
				try {
					Gson gson = new Gson();
					final HashMap<String, Object> rmap = gson.fromJson(
							arg2, new TypeToken<Map<String, Object>>() {
							}.getType());
					if (rmap.get("returnCode").equals("Success")) {
						
						List<Map<String, Object>> map = (List<Map<String, Object>>) rmap.get("groupDatas");
						
						for(Map<String, Object> m:map){
							GroupData gd = new GroupData(m);
							//0表示客户，1表示船公司
							if(GroupCode.gkehu.getDescription().equals(gType)&&"0".equals(gd.getGroupType())){
								if("所有".equals(gd.getGroupName()))
									continue;
								
								gs.add(gd);
							}else if(GroupCode.gship.getDescription().equals(gType)&&"1".equals(gd.getGroupType())){
								gs.add(gd);
							}
							totalShipCooordDatas.add(gd);
						}
						if(gs.size()>0){
							
							handler.sendEmptyMessage(1);
							
						}else{
							Toast.makeText(context, "数据为空！", Toast.LENGTH_LONG).show();
						}
					}else
						Toast.makeText(context, "加载数据失败！", Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					Toast.makeText(context, "加载数据失败！", Toast.LENGTH_LONG).show();
				}
				dialog.dismiss();
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(context, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(context, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(context, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(context, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.groups, apiParams, "get");	
		}
	private void postSC(final GroupData map) {
		for(GroupData g:totalShipCooordDatas){
			if(g.getGroupID().equals(map.getGroupID())){
				g.setFlag(true);
			}else{
				g.setFlag(false);
			}
		}
		
		String PHPSESSID = "";
        List<Cookie> list = GlobalApplication.getInstance().getCookies();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("PHPSESSID")) {
            	PHPSESSID = cookie.getValue();
            }
        }
		
		Gson s = new Gson();
		String json = s.toJson(totalShipCooordDatas);

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
				
				GroupListActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
//						groupListener.getResult(map);
//						startActivity(new Intent(GroupListActivity.this, NewPageHomeMainActivity.class)
//						.putExtra("groupData", map));
						GlobalApplication.getInstance().setGroupData(map);
						startActivity(new Intent(GroupListActivity.this, NewPageHomeMainActivity.class));
						finish();
					}
				});
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(context, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(context, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(context, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(context, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.saveMobGroups, apiParams, "post");	
		
	}
	private void loadData() {}

	@Override
	protected void onStart() {
		super.onStart();
		if(gType.equals(GroupCode.gport.getDescription())){
			setTitle(GroupCode.gport.getDescription());
		}else if(gType.equals(GroupCode.gkehu.getDescription())){
			setTitle(GroupCode.gkehu.getDescription());
			
		}else if(gType.equals(GroupCode.gship.getDescription())){
			setTitle(GroupCode.gship.getDescription());
			
		}
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
		smpAdapter = new GroupListAdapter(this, gs);
		mlistView.setAdapter(smpAdapter);
		mlistView.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		GroupData map = (GroupData) mlistView.getItemAtPosition(position);
		if(map.isPort()){
//			groupListener.getResult(map);
//			startActivity(new Intent(GroupListActivity.this, NewPageHomeMainActivity.class)
//			.putExtra("groupData", map));
			GlobalApplication.getInstance().setGroupData(map);
			startActivity(new Intent(GroupListActivity.this, NewPageHomeMainActivity.class));
			finish();
			
		}
		else
			postSC(map);
	}

}
