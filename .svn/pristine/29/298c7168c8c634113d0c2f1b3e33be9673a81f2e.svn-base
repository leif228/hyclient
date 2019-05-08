package com.eyunda.third.activities.ship;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.order.OrderData;
import com.eyunda.third.domain.ship.ShipMonitorPlantCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.loaders.Image_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;
/**
 * 调整地图数据源
 * @author guoqiang
 *
 */
public class ShipDataSource extends CommonActivity implements
OnClickListener{

	Image_loader imgLoader;
	Data_loader dataLoader;
	
	private Button saveBtn ;
	
	private RadioGroup mRadioGroup;
	private RadioButton ais, shipMP;
	private ShipMonitorPlantCode smpc;
	private String shipId;
	private String mmsi;
	//处理保存结果
	AsyncHttpResponseHandler resultHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onFailure(Throwable arg0, String content) {
			super.onFailure(arg0, content);
			Toast.makeText(ShipDataSource.this, content,
					Toast.LENGTH_SHORT).show();
		}
		@Override
		public void onStart(){
			dialog = dialogUtil.loading("通知", "数据保存中，请稍候...");
		}
		@Override
		public void onSuccess(String res) {
			super.onSuccess(res);
			ConvertData cd = new ConvertData(res);
			dialog.dismiss();
			if(cd.getReturnCode().equalsIgnoreCase("Success")){
				//接收返回的orderId

				Toast.makeText(ShipDataSource.this, "保存成功", Toast.LENGTH_SHORT).show();

				//跳转到货物
//				Intent intent = new Intent(ShipDataSource.this,AddOrderGSHActivity.class);
//				Bundle bundle = new Bundle();
//				//bundle.putString("orderId", orderId);//合同ID
//				intent.putExtras(bundle);
//				startActivity(intent);
				finish();

			}else{
				Toast.makeText(ShipDataSource.this, cd.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}

	};


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());
		dataLoader = new Data_loader();
		imgLoader = new Image_loader(this, (GlobalApplication) getApplication());


		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		shipId=(String)bundle.getString("shipId");
		mmsi=(String)bundle.getString("mmsi");
		setContentView(R.layout.eyd_ship_data_source);
		smpc = ShipMonitorPlantCode.baochuanwang;
		
		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		ais = (RadioButton) findViewById(R.id.AIS);
		shipMP = (RadioButton) findViewById(R.id.ShipMP);
		saveBtn = (Button)findViewById(R.id.buttonSave);
		mRadioGroup.setOnCheckedChangeListener(mChangeRadio);
		saveBtn.setOnClickListener(this);
	}
	private RadioGroup.OnCheckedChangeListener mChangeRadio = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId == ais.getId()) {
				smpc = ShipMonitorPlantCode.baochuanwang;
			} else if (checkedId == shipMP.getId()) {
				smpc = ShipMonitorPlantCode.shipmanagerplant;
			}
		}
	};
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("修改数据来源"); 
		loadDate();
	}

	protected synchronized void loadDate() {
		final Map<String,Object> apiParams = new HashMap<String, Object>();
		apiParams.put("mmsi",mmsi);
		AsyncHttpResponseHandler showHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				Toast.makeText(ShipDataSource.this, content,
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onStart(){
				dialog = dialogUtil.loading("通知", "数据读取中，请稍候...");
			}
			@Override
			public void onSuccess(String res) {
				super.onSuccess(res);
				ConvertData cd = new ConvertData(res,"/mobile/ship/myShip/getPlant",apiParams);
				HashMap<String, Object> var = (HashMap<String, Object>)cd.getContent();

				smpc = ShipMonitorPlantCode.valueOf((String)var.get("plantName"));
				if (smpc.equals(ShipMonitorPlantCode.baochuanwang)) {
					shipMP.setChecked(false);
					ais.setChecked(true);
				} else {
					shipMP.setChecked(true);
					ais.setChecked(false);
				}
				dialog.dismiss();
			}

		};
		//调用获取合同接口
		dataLoader.getApiResult(showHandler,"/mobile/ship/myShip/getPlant", apiParams,"get");
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonSave){
			Map<String,Object> apiParams = new HashMap<String, Object>();
			apiParams.put("shipId", Long.parseLong(shipId));
			apiParams.put("plantName", smpc);
			//调用船舶接口
			dataLoader.getApiResult(resultHandler,"/mobile/ship/myShip/setPlant", apiParams,"post");
		}

	}

}
