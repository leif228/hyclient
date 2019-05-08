package com.eyunda.third.activities.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.eyunda.main.SplashActivity;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.order.OrderData;
import com.eyunda.third.domain.order.TemplateData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 编辑合同-合同条款
 * 
 * @author guoqiang
 *
 */
public class AddOrderTKActivity extends AddOrderActivity implements
OnClickListener {
	Data_loader dataLoader;

	private Button btnSave,btnDelAll;

	private ListView lv;
	private List<Map<String, Object>> lists = new ArrayList<Map<String,Object>>();
	private List<TemplateData> tempList = new ArrayList<TemplateData>();

	private RelativeLayout spLayout;

	private Spinner spTemp;

	private SimpleAdapter tempAdapter;

	private ArrayAdapter<TemplateData> mAdapter;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_order_addtk);
		dataLoader = new Data_loader();
		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		orderId = (Long) bundle.getLong("orderId");
		initView();
		loadDate();
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("编辑合同-合同条款");
		btnAddTK.setOnClickListener(null);
		btnAddTK.setBackgroundColor(0xFF6db7ff);
	}

	private void initView() {
		lv = (ListView)findViewById(R.id.orderTempList);
		btnSave = (Button) findViewById(R.id.btnSave);                                 
		btnDelAll = (Button) findViewById(R.id.btnDelAll);
		spLayout = (RelativeLayout) findViewById(R.id.firstAdd);
		spTemp = (Spinner) findViewById(R.id.orderTemplateSpinner);
		btnSave.setOnClickListener(this);
		btnDelAll.setOnClickListener(this);
		registerForContextMenu(lv);
		mAdapter = new ArrayAdapter<TemplateData>(this,R.layout.spinner_item,R.id.contentTextView,tempList);

		tempAdapter = new SimpleAdapter(this, lists, R.layout.eyd_order_temp_item, new String[]{"num","tempName",
		}, new int[]{R.id.number,R.id.tempName});
		lv.setAdapter(tempAdapter);

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSave:
			if(btnDelAll.getVisibility()==View.GONE){
				Long currentId =((TemplateData)spTemp.getSelectedItem()).getId();
				Map<String, Object> apiParams = new HashMap<String, Object>();
				apiParams.put("orderId", orderId);
				apiParams.put("templateId", currentId);
				dataLoader.getApiResult(saveHandler, "/mobile/order/myOrder/saveOrderItem",apiParams,"get");
			}else{
				//新增一个条款
				Intent intent = new Intent(this,AddOrderOneTemplateActivity.class);
				intent.putExtra("orderId", orderId);
				intent.putExtra("tempId", "0");				 
				intent.putExtra("tempName", "");
				startActivity(intent);
			}
			break;

		case R.id.btnDelAll:
			new AlertDialog.Builder(AddOrderTKActivity.this)
			.setTitle("删除所有条款?")
			.setNegativeButton("取消", null)
			.setPositiveButton("确认", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					Map<String, Object> apiParams2 = new HashMap<String, Object>();
					apiParams2.put("id", orderId);
					dataLoader.getApiResult(delAllHandler, "/mobile/order/myOrder/delAllOrderItem",apiParams2, "post");
				}
			})
			.show();
			break;

		}
	};
	// 保存模板选择结果
	AsyncHttpResponseHandler saveHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			dialog = dialogUtil.loading("通知", "保存中，请稍候...");
		};
		@Override
		public void onFailure(Throwable arg0, String content) {
			super.onFailure(arg0, content);
			dialog.dismiss();
			Toast.makeText(AddOrderTKActivity.this, content,
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onSuccess(String res) {
			super.onSuccess(res);
			dialog.dismiss();
			ConvertData cd = new ConvertData(res);
			if(cd.getReturnCode().equalsIgnoreCase("success")){
				Toast.makeText(AddOrderTKActivity.this, cd.getMessage(), Toast.LENGTH_SHORT).show();
				//返回到当前activity
				Intent intent = new Intent(AddOrderTKActivity.this,AddOrderTKActivity.class);
				Bundle bundle= new Bundle();
				bundle.putLong("orderId",orderId);//合同ID
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		}
	};
	// 保存模板选择结果
	AsyncHttpResponseHandler delAllHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			dialog = dialogUtil.loading("通知", "数据获取中，请稍候...");
		};
		@Override
		public void onFailure(Throwable arg0, String content) {
			super.onFailure(arg0, content);
			dialog.dismiss();
			Toast.makeText(AddOrderTKActivity.this, content,
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onSuccess(String res) {
			super.onSuccess(res);
			dialog.dismiss();
			ConvertData cd = new ConvertData(res);
			if(cd.getReturnCode().equalsIgnoreCase("success")){
				Toast.makeText(AddOrderTKActivity.this, cd.getMessage(), Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(AddOrderTKActivity.this,AddOrderTKActivity.class);
				Bundle bundle= new Bundle();
				bundle.putLong("orderId",orderId);//合同ID
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}else{
				Toast.makeText(AddOrderTKActivity.this, cd.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	};


	@Override
	protected synchronized void loadDate() {
		//获取合同信息
		final Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("orderId", orderId);
		// 获取合同条款列表
		AsyncHttpResponseHandler showHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				Toast.makeText(AddOrderTKActivity.this, content,
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onStart(){
				dialog = dialogUtil.loading("通知", "数据获取中，请稍候...");
			}
			@Override
			public void onSuccess(String res) {
				super.onSuccess(res);
				dialog.dismiss();
				ConvertData cd = new ConvertData(res,"/mobile/order/myOrder/edit",apiParams);
				if(cd.getReturnCode().equalsIgnoreCase("success")){
					HashMap<String, Object> var = (HashMap<String, Object>) cd.getContent();
					HashMap<String, Object> var2 = (HashMap<String, Object>) var.get("orderData");
					OrderData order = new OrderData(var2);
//					List<OrderItemData> orderItemDatas = order.getOrderItemDatas();
//
//					//根据结果控制视图显示
					if(false){
//						//给listView赋值
//						for(OrderItemData oid:orderItemDatas){
//							Map<String, Object> map = new HashMap<String, Object>();
//							map.put("orderId",orderId);
//							map.put("num", "序号"+oid.getNo().toString()+":");
//							map.put("tempId",oid.getId().toString());
//							map.put("tempDetailId",""+oid.getId());
//							map.put("tempName",""+oid.getContent());
//							map.put("btnEdit",false);
//							map.put("btnDel",false);
//							lists.add(map);
//						}
//						tempAdapter.notifyDataSetChanged();
					}else{
						spLayout.setVisibility(View.VISIBLE);
						lv.setVisibility(View.GONE);
						btnDelAll.setVisibility(View.GONE);
						btnSave.setText("保存");
//						TemplateData temp = order.getShipData().getTemplateData();
//						tempList.add(new TemplateData(temp.getId(),temp.getTitle(),temp.getTypeCode()));
//						spTemp.setAdapter(mAdapter);
					}
				}else{
					dialog.dismiss();
					Toast.makeText(AddOrderTKActivity.this, "登录超时，请重新登录", Toast.LENGTH_SHORT).show();
					//跳转到首页
					startActivity(new Intent(AddOrderTKActivity.this,
							SplashActivity.class));
				}
			}
		};
		dataLoader.getApiResult(showHandler, "/mobile/order/myOrder/edit",apiParams, "get");
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
		HashMap<String, Object> map = (HashMap<String, Object>) tempAdapter.getItem(menuInfo.position);
		switch(item.getItemId()) {
		case 1:
			Intent intent = new Intent(AddOrderTKActivity.this,AddOrderOneTemplateActivity.class);
			intent.putExtra("orderId", orderId);
			intent.putExtra("tempId",map.get("tempDetailId").toString());
			intent.putExtra("tempName", map.get("tempName").toString());
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
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				Gson gson = new Gson();
				HashMap<String, Object> result= gson.fromJson(
						(String) arg0,
						new TypeToken<Map<String, Object>>() {
						}.getType());
				if (result.get("returnCode").equals("Success")) {
					// 请求成功后返回到列表视图
					lists.remove(position);
					tempAdapter.notifyDataSetChanged();
					Toast.makeText(AddOrderTKActivity.this,"删除"+result.get("message"), Toast.LENGTH_SHORT).show();

				}
			}

			@Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);
				Toast.makeText(AddOrderTKActivity.this,"删除失败", Toast.LENGTH_SHORT).show();

			}

		};
		HashMap<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("id", map.get("tempDetailId"));
		apiParams.put("_method", "delete");
		dataLoader.getApiResult(handler, "/mobile/order/myOrder/removeOrderItem",apiParams,"post");
	}


}
