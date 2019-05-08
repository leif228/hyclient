package com.hangyi.zd.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.NewPageHomeMainActivity;
import com.eyunda.third.domain.location.ShipCooordData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.tools.Util;
import com.hangyi.zd.adapter.DownGroupAdapter;
import com.hangyi.zd.adapter.SearchShipAdapter;
import com.hangyi.zd.domain.GroupData;
import com.hangyi.zd.widge.SearchView;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class HXDownPopupWindow implements
OnItemClickListener,View.OnClickListener{
	Data_loader data;
	DialogUtil dialogUtil;
	ProgressDialog dialog;
	private Activity context;
	protected PopupWindow popupWindow;// 弹出菜单
	protected boolean state = true;// 上下文菜单是否显示
	private View layout;
	private ListView mlistView;
	private DownGroupAdapter smpAdapter;
	List<GroupData> totalShipCooordDatas = new ArrayList<GroupData>();
	List<GroupData> displayShipCooordDatas = new ArrayList<GroupData>();
	SearchView.SearchViewListener listener;
	boolean isOpen = false;
	GroupResultListener groupListener;
	
	public interface GroupResultListener{
		void getResult(GroupData groupData);
	}
	public void setGroupListener(GroupResultListener groupListener){
		this.groupListener = groupListener;
	}

	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	public HXDownPopupWindow(Activity context) {
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
		layout = inflater.inflate(R.layout.zd_popup_view, null);
		popupWindow = new PopupWindow(layout, 300,
				600, false);
		// 设置pop的内容
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		
		mlistView = (ListView) layout.findViewById(R.id.lv);
		smpAdapter = new DownGroupAdapter(context, displayShipCooordDatas);
		mlistView.setAdapter(smpAdapter);
		mlistView.setOnItemClickListener(this);
	}
	public void setListener(SearchView.SearchViewListener listener){
		this.listener = listener;
	}
	
	public void setData(List<GroupData> totals, String gType){

		totalShipCooordDatas.clear();
		displayShipCooordDatas.clear();
		
		if (totals != null && totals.size() > 0) {
			totalShipCooordDatas.addAll(totals);
			
			if(HXUpPopupWindow.gport.equals(gType)){
				displayShipCooordDatas.addAll(totalShipCooordDatas);
			}else{
				for(GroupData g:totalShipCooordDatas){
					//0表示客户，1表示船公司
					if(HXUpPopupWindow.gkehu.equals(gType)&&"0".equals(g.getGroupType())){
						displayShipCooordDatas.add(g);
					}else if(HXUpPopupWindow.gship.equals(gType)&&"1".equals(g.getGroupType())){
						displayShipCooordDatas.add(g);
					}
				}
			}
		}
		smpAdapter.notifyDataSetChanged();
		
		if(displayShipCooordDatas.isEmpty())
			closePopWindow();
	}
	
	public  void closePopWindow() {
		state = true;
		popupWindow.dismiss();
		isOpen = false;
	}

	public void showPopWindow(View v) {
		if (!popupWindow.isShowing()) {
			Rect r = new Rect();
			v.getGlobalVisibleRect(r);
			int yt = r.top;
			int yb = r.bottom;
			
			int i1 = -(popupWindow.getHeight()+(yb-yt));
			popupWindow.showAsDropDown(v, 200, i1);
			isOpen = true;
			state = false;
//			popupWindow.dismiss();

		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		GroupData map = (GroupData) mlistView.getItemAtPosition(position);
		if(map.isPort())
			groupListener.getResult(map);
		else
			postSC(map);
		
		closePopWindow();
	}
	private void postSC(final GroupData map) {
		for(GroupData g:totalShipCooordDatas){
			if(g.getGroupID().equals(map.getGroupID())){
				g.setFlag(true);
			}else{
				g.setFlag(false);
			}
		}
		
		String PHPSESSID = "";
        List<Cookie> list = GlobalApplication.getInstance().getCookies();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("PHPSESSID")) {
            	PHPSESSID = cookie.getValue();
            }
        }
		
		Gson s = new Gson();
		String json = s.toJson(totalShipCooordDatas);

		Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("groupDatas", json);
		apiParams.put("PHPSESSID", PHPSESSID);
		
		data.getZd_JavaManageResult(new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("加载中", "请稍候...");
			}
			@Override
			public void onSuccess(String arg2) {
				dialog.dismiss();
				
				Gson gson = new Gson();
				final HashMap<String, Object> rmap = gson.fromJson(
						arg2, new TypeToken<Map<String, Object>>() {
						}.getType());
				
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						groupListener.getResult(map);
					}
				});
//				Toast.makeText(NewShipGroupActivity.this, (String)rmap.get("message"), Toast.LENGTH_SHORT).show();
				
//				Intent intent = new Intent(NewShipGroupActivity.this,
//						NewPageHomeMainActivity.class);
//				intent.putExtra("isPost", false);
//				
//				setResult(RESULT_OK, intent);
//				finish();
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
		}, ApplicationUrls.saveMobGroups, apiParams, "post");	
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
