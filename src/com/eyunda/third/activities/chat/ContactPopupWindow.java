package com.eyunda.third.activities.chat;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hy.client.R;

public class ContactPopupWindow{
	private Activity context;
	protected PopupWindow popupWindow;// 弹出菜单
	protected boolean state = true;// 上下文菜单是否显示
	private View layout;

	public ContactPopupWindow(Activity context) {
		this.context = context;

		// 弹出菜单
		LayoutInflater inflater = LayoutInflater.from(context);
		layout = inflater.inflate(R.layout.eyd_contact_popup_view, null);
		popupWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, false);
		// 设置pop的内容
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
	}
	public  void closePopWindow() {
		state = true;
		popupWindow.dismiss();
	}

	public void togglePopWindow(View v) {
		if (state) {
	//		state = false;
			// 这个是显示在button下面X,Y位置
			popupWindow.showAsDropDown(v, 0, 0);
		} else {
			state = true;
			popupWindow.dismiss();

		}
	}
	public LinearLayout getBroker(){
		return (LinearLayout) layout.findViewById(R.id.broker);
	}
	public LinearLayout getHandler(){
		return (LinearLayout) layout.findViewById(R.id.handler);
	}
	public LinearLayout getSailor(){
		return (LinearLayout) layout.findViewById(R.id.sailor);
	}
	public LinearLayout getMaster(){
		return (LinearLayout) layout.findViewById(R.id.master);
	}
	public LinearLayout getOwner(){
		return (LinearLayout) layout.findViewById(R.id.owner);
	}
	public LinearLayout getMember(){
		return (LinearLayout) layout.findViewById(R.id.member);
	}
}
