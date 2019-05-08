package com.hangyi.zd.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.eyunda.main.CommonListActivity;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.domain.location.ShipCooordData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.CalendarUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.tools.ParseJson;
import com.hangyi.tools.Util;
import com.hangyi.zd.activity.dialog.CustomDialog;
import com.hangyi.zd.activity.dialog.ViewArea;
import com.hangyi.zd.domain.NodeCode;
import com.hangyi.zd.domain.PoliceData;
import com.hangyi.zd.domain.PositionData;
import com.hangyi.zd.domain.ShipCKGpssData;
import com.hangyi.zd.domain.ShipGpsData;
import com.hangyi.zd.domain.ShipModelCode;
import com.hangyi.zd.domain.ShipModelData;
import com.hangyi.zd.domain.ShipModelNoData;
import com.hangyi.zd.domain.ShipOneCKHcData;
import com.hangyi.zd.domain.ShipVoyageData;
import com.hangyi.zd.domain.ShipVoyageNodeData;
import com.hangyi.zd.domain.UserPowerData;
import com.hangyi.zd.domain.UserPowerShipData;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

@SuppressLint("ResourceAsColor")
public class ShipPoliceActivity extends CommonListActivity implements OnClickListener,OnMapClickListener {
	ImageLoader mImageLoader;
	Data_loader dataLoader;
	
	private RelativeLayout history;
	private LinearLayout ll_1,ll_2,ll_3,ll_4,ll_5,ll_6;
	private LinearLayout shipImg,shipImg3,shipImg4,shipInfo3,shipInfo4;
	private TextView tv41,tv42,tv43,tv44,tv45,tv46,tv47,tv48;
	private TextView tv31,tv32,tv33,tv34,tv35,tv36,tv37,tv38;
	private TextView tv_speed4,tv_lat4,tv_j4,tv_w4;
	private TextView tv_speed3,tv_lat3,tv_j3,tv_w3;
	private ImageView img31,img32,img33;
	private ImageView img1;
	private ImageView img2;
	private ImageView img3;
	private ImageView img4;
	
	MapView mapHistory;
	BaiduMap bmHistory;
	UiSettings uisHistory;
	public static final LatLng BEIJING_LATLNG = new LatLng(23.038006, 113.509988);
	
	private String pushMsgNid = "";
	private String shipName = "";
	private String shipId= "";
	private int imgSize = 0;
	private String policeType = "";
	private String hcNum= "";
	private String start= "";
	private String end= "";
	private String eventTime= "";
	List<ShipModelNoData> smnds = null;
	BitmapDescriptor mRedTexture = BitmapDescriptorFactory
			.fromAsset("icon_road_red_arrow.png");
	List<PositionData> positionDatas = new ArrayList<PositionData>();
	
	protected ShipCooordData curShip;
	Thread parseJsonThread = null;
	Thread parseJsonThread2 = null;
	List<ShipOneCKHcData> ckGpsData;
	List<ShipGpsData> ssGpsData;
	String leaveLoadingDockTime;
	String arrivedUnloadingDockTime;
	String moreTRoutePoliceSSTime="";
	String sSDistance = "";
	public static final int LoadedSSDistance = 12;
	public static final int LoadedShipPositionFail = 11;
	public static final int ParseCKJson = 10;
	public static final int ParseSSJson = 9;
	public static final int ParseCKJsonIsEmpty = 8;
	public static final int ParseCKJsonIsError = 7;
	public static final int ParseSSJsonIsEmpty = 6;
	public static final int ParseSSJsonIsError = 5;
	public static final int LoadedPoliceInfo = 4;
	public static final int LoadedShipPosition = 3;
	public static final int LoadedMoreTRoutePoliceSSTime = 2;
	public static final int LoadedPortArea = 1;
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LoadedMoreTRoutePoliceSSTime:
//				tv44.setVisibility(View.VISIBLE);
				tv45.setText("运行航时："+moreTRoutePoliceSSTime);
				
//				tv35.setVisibility(View.VISIBLE);
				tv36.setText("运行航时："+moreTRoutePoliceSSTime);
				break;
			case LoadedPortArea:
				addPortArea();
				break;
			case LoadedSSDistance:
				tv38.setText("运行里程(km)："+sSDistance);
				tv47.setText("运行里程(km)："+sSDistance);
				break;
			case LoadedShipPosition:
				initViewData();
				showShipImg();
				addMarker();
				break;
			case LoadedShipPositionFail:
				Toast.makeText(ShipPoliceActivity.this, "数据加载失败！",
						Toast.LENGTH_SHORT).show();
				break;
			case LoadedPoliceInfo:
				initView();
				postReadFlag();
				loadPortArea();
				loadDataOneMin(eventTime);
				loadShipCurrHC("0x"+shipId,hcNum);//Function=25&ShipID=0x112F&str=航次编号
				loadShipCKHX(start,end);;
				break;
			case ParseCKJsonIsEmpty:
				Toast.makeText(ShipPoliceActivity.this, "参考航线数据为空",
						Toast.LENGTH_SHORT).show();
				break;
			case ParseCKJsonIsError:
				Toast.makeText(ShipPoliceActivity.this, "参考航线数据解析异常",
						Toast.LENGTH_SHORT).show();
				break;
			case ParseSSJsonIsEmpty:
				Toast.makeText(ShipPoliceActivity.this, "实际运行航线数据为空",
						Toast.LENGTH_SHORT).show();
				break;
			case ParseSSJsonIsError:
				Toast.makeText(ShipPoliceActivity.this, "实际运行航线数据解析异常",
						Toast.LENGTH_SHORT).show();
				break;
			case ParseSSJson:
				if(ssGpsData!=null&&ssGpsData.size()>0){
					drawLines(ssGpsData,null,13);
				}
				break;
			case ParseCKJson:
				if(ckGpsData!=null&&ckGpsData.size()>0){
					initCKData(ckGpsData);
					for(ShipOneCKHcData socd:ckGpsData){
						if(socd.getList().size()>0)
							drawLines(null,socd.getList(),13);
					}
				}
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
		SDKInitializer.initialize(getApplicationContext());
		com.baidu.mapapi.map.MapView.setCustomMapStylePath(NewContentFragment.getAssetsCacheFile(this,"baidu_custom_config"));
		setContentView(R.layout.zd_ship_police);

		ImageLoader.getInstance().destroy();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(GlobalApplication.getInstance().getImageLoaderConfiguration());
		dataLoader = new Data_loader();
		
		pushMsgNid = getIntent().getStringExtra("msgNid");
//		shipId = getIntent().getStringExtra("shipId");
//		shipName = getIntent().getStringExtra("shipName");
//		imgSize = getIntent().getIntExtra("imgSize", 0);
//		policeType = getIntent().getStringExtra("policeType");
//		hcNum = getIntent().getStringExtra("hcNum");
//		start = getIntent().getStringExtra("start");
//		end = getIntent().getStringExtra("end");
//		time = getIntent().getStringExtra("time");
		
		initMap();
		NewContentFragment.mapAddPort(this,bmHistory);
		
		if(pushMsgNid!=null&&!"0".equals(pushMsgNid))
			loadData();
		else
			Toast.makeText(ShipPoliceActivity.this, "数据加载为空！",
					Toast.LENGTH_SHORT).show();
	}
	
	protected void initCKData(List<ShipOneCKHcData> cks) {
		String ckD = "";
		String ckT = "";
		for(ShipOneCKHcData s:cks){
			ckD += s.getRouteDistance() + "；";
			ckT += Util.getFlightTimeByMins(s.getRouteTime()) + "；";
		}
		if(!"".equals(ckD)){
			ckD = ckD.substring(0, ckD.length()-1);
		}
		if(!"".equals(ckT)){
			ckT = ckT.substring(0, ckT.length()-1);
		}
		
		tv35.setText("参考航时："+ckT);
		tv37.setText("参考里程(km)："+ckD);
		tv44.setText("参考航时："+ckT);
		tv46.setText("参考里程(km)："+ckD);
		
	}

	private void loadPortArea(){
		
		Map<String, Object> apiParams = new HashMap<String, Object>();
		dataLoader.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg2) {
				positionDatas = ParseJson.getPortArea(arg2);
				handler.sendEmptyMessage(LoadedPortArea);
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

			}
		}, ApplicationUrls.portarea.replaceAll(" ", "%20"), apiParams, "get");	
	}
	
	private void loadSSDistance(String startTime,String endTime){
		
		Map<String, Object> apiParams = new HashMap<String, Object>();
		dataLoader.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg2) {
				sSDistance = ParseJson.parseSSDistance(arg2);
				handler.sendEmptyMessage(LoadedSSDistance);
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

			}
		}, ApplicationUrls.loadSSDistance+"0x"+shipId
		+"&StartTime="+startTime.replaceAll(" ", "%20")+"&EndTime="+endTime.replaceAll(" ", "%20"), apiParams, "get");	
	}
	
	private void addPortArea(){
		List<LatLng> spts = new ArrayList<LatLng>(); 
		List<LatLng> epts = new ArrayList<LatLng>(); 
		LatLng spt1,spt2,spt3,spt4; 
		LatLng ept1,ept2,ept3,ept4;
		for(PositionData p:positionDatas){
			if(p.getPortName().equals(start)){
				spt1 = new LatLng(p.getPalat1(),p.getPalng1());
				spt2 = new LatLng(p.getPalat1(),p.getPalng2());
				spt3 = new LatLng(p.getPalat2(),p.getPalng2());
				spt4 = new LatLng(p.getPalat2(),p.getPalng1());
				spts.add(spt1);
				spts.add(spt2);
				spts.add(spt3);
				spts.add(spt4);
			}
			if(p.getPortName().equals(end)){
				ept1 = new LatLng(p.getPalat1(),p.getPalng1());
				ept2 = new LatLng(p.getPalat1(),p.getPalng2());
				ept3 = new LatLng(p.getPalat2(),p.getPalng2());
				ept4 = new LatLng(p.getPalat2(),p.getPalng1());
				epts.add(ept1);
				epts.add(ept2);
				epts.add(ept3);
				epts.add(ept4);
			}
		}
		if(!spts.isEmpty()){
			OverlayOptions spolygonOption = new PolygonOptions()  
			.points(spts)  
			.stroke(new Stroke(5, 0xAA00CC00))  
			.fillColor(0xAAFFF8DC);  
			bmHistory.addOverlay(spolygonOption);
		}
		if(!epts.isEmpty()){
			OverlayOptions epolygonOption = new PolygonOptions()  
			.points(epts)  
			.stroke(new Stroke(5, 0xAA00CC00))  
			.fillColor(0xAAFFF8DC);  
			bmHistory.addOverlay(epolygonOption);
		}
	}
	
	private void loadData() {
		Calendar now = Calendar.getInstance();
		String startTime = CalendarUtil.toYYYY_MM_DD_HH_MM_SS(CalendarUtil.addMonths(now, PoliceActivity.offMonths));
		String endTime = CalendarUtil.toYYYY_MM_DD_HH_MM_SS(now);
		
		Map<String, Object> apiParams = new HashMap<String, Object>();
		dataLoader.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg2) {
				try {
					List<PoliceData> list = ParseJson.parserPolice(arg2);
					if(list.size()>0){
						for(PoliceData pd:list){
							if(pd.getnID().equals(pushMsgNid)){
								
								SharedPreferences sp = getSharedPreferences(ApplicationConstants.UserPowerData_SharedPreferences, Context.MODE_PRIVATE);
								String object = sp.getString("UserPower", "");
								
								UserPowerData data = null;
								if(!"".equals(object)){
									Gson gson = new Gson();
									data = gson.fromJson(object, new TypeToken<UserPowerData>() {}.getType());
								}else
									data = new UserPowerData();
								
								flag:
									for(UserPowerShipData upsd:data.getUserPowerShipDatas()){
										if((upsd.getShipID()).equals("0x"+pd.getShipID())){
											for(ShipModelData smd:upsd.getShipModels()){
												if(smd.getModel() == ShipModelCode.five){
													imgSize = smd.getModelNolist().size() ;
													smnds = smd.getModelNolist();
													// 排序
													Collections.sort(smnds, new Comparator<ShipModelNoData>() {

														@Override
														public int compare(ShipModelNoData lhs, ShipModelNoData rhs) {
															return Integer.valueOf((lhs.getModelNo())).compareTo(
																	Integer.valueOf((rhs.getModelNo())));
														}
													});
													break flag;
												}
											}
										}
									}
								NodeCode nc = NodeCode.getByN(Integer.valueOf(pd.getEventType()));
								
								shipId = pd.getShipID();
								shipName = pd.getShipName();
								policeType = nc!=null?nc.getDescription():"未知报警";
								hcNum = pd.getVorageNumber();
								start = pd.getStart();
								end = pd.getEnd();
								eventTime = pd.getEventTime();
								
								handler.sendEmptyMessage(LoadedPoliceInfo); 
								break;
							}
						}
					}else{
						Toast.makeText(ShipPoliceActivity.this, "数据加载为空！",
								Toast.LENGTH_SHORT).show();
					}
					
				} catch (Exception e) {
					Toast.makeText(ShipPoliceActivity.this, "数据解析出错！",
							Toast.LENGTH_SHORT).show();
				}
				
//				dialog.dismiss();
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
		}, ApplicationUrls.policeList+NewContentFragment.getUrlTimeStr(startTime)+"&EndTime="+NewContentFragment.getUrlTimeStr(endTime), apiParams, "get");	
		}
	private void postReadFlag() {
		
		Map<String, Object> apiParams = new HashMap<String, Object>();
		dataLoader.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(final String arg2) {
				super.onSuccess(arg2);
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

			}
		}, ApplicationUrls.policeReadFlag+pushMsgNid, apiParams, "get");
	
	}

	protected void drawLines(List<ShipGpsData> shipGpsDatas, List<ShipCKGpssData> shipOneCKHcData,int z) {
		if (shipGpsDatas!=null&&shipGpsDatas.size() >= 2) {
			List<LatLng> points = new ArrayList<LatLng>();
			
			for(int i=0; i<shipGpsDatas.size();i++){
				
				LatLng start;
				if(shipGpsDatas.get(i).getBdgpsLongitude()!=null&&shipGpsDatas.get(i).getBdgpsLongitude()!=0.0)
					start = new LatLng(shipGpsDatas.get(i).getBdgpsLatitude(),shipGpsDatas.get(i).getBdgpsLongitude());
				else
					start = new LatLng( shipGpsDatas.get(i).getGpsLatitude(),shipGpsDatas.get(i).getGpsLongitude());
				
				points.add(start);
			}
			if (points.size() > 1) {
				// 添加纹理图片列表
				List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
				List<Integer> textureIndexs = new ArrayList<Integer>();
				for (int i = 0; i <points.size() ; i++) {
					textureList.add(mRedTexture);
					textureIndexs.add(i);
				}
				
				//.color(0xAAFF0000)
				OverlayOptions ooPolyline = new PolylineOptions().width(10)
						.zIndex(20).points(points).textureIndex(textureIndexs)// 点位纹理图片信息的顺序列表
						.customTextureList(textureList);// 纹理图片列表;;
				bmHistory.addOverlay(ooPolyline);
			}
		}else if(shipOneCKHcData!=null&&shipOneCKHcData.size() >= 2){
			List<LatLng> points = new ArrayList<LatLng>();
			
			for(int i=0; i<shipOneCKHcData.size();i++){
				
				LatLng start= new LatLng(Double.valueOf(shipOneCKHcData.get(i).getLatitude())
							,Double.valueOf(shipOneCKHcData.get(i).getLongitude()));
				
				points.add(start);
			}
			if (points.size() > 1) {
				OverlayOptions ooPolyline = new PolylineOptions().width(7)
						.zIndex(20).color(0xAA0000FF).points(points);
				bmHistory.addOverlay(ooPolyline);
			}
		}
	}
	public void setCenter(LatLng cenpt, float z2) {
		// 设定中心点坐标
//		LatLng cenpt =  MapConvertUtil.convertFromGPS(new LatLng(lat, lng));
//		LatLng cenpt =  new LatLng(lat, lng);
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(z2)
				.build();

		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		// 改变地图状态
		this.bmHistory.animateMapStatus(mMapStatusUpdate);
	}
	private void addMarker() {
		if(curShip==null)
			return;
		// 定义Maker坐标点
		// 构建Marker图标
//		LatLng point = MapConvertUtil.convertFromGPS( new LatLng(Double.parseDouble(toPoint.getLatitude()),
//				Double.parseDouble(toPoint.getLongitude())));
		LatLng first;
		if (null != curShip.getBdLatitude()
				&& !"0.0".equals(curShip.getBdLatitude())) {

			first = new LatLng(Double.valueOf(curShip.getBdLatitude()),
					Double.valueOf(curShip.getBdLongitude()));
		} else {

			first = new LatLng(Double.valueOf(curShip.getGpsLatitude()),
					Double.valueOf(curShip.getGpsLongitude()));
		}
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.ship_green1);
		
		// 构建MarkerOption，用于在地图上添加Marker
//		float cou = (float) (0-Double.valueOf(curShip.getGpsCourse()));
		float cou = (float) (Float.valueOf(curShip.getGpsCourse())-0.0f);
		
		BitmapDescriptor baseBitmapOrg = BitmapDescriptorFactory
				.fromResource(R.drawable.kuang2);
		
		bitmap = BitmapDescriptorFactory.fromBitmap(Util.first(getResources(),bitmap.getBitmap(),cou,baseBitmapOrg.getBitmap()));
		
		OverlayOptions option = new MarkerOptions().position(first)
				.icon(bitmap).anchor(0.5f, 0.5f).title("船名");
//		OverlayOptions option = new MarkerOptions().position(first)
//				.icon(bitmap).rotate(cou);
		this.bmHistory.addOverlay(option);
		setCenter(first, 13);
	}


	private void loadShipCurrHC(String shipId2, String hcNum2) {
		
		Map<String, Object> apiParams = new HashMap<String, Object>();
		dataLoader.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(final String arg2) {
				super.onSuccess(arg2);
				try {
					ShipVoyageData list = ParseJson.parserHangchi(arg2);
					
					for(ShipVoyageNodeData pd :list.getNodes()){
						//从“驶离装点”节点开始判断
						if(pd.getNodeCode()==NodeCode.leaveLoadingDock){
							leaveLoadingDockTime = pd.getOpTime();
						}
						//根据当前航次状态来判断实时轨迹起止时间点
						//（1、还没有到卸点为当前时间2、到达卸点为到卸点时间）
						if(pd.getNodeCode()==NodeCode.arrivedUnloadingDock){
							arrivedUnloadingDockTime = pd.getOpTime();
						}
					}
					if(leaveLoadingDockTime != null){
						if(policeType.equals(NodeCode.unRoutePolice.getDescription())){
							if(arrivedUnloadingDockTime != null){
								loadShipSSHX(list.getShipID(),leaveLoadingDockTime,arrivedUnloadingDockTime);
							}else{
								loadShipSSHX(list.getShipID(),leaveLoadingDockTime,CalendarUtil.toYYYY_MM_DD_HH_MM_SS(Calendar.getInstance()));
							}
						}else {
							if(arrivedUnloadingDockTime != null){
								moreTRoutePoliceSSTime = Util.getFlightTimeOnLine(leaveLoadingDockTime, arrivedUnloadingDockTime);
								handler.sendEmptyMessage(LoadedMoreTRoutePoliceSSTime);
								
								loadSSDistance(leaveLoadingDockTime,arrivedUnloadingDockTime);
							}else{
								moreTRoutePoliceSSTime = Util.getFlightTimeOnLine(leaveLoadingDockTime, CalendarUtil.toYYYY_MM_DD_HH_MM_SS(Calendar.getInstance()));
								handler.sendEmptyMessage(LoadedMoreTRoutePoliceSSTime);
								
								loadSSDistance(leaveLoadingDockTime,CalendarUtil.toYYYY_MM_DD_HH_MM_SS(Calendar.getInstance()));
							}
						}
					}
				} catch (Exception e) {
					Toast.makeText(ShipPoliceActivity.this, "当前航次数据解析异常",
							Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
		}, ApplicationUrls.shipCurrHC+shipId2+"&str="+hcNum2, apiParams, "get");
	
	}

	private void loadShipSSHX(String shipID,String startTime, String endTime) {
		
		Map<String, Object> apiParams = new HashMap<String, Object>();
		dataLoader.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(final String arg2) {
				super.onSuccess(arg2);
				
				parseJsonThread2 = new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							ssGpsData = ParseJson.getHistoryGps(arg2);
							
							if (ssGpsData.isEmpty()) {
								
								Message message = new Message();
								message.what = ParseSSJsonIsEmpty;
								handler.sendMessage(message);
							} else {
								
									Message message = new Message();
									message.what = ParseSSJson;
									handler.sendMessage(message);
								
							}
						} catch (Exception e) {
							Message message = new Message();
							message.what = ParseSSJsonIsError;
							handler.sendMessage(message);
						}
					}
				});
				parseJsonThread2.setDaemon(true);
				parseJsonThread2.start();
//				dialog.dismiss();
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
		}, ApplicationUrls.historyGps + shipID + "&StartTime=" + startTime.replace(" ", "%20") + "&EndTime="
					+ endTime.replace(" ", "%20"), apiParams, "get");
	}

	private void loadShipCKHX(final String start,final String end) {
		
		Map<String, Object> apiParams = new HashMap<String, Object>();
		dataLoader.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("数据加载中", "请稍候...");
			}
			@Override
			public void onSuccess(final String arg2) {
				super.onSuccess(arg2);
				
				parseJsonThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
//							writeLog(start,end,arg2);
//							Util.writeFileData(start+end, arg2);
							
							ckGpsData = ParseJson.getCKHCGps(arg2);
							
							if (ckGpsData.isEmpty()) {
								
								Message message = new Message();
								message.what = ParseCKJsonIsEmpty;
								handler.sendMessage(message);
								
							}else{
								Message message = new Message();
								message.what = ParseCKJson;
								handler.sendMessage(message);
								
							} 
						} catch (Exception e) {
							
							Message message = new Message();
							message.what = ParseCKJsonIsError;
							handler.sendMessage(message);
						}
					}
				});
				parseJsonThread.setDaemon(true);
				parseJsonThread.start();
				dialog.dismiss();
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

				dialog.dismiss();
//				if (content != null && content.equals("can't resolve host"))
//					Toast.makeText(ShipPoliceActivity.this, "获取参考航线时，网络连接异常",
//							Toast.LENGTH_SHORT).show();
//				else if (content != null && content.equals("socket time out")) {
//					Toast.makeText(ShipPoliceActivity.this, "获取参考航线时，连接服务器超时",
//							Toast.LENGTH_SHORT).show();
//				} else if (content != null) {
//					Toast.makeText(ShipPoliceActivity.this, "获取参考航线失败",
//							Toast.LENGTH_SHORT).show();
//				} else 
//					Toast.makeText(ShipPoliceActivity.this, "获取参考航线时，未知异常",
//							Toast.LENGTH_SHORT).show();

			}
		}, ApplicationUrls.shipCKHX+start+"&EndPort="+end, apiParams, "get");
	
	}


	protected void initViewData() {
		if(curShip!=null){
			tv31.setText("航次："+hcNum);
			tv32.setText("时间："+eventTime);
			tv33.setText("起运港："+start);
			tv34.setText("到达港："+end);
			tv35.setText("参考航时：");
			tv36.setText("运行航时：");
			tv37.setText("参考里程(km)：");
			tv38.setText("运行里程(km)：");
			tv_speed3.setText("航速："+curShip.getGpsSpeed()+"节");
			tv_lat3.setText("航向："+curShip.getGpsCourse());
			tv_j3.setText("经度："+curShip.getGpsLongitude());
			tv_w3.setText("纬度："+curShip.getGpsLatitude());
			
			tv41.setText("航次："+hcNum);
			tv42.setText("起运港："+start);
			tv43.setText("到达港："+end);
			tv44.setText("参考航时：");
			tv45.setText("运行航时：");
			tv46.setText("参考里程(km)：");
			tv47.setText("运行里程(km)：");
			tv48.setText("时间："+eventTime);
			tv_speed4.setText("航速："+curShip.getGpsSpeed()+"节");
			tv_lat4.setText("航向："+curShip.getGpsCourse());
			tv_j4.setText("经度："+curShip.getGpsLongitude());
			tv_w4.setText("纬度："+curShip.getGpsLatitude());
		}
	}

	private void initView() {
		setTitle(policeType+"("+shipName+")");
		
		ll_1 = (LinearLayout)  findViewById(R.id.ll_1);
		ll_2 = (LinearLayout)  findViewById(R.id.ll_2);
		ll_3 = (LinearLayout)  findViewById(R.id.ll_3);
		ll_4 = (LinearLayout)  findViewById(R.id.ll_4);
		ll_5 = (LinearLayout)  findViewById(R.id.ll_5);
		ll_6 = (LinearLayout)  findViewById(R.id.ll_6);
		
		shipInfo3 = (LinearLayout)  findViewById(R.id.shipInfo3);
		shipInfo3.setOnClickListener(this);
		shipInfo4 = (LinearLayout)  findViewById(R.id.shipInfo4);
		shipInfo4.setOnClickListener(this);
		tv_speed3 = (TextView)  findViewById(R.id.tv_speed3);
		tv_lat3 = (TextView)  findViewById(R.id.tv_lat3);
		tv_speed4 = (TextView)  findViewById(R.id.tv_speed4);
		tv_lat4 = (TextView)  findViewById(R.id.tv_lat4);
		tv_j3 = (TextView)  findViewById(R.id.tv_j3);
		tv_j4 = (TextView)  findViewById(R.id.tv_j4);
		tv_w3 = (TextView)  findViewById(R.id.tv_w3);
		tv_w4 = (TextView)  findViewById(R.id.tv_w4);
		
		tv31 = (TextView)  findViewById(R.id.tv31);
		tv32 = (TextView)  findViewById(R.id.tv32);
		tv33 = (TextView)  findViewById(R.id.tv33);
		tv34 = (TextView)  findViewById(R.id.tv34);
		tv35 = (TextView)  findViewById(R.id.tv35);
		tv36 = (TextView)  findViewById(R.id.tv36);
		tv37 = (TextView)  findViewById(R.id.tv37);
		tv38 = (TextView)  findViewById(R.id.tv38);
		
		tv41 = (TextView)  findViewById(R.id.tv41);
		tv42 = (TextView)  findViewById(R.id.tv42);
		tv43 = (TextView)  findViewById(R.id.tv43);
		tv44 = (TextView)  findViewById(R.id.tv44);
		tv45 = (TextView)  findViewById(R.id.tv45);
		tv46 = (TextView)  findViewById(R.id.tv46);
		tv47 = (TextView)  findViewById(R.id.tv47);
		tv48 = (TextView)  findViewById(R.id.tv48);
		
		shipImg = (LinearLayout)  findViewById(R.id.shipImg);
		shipImg3 = (LinearLayout)  findViewById(R.id.shipImg3);
		shipImg4 = (LinearLayout)  findViewById(R.id.shipImg4);
		img1 = (ImageView)  findViewById(R.id.img1);
		img2 = (ImageView)  findViewById(R.id.img2);
		img3 = (ImageView)  findViewById(R.id.img3);
		img4 = (ImageView)  findViewById(R.id.img4);
		
		img31 = (ImageView)  findViewById(R.id.img31);
		img32 = (ImageView)  findViewById(R.id.img32);
		img33 = (ImageView)  findViewById(R.id.img33);
		
		history = (RelativeLayout)  findViewById(R.id.history);
		history.setOnClickListener(this);
		
		if(imgSize != 0&&imgSize == 3){
			shipImg.setVisibility(View.VISIBLE);
			shipImg3.setVisibility(View.VISIBLE);
		}else if(imgSize != 0&&imgSize <= 4){
			shipImg.setVisibility(View.VISIBLE);
			shipImg4.setVisibility(View.VISIBLE);
		}
		
		if(policeType.equals(NodeCode.moreTRoutePolice.getDescription())){
			tv44.setVisibility(View.VISIBLE);
			tv45.setVisibility(View.VISIBLE);
			
			ll_3.setVisibility(View.VISIBLE);
			tv35.setVisibility(View.VISIBLE);
			tv36.setVisibility(View.VISIBLE);
			
			ll_1.setBackgroundColor(getResources().getColor(R.color.ship_jj1));
			ll_2.setBackgroundColor(getResources().getColor(R.color.ship_jj2));
			ll_3.setBackgroundColor(getResources().getColor(R.color.ship_jj1));
			ll_5.setBackgroundColor(getResources().getColor(R.color.ship_jj2));
			ll_6.setBackgroundColor(getResources().getColor(R.color.ship_jj1));
		}else if(policeType.equals(NodeCode.moreDRoutePolice.getDescription())){
			tv46.setVisibility(View.VISIBLE);
			tv47.setVisibility(View.VISIBLE);
			
			ll_4.setVisibility(View.VISIBLE);
			tv37.setVisibility(View.VISIBLE);
			tv38.setVisibility(View.VISIBLE);
			
			ll_1.setBackgroundColor(getResources().getColor(R.color.ship_jj1));
			ll_2.setBackgroundColor(getResources().getColor(R.color.ship_jj2));
			ll_4.setBackgroundColor(getResources().getColor(R.color.ship_jj1));
			ll_5.setBackgroundColor(getResources().getColor(R.color.ship_jj2));
			ll_6.setBackgroundColor(getResources().getColor(R.color.ship_jj1));
		}else{
			ll_1.setBackgroundColor(getResources().getColor(R.color.ship_jj1));
			ll_2.setBackgroundColor(getResources().getColor(R.color.ship_jj2));
			ll_5.setBackgroundColor(getResources().getColor(R.color.ship_jj1));
			ll_6.setBackgroundColor(getResources().getColor(R.color.ship_jj2));
		}
	}
	protected void loadDataOneMin(String pTime) {
		if(pTime==null||pTime.equals(""))
			return;
		
		Map<String, Object> apiParams = new HashMap<String, Object>();
		
		dataLoader.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg2) {
				super.onSuccess(arg2);
				
				ShipCooordData shipCooordData = ParseJson.parserSTPosition(arg2);
				
					if(shipCooordData!=null){
						curShip = shipCooordData;
						curShip.setShipName(shipName);
						
						Message message = new Message();
						message.what = LoadedShipPosition;
						handler.sendMessage(message);
					}else{
						handler.sendEmptyMessage(LoadedShipPositionFail);
					}
				
				
				
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

//				if (content != null && content.equals("can't resolve host"))
//					Toast.makeText(ShipPoliceActivity.this, "数据异常，请稍后再试！",
//							Toast.LENGTH_SHORT).show();
//				else if (content != null && content.equals("socket time out")) {
//					Toast.makeText(ShipPoliceActivity.this, "连接服务器超时",
//							Toast.LENGTH_SHORT).show();
//				} else if (content != null) {
//					Toast.makeText(ShipPoliceActivity.this, content,
//							Toast.LENGTH_SHORT).show();
//				} else
//					Toast.makeText(ShipPoliceActivity.this, "未知异常",
//							Toast.LENGTH_SHORT).show();
			}
		}, ApplicationUrls.loadSTPosition+"0x"+shipId+"&GPSTime="+pTime.replace(" ", "%20"), apiParams, "get");
	}


	private void initMap() {

		mapHistory = (MapView) findViewById(R.id.mapHistory);
//		Uri uri=Uri.parse("android:resource://"+this.getPackageName()+"/"+R.raw.baidu_custom_config);
//		mapHistory.setCustomMapStylePath(getAssetsCacheFile(this,"baidu_custom_config"));
		// 不显示地图缩放控件（按钮控制栏）
		mapHistory.showZoomControls(false);
		// 不显示地图上比例尺
//		mapHistory.showScaleControl(false);
		// 隐藏百度的LOGO
		View child = mapHistory.getChildAt(1);
		if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) 
			child.setVisibility(View.INVISIBLE);
		
		bmHistory = mapHistory.getMap();
		bmHistory.showMapPoi(false);// 将底图标注设置为隐藏
		
		this.uisHistory = this.bmHistory.getUiSettings();
		this.uisHistory.setOverlookingGesturesEnabled(false);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(BEIJING_LATLNG);
		this.bmHistory.setMapStatus(msu);
		float z = 13;//
		msu = MapStatusUpdateFactory.zoomTo(z);
		this.bmHistory.setMapStatus(msu);
		this.bmHistory.setOnMapClickListener(this);
	}
	@Override
	public void onPause() {
		super.onPause();
		mapHistory.onPause();
	}
	@Override
	public void onResume() {
		super.onResume();
		mapHistory.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if("".equals(policeType)&&"".equals(shipName))
			setTitle("报警详情");
		else
			setTitle(policeType+"("+shipName+")");
		
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
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shipInfo3:
			startActivity(new Intent(ShipPoliceActivity.this,ShipInfoActivity.class).putExtra("shipData", curShip));
			break;
		case R.id.shipInfo4:
			startActivity(new Intent(ShipPoliceActivity.this,ShipInfoActivity.class).putExtra("shipData", curShip));
			break;
		case R.id.history:
			if(leaveLoadingDockTime!=null){
				Intent intent = new Intent(ShipPoliceActivity.this,ZdShipDynamicActivity.class)
				.putExtra("shipID", curShip.getShipID())
				.putExtra("shipName", curShip.getShipName())
				.putExtra("startTime", leaveLoadingDockTime)
				.putExtra("startPort", start)
				.putExtra("endPort", end)
				.putExtra("policeType", policeType)
				.putExtra(ApplicationConstants.historyLineType, ApplicationConstants.historyLinePolice);
				
				if(policeType.equals(NodeCode.unRoutePolice.getDescription())){
					if(arrivedUnloadingDockTime != null){
						intent.putExtra("endTime", arrivedUnloadingDockTime);
					}else{
						intent.putExtra("endTime",CalendarUtil.toYYYY_MM_DD_HH_MM_SS(Calendar.getInstance()));
					}
				}else{
					//处理回放时报警时间后延多少时间为回放结束时间（大于当前时间为当前时间）
					if(ckGpsData!=null&&ckGpsData.size()>0
							&&ckGpsData.get(0).getRoutePlayRemain()!=null
							&&(!"".equals(ckGpsData.get(0).getRoutePlayRemain()))){
						
						try {
							Calendar etc = CalendarUtil.parseYYYY_MM_DD_HH_MM_SS(eventTime);
							Calendar etc_offmins = CalendarUtil.addMinutes(etc, Integer.parseInt(ckGpsData.get(0).getRoutePlayRemain()));
							if(Calendar.getInstance().getTimeInMillis()>etc_offmins.getTimeInMillis())
								intent.putExtra("endTime", CalendarUtil.toYYYY_MM_DD_HH_MM_SS(etc_offmins));
							else
								intent.putExtra("endTime", CalendarUtil.toYYYY_MM_DD_HH_MM_SS(Calendar.getInstance()));
						} catch (Exception e) {
							intent.putExtra("endTime", eventTime);
						}
					}else
						intent.putExtra("endTime", eventTime);
				}
				
				startActivity(intent);
			}else
				Toast.makeText(ShipPoliceActivity.this, "航次数据加载失败",
						Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
		
	}
	
	private void showShipImg(){
		if(smnds == null)
			 return;
		if(smnds.size()>4){
			Toast.makeText(this, "目前最多只支持四个镜头显视。", Toast.LENGTH_LONG).show();
			return;
		}
			
		shipImg.setVisibility(View.VISIBLE);
		if(imgSize == 3){
			shipImg3.setVisibility(View.VISIBLE);
			shipImg4.setVisibility(View.GONE);
			
			img31.setOnTouchListener(new MyOnTouchListener(curShip,Integer.valueOf(smnds.get(0).getModelNo()),img31));
			NewContentFragment.displayImageForPolice(mImageLoader,this,img31,curShip,Integer.valueOf(smnds.get(0).getModelNo()));
			img32.setOnTouchListener(new MyOnTouchListener(curShip,Integer.valueOf(smnds.get(1).getModelNo()),img32));
			NewContentFragment.displayImageForPolice(mImageLoader,this,img32,curShip,Integer.valueOf(smnds.get(1).getModelNo()));
			img33.setOnTouchListener(new MyOnTouchListener(curShip,Integer.valueOf(smnds.get(2).getModelNo()),img33));
			NewContentFragment.displayImageForPolice(mImageLoader,this,img33,curShip,Integer.valueOf(smnds.get(2).getModelNo()));
		}else{
			shipImg3.setVisibility(View.GONE);
			shipImg4.setVisibility(View.VISIBLE);
			
			for (int i = 1; i <= imgSize; i++) {
//				if(imgSize == 1) {
//					img2.setVisibility(View.VISIBLE);
//					img2.setOnTouchListener(new MyOnTouchListener(curShip,i,img2));
//					
//					NewContentFragment.displayImageForPolice(mImageLoader,this,img2,curShip,i);
//				} else if(imgSize == 3){
//					if(i ==3){
//						img4.setVisibility(View.VISIBLE);
//						img4.setOnTouchListener(new MyOnTouchListener(curShip,i,img4));
//						
//						NewContentFragment.displayImageForPolice(mImageLoader,this,img4,curShip,i);
//						break;
//					}
//					int view_id = getResources().getIdentifier("img"+i, "id",  this.getPackageName());
//					ImageView view = (ImageView) this.findViewById(view_id);
//					view.setVisibility(View.VISIBLE);
//					view.setOnTouchListener(new MyOnTouchListener(curShip,i,view));
//					
//					NewContentFragment.displayImageForPolice(mImageLoader,this,view,curShip,i);
//				}else {
					
					int view_id = getResources().getIdentifier("img"+i, "id",  this.getPackageName());
					ImageView view = (ImageView) this.findViewById(view_id);
					view.setVisibility(View.VISIBLE);
					view.setOnTouchListener(new MyOnTouchListener(curShip,Integer.valueOf(smnds.get(i-1).getModelNo()),view));
					
					NewContentFragment.displayImageForPolice(mImageLoader,this,view,curShip,Integer.valueOf(smnds.get(i-1).getModelNo()));
//				}
			}
		}
	}
	
	private class MyOnTouchListener implements OnTouchListener {
		private int i;
		ImageView view;

		public MyOnTouchListener(ShipCooordData curShip1, int i,ImageView view) {
			this.i = i;
			this.view = view;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:// 手指压下屏幕
//				startActivity(new Intent(this,ShowBigImage.class).putExtra("shipId", curShip.getShipID()).putExtra("channel", i).putExtra("shipName", curShip.getShipName()));
				showCustomDialog(i,view);
				break;
			}
			return false;
		}
	}
	private  void showCustomDialog(int i,ImageView view1) {
		// 初始化一个自定义的Dialog
		final CustomDialog.Builder b = new CustomDialog.Builder(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		final View view = inflater.inflate(R.layout.progress_dialog, null);

		LinearLayout ll_viewArea = (LinearLayout) view.findViewById(R.id.ll_viewArea);
		LinearLayout.LayoutParams parm = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		parm.gravity = Gravity.CENTER;

		// 自定义布局控件，用来初始化并存放自定义imageView
		ViewArea viewArea = new ViewArea(b,this,curShip, i,view1);

		ll_viewArea.addView(viewArea, parm);

		b.setView(view);
		b.show();

		ll_viewArea.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				b.dismiss();
			}
		});
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapHistory.onDestroy();
		
		mRedTexture.recycle();
		if(parseJsonThread!=null){
			parseJsonThread.interrupt();
			parseJsonThread = null;
		}
		if(parseJsonThread2!=null){
			parseJsonThread2.interrupt();
			parseJsonThread2 = null;
		}
	}
}
