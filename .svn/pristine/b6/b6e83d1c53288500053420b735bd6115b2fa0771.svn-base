package com.hangyi.zd.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.domain.location.ShipCooordData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.tools.Util;
import com.hangyi.zd.activity.HXDownPopupWindow.GroupResultListener;
import com.hangyi.zd.adapter.SearchShipAdapter;
import com.hangyi.zd.adapter.SearchShipAdapter1;
import com.hangyi.zd.domain.GroupCode;
import com.hangyi.zd.domain.GroupData;
import com.hangyi.zd.domain.MapPortData;
import com.hangyi.zd.widge.SearchView;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class HXUpPopupWindow implements
OnItemClickListener,View.OnClickListener{
	Data_loader data;
	DialogUtil dialogUtil;
	ProgressDialog dialog;
	private Activity context;
	protected PopupWindow popupWindow;// 弹出菜单
	protected boolean state = true;// 上下文菜单是否显示
	private View layout;
	private ListView mlistView;
	private SearchShipAdapter1 smpAdapter;
	List<ShipCooordData> ShipCooordDatas = new ArrayList<ShipCooordData>();
	SearchView.SearchViewListener listener;
	public static final String gport = GroupCode.gport.getDescription();
	public static final String gship = GroupCode.gship.getDescription();
	public static final String gkehu = GroupCode.gkehu.getDescription();
	boolean isOpen = false;
	HXDownPopupWindow downPW;
	View viewd;
	Thread loadDataThread = null;
	Thread loadDataThread1 = null;
	List<GroupData> shipSCDataList;
	String gType = gport;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				dialog = dialogUtil.loading("加载中", "请稍候...");
				break;
			case 1:
				dialog.dismiss();
				downPW.showPopWindow(viewd);
				downPW.setData(shipSCDataList,gType);
				break;
			case 2:
				dialog.dismiss();
				Toast.makeText(context, "数据异常，请稍后再试！",
						Toast.LENGTH_LONG).show();
				break;
			case 3:
				dialog.dismiss();
				Toast.makeText(context, "加载数据失败！", Toast.LENGTH_LONG).show();
				break;
			case 4:
				dialog.dismiss();
				Toast.makeText(context, "数据为空！", Toast.LENGTH_LONG).show();
				break;

			default:
				break;
			}
		}
	};

	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	public HXUpPopupWindow(Activity context) {
		this.context = context;
		data = new Data_loader();
		dialogUtil = new DialogUtil(context);
		
		//屏幕的真实尺寸  
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);    
        Display mDisplay = mWindowManager.getDefaultDisplay();    
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();    
        mDisplay.getRealMetrics(mDisplayMetrics);    
        int realScreenW = mDisplayMetrics.widthPixels;  
        int realScreenH = mDisplayMetrics.heightPixels;  

		// 弹出菜单
		LayoutInflater inflater = LayoutInflater.from(context);
		layout = inflater.inflate(R.layout.zd_popup_view2, null);
		popupWindow = new PopupWindow(layout,200,
				360, false);
		// 设置pop的内容
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		
		setData();
		
		mlistView = (ListView) layout.findViewById(R.id.lv);
		smpAdapter = new SearchShipAdapter1(context, ShipCooordDatas);
		mlistView.setAdapter(smpAdapter);
		mlistView.setOnItemClickListener(this);
		
		downPW = new HXDownPopupWindow(context);
		downPW.setGroupListener((GroupResultListener)context);
	}
	
	public void setData(){
		ShipCooordData s = new ShipCooordData();
		s.setShipName(gport);
		ShipCooordDatas.add(s);
		ShipCooordData s1 = new ShipCooordData();
		s1.setShipName(gship);
		ShipCooordDatas.add(s1);
		ShipCooordData s2 = new ShipCooordData();
		s2.setShipName(gkehu);
		ShipCooordDatas.add(s2);
	}
	
	public  void closePopWindow() {
		state = true;
		popupWindow.dismiss();
		
		downPW.closePopWindow();
		isOpen = false;
		
		if(loadDataThread!=null){
			loadDataThread.interrupt();
			loadDataThread = null;
		}
		if(loadDataThread1!=null){
			loadDataThread1.interrupt();
			loadDataThread1 = null;
		}
	}
	
	public static int gety(View v,Activity context){
		//屏幕的真实尺寸  
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);    
        Display mDisplay = mWindowManager.getDefaultDisplay();    
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();    
        mDisplay.getRealMetrics(mDisplayMetrics);    
        int realScreenW = mDisplayMetrics.widthPixels;  
        int realScreenH = mDisplayMetrics.heightPixels;  
        
      //方法二，注意要等到窗口加载好后调用  
        Rect frame = new Rect();    
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);    
        int statusBarHeight = frame.top; //状态栏高度 
		
		Rect r = new Rect();
		v.getGlobalVisibleRect(r);
		int y = r.top;
		
		return -(realScreenH-y-statusBarHeight);
	}

	public void togglePopWindow(View v) {
		this.viewd = v;
		if(popupWindow.isShowing()){
			closePopWindow();
			isOpen = false;
		}else{
			Rect r = new Rect();
			v.getGlobalVisibleRect(r);
			int yt = r.top;
			int yb = r.bottom;
			
			int i1 = -(popupWindow.getHeight()+(yb-yt));
			popupWindow.showAsDropDown(v, 0, i1);
			isOpen = true;
		}
		
//		if (state) {
//			state = false;
//			// 这个是显示在button下面X,Y位置
//			popupWindow.showAsDropDown(v, 0, -530);
//			isOpen = true;
//		} else {
//			state = true;
////			popupWindow.dismiss();
//			closePopWindow();
//
//		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ShipCooordData map = (ShipCooordData) mlistView.getItemAtPosition(position);
		if(map.getShipName().equals(gport)){
			gType = gport;
			List<GroupData> list = getPort();
			if(list.size()>0){
				downPW.showPopWindow(viewd);
				downPW.setData(list,gType);
			}
		}else if(map.getShipName().equals(gkehu)){
			gType = gkehu;
			loadDataThread = new Thread(new Runnable() {
				@Override
				public void run() {
					getHx(gkehu);
				}
			});
			loadDataThread.setDaemon(true);
			loadDataThread.start();
			
		}else if(map.getShipName().equals(gship)){
			gType = gship;
			loadDataThread1 = new Thread(new Runnable() {
				@Override
				public void run() {
					getHx(gship);
				}
			});
			loadDataThread1.setDaemon(true);
			loadDataThread1.start();
		}
		
	}
	private void getHx(String type){
		shipSCDataList = new ArrayList<GroupData>();
		
		String PHPSESSID = "";
        List<Cookie> list = GlobalApplication.getInstance().getCookies();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("PHPSESSID")) {
            	PHPSESSID = cookie.getValue();
            	break;
            }
        }

		Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("PHPSESSID", PHPSESSID);
		
		data.getZd_JavaManageResult(new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				handler.sendEmptyMessage(0);
			}
			@Override
			public void onSuccess(String arg2) {
				
				try {
					Gson gson = new Gson();
					final HashMap<String, Object> rmap = gson.fromJson(
							arg2, new TypeToken<Map<String, Object>>() {
							}.getType());
					if (rmap.get("returnCode").equals("Success")) {
						
						List<Map<String, Object>> map = (List<Map<String, Object>>) rmap.get("groupDatas");
						
						for(Map<String, Object> m:map){
							GroupData gd = new GroupData(m);
							shipSCDataList.add(gd);
						}
						if(shipSCDataList.size()>0){
							
							handler.sendEmptyMessage(1);
							
						}else{
							handler.sendEmptyMessage(4);
						}
					}else
						handler.sendEmptyMessage(3);;
				} catch (Exception e) {
					handler.sendEmptyMessage(3);
				}
				
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(context, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(context, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(context, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(context, "未知异常",
							Toast.LENGTH_LONG).show();

			}
		}, ApplicationUrls.groups, apiParams, "get");	
		}
	private List<GroupData> getPort(){
		List<GroupData> shipCooordDatas = new ArrayList<GroupData>();
		
		SharedPreferences sp = context.getSharedPreferences(ApplicationConstants.MapPortData_SharedPreferences, Context.MODE_PRIVATE);
		String port = sp.getString("MapPort", "");

		Gson gson = new Gson();
		List<MapPortData> rmap = gson.fromJson((String) port,
				new TypeToken<List<MapPortData>>() {
				}.getType());
		if(rmap!=null&&rmap.size()>0){
			for(MapPortData m:rmap){
				GroupData g = new GroupData();
				g.setGroupName(m.getPort_name());
				g.setTempx(m.getX());
				g.setTempy(m.getY());
				g.setPort(true);
				
				shipCooordDatas.add(g);
			}
		}else{
			Toast.makeText(context, "码头数据加载失败！", Toast.LENGTH_LONG).show();
		}
		
		return shipCooordDatas;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
