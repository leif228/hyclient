package com.eyunda.third.activities.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.adapters.ship.OrderCargoAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.cargo.CargoData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class OrderCargoListActivity extends CommonListActivity implements
		OnItemClickListener {

	private ListView mListView;
	private OrderCargoAdapter adapter;
	private String orderId;
	private Data_loader data;
	private ArrayList<Map<String, Object>> mInfos;
	DialogUtil dialogUtil;
	ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_cargo_list);
		mListView = (ListView) findViewById(R.id.cargoList);
		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		orderId = (String) bundle.getString("orderId");
		data = new Data_loader();
		adapter = new OrderCargoAdapter(this, getData(),orderId);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
		dialogUtil = new DialogUtil(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("我的货物列表");
	}

	private ArrayList<Map<String, Object>> getData() {
		final HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("operCargo","all");
		mInfos = new ArrayList<Map<String, Object>>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "数据获取中...");
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0,"/mobile/cargo/AllCargos",hashMap);
				if (cd.getReturnCode().equals("Success")) {
					HashMap<String, Object> ma = (HashMap<String, Object>) cd
							.getContent();
					List<Map<String, Object>> list = (List<Map<String, Object>>) ma
							.get("cargos");

					if (list.size() > 0) {
						for (Map<String, Object> mm : list) {
							CargoData cargo = new CargoData(mm);
							Map<String, Object> map = new HashMap<String, Object>();
							// map.put("userId",
							// cargo.getOwnerId().toString());
							map.put("cargoId", cargo.getId().toString());
							map.put("consignerId", cargo.getOwnerId()
									.toString());
							map.put("chatName", cargo.getOwner()
									.getNickName());

							map.put("cargoImage", cargo.getCargoImage());
							map.put("shipper", "托运人:"
									+ cargo.getOwner().getTrueName());
							map.put("cargoName", "货名:" + cargo.getCargoName());
							map.put("cargoType", "货类:"
									+ cargo.getCargoType().getDescription());
							map.put("wrapCount", "数量:" );
							map.put("totalWeight",
									"总重:" + cargo.getContainerTeus() + "吨");
							map.put("number", "货类:"
									+ cargo.getCargoType().getDescription());
							map.put("timelimit", cargo.getPeriodCode());
							map.put("port", cargo.getStartFullName() + " 到  "
									+ cargo.getEndFullName());
							map.put("unitPrice", "单价:" + cargo.getPriceDes());
							map.put("totalPrice",
									"总价:" + cargo.getTransFeeDes());
							map.put("endTime", "截止:" + cargo.getPeriodTime());
							map.put("remark", cargo.getRemark());
							map.put("cargoStatus", cargo.getCargoStatus()
									.getDescription());
							map.put("Status", cargo.getCargoStatus());

							map.put("cargoData", cargo);
							map.put("phone", cargo.getOwner().getMobile());
							map.put("btnPublish", false);
							map.put("btnEdit", false);
							map.put("btnDelete", false);
							map.put("btnCancel", false);
							// 滚动到最底部
							// mListView.setSelection(mListView.getAdapter().getCount()-1);
							mInfos.add(map);
						}
					}
					adapter.notifyDataSetChanged();

				} else {
					Toast.makeText(OrderCargoListActivity.this,
							cd.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(OrderCargoListActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(OrderCargoListActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(OrderCargoListActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(OrderCargoListActivity.this, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		};
		
		data.getApiResult(handler, "/mobile/cargo/AllCargos", hashMap, "get");

		return mInfos;
	}

	@Override
	protected BaseAdapter setAdapter() {
		return null;
	}

	@Override
	protected void loadDate() {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> map = (HashMap<String, Object>) mListView
				.getItemAtPosition(position);
		Intent intent = new Intent(this, AddOrderOneGSHActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("cargoId", map.get("cargoId").toString());
		bundle.putString("orderId", orderId);
		bundle.putInt("request", 1);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}

}
