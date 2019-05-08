package com.eyunda.third.activities.ship.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.hy.client.R;


public class AlertSearchDialog {
     private Activity context;
     private View view;
	 private Builder ad;
	
	public AlertSearchDialog(Activity context) {
		LayoutInflater  inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.search_ship_item, null);
		AlertDialog.Builder ad =	new AlertDialog.Builder(context);
		this.context =context;
		this.view = view;
		this.ad =ad;
	}
	public void showAddDialog(DialogInterface.OnClickListener lis) {
		
		 ad.setTitle("查找船舶")
		.setView(view)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setNegativeButton("取消", null)
		.setPositiveButton("查找", lis)
		.show();
		
	}
	public void setTitle(CharSequence s){
		ad.setTitle(s);
	}
	
	public View getView() {
		return view;
	}
	public void setView(View view) {
		this.view = view;
	}
	
}
