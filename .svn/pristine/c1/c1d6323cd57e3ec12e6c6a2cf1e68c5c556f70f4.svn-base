package com.eyunda.third.activities.ship;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.R.bool;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.main.update.UpdateManager;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.chat.ShowNormalFileActivity;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.order.OrderData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.ta.util.cache.TAExternalOverFroyoUtils;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.FileHttpResponseHandler;

/**
 * 合同下载
 * 
 * @author guoqiang
 *
 */
public class WTCTemplateDownloadActivity extends CommonListActivity implements
		OnClickListener {

	protected Button answer_but;// 聊天和电话按钮
	protected LinearLayout liner_bg1;// 聊天和电话背景
	private String fileName = "";
	private String savePath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/eyunda/download/template/";
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private int progress;
	private Thread downLoadThread;

	private boolean interceptFlag = false;
	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar mProgress;

	final Activity activity = this;
	String orderId;
	String orderNum;
	String pdfFileName;
	File file = null;
	String remoteFilePath;

	ImageView download_img;
	TextView showText;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				
				break;
			case DOWN_OVER:
				answer_but.setClickable(true);
				liner_bg1.setBackgroundColor(0xff5D77B4);
				mProgress.setVisibility(View.INVISIBLE);
				showText.setText("下载成功！");
				download_img.setClickable(false);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_order_download);
		Intent intent = getIntent();
		pdfFileName = "trustdeed.docx";
		// url = intent.getStringExtra("url");

		fileName = pdfFileName;

		// Toast.makeText(this, orderId+orderNum, Toast.LENGTH_LONG).show();
		showText = (TextView) findViewById(R.id.showText);

		answer_but = (Button) findViewById(R.id.answer_but);
		download_img = (ImageView) findViewById(R.id.download_img);
		liner_bg1 = (LinearLayout) findViewById(R.id.liner_bg1);

		answer_but.setOnClickListener(this);
		download_img.setOnClickListener(this);
		
		//读取文件，判断是否与当前合同状态一致，一致则可直接打开
		File tmpf = new File(savePath + orderId + "/" + fileName);
		//Toast.makeText(OrderDownloadActivity.this, savePath + orderId + "/" + fileName, Toast.LENGTH_LONG).show();
//		if (tmpf.exists() && tmpf.length() > 0) {
//			download_img.setClickable(false);
//			file = tmpf;
//			liner_bg1.setBackgroundColor(0xff5D77B4);
//			showText.setText("下载成功！");
//		}else{
			answer_but.setClickable(false);
			liner_bg1.setBackgroundColor(0xff696969);
//		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("委托书模板下载");
		// Toast.makeText(this, orderId+orderNum, Toast.LENGTH_LONG).show();

	}

	@Override
	protected synchronized void loadDate() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.download_img) {// 生成文件并下载
			mProgress = (ProgressBar) findViewById(R.id.progress);
			mProgress.setVisibility(View.VISIBLE);
			loadDate();
		} else if (v.getId() == R.id.answer_but) {
			// 打开文件
			openFile(file, WTCTemplateDownloadActivity.this);
		}

	}

	@Override
	protected BaseAdapter setAdapter() {
		return null;
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {

				// URL url = new URL(ApplicationConstants.FILE_URL +
				// OrderDownloadActivity.this.url+"&sessionId="+sessionId);
				URL url = new URL(ApplicationConstants.SERVER_URL
						+ "/phone/trustdeed.docx");

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();

				InputStream is = conn.getInputStream();

				File f = new File(savePath + orderId + "/");
				if (!f.exists()) {
					boolean res = f.mkdirs();
				}
				// 清空目录
				deleteAllFiles(f);
				file = new File(savePath + orderId + "/" + fileName);
				FileOutputStream fos = new FileOutputStream(file);
				int count = 0;
				byte buf[] = new byte[1024];
				do {
					int numread = is.read(buf);
					count += numread;
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.

				fos.close();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
				showError(e.getMessage());
			}

		}
	};

	private void showError(final String msg) {
		runOnUiThread(new Runnable() {
			public void run() {
				showText.setText("下载失败！");
				Toast.makeText(WTCTemplateDownloadActivity.this, msg, 1).show();
			}
		});
	}

	private void openFile(File file, Activity activity) {
		Intent intent = new Intent();
		intent.addFlags(268435456);
		intent.setAction("android.intent.action.VIEW");
		String s = getMIMEType(file);
		intent.setDataAndType(Uri.fromFile(file), s);
		try {
			activity.startActivity(intent);
		} catch (Exception exception) {
			exception.printStackTrace();
			Toast.makeText(activity, "文件打开失败", 1).show();
		}
	}

	private String getMIMEType(File file) {
		String s = "";
		String s1 = file.getName();
		String s2 = s1.substring(s1.lastIndexOf(".") + 1, s1.length())
				.toLowerCase();
		s = MimeTypeMap.getSingleton().getMimeTypeFromExtension(s2);
		return s;
	}

	private void deleteAllFiles(File root) {
		File files[] = root.listFiles();
		if (files != null){
			for (File f : files) {
				if (f.isDirectory()) { // 判断是否为文件夹
					deleteAllFiles(f);
					try {
						f.delete();
					} catch (Exception e) {
					}
				} else {
					if (f.exists()) { // 判断是否存在
						deleteAllFiles(f);
						try {
							f.delete();
						} catch (Exception e) {
						}
					}
				}
			}
		}
	}

}
