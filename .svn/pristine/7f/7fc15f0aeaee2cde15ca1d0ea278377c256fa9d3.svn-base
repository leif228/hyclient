package com.eyunda.third.loaders;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpRequest;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;

/**
 * 同步请求api服务
 * @author guoqiang
 *
 */
public class SynData_loader extends AsyncHttpClient {

	protected static String serverUrl = ApplicationConstants.SERVER_URL; //2.0服务 测试服务器
	protected static String encoding = "UTF-8";

	private int responseCode;
	protected String result;
	protected AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {

		@Override
		protected void sendResponseMessage(org.apache.http.HttpResponse response) {
			responseCode = response.getStatusLine().getStatusCode();
			super.sendResponseMessage(response);
		}

		@Override
		protected void sendMessage(Message msg) {
			handleMessage(msg);
		}

		@Override
		public void onSuccess(String content) {
			result = content;
		}

		@Override
		public void onFailure(Throwable error, String content) {
			result = onRequestFailed(error, content);
		}
	};

	public int getResponseCode() {
		return responseCode;
	}

	@Override
	protected void sendRequest(DefaultHttpClient client,
			HttpContext httpContext, HttpUriRequest uriRequest,
			String contentType, AsyncHttpResponseHandler responseHandler,
			Context context) {
		if (contentType != null) {
		}
		new AsyncHttpRequest(client, httpContext, uriRequest, responseHandler).run();
	}

	public String onRequestFailed(Throwable error, String content) {
		return "";
	}

	public String bulidUrl(String url){
		return serverUrl+url;
	}
	
	
	private String get(String url, RequestParams params) {
		url = this.bulidUrl(url);
		this.get(url, params, responseHandler);
		return result;
	}


	private String post(String url, RequestParams params) {
		url = this.bulidUrl(url);
		this.post(url, params, responseHandler);
		return result;
	}

	public String getApiResult( String url){
		Map<String,Object> apiParams = new HashMap<String, Object>();
		return this.getApiResult(url, apiParams,"get");
	}
	
	public String getApiResult( String url,Map<String,Object> apiParams){
		return this.getApiResult(url, apiParams,"post");
	}
	
	public String getApiResult( String url,Map<String,Object> apiParams,String method){
		RequestParams params = new RequestParams();
		//判断是否存在sessionID
		String sessionId = "";
		if(GlobalApplication.getInstance() != null){
			sessionId = GlobalApplication.getInstance().getUserData().getSessionId();
		}
		if(!apiParams.containsKey(ApplicationConstants.SESSIONID)){
			apiParams.put(ApplicationConstants.SESSIONID, sessionId);
		}
		//Log.d("sessionId-key", ApplicationConstants.SESSIONID);
		//Log.d("sessionId-value", sessionId);
		//动态塞入请求参数
		if(apiParams!=null && !apiParams.isEmpty()){
			for(Map.Entry<String, Object> entry : apiParams.entrySet()){
				Object val =  entry.getValue();
				try {
					if(val instanceof File){
						params.put(entry.getKey(), ((File) entry.getValue()).getAbsoluteFile());
					}else{
						//params.put(entry.getKey(), URLEncoder.encode(String.valueOf( entry.getValue()), encoding));
						params.put(entry.getKey(), String.valueOf( entry.getValue()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}

		if(method.equalsIgnoreCase("get")){
			this.get(url,params);
		}else{
			this.post(url,params);
		}
		return result;
	}
}
