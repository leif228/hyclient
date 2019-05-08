package com.eyunda.third.activities.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eyunda.third.domain.order.TempItemData;
import com.eyunda.third.domain.order.TemplateData;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * 合同模板
 * @author Administrator
 *
 */
public class ContractTemplateActivity extends ShipinfoActivity   {

	private ListView mlistView;
	private SimpleAdapter adapter;
	private Data_loader data;
	private String shipId;
	List<Map<String, Object>> lists = new ArrayList<Map<String,Object>>();
	private String orderId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_ship_report_list);
		shipId=getIntent().getStringExtra("shipId");
		mlistView = (ListView)findViewById(R.id.report_listview);
		data = new Data_loader();
		adapter = new SimpleAdapter(this, getData(), R.layout.eyd_apply_item,
				new String[]{"num","content","tempId"}, 
				new int[] { R.id.app_No,R.id.content});
		mlistView.setAdapter(adapter);
		registerForContextMenu(mlistView);
	}
	public void addNew(View v){
		Intent intent = new Intent(this,EditTemplateActivity.class);
		intent.putExtra("shipId", id);
		intent.putExtra("orderTemplateId", orderId);
		intent.putExtra("orderItemId", "0");
		startActivity(intent);
	}
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("合同模板");
		menu_order.setOnClickListener(null);
		menu_order.setBackgroundColor(0xFF6db7ff);
		menu_basic.setBackgroundColor(Color.parseColor("#3B79C4"));
        
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// set context menu title
		menu.setHeaderTitle("文件操作");
		// add context menu item
		menu.add(0, 1, Menu.NONE, "编辑");
		menu.add(0, 2, Menu.NONE, "删除");
	}	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// 得到当前被选中的item信息
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		Log.v("this", "context item seleted ID="+ menuInfo.id);
		HashMap<String, Object> map = (HashMap<String, Object>) adapter.getItem(menuInfo.position);
		switch(item.getItemId()) {
		case 1:
			Intent intent = new Intent(ContractTemplateActivity.this,EditTemplateActivity.class);
			intent.putExtra("shipId", shipId);
			intent.putExtra("content", map.get("content").toString());
			intent.putExtra("orderTemplateId", map.get("tempId").toString());
			intent.putExtra("orderItemId", map.get("orderItemId").toString());
			startActivity(intent);
			break;
		case 2:
			delete(menuInfo.position,map);

			break;
		default:
			return super.onContextItemSelected(item);
		}
		return false;
	}

	private void delete(final int position, HashMap<String, Object> map) {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "正在删除中...");
			}
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				dialog.dismiss();
				Gson gson = new Gson();
				HashMap<String, Object> result= gson.fromJson(
						(String) arg0,
						new TypeToken<Map<String, Object>>() {
						}.getType());
				if (result.get("returnCode").equals("Success")) {
					// 请求成功后返回到列表视图
					lists.remove(position);
					adapter.notifyDataSetChanged();
					Toast.makeText(ContractTemplateActivity.this,"删除"+result.get("message"), Toast.LENGTH_SHORT).show();

				}
			}

			@Override
			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub
				super.onFailure(arg0);
				dialog.dismiss();
				Toast.makeText(ContractTemplateActivity.this,"删除失败", Toast.LENGTH_SHORT).show();

			}

		};
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", map.get("orderItemId"));
		data.getApiResult(handler, "/mobile/ship/myShip/removeOrderItem",params,"get");
	}
	//获取合同模板
	private List<Map<String, Object>> getData() {
		final HashMap<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler resHandler = new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "数据获取中，请稍后...");
			}
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String arg0) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0);
				dialog.dismiss();
				Gson gson = new Gson();
				HashMap<String, Object> result= gson.fromJson((String) arg0,
						new TypeToken<Map<String, Object>>() {}.getType());
				if (result.get("returnCode").equals("Success")) {
					HashMap<String, Object> content = (HashMap<String, Object>) result.get("content");
					Map<String, Object> shipParams=   (Map<String, Object>) content.get("shipData");
					ShipData  shipData = new ShipData(shipParams);
					TemplateData temp = shipData.getTemplateData();
					orderId=temp.getId().toString();
					List<TempItemData> tempList = temp.getTempItemDatas();
					for(TempItemData tempData : tempList){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("num", "序号"+tempData.getNo().toString()+":");//序号
						map.put("content", tempData.getContent());  //内容
						map.put("tempId", tempData.getTempId());  //模板id
						map.put("orderItemId", tempData.getId());  //id
						lists.add(map);
					}
					adapter.notifyDataSetChanged();
				}

			}
			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				if (content != null && content.equals("socket time out")) {
					Toast.makeText(ContractTemplateActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				}else if(content!=null){
					Toast.makeText(ContractTemplateActivity.this, content,Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(ContractTemplateActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();	
				}
			}
		};

		params.put("id", shipId);
		data.getApiResult(resHandler,"/mobile/ship/myShip/edit", params,"get");
		return lists;
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		lists.clear();
		getData();
	}

}
