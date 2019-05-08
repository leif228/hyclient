package com.hangyi.zd.activity;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.domain.location.ShipCooordData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.tools.ParseJson;
import com.hangyi.zd.domain.NodeCode;
import com.hangyi.zd.domain.ShipInfoData;
import com.hangyi.zd.domain.ShipVoyageData;
import com.hangyi.zd.domain.ShipVoyageNodeData;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ShipInfoActivity extends CommonListActivity {
	TextView dw1, dw2, dw5, sb1, sb2, gk1, gk2, gk3, gk4, gk5, gk6, gk7, gk8, gk9, gk10,zz1,zz2,zz3,zz4;
	private ShipCooordData shipCooordData;
	private TextView dw3;
	private TextView dw4;
	private TextView sb3;
	private TextView sb4;
	Data_loader dataLoader;
	ShipInfoData shipInfoData;
	String shipId = "";
	String currState = "";
	String startPort = "";
	String endPort = "";
	String zzs = "0";
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				setview();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_ship_information);
		
		dataLoader = new Data_loader();
		shipCooordData = (ShipCooordData) getIntent().getSerializableExtra("shipData");
		if(shipCooordData!=null)
			shipId = shipCooordData.getShipID();
		initview();
		loadDate();
		loadDate2();
		
	}

	private void initview() {
		dw1 = (TextView) findViewById(R.id.dw1);
		dw2 = (TextView) findViewById(R.id.dw2);
		dw3 = (TextView) findViewById(R.id.dw3);
		dw4 = (TextView) findViewById(R.id.dw4);
		dw5 = (TextView) findViewById(R.id.dw5);
		sb1 = (TextView) findViewById(R.id.sb1);
		sb2 = (TextView) findViewById(R.id.sb2);
		sb3 = (TextView) findViewById(R.id.sb3);
		sb4 = (TextView) findViewById(R.id.sb4);
		gk1 = (TextView) findViewById(R.id.gk1);
		gk2 = (TextView) findViewById(R.id.gk2);
		gk3 = (TextView) findViewById(R.id.gk3);
		gk4 = (TextView) findViewById(R.id.gk4);
		gk5 = (TextView) findViewById(R.id.gk5);
		gk6 = (TextView) findViewById(R.id.gk6);
		gk7 = (TextView) findViewById(R.id.gk7);
		gk8 = (TextView) findViewById(R.id.gk8);
		gk9 = (TextView) findViewById(R.id.gk9);
		gk10 = (TextView) findViewById(R.id.gk10);
		zz1 = (TextView) findViewById(R.id.zz1);
		zz2 = (TextView) findViewById(R.id.zz2);
		zz3 = (TextView) findViewById(R.id.zz3);
		zz4 = (TextView) findViewById(R.id.zz4);
		
		gk10.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(shipInfoData!=null&&shipInfoData.getTel().length()>0){
					
					Intent phoneIntent = new Intent(Intent.ACTION_CALL,
							Uri.parse("tel:" + shipInfoData.getTel()));
					startActivity(phoneIntent);
				}
			}
		});
	}

	private void setview() {
		
		if(shipCooordData!=null){
			dw1.setText("经度："+shipCooordData.getGpsLongitude());
			dw2.setText("纬度："+shipCooordData.getGpsLatitude());
			dw3.setText("航速："+shipCooordData.getGpsSpeed()+"节");
			dw4.setText("航向："+shipCooordData.getGpsCourse());
			dw5.setText("卫星时间："+shipCooordData.getGpsTime());
			
			sb1.setText("主电源电压："+shipCooordData.getMainVoltage());
			sb2.setText("备电源电压："+shipCooordData.getBackUpVoltage());
			sb3.setText("设备温度："+shipCooordData.getTemperature());
			sb4.setText("充电状态："+shipCooordData.getPowerManagementState());
			
		}
		if(shipInfoData!=null){
			gk1.setText("船长："+shipInfoData.getLength()+"米");
			gk2.setText("船宽："+shipInfoData.getBreadth()+"米");
			gk3.setText("型深："+shipInfoData.getDraught()+"米");
			gk4.setText("A级载重："+shipInfoData.getClassA()+"吨");
			gk5.setText("B级载重："+shipInfoData.getClassB()+"吨");
			gk6.setText("驾驶方式："+shipInfoData.getDriveStyle());
			gk7.setText("船型："+shipInfoData.getShipType());
			gk8.setText("船舶所属公司："+shipInfoData.getShipOwner());
			gk9.setText("联系人："+shipInfoData.getContact());
			gk10.setText("电话："+shipInfoData.getTel());
		}
		
		if(true){
			zz1.setText("当前状态："+currState);
			zz2.setText("起运港："+startPort);
			zz3.setText("到达港："+endPort);
			zz4.setText("装载量："+zzs+"吨");
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(shipCooordData!=null)
			setTitle(shipCooordData.getShipName());
		else
			setTitle("船舶详细信息");

	};

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {
		Map<String, Object> apiParams = new HashMap<String, Object>();
		
		dataLoader.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("加载中", "请稍候...");
			}
			@Override
			public void onSuccess(String arg2) {
				super.onSuccess(arg2);
				
				shipInfoData = ParseJson.parserShipInfo(arg2);
				
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
				
				dialog.dismiss();
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipInfoActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipInfoActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(ShipInfoActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipInfoActivity.this, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.shipInfo+shipId, apiParams, "get");

	}
	protected void loadDate2() {

		String PHPSESSID = "";
        List<Cookie> list = GlobalApplication.getInstance().getCookies();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("PHPSESSID")) {
            	PHPSESSID = cookie.getValue();
            }
        }
		Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("shipId", shipId);
		
		dataLoader.getZd_JavaManageResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg2) {
				
				if(arg2==null||"".equals(arg2)){
//					Toast.makeText(ShipHCListIngActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();
					return;
				}
				
				Gson gson = new Gson();
				final HashMap<String, Object> rmap = gson.fromJson(
						arg2, new TypeToken<Map<String, Object>>() {
						}.getType());
				if (rmap.get("returnCode").equals("Success")) {
					
					List<Map<String, Object>> map = (List<Map<String, Object>>) rmap.get("shipVoyageDatas");
					if(!map.isEmpty()){
						ShipVoyageData s = new ShipVoyageData(map.get(0));
						
						List<ShipVoyageNodeData> list = s.getNodes();
						if(!list.isEmpty()){
							Collections.sort(list, new Comparator<ShipVoyageNodeData>() {
								@Override
								public int compare(ShipVoyageNodeData o1, ShipVoyageNodeData o2) {
									return Integer.valueOf(o1.getStage())
											.compareTo(Integer.valueOf(o2.getStage()));
								}
							});
							boolean isSetedLastData = false;
							for(int i=list.size()-1;i>=0;i--){
								ShipVoyageNodeData iData = list.get(i);
								if(Integer.valueOf(iData.getStage())>NodeCode.unloadingEnd.getN())
									continue;
								if(Integer.valueOf(iData.getStage())==NodeCode.unloadingEnd.getN()){
									currState = NodeCode.unloadingEnd.getDescription();
									zzs = "0";
									break;
								}else{
									if(!isSetedLastData){
										NodeCode code = NodeCode.getByN(Integer.valueOf(iData.getStage()));
										if(code!=null){
											currState = code.getDescription();
											isSetedLastData = true;
										}
									}
									if(String.valueOf(NodeCode.loadingEnd.getN()).equals(iData.getStage())){
										zzs = iData.getValue();
										break;
									}
								}
								
							}
						}
						startPort = s.getStartPort();
						endPort = s.getEndPort();
						
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
					
				} else {
//					Toast.makeText(ShipHCListIngActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();
				}
				
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

			}
		}, ApplicationUrls.currHCByShipId+PHPSESSID, apiParams, "get");	
		
		
	
		
	}
}
