package com.eyunda.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.eyunda.main.data.Image_loader;
import com.eyunda.main.update.UpdateManager;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.MenuActivity;
import com.eyunda.third.activities.cargo.CargoPreviewActivity;
import com.eyunda.third.activities.chat.NewChatAllHistoryActivity;
import com.eyunda.third.activities.order.MyOrderActivity;
import com.eyunda.third.activities.ship.MyshipActivity;
import com.eyunda.third.activities.viewPage.ViewPagerActivity;
import com.eyunda.third.chat.event.LoginStatusCode;
import com.eyunda.third.chat.utils.SIMCardInfo;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.UpdateInfoData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

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
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.loading, null);
		setContentView(view);
		Intent intentThis = getIntent(); 
		notifyGotoArgs = intentThis.getBundleExtra("NotifyGotoActivity");
		updateLater = intentThis.getBooleanExtra("updateLater", false);
		gotoNewChatAllHistoryActivity = intentThis.getBooleanExtra("gotoNewChatAllHistoryActivity", false);
		dialogUtil = new DialogUtil(this);
		data1 = new Data_loader();

		/**
		 * 加载
		 */
		SharedPreferences sf = getSharedPreferences("eyunda",
				Context.MODE_PRIVATE);
		if (sf.getBoolean("isfirst", true)) {
			Editor editor = sf.edit();
			editor.putBoolean("isfirst", false);
			editor.commit();
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), ViewPagerActivity.class);
			startActivity(intent);
			finish();
			return;
		}

		if (updateLater) {
			Context ctx = SplashActivity.this;
			SharedPreferences sp = ctx.getSharedPreferences(
					"eyundaBindingCode", MODE_PRIVATE);
			String bindingCode = sp.getString("bindingCode", "");

			String simCardNo = SIMCardInfo.getInstance(ctx).getSimCardNumber();

			autoLogin(bindingCode, simCardNo);

			initData();

		} else {
			getVersion();
		}
		uploadBug();
	}

	public void autoLogin(String bindingCode, String simCardNo) {
		final Map<String, Object> params = new HashMap<String, Object>();
		// 判断能否自动登入
		if (bindingCode != null && simCardNo != null && !bindingCode.equals("")
				&& !simCardNo.equals("")) {
			AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
				@Override
				public void onStart() {
					super.onStart();
					// dialog = dialogUtil.loading("登录中", "请稍候...");
				}

				@Override
				public void onSuccess(String content) {

					// dialog.dismiss();
					Gson gson = new Gson();
					HashMap<String, Object> map = gson.fromJson(
							(String) content,
							new TypeToken<Map<String, Object>>() {
							}.getType());

					if (map.get("returnCode").equals("Success")) {
						try {
							@SuppressWarnings("unchecked")
							UserData userData = new UserData(
									(Map<String, Object>) map.get("content"));

							SharedPreferences sp = SplashActivity.this
									.getSharedPreferences("eyundaBindingCode",
											MODE_PRIVATE);
							// 存入数据
							Editor editor = sp.edit();
							editor.putString("loginName",
									userData.getLoginName());
							editor.putString("nickName", userData.getNickName());
							editor.putString("userLogo", userData.getUserLogo());
							editor.putString("roleDesc", userData.getShortRoleDesc());
							editor.putString("id", userData.getId().toString());
							editor.commit();
							
							// 保存sessionId
							GlobalApplication.getInstance().setUserData(userData);
							GlobalApplication.getInstance().setLoginStatus(LoginStatusCode.logined);

							// 是否进入gotoActivity
							if(gotoNewChatAllHistoryActivity){
								Intent mainIntent = new Intent(SplashActivity.this, MenuActivity.class);
								 mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								 Intent goIntent = new Intent(SplashActivity.this, NewChatAllHistoryActivity.class).putExtra("selTab", "chat");
								 Intent[] intents = {mainIntent, goIntent};
								 startActivities(intents);
								 finish();
							}else{
								if(notifyGotoArgs == null){
									startActivity(new Intent(
											SplashActivity.this,
											com.eyunda.third.activities.MenuActivity.class));
									finish();
								}else{
									Intent mainIntent = new Intent(context, MenuActivity.class);
									mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									
									String activity = notifyGotoArgs.getString("activity");
									Intent goIntent = null;
									if(activity.equals("NewChatAllHistoryActivity")){
									 	goIntent = new Intent(SplashActivity.this,
												NewChatAllHistoryActivity.class).putExtra("selTab", notifyGotoArgs.getString("selTab"));
									}else if(activity.equals("CargoPreviewActivity")){ 
										goIntent = new Intent(SplashActivity.this,
												CargoPreviewActivity.class).putExtra("id", notifyGotoArgs.getString("id"));
									}else if(activity.equals("MyshipActivity")){  //系统生成的通知消息，消息内容不带“发送者:”。  处理船舶加油
										goIntent = new Intent(SplashActivity.this,
												MyshipActivity.class);
									} else {
										goIntent = new Intent(SplashActivity.this,
												MyOrderActivity.class);
									}
								 	Intent[] intents = {mainIntent, goIntent};
						            context.startActivities(intents);
						            finish();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						// 自动登入失败时
						GlobalApplication.getInstance().setUserData(null);
						GlobalApplication.getInstance().setLoginStatus(
								LoginStatusCode.noLogin);

						SharedPreferences sp = SplashActivity.this
								.getSharedPreferences("eyundaBindingCode",
										MODE_PRIVATE);
						// 存入数据
						Editor editor = sp.edit();
						editor.putString("bindingCode", "");
						editor.putString("loginName", "");
						editor.putString("nickName", "");
						editor.putString("userLogo", "");
						editor.putString("roleDesc", "");
						editor.putString("id", "");
						editor.commit();
						
						// 是否进入gotoActivity
						if(notifyGotoArgs == null){
							startActivity(new Intent(SplashActivity.this,
									com.eyunda.third.activities.MenuActivity.class));
							finish();
						}else{
							startActivity(new Intent(
									SplashActivity.this,
									com.eyunda.third.activities.user.LoginActivity.class));
							finish();
						}
					}
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);

					// 自动登入失败时
					GlobalApplication.getInstance().setUserData(null);
					GlobalApplication.getInstance().setLoginStatus(
							LoginStatusCode.noLogin);

					if (content != null && content.equals("can't resolve host"))
						Toast.makeText(SplashActivity.this, "网络连接异常",
								Toast.LENGTH_SHORT).show();
					// dialog.dismiss();

					// 是否进入gotoActivity
					if(notifyGotoArgs == null){
						startActivity(new Intent(SplashActivity.this,
								com.eyunda.third.activities.MenuActivity.class));
						finish();
					}else{
						startActivity(new Intent(
								SplashActivity.this,
								com.eyunda.third.activities.user.LoginActivity.class));
						finish();
					}
				}
			};

			params.put("bindingCode", bindingCode);
			params.put("simCardNo", simCardNo);
			data1.getApiResult(handler, "/mobile/login/autoLogin", params);
		} else {
			// 是否进入gotoActivity
			if(notifyGotoArgs == null){
				startActivity(new Intent(SplashActivity.this,
						com.eyunda.third.activities.MenuActivity.class));
				finish();
			}else{
				startActivity(new Intent(
						SplashActivity.this,
						com.eyunda.third.activities.user.LoginActivity.class));
				finish();
			}
		}

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
			data1.getVersion(new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(String arg0) {
					super.onSuccess(arg0);
					ConvertData cd = new ConvertData(arg0);
					if (cd.getReturnCode().equalsIgnoreCase("success")) {
						HashMap<String, Object> var = (HashMap<String, Object>) cd
								.getContent();
						UpdateInfoData up = new UpdateInfoData(var);
//						Log.setLog2FileEnabled(true);
//						Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("eyunda",".log"));
//						Log.d(getVersionName()+"-----"+up.getVersion());
						//先读取配置文件，查看用户是否已经忽略当前升级版本
						//读取配置信息，查看用户是否已经忽略当前版本的升级
						SharedPreferences spNoUpdate = SplashActivity.this.getSharedPreferences(
								"noUpdate", Activity.MODE_PRIVATE);
						noUpdateVersion = spNoUpdate.getString("noUpdate", "");
						if ((getVersionName().equals(up.getVersion())) || (noUpdateVersion.equals(up.getVersion()))) {
							Context ctx = SplashActivity.this;
							SharedPreferences sp = ctx.getSharedPreferences(
									"eyundaBindingCode", MODE_PRIVATE);
							String bindingCode = sp
									.getString("bindingCode", "");
							String simCardNo = SIMCardInfo.getInstance(ctx)
									.getSimCardNumber();
							autoLogin(bindingCode, simCardNo);
							initData();
						} else {
							UpdateManager mUpdateManager = new UpdateManager(
									SplashActivity.this, up.getUrl(),"当前版本为："+getVersionName() +"\r\n"+up
											.getNote(),up.getVersion());
							mUpdateManager.checkUpdateInfo();
						}
					}

				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					if (content != null && content.equals("can't resolve host"))
						Toast.makeText(SplashActivity.this, "网络连接异常",
								Toast.LENGTH_LONG).show();
					else if (content != null
							&& content.equals("socket time out")) {
						Toast.makeText(SplashActivity.this, "连接服务器超时",
								Toast.LENGTH_LONG).show();
					} else if (content != null) {
						Toast.makeText(SplashActivity.this, content,
								Toast.LENGTH_LONG).show();
					} else
						Toast.makeText(SplashActivity.this, "未知异常",
								Toast.LENGTH_LONG).show();
				}
			});
		} else {
			Context ctx = SplashActivity.this;
			SharedPreferences sp = ctx.getSharedPreferences(
					"eyundaBindingCode", MODE_PRIVATE);
			String bindingCode = sp.getString("bindingCode", "");
			String simCardNo = SIMCardInfo.getInstance(ctx).getSimCardNumber();
			autoLogin(bindingCode, simCardNo);
			initData();
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
			String path = "/mnt/sdcard/eyunda/log/";
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
							// 获取分类搜索中条件列表,公司
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
