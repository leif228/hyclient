package com.eyunda.third.activities.cargo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.activities.home.HomeSearchResultActivity;
import com.eyunda.third.adapters.cargo.AgentAdapter;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.cargo.CargoData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.log.Log;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class AddCargoResultActivity extends CommonListActivity implements OnItemClickListener{

	private ListView mListView;
	private AgentAdapter adapter;
	private Data_loader data=new Data_loader();
	private ArrayList<Map<String, Object>> mInfos;
	private String words;
	private DialogUtil dialogUtil;
	private ProgressDialog dialog;
	private String cargoId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ship_detail_top);
		mListView = (ListView)findViewById(R.id.agent_cargoList);
		mListView.setOnItemClickListener(this);
		Intent intent = getIntent();
		words =intent.getStringExtra("keywords");
		dialogUtil = new DialogUtil(this);
		adapter = new AgentAdapter(this,getData());
		mListView.setAdapter(adapter);
	}
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("货运信息");;
	}

	private ArrayList<Map<String, Object>> getData() {
		mInfos= new ArrayList<Map<String,Object>>(); 
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "正在联网获取数据...");
			}
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0);
				if(cd.getReturnCode().equals("Success")){
					//Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
					@SuppressWarnings("unchecked")
					HashMap<String,Object> ma = (HashMap<String, Object>) cd.getContent();
					List list = (List<String>) ma.get("cargoDatas");
					for(int i=0;i<list.size();i++){
						CargoData cargo = new CargoData((Map<String, Object>) list.get(i));
						HashMap<String, Object> hashMap = new HashMap<String, Object>();
						hashMap.put("cargoId", cargo.getId().toString());
						hashMap.put("logo", cargo.getOwner().getUserLogo());
						hashMap.put("trueName", cargo.getOwner().getLoginName());
						hashMap.put("mobile", "电话:"+cargo.getOwner().getMobile());
						hashMap.put("email","Email:"+ cargo.getOwner().getEmail());
						hashMap.put("cargoTypeName","货类:"+cargo.getCargoType().getDescription());
						hashMap.put("cargoName","货名:"+ cargo.getCargoName());
						mInfos.add(hashMap);
						adapter.notifyDataSetChanged();
					}
				}else{
					Toast.makeText(getApplicationContext(), "未找到匹配成功的货运信息！", Toast.LENGTH_SHORT).show();

				}
			}
			
			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				if (content != null && content.equals("socket time out")) {
					Toast.makeText(AddCargoResultActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				}else if(content!=null){
					Toast.makeText(AddCargoResultActivity.this, "请检查网络",Toast.LENGTH_SHORT).show();
			}
			}
		};
		HashMap<String, Object> map  = new HashMap<String, Object>();
		map.put("cargoKeyWords", words);
		Log.v("params",words);
		data.getApiResult(handler, "/mobile/cargo/myCargo/serachCargos",map,"get");

		return mInfos;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		HashMap<String, Object> map = (HashMap<String, Object>)mListView.getItemAtPosition(position);
		cargoId=map.get("cargoId").toString();

		AlertDialog.Builder ad= new AlertDialog.Builder(AddCargoResultActivity.this); 	
		ad.setTitle("添加货物代理:"); 
		ad.setIcon(android.R.drawable.ic_dialog_info); 
		ad.setPositiveButton("代理",new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				saveCargo();
			}
		});
		ad.setNegativeButton("取消", null);
		ad.show();
	}
	protected void saveCargo() {
		AsyncHttpResponseHandler ahr = new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "正在联网获取数据...");
			}
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				dialog.dismiss();
				finish();
				Toast.makeText(getApplicationContext(), "代理成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(AddCargoResultActivity.this,CargoListActivity.class);
				startActivity(intent);
			}
			@Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "获取失败，请稍后再试", Toast.LENGTH_SHORT).show();

			}
		};
		HashMap<String, Object> map  = new HashMap<String, Object>();
		map.put("cargoId",cargoId);
		Log.v("params",words);
		data.getApiResult(ahr, "/mobile/cargo/myCargo/saveOperCargo",map,"post");

	}
	@Override
	protected BaseAdapter setAdapter() {
		return null;
	}
	@Override
	protected void loadDate() {	
	}
}
