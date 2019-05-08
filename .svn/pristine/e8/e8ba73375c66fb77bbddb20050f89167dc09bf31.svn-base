package com.hangyi.zd.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.hangyi.zd.adapter.SearchShipAdapter3;
import com.hangyi.zd.domain.UserPowerShipData;
import com.hangyi.zd.widge.SearchView;
import com.hy.client.R;

public class BasePopupWindow implements
OnItemClickListener,View.OnClickListener{
	private Activity context;
	protected PopupWindow popupWindow;// 弹出菜单
	protected boolean state = true;// 上下文菜单是否显示
	private View layout;
	private ListView mlistView;
	private SearchShipAdapter3 smpAdapter;
	List<UserPowerShipData> shipCooordDatas = new ArrayList<UserPowerShipData>();
	SearchView.SearchViewListener listener;
	boolean isOpen = false;

	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	public BasePopupWindow(Activity context) {
		this.context = context;
		
		int imgDisplayH = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();

		// 弹出菜单
		LayoutInflater inflater = LayoutInflater.from(context);
		layout = inflater.inflate(R.layout.zd_popup_view_searchship, null);
		popupWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
				imgDisplayH/4, false);
		// 设置pop的内容
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
//		popupWindow.setOutsideTouchable(true);
//		popupWindow.setFocusable(true);
		
		mlistView = (ListView) layout.findViewById(R.id.lv);
		smpAdapter = new SearchShipAdapter3(context, shipCooordDatas);
		mlistView.setAdapter(smpAdapter);
		mlistView.setOnItemClickListener(this);
	}
	public void setListener(SearchView.SearchViewListener listener){
		this.listener = listener;
	}
	
	public void setData(List<UserPowerShipData> ss){

		shipCooordDatas.clear();
		if (ss != null && ss.size() > 0) {
			shipCooordDatas.addAll(ss);
		}else{
//			Toast.makeText(context, "权限数据加载失败，请退出重新加载！", Toast.LENGTH_SHORT).show();
//			closePopWindow();
		}
		smpAdapter.notifyDataSetChanged();
	
	}
	
	public  void closePopWindow() {
		state = true;
		popupWindow.dismiss();
		isOpen = false;
	}

	public void togglePopWindow(View v) {
		if (state) {
			state = false;
			// 这个是显示在button下面X,Y位置
			popupWindow.showAsDropDown(v, 50, 0);
			isOpen = true;
		} else {
//			state = true;
//			isOpen = false;
//			popupWindow.dismiss();

		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		UserPowerShipData map = (UserPowerShipData) mlistView.getItemAtPosition(position);
		listener.onSearch(map.getShipName());
		
		return;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
