package com.hangyi.zd.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eyunda.main.CommonListActivity;
import com.eyunda.third.activities.NewPageHomeMainActivity;
import com.eyunda.third.domain.location.ShipCooordData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.zd.adapter.SearchShipAdapter;
import com.hy.client.R;

public class ShipSeachActivity extends CommonListActivity implements
		OnItemClickListener,View.OnClickListener{

	Data_loader data;
	private ListView mlistView;
	private SearchShipAdapter smpAdapter;
	List<ShipCooordData> shipCooordDatas = new ArrayList<ShipCooordData>();
	List<ShipCooordData> shipSearchCooordDatas = new ArrayList<ShipCooordData>();
    private EditText etInput;
    private ImageView ivDelete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zd_ship_seach);

		etInput = (EditText) findViewById(R.id.search_et_input);
		ivDelete = (ImageView) findViewById(R.id.search_iv_delete);
		mlistView = (ListView) findViewById(R.id.mlistView);
		smpAdapter = new SearchShipAdapter(this, shipCooordDatas);
		mlistView.setAdapter(smpAdapter);
		mlistView.setOnItemClickListener(this);
		
		initView();
		initData();
	}

	private void initView() {
		ivDelete.setOnClickListener(this);

		etInput.addTextChangedListener(new EditChangedListener());
		etInput.setOnClickListener(this);
		etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int actionId,
					KeyEvent keyEvent) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
				}
				return true;
			}
		});
	}

	private void initData() {
		SharedPreferences sp = getSharedPreferences("SearchShipData",
				Context.MODE_PRIVATE);
		String shipCooordDatasJson = sp.getString("SearchShipData", "");
		
		List<ShipCooordData> ss = null;
		if (!shipCooordDatasJson.equals("")) {
			Gson gson = new Gson();
			ss = gson.fromJson(shipCooordDatasJson,
					new TypeToken<List<ShipCooordData>>() {
					}.getType());
		}

		if (ss != null && ss.size() > 0) {
			shipCooordDatas.addAll(ss);
			smpAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		ShipCooordData map = (ShipCooordData) mlistView.getItemAtPosition(position);
		Intent intent = new Intent(ShipSeachActivity.this,
				NewPageHomeMainActivity.class);

		intent.putExtra("shipId", map.getShipID());
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void onStart() {
		super.onStart();

	};

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_iv_delete:
			etInput.setText("");
			smpAdapter = new SearchShipAdapter(ShipSeachActivity.this, shipCooordDatas);
    		mlistView.setAdapter(smpAdapter);
			break;
		}

	}
	
	private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i1, int i2, int i3) {
            if (!"".equals(charSequence.toString())) {
                ivDelete.setVisibility(View.VISIBLE);
                
                shipSearchCooordDatas.clear();
                if (shipCooordDatas != null && shipCooordDatas.size() > 0) {
                	for(int i=0;i<shipCooordDatas.size();i++){
                		if (shipCooordDatas.get(i).getShipName().contains(charSequence.toString().trim())) {
                			shipSearchCooordDatas.add(shipCooordDatas.get(i));
                    	}
                	}
                	smpAdapter = new SearchShipAdapter(ShipSeachActivity.this, shipSearchCooordDatas);
            		mlistView.setAdapter(smpAdapter);
                }
                
            } else {
                ivDelete.setVisibility(View.GONE);
                smpAdapter = new SearchShipAdapter(ShipSeachActivity.this, shipCooordDatas);
        		mlistView.setAdapter(smpAdapter);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}
