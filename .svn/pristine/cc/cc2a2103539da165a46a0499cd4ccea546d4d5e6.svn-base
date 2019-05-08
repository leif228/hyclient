package com.hy.client.tools;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.hangyi.zd.SplashActivity;
import com.hy.client.R;
import com.hy.client.activitys.MainActivity;

public class UpdateManager1 {

	private Activity mContext;

	// 提示语
	DialogUtil dialogUtil;
	// 返回的安装包url

	private Dialog noticeDialog;

	private Dialog downloadDialog;
	/* 下载包安装路径 */
	private String savePath = Environment.getExternalStorageDirectory()  
                    .getAbsolutePath() +"/hy";

	private     String saveFileName = savePath
			+ "/hy.zip";

	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar mProgress;

	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;
	String url = "";
	String text = "";
	String version = "";
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				try {
					doZip();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager1(Activity context, String url, String text,String version) {
		this.mContext = context;
		dialogUtil = new DialogUtil(context);
		this.url = url;
		this.text = text;
		this.version = version;
//		savePath="data/data/com.eyunda.main";
		
	}

	protected void doZip() throws ZipException, IOException {
		// TODO Auto-generated method stub
		String command = "chmod 777 " + saveFileName;
        Runtime runtime = Runtime.getRuntime();
        try {
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		
		upZipFile(new File(saveFileName),savePath);
		
	}
	     /** 
	      * 解压缩一个文件 
	      * 
	      * @param zipFile 要解压的压缩文件 
	      * @param folderPath 解压缩的目标目录 
	      * @throws IOException 当解压缩过程出错时抛出 
	 */
	     public void upZipFile(File zipFile, String folderPath) throws ZipException, IOException 
	      {
	          File desDir = new File(folderPath);
	          if(!desDir.exists())
	          {
	              desDir.mkdirs();
	          }
	         
	          ZipFile zf = new ZipFile(zipFile);  
	           for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) 
	           { 
	              ZipEntry entry = ((ZipEntry)entries.nextElement());  
	              InputStream in = zf.getInputStream(entry);  
	             String str = folderPath + File.separator + entry.getName(); 
	             str = new String(str.getBytes("8859_1"), "GB2312");  
	              File desFile = new File(str);  
	              if (!desFile.exists()) 
	             { 
	                   File fileParentDir = desFile.getParentFile();  
	                  if (!fileParentDir.exists()) 
	                   { 
	                      fileParentDir.mkdirs();  
	                   }  
	                      desFile.createNewFile();  
	             }  
	              OutputStream out = new FileOutputStream(desFile);  
	              byte buffer[] = new byte[1024];  
	              int realLength;  
	              while ((realLength = in.read(buffer)) > 0) 
	              {
	                 out.write(buffer, 0, realLength);  
	             }
	             in.close();  
	             out.close(); 
	             
	             downloadDialog.cancel();
	             ((MainActivity)mContext).init2();
	           }  
	     }

	// 外部接口让主Activity调用
	public void checkUpdateInfo() {
		showNoticeDialog();
	}

	private void showNoticeDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");
		builder.setMessage(text);
		builder.setCancelable(false);
		builder.setPositiveButton("下载", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//记录一下忽略的版本号
				SharedPreferences sp = mContext.getSharedPreferences(
						ApplicationConstants.noUpdate_SharedPreferences, Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit(); 
				editor.putString("noUpdate", version);
				editor.commit();
				dialog.dismiss();
				//dialogUtil.sysExitFalse();
//				Intent intent = new Intent(mContext,SplashTwoActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				intent.putExtra("updateLater", true);
				
//				mContext.startActivity(intent);
//				mContext.finish();
				((MainActivity)mContext).init();
			}
		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}

	private void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件下载中...");
		builder.setCancelable(false);
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);

		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
				dialogUtil.sysExit();
				
				((MainActivity)mContext).init();
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();

		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(UpdateManager1.this.url);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

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
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
			}

		}
	};

	/**
	 * 下载apk
	 * 
	 * @param url
	 */

	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		String command = "chmod 777 " + saveFileName;
        Runtime runtime = Runtime.getRuntime();
        try {
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		openFile(apkfile);
//		Intent i = new Intent(Intent.ACTION_VIEW);
//		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
//				"application/vnd.android.package-archive");
//		mContext.startActivity(i);

	}
	
	private void openFile(File f) {

		Intent intent = new Intent();

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		intent.setAction(android.content.Intent.ACTION_VIEW);

		String type = "application/vnd.android.package-archive";

		intent.setDataAndType(Uri.fromFile(f), type);

		mContext.startActivity(intent);

	}
	

}

