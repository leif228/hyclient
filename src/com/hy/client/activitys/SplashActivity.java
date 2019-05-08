package com.hy.client.activitys;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.data.Image_loader;
import com.eyunda.main.update.MyCallBack;
import com.eyunda.main.update.UpdateManager;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.UpdateInfoData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.PersistentCookieStore;

public class SplashActivity extends CommonActivity implements MyCallBack{

	ImageView sp_gg;

	Image_loader img_loader;
	LinearLayout down_lay;

	DialogUtil dialogUtil;
	ProgressDialog dialog;
	Data_loader data1;
	int count;
	boolean updateLater = false;
	private String noUpdateVersion = "";//无需升级当前版本

	private SharedPreferences sp;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.loading, null);
		setContentView(view);
		data1 = new Data_loader();
		dialogUtil = new DialogUtil(this);
		
		if (updateLater) {

			init();

		} else {
			getVersion();
		}

		uploadBug();
	}

	private void init() {
		sp = getSharedPreferences(ApplicationConstants.HY_SharedPreferences, MODE_PRIVATE);
		String userName = sp.getString("UserName", "");
		if("".equals(userName)) {
			startActivity(new Intent(this,MainActivity.class).putExtra(ApplicationConstants.Logined, false).putExtra(ApplicationConstants.Logined_Success, false));
			finish();
		} else {
			autologin2();
		}
	}
	private void autologin2() {
		PersistentCookieStore myCookieStore = new PersistentCookieStore(
				GlobalApplication.getInstance()); 
		myCookieStore.clear();
		Data_loader.asyncHttpClient.setCookieStore(myCookieStore); 

		Map<String, Object> params = new HashMap<String, Object>();

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("努力加载中", "请稍候...");
			}

			@Override
			public void onSuccess(String content) {
				dialog.dismiss();
				//返回0登录成功，-1密码错误，-2未知错误，-3找不到用户
				HashMap<String, Object> rmap = null;
				try {
					Gson gson = new Gson();
					rmap = gson.fromJson(
							content, new TypeToken<Map<String, Object>>() {
							}.getType());
				} catch (Exception e) {
				}
				if(rmap!=null&&"Success".equals(rmap.get("returnCode"))){
					MainActivity.getCookieText();
					
					startActivity(new Intent(SplashActivity.this,MainActivity.class).putExtra(ApplicationConstants.Logined, true).putExtra(ApplicationConstants.Logined_Success, true));
					finish();
				}else {
					Toast.makeText(SplashActivity.this, "自动登录错误！",Toast.LENGTH_LONG).show();
					
//					Editor editor = sp.edit();
//					editor.putString("AutoLogin", "false");
//					editor.putString("SavePassword", "false");
//					editor.putString("UserName", "");
//					editor.putString("UserPassword", "");
//					editor.commit();
					
					startActivity(new Intent(SplashActivity.this,MainActivity.class).putExtra(ApplicationConstants.Logined, true).putExtra(ApplicationConstants.Logined_Success, false));
					finish();
					
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);

				dialog.dismiss();
//				if (content != null && content.equals("can't resolve host"))
//					Toast.makeText(LoginActivity.this, "网络连接异常",
//							Toast.LENGTH_LONG).show();
//				else if (content != null && content.equals("socket time out")) {
//					Toast.makeText(LoginActivity.this, "连接服务器超时",
//							Toast.LENGTH_LONG).show();
//				} else if (content != null) {
//					Toast.makeText(LoginActivity.this, content,
//							Toast.LENGTH_LONG).show();
//				} else
					Toast.makeText(SplashActivity.this, "网络出现问题，请稍后再试！",
							Toast.LENGTH_LONG).show();
					SplashActivity.this.finish();

			}
		};

		params.put("loginName", sp.getString("UserName", ""));
		params.put("passWord", sp.getString("UserPassword", ""));
		params.put("loginType", "phone");
//		params.put("username", sp.getString("UserName", ""));
//		params.put("passcode", sp.getString("UserPassword", ""));
//		params.put("clienttype", ApplicationConstants.clienttype);
		data1.getHY_ApiResult(handler, ApplicationUrls.javaLogin, params, "post");
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	/**
	 * 版本检查及升级提示
	 */
	private void getVersion() {
		if (NetworkUtils.isNetworkAvailable()) {
			data1.getzdVersion(new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(String arg0) {
					super.onSuccess(arg0);
					ConvertData cd = new ConvertData(arg0);
					if (cd.getReturnCode()!=null&&cd.getReturnCode().equalsIgnoreCase("success")) {
						HashMap<String, Object> var = (HashMap<String, Object>) cd
								.getContent();
						UpdateInfoData up = new UpdateInfoData(var);
//						Log.setLog2FileEnabled(true);
//						Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("eyunda",".log"));
//						Log.d(getVersionName()+"-----"+up.getVersion());
						//先读取配置文件，查看用户是否已经忽略当前升级版本
						//读取配置信息，查看用户是否已经忽略当前版本的升级
						SharedPreferences spNoUpdate = SplashActivity.this.getSharedPreferences(
								ApplicationConstants.HY_noUpdate_SharedPreferences, Activity.MODE_PRIVATE);
						noUpdateVersion = spNoUpdate.getString("noUpdate", "");
						if ((getVersionName().equals(up.getVersion())) || (noUpdateVersion.equals(up.getVersion()))) {
							init();
						} else {
							UpdateManager mUpdateManager = new UpdateManager(
									SplashActivity.this, up.getUrl(),"当前版本为："+getVersionName() +"\r\n"+up
											.getNote(),up.getVersion());
							mUpdateManager.checkUpdateInfo();
						}
					}else{
						init();
					}

				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
//					if (content != null && content.equals("can't resolve host"))
//						Toast.makeText(SplashActivity.this, "网络连接异常",
//								Toast.LENGTH_LONG).show();
//					else if (content != null
//							&& content.equals("socket time out")) {
//						Toast.makeText(SplashActivity.this, "连接服务器超时",
//								Toast.LENGTH_LONG).show();
//					} else if (content != null) {
//						Toast.makeText(SplashActivity.this, content,
//								Toast.LENGTH_LONG).show();
//					} else
//						Toast.makeText(SplashActivity.this, "网络连接异常,请稍后再试！",
//								Toast.LENGTH_LONG).show();
//						SplashActivity.this.finish();
					init();
				}
			});
		} else {
			init();
		}
	}

	private String getVersionName() {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		String version = "";
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 初始化数据,写入本地文件
	 * 
	 * @return
	 */
	private void initData() {

	}

	private void uploadBug() {
		// 检查目录是否存在error-开头的文件
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String path = "/mnt/sdcard/hy/log/";
			File f = new File(path);
			if (f.exists() && f.isDirectory()) {
				if (f.listFiles().length > 0) {
					String[] file1 = f.list(new FilenameFilter() {// 使用匿名内部类重写accept方法
								public boolean accept(File dir, String name) {
									if (new File(dir, name).isDirectory()) {
										return true;
									}
									return name.indexOf("error-") != -1;// 筛选出error开头文件
								}
							});
					for (int j = 0; j < file1.length; j++) {
						String up = path + file1[j];
						final File upFile = new File(up);
						if (upFile.isFile()){// 存在则上传
							data1.uploadBug(new AsyncHttpResponseHandler() {
								@Override
								public void onSuccess(String arg0) {
									super.onSuccess(arg0);
									ConvertData cd = new ConvertData(arg0);
									if (cd.getReturnCode().equalsIgnoreCase("success")) {
										upFile.delete();
									}
								}
							}, up);
									
						}
					}
				}
			}
		}
		// 上传完成 删除源文件
	}

	@Override
	public void callBack() {
		init();
	}

}
