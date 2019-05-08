package com.eyunda.third.activities.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.eyunda.main.CommonActivity;
import com.eyunda.third.activities.ship.DynamicActivity;
import com.eyunda.third.activities.ship.MyshipActivity;
import com.eyunda.third.activities.ship.ShipMoniterActivity;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.enumeric.CollectCode;
import com.eyunda.third.domain.location.ShipCooordData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.DateUtils;
import com.eyunda.tools.MapConvertUtil;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 船舶分布
 * 
 * @author guoqiang
 *
 */
public class ShipDistributeActivity extends CommonActivity implements
		OnClickListener, OnMarkerClickListener, OnMapClickListener {
	Data_loader dataLoader;

	String userId;
	MapView mapHistory;
	BaiduMap bmHistory;
	UiSettings uisHistory;
	public static final LatLng BEIJING_LATLNG = new LatLng(23.038006,
			113.509988);
	public static final String XML = "XML";
	LinearLayout shipAgent, shipDyn, shipMoniter;
	List<Marker> lms;
	Timer timer;
	Map<Marker,String> curMarkers;
	List<ShipCooordData> shipCooordDatas;
	boolean center = true;//第一次设置中心，以后不设置
	String searchName = "";
	Integer pageNo = 1;
	CollectCode collectCode = CollectCode.mySelfShips;
	
	DistributeTimer task;
	List<OverlayOptions> optionsList;
	Map<LatLng,ShipCooordData> shipMap;
	private LayoutInflater mInflater;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.eyd_ship_distribute);
		dataLoader = new Data_loader();
		optionsList = new ArrayList<OverlayOptions>();
		shipMap = new HashMap<LatLng, ShipCooordData>();
		initView();
		Intent intent = getIntent();
		userId = intent.getStringExtra("id");
		searchName = intent.getStringExtra("searchName");
		collectCode = CollectCode.valueOf(intent.getStringExtra("collectCode"));
		String rolecode = intent.getStringExtra("role");
		pageNo = intent.getIntExtra("pageNo",1);
		this.initMap();
	}
	class DistributeTimer extends TimerTask{

		@Override
		public void run() {
			loadDataOneMin();
		}
	};
	private void initView() {
		shipAgent = (LinearLayout) findViewById(R.id.shipAgent);
		shipDyn = (LinearLayout) findViewById(R.id.shipDyn);
		shipMoniter = (LinearLayout) findViewById(R.id.shipMoniter);
		shipAgent.setOnClickListener(this);
		shipDyn.setOnClickListener(this);
		shipMoniter.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();

		initData();
	}
	private void initData(){
		lms = new ArrayList<Marker>();
		shipCooordDatas = new ArrayList<ShipCooordData>();
		curMarkers = new HashMap<Marker, String>();
		
		if (timer != null) {
			if (task != null) {
				task.cancel(); // 将原任务从队列中移除
			}
		}else{
			timer = new Timer(true);
		}
		task = new DistributeTimer();
		timer.schedule(task,0,5*60*1000); 
	}

	private void initMap() {
		mapHistory = (MapView) findViewById(R.id.mapHistory);
		bmHistory = mapHistory.getMap();
		this.uisHistory = this.bmHistory.getUiSettings();
		this.uisHistory.setOverlookingGesturesEnabled(false);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(BEIJING_LATLNG);
		this.bmHistory.setMapStatus(msu);
		float z = 13;//
		msu = MapStatusUpdateFactory.zoomTo(z);
		this.bmHistory.setMapStatus(msu);
		this.bmHistory.setOnMarkerClickListener(this);
		this.bmHistory.setOnMapClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.shipAgent:
			intent = new Intent(ShipDistributeActivity.this,
					MyshipActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.shipDyn:
			intent = new Intent(ShipDistributeActivity.this,
					DynamicActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.shipMoniter:
			intent = new Intent(ShipDistributeActivity.this,
					ShipMoniterActivity.class);
			startActivity(intent);
			finish();
			break;
		}

	}

	@Override
	public void onMapClick(LatLng arg0) {
		bmHistory.hideInfoWindow();
	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		return false;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
    	ShipCooordData curShip = shipMap.get( arg0.getPosition());
    	//创建InfoWindow展示的view  
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewCache =  mInflater.inflate(R.layout.custom_popup_window, null);
        TextView tv_name = (TextView) viewCache.findViewById(R.id.tv_name);
        tv_name.setText("船名:"+curShip.getShipName());
        TextView tv_mmsi = (TextView) viewCache.findViewById(R.id.tv_mmsi);
        tv_mmsi.setText("MMSI："+curShip.getMmsi());
        TextView tv_lat = (TextView) viewCache.findViewById(R.id.tv_lat);
        tv_lat.setText("经度："+curShip.getLongitude());
        TextView tv_lng = (TextView) viewCache.findViewById(R.id.tv_lng);
        tv_lng.setText("纬度："+curShip.getLatitude());
        TextView tv_speed = (TextView) viewCache.findViewById(R.id.tv_speed);
        tv_speed.setText("航速："+curShip.getSpeed().toString());
        TextView tv_direct = (TextView) viewCache.findViewById(R.id.tv_direct);
        tv_direct.setText("航向："+curShip.getCourse().toString());
        TextView tv_time= (TextView) viewCache.findViewById(R.id.tv_time);
        tv_time.setText("时间："+DateUtils.getTime("yyyy-MM-dd HH:mm:ss"));
    	//定义用于显示该InfoWindow的坐标点  
    	LatLng pt = arg0.getPosition();  
    	//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量 
    	InfoWindow mInfoWindow = new InfoWindow(viewCache, pt, -100);  
    	//显示InfoWindow  
    	bmHistory.showInfoWindow(mInfoWindow);
		return false;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				showAllOverlay();
			}
			super.handleMessage(msg);
		};
	};

	

	protected synchronized void loadDataOneMin() {
		Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("id", userId);
		apiParams.put("searchName", searchName);
		apiParams.put("pageNo", pageNo);
		apiParams.put("selectCode", collectCode);
		
		dataLoader.getApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg2) {
				super.onSuccess(arg2);
				ConvertData cd = new ConvertData(arg2);
				if (cd.getReturnCode().equalsIgnoreCase("success")) {
					shipCooordDatas.clear();
					Map<String, Object> var = (HashMap<String, Object>) cd.getContent();
					List<Map<String, Object>> pds = (ArrayList<Map<String, Object>>) var.get("shipPositions");
					if (pds.size() > 0) {
						for (Map<String, Object> pd : pds) {
							ShipCooordData cpd = new ShipCooordData(pd);
							shipCooordDatas.add(cpd);
						}	
					}
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
				} else {
					Toast.makeText(getApplicationContext(), cd.getMessage(),
							Toast.LENGTH_LONG).show();
				}
			}
		}, "/mobile/monitor/shipDistributoin", apiParams, "get");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		timer.cancel();
		timer.purge();
		timer = null;
	}
//	@Override
//	protected void onStop() {
//		super.onStop();
//		timer.cancel();
//		timer.purge();
//	}
//	@Override
//	protected void onPause() {
//		super.onPause();
//		timer.cancel();
//		timer.purge();
//	}
//	@Override
//	protected void onRestart() {
//		super.onRestart();
//		initData();
//	}
//	@Override
//	protected void onResume() {
//		super.onResume();
//		initData();
//	}
    protected void showAllOverlay() {
    	
    	if(shipCooordDatas.size()>0){
    		bmHistory.clear();
    		optionsList.clear();
    		for(ShipCooordData pd:shipCooordDatas){
    			//LatLng point =  MapConvertUtil.convertFromGPS(new LatLng(Double.parseDouble(pd.getLatitude()),Double.parseDouble(pd.getLongitude())));
    			LatLng point = new LatLng(pd.getLatitude(),pd.getLongitude());
	    		//LatLng textPoint = point;
	    		
	    		float cou = (float) (0.0F - pd.getCourse());
	    		//TODO 这里根据不同的船舶状态换不同的marker
	    		BitmapDescriptor bitmap = BitmapDescriptorFactory
	    				.fromResource(R.drawable.icon_marker);
	    		// 构建MarkerOption，用于在地图上添加Marker
	    		String name  =pd.getShipName();
	    		if(name == null || name.equals(""))name = "暂无船名";
	    		OverlayOptions option = new MarkerOptions().position(point)
	    				.icon(bitmap).rotate(cou);
	    		OverlayOptions textOption = new TextOptions()  
	    	    .bgColor(0xAAFFFF00)  
	    	    .fontSize(48)  
	    	    .fontColor(0xFFFF00FF) 
	    	    .rotate(cou)
	    	    .text(name) 
	    	    .position(point); 
	    		shipMap.put(point, pd);
	    		optionsList.add(option);
	    		optionsList.add(textOption);
    		}
    	}
    	if(optionsList.size() > 0){
	    	ShipOverlayManager overlayManager = new ShipOverlayManager(bmHistory);
	    	overlayManager.removeFromMap();
	        //bmHistory.setOnMarkerClickListener(overlayManager);
	        overlayManager.setData(optionsList);
	        overlayManager.addToMap();
	        if(center){
	        	overlayManager.zoomToSpan();
	        	center = false;
	        }
    	}
    }

}
