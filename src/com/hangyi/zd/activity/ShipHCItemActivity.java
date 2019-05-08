package com.hangyi.zd.activity;

import java.util.ArrayList;
import java.util.Map;

import android.os.Bundle;
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
import com.eyunda.third.loaders.Data_loader;
import com.hangyi.zd.adapter.ShipSCAdapter;
import com.hy.client.R;

public class ShipHCItemActivity extends CommonActivity implements
OnItemClickListener {
	Data_loader data;
	private EditText startPort;
	private ListView mlistView;
	private ArrayList<Map<String, Object>> dataList;
	private ShipSCAdapter smpAdapter;
	String shipName = "998";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zd_part_home);
		data = new Data_loader();
		dialogUtil = new DialogUtil(this);
		
		LinearLayout searchContainer = (LinearLayout) findViewById(R.id.searchContainer);// 搜索按钮
		searchContainer.setVisibility(View.GONE);
		
		initView();

	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle(shipName+"航次列表");
		setRight("关闭", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
		
		dataList =new ArrayList<Map<String,Object>>();
		mlistView =  (ListView)findViewById(R.id.mlistView);
		smpAdapter = new ShipSCAdapter(this, dataList);
		mlistView.setAdapter(smpAdapter);
		mlistView.setOnItemClickListener(this);

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
