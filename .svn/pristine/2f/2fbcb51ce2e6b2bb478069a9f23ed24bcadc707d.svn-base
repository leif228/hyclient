package com.eyunda.third.activities.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.eyunda.main.CommonActivity;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.ship.DynamicActivity;
import com.eyunda.third.activities.ship.MyshipActivity;
import com.eyunda.third.activities.ship.ShipMoniterActivity;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.location.ShipCooordData;
import com.eyunda.third.domain.ship.ShipMonitorPlantCode;
import com.eyunda.third.domain.ship.ShipStopData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.MapConvertUtil;
import com.eyunda.tools.log.Log;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 船舶动态
 * 
 * @author guoqiang
 *
 */
public class ShipDynamicActivity extends CommonActivity implements
		OnClickListener, OnMarkerClickListener, OnMapClickListener {
	Data_loader dataLoader;

	String shipId,mmsi,startTime,endTime,shipArvlftId,shipName,startPort,endPort;
	MapView mapHistory;
	BaiduMap bmHistory;
	UiSettings uisHistory;
	public static final LatLng BEIJING_LATLNG = new LatLng(23.038006,
			113.509988);
	LinearLayout shipAgent, shipDyn, shipMoniter, showPic;
	TextView shipNameText, shipTime, shipArea;
	ImageView shipPics;
	ShipCooordData lastPoint,tmpPoint;
	Timer timer;
	MyTimerTask mTimerTask;
	List<ShipCooordData> positionDatas;
	Marker marker;
	TextOptions historyTextOption = null;
	int lastPointIndex;
	boolean setCenterFlag = true;
	
	private ShipStopData shipArvlftData;
	String picString = "";//保存上一张图片
	String picStringBlue = "";//设置设置蓝屏图片
	int z = 13;//
	private ImageLoader mImageLoader;
	private LinearLayout shipPicsContain;
	private ShipMonitorPlantCode smpc;//数据来源
	private Boolean loadDataFinished = false;
    private Boolean scal = false;
    private Point pt;
	//private   船舶数据来源
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.eyd_ship_dyn_map);
		dataLoader = new Data_loader();
		smpc = ShipMonitorPlantCode.shipmanagerplant;
		mImageLoader = ImageLoader.getInstance();
		Intent intent = getIntent();
		shipId = intent.getStringExtra("id");
		shipArvlftId = intent.getStringExtra("shipArvlftId");
		mmsi = intent.getStringExtra("mmsi");
		startTime = intent.getStringExtra("startTime");
		endTime = intent.getStringExtra("endTime");
		shipName = intent.getStringExtra("shipName");
		startPort = intent.getStringExtra("startPort");
		endPort = intent.getStringExtra("endPort");
		pt = new Point();
		initView();
		this.initMap();
		timer = new Timer(true);
	}

	private void initView() {
        WindowManager manager = (WindowManager) this  
                .getSystemService(Context.WINDOW_SERVICE);  
        Display display = manager.getDefaultDisplay();  
        display.getSize(pt);  
		shipAgent = (LinearLayout) findViewById(R.id.shipAgent);
		shipDyn = (LinearLayout) findViewById(R.id.shipDyn);
		shipMoniter = (LinearLayout) findViewById(R.id.shipMoniter);
		showPic = (LinearLayout) findViewById(R.id.showPic);
		shipAgent.setOnClickListener(this);
		shipDyn.setOnClickListener(this);
		shipMoniter.setOnClickListener(this);
		shipNameText = (TextView) findViewById(R.id.shipName);
		shipTime = (TextView) findViewById(R.id.shipTime);
		shipArea = (TextView) findViewById(R.id.shipArea);
		shipNameText.setText(shipName);
		shipPics = (ImageView)findViewById(R.id.shipPics);
		showPic.setOnClickListener(this);
		shipPicsContain = (LinearLayout)findViewById(R.id.shipPicsContain);
		
		LayoutParams ps = shipPics.getLayoutParams();
		ps.width = pt.x/3;
		ps.height = ps.width;
		shipPics.setLayoutParams(ps);
	}

	@Override
	protected void onStart() {
		super.onStart();
		makeText();
		initData();
	}

	private void initData(){
		positionDatas = new ArrayList<ShipCooordData>();
		
		loadData();
		
		if (timer != null) {
			if (mTimerTask != null) {
				mTimerTask.cancel(); // 将原任务从队列中移除
			}
		}else{
			timer = new Timer(true);
		}
		mTimerTask = new MyTimerTask();
		timer.schedule(mTimerTask, 2000, 100);
		
	}
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {// 初始化数据完成
				drawLines();
				loadDataFinished = true;
			} else if (msg.what == 2) {// 读取新坐标完成
				lastPointIndex++;
				if (lastPointIndex < positionDatas.size()) {
					lastPoint = positionDatas.get(lastPointIndex);
					moveMarker(lastPoint);
					setCenter(lastPoint, z);
				}
			}
			super.handleMessage(msg);
		}

	};


	private void makeText() {
		shipArea.setText(startPort+" 到 "+ endPort);
		shipTime.setText(startTime+"-"+endTime);
	}
	private void drawLines() {
		if (positionDatas.size() >= 2) {
			List<LatLng> points = new ArrayList<LatLng>();
			lastPoint = positionDatas.get(0);
//			LatLng first = MapConvertUtil.convertFromGPS( new LatLng(Double.parseDouble(lastPoint.getLatitude()),
//					Double.parseDouble(lastPoint.getLongitude())));
			LatLng first = new LatLng(lastPoint.getLatitude(),lastPoint.getLongitude());
			points.add(first);
			for(int i=1; i<positionDatas.size();i++){
//				LatLng start =  MapConvertUtil.convertFromGPS(new LatLng(Double.parseDouble(positionDatas.get(
//						i).getLatitude()), Double.parseDouble(positionDatas.get(i)
//						.getLongitude())));
				LatLng start = new LatLng( positionDatas.get(i).getLatitude(),positionDatas.get(i).getLongitude());
				points.add(start);
			}
			if (points.size() > 1) {
				OverlayOptions ooPolyline = new PolylineOptions().width(10)
						.zIndex(20).color(0xAAFF0000).points(points);
				bmHistory.addOverlay(ooPolyline);
			}
			lastPointIndex = 0;
		}
	};

	private synchronized void moveMarker(final ShipCooordData toPoint) {
		if(smpc.equals(ShipMonitorPlantCode.shipmanagerplant)){
			//异步下载并显示图片
			Map<String, Object> apiParams = new HashMap<String, Object>();
			apiParams.put("mmsi", mmsi);
			apiParams.put("time", toPoint.getPosTime());
			apiParams.put("cameraNo","1");
			dataLoader.getApiResult(new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String arg2) {
					super.onSuccess(arg2);
					ConvertData cd = new ConvertData(arg2);
					if (cd.getReturnCode().equalsIgnoreCase("success")) {
						Map<String, Object> var = (HashMap<String, Object>) cd.getContent();
						String pic = (String) var.get("shipPic");
						//保存图片到临时变量
						tmpPoint = toPoint;
						picString = ApplicationConstants.IMAGE_URL+pic;
						//imgLoader.load_horizontal_Img(picString, shipPics);
						mImageLoader.displayImage(picString,shipPics, GlobalApplication.displayImageOptions);
					}else{
						
						//计算两点间隔时间是否大于五分钟
						if(tmpPoint !=null && calcTimeRect(toPoint,tmpPoint,5)){
							mImageLoader.displayImage(picStringBlue,shipPics, GlobalApplication.displayImageOptions);
						}else{
							//imgLoader.load_horizontal_Img(picString, shipPics);
							mImageLoader.displayImage(picString,shipPics, GlobalApplication.displayImageOptions);
						}
					}
					
					removeMarker();
					addMarker(toPoint);
					Log.i("----"+toPoint.getJsonString());
				}
				@Override
				public void onFailure(Throwable arg0, String content) {
					super.onFailure(arg0, content);
					//计算两点间隔时间是否大于五分钟
					if(tmpPoint !=null && calcTimeRect(toPoint,tmpPoint,5)){
						mImageLoader.displayImage(picStringBlue,shipPics, GlobalApplication.displayImageOptions);
					}else{
						mImageLoader.displayImage(picString,shipPics, GlobalApplication.displayImageOptions);
					}
					removeMarker();
					addMarker(toPoint);
					
					
				}
			}, "/mobile/monitor/getPic", apiParams, "get");
		}else{
			
			removeMarker();
			addMarker(toPoint);
		}	
	}
	/**
	 * 计算两点间时间间隔
	 * @param toPoint
	 * @param fromPoint
	 * @param minite
	 * @return
	 */
	private Boolean calcTimeRect(ShipCooordData toPoint,ShipCooordData fromPoint, int minute){
		return false;
	}
	private void addMarker(ShipCooordData toPoint) {
		// 定义Maker坐标点
		// 构建Marker图标
//		LatLng point = MapConvertUtil.convertFromGPS( new LatLng(Double.parseDouble(toPoint.getLatitude()),
//				Double.parseDouble(toPoint.getLongitude())));
		LatLng point = new LatLng(toPoint.getLatitude(), toPoint.getLongitude());
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_marker_2);
		// 构建MarkerOption，用于在地图上添加Marker
		float cou = (float) (0.0F-toPoint.getCourse()  );
		OverlayOptions option = new MarkerOptions().position(point)
				.icon(bitmap).rotate(cou);
		marker = (Marker) (this.bmHistory.addOverlay(option));
		option = null;
		bitmap = null;
	}

	private void removeMarker() {
		if (marker != null)
			marker.remove();
	}



	private void initMap() {
		mapHistory = (MapView) findViewById(R.id.mapHistory);
		bmHistory = mapHistory.getMap();
		this.uisHistory = this.bmHistory.getUiSettings();
		this.uisHistory.setOverlookingGesturesEnabled(false);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(BEIJING_LATLNG);
		this.bmHistory.setMapStatus(msu);
		
		msu = MapStatusUpdateFactory.zoomTo(z);
		this.bmHistory.setMapStatus(msu);
		this.bmHistory.setOnMarkerClickListener(this);
		this.bmHistory.setOnMapClickListener(this);
	}

	// 初始化坐标数据
	protected synchronized void loadData() {
		Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("mmsi", mmsi);
		apiParams.put("shipId", shipId);
		apiParams.put("shipArvlftId", shipArvlftId);
		apiParams.put("startTime",startTime );
		apiParams.put("endTime",endTime );
		dataLoader.getApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg2) {
				super.onSuccess(arg2);
				ConvertData cd = new ConvertData(arg2);
				if (cd.getReturnCode().equalsIgnoreCase("success")) {
					Map<String, Object> var = (HashMap<String, Object>) cd.getContent();
					List<Map<String, Object>> pds = (ArrayList<Map<String, Object>>) var.get("shipPositions");
					smpc = ShipMonitorPlantCode.valueOf((String) var.get("smpc"));
					if(smpc.equals(ShipMonitorPlantCode.shipmanagerplant)){
						shipPicsContain.setVisibility(View.VISIBLE);
					}else{
						shipPicsContain.setVisibility(View.GONE);
					}
					//shipArvlftData =  new ShipStopData((HashMap<String, Object>)var.get("shipArvlftData"));
					if (pds.size() > 0) {
						for (Map<String, Object> pd : pds) {
							ShipCooordData cpd = new ShipCooordData(pd);
							positionDatas.add(cpd);
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
		}, "/mobile/monitor/historyRoutePlay", apiParams, "get");
	}

	public void setCenter(ShipCooordData pd, int zoom) {
		double lat = pd.getLatitude();
		double lng = pd.getLongitude();
		if(setCenterFlag){
			setCenter(lat, lng, zoom);
			setCenterFlag = false;
		}
	}

	public void setCenter(double lat, double lng, int zoom) {
		// 设定中心点坐标
		LatLng cenpt =  MapConvertUtil.convertFromGPS(new LatLng(lat, lng));
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(zoom)
				.build();

		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		// 改变地图状态
		this.bmHistory.animateMapStatus(mMapStatusUpdate);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.shipAgent:
			intent = new Intent(ShipDynamicActivity.this, MyshipActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.shipDyn:
			intent = new Intent(ShipDynamicActivity.this, DynamicActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.shipMoniter:
			intent = new Intent(ShipDynamicActivity.this,
					ShipMoniterActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.showPic:
			toggleImage();
		}

	}

	@Override
	public void onMapClick(LatLng arg0) {
		//showPic.setVisibility(View.GONE);
	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		return false;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		//showPic.setVisibility(View.VISIBLE);
		return false;
	}

	// 读取最新的坐标信息

	protected synchronized void loadDataOneMin() {
		Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("id", shipId);
		dataLoader.getApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg2) {
				super.onSuccess(arg2);
				ConvertData cd = new ConvertData(arg2);
				if (cd.getReturnCode().equalsIgnoreCase("success")) {

					Map<String, Object> var = (HashMap<String, Object>) cd.getContent();
					List<Map<String, Object>> pds = (ArrayList<Map<String, Object>>) var.get("shipPositions");
					if (pds.size() > 0) {
						for (Map<String, Object> pd : pds) {
							ShipCooordData cpd = new ShipCooordData(pd);
							positionDatas.add(cpd);
						}
					}
					Message message = new Message();
					message.what = 2;
					handler.sendMessage(message);
				} else {
					Toast.makeText(getApplicationContext(), cd.getMessage(),
							Toast.LENGTH_LONG).show();
				}
			}
		}, "/mobile/monitor/historyRoutePlay", apiParams, "get");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		timer.cancel();
		timer.purge();
		timer = null;
	}
//
//	@Override
//	protected void onStop() {
//		super.onStop();
//		timer.cancel();
//		timer.purge();
//	}
//	@Override
//	protected void onPause() {
//		super.onPause();
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
	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			if(loadDataFinished){
				Message message = new Message();
				message.what = 2;
				handler.sendMessage(message);
			}
		}
	}
	private void toggleImage(){
		LayoutParams ps = shipPics.getLayoutParams();

		if(scal == false){
			ps.width = pt.x-30;
			ps.height = ps.width;
			scal = true;
		}else{
			scal = false;
			ps.width = pt.x/3;
			ps.height = ps.width;
		}
		shipPics.setLayoutParams(ps);
	}
}
