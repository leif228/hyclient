package com.eyunda.third.activities.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.eyunda.main.CommonActivity;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.ship.DynamicActivity;
import com.eyunda.third.activities.ship.MyshipActivity;
import com.eyunda.third.activities.ship.ShipMoniterActivity;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.location.ShipCooordData;
import com.eyunda.third.domain.ship.ShipStopData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.loaders.Image_loader;
import com.eyunda.tools.CooordUtil;
import com.eyunda.tools.MapConvertUtil;
import com.eyunda.tools.log.FilePathGenerator;
import com.eyunda.tools.log.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 船舶动态
 * 
 * @author guoqiang
 *
 */
public class ShipLatestDynamicActivity extends CommonActivity implements
		OnClickListener, OnMarkerClickListener, OnMapClickListener {
	Data_loader dataLoader;
	Image_loader imgLoader;
	String shipId, mmsi, startTime="", endTime="", shipArvlftId, shipName;
	ShipStopData startPortData,endPortData;
	MapView mapHistory;
	BaiduMap bmHistory;
	UiSettings uisHistory;
	public static final LatLng BEIJING_LATLNG = new LatLng(23.038006,
			113.509988);
	LinearLayout shipAgent, shipDyn, shipMoniter, showPic;
	TextView shipNameText, shipTime, shipArea;
	ShipCooordData lastPoint;
	Timer timer;
	MyTimerTask mTimerTask;
	ArrayList<ShipCooordData> positionDatas;
	Marker marker;

	int lastPointIndex;
	boolean setCenterFlag = true;
	boolean dataInited = false;// 标志初始坐标是否已经取回
	String type;// 请求来源,1从船舶列表查看，2从合同查看

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.eyd_ship_dyn_map);
		dataLoader = new Data_loader();
		imgLoader = new Image_loader(this, (GlobalApplication) getApplication());

		Intent intent = getIntent();
		shipId = intent.getStringExtra("id");
		mmsi = intent.getStringExtra("mmsi");
		shipName = intent.getStringExtra("shipName");
		type = intent.getStringExtra("type");

		initView();
		this.initMap();
		timer = new Timer(true);
	}

	private void initView() {
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
		showPic.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();

		initData();
	}

	private void initData() {
		positionDatas = new ArrayList<ShipCooordData>();
		if (timer != null) {
			if (mTimerTask != null) {
				mTimerTask.cancel(); // 将原任务从队列中移除
			}
		} else {
			timer = new Timer(true);
		}
		mTimerTask = new MyTimerTask();

		timer.schedule(mTimerTask, 30000, 1 * 60 * 1000);
		loadData();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {// 初始化数据完成
				makeText();
				drawLines();
				dataInited = true;
				if(lastPoint != null){
					setCenter(lastPoint, 13);
				}
			} else if (msg.what == 2) {// 读取新坐标完成
				ShipCooordData toPoint = positionDatas
						.get(positionDatas.size() - 1);
				if (lastPoint.getLatitude().equals(toPoint.getLatitude())
						&& lastPoint.getLongitude().equals(
								toPoint.getLongitude())) {
					// Toast.makeText(ShipLatestDynamicActivity.this,
					// "坐标未变化:"+positionDatas.size(),
					// Toast.LENGTH_SHORT).show();
				} else {
					drawOneLine(lastPoint, toPoint);
				}
				if(lastPoint != null){
					setCenter(lastPoint, 13);
				}
			}
			super.handleMessage(msg);
		}
		private void makeText() {
			if(startPortData != null){
				shipArea.setText(startPortData.getPortData().getFullName()+" 到 "+startPortData.getGoPortData().getFullName());
			}else{
				shipArea.setVisibility(View.GONE);
			}
			if(!startTime.equals("") && !endTime.equals("") ){
				shipTime.setText(startTime+"-"+endTime);
			}else{
				shipTime.setVisibility(View.GONE);
			}
		}

	};
	
	private void drawLines() {
		if (positionDatas.size() >= 2) {
			List<LatLng> points = new ArrayList<LatLng>();
			lastPoint = positionDatas.get(0);

			LatLng first = new LatLng(lastPoint.getLatitude(), lastPoint.getLongitude());
			points.add(first);
			// 实时动态
			for (int i = 1; i < positionDatas.size(); i++) {

				LatLng start = new LatLng(positionDatas.get(i).getLatitude(),positionDatas.get(i).getLongitude());
				points.add(start);
				lastPoint = positionDatas.get(i);
			}
			if (points.size() > 1) {
				OverlayOptions ooPolyline = new PolylineOptions().width(10)
						.zIndex(20).color(0xAAFF0000).points(points);
				bmHistory.addOverlay(ooPolyline);
			}
			lastPointIndex = positionDatas.size();
			addMarker(lastPoint);
		}
	};

	private void moveMarker(ShipCooordData toPoint) {
		removeMarker();
		addMarker(toPoint);
	}

	private void addMarker(ShipCooordData toPoint) {
		// 定义Maker坐标点
		// 构建Marker图标
		LatLng point = new LatLng(toPoint.getLatitude(), toPoint.getLongitude());
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_marker_2);
		// 构建MarkerOption，用于在地图上添加Marker
		float cou = (float) (0.0F - toPoint.getCourse());
		OverlayOptions option = new MarkerOptions().position(point)
				.icon(bitmap).rotate(cou);
		// 在地图上添加Marker，并显示
		// this.bmHistory.addOverlay(option);
		marker = (Marker) (this.bmHistory.addOverlay(option));
		option = null;
		bitmap = null;
	}

	private void removeMarker() {
		if (marker != null)
			marker.remove();
	}

	private void drawOneLine(ShipCooordData fromPoint, ShipCooordData toPoint) {
		// 画一条线
		lastPoint = toPoint;
		List<LatLng> points = new ArrayList<LatLng>();
		LatLng start = new LatLng(fromPoint.getLatitude(), fromPoint.getLongitude());
		points.add(start);

		LatLng end = new LatLng(toPoint.getLatitude(), toPoint.getLongitude());
		points.add(end);
		OverlayOptions ooPolyline = new PolylineOptions().width(10)
				.color(0xAAFF0000).points(points);
		bmHistory.addOverlay(ooPolyline);
		// 更新图片
		// String headString = ApplicationConstants.SERVER_URL +
		// toPoint.getPic();
		// Log.v("pic", headString);
		// imgLoader.load_horizontal_Img(headString, picSrc);
		// showPic.setVisibility(View.VISIBLE);

		moveMarker(lastPoint);
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

	// 初始化坐标数据
	protected synchronized void loadData() {
		Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("shipId", Long.parseLong(shipId));
		apiParams.put("type", type);
		dataLoader.getApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg2) {
				super.onSuccess(arg2);
				ConvertData cd = new ConvertData(arg2);
				if (cd.getReturnCode().equalsIgnoreCase("success")) {
					Map<String, Object> var = (HashMap<String, Object>) cd.getContent();
					List<Map<String, Object>> pds = (ArrayList<Map<String, Object>>) var.get("shipPositions");

					startPortData = new ShipStopData((HashMap<String, Object>) var.get("leftData"));
					if (pds.size() > 0) {
						ShipCooordData first = new ShipCooordData(pds.get(0));
						startTime = first.getPosTime();
						ShipCooordData end = new ShipCooordData(pds.get(pds.size()-1));
						endTime = end.getPosTime();
						
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
		}, "/mobile/monitor/currRoutePlay", apiParams, "get");
	}


	public void setCenter(ShipCooordData pd, int zoom) {
		double lat = pd.getLatitude();
		double lng = pd.getLongitude();
		if (setCenterFlag) {
			setCenter(lat, lng, zoom);
			setCenterFlag = false;
		}
	}

	public void setCenter(double lat, double lng, int zoom) {
		// 设定中心点坐标
		LatLng cenpt = MapConvertUtil.convertFromGPS(new LatLng(lat, lng));

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
			intent = new Intent(ShipLatestDynamicActivity.this,
					MyshipActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.shipDyn:
			intent = new Intent(ShipLatestDynamicActivity.this,
					DynamicActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.shipMoniter:
			intent = new Intent(ShipLatestDynamicActivity.this,
					ShipMoniterActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.showPic:

		}

	}

	@Override
	public void onMapClick(LatLng arg0) {
		// showPic.setVisibility(View.GONE);
	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		return false;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// showPic.setVisibility(View.VISIBLE);
		return false;
	}

	// 读取最新的坐标信息
	protected synchronized void loadDataOneMin() {
		Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("shipId", Long.parseLong(shipId));
		apiParams.put("type", type);
		dataLoader.getApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg2) {
				super.onSuccess(arg2);
				ConvertData cd = new ConvertData(arg2);
				if (cd.getReturnCode().equalsIgnoreCase("success")) {
					Map<String, Object> var = (HashMap<String, Object>) cd
							.getContent();
					Map<String, Object> pd = (Map<String, Object>) var
							.get("shipPosition");
					ShipCooordData cpd = new ShipCooordData(pd);
					Double ll = Double.valueOf(cpd.getLatitude());
					if (ll > 0) {
						positionDatas.add(cpd);
						Message message = new Message();
						message.what = 2;
						handler.sendMessage(message);
					}
				} else {
					Toast.makeText(getApplicationContext(), "服务不可用",
							Toast.LENGTH_LONG).show();
				}
			}

		}, "/mobile/monitor/latestPosition", apiParams, "get");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		timer.cancel();
		timer.purge();
		timer = null;
	}

	// @Override
	// protected void onStop() {
	// super.onStop();
	// timer.cancel();
	// timer.purge();
	// }
	// @Override
	// protected void onPause() {
	// super.onPause();
	// }
	// @Override
	// protected void onRestart() {
	// super.onRestart();
	// initData();
	// }
	class MyTimerTask extends TimerTask {
		public Handler mHandler;

		@Override
		public void run() {
			if (dataInited) {
				loadDataOneMin();
			}
		}
	}

	/**
	 * 获取最近的一个点
	 * 
	 * @param ll
	 * @param as
	 * @return
	 */
	private ShipCooordData getMinDistace(LatLng ll, ArrayList<ShipCooordData> as) {
		// pt1是点pt在折线（由points构成）上最近点。
		// points为构成polyline的点列表
		// LatLng pt1 = SpatialRelationUtil.getNearestPointFromLine(points, pt);

		double dist = 1000000.0D;
		ShipCooordData result = null;
		int size = as.size();
		if (size >= 2) {
			Double x = ll.latitude;
			Double y = ll.longitude;
			for (int i = 0; i < size - 2; i++) {
				Double x0 = as.get(i).getLatitude();
				Double x1 = as.get(i + 1).getLatitude();
				Double y0 = as.get(i).getLongitude();
				Double y1 = as.get(i + 1).getLongitude();
				if (!(Math.abs(x - x0) > Math.abs(x - x1))
						&& (Math.abs(y - y0) > Math.abs(y - y1))) {
					Double tmpDist = (x - x0) * (x - x0) + (y - y0) * (y - y0);
					if (dist > tmpDist) {
						dist = tmpDist;
						result = as.get(i);
					}
				}
			}
			// 计算最后一个距离
			Double lastX = as.get(size - 1).getLatitude();
			Double lastY = as.get(size - 1).getLongitude();
			Double lastDist = (x - lastX) * (x - lastX) + (y - lastY)
					* (y - lastY);
			if (dist > lastDist) {
				result = as.get(size - 1);
			}
		}
		return result;
	}
}
