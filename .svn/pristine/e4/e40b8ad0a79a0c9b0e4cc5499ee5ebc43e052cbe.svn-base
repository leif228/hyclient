package com.hangyi.zd.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.NewPageHomeMainActivity;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.zd.adapter.ShipGroupExpandableListAdapter;
import com.hangyi.zd.domain.GroupData;
import com.hangyi.zd.domain.MapPortData;
import com.hangyi.zd.domain.ShipGroupNodeData;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class NewShipGroupActivity extends CommonActivity {
	Data_loader data;

	private List<ShipGroupNodeData> mNews = new ArrayList<ShipGroupNodeData>();
	List<GroupData> shipSCDataList;
	
	private ExpandableListView operator_list;
	ShipGroupExpandableListAdapter adapter;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {// 初始化数据完成
				initList();
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zd_ship_group_new);
		data = new Data_loader();
		dialogUtil = new DialogUtil(this);
		
		this.operator_list = (ExpandableListView) this.findViewById(R.id.operator_list);
		operator_list.setGroupIndicator(null);
		adapter = new ShipGroupExpandableListAdapter(this, operator_list);
		
		setListView();
		loadData();
	}

	protected void initData() {
		mNews.clear();
		SharedPreferences sp = context.getSharedPreferences( "MapPortData", Context.MODE_PRIVATE);
		String port = sp.getString("MapPort", "");

		Gson gson = new Gson();
		List<MapPortData> rmap = gson.fromJson((String) port,
				new TypeToken<List<MapPortData>>() {
				}.getType());
		if(true){
			ShipGroupNodeData hd = new ShipGroupNodeData();
			hd.setShipName("码头");
			if(rmap!=null&&rmap.size()>0){
				
				hd.setHandlerDatas(rmap);
				
			}
			mNews.add(hd);
		}
		if(true){
			
			ShipGroupNodeData hd = new ShipGroupNodeData();
			hd.setShipName("航线");
			if(shipSCDataList!=null&&shipSCDataList.size()>0){
				
				List<MapPortData> handlerDatas = new ArrayList<MapPortData>();
				for(GroupData g:shipSCDataList){
					MapPortData m = new MapPortData();
					m.setPort_name(g.getGroupName());
					m.setX(g.getGroupID());
					handlerDatas.add(m);
				}
				hd.setHandlerDatas(handlerDatas);
				
			}
			mNews.add(hd);
		}
		
		if(true){
			ShipGroupNodeData hd = new ShipGroupNodeData();
			hd.setShipName("客户");
			
			mNews.add(hd);
		}
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
				mNews.clear();
				
				if(arg2==null||"".equals(arg2)){
					Toast.makeText(NewShipGroupActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();
					return;
				}
				
				Gson gson = new Gson();
				final HashMap<String, Object> rmap = gson.fromJson(
						arg2, new TypeToken<Map<String, Object>>() {
						}.getType());
				if (rmap.get("returnCode").equals("Success")) {
					
					List<Map<String, Object>> map = (List<Map<String, Object>>) rmap.get("groupDatas");
					shipSCDataList = new ArrayList<GroupData>();
					for(Map<String, Object> m:map){
						GroupData gd = new GroupData(m);
						shipSCDataList.add(gd);
					}
					initData();
				}else
					Toast.makeText(NewShipGroupActivity.this, (String)rmap.get("message"), Toast.LENGTH_SHORT).show();
				
				handler.sendEmptyMessage(1);
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(NewShipGroupActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(NewShipGroupActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(NewShipGroupActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(NewShipGroupActivity.this, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.groups, apiParams, "get");	
		}

	private void setListView() {
		initList();

		operator_list.setAdapter(adapter);
		int groupCount = operator_list.getCount();
		
		for (int i=0; i<groupCount; i++) {
			operator_list.expandGroup(i);
		};

		operator_list.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				return false;
			}
		});

		operator_list.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int parent, int children, long arg4) {
				ShipGroupNodeData s = adapter.getGroup(parent);
				MapPortData map = adapter.getChild(parent, children);
				
				Intent intent = new Intent(NewShipGroupActivity.this,
						NewPageHomeMainActivity.class);
				if(s.getShipName().equals("码头")){
					intent.putExtra("isPost", true);
					intent.putExtra("x", map.getX());
					intent.putExtra("y", map.getY());
					
					setResult(RESULT_OK, intent);
					finish();
				}else if(s.getShipName().equals("航线")){
					postSC(map.getX());
				}else if(s.getShipName().equals("客户")){
					
				}
				return false;
			}
		});
	}
	private void postSC(String groupId) {
		for(GroupData g:shipSCDataList){
			if(g.getGroupID().equals(groupId)){
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
//				Toast.makeText(NewShipGroupActivity.this, (String)rmap.get("message"), Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent(NewShipGroupActivity.this,
						NewPageHomeMainActivity.class);
				intent.putExtra("isPost", false);
				
				setResult(RESULT_OK, intent);
				finish();
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(NewShipGroupActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(NewShipGroupActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(NewShipGroupActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(NewShipGroupActivity.this, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.saveMobGroups, apiParams, "post");	
		
	}

	public void initList() {
		adapter.RemoveAll();
		initData();
		List<ShipGroupExpandableListAdapter.TreeNode> treeNode = adapter.GetTreeNode();
		for (int i = 0; i < mNews.size(); i++) {
			ShipGroupExpandableListAdapter.TreeNode node = new ShipGroupExpandableListAdapter.TreeNode();
			node.parent = mNews.get(i);
			List child = ((ArrayList) mNews.get(i).getHandlerDatas());
			if (child != null) {
				for (int ii = 0; ii < child.size(); ii++) {
					node.childs.add(child.get(ii));
				}
			}
			treeNode.add(node);
		}

		adapter.UpdateTreeNode(treeNode);
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onStart() {
		super.onStart();
//		if(svd!=null)
			setTitle("航线");
//		else
//			setTitle("节点信息列表");
//		setRight("关闭", new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
	}

}
