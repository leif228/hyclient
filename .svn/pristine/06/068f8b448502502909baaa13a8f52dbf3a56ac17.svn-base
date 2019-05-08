package com.eyunda.main.reg;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.hy.client.R;
import com.ygl.android.ui.BaseActivity;

public class EditImage extends BaseActivity{
	
	Button b1,b2,b3;
	ImageView i1;
	Bitmap bmp;
	PhotoUtil photoUtil;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.img_load);
		b1=(Button) findViewById(R.id.b1);
		b2=(Button) findViewById(R.id.b2);
		b3=(Button) findViewById(R.id.b3);
		i1=(ImageView) findViewById(R.id.i1);
		photoUtil=new PhotoUtil();
		/** 获取可見区域高度 **/
		WindowManager manager = getWindowManager();
		window_width = manager.getDefaultDisplay().getWidth();
		window_height = manager.getDefaultDisplay().getHeight();
		 path=getIntent().getStringExtra("path");
		 if(path!=null){
			 
			 bmp=photoUtil.getBitmapFormPath(path);
			 i1.setImageBitmap(bmp);
			 
		 }
		 b1.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				bmp=gerZoomRotateBitmap(bmp,-90);
				i1.setImageBitmap(bmp);
			}
		});
		 
		 b2.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					bmp=gerZoomRotateBitmap(bmp,90);
					i1.setImageBitmap(bmp);
				}
			});
		 b3.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					photoUtil.save(bmp, path);
					Intent intent=new Intent();
					intent.putExtra("path", path);
					setIntent(intent);
					setResult(44)	;
					finish();
					
				}
			});
		 
		 
	}
	private int window_width, window_height;// 控件宽度
	String path="";
	/**
	 * 缩放、翻转和旋转图片
	 * 
	 * @param bmpOrg
	 * @param rotate
	 * @return
	 */
	private Bitmap gerZoomRotateBitmap(Bitmap bmpOrg, int rotate) {
		// 获取图片的原始的大小
		int width = bmpOrg.getWidth();
		int height = bmpOrg.getHeight();

		int newWidth = window_width;
		int newheight = window_height;
		// 定义缩放的高和宽的比例
		float sw = ((float) newWidth) / width;
		float sh = ((float) newheight) / height;
		// 创建操作图片的用的Matrix对象
		android.graphics.Matrix matrix = new android.graphics.Matrix();
		// 缩放翻转图片的动作
		// sw sh的绝对值为绽放宽高的比例，sw为负数表示X方向翻转，sh为负数表示Y方向翻转
//		matrix.postScale(sw, sh);
		// 旋转30*
		matrix.postRotate(rotate);
		// 创建一个新的图片
		android.graphics.Bitmap resizeBitmap = android.graphics.Bitmap
				.createBitmap(bmpOrg, 0, 0, width, height, matrix, true);
		bmp=resizeBitmap;
		return resizeBitmap;
	}

}
