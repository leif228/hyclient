package com.hangyi.zd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;

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
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.data.Image_loader;
import com.eyunda.main.update.UpdateManager;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.MenuActivity;
import com.eyunda.third.activities.NewPageHomeMainActivity;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.UpdateInfoData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.hangyi.zd.activity.LoginActivity;
import com.hangyi.zd.activity.ShipPoliceActivity;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.PersistentCookieStore;

public class SplashActivity extends CommonActivity {

	ImageView sp_gg;

	Image_loader img_loader;
	LinearLayout down_lay;

	DialogUtil dialogUtil;
	ProgressDialog dialog;
	Data_loader data1;
	int count;
	boolean updateLater = false;
	private String noUpdateVersion = "";//无需升级当前版本
	
	boolean gotoNewChatAllHistoryActivity = false;
	Bundle notifyGotoArgs = null;

	private SharedPreferences sp;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.loading, null);
		setContentView(view);
		data1 = new Data_loader();
		dialogUtil = new DialogUtil(this);
		
		notifyGotoArgs = getIntent().getBundleExtra("NotifyGotoActivity");
		
		if (updateLater) {

			init();

		} else {
			getVersion();
		}

		uploadBug();
	}

	public void init() {
		sp = getSharedPreferences( ApplicationConstants.UserInfoConfig_SharedPreferences, MODE_PRIVATE);
		String userName = sp.getString("UserName", "");
		if("".equals(userName)) {
			startActivity(new Intent(this,LoginActivity.class));
			finish();
		} else {
			autologin2();
			
//			startActivity(new Intent(this,MenuActivity.class));
//			finish();
		}
	}
	public String getCookieText() {
		PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
		List<Cookie> cookies = myCookieStore.getCookies();
		GlobalApplication.getInstance().setCookies(cookies);
		 StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cookies.size(); i++) {
			 Cookie cookie = cookies.get(i);
			 String cookieName = cookie.getName();
			 String cookieValue = cookie.getValue();
			if (!TextUtils.isEmpty(cookieName)
					&& !TextUtils.isEmpty(cookieValue)) {
				sb.append(cookieName + "=");
				sb.append(cookieValue + ";");
			}
		}
		return sb.toString();
	}
	private void autologin2() {
		PersistentCookieStore myCookieStore = new PersistentCookieStore(this);    
		data1.asyncHttpClient.setCookieStore(myCookieStore); 

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
				if("0".equals(content)){
					getCookieText();
					GlobalApplication.getInstance().startPush();
					
					Intent intent = new Intent(SplashActivity.this,ReLoginListenerService.class); 
					SplashActivity.this.startService(intent);
					
					if(notifyGotoArgs != null){
						Intent mainIntent = new Intent(SplashActivity.this, NewPageHomeMainActivity.class);
						mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						
						String activity = notifyGotoArgs.getString("activity");
						String msgNid = notifyGotoArgs.getString("ShipPoliceActivity.msgNid");
						Intent goIntent = null;
						if(activity.equals("ShipPoliceActivity")){
						 	goIntent = new Intent(SplashActivity.this,
						 			ShipPoliceActivity.class);
						 	goIntent.putExtra("msgNid", msgNid);
						}
						Intent[] intents = {mainIntent, goIntent};
			            context.startActivities(intents);
			            finish();
						
					}else{
						startActivity(new Intent(SplashActivity.this,NewPageHomeMainActivity.class));
						finish();
					}
				}else {
					Toast.makeText(SplashActivity.this, "自动登录错误！",
							Toast.LENGTH_LONG).show();
					
					Editor editor = sp.edit();
					editor.putString("AutoLogin", "false");
					editor.putString("SavePassword", "false");
					editor.putString("UserName", "");
					editor.putString("UserPassword", "");
					editor.commit();
					
					startActivity(new Intent(SplashActivity.this,LoginActivity.class));
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

		params.put("username", sp.getString("UserName", ""));
		params.put("passcode", sp.getString("UserPassword", ""));
		params.put("clienttype", ApplicationConstants.clienttype);
		data1.getZd_ApiResult(handler, ApplicationUrls.login, params, "post");
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
								ApplicationConstants.noUpdate_SharedPreferences, Activity.MODE_PRIVATE);
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
						Toast.makeText(SplashActivity.this, "网络连接异常,请稍后再试！",
								Toast.LENGTH_LONG).show();
						SplashActivity.this.finish();
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
		// 获取地区列表，放到临时文件/全局变量？
		final Map<String, Object> params = new HashMap<String, Object>();
		data1.getApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				String fileName = ApplicationConstants.LF_AREA_NAME;
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equalsIgnoreCase("success")) {
					FileOutputStream outputStream;
					try {
						outputStream = openFileOutput(fileName,
								Activity.MODE_PRIVATE);
						outputStream.write(arg0.getBytes());
						outputStream.flush();
						outputStream.close();
						outputStream = null;
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {

				}
			}
		}, "/mobile/comm/getPorts", params, "get");

		// 获取分类搜索中条件列表,公司
		data1.getApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				String fileName = ApplicationConstants.LF_SEARCH_CATEGORY_DLR;
				ConvertData cd = new ConvertData(arg0);
				if ("success".equalsIgnoreCase(cd.getReturnCode())) {

					FileOutputStream outputStream;
					try {
						outputStream = openFileOutput(fileName,
								Activity.MODE_PRIVATE);
						outputStream.write(arg0.getBytes());
						outputStream.flush();
						outputStream.close();
						outputStream = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {

				}
			}
		}, "/mobile/home/company/list", params, "get");
		// 船舶类别
		data1.getApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				String fileName = ApplicationConstants.LF_SEARCH_SHIP_LIST;
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equalsIgnoreCase("success")) {
					FileOutputStream outputStream;
					try {
						outputStream = openFileOutput(fileName,
								Activity.MODE_PRIVATE);
						outputStream.write(arg0.getBytes());
						outputStream.flush();
						outputStream.close();
						outputStream = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {

				}
			}

		}, "/mobile/home/getAllTypes/show", params, "get");

	}

	private void uploadBug() {
		// 检查目录是否存在error-开头的文件
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String path = "/mnt/sdcard/zd/log/";
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

}
