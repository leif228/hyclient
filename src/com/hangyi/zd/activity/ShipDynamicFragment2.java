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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.domain.ship.ShipMonitorPlantCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.CalendarUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.tools.ParseJson;
import com.hangyi.tools.Util;
import com.hangyi.zd.ImgDownLoadService;
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

public class ShipDynamicFragment2 extends Fragment implements OnClickListener,
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
	private ShipMonitorPlantCode smpc;//数据来源
    
    private LinearLayout shipImg;
    private LinearLayout shipImgBig;
    private boolean isPlayBigImg = false;
//    private TextView shipNameText;
//	private ImageView img11,img12,img13,img14;
//	private ImageView img21,img22,img23,img24;
//	private SurfaceView img31,img32,img33,img34;
//	private SurfaceHolder holderimg31,holderimg32,holderimg33,holderimg34;
//	private ImageView img31,img32,img33,img34;
//	private ImageView img41,img42,img43,img44;
//	private LinearLayout img1,img2,img3,img4;
	private ImageView img1,img2,img3,img4;
	private ImageView imgBig1,imgBig2,imgBig3,imgBig4;
	
	private CommonVideoView commonVideoView;
	private List<String> channels = new ArrayList<String>();
	
	private static int ParseJsonException = 9;
	private static int ParseJsonIsEmpty = 8;
	private static int ParseJsonTooMuch = 7;
	
	Thread parseJsonThread = null;
	
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
		smpc = ShipMonitorPlantCode.shipmanagerplant;
		
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
		NewContentFragment.mapAddPort(getActivity(),bmHistory);
		
		Calendar now = Calendar.getInstance();
		if("".equals(startTime)||"".equals(endTime)){
			startTime = CalendarUtil.toYYYY_MM_DD_HH_MM_SS(CalendarUtil.addDays(now, -1));
			endTime = CalendarUtil.toYYYY_MM_DD_HH_MM_SS(now);
		}
		
		initView(startTime,endTime);
		initData(startTime,endTime);
		
		super.onActivityCreated(savedInstanceState);
	}
	private void initView(String startTime, String endTime) {
//        WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);  
//        Display display = manager.getDefaultDisplay();  
//        display.getSize(pt);  
		
		commonVideoView = (CommonVideoView) getActivity().findViewById(R.id.common_videoView);
		commonVideoView.init(CalendarUtil.parseYYYY_MM_DD_HH_MM_SS(startTime),CalendarUtil.parseYYYY_MM_DD_HH_MM_SS(endTime));
		
//		shipNameText = (TextView) getActivity().findViewById(R.id.shipName);
//		shipNameText.setText(shipName);
		
		shipImg = (LinearLayout) getActivity().findViewById(R.id.shipImg);
		shipImgBig = (LinearLayout) getActivity().findViewById(R.id.shipImgBig);
		img1 = (ImageView) getActivity().findViewById(R.id.img1);
		img2 = (ImageView) getActivity().findViewById(R.id.img2);
		img3 = (ImageView) getActivity().findViewById(R.id.img3);
		img4 = (ImageView) getActivity().findViewById(R.id.img4);
		imgBig1 = (ImageView) getActivity().findViewById(R.id.imgBig1);
		imgBig2 = (ImageView) getActivity().findViewById(R.id.imgBig2);
		imgBig3 = (ImageView) getActivity().findViewById(R.id.imgBig3);
		imgBig4 = (ImageView) getActivity().findViewById(R.id.imgBig4);
		
//		img1.setOnTouchListener(new MyOnTouchListener(img1));
//		img2.setOnTouchListener(new MyOnTouchListener(img2));
//		img3.setOnTouchListener(new MyOnTouchListener(img3));
//		img4.setOnTouchListener(new MyOnTouchListener(img4));
		
	}

	@Override
	public void onStart() {
		super.onStart();
//		testShowImg();
	}

	private void initData(String startTime,String endTime){
		if(positionDatas==null){
			positionDatas = new ArrayList<ShipGpsData>();
		}else{
			positionDatas.clear();
		}
		
		loadData(startTime,endTime);
		
	}
	private int centerChange=30;
	private int flag = 0;
	private boolean flagFirst = true;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {// 初始化数据完成
				drawLines();
				initShipPowerChannels();
				gpsLoadedAfterStart();
				
			} else if (msg.what == 2) {// 读取新坐标完成
				if(CommonVideoView.isPlaying){
					int lastPointIndex = currPlayPosition;
					if (lastPointIndex < positionDatas.size()) {
						ShipGpsData currPoint = positionDatas.get(lastPointIndex);
						if(!isPlayBigImg){
							moveMarker(currPoint);
							showShipImg(currPoint);
							
							if(flagFirst){
								setCenter(currPoint, z);
								flagFirst=false;
							}
							if(flag == centerChange){
								flag = 0;
								setCenter(currPoint, z);
							}
							flag++;
						}else{
							removeMarker();
							showShipImgBig(currPoint);
						}
						
						lastPointIndex++;
						currPlayPosition = lastPointIndex;
					}
				}
			}else if(msg.what == 3){
//				if (lastPointIndex < positionDatas.size()) {
//					ShipGpsData lastPoint = positionDatas.get(lastPointIndex);
//					showShipImg(lastPoint);
//					
//					lastPointIndex++;
//					LoadedList.getInstance().setCurrPlayPosition(lastPointIndex);
//				}
			}else if(msg.what == ParseJsonException&&getActivity()!=null){
				Toast.makeText(getActivity(), "数据解析异常！请稍后再试！", Toast.LENGTH_LONG).show();
			}else if(msg.what == ParseJsonIsEmpty&&getActivity()!=null){
				Toast.makeText(getActivity(), "数据加载为空！", Toast.LENGTH_LONG).show();
			}else if(msg.what == ParseJsonTooMuch&&getActivity()!=null){
				Toast.makeText(getActivity(), "航线数据太多，超出显视范围，请重新选择时间段！", Toast.LENGTH_LONG).show();
			}
			super.handleMessage(msg);
		}

	};
	
//	public String doGpsTime(String gpsTime){
//		String[] arr = gpsTime.split(":");
//		return arr[0]+":"+arr[1]+":" +"00";
//	}
	protected void initShipPowerChannels() {
		if(positionDatas!=null&&!positionDatas.isEmpty()){
			ShipGpsData sgd = positionDatas.get(0);
			shipID = sgd.getShipID();
			
			if(channels.isEmpty()){
				SharedPreferences sp = GlobalApplication.getInstance().getSharedPreferences("UserPowerData", Context.MODE_PRIVATE);
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
//				if(channels!=null&&!channels.isEmpty()){
//					switch (channels.size()) {
//					case 1:
//						img1 = (LinearLayout) getActivity().findViewById(R.id.img1);
//						img1.setVisibility(View.VISIBLE);
//						img11 = (ImageView) getActivity().findViewById(R.id.img11);
//						img12 = (ImageView) getActivity().findViewById(R.id.img12);
//						img13 = (ImageView) getActivity().findViewById(R.id.img13);
//						img14 = (ImageView) getActivity().findViewById(R.id.img14);
//						img11.setOnTouchListener(new MyOnTouchListener(img11));
//						img12.setOnTouchListener(new MyOnTouchListener(img12));
//						img13.setOnTouchListener(new MyOnTouchListener(img13));
//						img14.setOnTouchListener(new MyOnTouchListener(img14));
//						
//						break;
//					case 2:
//						img2 = (LinearLayout) getActivity().findViewById(R.id.img2);
//						img2.setVisibility(View.VISIBLE);
//						img21 = (ImageView) getActivity().findViewById(R.id.img21);
//						img22 = (ImageView) getActivity().findViewById(R.id.img22);
//						img23 = (ImageView) getActivity().findViewById(R.id.img23);
//						img24 = (ImageView) getActivity().findViewById(R.id.img24);
//						img21.setOnTouchListener(new MyOnTouchListener(img21));
//						img22.setOnTouchListener(new MyOnTouchListener(img22));
//						img23.setOnTouchListener(new MyOnTouchListener(img23));
//						img24.setOnTouchListener(new MyOnTouchListener(img24));
//						
//						break;
//					case 3:
//						img3 = (LinearLayout) getActivity().findViewById(R.id.img3);
//						img3.setVisibility(View.VISIBLE);
//	//					img31 = (SurfaceView) getActivity().findViewById(R.id.img31);
//	//					holderimg31 = img31.getHolder();
//	//					img32 = (SurfaceView) getActivity().findViewById(R.id.img32);
//	//					holderimg32 = img32.getHolder();
//	//					img33 = (SurfaceView) getActivity().findViewById(R.id.img33);
//	//					holderimg33 = img33.getHolder();
//	//					img34 = (SurfaceView) getActivity().findViewById(R.id.img34);
//	//					holderimg34 = img34.getHolder();
//						
//						img31 = (ImageView) getActivity().findViewById(R.id.img31);
//						img32 = (ImageView) getActivity().findViewById(R.id.img32);
//						img33 = (ImageView) getActivity().findViewById(R.id.img33);
//						img34 = (ImageView) getActivity().findViewById(R.id.img34);
//						img31.setOnTouchListener(new MyOnTouchListener(img31));
//						img32.setOnTouchListener(new MyOnTouchListener(img32));
//						img33.setOnTouchListener(new MyOnTouchListener(img33));
//						img34.setOnTouchListener(new MyOnTouchListener(img34));
//						
//						break;
//					case 4:
//						img4 = (LinearLayout) getActivity().findViewById(R.id.img4);
//						img4.setVisibility(View.VISIBLE);
//						img41 = (ImageView) getActivity().findViewById(R.id.img41);
//						img42 = (ImageView) getActivity().findViewById(R.id.img42);
//						img43 = (ImageView) getActivity().findViewById(R.id.img43);
//						img44 = (ImageView) getActivity().findViewById(R.id.img44);
//						img41.setOnTouchListener(new MyOnTouchListener(img41));
//						img42.setOnTouchListener(new MyOnTouchListener(img42));
//						img43.setOnTouchListener(new MyOnTouchListener(img43));
//						img44.setOnTouchListener(new MyOnTouchListener(img44));
//						break;
//	
//					default:
//						break;
//					}
//				}
			}
		}
	}

	File sdCardPath = Environment.getExternalStorageDirectory();// 获取SDCard目录
	File cacheDir = new File(sdCardPath + "/zd/img");
//	File shipCacheDir = new File(cacheDir + "/" +shipID);
//	String shipChannelCacheDir = shipCacheDir.getAbsolutePath();
	
	private String getUrl(ShipGpsData data,String string){
		String shipChannelCacheDirStr = cacheDir + "/" + shipID + "/" + string;
//		File shipChannelCacheImg = new File(shipChannelCacheDirStr+"/"+doGpsTime(data.getGpsTime())+".png");
		String shipChannelCacheImg = shipChannelCacheDirStr+"/"+LoadImgRunnable.doGpsTime(data.getGpsTime())+".png";
		if(new File(shipChannelCacheImg).exists())
			return shipChannelCacheImg;
//		    return "file://"+shipChannelCacheImg;
		else
			return null;
	}
//	private void testShowImg(){
//		String url = "";
//		
//		File sdCardPath = Environment.getExternalStorageDirectory();// 获取SDCard目录
//		File cacheDir = new File(sdCardPath + "/zd/img");
//		File shipCacheDir = new File(cacheDir + "/" +"0x1058");
//		String shipChannelCacheDir = shipCacheDir.getAbsolutePath();
//		String shipChannelCacheDirStr = shipChannelCacheDir + "/" + 1;
//		File shipChannelCacheImg = new File(shipChannelCacheDirStr+"/"+"2016-08-05 11:37:00"+".png");
//		if(shipChannelCacheImg.exists())
//			url = "file://"+shipChannelCacheImg.getAbsolutePath();
//		
//		shipImg.setVisibility(View.VISIBLE);
//		img2.setVisibility(View.VISIBLE);
//		mImageLoader.displayImage(url,
//				img2,
//				NewPageHomeMainActivity.options);
//	}
	
	private void showShipImgBig(ShipGpsData curShip) {
		shipImg.setVisibility(View.GONE);
		shipImgBig.setVisibility(View.VISIBLE);
		int size = channels.size();
		if(size ==0)
			return;
		for (int i = 1; i <= size; i++) {
			if(size == 1) {
				imgBig2.setVisibility(View.VISIBLE);
				String imgStr21 = getUrl(curShip,channels.get(i-1));
				if(imgStr21 !=null )
					imgBig2.setImageDrawable(AppAdapter.createDrawable(getActivity(), BitmapFactory.decodeFile(imgStr21), null, curShip.getGpsTime()));
			} else if(size == 3){
				if(i ==3){
					imgBig4.setVisibility(View.VISIBLE);
					
					String imgStr21 = getUrl(curShip,channels.get(i-1));
					if(imgStr21 !=null )
						imgBig4.setImageDrawable(AppAdapter.createDrawable(getActivity(), BitmapFactory.decodeFile(imgStr21), null, curShip.getGpsTime()));
//						imgBig4.setImageBitmap(BitmapFactory.decodeFile(imgStr21));
					break;
				}
				int view_id = getResources().getIdentifier("imgBig"+i, "id",  getActivity().getPackageName());
				ImageView view = (ImageView) getActivity().findViewById(view_id);
				view.setVisibility(View.VISIBLE);
				
				String imgStr21 = getUrl(curShip,channels.get(i-1));
				if(imgStr21 !=null )
					view.setImageDrawable(AppAdapter.createDrawable(getActivity(), BitmapFactory.decodeFile(imgStr21), null, curShip.getGpsTime()));
//					view.setImageBitmap(BitmapFactory.decodeFile(imgStr21));
			}else {
				
				int view_id = getResources().getIdentifier("imgBig"+i, "id",  getActivity().getPackageName());
				ImageView view = (ImageView) getActivity().findViewById(view_id);
				view.setVisibility(View.VISIBLE);
				
				String imgStr21 = getUrl(curShip,channels.get(i-1));
				if(imgStr21 !=null )
					view.setImageDrawable(AppAdapter.createDrawable(getActivity(), BitmapFactory.decodeFile(imgStr21), null, curShip.getGpsTime()));
//					view.setImageBitmap(BitmapFactory.decodeFile(imgStr21));
			}
		}
		
	}
	private void showShipImg(ShipGpsData curShip) {
//			shipImg.setVisibility(View.VISIBLE);
//			int size = channels.size();
//			if(size ==0)
//				return;
//			switch (size) {
//			case 1:
//				String imgStr12 = getUrl(curShip,channels.get(0));
//				if(imgStr12 !=null )
//					img12.setImageBitmap(BitmapFactory.decodeFile(imgStr12));
//				break;
//			case 2:
//				String imgStr21 = getUrl(curShip,channels.get(0));
//				if(imgStr21 !=null )
//					img21.setImageBitmap(BitmapFactory.decodeFile(imgStr21));
//				String imgStr22 = getUrl(curShip,channels.get(1));
//				if(imgStr22 !=null )
//					img22.setImageBitmap(BitmapFactory.decodeFile(imgStr22));
//				break;
//			case 3:
//				
//				String imgStr31 = getUrl(curShip,channels.get(0));
//				if(imgStr31 !=null )
//					img31.setImageBitmap(BitmapFactory.decodeFile(imgStr31));
//				String imgStr32 = getUrl(curShip,channels.get(1));
//				if(imgStr32 !=null )
//					img32.setImageBitmap(BitmapFactory.decodeFile(imgStr32));
//				String imgStr34 = getUrl(curShip,channels.get(2));
//				if(imgStr34 !=null )
//					img34.setImageBitmap(BitmapFactory.decodeFile(imgStr34));
//				break;
//			case 4:
//				String imgStr41 = getUrl(curShip,channels.get(0));
//				if(imgStr41 !=null )
//					img41.setImageBitmap(BitmapFactory.decodeFile(imgStr41));
//				String imgStr42 = getUrl(curShip,channels.get(1));
//				if(imgStr42 !=null )
//					img42.setImageBitmap(BitmapFactory.decodeFile(imgStr42));
//				String imgStr43 = getUrl(curShip,channels.get(2));
//				if(imgStr43 !=null )
//					img43.setImageBitmap(BitmapFactory.decodeFile(imgStr43));
//				String imgStr44 = getUrl(curShip,channels.get(3));
//				if(imgStr44 !=null )
//					img44.setImageBitmap(BitmapFactory.decodeFile(imgStr44));
//				break;
//
//			default:
//				break;
//			}
	
			try {
				shipImg.setVisibility(View.VISIBLE);
				shipImgBig.setVisibility(View.GONE);
				int size = channels.size();
				if(size ==0)
					return;
				for (int i = 1; i <= size; i++) {
					if(size == 1) {
						img2.setVisibility(View.VISIBLE);
						String imgStr12 = getUrl(curShip,channels.get(i-1));
						if(imgStr12 !=null )
							img2.setImageDrawable(AppAdapter.createDrawable(getActivity(), BitmapFactory.decodeFile(imgStr12), null, curShip.getGpsTime()));
//						img2.setImageBitmap(BitmapFactory.decodeFile(imgStr12));
					} else if(size == 3){
						if(i ==3){
							img4.setVisibility(View.VISIBLE);
							
							String imgStr21 = getUrl(curShip,channels.get(i-1));
							if(imgStr21 !=null )
								img4.setImageDrawable(AppAdapter.createDrawable(getActivity(), BitmapFactory.decodeFile(imgStr21), null, curShip.getGpsTime()));
//							img4.setImageBitmap(BitmapFactory.decodeFile(imgStr21));
							break;
						}
						int view_id = getResources().getIdentifier("img"+i, "id",  getActivity().getPackageName());
						ImageView view = (ImageView) getActivity().findViewById(view_id);
						view.setVisibility(View.VISIBLE);
						
						String imgStr21 = getUrl(curShip,channels.get(i-1));
						if(imgStr21 !=null )
							view.setImageDrawable(AppAdapter.createDrawable(getActivity(), BitmapFactory.decodeFile(imgStr21), null, curShip.getGpsTime()));
//						view.setImageBitmap(BitmapFactory.decodeFile(imgStr21));
					}else {
						
						int view_id = getResources().getIdentifier("img"+i, "id",  getActivity().getPackageName());
						ImageView view = (ImageView) getActivity().findViewById(view_id);
						view.setVisibility(View.VISIBLE);
						
						String imgStr21 = getUrl(curShip,channels.get(i-1));
						if(imgStr21 !=null )
							view.setImageDrawable(AppAdapter.createDrawable(getActivity(), BitmapFactory.decodeFile(imgStr21), null, curShip.getGpsTime()));
//						view.setImageBitmap(BitmapFactory.decodeFile(imgStr21));
					}
				}
			} catch (Exception e) {
			}
			
	}
//	private void handleImgShow(String url, SurfaceHolder holderimg) {
//		if(url==null)
//			return;
//		// 锁定整个SurfaceView
//		Canvas canvas = holderimg.lockCanvas();
//		// 绘制背景
//		Bitmap back = BitmapFactory.decodeFile(url);
//		// 绘制背景
//		canvas.drawBitmap(back, 0, 0, null);
//		// 绘制完成，释放画布，提交修改
//		holderimg.unlockCanvasAndPost(canvas);
//		// 重新锁一次，"持久化"上次所绘制的内容
//		holderimg.lockCanvas(new Rect(0, 0, 0, 0));
//		holderimg.unlockCanvasAndPost(canvas);
//		
//	}

	protected void gpsLoadedAfterStart() {
		LoadedList.getInstance().initQueue(positionDatas.size());
		commonVideoView.setCommonVideoChangLintener(this);
		commonVideoView.startCacheSeekBarTimer();
		startThreadLoadImg();
		PlayGpsTimer.getInstance().setHandler(handler);
    	PlayGpsTimer.getInstance().startPlayGpsTimer();
    	modChang();
//    	startShipImgTimer();
    	
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
			if (points.size() > 1) {
				OverlayOptions ooPolyline = new PolylineOptions().width(10)
						.zIndex(20).color(0xAAFF0000).points(points);
				bmHistory.addOverlay(ooPolyline);
			}
		}
	};

	private synchronized void moveMarker(final ShipGpsData toPoint) {
		if(smpc.equals(ShipMonitorPlantCode.shipmanagerplant)){
					removeMarker();
					addMarker(toPoint);
		}else{
			removeMarker();
			addMarker(toPoint);
		}	
	}
	private void addMarker(ShipGpsData point) {
		// 定义Maker坐标点
		// 构建Marker图标
//		LatLng point = MapConvertUtil.convertFromGPS( new LatLng(Double.parseDouble(toPoint.getLatitude()),
//				Double.parseDouble(toPoint.getLongitude())));
		LatLng first;
		if(point.getBdgpsLongitude()!=null&&point.getBdgpsLongitude()!=0.0)
			first = new LatLng(point.getBdgpsLatitude(),point.getBdgpsLongitude());
		else
		    first = new LatLng(point.getGpsLatitude(),point.getGpsLongitude());
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon50);
		
		// 构建MarkerOption，用于在地图上添加Marker
		float cou = (float) (0.0F-point.getGpsCourse()  );
//		float cou = (float) (point.getGpsCourse()-0.0f);
		
		BitmapDescriptor baseBitmapOrg = BitmapDescriptorFactory
				.fromResource(R.drawable.kuang1);
		
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
	}
	
	// 初始化坐标数据
	protected synchronized void loadData(String startTime,String endTime) {
//		String shipID = "0x1058";
//		Calendar now = Calendar.getInstance();
//		String endTime = CalendarUtil.toYYYY_MM_DD_HH_MM_SS(CalendarUtil.addDays(now, -6));
//		String startTime = CalendarUtil.toYYYY_MM_DD_HH_MM_SS(CalendarUtil.addDays(now, -7));
		
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
				
				smpc = ShipMonitorPlantCode.shipmanagerplant;
//					if(smpc.equals(ShipMonitorPlantCode.shipmanagerplant)){
//						shipImg.setVisibility(View.VISIBLE);
//					}else{
//						shipImg.setVisibility(View.GONE);
//					}
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

				dialog.dismiss();
				if (content != null && content.equals("can't resolve host")&&getActivity()!=null)
					Toast.makeText(getActivity(), "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")&&getActivity()!=null) {
					Toast.makeText(getActivity(), "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null&&getActivity()!=null) {
					Toast.makeText(getActivity(), content,
							Toast.LENGTH_LONG).show();
				} else if(getActivity()!=null)
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
		shipImgBig.setVisibility(View.GONE);
//		clearImageview();
		commonVideoView.reLoadView();
		preClose();
		initData(CalendarUtil.toYYYY_MM_DD_HH_MM_SS(startCalendar), CalendarUtil.toYYYY_MM_DD_HH_MM_SS(endCalendar));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		commonVideoView.reLoadView();
		preClose();
		if(parseJsonThread!=null){
			parseJsonThread.interrupt();
			parseJsonThread = null;
		}
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
	
	private class MyOnTouchListener implements OnTouchListener {
		private ImageView img;
		int count = 0;   
		int firClick = 0;   
		int secClick = 0;   

		public MyOnTouchListener(ImageView img) {
			this.img = img;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:// 手指压下屏幕
				//TODO 控制面板打开
				break;
			}
			return true;
		}
	}
	@Override
	public void fastChang() {
		if (positionDatas != null && !positionDatas.isEmpty()) {
			int size = positionDatas.size();
			int ss = size/playfast;
			String time = secToTime(ss);
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
			String time = secToTime(ss);
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
			String time = secToTime(ss);
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
	
	public static String secToTime(int time) {  
        String timeStr = null;  
        int hour = 0;  
        int minute = 0;  
        int second = 0;  
        if (time <= 0)  
            return "00:00";  
        else {  
            minute = time / 60;  
            if (minute < 60) {  
                second = time % 60;  
                timeStr = unitFormat(minute) + ":" + unitFormat(second);  
            } else {  
                hour = minute / 60;  
                if (hour > 99)  
                    return "99:59:59";  
                minute = minute % 60;  
                second = time - hour * 3600 - minute * 60;  
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);  
            }  
        }  
        return timeStr;  
    }  
  
    public static String unitFormat(int i) {  
        String retStr = null;  
        if (i >= 0 && i < 10)  
            retStr = "0" + Integer.toString(i);  
        else  
            retStr = "" + i;  
        return retStr;  
    }

	@Override
	public void playBigImg() {
		// TODO Auto-generated method stub
		isPlayBigImg = true;
		
	}

	@Override
	public void playSmaImg() {
		// TODO Auto-generated method stub
		isPlayBigImg = false;
		
	}

	@Override
	public void disPlayPauseImg() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void noDisPlayPauseImg() {
		// TODO Auto-generated method stub
		
	} 
	
}
