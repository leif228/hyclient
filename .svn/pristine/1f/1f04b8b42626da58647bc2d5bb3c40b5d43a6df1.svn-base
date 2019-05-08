package com.hangyi.zd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.BaseAdapter;

import com.eyunda.main.CommonListActivity;
import com.eyunda.third.ApplicationConstants;
import com.hy.client.R;

public class ZdShipDynamicActivity extends CommonListActivity {
	
	private String shipID;
	private String shipName;
	private String lineType;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.zd_ship_play);
		
		Intent intent = getIntent();
		shipID = intent.getStringExtra("shipID");
		shipName = intent.getStringExtra("shipName");
		
		lineType = intent.getStringExtra(ApplicationConstants.historyLineType)!=null
				?intent.getStringExtra(ApplicationConstants.historyLineType):ApplicationConstants.historyLineNormal;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(lineType.equals(ApplicationConstants.historyLinePolice)){
			Intent intent = getIntent();
			String policeType = intent.getStringExtra("policeType");
			if(policeType != null)
				setTitle(policeType+"("+shipName+")");
		} else {
			if (shipName != null)
				setTitle(shipName);
			else
				setTitle("历史回放");
		}
	}

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub
		
	}

}
