package com.hangyi.zd.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
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
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.CalendarUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.tools.ParseJson;
import com.hangyi.tools.Util;
import com.hangyi.zd.ClearService;
import com.hangyi.zd.ImgDownLoadService;
import com.hangyi.zd.activity.dialog.CustomDialog;
import com.hangyi.zd.activity.dialog.ViewArea2;
import com.hangyi.zd.activity.gridviewpage.AppAdapter;
import com.hangyi.zd.domain.ShipGpsData;
import com.hangyi.zd.domain.ShipModelCode;
import com.hangyi.zd.domain.ShipModelData;
import com.hangyi.zd.domain.ShipModelNoData;
import com.hangyi.zd.domain.UserPowerData;
import com.hangyi.zd.domain.UserPowerShipData;
import com.hangyi.zd.play.CacheSeekBarTimer;
import com.hangyi.zd.play.GpsDataQueue;
import com.hangyi.zd.play.LoadImgRunnable;
import com.hangyi.zd.play.LoadedList;
import com.hangyi.zd.play.PlayGpsTimer;
import com.hangyi.zd.play.PlayImageLoader;
import com.hangyi.zd.play.PlayListenerTimer;
import com.hangyi.zd.widge.CommonVideoView;
import com.hangyi.zd.widge.CommonVideoView.CommonVideoChangLintener;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ShipDynamicFragment extends Fragment implements OnClickListener,
		OnMarkerClickListener, OnMapClickListener ,CommonVideoChangLintener{
	
	Data_loader dataLoader;
	protected DialogUtil dialogUtil;
	protected ProgressDialog dialog;

	String shipID="",startTime="",endTime="",shipName="";
	MapView mapHistory;
	BaiduMap bmHistory;
	UiSettings uisHistory;
	public static final int playfast = 10;
	public static final int playmod = 5;
	public static final int playslo = 2;
	volatile public static int currPlayPosition = 0;
	public static final LatLng BEIJING_LATLNG = new LatLng(23.038006,
			113.509988);
	List<ShipGpsData> positionDatas;
	Marker marker;
	TextOptions historyTextOption = null;
	boolean setCenterFlag = true;
	
	float z = 13;//
	private ImageLoader mImageLoader;
    
	private ImageView videoPauseImg;
	private ImageView img1,img2,img3,img4;
	private ImageView img31,img32,img33;
	private TextView tv_time4,tv_speed4,tv_lat4,tv_j4,tv_w4;
	private TextView tv_time3,tv_speed3,tv_lat3,tv_j3,tv_w3;
	private LinearLayout shipImg,shipImg3,shipImg4,shipInfo3,shipInfo4;
	
	private CommonVideoView commonVideoView;
	private List<String> channels = new ArrayList<String>();
	
	private static int ParseJsonException = 9;
	private static int ParseJsonIsEmpty = 8;
	private static int ParseJsonTooMuch = 7;
	
	Thread parseJsonThread = null;
	ShipGpsData cPoint;
	
	BitmapDescriptor mRedTexture = BitmapDescriptorFactory
			.fromAsset("icon_road_red_arrow.png");
	
	private ImgDownLoadService.ServiceBinder mBinderService;
	private ServiceConnection connection = new ServiceConnection() {  
		@Override  
		public void onServiceDisconnected(ComponentName name) {  
		}  
		
		@Override  
		public void onServiceConnected(ComponentName name, IBinder service) {  
			mBinderService = (ImgDownLoadService.ServiceBinder) service;
		}  
	};

	public static File sdCardPath = Environment.getExternalStorageDirectory();// 获取SDCard目录
	public static File cacheDir = new File(sdCardPath + ApplicationConstants.imgCachePath);
	private ViewArea2 viewArea;
	
	/**实际加载GPS数*/
	public static int loadedGpsDataSize=0;
	/**加载时间段分钟数*/
	public static int needLoadedMinutes=0;
	
	
	private static final int centerChange=30;
	public static int flag = 0;
	public static boolean flagFirst = true;
	
	PowerManager.WakeLock mWakeLock;
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if(isAdded()){
				if (msg.what == 1) {// 初始化数据完成
					drawLines();
					initShipPowerChannels();
					gpsLoadedAfterStart();
					
				} else if (msg.what == 2) {// 读取新坐标完成
					if(CommonVideoView.isPlaying){
						int lastPointIndex = currPlayPosition;
						if (lastPointIndex < positionDatas.size()) {
							ShipGpsData currPoint = positionDatas.get(lastPointIndex);
							cPoint = currPoint;
							
							if(lastPointIndex ==0)
								moveMarker(currPoint, currPoint);
							else
								moveMarker(positionDatas.get(lastPointIndex-1),currPoint);
							
							showShipImg(currPoint);
							
							if(viewArea!=null)
								viewArea.playImg(cPoint);
							
							if(flagFirst){
								setCenter(currPoint, z);
								flagFirst=false;
							}
							if(flag == centerChange){
								flag = 0;
								setCenter(currPoint, z);
							}
							flag++;
							
							lastPointIndex++;
							currPlayPosition = lastPointIndex;
						}else{
//						CommonVideoView.isPlaying = false;
//						currPlayPosition = 0;
//						flagFirst=true;
//						flag = 0;
						}
					}else{
					}
					
				}else if(msg.what == 3){
//				if (lastPointIndex < positionDatas.size()) {
//					ShipGpsData lastPoint = positionDatas.get(lastPointIndex);
//					showShipImg(lastPoint);
//					
//					lastPointIndex++;
//					LoadedList.getInstance().setCurrPlayPosition(lastPointIndex);
//				}
				}else if(msg.what == ParseJsonException){
					Toast.makeText(getActivity(), "数据解析异常！请稍后再试！", Toast.LENGTH_LONG).show();
				}else if(msg.what == ParseJsonIsEmpty){
					Toast.makeText(getActivity(), "数据加载为空！", Toast.LENGTH_LONG).show();
				}else if(msg.what == ParseJsonTooMuch){
					Toast.makeText(getActivity(), "航线数据太多，超出显视范围，请重新选择时间段！", Toast.LENGTH_LONG).show();
				}
				super.handleMessage(msg);
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SDKInitializer.initialize(getActivity().getApplicationContext());
		com.baidu.mapapi.map.MapView.setCustomMapStylePath(ContentFragment.getAssetsCacheFile(getActivity(),"baidu_custom_config"));
		return inflater.inflate(R.layout.zd_ship_play_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		dataLoader = new Data_loader();
		dialogUtil=new DialogUtil(getActivity());
		
		ImageLoader.getInstance().destroy();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(PlayImageLoader.getInstance());
		
		Intent bindIntent = new Intent(getActivity(), ImgDownLoadService.class);  
		getActivity().bindService(bindIntent, connection, Context.BIND_AUTO_CREATE); 
		
		Intent intent = getActivity().getIntent();
		shipID = intent.getStringExtra("shipID")!=null?intent.getStringExtra("shipID"):"";
		startTime = intent.getStringExtra("startTime")!=null?intent.getStringExtra("startTime"):"";//"yyyy-MM-dd HH:mm:ss"
		endTime = intent.getStringExtra("endTime")!=null?intent.getStringExtra("endTime"):"";//"yyyy-MM-dd HH:mm:ss"
		shipName = intent.getStringExtra("shipName")!=null?intent.getStringExtra("shipName"):"";
		this.initMap();
		
		Calendar now = Calendar.getInstance();
		if("".equals(startTime)||"".equals(endTime)){
			startTime = CalendarUtil.toYYYY_MM_DD_HH_MM_SS(CalendarUtil.addDays(now, -1));
			endTime = CalendarUtil.toYYYY_MM_DD_HH_MM_SS(now);
		}
		
		initView(startTime,endTime);
		initData(startTime,endTime);
		
		PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag"); 
		
		super.onActivityCreated(savedInstanceState);
	}

	private void initView(String startTime, String endTime) {
//        WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);  
//        Display display = manager.getDefaultDisplay();  
//        display.getSize(pt);  
		
		commonVideoView = (CommonVideoView) getActivity().findViewById(R.id.common_videoView);
		commonVideoView.init(CalendarUtil.parseYYYY_MM_DD_HH_MM_SS(startTime),CalendarUtil.parseYYYY_MM_DD_HH_MM_SS(endTime));
		commonVideoView.setCommonVideoChangLintener(this);
		
		shipInfo3 = (LinearLayout) getActivity().findViewById(R.id.shipInfo3);
		shipInfo3.setOnClickListener(this);
		shipInfo4 = (LinearLayout) getActivity().findViewById(R.id.shipInfo4);
		shipInfo4.setOnClickListener(this);
		
		shipImg = (LinearLayout) getActivity().findViewById(R.id.shipImg);
		shipImg3 = (LinearLayout) getActivity().findViewById(R.id.shipImg3);
		shipImg4 = (LinearLayout) getActivity().findViewById(R.id.shipImg4);
		tv_time3 = (TextView) getActivity().findViewById(R.id.tv_time3);
		tv_speed3 = (TextView) getActivity().findViewById(R.id.tv_speed3);
		tv_lat3 = (TextView) getActivity().findViewById(R.id.tv_lat3);
		tv_time4 = (TextView) getActivity().findViewById(R.id.tv_time4);
		tv_speed4 = (TextView) getActivity().findViewById(R.id.tv_speed4);
		tv_lat4 = (TextView) getActivity().findViewById(R.id.tv_lat4);
		tv_j3 = (TextView) getActivity().findViewById(R.id.tv_j3);
		tv_j4 = (TextView) getActivity().findViewById(R.id.tv_j4);
		tv_w3 = (TextView) getActivity().findViewById(R.id.tv_w3);
		tv_w4 = (TextView) getActivity().findViewById(R.id.tv_w4);
		
		img1 = (ImageView) getActivity().findViewById(R.id.img1);
		img2 = (ImageView) getActivity().findViewById(R.id.img2);
		img3 = (ImageView) getActivity().findViewById(R.id.img3);
		img4 = (ImageView) getActivity().findViewById(R.id.img4);
//		img1.setOnTouchListener(new MyOnTouchListener(1,img1));
//		img2.setOnTouchListener(new MyOnTouchListener(2,img2));
//		img3.setOnTouchListener(new MyOnTouchListener(3,img3));
//		img4.setOnTouchListener(new MyOnTouchListener(4,img4));
		
		img31 = (ImageView) getActivity().findViewById(R.id.img31);
		img32 = (ImageView) getActivity().findViewById(R.id.img32);
		img33 = (ImageView) getActivity().findViewById(R.id.img33);
//		img31.setOnTouchListener(new MyOnTouchListener(1,img31));
//		img32.setOnTouchListener(new MyOnTouchListener(2,img32));
//		img33.setOnTouchListener(new MyOnTouchListener(3,img33));
		
		videoPauseImg = (ImageView) getActivity().findViewById(R.id.videoPauseImg);
		videoPauseImg.setOnClickListener(this);
	}

	private void initData(String startTime,String endTime){
		if(positionDatas==null){
			positionDatas = new ArrayList<ShipGpsData>();
		}else{
			positionDatas.clear();
		}
		loadData(startTime,endTime);
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	protected void initShipPowerChannels() {
		if(positionDatas!=null&&!positionDatas.isEmpty()){
			ShipGpsData sgd = positionDatas.get(0);
			shipID = sgd.getShipID();
			
			if(channels.isEmpty()){
				SharedPreferences sp = GlobalApplication.getInstance().getSharedPreferences(ApplicationConstants.UserPowerData_SharedPreferences, Context.MODE_PRIVATE);
				String object = sp.getString("UserPower", "");
				
				UserPowerData data = null;
				if(!"".equals(object)){
					Gson gson = new Gson();
					data = gson.fromJson(object, new TypeToken<UserPowerData>() {}.getType());
				}else
					data = new UserPowerData();
				
				
				flag:
				for(UserPowerShipData upsd:data.getUserPowerShipDatas()){
					if(upsd.getShipID().equals(sgd.getShipID())){
						for(ShipModelData smd:upsd.getShipModels()){
							if(smd.getModel() == ShipModelCode.five){
								for(ShipModelNoData smnd:smd.getModelNolist()){
									channels.add(smnd.getModelNo());
								}
								break flag;
							}else
								continue;
						}
					}else
						continue;
				}
				if(channels.size() == 3){
					img31.setOnTouchListener(new MyOnTouchListener(Integer.valueOf(channels.get(0)),img31));
					img32.setOnTouchListener(new MyOnTouchListener(Integer.valueOf(channels.get(1)),img32));
					img33.setOnTouchListener(new MyOnTouchListener(Integer.valueOf(channels.get(2)),img33));
				}else{
					for(int i=1;i<=channels.size();i++){
						int view_id = getResources().getIdentifier("img"+i, "id",  getActivity().getPackageName());
						ImageView view = (ImageView) getActivity().findViewById(view_id);
						view.setOnTouchListener(new MyOnTouchListener(Integer.valueOf(channels.get(i-1)),view));
					}
				}
			}
		}
	}
	
	public static String getUrl(ShipGpsData data,String string){
		String shipChannelCacheDirStr = cacheDir + "/" + data.getShipID() + "/" + string;
		String shipChannelCacheImg = shipChannelCacheDirStr+"/"+LoadImgRunnable.doGpsTime(data.getGpsTime())+".png";
		if(new File(shipChannelCacheImg).exists())
			return shipChannelCacheImg;
		else
			return null;
	}
	
	private void showShipImg(ShipGpsData curShip) {
	
			try {
				int size = channels.size();
				if(size ==0)
					return;
				
				if(size==3){
					shipImg.setVisibility(View.VISIBLE);
					shipImg3.setVisibility(View.VISIBLE);
					
					tv_time3.setText("最后时间："+curShip.getGpsTime());
					tv_speed3.setText("航速："+curShip.getGpsSpeed()+"节");
					tv_lat3.setText("航向："+curShip.getGpsCourse());
					tv_j3.setText("经度："+curShip.getGpsLongitude());
					tv_w3.setText("纬度："+curShip.getGpsLatitude());
					
//					img31.setOnTouchListener(new MyOnTouchListener(curShip,1,img31));
					String imgStr31 = getUrl(curShip,channels.get(0));
					if(imgStr31 !=null )
						img31.setImageDrawable(AppAdapter.createDrawable(getActivity(), BitmapFactory.decodeFile(imgStr31), null, curShip.getGpsTime()));
					
//					img32.setOnTouchListener(new MyOnTouchListener(curShip,2,img32));
					String imgStr32 = getUrl(curShip,channels.get(1));
					if(imgStr32 !=null )
						img32.setImageDrawable(AppAdapter.createDrawable(getActivity(), BitmapFactory.decodeFile(imgStr32), null, curShip.getGpsTime()));
					
//					img33.setOnTouchListener(new MyOnTouchListener(curShip,3,img33));
					String imgStr33 = getUrl(curShip,channels.get(2));
					if(imgStr33 !=null )
						img33.setImageDrawable(AppAdapter.createDrawable(getActivity(), BitmapFactory.decodeFile(imgStr33), null, curShip.getGpsTime()));
				}else{
					shipImg.setVisibility(View.VISIBLE);
					shipImg4.setVisibility(View.VISIBLE);
					
					tv_time4.setText("最后时间："+curShip.getGpsTime());
					tv_speed4.setText("航速："+curShip.getGpsSpeed()+"节");
					tv_lat4.setText("航向："+curShip.getGpsCourse());
					tv_j4.setText("经度："+curShip.getGpsLongitude());
					tv_w4.setText("纬度："+curShip.getGpsLatitude());
					
					for (int i = 1; i <= size; i++) {
//						if(size == 1) {
//							img2.setVisibility(View.VISIBLE);
//							String imgStr12 = getUrl(curShip,channels.get(i-1));
//							if(imgStr12 !=null )
//								img2.setImageDrawable(AppAdapter.createDrawable(getActivity(), BitmapFactory.decodeFile(imgStr12), null, curShip.getGpsTime()));
////						img2.setImageBitmap(BitmapFactory.decodeFile(imgStr12));
//						} else if(size == 3){
//							if(i ==3){
//								img4.setVisibility(View.VISIBLE);
//								
//								String imgStr21 = getUrl(curShip,channels.get(i-1));
//								if(imgStr21 !=null )
//									img4.setImageDrawable(AppAdapter.createDrawable(getActivity(), BitmapFactory.decodeFile(imgStr21), null, curShip.getGpsTime()));
////							img4.setImageBitmap(BitmapFactory.decodeFile(imgStr21));
//								break;
//							}
//							int view_id = getResources().getIdentifier("img"+i, "id",  getActivity().getPackageName());
//							ImageView view = (ImageView) getActivity().findViewById(view_id);
//							view.setVisibility(View.VISIBLE);
//							
//							String imgStr21 = getUrl(curShip,channels.get(i-1));
//							if(imgStr21 !=null )
//								view.setImageDrawable(AppAdapter.createDrawable(getActivity(), BitmapFactory.decodeFile(imgStr21), null, curShip.getGpsTime()));
////						view.setImageBitmap(BitmapFactory.decodeFile(imgStr21));
//						}else {
							
							int view_id = getResources().getIdentifier("img"+i, "id",  getActivity().getPackageName());
							ImageView view = (ImageView) getActivity().findViewById(view_id);
							view.setVisibility(View.VISIBLE);
							
							String imgStr21 = getUrl(curShip,channels.get(i-1));
							if(imgStr21 !=null )
								view.setImageDrawable(AppAdapter.createDrawable(getActivity(), BitmapFactory.decodeFile(imgStr21), null, curShip.getGpsTime()));
////						view.setImageBitmap(BitmapFactory.decodeFile(imgStr21));
						}
//					}
				}
				
			} catch (Exception e) {
			}
			
	}

	protected void gpsLoadedAfterStart() {
		LoadedList.getInstance().initQueue(positionDatas.size());
		
		commonVideoView.startCacheSeekBarTimer();
		startThreadLoadImg();
		PlayGpsTimer.getInstance().setHandler(handler);
    	PlayGpsTimer.getInstance().startPlayGpsTimer();
    	modChang();
	}

	private void startThreadLoadImg() {
		mBinderService.startThreadLoadImg(channels);
	}
	
	private void drawLines() {
		bmHistory.clear();
		NewContentFragment.mapAddPort(getActivity(),bmHistory);
		
		if (positionDatas.size() >= 2) {
			List<LatLng> points = new ArrayList<LatLng>();
			ShipGpsData lastPoint = positionDatas.get(0);
//			LatLng first = MapConvertUtil.convertFromGPS( new LatLng(Double.parseDouble(lastPoint.getLatitude()),
//					Double.parseDouble(lastPoint.getLongitude())));
			LatLng first;
			if(lastPoint.getBdgpsLongitude()!=null&&lastPoint.getBdgpsLongitude()!=0.0)
				first = new LatLng(lastPoint.getBdgpsLatitude(),lastPoint.getBdgpsLongitude());
			else
			    first = new LatLng(lastPoint.getGpsLatitude(),lastPoint.getGpsLongitude());
			points.add(first);
			setCenter(lastPoint, z);
			
			for(int i=1; i<positionDatas.size();i++){
//				LatLng start =  MapConvertUtil.convertFromGPS(new LatLng(Double.parseDouble(positionDatas.get(
//						i).getLatitude()), Double.parseDouble(positionDatas.get(i)
//						.getLongitude())));
				
				LatLng start;
				if(positionDatas.get(i).getBdgpsLongitude()!=null&&positionDatas.get(i).getBdgpsLongitude()!=0.0)
					start = new LatLng(positionDatas.get(i).getBdgpsLatitude(),positionDatas.get(i).getBdgpsLongitude());
				else
					start = new LatLng( positionDatas.get(i).getGpsLatitude(),positionDatas.get(i).getGpsLongitude());
				
				points.add(start);
			}
			if (points.size() > 1) { //.color(0xAAFF0000)
				// 添加纹理图片列表
				List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
				List<Integer> textureIndexs = new ArrayList<Integer>();
				for (int i = 0; i <points.size() ; i++) {
					textureList.add(mRedTexture);
					textureIndexs.add(i);
				}
				
				OverlayOptions ooPolyline = new PolylineOptions().width(10)
						.zIndex(20).points(points).textureIndex(textureIndexs)// 点位纹理图片信息的顺序列表
						.customTextureList(textureList);// 纹理图片列表;
				bmHistory.addOverlay(ooPolyline);
			}
		}
	};

	private synchronized void moveMarker(ShipGpsData tpp,ShipGpsData toPoint) {
		removeMarker();
		addMarker(tpp,toPoint);
	}
	
	private void addMarker(ShipGpsData topPoint,ShipGpsData currPoint) {
		// 定义Maker坐标点
		// 构建Marker图标
//		LatLng point = MapConvertUtil.convertFromGPS( new LatLng(Double.parseDouble(toPoint.getLatitude()),
//				Double.parseDouble(toPoint.getLongitude())));
		LatLng first;
		if(currPoint.getBdgpsLongitude()!=null&&currPoint.getBdgpsLongitude()!=0.0)
			first = new LatLng(currPoint.getBdgpsLatitude(),currPoint.getBdgpsLongitude());
		else
		    first = new LatLng(currPoint.getGpsLatitude(),currPoint.getGpsLongitude());
		
		Double currPointSpeed = currPoint.getGpsSpeed()!=null?currPoint.getGpsSpeed():0;
		
		BitmapDescriptor bitmap = NewContentFragment.getImgByState(String.valueOf(currPointSpeed), topPoint.getGpsTime(), currPoint.getGpsTime());
		
		// 构建MarkerOption，用于在地图上添加Marker
		float cou = (float) (0.0F-currPoint.getGpsCourse()  );
//		float cou = (float) (currPoint.getGpsCourse()-0.0f);
		
		BitmapDescriptor baseBitmapOrg = BitmapDescriptorFactory
				.fromResource(R.drawable.kuang2);
		
		bitmap = BitmapDescriptorFactory.fromBitmap(Util.first(getResources(),bitmap.getBitmap(),cou,baseBitmapOrg.getBitmap()));
		
		OverlayOptions option = new MarkerOptions().position(first)
				.icon(bitmap).anchor(0.5f, 0.5f).title("船名");
//		OverlayOptions option = new MarkerOptions().position(first)
//				.icon(bitmap).rotate(cou);
		marker = (Marker) (this.bmHistory.addOverlay(option));
		option = null;
		bitmap = null;
	}

	private void removeMarker() {
		if (marker != null)
			marker.remove();
	}
	@Override
	public void onPause() {
		super.onPause();
		mapHistory.onPause();
		mWakeLock.release();
	}
	@Override
	public void onResume() {
		super.onResume();
		mapHistory.onResume();
		mWakeLock.acquire(); 
	}


	private void initMap() {
		com.baidu.mapapi.map.MapView.setCustomMapStylePath(ContentFragment.getAssetsCacheFile(getActivity(),"baidu_custom_config"));
		mapHistory = (MapView) getActivity().findViewById(R.id.mapHistory);
//		mapHistory.setCustomMapStylePath(ContentFragment.getAssetsCacheFile(getActivity(),"baidu_custom_config"));
		
		// 不显示地图缩放控件（按钮控制栏）
		mapHistory.showZoomControls(false);
		// 不显示地图上比例尺
//				mapHistory.showScaleControl(false);
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
		
		msu = MapStatusUpdateFactory.zoomTo(z);
		this.bmHistory.setMapStatus(msu);
		this.bmHistory.setOnMarkerClickListener(this);
		this.bmHistory.setOnMapClickListener(this);
		
		bmHistory.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

			@Override
			public void onMapStatusChange(MapStatus arg0) {
			}

			@Override
			public void onMapStatusChangeFinish(MapStatus status) {
				z = status.zoom;                
//		    	if(Math.abs(NearActivity.this.zoom-zoom)> 0.5){
//
//		    		if(marker != null){
//		    			marker.remove();  
//		    		}
//		    		marker = BaiduMapHelper.addMarker(mBaiduMap, 
//		    				Double.parseDouble(sharedPreferences.getString("Lat", "0")), 
//		    				Double.parseDouble(sharedPreferences.getString("Lng", "0")), 
//		    				"我的位置",
//		    				R.drawable.marker_me, true);
//		    		
//		    		NearActivity.this.zoom =zoom;                    
//                    Log.d("zoom","缩放起了变化，现在缩放等级为"+zoom);                
//                }
				
			}

			@Override
			public void onMapStatusChangeStart(MapStatus arg0) {
			}  });
		
		NewContentFragment.mapAddPort(getActivity(),bmHistory);
	}
	
	// 初始化坐标数据
	protected synchronized void loadData(String startTime,String endTime) {
		
		Map<String, Object> apiParams = new HashMap<String, Object>();
		dataLoader.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				if(!isAdded())
					return;
				dialog = dialogUtil.loading("数据加载中", "请稍候...");
			}
			@Override
			public void onSuccess(final String arg2) {
				super.onSuccess(arg2);
				if(!isAdded())
					return;
				
				parseJsonThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						List<ShipGpsData> list = new ArrayList<ShipGpsData>();
						try {
							list = ParseJson.getHistoryGps(arg2);
							
							if (list.isEmpty()) {
								Message message = new Message();
								message.what = ParseJsonIsEmpty;
								handler.sendMessage(message);
							} else {
								if (list.size() >= 10000) {
									Message message = new Message();
									message.what = ParseJsonTooMuch;
									handler.sendMessage(message);
								} else {
									positionDatas.addAll(list);
									Message message = new Message();
									message.what = 1;
									handler.sendMessage(message);
								}
							}
						} catch (Exception e) {
							Message message = new Message();
							message.what = ParseJsonException;
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
				if(!isAdded())
					return;

				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(getActivity(), "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(getActivity(), "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(getActivity(), content,
							Toast.LENGTH_LONG).show();
				} else 
					Toast.makeText(getActivity(), "未知异常",
							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.historyGps + shipID + "&StartTime=" + startTime.replace(" ", "%20") + "&EndTime="
					+ endTime.replace(" ", "%20"), apiParams, "get");
	}

	public void setCenter(ShipGpsData pd, float z2) {

		double lat ;
		double lng ;
		if(pd.getBdgpsLongitude()!=null&&pd.getBdgpsLongitude()!=0.0){
			lat = pd.getBdgpsLatitude();
			lng = pd.getBdgpsLongitude();
		}else{

			lat = pd.getGpsLatitude();
			lng = pd.getGpsLongitude();
		}
		
		
//		if(setCenterFlag){
			setCenter(lat, lng, z2);
			setCenterFlag = false;
//		}
		
	}

	public void setCenter(double lat, double lng, float z2) {
		// 设定中心点坐标
//		LatLng cenpt =  MapConvertUtil.convertFromGPS(new LatLng(lat, lng));
		LatLng cenpt =  new LatLng(lat, lng);
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(z2)
				.build();

		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		// 改变地图状态
		this.bmHistory.animateMapStatus(mMapStatusUpdate);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.videoPauseImg:
			videoPauseImg.setVisibility(View.GONE);
			commonVideoView.playPause();
			break;

		default:
			break;
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
	private void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }
//	private void clearImageview(){
//		int size = channels.size();
//		if(size ==0)
//			return;
//		switch (size) {
//		case 1:
//			releaseImageViewResouce(img12);
//			break;
//		case 2:
//			releaseImageViewResouce(img21);
//			releaseImageViewResouce(img22);
//			break;
//		case 3:
//			releaseImageViewResouce(img31);
//			releaseImageViewResouce(img32);
//			releaseImageViewResouce(img34);
//			break;
//		case 4:
//			releaseImageViewResouce(img41);
//			releaseImageViewResouce(img42);
//			releaseImageViewResouce(img43);
//			releaseImageViewResouce(img44);
//			break;
//
//		default:
//			break;
//		}
//	}


	@Override
	public void endTimeChang(Calendar startCalendar, Calendar endCalendar) {
		mBinderService.stopThreadLoadImg();
		currPlayPosition = 0;
		bmHistory.clear();
		NewContentFragment.mapAddPort(getActivity(),bmHistory);
		shipImg.setVisibility(View.GONE);
//		clearImageview();
		commonVideoView.reLoadView();
		preClose();
		initData(CalendarUtil.toYYYY_MM_DD_HH_MM_SS(startCalendar), CalendarUtil.toYYYY_MM_DD_HH_MM_SS(endCalendar));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapHistory.onDestroy();
		mRedTexture.recycle();
		
		mBinderService.stopThreadLoadImg();
		getActivity().unbindService(connection);
		
		commonVideoView.reLoadView();
		preClose();
		if(parseJsonThread!=null){
			parseJsonThread.interrupt();
			parseJsonThread = null;
		}
		
		Intent intent = new Intent(getActivity(),ClearService.class); 
		getActivity().startService(intent);
	}

	private void preClose(){
		PlayGpsTimer.getInstance().stopPlayGpsTimer();
		CacheSeekBarTimer.getInstance().stopCacheSeekBarTimer();
		PlayListenerTimer.getInstance().stopPlayListenerTimer();
		GpsDataQueue.getInstance().clear();
		LoadedList.getInstance().clear();
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
	private void toggleImage(){
//		LayoutParams ps = shipPics.getLayoutParams();
//
//		if(scal == false){
//			ps.width = pt.x-30;
//			ps.height = ps.width;
//			scal = true;
//		}else{
//			scal = false;
//			ps.width = pt.x/3;
//			ps.height = ps.width;
//		}
//		shipPics.setLayoutParams(ps);
	}
	@Override
	public void fastChang() {
		if (positionDatas != null && !positionDatas.isEmpty()) {
			int size = positionDatas.size();
			int ss = size/playfast;
			String time = Util.secToTime(ss);
			commonVideoView.setVideoTotalTimeText(time);
			try {
				CacheSeekBarTimer.getInstance().resetTime(Long.valueOf(1000/playfast));
				PlayGpsTimer.getInstance().resetTime(Long.valueOf(1000/playfast));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void modChang() {
		if (positionDatas != null && !positionDatas.isEmpty()) {
			int size = positionDatas.size();
			int ss = size/playmod;
			String time = Util.secToTime(ss);
			commonVideoView.setVideoTotalTimeText(time);
			try {
				CacheSeekBarTimer.getInstance().resetTime(Long.valueOf(1000/playmod));
				PlayGpsTimer.getInstance().resetTime(Long.valueOf(1000/playmod));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void sloChang() {
		if (positionDatas != null && !positionDatas.isEmpty()) {
			int size = positionDatas.size();
			int ss = size/playslo;
			String time = Util.secToTime(ss);
			commonVideoView.setVideoTotalTimeText(time);
			try {
				CacheSeekBarTimer.getInstance().resetTime(Long.valueOf(1000/playslo));
				PlayGpsTimer.getInstance().resetTime(Long.valueOf(1000/playslo));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void playBigImg() {
		// TODO Auto-generated method stub
//		isPlayBigImg = true;
		
	}

	@Override
	public void playSmaImg() {
		// TODO Auto-generated method stub
//		isPlayBigImg = false;
		
	} 
	private class MyOnTouchListener implements OnTouchListener {
		private ImageView view;
		private int i;

		public MyOnTouchListener(int i,ImageView view) {
			this.view = view;
			this.i = i;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:// 手指压下屏幕
				showCustomDialog(i,view);
				break;
			}
			return false;
		}
	}
	private  void showCustomDialog(int i, ImageView view2) {
		// 初始化一个自定义的Dialog
		final CustomDialog.Builder b = new CustomDialog.Builder(getActivity());
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View view = inflater.inflate(R.layout.progress_dialog, null);

		LinearLayout ll_viewArea = (LinearLayout) view.findViewById(R.id.ll_viewArea);
		LinearLayout.LayoutParams parm = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		parm.gravity = Gravity.CENTER;

		// 自定义布局控件，用来初始化并存放自定义imageView
		viewArea = new ViewArea2(b,getActivity(),cPoint, i,view2);

		ll_viewArea.addView(viewArea, parm);

		b.setView(view);
		b.show();

		ll_viewArea.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewArea = null;
				b.dismiss();
			}
		});
	}

	@Override
	public void disPlayPauseImg() {
		videoPauseImg.setVisibility(View.VISIBLE);
	}

	@Override
	public void noDisPlayPauseImg() {
		videoPauseImg.setVisibility(View.GONE);
	}
}
