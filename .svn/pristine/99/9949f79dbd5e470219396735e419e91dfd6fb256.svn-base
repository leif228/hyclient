package com.hangyi.zd.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.NewPageHomeMainActivity;
import com.eyunda.third.activities.map.ShipOverlayManager;
import com.eyunda.third.domain.enumeric.CollectCode;
import com.eyunda.third.domain.location.ShipCooordData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.DateUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.tools.ParseJson;
import com.hangyi.zd.activity.gridviewpage.AppAdapter;
import com.hangyi.zd.domain.ShipModelCode;
import com.hangyi.zd.domain.ShipModelData;
import com.hangyi.zd.domain.ShipModelNoData;
import com.hangyi.zd.domain.UserPowerData;
import com.hangyi.zd.domain.UserPowerShipData;
import com.hangyi.zd.widge.TouchListener;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ContentFragment extends Fragment implements OnClickListener,
		OnMarkerClickListener, OnMapClickListener {
	
	Data_loader dataLoader;
	ImageLoader mImageLoader;

	String userId;
	MapView mapHistory;
	BaiduMap bmHistory;
	UiSettings uisHistory;
	public static final LatLng BEIJING_LATLNG = new LatLng(23.038006,
			113.509988);
	public static final String XML = "XML";
	LinearLayout search_btn;
	EditText search_et;
	Timer shipCooordTimer;
	List<ShipCooordData> shipCooordDatas;
	boolean center = true;//第一次设置中心，以后不设置
	String searchName = "";
	Integer pageNo = 1;
	CollectCode collectCode = CollectCode.mySelfShips;
	
	DistributeTimerTask distributeTask;
	List<OverlayOptions> optionsList;
	Map<LatLng,ShipCooordData> shipMap;
	private LayoutInflater mInflater;
	private LinearLayout shipMoniter;
	private LinearLayout llNav2,shipls,shipzx;
	private LinearLayout shipImg;
	private TextView shipName;
	private ImageView img1;
	private ImageView img2;
	private ImageView img3;
	private ImageView img4;
	private Map<String,List<ShipModelNoData>> maplist = new HashMap<String,List<ShipModelNoData>>();// 船舶授权的镜头数
	private Timer shipImgTimer;
	private ShipImgTimerTask shipImgTask;
	private ShipCooordData curShip;
	private LinearLayout search_ll;
	private LinearLayout shipgroupsz;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		SDKInitializer.initialize(getActivity().getApplicationContext());
		com.baidu.mapapi.map.MapView.setCustomMapStylePath(ContentFragment.getAssetsCacheFile(getActivity(),"baidu_custom_config"));
		return inflater.inflate(R.layout.zd_ship_distribute, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		dataLoader = new Data_loader();
//		mImageLoader = ImageLoader.getInstance();
//		mImageLoader.init(GlobalApplication.getInstance().getImageLoaderConfiguration());
		
		optionsList = new ArrayList<OverlayOptions>();
		shipMap = new HashMap<LatLng, ShipCooordData>();
		initView();
		initMap();
		
		initShipCooordData();
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onStart() {
		ImageLoader.getInstance().destroy();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(GlobalApplication.getInstance().getImageLoaderConfiguration());
		super.onStart();
	}
//	class DistributeTimer extends TimerTask{
//		
//		@Override
//		public void run() {
//			loadDataOneMin();
//		}
//	};
	class ShipImgTimerTask extends TimerTask{
		private ShipCooordData curShip2;
		public ShipImgTimerTask(ShipCooordData curShip2) {
			this.curShip2 = curShip2;
		}

		@Override
		public void run() {
			loadShipImg(curShip2);
		}
	};
	class DistributeTimerTask extends TimerTask{

		@Override
		public void run() {
			loadDataOneMin();
		}
	};
	private void initView() { 
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
		
		shipImg = (LinearLayout) getActivity().findViewById(R.id.shipImg);
		shipName = (TextView) getActivity().findViewById(R.id.shipName);
		img1 = (ImageView) getActivity().findViewById(R.id.img1);
		img2 = (ImageView) getActivity().findViewById(R.id.img2);
		img3 = (ImageView) getActivity().findViewById(R.id.img3);
		img4 = (ImageView) getActivity().findViewById(R.id.img4);
		
//		img1.setOnTouchListener(new MyOnTouchListener(img1));
//		img2.setOnTouchListener(new MyOnTouchListener(img2));
//		img3.setOnTouchListener(new MyOnTouchListener(img3));
//		img4.setOnTouchListener(new MyOnTouchListener(img4));
		
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
		shipCooordDatas = new ArrayList<ShipCooordData>();
		
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
		}

	}

	@Override
	public void onMapClick(LatLng arg0) {
		hideCurrShip();
	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		return false;
	}
	private void hideCurrShip(){
		bmHistory.hideInfoWindow();
		hideShipImg();
		
		if (shipImgTimer != null) {
			if (shipImgTask != null) {
				shipImgTask.cancel(); // 将原任务从队列中移除
			}
		}
	}
	private void showCurrShip(final ShipCooordData curShip, LatLng ll){
		//创建InfoWindow展示的view  
        mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewCache =  mInflater.inflate(R.layout.zd_popup_window, null);
        TextView tv_name = (TextView) viewCache.findViewById(R.id.tv_name);
        tv_name.setText("船名："+curShip.getShipName());
        TextView tv_lat = (TextView) viewCache.findViewById(R.id.tv_lat);
        tv_lat.setText("航向："+curShip.getGpsCourse());
        TextView tv_speed = (TextView) viewCache.findViewById(R.id.tv_speed);
        tv_speed.setText("航速："+curShip.getGpsSpeed()+"节");
        TextView tv_time= (TextView) viewCache.findViewById(R.id.tv_time);
        tv_time.setText("最后时间："+curShip.getGpsTime());
        LinearLayout more= (LinearLayout) viewCache.findViewById(R.id.more);
        more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),ShipInfoActivity.class).putExtra("shipData", curShip));
			}
		});
        
        
    	//定义用于显示该InfoWindow的坐标点  
//    	LatLng pt = arg0.getPosition();  /
    	//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量 
    	InfoWindow mInfoWindow = new InfoWindow(viewCache, ll, -100);  
    	//显示InfoWindow  
    	bmHistory.showInfoWindow(mInfoWindow);
    	
    	setCenter(curShip,13);
    	
    	startShipImgTimer(curShip);
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
    	curShip = shipMap.get( arg0.getPosition());
    	showCurrShip(curShip,arg0.getPosition());
		return false;
	}

	private void startShipImgTimer(ShipCooordData curShip2) {
		if (shipImgTimer != null) {
			if (shipImgTask != null) {
				shipImgTask.cancel(); // 将原任务从队列中移除
			}
		}else{
			shipImgTimer = new Timer(true);
		}
		shipImgTask = new ShipImgTimerTask(curShip2);
		shipImgTimer.schedule(shipImgTask,0,1*60*1000); 
	}

	private void loadShipImg(ShipCooordData curShip2) {
		curShip = curShip2;
		List<ShipModelNoData> list = maplist.get(curShip2.getShipID());
		if(list == null){
			
			SharedPreferences sp = getActivity().getSharedPreferences("UserPowerData", Context.MODE_PRIVATE);
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
		message.what = 2;
		handler.sendMessage(message);
		
	}
	private void showShipImg(){
		List<ShipModelNoData> list = maplist.get(curShip.getShipID());
		if(list!=null&&list.size()>0){
			int size = list.size();
			if(size>4){
				Toast.makeText(getActivity(), "目前最多只支持四个镜头显视。", Toast.LENGTH_LONG).show();
				return;
			}
			shipImg.setVisibility(View.VISIBLE);
			shipName.setText(curShip.getShipName());
			for (int i = 1; i <= size; i++) {
				if(size == 1) {
					img2.setVisibility(View.VISIBLE);
					img2.setOnTouchListener(new MyOnTouchListener(curShip,i));
					
					displayImage(img2,curShip,i);
				} else if(size == 3){
					if(i ==3){
						img4.setVisibility(View.VISIBLE);
						img4.setOnTouchListener(new MyOnTouchListener(curShip,i));
		
						displayImage(img4,curShip,i);
						break;
					}
					int view_id = getResources().getIdentifier("img"+i, "id",  getActivity().getPackageName());
					ImageView view = (ImageView) getActivity().findViewById(view_id);
					view.setVisibility(View.VISIBLE);
					view.setOnTouchListener(new MyOnTouchListener(curShip,i));
	
					displayImage(view,curShip,i);
				}else {
				
					int view_id = getResources().getIdentifier("img"+i, "id",  getActivity().getPackageName());
					ImageView view = (ImageView) getActivity().findViewById(view_id);
					view.setVisibility(View.VISIBLE);
					view.setOnTouchListener(new MyOnTouchListener(curShip,i));
					
					displayImage(view,curShip,i);
				}
			}
		}
	}
	
	private void displayImage(ImageView view,final ShipCooordData curShip,int i ){
		mImageLoader.displayImage(ApplicationConstants.ZDPHP_PRE_URL+ "/clientapi/?Function=3&ShipID="+curShip.getShipID()+"&Channel="+i+"&PictureTime="+ getCurrTimeStr(),
				view,
				NewPageHomeMainActivity.options,new ImageLoadingListener(){

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				((ImageView)view).setImageDrawable(AppAdapter.createDrawable(getActivity(),loadedImage,null,DateUtils.getTime("yyyy-MM-dd HH:mm:ss")));
			}

			@Override
			public void onLoadingFailed(String arg0, View view,
					FailReason arg2) {
				((ImageView)view).setDrawingCacheEnabled(true);
				((ImageView)view).setImageDrawable(AppAdapter.createDrawable(getActivity(),((ImageView)view).getDrawingCache(),null,DateUtils.getTime("yyyy-MM-dd HH:mm:ss")));
				((ImageView)view).setDrawingCacheEnabled(false);
				
			}

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}});
	}

	private void hideShipImg() {
		img1.setVisibility(View.INVISIBLE);
		img2.setVisibility(View.INVISIBLE);
		img3.setVisibility(View.INVISIBLE);
		img4.setVisibility(View.INVISIBLE);
		shipImg.setVisibility(View.GONE);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				showAllOverlay();
				break;
			case 2:
				showShipImg();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		};
	};
	private boolean setCenterFlag = true;

	protected synchronized void loadDataOneMin() {
		Map<String, Object> apiParams = new HashMap<String, Object>();
		
		dataLoader.getZd_ApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg2) {
				super.onSuccess(arg2);
				
				shipCooordDatas.clear();
				shipCooordDatas = ParseJson.parserHome(arg2);
				
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				
				shipCooordDatas.clear();
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);

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
		}, ApplicationUrls.pageHomeGps+getCurrTimeStr(), apiParams, "get");
	}
	
	public static String getCurrTimeStr(){
		String day = DateUtils.getTime("yyyy-MM-dd HH:mm:ss");
		String[] arr = day.split(" ");
		String[] arr2 = arr[1].split(":");
		day = arr[0] + "%20" + arr2[0]+":"+arr2[1]+":"+"00";
		return day;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapHistory.onDestroy();
		
		if(shipCooordTimer!=null){
			
			shipCooordTimer.cancel();
			shipCooordTimer.purge();
			shipCooordTimer = null;
		}
		if(shipImgTimer!=null){
			
			shipImgTimer.cancel();
			shipImgTimer.purge();
			shipImgTimer = null;
		}
	}
//	@Override
//	protected void onStop() {
//		super.onStop();
//		timer.cancel();
//		timer.purge();
//	}
	@Override
	public void onPause() {
		super.onPause();
		mapHistory.onPause();
//		timer.cancel();
//		timer.purge();
	}
//	@Override
//	protected void onRestart() {
//		super.onRestart();
//		initData();
//	}
	@Override
	public void onResume() {
		super.onResume();
		mapHistory.onResume();
//		initData();
	}
	public void setCenter(ShipCooordData pd, int zoom) {
//		if(setCenterFlag){
		LatLng point ;
		if(pd.getBdLatitude()!=null&&!pd.getBdLatitude().equals(""))
			point = new LatLng(Double.valueOf(pd.getBdLatitude()),Double.valueOf(pd.getBdLongitude()));
		else
			point = new LatLng(Double.valueOf(pd.getGpsLatitude()),Double.valueOf(pd.getGpsLongitude()));
			// 设定中心点坐标
//			LatLng cenpt =  MapConvertUtil.convertFromGPS(new LatLng(lat, lng));
			MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(zoom).build();

			// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
			MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
			// 改变地图状态
			this.bmHistory.animateMapStatus(mMapStatusUpdate);
			
			setCenterFlag = false;
//		}	
	}
    protected void showAllOverlay() {
    	
    	bmHistory.clear();
    	optionsList.clear();
    	shipMap.clear();
    	if(shipCooordDatas.size()>0){
    		for(ShipCooordData pd:shipCooordDatas){
    			//LatLng point =  MapConvertUtil.convertFromGPS(new LatLng(Double.parseDouble(pd.getLatitude()),Double.parseDouble(pd.getLongitude())));
    			LatLng point ;
    			if(null != pd.getBdLatitude()&&!"".equals(pd.getBdLatitude()))
    				point = new LatLng(Double.valueOf(pd.getBdLatitude()),Double.valueOf(pd.getBdLongitude()));
    			else
    				point = new LatLng(Double.valueOf(pd.getGpsLatitude()),Double.valueOf(pd.getGpsLongitude()));
    			//LatLng textPoint = point;
	    		
	    		float cou = (float) (0.0F - Double.valueOf(pd.getGpsCourse()));
	    		//TODO 这里根据不同的船舶状态换不同的marker
//	    		BitmapDescriptor bitmap = BitmapDescriptorFactory
//	    				.fromResource(R.drawable.icon_marker);
	    		BitmapDescriptor bitmap = null;
	    		if(!pd.getGpsSpeed().equals("")&&Double.valueOf(pd.getGpsSpeed())>1)
	    			bitmap = BitmapDescriptorFactory
    				.fromResource(R.drawable.icon_marker);
	    		else
	    			bitmap = BitmapDescriptorFactory
	    			.fromResource(R.drawable.icon_marker_stop);
	    		// 构建MarkerOption，用于在地图上添加Marker
	    		String name  =pd.getShipName();
	    		if(name == null || name.equals(""))
	    			name = "暂无船名";
	    		
	    		OverlayOptions option = new MarkerOptions()
	    		.position(point)
	    		.icon(bitmap)
	    		.rotate(cou);
	    		
	    		OverlayOptions textOption = new TextOptions()  
	    	    .bgColor(0xAAFFFF00)  
	    	    .fontSize(28)  
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

	public void selectShip(String shipId) {
		hideCurrShip();
		for (LatLng key : shipMap.keySet()) {
			curShip = shipMap.get(key);
			if(curShip.getShipID().equals(shipId)){
				showCurrShip(curShip,key);
			}
		}
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
		private int i;

		public MyOnTouchListener(ShipCooordData curShip, int i) {
			this.curShip = curShip;
			this.i = i;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:// 手指压下屏幕
				startActivity(new Intent(getActivity(),ShowBigImage.class).putExtra("shipId", curShip.getShipID()).putExtra("channel", i).putExtra("shipName", curShip.getShipName()));
				break;
			}
			return false;
		}
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
	
}
