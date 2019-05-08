package com.eyunda.third.activities.chat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eyunda.main.update.UpdateManager;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.chat.widget.photoview.PhotoView;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.tools.ImageCompress;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 下载显示大图
 * 
 */
public class ShowBigImage extends Activity {
	
	final static String DOWNLOAD_DIR = "/eyunda/download/imge/";
	private PhotoView image;
	private ProgressBar mProgress;
	private int default_res = R.drawable.zd_icon;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private int progress;
	private Thread downLoadThread;
	private String savePath = "";
	private String saveFileName = "";
	private File file;

	ImageCompress compress;
	ImageCompress.CompressOptions options;
	ImageLoader mImageLoader;
	private boolean interceptFlag = false;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				mProgress.setVisibility(View.INVISIBLE);
				Uri uri = Uri.fromFile(file);
				// 处理图片分辨率太大imageview不能显视出来
				options.uri = uri;
				options.maxWidth = getWindowManager().getDefaultDisplay()
						.getWidth();
				options.maxHeight = getWindowManager().getDefaultDisplay()
						.getHeight();
				Bitmap bitmap = compress.compressFromUri(ShowBigImage.this, options);
				image.setImageBitmap(bitmap);
				break;
			default:
				break;
			}
		};
	};
	private String remotepath;
	private String localpath;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_big_image);

		compress = new ImageCompress();
		options = new ImageCompress.CompressOptions();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(GlobalApplication.getInstance()
				.getImageLoaderConfiguration());

		image = (PhotoView) findViewById(R.id.image);
		mProgress = (ProgressBar) findViewById(R.id.pb_load_local);
		default_res = getIntent().getIntExtra("default_image",
				R.drawable.zd_icon);

		 remotepath = getIntent().getExtras().getString("remotepath");
		 localpath = getIntent().getExtras().getString("localpath");
		
		try{
		// 本地存在，直接显示本地的图片
		if (localpath != null && new File(localpath).exists()) {
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			
			File file = new File(localpath);
			Uri uri = Uri.fromFile(file);
			// 处理图片分辨率太大imageview不能显视出来
			options.uri = uri;
			options.maxWidth = getWindowManager().getDefaultDisplay()
					.getWidth();
			options.maxHeight = getWindowManager().getDefaultDisplay()
					.getHeight();
			Bitmap bitmap = compress.compressFromUri(this, options);
			image.setImageBitmap(bitmap);
		} else if (remotepath != null&&!"".equals(remotepath)) { // 去服务器下载图片
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			
			String remoteImageName = "";
			if(remotepath.contains("/"))
				remoteImageName = remotepath.substring(remotepath.lastIndexOf("/")+1);
			else if(remotepath.contains("\\"))
				remoteImageName = remotepath.substring(remotepath.lastIndexOf("\\")+1);
			 file = new File(Environment.getExternalStorageDirectory() + DOWNLOAD_DIR + remoteImageName);
			if (file.exists()) {
				Uri uri = Uri.fromFile(file);
				// 处理图片分辨率太大imageview不能显视出来
				options.uri = uri;
				options.maxWidth = getWindowManager().getDefaultDisplay()
						.getWidth();
				options.maxHeight = getWindowManager().getDefaultDisplay()
						.getHeight();
				Bitmap bitmap = compress.compressFromUri(this, options);
				image.setImageBitmap(bitmap);
			}else{
				mImageLoader.displayImage(ApplicationConstants.IMAGE_URL
						+ remotepath, image, MyshipAdapter.displayImageOptions);
				
				downLoadThread = new Thread(mdownApkRunnable);
				downLoadThread.start();
			}
		} else {
			image.setImageResource(default_res);
		}

		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
//		interceptFlag = true;
//		if (file.exists())
//			file.delete();
		finish();
	}
	
	private Runnable mdownApkRunnable = new Runnable() {
		InputStream is;
		HttpURLConnection conn;
		FileOutputStream fos;

		@Override
		public void run() {
			try {
				String sessionId = "";
				if (GlobalApplication.getInstance().getUserData() != null) {
					sessionId = GlobalApplication.getInstance().getUserData()
							.getSessionId();
				}
				if ("".equals(sessionId)) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(ShowBigImage.this,
									"sessionId为空", 0).show();
							finish();
						}
					});
				}
				mProgress.setVisibility(View.VISIBLE);
				URL url = new URL(ApplicationConstants.FILE_URL
						+ remotepath + "&sessionId=" + sessionId);

				conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				int length = Integer.valueOf(conn
						.getHeaderField("Content_Length"));
				is = conn.getInputStream();
				
				File f = new File(Environment.getExternalStorageDirectory()
						+ DOWNLOAD_DIR);
				if (!f.exists()) {
					f.mkdirs();
				}
				fos = new FileOutputStream(file);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.

				fos.close();
				is.close();
				conn.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					fos.close();
					is.close();
					conn.disconnect();
					runOnUiThread(new Runnable() {
						public void run() {
						//	textView.setText("下载失败！");
							if (file.exists())
								file.delete();
							Toast.makeText(ShowBigImage.this, "下载大图失败: ",
									0).show();
							mProgress.setVisibility(View.INVISIBLE);
						}
					});
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		}
	};
}
