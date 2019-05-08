package com.hangyi.zd.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.NewPageHomeMainActivity;
import com.eyunda.third.adapters.chat.widget.photoview.PhotoView;
import com.eyunda.tools.DateUtils;
import com.hangyi.zd.activity.gridviewpage.AppAdapter;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * 下载显示大图
 * 
 */
public class ShowBigImage extends Activity {
	
	ImageLoader mImageLoader;
	private PhotoView image;
	private ProgressBar mProgress;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				showShipImg();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		};
	};
	private String shipId;
	private String shipName;
	private int channel;
	
	private Timer shipImgTimer;
	private ShipImgTimerTask shipImgTask;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_big_image);
		
		ImageLoader.getInstance().destroy();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(GlobalApplication.getInstance().getImageLoaderConfiguration());

		image = (PhotoView) findViewById(R.id.image);
		mProgress = (ProgressBar) findViewById(R.id.pb_load_local);

		shipId = getIntent().getExtras().getString("shipId");
		shipName = getIntent().getExtras().getString("shipName");
		channel = getIntent().getExtras().getInt("channel", 0);
		
		startShipImgTimer();
	}
	
	protected void showShipImg() {
		mImageLoader.displayImage(ApplicationConstants.ZDPHP_PRE_URL+ "/clientapi/?Function=3&ShipID="+shipId+"&Channel="+channel+"&PictureTime="+ ContentFragment.getCurrTimeStr(),
				image,
				NewPageHomeMainActivity.options,new ImageLoadingListener(){

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				((ImageView)view).setImageDrawable(AppAdapter.createDrawable(ShowBigImage.this,loadedImage,shipName,DateUtils.getTime("yyyy-MM-dd HH:mm:ss")));
			}

			@Override
			public void onLoadingFailed(String arg0, View view,
					FailReason arg2) {
				((ImageView)view).setDrawingCacheEnabled(true);
				((ImageView)view).setImageDrawable(AppAdapter.createDrawable(ShowBigImage.this,((ImageView)view).getDrawingCache(),shipName,DateUtils.getTime("yyyy-MM-dd HH:mm:ss")));
				((ImageView)view).setDrawingCacheEnabled(false);
				
			}

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}});
	}

	private void startShipImgTimer() {
		if (shipImgTimer != null) {
			if (shipImgTask != null) {
				shipImgTask.cancel(); // 将原任务从队列中移除
			}
		}else{
			shipImgTimer = new Timer(true);
		}
		shipImgTask = new ShipImgTimerTask();
		shipImgTimer.schedule(shipImgTask,0,1*60*1000); 
	}
	
	class ShipImgTimerTask extends TimerTask{
		public ShipImgTimerTask() {
		}

		@Override
		public void run() {
			Message message = new Message();
			message.what = 2;
			handler.sendMessage(message);
		}
	};

	@Override
	public void onBackPressed() {
//		interceptFlag = true;
//		if (file.exists())
//			file.delete();
		finish();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (shipImgTask != null) {
			shipImgTask.cancel(); // 将原任务从队列中移除
			shipImgTask = null;
		}
		if(shipImgTimer!=null){
			shipImgTimer.cancel();
			shipImgTimer.purge();
			shipImgTimer = null;
		}
	}
	

}
