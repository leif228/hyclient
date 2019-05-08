package com.eyunda.third.activities.user;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hy.client.R;

public class BasePopupWindow{
	private Activity context;
	protected PopupWindow popupWindow;// 弹出菜单
	protected boolean state = true;// 上下文菜单是否显示
	private View layout;

	public BasePopupWindow(Activity context) {
		this.context = context;

		// 弹出菜单
		LayoutInflater inflater = LayoutInflater.from(context);
		layout = inflater.inflate(R.layout.eyd_user_popup_view, null);
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
	public LinearLayout getBtnA(){
		return (LinearLayout) layout.findViewById(R.id.menu_baseic);
	}
	public LinearLayout getBtnB(){
		return (LinearLayout) layout.findViewById(R.id.menu_account);
	}
	public LinearLayout getBtnC(){
		return (LinearLayout) layout.findViewById(R.id.menu_apply_agent);
	}
	public LinearLayout getBtnD(){
		return (LinearLayout) layout.findViewById(R.id.menu_agent);
	}
//	public LinearLayout getBtnE(){
//		return (LinearLayout) layout.findViewById(R.id.menu_agent_apply);
//	}
	public LinearLayout getBtnF(){
		return (LinearLayout) layout.findViewById(R.id.menu_update_pwd);
	}
	public LinearLayout getBtnG(){
		return (LinearLayout) layout.findViewById(R.id.menu_userchild);
	}
	public LinearLayout getBtnH(){
		return (LinearLayout) layout.findViewById(R.id.menu_bindcard_paypwd);
	}
}
