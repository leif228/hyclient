package com.hangyi.zd.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.http.cookie.Cookie;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.NewPageHomeMainActivity;
import com.eyunda.third.activities.map.MapPortOverlayManager;
import com.eyunda.third.activities.map.ShipOverlayManager;
import com.eyunda.third.domain.location.ShipCooordData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.CalendarUtil;
import com.eyunda.tools.DateUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.tools.ParseJson;
import com.hangyi.tools.Util;
import com.hangyi.zd.ClearService;
import com.hangyi.zd.PageHomeImgDownLoadService;
import com.hangyi.zd.activity.dialog.CustomDialog;
import com.hangyi.zd.activity.dialog.TouchView;
import com.hangyi.zd.activity.dialog.ViewArea;
import com.hangyi.zd.activity.gridviewpage.AppAdapter;
import com.hangyi.zd.domain.MapPortData;
import com.hangyi.zd.domain.NodeCode;
import com.hangyi.zd.domain.PoliceData;
import com.hangyi.zd.domain.ShipModelCode;
import com.hangyi.zd.domain.ShipModelData;
import com.hangyi.zd.domain.ShipModelNoData;
import com.hangyi.zd.domain.ShipVoyageData;
import com.hangyi.zd.domain.ShipVoyageNodeData;
import com.hangyi.zd.domain.UserPowerData;
import com.hangyi.zd.domain.UserPowerShipData;
import com.hangyi.zd.widge.TouchListener;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.ta.util.http.AsyncHttpResponseHandler;

public class NewContentFragment extends Fragment implements OnClickListener,
		OnMarkerClickListener, OnMapClickListener {
	
	Data_loader dataLoader;
	ImageLoader mImageLoader;
	TextView unread_msg_number;
	RelativeLayout history,msg,group,me;
	String userId;
	MapView mapHistory;
	BaiduMap bmHistory;
	UiSettings uisHistory;
	public static final LatLng BEIJING_LATLNG = new LatLng(23.038006, 113.509988);
	public static final String XML = "XML";
	public static final String mapPort = "mapPort";
	public static final int ZHUANGZHAI = 8;
	public static final int showShipImg = 15;
	public static final int showShipOverlay = 16;
	private static final String zzFinalStr = "装载量：0吨 ";
	private String zzStr = zzFinalStr;
	LinearLayout search_btn;
	EditText search_et;
	Timer shipCooordTimer;
	ConcurrentLinkedQueue<ShipCooordData> shipCooordDatas;
	boolean center = true;//第一次设置中心，以后不设置
	String searchName = "";
	
	DistributeTimerTask distributeTask;
	volatile List<OverlayOptions> optionsList;
	private LayoutInflater mInflater;
	private LinearLayout shipMoniter;
	private LinearLayout llNav2,shipls,shipzx;
	private LinearLayout shipImg,shipImg3,shipImg4,shipInfo3,shipInfo4;
	private TextView shipName;
	private TextView tv_zz4,tv_time4,tv_speed4,tv_lat4,tv_j4,tv_w4;
	private TextView tv_zz3,tv_time3,tv_speed3,tv_lat3,tv_j3,tv_w3;
	private ImageView img31,img32,img33;
	private ImageView img1;
	private ImageView img2;
	private ImageView img3;
	private ImageView img4;
	private ImageView gps;
	private ConcurrentHashMap<String,List<ShipModelNoData>> maplist = new ConcurrentHashMap<String,List<ShipModelNoData>>();// 船舶授权的镜头数
	private volatile ShipCooordData curShip;
	private LinearLayout search_ll;
	private LinearLayout shipgroupsz;
	private ShipOverlayManager overlayManager;
	private MyListener myListener; 
	private static ViewArea viewArea;
	private boolean isToasted = false;
	float zoom =11;
	static final float smallz =11;
	HXUpPopupWindow hxPopupWindow;
	LinearLayout llNav1;
	public static int unReadMsgs = 0;
	ZdUnReadMsgBroadcastReceiver receiver;
	Thread parseJsonThread = null;
	Thread parseJsonThread3 = null;
	Thread parseJsonThread4 = null;
	UserImgPowerThread userImgPowerThread = null;
	AllOverlayThread allOverlayThread = null;
	
	MapPortOverlayManager om = null;
	
	BitmapDescriptor mapspBitmap = BitmapDescriptorFactory.fromResource(R.drawable.mapsp);
	BitmapDescriptor baseBitmapOrg = BitmapDescriptorFactory.fromResource(R.drawable.kuang2);
	
	public static BitmapDescriptor mapsp1Bitmap = BitmapDescriptorFactory.fromResource(R.drawable.mapsp1);
	public static BitmapDescriptor ship_green1Bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ship_green1);
	public static BitmapDescriptor ship_greenBitmap = BitmapDescriptorFactory.fromResource(R.drawable.ship_green);
	public static BitmapDescriptor ship_yellow1Bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ship_yellow1);
	public static BitmapDescriptor ship_yellowBitmap = BitmapDescriptorFactory.fromResource(R.drawable.ship_yellow);
	public static BitmapDescriptor ship_red1Bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ship_red1);
	public static BitmapDescriptor ship_redBitmap = BitmapDescriptorFactory.fromResource(R.drawable.ship_red);
	public static BitmapDescriptor ship_black1Bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ship_black1);
	public static BitmapDescriptor ship_blackBitmap = BitmapDescriptorFactory.fromResource(R.drawable.ship_black);
	static Bitmap defaultBitmap = BitmapFactory.decodeResource(GlobalApplication.getInstance().getResources(), R.drawable.home_default);
	

	static File sdCardPath = Environment.getExternalStorageDirectory();// 获取SDCard目录
	static File cacheDir = new File(sdCardPath + ApplicationConstants.pageHomeImgCachePath);
	
	private PageHomeImgDownLoadService.ServiceBinder mBinderService;
	private ServiceConnection connection = new ServiceConnection() {  
		@Override  
		public void onServiceDisconnected(ComponentName name) {  
		}  
		
		@Override  
		public void onServiceConnected(ComponentName name, IBinder service) {  
			mBinderService = (PageHomeImgDownLoadService.ServiceBinder) service;
		}  
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if(!isAdded()){
				return;
			}
			switch (msg.what) {
			case 1:
				parseJsonThread = null;
				
				if(viewArea!=null){
					viewArea.setCurShip(curShip);
				}
				showAllOverlay(zoom);
				loadShipImg();
				break;
			case 7:
				parseJsonThread4 = null;
				
				if(unReadMsgs>0){
					unread_msg_number.setVisibility(View.VISIBLE);
					unread_msg_number.setText(String.valueOf(unReadMsgs));
				}else{
					unread_msg_number.setVisibility(View.GONE);
				}
				break;
			case ZHUANGZHAI:
				parseJsonThread3 = null;
				
				tv_zz3.setText(zzStr);
				tv_zz4.setText(zzStr);
				break;
//			case showShipImg:
//				showShipImg();
//				parseJsonThread3 = new Thread(new Runnable() {
//					@Override
//					public void run() {
//						loadDate2();
//					}
//				});
//				parseJsonThread3.setDaemon(true);
//				parseJsonThread3.start();
//				break;
			case showShipOverlay:
				allOverlayThread = null;
				
				float curz = (Float) msg.obj;
				
				if (optionsList.size() > 0) {
					
					overlayManager.removeFromMap();
					overlayManager.setData(optionsList);
					if(curz>smallz&&curShip!=null){
						currClickedMarker = overlayManager.addToMap2(curShip);
					}else{
						overlayManager.addToMap();
					}
					if (center) {
						overlayManager.zoomToSpan();
						center = false;
					}
				}
				
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		};
	};
	private Marker currClickedMarker;
	
	public interface MyListener   
    {  
        public void showMessage(String shipName);  
        public void hideBasePP();
    } 
	
	  @Override  
	    public void onAttach(Activity activity)   
	    {  
	        super.onAttach(activity);  
	     
	        myListener = (MyListener) activity;   
	    } 


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		SDKInitializer.initialize(getActivity().getApplicationContext());
		com.baidu.mapapi.map.MapView.setCustomMapStylePath(NewContentFragment.getAssetsCacheFile(getActivity(),"baidu_custom_config"));
		return inflater.inflate(R.layout.zd_ship_distribute_new, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		dataLoader = new Data_loader();
//		mImageLoader = ImageLoader.getInstance();
//		mImageLoader.init(GlobalApplication.getInstance().getImageLoaderConfiguration());
		
		optionsList = new ArrayList<OverlayOptions>();
		shipCooordDatas = new ConcurrentLinkedQueue<ShipCooordData>();
		initView();
		initMap();
		
		Intent bindIntent = new Intent(getActivity(), PageHomeImgDownLoadService.class);  
		getActivity().bindService(bindIntent, connection, Context.BIND_AUTO_CREATE);
		
//		initShipCooordData();
//		mapAddPort(getActivity(),bmHistory);
		
		// 注册接收消息广播
		receiver = new ZdUnReadMsgBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(
				"android.intent.action.ZdUnReadMsgBroadcast");
		// 设置广播的优先级别大于ChatAllHistoryAtivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
		intentFilter.setPriority(5);
		getActivity().registerReceiver(receiver, intentFilter);
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onStart() {
		ImageLoader.getInstance().destroy();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(GlobalApplication.getInstance().getImageLoaderConfiguration());
		
		parseJsonThread4 = new Thread(new Runnable() {
			@Override
			public void run() {
				loadUnReadMsgs();
			}
		});
		parseJsonThread4.setDaemon(true);
		parseJsonThread4.start();
		
		super.onStart();
	}
	class DistributeTimerTask extends TimerTask{

		@Override
		public void run() {
			loadDataOneMin();
		}
	};
	private void initView() { 
		llNav1 = (LinearLayout) getActivity().findViewById(R.id.llNav1);
		llNav2 = (LinearLayout) getActivity().findViewById(R.id.llNav2);
		shipls = (LinearLayout) getActivity().findViewById(R.id.shipls);
		shipzx = (LinearLayout) getActivity().findViewById(R.id.shipzx);
		shipgroupsz = (LinearLayout) getActivity().findViewById(R.id.shipgroupsz);
		shipgroupsz.setOnClickListener(this);
		shipls.setOnClickListener(this);
		shipzx.setOnClickListener(this);
		
		search_ll = (LinearLayout) getActivity().findViewById(R.id.llNav);
		search_btn = (LinearLayout) getActivity().findViewById(R.id.search_btn);
		search_et = (EditText) getActivity().findViewById(R.id.search_et);
		search_btn.setOnClickListener(this);
		
		shipMoniter = (LinearLayout) getActivity().findViewById(R.id.shipMoniter);
		shipMoniter.setOnClickListener(this);
		shipInfo3 = (LinearLayout) getActivity().findViewById(R.id.shipInfo3);
		shipInfo3.setOnClickListener(this);
		shipInfo4 = (LinearLayout) getActivity().findViewById(R.id.shipInfo4);
		shipInfo4.setOnClickListener(this);
		
		shipImg = (LinearLayout) getActivity().findViewById(R.id.shipImg);
		shipImg3 = (LinearLayout) getActivity().findViewById(R.id.shipImg3);
		shipImg4 = (LinearLayout) getActivity().findViewById(R.id.shipImg4);
		shipName = (TextView) getActivity().findViewById(R.id.shipName);
		tv_zz3 = (TextView) getActivity().findViewById(R.id.tv_zz3);
		tv_time3 = (TextView) getActivity().findViewById(R.id.tv_time3);
		tv_speed3 = (TextView) getActivity().findViewById(R.id.tv_speed3);
		tv_lat3 = (TextView) getActivity().findViewById(R.id.tv_lat3);
		tv_zz4 = (TextView) getActivity().findViewById(R.id.tv_zz4);
		tv_time4 = (TextView) getActivity().findViewById(R.id.tv_time4);
		tv_speed4 = (TextView) getActivity().findViewById(R.id.tv_speed4);
		tv_lat4 = (TextView) getActivity().findViewById(R.id.tv_lat4);
		tv_j3 = (TextView) getActivity().findViewById(R.id.tv_j3);
		tv_j4 = (TextView) getActivity().findViewById(R.id.tv_j4);
		tv_w3 = (TextView) getActivity().findViewById(R.id.tv_w3);
		tv_w4 = (TextView) getActivity().findViewById(R.id.tv_w4);
		unread_msg_number = (TextView) getActivity().findViewById(R.id.unread_msg_number);
		img1 = (ImageView) getActivity().findViewById(R.id.img1);
		img2 = (ImageView) getActivity().findViewById(R.id.img2);
		img3 = (ImageView) getActivity().findViewById(R.id.img3);
		img4 = (ImageView) getActivity().findViewById(R.id.img4);
		
		img31 = (ImageView) getActivity().findViewById(R.id.img31);
		img32 = (ImageView) getActivity().findViewById(R.id.img32);
		img33 = (ImageView) getActivity().findViewById(R.id.img33);
		
		history = (RelativeLayout) getActivity().findViewById(R.id.history);
		msg = (RelativeLayout) getActivity().findViewById(R.id.msg);
		group = (RelativeLayout) getActivity().findViewById(R.id.group);
		me = (RelativeLayout) getActivity().findViewById(R.id.me);
		history.setOnClickListener(this);
		msg.setOnClickListener(this);
		group.setOnClickListener(this);
		me.setOnClickListener(this);
		
		gps = (ImageView) getActivity().findViewById(R.id.gps);
		gps.setOnClickListener(this);
		
	}
	private void showGpsImg(){
		gps.setVisibility(View.VISIBLE);
	}
	private void hideGpsImg(){
		gps.setVisibility(View.GONE);
	}
	
	public void hideSearch(){
		search_ll.setVisibility(View.GONE);
		shipMoniter.setVisibility(View.GONE);
		
	}
	public void hideDownBtn(){
		llNav2.setVisibility(View.GONE);
		shipls.setVisibility(View.GONE);
		shipzx.setVisibility(View.GONE);
		
	}
	private void initShipCooordData(){
		
		if (shipCooordTimer != null) {
			if (distributeTask != null) {
				distributeTask.cancel(); // 将原任务从队列中移除
			}
		}else{
			shipCooordTimer = new Timer(true);
		}
		distributeTask = new DistributeTimerTask();
		shipCooordTimer.schedule(distributeTask,0,1*60*1000); 
	}
	
	   public static String getAssetsCacheFile(Context context,String fileName)   {
	        File cacheFile = new File(context.getCacheDir(), fileName);
	        try {
	            InputStream inputStream = context.getAssets().open(fileName);
	            try {
	                FileOutputStream outputStream = new FileOutputStream(cacheFile);
	                try {
	                    byte[] buf = new byte[1024];
	                    int len;
	                    while ((len = inputStream.read(buf)) > 0) {
	                        outputStream.write(buf, 0, len);
	                    }
	                } finally {
	                    outputStream.close();
	                }
	            } finally {
	                inputStream.close();
	            }
	        } catch (IOException e) {
	           e.printStackTrace();
	        }
	        return cacheFile.getAbsolutePath();
	    }

	private void initMap() {
		mapHistory = (MapView) getActivity().findViewById(R.id.mapHistory);
//		Uri uri=Uri.parse("android:resource://"+getActivity().getPackageName()+"/"+R.raw.baidu_custom_config);
//		mapHistory.setCustomMapStylePath(getAssetsCacheFile(getActivity(),"baidu_custom_config"));
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
		msu = MapStatusUpdateFactory.zoomTo(zoom);
		this.bmHistory.setMapStatus(msu);
		this.bmHistory.setOnMarkerClickListener(this);
		this.bmHistory.setOnMapClickListener(this);
		
		om = new MapPortOverlayManager(bmHistory);
		overlayManager = new ShipOverlayManager(bmHistory);
		
		bmHistory.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

			@Override
			public void onMapStatusChange(MapStatus arg0) {
			}

			@Override
			public void onMapStatusChangeFinish(MapStatus status) {
				if(status.zoom>smallz&&NewContentFragment.this.zoom<=smallz){
					mapAddPort2(getActivity(),bmHistory);
					showAllOverlay(status.zoom); 
				}else if(status.zoom<=smallz&&NewContentFragment.this.zoom>smallz){
					if(om!=null)
						om.removeFromMap();
					showAllOverlay(status.zoom);                
				}
				NewContentFragment.this.zoom = status.zoom;
				
			}

			@Override
			public void onMapStatusChangeStart(MapStatus arg0) {
			}  });
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.search_btn:
			String shipName = search_et.getText().toString().trim();
			if(TextUtils.isEmpty(shipName)){
				Toast.makeText(getActivity(), "请输入船名", Toast.LENGTH_LONG).show();
				return;
			}
			
//			intent = new Intent(getActivity(), MyshipActivity.class);
//			startActivity(intent);
			break;
		case R.id.shipMoniter:
//			intent = new Intent(getActivity(), AllAppListActivity.class);
//			startActivity(intent);
			intent = new Intent(getActivity(), MainActivity.class);
			startActivity(intent);
			break;
		case R.id.shipls:
//			intent = new Intent(getActivity(), AllAppListActivity.class);
//			startActivity(intent);
//			intent = new Intent(getActivity(), PayActivity.class);
//			startActivity(intent);
			startActivity(new Intent(getActivity(),
					LSShipListActivity.class));
			break;
		case R.id.shipzx:
			intent = new Intent(getActivity(), ShipHCListIngActivity.class);
			startActivity(intent);
			break;
		case R.id.shipgroupsz:
			intent = new Intent(getActivity(), ShipGroupActivity.class);
			startActivity(intent);
			break;
		case R.id.history:
			if(curShip != null){
				startActivity(new Intent(getActivity(),ZdShipDynamicActivity.class)
				.putExtra("shipID", curShip.getShipID())
				.putExtra("shipName", curShip.getShipName())
				.putExtra(ApplicationConstants.historyLineType, ApplicationConstants.historyLineNormal));
			}else{
				startActivity(new Intent(getActivity(),
						LSShipListActivity.class));
			}
			break;
		case R.id.msg:
			startActivity(new Intent(getActivity(),
					PoliceActivity.class));
			break;
		case R.id.group:
//			intent = new Intent(getActivity(), NewShipGroupActivity.class);
//			startActivity(intent);
//			intent = new Intent(getActivity(), TestImageBase64Activity.class);
//			startActivity(intent);
//			intent = new Intent(getActivity(),NewShipGroupActivity.class);
//			getActivity().startActivityForResult(intent, 1);
			
//			hxPopupWindow.togglePopWindow(v);//启用的
			startActivity(new Intent(getActivity(),
			ShipHCActivity.class));
			break;
		case R.id.me:
			startActivity(new Intent(getActivity(),
					AccountInfoActivity.class));
			
//			startActivity(new Intent(getActivity(),
//					PayActivity.class));// 导向个人中心
			break;
		case R.id.shipInfo3:
			startActivity(new Intent(getActivity(),ShipInfoActivity.class).putExtra("shipData", curShip));
			break;
		case R.id.shipInfo4:
			startActivity(new Intent(getActivity(),ShipInfoActivity.class).putExtra("shipData", curShip));
			break;
		case R.id.gps:
			if(curShip!=null)
				setCenter(curShip);
			break;
		}

	}

	@Override
	public void onMapClick(LatLng arg0) {
		myListener.hideBasePP();
	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		return false;
	}
	public void hideCurrShip(){
		hideShipImg();
		if(currClickedMarker!=null)
			addNormalMarker(curShip);
		curShip=null;
		
	}
	private void showShipName(String shipName, LatLng ll){
		//创建InfoWindow展示的view  
        mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewCache =  mInflater.inflate(R.layout.zd_popup_window2, null);
        TextView tv_name = (TextView) viewCache.findViewById(R.id.tv_name);
        tv_name.setText(shipName);
        
    	//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量 
    	InfoWindow mInfoWindow = new InfoWindow(viewCache, ll, -100);  
    	//显示InfoWindow  
    	bmHistory.showInfoWindow(mInfoWindow);
	}
//	private void showCurrShip(final ShipCooordData curShip, LatLng ll){
//		//创建InfoWindow展示的view  
//		mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View viewCache =  mInflater.inflate(R.layout.zd_popup_window, null);
//		TextView tv_name = (TextView) viewCache.findViewById(R.id.tv_name);
//		tv_name.setText("船名："+curShip.getShipName());
//		TextView tv_lat = (TextView) viewCache.findViewById(R.id.tv_lat);
//		tv_lat.setText("航向："+curShip.getGpsCourse());
//		TextView tv_speed = (TextView) viewCache.findViewById(R.id.tv_speed);
//		tv_speed.setText("航速："+curShip.getGpsSpeed()+"节");
//		TextView tv_time= (TextView) viewCache.findViewById(R.id.tv_time);
//		tv_time.setText("最后时间："+curShip.getGpsTime());
//		LinearLayout more= (LinearLayout) viewCache.findViewById(R.id.more);
//		more.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(getActivity(),ShipInfoActivity.class).putExtra("shipData", curShip));
//			}
//		});
//		
//		
//		//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量 
//		InfoWindow mInfoWindow = new InfoWindow(viewCache, ll, -100);  
//		//显示InfoWindow  
//		bmHistory.showInfoWindow(mInfoWindow);
//		
//		setCenter(curShip,13);
//		
//		startShipImgTimer(curShip);
//	}
	
	private void addNormalMarker(ShipCooordData scd){
		if(scd == null)
			return;
		
		Double currPointSpeed = (!"".equals(scd.getGpsSpeed()))?Double.valueOf(scd.getGpsSpeed()):0;
		Double currPointCourse = (!"".equals(scd.getGpsCourse()))?Double.valueOf(scd.getGpsCourse()):0;
//		float cou = (float) (currPointCourse - 0.0F);
		float cou = (float) (0.0F - currPointCourse);
		
		BitmapDescriptor bitmap = NewContentFragment.getImgByState(String.valueOf(currPointSpeed), scd.getGpsTime(), null);
		
		bitmap = BitmapDescriptorFactory.fromBitmap(Util.createDrawable(getActivity(),bitmap.getBitmap(),scd.getShipName()));
		
		if(currClickedMarker!=null){
			currClickedMarker.setIcon(bitmap);
			currClickedMarker.setRotate(cou);
			currClickedMarker.setAnchor(0.5f, 0.5f);
		}
		
	}
	
	private void addClickedMarker(ShipCooordData scd) {
		if(scd == null)
			return;
		
		Double currPointSpeed = (!"".equals(scd.getGpsSpeed()))?Double.valueOf(scd.getGpsSpeed()):0;
		Double currPointCourse = (!"".equals(scd.getGpsCourse()))?Double.valueOf(scd.getGpsCourse()):0;
		
		BitmapDescriptor bitmap = NewContentFragment.getImgByState(String.valueOf(currPointSpeed), scd.getGpsTime(), null);
		
		// 构建MarkerOption，用于在地图上添加Marker
//		float cou = (float) (0-currPointCourse);
		float cou = (float) (currPointCourse-0f);
		
		bitmap = BitmapDescriptorFactory.fromBitmap(Util.createDrawable(getActivity(),Util.first(getResources(),bitmap.getBitmap(),cou,baseBitmapOrg.getBitmap()),scd.getShipName()));
		
		for(Overlay overlay:overlayManager.getmOverlayList()){
			if (overlay instanceof Marker) {
                if(((Marker) overlay).getTitle().equals(scd.getShipName())){
                	((Marker) overlay).setIcon(bitmap);
                	((Marker) overlay).setRotate(0);
                	((Marker) overlay).setAnchor(0.5f, 0.5f);
                	currClickedMarker = (Marker) overlay;
                	
                	break;
                }
            }
		}
	}
	
	class UserImgPowerThread extends Thread {

		@Override
		public void run() {
			List<ShipModelNoData> list = maplist.get(curShip.getShipID());
			if(list == null){
				
				SharedPreferences sp = getActivity().getSharedPreferences(ApplicationConstants.UserPowerData_SharedPreferences, Context.MODE_PRIVATE);
				String object = sp.getString("UserPower", "");
				
				UserPowerData data = null;
				if(!"".equals(object)){
					Gson gson = new Gson();
					data = gson.fromJson(object, new TypeToken<UserPowerData>() {}.getType());
				}else
					data = new UserPowerData();
				
				flag:
					for(UserPowerShipData upsd:data.getUserPowerShipDatas()){
						if(upsd.getShipID().equals(curShip.getShipID())){
							for(ShipModelData smd:upsd.getShipModels()){
								if(smd.getModel() == ShipModelCode.five){
									maplist.put(curShip.getShipID(), smd.getModelNolist()) ;
									break flag;
								}
							}
						}
					}
			}
			Message message = new Message();
			message.what = showShipImg;
			handler.sendMessage(message);
		}
		
	}

	private void loadShipImg() {
		if(curShip!=null){
//			userImgPowerThread = new UserImgPowerThread();
//			userImgPowerThread.start();
			
			List<ShipModelNoData> list = maplist.get(curShip.getShipID());
			if(list == null){
				
				SharedPreferences sp = getActivity().getSharedPreferences(ApplicationConstants.UserPowerData_SharedPreferences, Context.MODE_PRIVATE);
				String object = sp.getString("UserPower", "");
				
				UserPowerData data = null;
				if(!"".equals(object)){
					Gson gson = new Gson();
					data = gson.fromJson(object, new TypeToken<UserPowerData>() {}.getType());
				}else
					data = new UserPowerData();
				
				flag:
					for(UserPowerShipData upsd:data.getUserPowerShipDatas()){
						if(upsd.getShipID().equals(curShip.getShipID())){
							for(ShipModelData smd:upsd.getShipModels()){
								if(smd.getModel() == ShipModelCode.five){
									List<ShipModelNoData> smnds = smd.getModelNolist();
									// 排序
									Collections.sort(smnds, new Comparator<ShipModelNoData>() {

										@Override
										public int compare(ShipModelNoData lhs, ShipModelNoData rhs) {
											return Integer.valueOf((lhs.getModelNo())).compareTo(
													Integer.valueOf((rhs.getModelNo())));
										}
									});
									
									maplist.put(curShip.getShipID(), smnds) ;
									break flag;
								}
							}
						}
					}
			}
			showShipImg();
			
			parseJsonThread3 = new Thread(new Runnable() {
				@Override
				public void run() {
					loadDate2();
				}
			});
			parseJsonThread3.setDaemon(true);
			parseJsonThread3.start();
		}
		
	}
	private void showShipImg(){
		if(curShip == null)
			 return;
		
		List<ShipModelNoData> list = maplist.get(curShip.getShipID());
		
		myListener.showMessage(curShip.getShipName());
		
		if(list!=null&&list.size()>0){
			int size = list.size();
			if(size>4){
				Toast.makeText(getActivity(), "目前最多只支持四个镜头显视。", Toast.LENGTH_LONG).show();
				return;
			}
			this.showGpsImg();
			
			shipImg.setVisibility(View.VISIBLE);
			shipName.setText(curShip.getShipName());
			if(size == 3){
				shipImg3.setVisibility(View.VISIBLE);
				shipImg4.setVisibility(View.GONE);
				tv_time3.setText("最后时间："+curShip.getGpsTime());
				tv_speed3.setText("航速："+curShip.getGpsSpeed()+"节");
				tv_zz3.setText(zzStr);
				tv_lat3.setText("航向："+curShip.getGpsCourse());
				tv_j3.setText("经度："+curShip.getGpsLongitude());
				tv_w3.setText("纬度："+curShip.getGpsLatitude());
				
				img31.setOnTouchListener(new MyOnTouchListener(curShip,Integer.valueOf(list.get(0).getModelNo()),img31));
				displayImage(mImageLoader,getActivity(),img31,curShip,Integer.valueOf(list.get(0).getModelNo()));
				img32.setOnTouchListener(new MyOnTouchListener(curShip,Integer.valueOf(list.get(1).getModelNo()),img32));
				displayImage(mImageLoader,getActivity(),img32,curShip,Integer.valueOf(list.get(1).getModelNo()));
				img33.setOnTouchListener(new MyOnTouchListener(curShip,Integer.valueOf(list.get(2).getModelNo()),img33));
				displayImage(mImageLoader,getActivity(),img33,curShip,Integer.valueOf(list.get(2).getModelNo()));
			}else{
				shipImg3.setVisibility(View.GONE);
				shipImg4.setVisibility(View.VISIBLE);
				tv_time4.setText("最后时间："+curShip.getGpsTime());
				tv_zz4.setText(zzStr);
				tv_speed4.setText("航速："+curShip.getGpsSpeed()+"节");
				tv_lat4.setText("航向："+curShip.getGpsCourse());
				tv_j4.setText("经度："+curShip.getGpsLongitude());
				tv_w4.setText("纬度："+curShip.getGpsLatitude());
				
				for (int i = 1; i <= size; i++) {
//					if(size == 1) {
//						img2.setVisibility(View.VISIBLE);
//						img2.setOnTouchListener(new MyOnTouchListener(curShip,Integer.valueOf(list.get(i-1).getModelNo()),img2));
//						
//						displayImage(mImageLoader,getActivity(),img2,curShip,Integer.valueOf(list.get(i-1).getModelNo()));
//					} else if(size == 3){
//						if(i ==3){
//							img4.setVisibility(View.VISIBLE);
//							img4.setOnTouchListener(new MyOnTouchListener(curShip,Integer.valueOf(list.get(i-1).getModelNo()),img4));
//							
//							displayImage(mImageLoader,getActivity(),img4,curShip,Integer.valueOf(list.get(i-1).getModelNo()));
//							break;
//						}
//						int view_id = getResources().getIdentifier("img"+i, "id",  getActivity().getPackageName());
//						ImageView view = (ImageView) getActivity().findViewById(view_id);
//						view.setVisibility(View.VISIBLE);
//						view.setOnTouchListener(new MyOnTouchListener(curShip,Integer.valueOf(list.get(i-1).getModelNo()),view));
//						
//						displayImage(mImageLoader,getActivity(),view,curShip,Integer.valueOf(list.get(i-1).getModelNo()));
//					}else {
						
						int view_id = getResources().getIdentifier("img"+i, "id",  getActivity().getPackageName());
						ImageView view = (ImageView) getActivity().findViewById(view_id);
						view.setVisibility(View.VISIBLE);
						view.setOnTouchListener(new MyOnTouchListener(curShip,Integer.valueOf(list.get(i-1).getModelNo()),view));
						
						displayImage(mImageLoader,getActivity(),view,curShip,Integer.valueOf(list.get(i-1).getModelNo()));
						
						hiddenOtherImgView(size);
//					}
				}
			}
		}else{
			shipImg.setVisibility(View.GONE);
			if(!isToasted){
				Toast.makeText(getActivity(), "没有查看该船舶镜头权限！", Toast.LENGTH_LONG).show();
				isToasted = true;
			}
		}
	}
	
	private void hiddenOtherImgView(int size) {
		switch (size) {
		case 1:
			img2.setImageResource(R.drawable.home_default);
			img3.setImageResource(R.drawable.home_default);
			img4.setImageResource(R.drawable.home_default);
			break;
		case 2:
			img3.setImageResource(R.drawable.home_default);
			img4.setImageResource(R.drawable.home_default);
			break;
		case 3:
			img4.setImageResource(R.drawable.home_default);
			break;

		default:
			break;
		}
	}

	public static void displayClickBigImage(ImageLoader mImageLoader,
			Context mContext, TouchView view, ShipCooordData curShip, int i) {
		if (curShip == null)
			return;

		File shipCacheDir = new File(cacheDir + "/" + curShip.getShipID());
		File shipChannelCacheImg = new File(shipCacheDir + "/" + i + ".png");
		if (shipChannelCacheImg.exists()) {
			Bitmap b = Util.getLoacalBitmap(shipChannelCacheImg.getAbsolutePath());
			if (b != null) {
				view.setImageDrawable(AppAdapter.createDrawable(mContext, b, null, curShip.getGpsTime()));
				return;
			}
		}

		view.setImageDrawable(AppAdapter.createDrawable(mContext, defaultBitmap, null, curShip.getGpsTime()));
	}
	
	public static void displayImageForPolice(ImageLoader mImageLoader,final Context mContext,ImageView view,final ShipCooordData curShip,final int i ){
		if(curShip==null)
			return;
		mImageLoader.displayImage(ApplicationConstants.ZDPHP_PRE_URL+ ApplicationUrls.loadImg+curShip.getShipID()+"&Channel="+i+"&PictureTime="+ getUrlTimeStr(curShip.getGpsTime()),
				view,
				NewPageHomeMainActivity.options,new ImageLoadingListener(){
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//				((ImageView)view).setImageDrawable(AppAdapter.createDrawable(getActivity(),loadedImage,null,DateUtils.getTime("yyyy-MM-dd HH:mm:ss")));
				((ImageView)view).setImageDrawable(AppAdapter.createDrawable(mContext,loadedImage,null,curShip.getGpsTime()));
				
				File shipCacheDir = new File(cacheDir + "/" +curShip.getShipID());
				if (!shipCacheDir.exists()) {  
					shipCacheDir.mkdirs();  
				}
				File shipChannelCacheImg = new File(shipCacheDir + "/" + i + ".png");
				if (shipChannelCacheImg.exists()) {  
					shipChannelCacheImg.delete();  
				}
				Util.saveMyBitmap2(loadedImage, shipChannelCacheImg);
			}
			
			@Override
			public void onLoadingFailed(String arg0, View view,
					FailReason arg2) {
//				((ImageView)view).setDrawingCacheEnabled(true);
//				((ImageView)view).setImageDrawable(AppAdapter.createDrawable(mContext,((ImageView)view).getDrawingCache(),null,curShip.getGpsTime()));
//				((ImageView)view).setDrawingCacheEnabled(false);
				
				((ImageView)view).setDrawingCacheEnabled(true);
				((ImageView)view).setImageDrawable(AppAdapter.createDrawable(mContext,((ImageView)view).getDrawingCache(),null,curShip.getGpsTime()));
				((ImageView)view).setDrawingCacheEnabled(false);
			}
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}});
	}
	public static void displayImage(ImageLoader mImageLoader,final Context mContext,ImageView view,final ShipCooordData curShip,final int i ){
		if(curShip==null)
			return;
		mImageLoader.displayImage(ApplicationConstants.ZDPHP_PRE_URL+ ApplicationUrls.loadImg+curShip.getShipID()+"&Channel="+i+"&PictureTime="+ getUrlTimeStr(curShip.getGpsTime()),
				view,
				NewPageHomeMainActivity.options,new ImageLoadingListener(){

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//				((ImageView)view).setImageDrawable(AppAdapter.createDrawable(getActivity(),loadedImage,null,DateUtils.getTime("yyyy-MM-dd HH:mm:ss")));
				((ImageView)view).setImageDrawable(AppAdapter.createDrawable(mContext,loadedImage,null,curShip.getGpsTime()));
				
				File shipCacheDir = new File(cacheDir + "/" +curShip.getShipID());
				if (!shipCacheDir.exists()) {  
					shipCacheDir.mkdirs();  
				}
				File shipChannelCacheImg = new File(shipCacheDir + "/" + i + ".png");
				if (shipChannelCacheImg.exists()) {  
					shipChannelCacheImg.delete();  
				}
				Util.saveMyBitmap2(loadedImage, shipChannelCacheImg);
				
				loadBigImage(viewArea, i);
			}

			@Override
			public void onLoadingFailed(String arg0, View view,
					FailReason arg2) {
//				((ImageView)view).setDrawingCacheEnabled(true);
//				((ImageView)view).setImageDrawable(AppAdapter.createDrawable(mContext,((ImageView)view).getDrawingCache(),null,curShip.getGpsTime()));
//				((ImageView)view).setDrawingCacheEnabled(false);
				
				File shipCacheDir = new File(cacheDir + "/" +curShip.getShipID());
				File shipChannelCacheImg = new File(shipCacheDir + "/" + i + ".png");
				if (shipChannelCacheImg.exists()) { 
					Bitmap b = Util.getLoacalBitmap(shipChannelCacheImg.getAbsolutePath());
					if(b!=null){
						((ImageView)view).setDrawingCacheEnabled(true);
						((ImageView)view).setImageDrawable(AppAdapter.createDrawable(mContext,b,null,curShip.getGpsTime()));
						((ImageView)view).setDrawingCacheEnabled(false);
						
						return;
					}
				}
				
				((ImageView)view).setDrawingCacheEnabled(true);
				((ImageView)view).setImageDrawable(AppAdapter.createDrawable(mContext,((ImageView)view).getDrawingCache(),null,curShip.getGpsTime()));
				((ImageView)view).setDrawingCacheEnabled(false);
				
				loadBigImage(viewArea, i);
				 
			}

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}});
	}
	private static void loadBigImage(ViewArea viewArea, int i){
		if(viewArea!=null){
			if(i==viewArea.getI())
				viewArea.changImg();
		}
	}

	private void hideShipImg() {
		shipImg.setVisibility(View.GONE);
		shipImg3.setVisibility(View.GONE);
		shipImg4.setVisibility(View.GONE);
		
		this.hideGpsImg();
	}
	
	protected void loadDate2() {
		if(curShip == null)
			return;

		String PHPSESSID = "";
        List<Cookie> list = GlobalApplication.getInstance().getCookies();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("PHPSESSID")) {
            	PHPSESSID = cookie.getValue();
            }
        }
		Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("shipId", curShip.getShipID());
		
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
					zzStr = zzFinalStr;
					
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
							
							for(int i=list.size()-1;i>=0;i--){
								ShipVoyageNodeData iData = list.get(i);
								if(Integer.valueOf(iData.getStage())>NodeCode.unloadingEnd.getN())
									continue;
								if(Integer.valueOf(iData.getStage())==NodeCode.unloadingEnd.getN())
									break;
								
								if(String.valueOf(NodeCode.loadingEnd.getN()).equals(iData.getStage())){
									zzStr = "装载量："+iData.getValue()+"吨 ";;
									
									break;
								}
							}
						}
					}
					
				} else {
					zzStr = zzFinalStr;
//					Toast.makeText(ShipHCListIngActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();
				}
				Message message = new Message();
				message.what = ZHUANGZHAI;
				handler.sendMessage(message);
				
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				
				zzStr = zzFinalStr;
				Message message = new Message();
				message.what = ZHUANGZHAI;
				handler.sendMessage(message);
			}
		}, ApplicationUrls.currHCByShipId+PHPSESSID, apiParams, "get");	
		
		
	}

	private boolean setCenterFlag = true;

	protected synchronized void loadDataOneMin() {
		Map<String, Object> apiParams = new HashMap<String, Object>();
		
		dataLoader.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(final String arg2) {
				super.onSuccess(arg2);
				if(!isAdded()){
					return;
				}
				parseJsonThread = new Thread(new Runnable() {
					@Override
					public void run() {
//						shipCooordDatas.clear();
						ArrayList<ShipCooordData> shipCooordDatas2 = ParseJson.parserHome(arg2);
						
						if(shipCooordDatas2.size()>0){
							SharedPreferences sp = getActivity().getSharedPreferences(ApplicationConstants.SearchShipData_SharedPreferences, Context.MODE_PRIVATE);
							Editor editor = sp.edit();
							Gson gson = new Gson();
							String s = gson.toJson(shipCooordDatas2);
							editor.putString("SearchShipData", s);
							editor.commit();
							
//							mBinderService.startThreadLoadImg(shipCooordDatas2);
							
							if(curShip!=null){
								for(ShipCooordData scd:shipCooordDatas2){
									if(curShip.getShipID().equals(scd.getShipID())){
										curShip = scd;
										break;
									}
								}
							}
							
							shipCooordDatas.clear();
							shipCooordDatas.addAll(shipCooordDatas2);
							
						}
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						
//						else{
//							loadCache();
//						}
					}
					
				});
				parseJsonThread.setDaemon(true);
				parseJsonThread.start();
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
//				parseJsonThread = new Thread(new Runnable() {
//					@Override
//					public void run() {
//						shipCooordDatas.clear();
//						
//						loadCache();
//		
//						Message message = new Message();
//						message.what = 1;
//						handler.sendMessage(message);
//					}
//					
//				});
//				parseJsonThread.setDaemon(true);
//				parseJsonThread.start();

//				if (content != null && content.equals("can't resolve host"))
//					Toast.makeText(getActivity(), "网络连接异常",
//							Toast.LENGTH_LONG).show();
//				else if (content != null && content.equals("socket time out")) {
//					Toast.makeText(getActivity(), "连接服务器超时",
//							Toast.LENGTH_LONG).show();
//				} else if (content != null) {
//					Toast.makeText(getActivity(), content,
//							Toast.LENGTH_LONG).show();
//				} else
//					Toast.makeText(getActivity(), "未知异常",
//							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.pageHomeGps+getCurrTimeStr(), apiParams, "get");
	}
	
	private void loadCache(){
		SharedPreferences sp = getActivity().getSharedPreferences("SearchShipData", Context.MODE_PRIVATE);
		String strData = sp.getString("SearchShipData", "");
		
		if(!strData.equals("")){
			Gson gson = new Gson();
			List<ShipCooordData> rmap = gson.fromJson(
					strData, new TypeToken<List<ShipCooordData>>() {
					}.getType());
			
			shipCooordDatas.addAll(rmap);
		}
	}
	
	public static String getCurrTimeStr(){
		String day = DateUtils.getTime("yyyy-MM-dd HH:mm:ss");
		String[] arr = day.split(" ");
		String[] arr2 = arr[1].split(":");
		day = arr[0] + "%20" + arr2[0]+":"+arr2[1]+":"+"00";
		return day;
	}
	public static String getUrlTimeStr(String time){
		time = time.replace(" ", "%20");
		return time;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapHistory.onDestroy();
		getActivity().unregisterReceiver(receiver);
		
		if(parseJsonThread!=null){
			parseJsonThread.interrupt();
			parseJsonThread = null;
		}
		if(parseJsonThread3!=null){
			parseJsonThread3.interrupt();
			parseJsonThread3 = null;
		}
		if(parseJsonThread4!=null){
			parseJsonThread4.interrupt();
			parseJsonThread4 = null;
		}
		if(userImgPowerThread!=null){
			userImgPowerThread.interrupt();
			userImgPowerThread = null;
		}
		if(allOverlayThread!=null){
			allOverlayThread.interrupt();
			allOverlayThread = null;
		}
		
		if(shipCooordTimer!=null){
			
			shipCooordTimer.cancel();
			shipCooordTimer.purge();
			shipCooordTimer = null;
		}
		mBinderService.stopThreadLoadImg();
		getActivity().unbindService(connection);
		
		Intent intent = new Intent(getActivity(),ClearService.class); 
		getActivity().startService(intent);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mapHistory.onPause();
		
		if (shipCooordTimer != null) {
			if (distributeTask != null) {
				distributeTask.cancel(); // 将原任务从队列中移除
				distributeTask = null;
			}
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		mapHistory.onResume();
		
		if (shipCooordTimer != null) {
			if (distributeTask != null) {
				distributeTask.cancel(); // 将原任务从队列中移除
			}
		}else{
			shipCooordTimer = new Timer(true);
		}
		distributeTask = new DistributeTimerTask();
		shipCooordTimer.schedule(distributeTask,0,1*60*1000); 
		
	}
	public void setCenter(ShipCooordData pd) {
		if(pd==null)
			return;
//		if(setCenterFlag){
		LatLng point ;
		if(pd.getBdLatitude()!=null&&!pd.getBdLatitude().equals(""))
			point = new LatLng(Double.valueOf(pd.getBdLatitude()),Double.valueOf(pd.getBdLongitude()));
		else
			point = new LatLng(Double.valueOf(pd.getGpsLatitude()),Double.valueOf(pd.getGpsLongitude()));
			// 设定中心点坐标
//			LatLng cenpt =  MapConvertUtil.convertFromGPS(new LatLng(lat, lng));
			MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(13).build();

			// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
			MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
			// 改变地图状态
			this.bmHistory.animateMapStatus(mMapStatusUpdate);
			
			setCenterFlag = false;
//		}	
	}
	/**带线（航行）：航速大于2.7公里/小时
		不带线（停泊）：航速小于1.8公里/小时
		中间状态：保持上一状态。
		
		黑色：早于24小时的数据
		红色：30分钟~24小时的数据
		黄色：15~30分钟内的数据
		绿色：15分钟之内的数据
		*/
    public static BitmapDescriptor getImgByState(String speed,String time1,String time2){
    	BitmapDescriptor bitmap = ship_green1Bitmap;
    	
    	try {
    		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		
    		Date now = Calendar.getInstance().getTime();
    		if(time2 != null)
    			now = df.parse(time2);
    		
			Date d1 = df.parse(time1);
			long diff = now.getTime()/1000 - d1.getTime()/1000;
			
			if (speed!=null&&!speed.equals("") && Double.valueOf(speed) > 1.8){
				if(diff > 24*60*60)
					bitmap =  ship_blackBitmap;
				else if(diff<=24*60*60&&diff>=30*60)
					bitmap =  ship_redBitmap;
				else if(diff<30*60&&diff>15*60)
					bitmap =  ship_yellowBitmap;
				else
					bitmap =  ship_greenBitmap;
			}else{
				if(diff > 24*60*60)
					bitmap =  ship_black1Bitmap;
				else if(diff<=24*60*60&&diff>=30*60)
					bitmap =  ship_red1Bitmap;
				else if(diff<30*60&&diff>15*60)
					bitmap =  ship_yellow1Bitmap;
				else
					bitmap =  ship_green1Bitmap;
			}
		} catch (Exception e) {
		} 
		
		return bitmap;
    }
    
    class AllOverlayThread extends Thread{
    	float curz;
    	public AllOverlayThread(float curz){
    		this.curz = curz;
    	}
    	@Override
		public void run() {
			optionsList.clear();
			if(!isAdded())
				return;
			for (ShipCooordData pd : shipCooordDatas) {
				// LatLng point = MapConvertUtil.convertFromGPS(new
				// LatLng(Double.parseDouble(pd.getLatitude()),Double.parseDouble(pd.getLongitude())));
				LatLng point;
				if ((0 == Double.valueOf(pd.getBdLatitude()) || 0 == Double
						.valueOf(pd.getBdLongitude()))
						&& (0 == Double.valueOf(pd.getGpsLatitude()) || 0 == Double
								.valueOf(pd.getGpsLongitude())))
					continue;

				if (null != pd.getBdLatitude()
						&& !"0.0".equals(pd.getBdLatitude())) {

					point = new LatLng(Double.valueOf(pd.getBdLatitude()),
							Double.valueOf(pd.getBdLongitude()));
				} else {

					point = new LatLng(Double.valueOf(pd.getGpsLatitude()),
							Double.valueOf(pd.getGpsLongitude()));
				}
				// LatLng textPoint = point;

				 float cou = (float) (0.0F -
				 Double.valueOf(pd.getGpsCourse()));
//				float cou = (float) (Double.valueOf(pd.getGpsCourse()) - 0.0F);
				String name = pd.getShipName();
				if (name == null || name.equals(""))
					name = "暂无船名";

				// TODO 这里根据不同的船舶状态换不同的marker
				MarkerOptions option;
				// OverlayOptions textOption;

				if (curz > smallz) {

					if (curShip != null
							&& curShip.getShipID().equals(pd.getShipID())) {

						BitmapDescriptor bitmap = getImgByState(
								pd.getGpsSpeed(), pd.getGpsTime(), null);

						bitmap = BitmapDescriptorFactory.fromBitmap(Util
								.createDrawable(getActivity(), Util.first(
										getResources(), bitmap.getBitmap(),
										0 - cou, baseBitmapOrg.getBitmap()),
										name));

						option = new MarkerOptions().position(point)
								.icon(bitmap).anchor(0.5f, 0.5f).title(name);

						// textOption = new TextOptions()
						// .bgColor(0xAAFFFF00)
						// .fontSize(28)
						// .fontColor(0xFFFF00FF)
						// .text(name)
						// .position(point)
						// .align(TextOptions.ALIGN_LEFT,
						// TextOptions.ALIGN_BOTTOM);

						optionsList.add(option);
					} else {

						BitmapDescriptor bitmap = getImgByState(
								pd.getGpsSpeed(), pd.getGpsTime(), null);

						// option = new MarkerOptions().position(point)
						// .icon(bitmap)
						// .rotate(cou).title(name);
						option = new MarkerOptions()
								.position(point)
								.icon(BitmapDescriptorFactory.fromBitmap(Util
										.createDrawable(getActivity(),
												bitmap.getBitmap(),
												pd.getShipName()))).rotate(cou)
								.anchor(0.5f, 0.5f).title(name);

						// textOption = new TextOptions()
						// .bgColor(0xAAFFFF00).fontSize(24)
						// .fontColor(0xFFFF00FF).rotate(cou).text(name)
						// .position(point);
						optionsList.add(option);
						// optionsList.add(textOption);
					}

					// optionsList.add(option);
					// showShipName(name, point);
				} else {

					option = new MarkerOptions().position(point)
							.icon(mapspBitmap).title(name).rotate(cou)
							.anchor(0.5f, 0.5f);
					OverlayOptions textOption = new TextOptions()
							.bgColor(0xAAFFFF00)
							.fontSize(24)
							.fontColor(0xFFFF00FF)
							.position(point)
							.text(pd.getShipName())
							.align(TextOptions.ALIGN_LEFT,
									TextOptions.ALIGN_BOTTOM);
					optionsList.add(option);
					optionsList.add(textOption);
				}
			}
			Message message = new Message();
			message.what = showShipOverlay;
			message.obj = curz;
			handler.sendMessage(message);

		}
    }
	
	protected void showAllOverlay(float curz) {
		
//		bmHistory.clear();
//		if(curz>smallz)
//			mapAddPort(getActivity(),bmHistory);
////		optionsList.clear();
		if (shipCooordDatas.size() > 0) {
			allOverlayThread = new AllOverlayThread(curz);
			allOverlayThread.start();
			
//			for (ShipCooordData pd : shipCooordDatas) {
//				// LatLng point = MapConvertUtil.convertFromGPS(new
//				// LatLng(Double.parseDouble(pd.getLatitude()),Double.parseDouble(pd.getLongitude())));
//				LatLng point;
//				if((0 == Double.valueOf(pd.getBdLatitude())||0 == Double.valueOf(pd.getBdLongitude()))
//						&&(0 == Double.valueOf(pd.getGpsLatitude())||0 == Double.valueOf(pd.getGpsLongitude())))
//					continue;
//				
//				if (null != pd.getBdLatitude() && !"0.0".equals(pd.getBdLatitude())){
//					
//					point = new LatLng(Double.valueOf(pd.getBdLatitude()),
//							Double.valueOf(pd.getBdLongitude()));
//				}
//				else{
//					
//					point = new LatLng(Double.valueOf(pd.getGpsLatitude()),
//							Double.valueOf(pd.getGpsLongitude()));
//				}
//				// LatLng textPoint = point;
//
//				// float cou = (float) (0.0F - Double.valueOf(pd.getGpsCourse()));
//				float cou = (float) (Double.valueOf(pd.getGpsCourse()) - 0.0F);
//				String name = pd.getShipName();
//				if (name == null || name.equals(""))
//					name = "暂无船名";
//
//				// TODO 这里根据不同的船舶状态换不同的marker
//				MarkerOptions option;
////				OverlayOptions textOption;
//				
//				
//				if(curz>smallz){
//					
//					if (curShip != null && curShip.getShipID().equals(pd.getShipID())) {
//						
//						BitmapDescriptor bitmap = getImgByState(pd.getGpsSpeed(),pd.getGpsTime(),null);
//						
//						bitmap = BitmapDescriptorFactory.fromBitmap(Util.first(
//								getResources(), bitmap.getBitmap(), 0-cou,
//								baseBitmapOrg.getBitmap()));
//						
//						option = new MarkerOptions().position(point)
//								.icon(bitmap)
//								.anchor(0.5f, 0.5f).title(name);
//						
////					textOption = new TextOptions()
////							.bgColor(0xAAFFFF00)
////							.fontSize(28)
////							.fontColor(0xFFFF00FF)
////							.text(name)
////							.position(point)
////							.align(TextOptions.ALIGN_LEFT,
////									TextOptions.ALIGN_BOTTOM);
//						
//						optionsList.add(option);
//					} else {
//						
//						BitmapDescriptor bitmap = getImgByState(pd.getGpsSpeed(),pd.getGpsTime(),null);
//						
////						option = new MarkerOptions().position(point)
////								.icon(bitmap)
////								.rotate(cou).title(name);
//						option = new MarkerOptions().position(point)
//								.icon(BitmapDescriptorFactory.fromBitmap(Util.createDrawable(getActivity(),bitmap.getBitmap(),pd.getShipName())))
//								.rotate(cou).anchor(0.5f, 0.5f).title(name);
//						
////					textOption = new TextOptions()
////							.bgColor(0xAAFFFF00).fontSize(24)
////							.fontColor(0xFFFF00FF).rotate(cou).text(name)
////							.position(point);
//					optionsList.add(option);
////					optionsList.add(textOption);
//					}
//					
////					optionsList.add(option);
////					showShipName(name, point);
//				}else{
//					
//					
//					option = new MarkerOptions().position(point)
//							.icon(mapspBitmap).title(name)
//							.rotate(cou).anchor(0.5f, 0.5f);
//					OverlayOptions textOption = new TextOptions()
//					.bgColor(0xAAFFFF00).fontSize(24)
//					.fontColor(0xFFFF00FF)
//					.position(point)
//					.text(pd.getShipName())
//					.align(TextOptions.ALIGN_LEFT,
//							TextOptions.ALIGN_BOTTOM);
//					optionsList.add(option);
//					optionsList.add(textOption);
//				}
//			}
		}
//		if (optionsList.size() > 0) {
//			overlayManager = new ShipOverlayManager(bmHistory);
//			overlayManager.removeFromMap();
//			// bmHistory.setOnMarkerClickListener(overlayManager);
//			overlayManager.setData(optionsList);
//			if(curz>smallz&&curShip!=null){
//				currClickedMarker = overlayManager.addToMap2(curShip);
//			}else{
//				overlayManager.addToMap();
//			}
//			if (center) {
//				overlayManager.zoomToSpan();
//				center = false;
//			}
//		}
	}
	
	public void mapAddPort2(Context context,BaiduMap bmHistory) {
		SharedPreferences sp = context.getSharedPreferences( "MapPortData", Context.MODE_PRIVATE);
		String port = sp.getString("MapPort", "");
		
		Gson gson = new Gson();
		List<MapPortData> rmap = gson.fromJson((String) port,
				new TypeToken<List<MapPortData>>() {
		}.getType());
		
		List<OverlayOptions> mapPortOptionsList = new ArrayList<OverlayOptions>();
		if(rmap!=null&&rmap.size()>0) {
			for (MapPortData pd : rmap) {
				LatLng point = new LatLng(Double.valueOf(pd.getY()),Double.valueOf(pd.getX()));
				
				OverlayOptions option = new MarkerOptions().position(point)
						.icon(mapsp1Bitmap)
						.anchor(0.5f, 0.5f)
						.title(mapPort)
						;
				
				OverlayOptions textOption = new TextOptions()
//				.bgColor(0xAAFFFF00)
				.fontSize(24)
				.fontColor(0xAA0000FF)
				.position(point)
				.text(pd.getPort_name())
				.align(TextOptions.ALIGN_LEFT,
						TextOptions.ALIGN_BOTTOM);
				
				mapPortOptionsList.add(option);
				mapPortOptionsList.add(textOption);
			}
		}
		if (mapPortOptionsList.size() > 0) {
			
//			om.removeFromMap();
			om.setData(mapPortOptionsList);
			om.addToMap();
			
		}
	}
	public static void mapAddPort(Context context,BaiduMap bmHistory) {
		SharedPreferences sp = context.getSharedPreferences( ApplicationConstants.MapPortData_SharedPreferences, Context.MODE_PRIVATE);
		String port = sp.getString("MapPort", "");

		Gson gson = new Gson();
		List<MapPortData> rmap = gson.fromJson((String) port,
				new TypeToken<List<MapPortData>>() {
				}.getType());
		
		List<OverlayOptions> mapPortOptionsList = new ArrayList<OverlayOptions>();
		if(rmap!=null&&rmap.size()>0) {
			for (MapPortData pd : rmap) {
				LatLng point = new LatLng(Double.valueOf(pd.getY()),Double.valueOf(pd.getX()));

				OverlayOptions option = new MarkerOptions().position(point)
						.icon(mapsp1Bitmap)
						.anchor(0.5f, 0.5f)
						.title(mapPort)
						;
				
				OverlayOptions textOption = new TextOptions()
//				.bgColor(0xAAFFFF00)
				.fontSize(24)
				.fontColor(0xAA0000FF)
				.position(point)
				.text(pd.getPort_name())
				.align(TextOptions.ALIGN_LEFT,
						TextOptions.ALIGN_BOTTOM);

				mapPortOptionsList.add(option);
				mapPortOptionsList.add(textOption);
			}
		}
		if (mapPortOptionsList.size() > 0) {
			MapPortOverlayManager om = new MapPortOverlayManager(bmHistory);
//			om.removeFromMap();
			om.setData(mapPortOptionsList);
			om.addToMap();
			
		}
	}
	
	@Override
	public boolean onMarkerClick(Marker arg0) {
		if(arg0==null)
			return false;
		
		if(mapPort.equals(arg0.getTitle()))//点击地图港口marker
			return false;
		
		if(curShip!=null&&arg0.getTitle().equals(curShip.getShipName()))
			return false;
		
		if(zoom>smallz){
			if(curShip != null){
//				overlayManager.removeOverlay(curShip.getShipName());
				addNormalMarker(curShip);
			}
		}
		for(ShipCooordData s:shipCooordDatas){
			if(s.getShipName().equals(arg0.getTitle())){
				curShip = s;
				if(zoom>smallz){
					Double currPointSpeed = (!"".equals(curShip.getGpsSpeed()))?Double.valueOf(curShip.getGpsSpeed()):0;
					Double currPointCourse = (!"".equals(curShip.getGpsCourse()))?Double.valueOf(curShip.getGpsCourse()):0;
					
					BitmapDescriptor bitmap = NewContentFragment.getImgByState(String.valueOf(currPointSpeed), curShip.getGpsTime(), null);
					
					// 构建MarkerOption，用于在地图上添加Marker
					float cou = (float) (currPointCourse-0f);
//					float cou = (float) (0-currPointCourse);
					
					bitmap = BitmapDescriptorFactory.fromBitmap(Util.createDrawable(getActivity(),Util.first(getResources(),bitmap.getBitmap(),cou,baseBitmapOrg.getBitmap()),curShip.getShipName()));
					
					arg0.setIcon(bitmap);
					arg0.setRotate(0);
					arg0.setAnchor(0.5f, 0.5f);
			        currClickedMarker = arg0;
					
//					overlayManager.removeOverlay(curShip.getShipName());
//					addClickedMarker(curShip);
				}
				break;
			}
		}
    	
    	isToasted = false;
//    	showAllOverlay(zoom);
    	loadShipImg();
		return false;
	}

	public void selectShip(String shipId, String shipName) {
		
//		bmHistory.hideInfoWindow();
//		hideShipImg();
//		curShip=null;
		
		isToasted = false;
		boolean isHave = false;
		
		for(ShipCooordData s:shipCooordDatas){
			if(s.getShipID().equals(shipId)){
				isHave = true;
				curShip = s;
				if(zoom>smallz){
					addClickedMarker(curShip);
				}
				break;
			}
		}
		if(!isHave){
			Toast.makeText(getActivity(), shipName+"无信号，可能设备断电了！", Toast.LENGTH_SHORT).show();
			return ;
		}
		loadShipImg();
		setCenter(curShip);
		
		
//		Iterator<Entry<LatLng, ShipCooordData>> it = shipMap.entrySet()
//				.iterator();
//		while (it.hasNext()) {
//			Entry<LatLng, ShipCooordData> entry = it.next();
//			if (entry.getValue().getShipID().equals(shipId)) {
//				curShip = entry.getValue();
//				break;
//			}
//		}
//		if(curShip == null){
//			myListener.showLLSearch();
//			Toast.makeText(getActivity(), "该船舶当前无信号！", Toast.LENGTH_LONG).show();
//		}else{
//			if(currMarker != null){
				
//				LatLng currll = currMarker.getPosition();
//				currMarker.remove();
//				addNormalMarker(shipMap.get(currll));
				
//				addClickedMarker(curShip);
				
//			}else{
//				addClickedMarker(curShip);
//			}
			
//			loadShipImg();
//			setCenter(curShip);
//		}
	}
	
	 public  void AlertDialog(String title,ImageView img) {
			LayoutInflater  inflater = LayoutInflater.from(getActivity());
			View view = inflater.inflate(R.layout.dialog_custom_imgbs_layout, null);
			
			ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
			imageView.setOnTouchListener(new TouchListener(imageView));
			
			imageView.setImageDrawable(img.getDrawable());
			
			
			new AlertDialog.Builder(getActivity())
			.setTitle(title)
			.setView(view)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setNegativeButton("取消", null)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
						
				}
			})
			.show();
		}
	
	private class MyOnTouchListener implements OnTouchListener {
		private ShipCooordData curShip;
		private ImageView view;
		private int i;

		public MyOnTouchListener(ShipCooordData curShip, int i,ImageView view) {
			this.curShip = curShip;
			this.view = view;
			this.i = i;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:// 手指压下屏幕
//				startActivity(new Intent(getActivity(),ShowBigImage.class).putExtra("shipId", curShip.getShipID()).putExtra("channel", i).putExtra("shipName", curShip.getShipName()));
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
		viewArea = new ViewArea(b,getActivity(),curShip, i,view2);

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
//	private class MyOnTouchListener implements OnTouchListener {
//		private ImageView img;
//		int count = 0;   
//		int firClick = 0;   
//		int secClick = 0;   
//
//		public MyOnTouchListener(ImageView img) {
//			this.img = img;
//		}
//
//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//			switch (event.getAction() & MotionEvent.ACTION_MASK) {
//			case MotionEvent.ACTION_DOWN:// 手指压下屏幕
//				count++;
//				if (count == 1) {
//					firClick = (int) System.currentTimeMillis();
//
//				} else if (count == 2) {
//					secClick = (int) System.currentTimeMillis();
//					if (secClick - firClick < 1000)
//						AlertDialog("查看图片", img); // 双击事件
//
//					count = 0;
//					firClick = 0;
//					secClick = 0;
//				}
//				break;
//			}
//			return false;
//		}
//	}


	public void loadChang() {
		hideCurrShip();
		loadDataOneMin();
	}


	public void setPoint(String lon, String lat) {
		hideCurrShip();
		
		LatLng point = new LatLng(Double.valueOf(lat),Double.valueOf(lon));
			// 设定中心点坐标
//			LatLng cenpt =  MapConvertUtil.convertFromGPS(new LatLng(lat, lng));
			MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(13).build();

			// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
			MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
			// 改变地图状态
			this.bmHistory.animateMapStatus(mMapStatusUpdate);
	}


	public void setPopupWindow(HXUpPopupWindow hxPopupWindow) {
		this.hxPopupWindow = hxPopupWindow;
		
	}
	
	private void loadUnReadMsgs() {
		Calendar now = Calendar.getInstance();
		String startTime = CalendarUtil.toYYYY_MM_DD_HH_MM_SS(CalendarUtil.addMonths(now, -3));
		String endTime = CalendarUtil.toYYYY_MM_DD_HH_MM_SS(now);
		
		Map<String, Object> apiParams = new HashMap<String, Object>();
		dataLoader.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg2) {
				try {
					unReadMsgs = 0;
					List<PoliceData> list = ParseJson.parserPolice(arg2);
					for(PoliceData p:list){
						if(!p.isbRead()){
							unReadMsgs++;
						}
					}
					
					handler.sendEmptyMessage(7);
				} catch (Exception e) {
				}
				
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				parseJsonThread4 = null;
			}
		}, ApplicationUrls.policeList+NewContentFragment.getUrlTimeStr(startTime)+"&EndTime="+NewContentFragment.getUrlTimeStr(endTime), apiParams, "get");	
		}
	
	private class ZdUnReadMsgBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			parseJsonThread4 = new Thread(new Runnable() {
				@Override
				public void run() {
					loadUnReadMsgs();
				}
			});
			parseJsonThread4.setDaemon(true);
			parseJsonThread4.start();
//			handler.sendEmptyMessage(7);
				// 记得把广播给终结掉
				abortBroadcast();
		}
	}

}
