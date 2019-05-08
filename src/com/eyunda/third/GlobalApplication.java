package com.eyunda.third;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Audio;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.mapapi.BMapManager;
import com.eyunda.third.chat.event.LoginStatusCode;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.CrashHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.baidu.push.Utils;
import com.hangyi.tools.CookieImageDownloader;
import com.hangyi.zd.SplashActivity;
import com.hangyi.zd.domain.GroupData;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.ta.TAApplication;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.PersistentCookieStore;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

/**
 * 保存全局变量
 */
public class GlobalApplication extends TAApplication {
	private static GlobalApplication instance;
	public static DisplayImageOptions displayImageOptions =new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.eyd_chat_search_clear_pressed)
		    .showImageOnFail(R.drawable.img_load_failed)  
		    .bitmapConfig(Bitmap.Config.RGB_565)
		    .cacheInMemory(true)    
		    .cacheOnDisc(true)      //缓存到sd卡
		    .displayer(new SimpleBitmapDisplayer()).build();    
	private UserData userData = null;// 为登入用户
	private LoginStatusCode loginStatus = LoginStatusCode.noLogin;
	private ImageLoaderConfiguration imageLoaderConfiguration;
	private File SdCardPath = Environment.getExternalStorageDirectory();// 获取SDCard目录

	private boolean chatMsgNotifyed = false;// 聊天消息过来时通知用
	private boolean haveOneChatMsgNotify = false;// 设置只有一个聊天消息提醒在通知栏
	
	BMapManager mBMapManager = null; 
	private GroupData groupData;

	public static GlobalApplication getInstance() {
		return instance;
	}
	private List<Cookie> cookies;
	private SharedPreferences sp;

	public List<Cookie> getCookies() {
		return cookies != null ? cookies : new ArrayList<Cookie>();
	}

	public void setCookies(List<Cookie> cookies) {
		this.cookies = cookies;
	}

	public GroupData getGroupData() {
		return groupData;
	}

	public void setGroupData(GroupData groupData) {
		this.groupData = groupData;
	}

	public File getSdCardPath() {
		return SdCardPath;
	}

	public void setSdCardPath(File sdCardPath) {
		SdCardPath = sdCardPath;
	}

	public synchronized LoginStatusCode getLoginStatus() {
		return loginStatus;
	}

	public synchronized void setLoginStatus(LoginStatusCode loginStatus) {
		this.loginStatus = loginStatus;
	}

	public synchronized UserData getUserData() {
		UserData ret = null;

		switch (loginStatus) {
		case noLogin:
			ret = new UserData();
			break;
//		case logining:
//			int n = 0;
//			while (this.userData == null && n++ <= 5) {
//				try {
//					Thread.sleep(200L);
//				} catch (InterruptedException e) {
//				}
//			}
//			ret = this.userData;
//			
//			if(!NetworkUtils.isNetworkAvailable()){//网络断了，给userData附值
//				SharedPreferencesUtils currUser = new SharedPreferencesUtils("currUser",null);
//				String json = currUser.getParam();
//				
//				Gson gson = new Gson();
//				HashMap<String, Object> map = gson.fromJson((String) json,
//						new TypeToken<Map<String, Object>>() {
//						}.getType());
//				ret = new UserData(
//						(Map<String, Object>) map.get("content"));
//			}
//			break;
		case logined:
			ret = this.userData;
			break;
		default:
			ret = this.userData;
			break;
		}

		return ret;
	}

	public synchronized void setUserData(UserData userData) {
		this.userData = userData;
	}

	public ImageLoaderConfiguration getImageLoaderConfiguration() {
		return imageLoaderConfiguration;
	}

	public void setImageLoaderConfiguration(ImageLoaderConfiguration imageLoaderConfiguration) {
		this.imageLoaderConfiguration = imageLoaderConfiguration;
	}

	public boolean isChatMsgNotifyed() {
		return chatMsgNotifyed;
	}

	public void setChatMsgNotifyed(boolean chatMsgNotifyed) {
		this.chatMsgNotifyed = chatMsgNotifyed;
	}

	public boolean isHaveOneChatMsgNotify() {
		return haveOneChatMsgNotify;
	}

	public void setHaveOneChatMsgNotify(boolean haveOneChatMsgNotify) {
		this.haveOneChatMsgNotify = haveOneChatMsgNotify;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		// registerActivityLifecycleCallbacks(new
		// MyActivityLifecycleCallbacks());

		// MessageThread thread = new MessageThread();
		// thread.start();// 开启消息处理线程
		// Intent intent = new Intent(this,com.eyunda.third.PushService.class);
		// startService(intent);

		// 缓存在sd卡中指定目录
		File cacheDir = new File(SdCardPath + "/hy/img");
		// 完成ImageLoaderConfiguration的配置
		imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(this)
//				.memoryCacheExtraOptions(200, 200)
				// 设置内存缓存的详细信息
				// max width, max height，即保存的每个缓存文件的最大长宽
//				.discCacheFileNameGenerator(new TrueFileNameGenerator())
				// .discCacheExtraOptions(48, 48, null, 0, null)//设置sd卡缓存的详细信息
				// 线程池内加载的数量
				.threadPoolSize(4)
				.threadPriority(Thread.NORM_PRIORITY - 2)
//				.discCache(new UnlimitedDiscCache(cacheDir))
				// 自定义缓存路径,图片缓存到sd卡
//				.tasksProcessingOrder(QueueProcessingType.FIFO)
//				.memoryCache(new LruMemoryCache(4 * 1024 * 1024))
//				.memoryCacheSizePercentage(10)
//				.discCacheFileCount(5000) //缓存的文件数量  
				// 超时时间 5秒
				.imageDownloader(
						new CookieImageDownloader(this, 5 * 1000, 30 * 1000))
				.imageDecoder(new BaseImageDecoder(true))
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.writeDebugLogs().build();// 开始构建

		ImageLoader.getInstance().init(imageLoaderConfiguration); // 初始化
		// 记录crash-log
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		
		initTBS();
//		initCookie();
		
//		initAPI_KEY();
//		loadAPI_KEY();
		//在APP应用启动的时候，进行初始化验证  
//        initEngineManager(this);  
	}

	private void initTBS() {
		//搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
		//TbsDownloader.needDownload(getApplicationContext(), false);
		
		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
			
			@Override
			public void onViewInitFinished(boolean arg0) {
				// TODO Auto-generated method stub
				Log.e("app", " onViewInitFinished is " + arg0);
			}
			
			@Override
			public void onCoreInitFinished() {
				// TODO Auto-generated method stub
				
			}
		};
		QbSdk.setTbsListener(new TbsListener() {
	            @Override
	            public void onDownloadFinish(int i) {
	                Log.d("app","onDownloadFinish");
	            }

	            @Override
	            public void onInstallFinish(int i) {
	                Log.d("app","onInstallFinish");
	            }

	            @Override
	            public void onDownloadProgress(int i) {
	                Log.d("app","onDownloadProgress:"+i);
	            }
	        });
		
		QbSdk.initX5Environment(getApplicationContext(),  cb);
	}

	/**
	 * 进行验证key
	 * 
	 * @param pContext
	 */
//	private void initEngineManager(Context pContext) {
//		if (mBMapManager == null) {
//			mBMapManager = new BMapManager();
//		}
//
//		if (!mBMapManager.init("dfsdfs", new MyGeneralListener())) {
//			Toast.makeText(MyApplication.getInstance(), "BMapManager  初始化错误!",
//					Toast.LENGTH_LONG).show();
//		}
//	}
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等  
//    static class MyGeneralListener implements MKGeneralListener {  
//          
//        @Override  
//        public void onGetNetworkState(int iError) {  
//            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {  
//                Toast.makeText(MyApplication.getInstance(), "您的网络出错啦！",  
//                    Toast.LENGTH_LONG).show();  
//            }  
//            else if (iError == MKEvent.ERROR_NETWORK_DATA) {  
//                Toast.makeText(MyApplication.getInstance(), "输入正确的检索条件！",  
//                        Toast.LENGTH_LONG).show();  
//            }  
//            // ...  
//        }  
//  
//        @Override  
//        public void onGetPermissionState(int iError) {  
//            //非零值表示key验证未通过  
//            if (iError != 0) {  
//                //授权Key错误：  
//                Toast.makeText(MyApplication.getInstance(),   
//                        "请在 DemoApplication.java文件输入正确的授权Key,并检查您的网络连接是否正常！error: "+iError, Toast.LENGTH_LONG).show();  
//                MyApplication.getInstance().m_bKeyRight = false;  
//            }  
//            else{  
//                MyApplication.getInstance().m_bKeyRight = true;  
//                Toast.makeText(MyApplication.getInstance(),   
//                        "key认证成功", Toast.LENGTH_LONG).show();  
//            }  
//        }  
//    }  

	private void loadAPI_KEY() {
		Map<String, Object> params = new HashMap<String, Object>();

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				//android的百度key:{"APIKey":"YRknVbnXEQ1SrPGu2fmblS4mQbSbPK40"}
				
				try {
					Gson gson = new Gson();
					HashMap<String, Object> result= gson.fromJson(
							content,
							new TypeToken<Map<String, Object>>() {
							}.getType());
					if(result!=null&&!result.isEmpty()){
						
//						initAPI_KEY((String)result.get("APIKey"));
						startPush();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
		};
		new Data_loader().getZd_ApiResult(handler, ApplicationUrls.api_key, params, "get");
	}

	private void initAPI_KEY() {
		
		ApplicationInfo appi;
		try {
			appi = getApplicationInfo();
			appi.metaData.putString(ApplicationConstants.api_key, "PdpcawihTq1HpInI1SxxTuZNHNDGeIyN");
			appi.metaData.putString(ApplicationConstants.com_baidu_lbsapi_API_KEY, "PdpcawihTq1HpInI1SxxTuZNHNDGeIyN");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void initCookie() {
		sp = getSharedPreferences( "UserInfoConfig", MODE_PRIVATE);
		String userName = sp.getString("UserName", "");
		if(!"".equals(userName)) {
			autologin2();
		}
	}
	private void autologin2() {
		Data_loader data1 = new Data_loader();
		
		PersistentCookieStore myCookieStore = new PersistentCookieStore(this);    
		data1.asyncHttpClient.setCookieStore(myCookieStore); 

		Map<String, Object> params = new HashMap<String, Object>();

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				//返回0登录成功，-1密码错误，-2未知错误，-3找不到用户
				if("0".equals(content)){
					getCookieText();
				}else {
					
					Editor editor = sp.edit();
					editor.putString("AutoLogin", "false");
					editor.putString("SavePassword", "false");
					editor.putString("UserName", "");
					editor.putString("UserPassword", "");
					editor.commit();
					
					startActivity(new Intent(GlobalApplication.this,SplashActivity.class));
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
		};

		params.put("username", sp.getString("UserName", ""));
		params.put("passcode", sp.getString("UserPassword", ""));
		params.put("clienttype", ApplicationConstants.clienttype);
		data1.getZd_ApiResult(handler, ApplicationUrls.login, params, "post");
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

	public void startPush(){
		Resources resource = getApplicationContext().getResources();
        String pkgName = getApplicationContext().getPackageName();
        
        PushManager.startWork(getApplicationContext(),
        		PushConstants.LOGIN_TYPE_API_KEY,Utils.getMetaValue(getApplicationContext(), "api_key"));
        
        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
        		resource.getIdentifier("notification_custom_builder", "layout", pkgName),
        		resource.getIdentifier("notification_icon", "id", pkgName),
        		resource.getIdentifier("notification_title", "id", pkgName),
        		resource.getIdentifier("notification_text", "id", pkgName));
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(getApplicationContext().getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(resource.getIdentifier(
        		"simple_notification_icon", "drawable", pkgName));
        cBuilder.setNotificationSound(Uri.withAppendedPath(
        		Audio.Media.INTERNAL_CONTENT_URI, "6").toString());
        // 推送高级设置，通知栏样式设置为下面的ID
        PushManager.setNotificationBuilder(getApplicationContext(), 1, cBuilder);
	}
	
	public void stopPush(){
		PushManager.stopWork(getApplicationContext());
	}
	public void resumePush(){
		PushManager.resumeWork(getApplicationContext());
	}
}
