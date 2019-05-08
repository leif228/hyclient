package com.eyunda.main.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ygl.android.config.Config;

public class QuitDialog extends LinearLayout {
	Context context;
	public TextView title;
	public TextView content;
	public Button ok, center, cancle;
	private float I = 0.0f;

	public QuitDialog(Context context) {
		super(context);

		this.context = context;

	}

	public QuitDialog Show2() {
		this.setOrientation(VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		LinearLayout lay1 = new LinearLayout(context);
		lay1.setPadding(0, 5, 0, 5);
		lay1.setGravity(Gravity.CENTER_VERTICAL);
		LinearLayout lay2 = new LinearLayout(context);
		lay2.setOrientation(VERTICAL);
		this.addView(lay1, params);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		lay2.setPadding(5, 5, 5, 5);
		this.addView(lay2, params);
		lay1.setBackgroundColor(Config.ColorStyle);
		ImageView infoLog = new ImageView(context);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		infoLog.setBackgroundResource(android.R.drawable.ic_dialog_info);
		params.setMargins(5, 0, 0, 0);
		lay1.addView(infoLog, params);
		title = new TextView(context);
		title.setTextColor(Color.WHITE);
		title.setTextSize(20.0f + I);
		title.setText("退出");
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(5, 0, 0, 0);
		lay1.addView(title, params);

		content = new TextView(context);

		content.setTextColor(Color.BLACK);
		content.setTextSize(18.0f + I);
		content.setText("是否退出？ ");
		content.setMinWidth(DialogUtil.WIDTH * 4 / 5);
		lay2.setBackgroundColor(Color.WHITE);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT, 1.0f);
		params.setMargins(5, 8, 5, 5);
		lay2.addView(content, params);

		LinearLayout buttonLayout = new LinearLayout(context);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 10, 0, 0);
		lay2.addView(buttonLayout, params);
		ok = new Button(context);
		ok.setText("确定");
		ok.setGravity(Gravity.CENTER);
		ok.setTextColor(Color.BLACK);
		ok.setTextSize(16.0f + I);
		ok.setPadding(5, 5, 5, 5);
		cancle = new Button(context);
		cancle.setText("取消");
		cancle.setTextColor(Color.BLACK);
		cancle.setPadding(5, 5, 5, 5);
		cancle.setTextSize(16.0f + I);
		cancle.setGravity(Gravity.CENTER);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT, 1.0f);
		params.leftMargin = 10;
		buttonLayout.addView(ok, params);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT, 1.0f);
		params.rightMargin = 10;
		buttonLayout.addView(cancle, params);
		return this;

	}

	public QuitDialog show3() {
		this.setOrientation(VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		LinearLayout lay1 = new LinearLayout(context);
		lay1.setPadding(0, 5, 0, 5);
		lay1.setGravity(Gravity.CENTER_VERTICAL);
		LinearLayout lay2 = new LinearLayout(context);
		lay2.setOrientation(VERTICAL);
		this.addView(lay1, params);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		lay2.setPadding(5, 5, 5, 5);
		this.addView(lay2, params);
		lay1.setBackgroundColor(Config.ColorStyle);
		ImageView infoLog = new ImageView(context);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		infoLog.setBackgroundResource(android.R.drawable.ic_dialog_info);
		params.setMargins(5, 0, 0, 0);
		lay1.addView(infoLog, params);
		title = new TextView(context);
		title.setTextColor(Color.WHITE);
		title.setTextSize(20.0f + I);
		title.setText("提示");
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(5, 0, 0, 0);
		lay1.addView(title, params);

		content = new TextView(context);

		content.setTextColor(Color.BLACK);
		content.setTextSize(18.0f + I);
		content.setText("  选择文件 ");
		content.setMinWidth(DialogUtil.WIDTH * 4 / 5);
		lay2.setBackgroundColor(Color.WHITE);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT, 1.0f);
		params.setMargins(5, 8, 5, 5);
		lay2.addView(content, params);

		LinearLayout buttonLayout = new LinearLayout(context);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 10, 0, 0);
		lay2.addView(buttonLayout, params);
		ok = new Button(context);
		ok.setText("拍照");
		ok.setGravity(Gravity.CENTER);
		ok.setTextColor(Color.BLACK);
		ok.setTextSize(16.0f + I);
		ok.setPadding(5, 5, 5, 5);
		center = new Button(context);
		center.setText("手机照片");
		center.setGravity(Gravity.CENTER);
		center.setTextColor(Color.BLACK);
		center.setTextSize(16.0f + I);
		center.setPadding(5, 5, 5, 5);

		cancle = new Button(context);
		cancle.setText("取消");
		cancle.setTextColor(Color.BLACK);
		cancle.setPadding(5, 5, 5, 5);
		cancle.setTextSize(16.0f + I);
		cancle.setGravity(Gravity.CENTER);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT, 1.0f);
		params.leftMargin = 10;
		buttonLayout.addView(ok, params);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT, 1.0f);
		buttonLayout.addView(center, params);

		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT, 1.0f);
		params.rightMargin = 10;
		buttonLayout.addView(cancle, params);
		return this;
	}
	
	
	
	public QuitDialog show3(String top,String msg,String button1,String button2,String button3) {
		this.setOrientation(VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		LinearLayout lay1 = new LinearLayout(context);
		lay1.setPadding(0, 5, 0, 5);
		lay1.setGravity(Gravity.CENTER_VERTICAL);
		LinearLayout lay2 = new LinearLayout(context);
		lay2.setOrientation(VERTICAL);
		this.addView(lay1, params);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		lay2.setPadding(5, 5, 5, 5);
		this.addView(lay2, params);
		lay1.setBackgroundColor(Config.ColorStyle);
		ImageView infoLog = new ImageView(context);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		infoLog.setBackgroundResource(android.R.drawable.ic_dialog_info);
		params.setMargins(5, 0, 0, 0);
		lay1.addView(infoLog, params);
		title = new TextView(context);
		title.setTextColor(Color.WHITE);
		title.setTextSize(20.0f + I);
		title.setText(top);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(5, 0, 0, 0);
		lay1.addView(title, params);

		content = new TextView(context);

		content.setTextColor(Color.BLACK);
		content.setTextSize(18.0f + I);
		content.setText(msg);
		content.setMinWidth(DialogUtil.WIDTH * 4 / 5);
		lay2.setBackgroundColor(Color.WHITE);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT, 1.0f);
		params.setMargins(5, 8, 5, 5);
		lay2.addView(content, params);

		LinearLayout buttonLayout = new LinearLayout(context);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 10, 0, 0);
		lay2.addView(buttonLayout, params);
		ok = new Button(context);
		ok.setText(button1);
		ok.setGravity(Gravity.CENTER);
		ok.setTextColor(Color.BLACK);
		ok.setTextSize(16.0f + I);
		ok.setPadding(5, 5, 5, 5);
		center = new Button(context);
		center.setText(button2);
		center.setGravity(Gravity.CENTER);
		center.setTextColor(Color.BLACK);
		center.setTextSize(16.0f + I);
		center.setPadding(5, 5, 5, 5);

		cancle = new Button(context);
		cancle.setText(button3);
		cancle.setTextColor(Color.BLACK);
		cancle.setPadding(5, 5, 5, 5);
		cancle.setTextSize(16.0f + I);
		cancle.setGravity(Gravity.CENTER);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT, 1.0f);
		params.leftMargin = 10;
		buttonLayout.addView(ok, params);
		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT, 1.0f);
		buttonLayout.addView(center, params);

		params = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT, 1.0f);
		params.rightMargin = 10;
		buttonLayout.addView(cancle, params);
		return this;
	}
	
	

}
