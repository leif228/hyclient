package com.hangyi.zd.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.CalendarUtil;
import com.hangyi.zd.adapter.TreeViewExpandableListAdapter;
import com.hangyi.zd.domain.HcNodeChildData;
import com.hangyi.zd.domain.HcNodeData;
import com.hangyi.zd.domain.NodeCode;
import com.hangyi.zd.domain.ShipVoyageData;
import com.hangyi.zd.domain.ShipVoyageNodeData;
import com.hy.client.R;

public class ShipHCNodeActivity extends CommonActivity implements
		OnItemClickListener {
	Data_loader data;

	private ShipVoyageData svd;
	private List<HcNodeData> mNews = new ArrayList<HcNodeData>();
	ExpandableListView expandableList;
	TreeViewExpandableListAdapter adapter;
	private boolean pointed = false;
	private HcNodeData preHcNodeData = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zd_hc_node);
		data = new Data_loader();
		dialogUtil = new DialogUtil(this);
		svd = (ShipVoyageData) getIntent().getSerializableExtra("data");
		initView();

	}

	public void initList() {
		adapter.RemoveAll();
		
		if(svd!=null){
			List<ShipVoyageNodeData> list = svd.getNodes();//已经按optime排序过
			for(ShipVoyageNodeData s:list){
				NodeCode code = NodeCode.getByN(Integer.valueOf(s.getStage()));
				if(code!=null){
					int n = Integer.valueOf(s.getStage());
					if(NodeCode.arrivedLoadingDock.getN()<=n&&n<=NodeCode.unloadingEnd.getN()){
						HcNodeData h = new HcNodeData();
						h.setStatus(code);
						h.setShipID(svd.getShipID());
						h.setShipName(svd.getShipName());
						h.setOpTime(s.getOpTime());
						h.setWeather(s.getWeather());
						
						mNews.add(h);
					}else{
						if(!mNews.isEmpty()){
							HcNodeChildData cd = new HcNodeChildData();
							
							String police = "报警信息："+svd.getShipName()+"，"+s.getOpTime()
									+"，发生了"+code.getDescription()+"事件。";
							cd.setPolice(police);
							cd.setWeather(s.getWeather());
							
							mNews.get(mNews.size()-1).getHandlerDatas().add(cd);
						}
					}
					
				}
			}
		}

		List<TreeViewExpandableListAdapter.TreeNode> treeNode = adapter
				.GetTreeNode();
		for (int i = 0; i < mNews.size(); i++) {
			TreeViewExpandableListAdapter.TreeNode node = new TreeViewExpandableListAdapter.TreeNode();
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
		if(svd!=null)
			setTitle(svd.getShipName());
		else
			setTitle("节点信息列表");
		setRight("关闭", new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initView() {
		expandableList = (ExpandableListView) findViewById(R.id.category_items);
		adapter = new TreeViewExpandableListAdapter(this, expandableList);
		expandableList.setGroupIndicator(null);

		initList();

		expandableList.setAdapter(adapter);
		
	    for(int i = 0; i < adapter.getGroupCount(); i++){  
	    	expandableList.expandGroup(i);  
	     }  

		expandableList.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if(!pointed){
					pointed = true;
					preHcNodeData = (HcNodeData) adapter.getGroup(groupPosition);
					
				}else{
					final HcNodeData sedHcNodeData = (HcNodeData) adapter.getGroup(groupPosition);
					DialogUtil dialogAlert = new DialogUtil(ShipHCNodeActivity.this);
					dialogAlert.showDialogFromConfig2("提示", "要查看("+preHcNodeData.getStatus().getDescription()
							+"节点)~("+sedHcNodeData.getStatus().getDescription()+"节点)时段历史回放吗?", new Handler() {
						@Override
						public void handleMessage(Message msg) {
							super.handleMessage(msg);
							switch (msg.what) {
							case 1:
								String startTime="",endTime="";
								if(CalendarUtil.parseYYYY_MM_DD_HH_MM_SS(preHcNodeData.getOpTime()).getTimeInMillis()>
								CalendarUtil.parseYYYY_MM_DD_HH_MM_SS(sedHcNodeData.getOpTime()).getTimeInMillis()){
									startTime = sedHcNodeData.getOpTime();
									endTime = preHcNodeData.getOpTime();
								}else{
									startTime = preHcNodeData.getOpTime();
									endTime = sedHcNodeData.getOpTime();
								}
								
								Intent intent = new Intent(ShipHCNodeActivity.this,ZdShipDynamicActivity.class); 
//								if(sedHcNodeData.getShipID().startsWith("0x"))
									intent.putExtra("shipID", sedHcNodeData.getShipID());
//								else
//									intent.putExtra("shipID", IntToHex(Integer.valueOf(sedHcNodeData.getShipID())));
								intent.putExtra("shipName", sedHcNodeData.getShipName());
								intent.putExtra("startTime", startTime);
								intent.putExtra("endTime", endTime);
								intent.putExtra(ApplicationConstants.historyLineType, ApplicationConstants.historyLineNormal);
								ShipHCNodeActivity.this.startActivity(intent);;
								
								pointed = false;
								preHcNodeData =null;
								
								break;
							case 2:
								pointed = false;
								preHcNodeData =null;
								break;

							default:
								break;
							}
							
						}
					});
				}
				return true;
			}
		});

		expandableList.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int parent, int children, long arg4) {
				HcNodeChildData map = adapter.getChild(parent, children);
				return true;
			}
		});
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		// HashMap<String, Object> map = (HashMap<String, Object>)
		// mlistView.getItemAtPosition(position);
		// Intent intent = new
		// Intent(ShipMoniterActivity.this,ShipLineListActivity.class);
		// // 绑定传输的船舶数据
		// intent.putExtra("shipId", map.get("shipId").toString());
		// intent.putExtra("shipName", map.get("shipName").toString());
		// intent.putExtra("mmsi", map.get("MMSI").toString());
		// startActivity(intent);
	}

}
