package com.hangyi.zd.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.location.ShipCooordData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.MapConvertUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.tools.ParseJson;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

public class MainActivity extends Activity implements
OnClickListener, OnMarkerClickListener, OnMapClickListener{

	Data_loader dataLoader;
	private LinearLayout shipAgent;
	private LinearLayout showPic;
	//时间
	private TextView shipTime;
	//船区
	private TextView shipArea;
	//船舶监控
	private ImageView shipPics;
	private LinearLayout shipPicsContain;
	private Point pt;
	//百度地图控件
	private MapView mapHistory;
	BaiduMap bmHistory;
	public static final LatLng BEIJING_LATLNG = new LatLng(23.038006,
			113.509988);
	//模拟数据
	//{"Data":[{"State":1,"ShipID":"0x0016","GPS":{"GPSTime":"2016-04-30 23:59:25",
	//"GPSLongitude":113.959740,"GPSLatitude":22.361063,"GPSSpeed":0.00,"GPSCourse":0.00},
	//"SpeedMeter":[{"ID":1,"Volume":0},{"ID":2,"Volume":0}],"LevelMeter":[{"ID":0,"Level":1077.2,
	//"Volume":0.0},{"ID":1,"Level":1276.3,"Volume":0.0},{"ID":2,"Level":743.7,"Volume":0.0}],
	//"PowerManagement":{"MainVoltage":15.891,"BackUpVoltage":13.087,"Temperature":35,"State":0}}]}
	List<ShipCooordData> positionDatas;
	ShipCooordData lastPoint,tmpPoint;
	private int lastPointIndex;
	private Marker marker;
	private UiSettings uisHistory;
	boolean setCenterFlag = true;
	private ArrayList<LatLng> points;
	private EditText tvStartTime;
	private EditText tvEndTime;
	private Button btSubmit;
	private String json;
	private String useName;
	private String passWord;
	private ArrayList<ShipCooordData> list;
	private String text;
	private Bitmap picture;
	private ImageLoader mImageLoader;
	private Timer timer;
	private MyTimerTask mTimerTask;
	private String time;
	int b=0;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
//				list = (ArrayList<ShipCooordData>) msg.obj;
				//测试代码
//				for(int i = 0;i<list.size();i++) {
//					ShipCooordData lng = list.get(i);
//					String latitude = lng.getLatitude();
//					String longtitude = lng.getLongitude();
//					Log.i("lng", latitude+","+longtitude);
//				}
				setCenter(list.get(0),13);
				drawLines();
				addMarker(list.get(0));
				break;
			case 2:
				Bitmap bm = (Bitmap) msg.obj;
				shipPics.setImageBitmap(bm);
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
        setContentView(R.layout.activity_main); 
        dataLoader = new Data_loader();
        mImageLoader = ImageLoader.getInstance();
        list = new ArrayList<ShipCooordData>();
		initView();
		getGpsData();
	}
	private void getGpsData() {

		Map<String, Object> apiParams = new HashMap<String, Object>();
		
		dataLoader.getZd_JavaManageResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg2) {
				super.onSuccess(arg2);
				
				list.clear();
				Gson gson = new Gson();
				HashMap<String, Object> map = gson.fromJson(
						(String) arg2,
						new TypeToken<Map<String, Object>>() {
						}.getType());

				if (map.get("returnCode").equals("Success")) {
					List<ShipCooordData> contacts = (List<ShipCooordData>) map
							.get("message");
					if (contacts != null && !contacts.isEmpty()) {
						for (int i = 0; i < contacts.size(); i++) {
							ShipCooordData userData = new ShipCooordData(
									(Map<String, Object>) contacts.get(i));
							list.add(userData);
							}
						}
					
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
				}
				
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(MainActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(MainActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(MainActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(MainActivity.this, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		}, "/client/push/getTestGps", apiParams, "get");
	
	}
	@Override
	protected void onStart() {
		super.onStart();
		
//		String[] latitudes=new String[]{"22.361122","22.361403","22.308838","22.308565","22.358107"};
//		String[] longitudes=new String[]{"113.959747","113.959817","114.147815","114.125832","114.114007"};
//		for(int i=0;i<longitudes.length;i++) {
//			positionDatas = ParseJson.parseJson(latitudes[i], longitudes[i]);
//		}
		
//		setCenter(new LatLng(39.963175, 116.400244), 13);
//		drawTwoPoints();
		
//		new Thread(){
//			public void run() {
//				text = HttpConnection.get();
//				Log.i("text:", text);
//				list = ParseJson.parser(text);
//				Message msg = new Message();
//				msg.what = 1;
//				msg.obj = list;
//				handler.sendMessage(msg);
//			};
//		}.start();
//		singleThreadGetImage();
//		for(int i=0;i<1440;i++) {
//			String date = CommonUtils.getFormattedTime(i*60000);
//			Log.i("", date);
//		}

//			
//		      String path = "http://www.hx-oil.com:8888/API/index_test.php?Function=2&"
//		      		+ "ShipID=0x1093&PictureTime=2016-05-01%20"+time+":00";
////		      ImageLoader.getInstance().displayImage(path,shipPics, MyApplication.displayImageOptions);
//					picture = HttpConnection.getImage(path);
//						Log.i("图片写入成功", "图片写入成功");
////						Runnable action = new Runnable() {
////							@Override
////							public void run() {
////								shipPics.setImageBitmap(picture);
////							}
////						};
////						runOnUiThread(action);
//					timer = new Timer(true);
//					mTimerTask = new MyTimerTask();
//					timer.schedule(mTimerTask, 2000, 100);
//				}
//			}
//		}.start();
	}
//	private void universalGetImage() {
//		for(int i=0;i<1440;i++) {
//			if(i<10) {
//				time = "00:0"+i;
//			} else if(i<60) {
//				time = "00:"+i;
//			}else if(i/60<10){
//				if(i%60<10) {
//					time = "0"+i/60+":0"+i%60;
//				}else{
//					time = "0"+i/60+":"+i%60;
//				}
//			} else if(i/60>=10) {
//				if(i%60<10) {
//					time = i/60+":0"+i%60;
//				}else{
//					time = i/60+":"+i%60;
//				}
//			}
//			Log.i("图片","第"+i+"张图片");
//			Log.i("time", time);
//				  String path = "http://www.hx-oil.com:8888/API/index_test.php?Function=2&"
//						  + "ShipID=0x1093&PictureTime=2016-05-01%20"+time+":00";
////		mImageLoader.init(MyApplication.imageLoaderConfiguration.build());
//		mImageLoader.displayImage(path, shipPics,MyApplication.displayImageOptions);
//		}
//	}
//	private void singleThreadGetImage() {
//		Log.i("开始时间",  System.currentTimeMillis()+"");
//		new Thread() {
//			public void run() {
//				for(int i=0;i<1440;i++) {
//					if(i<10) {
//						time = "00:0"+i;
//					} else if(i<60) {
//						time = "00:"+i;
//					}else if(i/60<10){
//						if(i%60<10) {
//							time = "0"+i/60+":0"+i%60;
//						}else{
//							time = "0"+i/60+":"+i%60;
//						}
//					} else if(i/60>=10) {
//						if(i%60<10) {
//							time = i/60+":0"+i%60;
//						}else{
//							time = i/60+":"+i%60;
//						}
//					}
//					Log.i("图片","第"+i+"张图片");
//					Log.i("time", time);
//						  String path = "http://www.hx-oil.com:8888/API/index_test.php?Function=2&"
//									  + "ShipID=0x1093&PictureTime=2016-05-01%20"+time+":00";
////					      String path = "http://www.hx-oil.com:8888/API/index_test.php?Function=2&"
////					    		  + "ShipID=0x1093&PictureTime=2016-05-01%2000:00:00";
//					      String imageName = "/"+i+".jpg";
////					      ImageLoader.getInstance().displayImage(path,shipPics, MyApplication.displayImageOptions);
//							  HttpConnection httpConnection = new HttpConnection();
//							  picture = httpConnection.getImage(path,imageName, new IgetImageFinish() {
//								  @Override
//								  public void getImageFinish() {
//									  b++;
//									  Log.i("图片数量", b+"");
//								  }
//							  });
//							  if(picture == null){
//								  Log.i("网络延迟", i+"图片获取失败");
//							  }else {
//								  Log.i("图片获取成功", i+"图片获取成功");
//								  Log.i("用时", i+"图片，用时："+System.currentTimeMillis()+"");
//									timer = new Timer(true);
//									mTimerTask = new MyTimerTask();
//									timer.schedule(mTimerTask, 2000, 100);
//							  }
//				}
//			};
//		}.start();
//	}
//	private void moreThreadGetImage(int threadCount){
//		Log.i("开始时间", System.currentTimeMillis()+"");
//		
//		for(int i=0;i<threadCount;i++) {
//			new Thread() {
//				int a=0;
//				int x=0;
//				public void run() {
//					for(int j=0;j<1440/x;j++) {
//						if(1440/x*a+j<10) {
//							time = "00:0"+j;
//						} else if(1440/x*a+j<60) {
//							time = "00:"+j;
//						}else if((1440/x*a+j)/60<10){
//							if((1440/x*a+j)%60<10) {
//								time = "0"+(1440/x*a+j)/60+":0"+(1440/x*a+j)%60;
//							}else{
//								time = "0"+(1440/x*a+j)/60+":"+(1440/x*a+j)%60;
//							}
//						} else if((1440/x*a+j)/60>=10) {
//							if((1440/x*a+j)%60<10) {
//								time = (1440/x*a+j)/60+":0"+(1440/x*a+j)%60;
//							}else{
//								time = (1440/x*a+j)/60+":"+(1440/x*a+j)%60;
//							}
//						}
//						Log.i("图片","第"+a+"条线程的第"+(1440/x*a+j)+"张图片");
//						Log.i("time", time);
//						String imageName = "/"+(1440/x*a+j)+".jpg";
//								  String path = "http://www.hx-oil.com:8888/API/index_test.php?Function=2&"
//										  + "ShipID=0x1093&PictureTime=2016-05-01%20"+time+":00";
//								  HttpConnection httpConnection = new HttpConnection();
//								  picture = httpConnection.getImage(path,imageName, new IgetImageFinish() {
//									  @Override
//									  public void getImageFinish() {
//										  b++;
//										  Log.i("获取图片的数量", b+"");
//									  }
//								  });
//								  if(picture == null){
//									  Log.i("网络延迟", (1440/x*a+j)+"图片获取失败");
//								  }else {
//									  Log.i("图片获取成功", (1440/x*a+j)+"图片获取成功");
//									  Log.i("用时", (1440/x*a+j)+"图片，用时："+System.currentTimeMillis()+"");
//						  }
//					}
//				}
//				public void _start(int i,int threadCount) {
//					a = i;
//					x = threadCount;
//					this.start();
//				};
//			}._start(i,threadCount);
//		}
//	}
//	private void moreThreadGetImage() {
//		Log.i("开始时间", System.currentTimeMillis()+"");
//		
//		for(int i=0;i<10;i++) {
//			new Thread() {
//				int a=0;
//				public void run() {
//					for(int j=0;j<144;j++) {
//						if(144*a+j<10) {
//							time = "00:0"+j;
//						} else if(144*a+j<60) {
//							time = "00:"+j;
//						}else if((144*a+j)/60<10){
//							if((144*a+j)%60<10) {
//								time = "0"+(144*a+j)/60+":0"+(144*a+j)%60;
//							}else{
//								time = "0"+(144*a+j)/60+":"+(144*a+j)%60;
//							}
//						} else if((144*a+j)/60>=10) {
//							if((144*a+j)%60<10) {
//								time = (144*a+j)/60+":0"+(144*a+j)%60;
//							}else{
//								time = (144*a+j)/60+":"+(144*a+j)%60;
//							}
//						}
//						Log.i("图片","第"+a+"条线程的第"+(144*a+j)+"张图片");
//						Log.i("time", time);
//						String imageName = "/"+(144*a+j)+".jpg";
//								  String path = "http://www.hx-oil.com:8888/API/index_test.php?Function=2&"
//										  + "ShipID=0x1093&PictureTime=2016-05-01%20"+time+":00";
////						      ImageLoader.getInstance().displayImage(path,shipPics, MyApplication.displayImageOptions);
//								  HttpConnection httpConnection = new HttpConnection();
//								  picture = httpConnection.getImage(path,imageName, new IgetImageFinish() {
//									  @Override
//									  public void getImageFinish() {
//										  b++;
//										  Log.i("获取图片的数量", b+"");
//									  }
//								  });
//								  if(picture == null){
//									  Log.i("网络延迟", (144*a+j)+"图片获取失败");
//								  }else {
//									  Log.i("图片获取成功", (144*a+j)+"图片获取成功");
//									  Log.i("用时", (144*a+j)+"图片，用时："+System.currentTimeMillis()+"");
//						  }
//					}
//					for(int x=0;x<1440;x++) {
//						 timer = new Timer(true);
//						 mTimerTask = new MyTimerTask();
//						 timer.schedule(mTimerTask, 2000, 100);
//					}
//				}
//				public void _start(int i) {
//					a = i;
//					this.start();
//				};
//			}._start(i);
//		}
//	}
	@Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
        mapHistory.onDestroy();  
    }  
    @Override  
    protected void onResume() {  
        super.onResume();  
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        mapHistory.onResume();  
        }  
    @Override  
    protected void onPause() {  
        super.onPause();  
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mapHistory.onPause();  
        } 

	private void initView() { 
		initMap();
		shipPics =(ImageView)findViewById(R.id.shipPic);
//		btSubmit.setOnClickListener(this);
//		universalGetImage();
		//		shipArea = (TextView)findViewById(R.id.shipArea);
//		shipTime = (TextView)findViewById(R.id.shipTime);
	}
	private void initMap() {
		mapHistory = (MapView) findViewById(R.id.mapHistory);
		bmHistory = mapHistory.getMap();
		this.uisHistory = this.bmHistory.getUiSettings();
		this.uisHistory.setOverlookingGesturesEnabled(false);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(BEIJING_LATLNG);
		this.bmHistory.setMapStatus(msu);
		
		msu = MapStatusUpdateFactory.zoomTo(13);
		this.bmHistory.setMapStatus(msu);
		this.bmHistory.setOnMarkerClickListener(this);
		this.bmHistory.setOnMapClickListener(this);
	}
	private void drawTwoPoints() {
		points = new ArrayList<LatLng>();
		LatLng one = new LatLng(39.963175, 116.400244);
		LatLng two = new LatLng(39.91923, 116.387428);
		LatLng three = new LatLng(39.89923, 116.367428);
		points.add(one);
		points.add(two);
		points.add(three);
		OverlayOptions ooPolyline = new PolylineOptions().width(10).color(0xAAFF0000).points(points);
		bmHistory.addOverlay(ooPolyline);
	}
	private void drawLines() {
		points = new ArrayList<LatLng>();
//		LatLng first = new LatLng(list.get(0).getLatitude(), 
//				list.get(0).getLongitude());
//		points.add(first);
		for(int i=0; i<list.size();i++){
//			LatLng cenpt =  MapConvertUtil.convertFromGPS(new LatLng(list.get(i).getLatitude(), 
//					list.get(i).getLongitude()));
			LatLng start =  new LatLng(list.get(i).getLatitude(), 
					list.get(i).getLongitude());
			points.add(start);
		}
		OverlayOptions ooPolyline = new PolylineOptions().width(10)
				.zIndex(20).color(0xAAFF0000).points(points);
		bmHistory.addOverlay(ooPolyline);
//			lastPoint = positionDatas.get(0);
//			String longitude = lastPoint.getLongitude();
//			String latitude = lastPoint.getLatitude();
//			LatLng first = MapConvertUtil.convertFromGPS(new LatLng(Double.parseDouble(latitude),
//					Double.parseDouble(longitude)));
//			points.add(first);
//			for(int i=1; i<positionDatas.size();i++){
//				LatLng start =  MapConvertUtil.convertFromGPS(new LatLng(Double.parseDouble(positionDatas.get(
//						i).getLatitude()), Double.parseDouble(positionDatas.get(i).getLongitude())));
//				//[latitude: 22.361775, longitude: 114.125546, latitude: 22.361783, longitude: 114.125537,
//				//latitude: 22.361775, longitude: 114.125537, latitude: 22.361775, longitude: 114.125546,
//				//latitude: 22.361783, longitude: 114.125546]
//				//GPSLongitude":113.959747,"GPSLatitude":22.361122
//				//GPSLongitude":113.959817,"GPSLatitude":22.361403
//				//GPSLongitude":114.147815,"GPSLatitude":22.308838
//				//GPSLongitude":114.125832,"GPSLatitude":22.308565
//				//GPSLongitude":114.114007,"GPSLatitude":22.358107
//				points.add(start);
//			}
//			if (points.size() > 1) {
//				OverlayOptions ooPolyline = new PolylineOptions().width(10)
//						.zIndex(20).color(0xAAFF0000).points(points);
//				bmHistory.addOverlay(ooPolyline);
//			}
//			lastPointIndex = 0;
	};
	private void addMarker(LatLng point) {
		// 定义Maker坐标点
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.ship);
		// 构建MarkerOption，用于在地图上添加Marker
		float cou = (float) (0.0F-point.latitude);
		OverlayOptions option = new MarkerOptions().position(point)
				.icon(bitmap).rotate(cou);
		marker = (Marker) (this.bmHistory.addOverlay(option));
		option = null;
		bitmap = null;
	}
	private void addMarker(ShipCooordData toPoint) {
		// 定义Maker坐标点
		// 构建Marker图标
//		LatLng point = MapConvertUtil.convertFromGPS( new LatLng(toPoint.getLatitude(),
//				toPoint.getLongitude()));
		LatLng point = new LatLng(toPoint.getLatitude(),
				toPoint.getLongitude());
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.shiping);
		// 构建MarkerOption，用于在地图上添加Marker
		float cou = (float) (0.0F-toPoint.getCourse()  );
		OverlayOptions option = new MarkerOptions().position(point)
				.icon(bitmap).rotate(cou);
		marker = (Marker) (this.bmHistory.addOverlay(option));
		option = null;
		bitmap = null;
	}
	
	public void setCenter(LatLng pd, int zoom) {
		double lat = pd.latitude;
		double lng = pd.longitude;
		if(setCenterFlag){
			setCenter(lat, lng, zoom);
			setCenterFlag = false;
		}
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
	public void onMapClick(LatLng arg0) {
		
	}
	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		return false;
	}
	@Override
	public boolean onMarkerClick(Marker arg0) {
		return false;
	}
	@Override
	public void onClick(View v) {
//			new Thread(){
//				public void run() {
//					String result = HttpConnection.post(useName, passWord);
//					Log.i("result:", result);
//				};
//			}.start();
	}
	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
//			Message msg = new Message();
//			msg.what = 2;
//			msg.obj = picture;
//			   String uri = "http://www.hx-oil.com:8888/API/index_test.php?Function=2&"
//			      		+ "ShipID=0x1093&PictureTime=2016-05-01%20"+time;
//					mImageLoader.getInstance().displayImage(uri, shipPics, MyApplication.displayImageOptions);
					Runnable action = new Runnable() {
						@Override
						public void run() {
							shipPics.setImageBitmap(picture);
						}
					};
					runOnUiThread(action);
		}
	}
}
