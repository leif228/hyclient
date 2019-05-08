package com.hangyi.zd.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.zd.adapter.ShipAdapter;
import com.hangyi.zd.domain.UserPowerData;
import com.hangyi.zd.domain.UserPowerShipData;
import com.hy.client.R;
import com.ygl.android.ui.BaseActivity;

public class LSShipListActivity extends BaseActivity implements
OnItemClickListener {
	Data_loader data;
	DialogUtil dialogUtil;
	private EditText startPort;
	private ListView mlistView;
	private ArrayList<UserPowerShipData> dataList =new ArrayList<UserPowerShipData>();;
	private ArrayList<UserPowerShipData> tempList =new ArrayList<UserPowerShipData>();;
	private ShipAdapter smpAdapter;
	
	private Button top_back;
	private LinearLayout ll_search;
	private EditText search_et_input;
	private ImageView search_iv_delete;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zd_part_home2);
		data = new Data_loader();
		dialogUtil = new DialogUtil(this);
		
		LinearLayout searchContainer = (LinearLayout) findViewById(R.id.searchContainer);// 搜索按钮
		searchContainer.setVisibility(View.GONE);
		
		initData();
		initView();

	}

	private void initData() {
		SharedPreferences sp = getSharedPreferences("UserPowerData", Context.MODE_PRIVATE);
		String object = sp.getString("UserPower", "");
		
		UserPowerData data = null;
		if(!"".equals(object)){
			Gson gson = new Gson();
			data = gson.fromJson(object, new TypeToken<UserPowerData>() {}.getType());
		}else
			data = new UserPowerData();
		
		dataList.addAll(data.getUserPowerShipDatas());
	}

	@Override
	protected void onStart() {
		super.onStart();
//		setTitle("船舶列表");
//		setRight("分布图", new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(LSShipListActivity.this,ShipFBActivity.class));
//			}
//		});
	}
	@Override
	public boolean backKeyPressed() {
		//隐藏软键盘
        InputMethodManager imm = (InputMethodManager) LSShipListActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromWindow(search_et_input.getWindowToken(), 0);
		finish();
		return true;
	}

	private void initView() {
		this.top_back = (Button) findViewById(R.id.top_back);
		this.top_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//隐藏软键盘
		        InputMethodManager imm = (InputMethodManager) LSShipListActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
//		        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		        imm.hideSoftInputFromWindow(search_et_input.getWindowToken(), 0);
				finish();
			}
		});
		this.search_et_input = (EditText) findViewById(R.id.search_et_input);
		this.search_iv_delete = (ImageView) findViewById(R.id.search_iv_delete);
		this.ll_search = (LinearLayout) findViewById(R.id.ll_search);
		this.ll_search.setOnClickListener(onClickListener);
		
		search_iv_delete.setOnClickListener(onClickListener);
		search_et_input.addTextChangedListener(new EditChangedListener());
		search_et_input.setOnClickListener(onClickListener);
		
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
		smpAdapter = new ShipAdapter(this, dataList);
		mlistView.setAdapter(smpAdapter);
		mlistView.setOnItemClickListener(this);

	}
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if(arg0.getId() == R.id.ll_search){
			}else if(arg0.getId() == R.id.search_iv_delete){
				search_et_input.setText("");
				
				smpAdapter = new ShipAdapter(LSShipListActivity.this, dataList);
				mlistView.setAdapter(smpAdapter);
				mlistView.setOnItemClickListener(LSShipListActivity.this);
			}else if(arg0.getId() == R.id.search_et_input){
			}
		}
	};
	private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i1, int i2, int i3) {
            if (!"".equals(charSequence.toString())) {
            	search_iv_delete.setVisibility(View.VISIBLE);
                
            	tempList.clear();
                if (dataList != null && dataList.size() > 0) {
                	for(int i=0;i<dataList.size();i++){
                		if (dataList.get(i).getShipName().contains(charSequence.toString().trim())) {
                			tempList.add(dataList.get(i));
                    	}
                	}
                	smpAdapter = new ShipAdapter(LSShipListActivity.this, tempList);
    				mlistView.setAdapter(smpAdapter);
    				mlistView.setOnItemClickListener(LSShipListActivity.this);
    				
                }
                
            } else {
            	search_iv_delete.setVisibility(View.GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		UserPowerShipData map = (UserPowerShipData) mlistView.getItemAtPosition(position);
		startActivity(new Intent(LSShipListActivity.this,ZdShipDynamicActivity.class)
		.putExtra("shipID", map.getShipID())
		.putExtra("shipName", map.getShipName())
		.putExtra(ApplicationConstants.historyLineType, ApplicationConstants.historyLineNormal));
		
		//隐藏软键盘
        InputMethodManager imm = (InputMethodManager) LSShipListActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
//      imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
      imm.hideSoftInputFromWindow(search_et_input.getWindowToken(), 0);
		
//		Intent intent = new Intent(ShipMoniterActivity.this,ShipLineListActivity.class);
//		// 绑定传输的船舶数据
//		intent.putExtra("shipId", map.get("shipId").toString());
//		intent.putExtra("shipName", map.get("shipName").toString());
//		intent.putExtra("mmsi", map.get("MMSI").toString());
//		startActivity(intent);
	}

}
