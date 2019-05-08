package com.eyunda.third.activities.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.activities.ship.widget.DatePickerFragment;
import com.eyunda.third.adapters.CommonAdapter;
import com.eyunda.third.adapters.ViewHolder;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.enumeric.ReleaseStatusCode;
import com.eyunda.third.domain.ship.ShipCabinData;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.AreaSelect;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 船舶接货
 *
 */
public class ShipCabinActivity extends CommonActivity implements OnClickListener{
	Data_loader dataLoader = new Data_loader();
	private Long cabinId;
	private ShipCabinData currentData;
	
	private Spinner selectShipSpinner;
	private TextView etaTime;
	private EditText remark;
	private TextView endPortView;
	private Button btnSave;
	
	private Long selectShipId;
	private String selectPortNo;
	private List<SpinnerItem> shipDatas;
	private CommonAdapter<SpinnerItem> shipAdapter;
	
	private RelativeLayout container;
	RadioGroup pubStatus;
	RadioButton privateCabin;
	RadioButton publicCabin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_carbin);
		cabinId = getIntent().getLongExtra("cabinId", 0L);
		shipDatas = new ArrayList<SpinnerItem>();
		initViews();
		getDatas();

	}

	private void initViews() {
		
		container = (RelativeLayout)findViewById(R.id.cabin_Layout);
		selectShipSpinner = (Spinner)findViewById(R.id.selectShip);
		etaTime = (TextView)findViewById(R.id.etaTime);
		etaTime.setOnClickListener(this);
		remark = (EditText)findViewById(R.id.remark);
		endPortView = (TextView)findViewById(R.id.endPort);
		endPortView.setOnClickListener(this);
		pubStatus = (RadioGroup)findViewById(R.id.pubStatus);
		privateCabin = (RadioButton)findViewById(R.id.privateCabin);
		publicCabin = (RadioButton)findViewById(R.id.publicCabin);

		
		btnSave = (Button)findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);
		shipAdapter = new CommonAdapter<SpinnerItem>(getApplicationContext(),shipDatas,R.layout.eyd_popup_item) {
			
			@Override
			public void convert(ViewHolder helper, SpinnerItem item) {
				
			}
		};
		selectShipSpinner.setAdapter(shipAdapter);
	}

	private void getDatas() {
		final Map<String, Object> params = new HashMap<String, Object>();

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			public void onStart() {
				dialog = dialogUtil.loading("通知", "数据获取中，请稍后...");
			};

			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0, "/mobile/cabin/getMyCabin", params);
				if (cd.getReturnCode().equals("Success")) {
					HashMap<String, Object> content = (HashMap<String, Object>) cd.getContent();
					currentData = new ShipCabinData((Map<String,Object>)content.get("ShipCabinData"));
					List<Map<String, Object>> ships = (ArrayList<Map<String,Object>>)content.get("shipDatas");
					for(Map<String, Object> ship: ships){
						ShipData sd = new ShipData(ship);
						SpinnerItem si = new SpinnerItem();
						si.setId(sd.getId().toString());
						si.setValue(sd.getShipName()+"-MMSI:"+sd.getMmsi());
						shipDatas.add(si);
					}
					setViews();	
				}

			};

			public void onFailure(Throwable arg0, String arg1) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), arg1, Toast.LENGTH_SHORT).show();

			};
		};

		params.put("id", cabinId);
		dataLoader.getApiResult(handler, "/mobile/cabin/getMyCabin", params, "get");

	}

	
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("船舶上报");
		
	};

	private void setViews() {
		//TODO:设置船舶spinner信息
		if(shipDatas.size() > 0){
			for(int i=0; i<shipDatas.size(); i++){
				SpinnerItem si = shipDatas.get(i);
				if(si.getId().equals(currentData.getShipData().getId().toString())){
					selectShipSpinner.setSelection(i);
				}
			}
		}else{
			selectShipSpinner.setSelection(0);
		}
		shipAdapter.notifyDataSetChanged();
		etaTime.setText(currentData.getArrivePortTime());
		endPortView.setText(currentData.getPortData().getFullName());
		endPortView.setTag(currentData.getPortData().getPortNo());
		remark.setText(currentData.getRemark());
		
		publicCabin.setText(ReleaseStatusCode.publish.getDescription());
		publicCabin.setTag(ReleaseStatusCode.publish);
		privateCabin.setText(ReleaseStatusCode.unpublish.getDescription());
		privateCabin.setTag(ReleaseStatusCode.unpublish);
		if(currentData.getStatus() == null){
			currentData.setStatus(ReleaseStatusCode.publish);
		}
		pubStatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                //获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton)ShipCabinActivity.this.findViewById(radioButtonId);
                currentData.setStatus((ReleaseStatusCode)rb.getTag());
            }
        });
		
		if(currentData.getStatus() == ReleaseStatusCode.unpublish){
			privateCabin.setChecked(true);
			publicCabin.setChecked(false);

		}else if(currentData.getStatus() == ReleaseStatusCode.publish){
			privateCabin.setChecked(false);
			publicCabin.setChecked(true);
		}else{
			privateCabin.setChecked(true);
			publicCabin.setChecked(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	
		case R.id.endPort:
			AreaSelect pointAt = new AreaSelect(this, endPortView);
//			PopupWindow popupWindow = pointAt.makePopupWindow();
			int[] xy = new int[2];
			container.getLocationOnScreen(xy);
			int localHeight = pointAt.getHeight();
//			popupWindow.showAtLocation(container, Gravity.CENTER | Gravity.BOTTOM, 0, -localHeight);
			break;
		case R.id.etaTime:
			DatePickerFragment datePicker = new DatePickerFragment(etaTime);
			datePicker.show(getFragmentManager(), "datePicker");
			break;
		case R.id.btnSave:
			saveShipcabin();
			break;
		default:
			break;
		}
		
	}

	private void saveShipcabin() {
		final Map<String, Object> params = new HashMap<String, Object>();

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			public void onStart() {
				dialog = dialogUtil.loading("通知", "数据保存中，请稍候...");
			};

			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0, "/mobile/cabin/saveMyCabin", params);
				if (cd.getReturnCode().equals("Success")) {
					Toast.makeText(null, cd.getMessage(), Toast.LENGTH_SHORT).show();
					startActivity(new Intent(getApplicationContext(),ShipCabinListActivity.class));
					finish();
				}

			};

			public void onFailure(Throwable arg0, String arg1) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), arg1, Toast.LENGTH_SHORT).show();

			};
		};
		
		String arrivePortTime = etaTime.getText().toString();
		if(arrivePortTime.isEmpty()){
			Toast.makeText(getApplicationContext(), "请选择预期到达时间", Toast.LENGTH_SHORT).show();
			return;
		}
		String remarkStr = remark.getText().toString();
		if(remarkStr.isEmpty()){
			Toast.makeText(getApplicationContext(), "请填写接货说明", Toast.LENGTH_SHORT).show();
			return;
		}
		Long shipId = Long.parseLong(((SpinnerItem)selectShipSpinner.getSelectedItem()).getId());
		//private ReleaseStatusCode status = ReleaseStatusCode.publish; // 状态
		params.put("id", currentData.getId());
		params.put("shiperId", shipId);
		params.put("arrivePortTime", arrivePortTime);
		params.put("portNo", endPortView.getTag());
		params.put("remark", remarkStr);
		params.put("status", currentData.getStatus());
		dataLoader.getApiResult(handler, "/mobile/cabin/saveMyCabin", params, "post");
		
	}


	




}
