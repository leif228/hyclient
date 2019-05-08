package com.eyunda.third.activities.chat;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyunda.third.activities.user.BasicInfoActivity;
import com.hy.client.R;
import com.ta.util.extend.draw.ImageUtils;

public class AlertDialog extends BaseActivity {
	private TextView mTextView;
	private Button mButton;
	private int position;
	private ImageView imageView;
	private EditText editText;
	private boolean isEditextShow;
	private String voicePath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_chat_alert_dialog);
		mTextView = (TextView) findViewById(R.id.title);
		mButton = (Button) findViewById(R.id.btn_cancel);
		imageView = (ImageView) findViewById(R.id.image);
		editText = (EditText) findViewById(R.id.edit);
		//提示内容
		String msg = getIntent().getStringExtra("msg");
		//提示标题
		String title = getIntent().getStringExtra("title");
//		voicePath = getIntent().getStringExtra("voicePath");
		position = getIntent().getIntExtra("position", -1);
		//是否显示取消标题
		boolean isCanceTitle=getIntent().getBooleanExtra("titleIsCancel", false);
		//是否显示取消按钮
		boolean isCanceShow = getIntent().getBooleanExtra("cancel", false);
		//是否显示文本编辑框
		isEditextShow = getIntent().getBooleanExtra("editTextShow",false);
		//转发复制的图片的path
		String path = getIntent().getStringExtra("forwardImage");
		if(msg != null)
		    ((TextView)findViewById(R.id.alert_message)).setText(msg);
		if(title != null)
			mTextView.setText(title);
		if(isCanceTitle){
			mTextView.setVisibility(View.GONE);
		}
		if(isCanceShow)
			mButton.setVisibility(View.VISIBLE);
//		if(path != null){
//			 //优先拿大图，没有去取缩略图
//			if(!new File(path).exists())
//				path = DownloadImageTask.getThumbnailImagePath(path);
//		    imageView.setVisibility(View.VISIBLE);
//		    ((TextView)findViewById(R.id.alert_message)).setVisibility(View.GONE);
//		    if(ImageCache.getInstance().get(path) != null){
//		        imageView.setImageBitmap(ImageCache.getInstance().get(path));
//		    }else{
//		        Bitmap bm = ImageUtils.decodeScaleImage(path, 150, 150);
//		        imageView.setImageBitmap(bm);
//		        ImageCache.getInstance().put(path, bm);
//		    }
//		    
//		}
		if(isEditextShow){
			editText.setVisibility(View.VISIBLE);
			
		}
	}
	
	public void ok(View view){
		setResult(RESULT_OK,new Intent().putExtra("position", position).
				putExtra("edittext", editText.getText().toString()).putExtra("btnSure", true).putExtra("chatbtnSure", true)
				/*.putExtra("voicePath", voicePath)*/);
		if(position != -1)
			ChatActivity.resendPos = position;
		finish();
		
	}
	
	public void cancel(View view){
		setResult(RESULT_OK,new Intent().putExtra("btnSure", false).putExtra("chatbtnSure", false)
				/*.putExtra("voicePath", voicePath)*/);
		
		finish();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}

	 

	

}
