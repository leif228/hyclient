package com.hangyi.zd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.cookie.Cookie;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.text.TextUtils;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.loaders.Data_loader;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.PersistentCookieStore;

public class ReLoginListenerService extends Service {  
  
    Thread reLoginThread;
	private Data_loader data1;
	private SharedPreferences sp;
    
    @Override  
    public void onCreate() {  
        super.onCreate(); 
        data1 = new Data_loader();
        sp = getSharedPreferences( "UserInfoConfig", MODE_PRIVATE);
        doReLogin();
    }  
    
    public void doReLogin(){
    	reLoginThread = new Thread(new Runnable() {
    		
    		@Override
    		public void run() {
    			while(true){
    				if(GlobalApplication.getInstance().getCookies().isEmpty()&&!sp.getString("UserName", "").equals("")){
    					autologin2() ;
    					try {
							Thread.sleep(10*1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
    				}
    			}
    		}
    	});
    	reLoginThread.start();
    }
    
    private void autologin2() {
		PersistentCookieStore myCookieStore = new PersistentCookieStore(this);    
		data1.asyncHttpClient.setCookieStore(myCookieStore); 

		Map<String, Object> params = new HashMap<String, Object>();

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				//返回0登录成功，-1密码错误，-2未知错误，-3找不到用户
				if("0".equals(content)){
					getCookieText();
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
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	
    	return super.onStartCommand(intent, flags, startId);
    }
  
    @Override  
    public void onDestroy() { 
    	reLoginThread =null;
    	super.onDestroy();  
    }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}  

}
