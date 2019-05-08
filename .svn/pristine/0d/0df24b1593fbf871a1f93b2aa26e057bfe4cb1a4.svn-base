package com.eyunda.third.activities.chat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
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
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.order.OrderDownloadActivity;
import com.hy.client.R;
/**
 * 文件下载
 * 
 */
public class ShowNormalFileActivity extends CommonListActivity implements
		OnClickListener {
	final static String DOWNLOAD_DIR = "eyunda/download/file/";
	private ProgressBar progressBar;
	private File file;
	TextView textView;
	Button answer_but;
	ImageView download_img;
	LinearLayout view;
	String remoteFilePath;
	String fileName;
	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				progressBar.setProgress(progress);
				break;
			case DOWN_OVER:
				textView.setText("下载文件成功");
				answer_but.setClickable(true);
				progressBar.setVisibility(View.INVISIBLE);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_file);
		
		textView = (TextView) findViewById(R.id.textView);
		view = (LinearLayout) findViewById(R.id.bottom);
		answer_but = (Button) findViewById(R.id.answer_but);
		download_img = (ImageView)findViewById(R.id.download_img);
		
		answer_but.setOnClickListener(this);
		download_img.setOnClickListener(this);;
		answer_but.setClickable(false);

		remoteFilePath = getIntent().getStringExtra("filePath");
		// fileName = remoteFilePath
		// .substring(remoteFilePath.lastIndexOf("/") + 1);
		fileName = getIntent().getStringExtra("fileName");
		file = new File(Environment.getExternalStorageDirectory()
				+ "/eyunda/download/file/" + fileName);
	}

	private synchronized void download() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
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
							textView.setText("下载失败！");
							Toast.makeText(ShowNormalFileActivity.this,
									"sessionId为空", 0).show();
							finish();
						}
					});
				}

				URL url = new URL(ApplicationConstants.FILE_URL
						+ remoteFilePath + "&sessionId=" + sessionId);

				conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				int length = Integer.valueOf(conn
						.getHeaderField("Content_Length"));
				is = conn.getInputStream();
				
				File f = new File(Environment.getExternalStorageDirectory()
						+ "/eyunda/download/file/");
				if (!f.exists()) {
					f.mkdirs();
				}
				fos = new FileOutputStream(file);

				int count = 0;
				byte buf[] = new byte[1024];
				runOnUiThread(new Runnable() {
					public void run() {
						textView.setText("文件正在下载...请稍候");
					}
				}); 

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
							textView.setText("下载失败！");
							if (file.exists())
								file.delete();
							Toast.makeText(ShowNormalFileActivity.this, "下载文件失败: ",
									0).show();
							progressBar.setVisibility(View.INVISIBLE);
						}
					});
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		}
	};
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("文件下载");
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

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.download_img) {// 生成文件并下载
			progressBar = (ProgressBar) findViewById(R.id.progressBar);
			progressBar.setVisibility(View.VISIBLE);
			download();
		} else if (v.getId() == R.id.answer_but) {
			//打开文件
			openFile(file, ShowNormalFileActivity.this);
		}

	}

	@Override
	protected BaseAdapter setAdapter() {
		return null;
	}

	@Override
	protected void loadDate() {

	}
}
