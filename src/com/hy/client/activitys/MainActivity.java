package com.hy.client.activitys;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.http.cookie.Cookie;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.eyunda.main.update.MyCallBack;
import com.eyunda.main.update.UpdateManager;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.UpdateInfoData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.PersistentCookieStore;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class MainActivity extends Activity implements OnClickListener,
		MyCallBack {
	private WebView webView;
	LinearLayout logoView;

	Data_loader data1;
	ProgressDialog dialog;
	DialogUtil dialogUtil;
	boolean active = false;
	boolean reload = false;// 刷新当前页面

	PowerManager powerManager = null;
	WakeLock wakeLock = null;
	JSHook jsHook = null;

	public static final String testjsloaderror_loadnativejs = "file:///android_asset/js/client-current.js";

	public static final String NOT_FOUND_PAGE = "file:///android_asset/not_found.html";
	public static final String TIME_OUT_PAGE = "file:///android_asset/time_out.html";

	private static final String historyUrl = "/client/monitor/history";
	private static final String homeUrl = "/client/monitor/current";
	private static final String loginUrl = "/client/login/login";
	private static final String tankUrl = "/tank";

	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hy_main);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//keep light
		
		data1 = new Data_loader();
		dialogUtil = new DialogUtil(this);

		powerManager = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"My Lock");

		webView = (WebView) findViewById(R.id.wv_help);
		logoView = (LinearLayout) findViewById(R.id.logo_ll);
		
		jsHook = new JSHook();

		final ProgressBar bar = (ProgressBar) findViewById(R.id.myProgressBar);

		final boolean logined = getIntent().getBooleanExtra(
				ApplicationConstants.Logined, false);
		final boolean loginSuccess = getIntent().getBooleanExtra(
				ApplicationConstants.Logined_Success, false);

		// 设置加载进来的页面自适应手机屏幕
		// webView.getSettings().setUseWideViewPort(true);
		// webView.getSettings().setLoadWithOverviewMode(true);

		webView.getSettings().setJavaScriptEnabled(true); // 开启javascript支持
		webView.getSettings().setSupportZoom(false);
		webView.getSettings().setAppCacheEnabled(false);
		webView.getSettings().setAllowFileAccess(true);

		// 优先使用缓存：
		// webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		// 不使用缓存：
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

		// 密码会被明文保到 /data/data/com.package.name/databases/webview.db
		// 中，这样就有被盗取密码的危险
		webView.getSettings().setSavePassword(false);

		webView.addJavascriptInterface(jsHook, "JSHook");

		if (!logined) {

			clearCookies(this);
			webView.loadUrl(ApplicationConstants.HY_PRE_URL + loginUrl
					+ "?loginType=phone");

		} else {
			if(!loginSuccess){
				clearCookies(this);
				webView.loadUrl(ApplicationConstants.HY_PRE_URL + loginUrl
						+ "?loginType=phone");
				
			}else{
			
				String url = ApplicationConstants.HY_PRE_URL + homeUrl
						+ "?loginType=phone";
	
				synCookies(this, url);
				webView.loadUrl(url);
			}
		}

		webView.setWebViewClient(new NoAdWebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (logined && (!loginSuccess) && (url.contains(loginUrl))) {

					jsHook.showAndroid();
				}
			}

			@Override
			public void doUpdateVisitedHistory(WebView view, String url,
					boolean isReload) {
				super.doUpdateVisitedHistory(view, url, isReload);
				if (url.contains(homeUrl)) {
					view.clearHistory();// 清除历史记录
				}
			}

			// 重写父类方法，让新打开的网页在当前的WebView中显示
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);

				if (url.contains(historyUrl))
					openWakeLock();
				else
					closeWakeLock();

				return true;
			}

			@SuppressWarnings("deprecation")
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);

				// Handle the error
				// if(failingUrl.contains("js")){
				// view.loadUrl(TIME_OUT_PAGE);
				// view.stopLoading();

				// String jspath =
				// failingUrl.substring(failingUrl.indexOf("/js"));
				// view.loadUrl("file:///android_asset"+jspath);
				// }
			}

			@Override
			public void onReceivedError(WebView view, WebResourceRequest req,
					WebResourceError rerr) {
				onReceivedError(view, rerr.getErrorCode(), rerr
						.getDescription().toString(), req.getUrl().toString());
			}
			
			//加载本地资源文件：css、js、img
//			@Override
//			public WebResourceResponse shouldInterceptRequest(WebView view,
//					String url) {
////				try {
////					if (url.equals("http://localhost/qijian.png")) {
////						AssetFileDescriptor fileDescriptor = getAssets()
////								.openFd("s07.jpg");
////						InputStream stream = fileDescriptor.createInputStream();
////						WebResourceResponse response = new WebResourceResponse(
////								"image/png", "UTF-8", stream);
////						return response;
////					}
////				} catch (Exception e) {
////				}
//				return super.shouldInterceptRequest(view, url);
//			}
		});

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					bar.setVisibility(View.GONE);
					logoView.setVisibility(View.GONE);
				} else {
					if (newProgress > 80) {
						logoView.setVisibility(View.GONE);
					}
					if (View.GONE == bar.getVisibility()) {
						bar.setVisibility(View.VISIBLE);
					}
					bar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				CharSequence pnotfound = "404";
				// if (title.contains(pnotfound)) {
				// view.loadUrl(NOT_FOUND_PAGE);
				// view.stopLoading();
				// }
			}
		});
	}

	@Override
	public void onClick(View v) {

	}

	/** 清空所有的cookie */
	public static void clearCookies(Context context) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();

		cookieManager.setAcceptCookie(true);

		cookieManager.removeSessionCookie();// 移除
		cookieManager.removeAllCookie();

		CookieSyncManager.getInstance().sync();
	}

	/** 同步cookie */
	public static void synCookies(Context context, String url) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();

		cookieManager.setAcceptCookie(true);

		cookieManager.removeSessionCookie();// 移除

		List<Cookie> cs = getCookieText();
		for (int i = 0; i < cs.size(); i++) {
			StringBuffer sb = new StringBuffer();
			Cookie cookie = cs.get(i);
			String cookieName = cookie.getName();
			String cookieValue = cookie.getValue();
			if (!TextUtils.isEmpty(cookieName)
					&& !TextUtils.isEmpty(cookieValue)) {
				sb.append(cookieName + "=");
				sb.append(cookieValue + ";");

				cookieManager.setCookie(url, sb.toString());// 这个方法只是在已有的基础上继续添加cookie，并不能重置已有的cookie。
				// saveToFile(cookieName,url+"=="+sb.toString());
			}
		}

		// cookieManager.setCookie(url, getCookieText());//
		// 这个方法只是在已有的基础上继续添加cookie，并不能重置已有的cookie。

		CookieSyncManager.getInstance().sync();
	}

	private static void saveToFile(String cookieName, String str) {

		try {
			String fileName = cookieName + ".log";
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String path = "/mnt/sdcard/hy/log/";
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName);
				fos.write(str.toString().getBytes());
				fos.close();
			}
		} catch (Exception e) {
		}
	}

	private void openWakeLock() {
		if (wakeLock != null) {
			wakeLock.setReferenceCounted(false);
			wakeLock.acquire();
		}
	}

	private void closeWakeLock() {
		if (wakeLock != null && wakeLock.isHeld()) {
			try {
				wakeLock.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		closeWakeLock();
	}

	public static List<Cookie> getCookieText() {
		PersistentCookieStore myCookieStore = new PersistentCookieStore(
				GlobalApplication.getInstance());
		List<Cookie> cookies = myCookieStore.getCookies();
		GlobalApplication.getInstance().setCookies(cookies);

		return cookies;
	}

	class JSHook {
		@JavascriptInterface
		public void login(String username, String password) {
			SharedPreferences sp = getSharedPreferences(
					ApplicationConstants.HY_SharedPreferences, MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("UserName", username);
			editor.putString("UserPassword", password);
			editor.commit();
		}

		@JavascriptInterface
		public void logout() {
			SharedPreferences sp = getSharedPreferences(
					ApplicationConstants.HY_SharedPreferences, MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("UserName", "");
			editor.putString("UserPassword", "");
			editor.commit();

			MainActivity.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());
		}

		@JavascriptInterface
		public void version() {
			getVersion();
		}

		public void showAndroid() {

			SharedPreferences sp = getSharedPreferences(
					ApplicationConstants.HY_SharedPreferences, MODE_PRIVATE);

			final String info = sp.getString("UserName", "") + ":"
					+ sp.getString("UserPassword", "");

			// 通过Handler发送消息
			webView.post(new Runnable() {
				@Override
				public void run() {
					// 注意调用的JS方法名要对应上 // 调用javascript的callJS()方法
					webView.loadUrl("javascript:show('" + info + "')");
				}
			});
		}
	}

	// 设置回退
	// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (webView.canGoBack()) {
				webView.goBack(); // goBack()表示返回WebView的上一页面

				String url = webView.getUrl();
				if (url.contains(historyUrl))
					closeWakeLock();
			} else
				this.finish();

			return true;
		}
		return false;
	}

	private void getVersion() {

		if (NetworkUtils.isNetworkAvailable()) {
			data1.getzdVersion(new AsyncHttpResponseHandler() {
				@Override
				public void onStart() {
					super.onStart();
					dialog = dialogUtil.loading("加载中", "请稍候...");
				}

				@SuppressWarnings("unchecked")
				@Override
				public void onSuccess(String arg0) {
					super.onSuccess(arg0);

					ConvertData cd = new ConvertData(arg0);
					if ("success".equalsIgnoreCase(cd.getReturnCode())) {
						HashMap<String, Object> var = (HashMap<String, Object>) cd
								.getContent();
						UpdateInfoData up = new UpdateInfoData(var);
						// 先读取配置文件，查看用户是否已经忽略当前升级版本
						// 读取配置信息，查看用户是否已经忽略当前版本的升级
						SharedPreferences spNoUpdate = MainActivity.this
								.getSharedPreferences(
										ApplicationConstants.HY_noUpdate_SharedPreferences,
										Activity.MODE_PRIVATE);
						String noUpdateVersion = spNoUpdate.getString(
								"noUpdate", "");
						if ((getVersionName().equals(up.getVersion()))) {
							setDialog();
						} else {
							UpdateManager mUpdateManager = new UpdateManager(
									MainActivity.this, up.getUrl(), "最新版本为："
											+ up.getVersion() + "\r\n"
											+ up.getNote(), up.getVersion());
							mUpdateManager.checkUpdateInfo();
						}
					} else {
						setDialog();
					}
					dialog.dismiss();

				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					setDialog();
					dialog.dismiss();
				}
			});
		} else {
			setDialog();
		}

	}

	private void setDialog() {
		if (active) {
			String str = "当前版本："
					+ getVersionName()
					+ "\n本软件下载、安装免费，使用中的通信流量费由运营商收取。\n\n官方网站：http://www.hx-oil.com\n客服电话：+86 020-66676862\nEmail：hxshipping@yeah.net\n\n对于在使用的过程中的任何问题和意见，欢迎发邮件给我们。\n\n";
			AlertDialog ad = new AlertDialog.Builder(MainActivity.this)
					.setTitle("航易船舶监控系统").setMessage(str)
					.setPositiveButton("确定", null).create();
			ad.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
			ad.show();
		}
	}

	private String getVersionName() {
		String version = "";
		try {
			// 获取packagemanager的实例
			PackageManager packageManager = getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			version = packInfo.versionName;
		} catch (Exception e) {
		}
		return version;
	}

	public void init() {
		webView.loadUrl("file:///android_asset/about.html");
	}

	public void init2() {
		webView.loadUrl("file:///"
				+ Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/hy" + "/about.html");
	}

	@Override
	protected void onStart() {
		super.onStart();
		active = true;
//		if (reload) {
//			String url = webView.getUrl();
//			if (url != null && (url.contains(homeUrl)))
//				webView.reload();
//		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		String url = webView.getUrl();
		if (url != null && (url.contains(homeUrl)))
			webView.reload();
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		active = false;
		reload = true;
	}

	@Override
	public void callBack() {
		setDialog();
	}
}
