package com.eyunda.third.loaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import android.content.Intent;

import com.eyunda.main.reg.MD5Util;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.locatedb.NeedCacheUtils;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.NoNetworkDialog;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.ta.annotation.TAInject;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.PersistentCookieStore;
import com.ta.util.http.RequestParams;

public class Data_loader {
	// protected static String serverUrl = "http://59.175.144.111:9001";//正式服务

	protected static String serverUrl = ApplicationConstants.SERVER_URL; // 2.0服务
																			// 测试服务器

	protected static String encoding = "UTF-8";
	@TAInject
	public static AsyncHttpClient asyncHttpClient;
	public static CookieStore bcs;

	public Data_loader() {
		if (asyncHttpClient == null)
			asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.setTimeout(10 * 1000);
	}

	/**
	 * 通用接口
	 * 
	 * @param handler
	 *            结果回调处理函数
	 * @param apiUrl
	 *            请求接口地址
	 */
	public void getApiResult(AsyncHttpResponseHandler handler, String apiUrl) {
		Map<String, Object> params = new HashMap<String, Object>();
		getApiResult(handler, apiUrl, params, "get");
	}

	/**
	 * 通用接口
	 * 
	 * @param handler
	 *            结果回调处理函数
	 * @param apiUrl
	 *            请求接口地址
	 * @param apiParams
	 *            请求参数map
	 */
	public void getApiResult(AsyncHttpResponseHandler handler, String apiUrl,
			Map<String, Object> apiParams) {
		getApiResult(handler, apiUrl, apiParams, "post");
	}

	/**
	 * 
	 * 通用接口
	 * 
	 * @param handler
	 *            结果回调处理函数
	 * @param apiUrl
	 *            请求接口地址
	 * @param apiParams
	 *            请求参数map
	 * @param method
	 *            请求方式"post"/"get"
	 */
	public void getApiResult(AsyncHttpResponseHandler handler, String apiUrl,
			Map<String, Object> apiParams, String method) {
		// 1.网络不通情况
		if (!NetworkUtils.isNetworkAvailable()) {
			// 判断此url是否要读缓存数据
			if (NeedCacheUtils.isNeedCacheUrl(apiUrl)) {
				SharedPreferencesUtils spu = new SharedPreferencesUtils(apiUrl,
						apiParams);
				String json = spu.getParam();
				if ("".equals(json))
					;
				else {
					handler.onStart();
					handler.onSuccess(json);
				}
				return;
			}
			// todo转到网络不通activity
			// Log.setLog2FileEnabled(true);
			// Log.setFilePathGenerator(new
			// FilePathGenerator.DefaultFilePathGenerator("eyunda",".log"));
			// Log.d("Data_loader", "打印网络不通情况下没有缓存处理的 请求Url:"+apiUrl);

			Intent intent = new Intent(GlobalApplication.getInstance(),
					NoNetworkDialog.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			GlobalApplication.getInstance().startActivity(intent);
			
			if(!apiUrl.equals("/mobile/login/autoLogin"))
				return;
		}

		// 2.网络连通情况
		if (NeedCacheUtils.isNeedCacheUrl(apiUrl)) {
			SharedPreferencesUtils spu = new SharedPreferencesUtils(apiUrl,
					apiParams);
			String contentMD5 = spu.getContentMD5();
			apiParams.put(ApplicationConstants.CONTENTMD5, contentMD5);
		}

		RequestParams params = new RequestParams();
		// 判断是否存在sessionID
		String sessionId = "";
		if (GlobalApplication.getInstance().getUserData() != null) {
			sessionId = GlobalApplication.getInstance().getUserData()
					.getSessionId();
		}
		if (!apiParams.containsKey(ApplicationConstants.SESSIONID)) {
			apiParams.put(ApplicationConstants.SESSIONID, sessionId);
		}
		// Log.d("sessionId-key", ApplicationConstants.SESSIONID);
		// Log.d("sessionId-value", sessionId);
		System.err.println("sessionid:" + sessionId);
		// 动态塞入请求参数
		if (apiParams != null && !apiParams.isEmpty()) {
			for (Map.Entry<String, Object> entry : apiParams.entrySet()) {
				Object val = entry.getValue();
				try {
					if (val instanceof File) {
						params.put(entry.getKey(),
								((File) entry.getValue()).getAbsoluteFile());
					} else {
						// params.put(entry.getKey(),
						// URLEncoder.encode(String.valueOf( entry.getValue()),
						// encoding));
						params.put(entry.getKey(),
								String.valueOf(entry.getValue()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		String toUrl = serverUrl + apiUrl;
		if (method.equalsIgnoreCase("download")) {
			asyncHttpClient.download(toUrl, params, handler);
		} else if (method.equalsIgnoreCase("post")) {
			asyncHttpClient.post(toUrl, params, handler);
		} else {
			if (apiParams != null && !apiParams.isEmpty()) {
				asyncHttpClient.get(toUrl, params, handler);
			} else {
				asyncHttpClient.get(toUrl, handler);
			}
		}

	}
	
	public void getZd_JavaManageResult(AsyncHttpResponseHandler handler, String apiUrl, Map<String, Object> apiParams, String method) {
//		if (!Util.getCookies().isEmpty()) {
//			BasicCookieStore bcs = new BasicCookieStore();
//			bcs.addCookies(Util.getCookies().toArray(
//					new Cookie[Util.getCookies().size()]));
//			asyncHttpClient.setCookieStore(bcs);
//		}
		RequestParams params = new RequestParams();
		// 动态塞入请求参数
		if (apiParams != null && !apiParams.isEmpty()) {
			for (Map.Entry<String, Object> entry : apiParams.entrySet()) {
				Object val = entry.getValue();
				try {
					if (val instanceof File) {
						params.put(entry.getKey(),
								((File) entry.getValue()).getAbsoluteFile());
					} else {
						// params.put(entry.getKey(),
						// URLEncoder.encode(String.valueOf( entry.getValue()),
						// encoding));
						params.put(entry.getKey(),
								String.valueOf(entry.getValue()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
		String toUrl = ApplicationConstants.ZDJAVA_PRE_URL + apiUrl;
		if (method.equalsIgnoreCase("download")) {
			asyncHttpClient.download(toUrl, params, handler);
		} else if (method.equalsIgnoreCase("post")) {
			asyncHttpClient.post(toUrl, params, handler);
		} else {
			if (apiParams != null && !apiParams.isEmpty()) {
				asyncHttpClient.get(toUrl, params, handler);
			} else {
				asyncHttpClient.get(toUrl, handler);
			}
		}
		
	}
	public void getZd_ApiResult(AsyncHttpResponseHandler handler, String apiUrl, Map<String, Object> apiParams, String method) {
//			if (!GlobalApplication.getInstance().getCookies().isEmpty()) {
//				if(bcs == null){
//				
//				bcs = new BasicCookieStore();
//				List<Cookie> list = GlobalApplication.getInstance().getCookies();
//
//				String PHPSESSID = "";
//				  int version = 0;
//				  String domain = "";
//				  Date expiry = new Date();
//				        for (Cookie cookie : list) {
//				            if (cookie.getName().equals("PHPSESSID")) {
//				            	PHPSESSID = cookie.getValue();
//				            }
//				            version = cookie.getVersion();
//				            domain = cookie.getDomain();
//				            expiry = cookie.getExpiryDate();
//				        }
//				
//				BasicClientCookie cookie = new BasicClientCookie("PHPSESSID", PHPSESSID);
//				    cookie.setVersion(version);
//				    cookie.setDomain(domain);
//				    cookie.setExpiryDate(expiry);
//				
//				bcs.addCookie(cookie);
//				int i = bcs.getCookies().size();
//				asyncHttpClient.setCookieStore(bcs);
//			}else{
//				int wr = bcs.getCookies().size();
//				System.out.println(wr);
//			}
//		}
			
//		if (!GlobalApplication.getInstance().getCookies().isEmpty()) {
//			
//			CookieStore bcs = new BasicCookieStore();
//			List<Cookie> list = GlobalApplication.getInstance().getCookies();
//
//			String PHPSESSID = "";
//			  int version = 0;
//			  String domain = "";
//			  Date expiry = new Date();
//			        for (Cookie cookie : list) {
//			            if (cookie.getName().equals("PHPSESSID")) {
//			            	PHPSESSID = cookie.getValue();
//			            }
//			            version = cookie.getVersion();
//			            domain = cookie.getDomain();
//			            expiry = cookie.getExpiryDate();
//			        }
//			
//			BasicClientCookie cookie = new BasicClientCookie("PHPSESSID", PHPSESSID);
//			    cookie.setVersion(version);
//			    cookie.setDomain(domain);
//			    cookie.setExpiryDate(expiry);
//			
//			bcs.addCookie(cookie);
//			int i = bcs.getCookies().size();
//			asyncHttpClient.setCookieStore(bcs);
//		}
		
		RequestParams params = new RequestParams();
		// 动态塞入请求参数
		if (apiParams != null && !apiParams.isEmpty()) {
			for (Map.Entry<String, Object> entry : apiParams.entrySet()) {
				Object val = entry.getValue();
				try {
					if (val instanceof File) {
						params.put(entry.getKey(),
								((File) entry.getValue()).getAbsoluteFile());
					} else {
						// params.put(entry.getKey(),
						// URLEncoder.encode(String.valueOf( entry.getValue()),
						// encoding));
						params.put(entry.getKey(),
								String.valueOf(entry.getValue()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		String toUrl = ApplicationConstants.HYPHP_PRE_URL + apiUrl;
		if (method.equalsIgnoreCase("download")) {
			asyncHttpClient.download(toUrl, params, handler);
		} else if (method.equalsIgnoreCase("post")) {
			asyncHttpClient.post(toUrl, params, handler);
		} else {
			if (apiParams != null && !apiParams.isEmpty()) {
				asyncHttpClient.get(toUrl, params, handler);
			} else {
				asyncHttpClient.get(toUrl, handler);
			}
		}

	}
	public void getHY_ApiResult(AsyncHttpResponseHandler handler, String apiUrl, Map<String, Object> apiParams, String method) {
//			if (!GlobalApplication.getInstance().getCookies().isEmpty()) {
//				if(bcs == null){
//				
//				bcs = new BasicCookieStore();
//				List<Cookie> list = GlobalApplication.getInstance().getCookies();
//
//				String PHPSESSID = "";
//				  int version = 0;
//				  String domain = "";
//				  Date expiry = new Date();
//				        for (Cookie cookie : list) {
//				            if (cookie.getName().equals("PHPSESSID")) {
//				            	PHPSESSID = cookie.getValue();
//				            }
//				            version = cookie.getVersion();
//				            domain = cookie.getDomain();
//				            expiry = cookie.getExpiryDate();
//				        }
//				
//				BasicClientCookie cookie = new BasicClientCookie("PHPSESSID", PHPSESSID);
//				    cookie.setVersion(version);
//				    cookie.setDomain(domain);
//				    cookie.setExpiryDate(expiry);
//				
//				bcs.addCookie(cookie);
//				int i = bcs.getCookies().size();
//				asyncHttpClient.setCookieStore(bcs);
//			}else{
//				int wr = bcs.getCookies().size();
//				System.out.println(wr);
//			}
//		}
		
//		if (!GlobalApplication.getInstance().getCookies().isEmpty()) {
//			
//			CookieStore bcs = new BasicCookieStore();
//			List<Cookie> list = GlobalApplication.getInstance().getCookies();
//
//			String PHPSESSID = "";
//			  int version = 0;
//			  String domain = "";
//			  Date expiry = new Date();
//			        for (Cookie cookie : list) {
//			            if (cookie.getName().equals("PHPSESSID")) {
//			            	PHPSESSID = cookie.getValue();
//			            }
//			            version = cookie.getVersion();
//			            domain = cookie.getDomain();
//			            expiry = cookie.getExpiryDate();
//			        }
//			
//			BasicClientCookie cookie = new BasicClientCookie("PHPSESSID", PHPSESSID);
//			    cookie.setVersion(version);
//			    cookie.setDomain(domain);
//			    cookie.setExpiryDate(expiry);
//			
//			bcs.addCookie(cookie);
//			int i = bcs.getCookies().size();
//			asyncHttpClient.setCookieStore(bcs);
//		}
		
		RequestParams params = new RequestParams();
		// 动态塞入请求参数
		if (apiParams != null && !apiParams.isEmpty()) {
			for (Map.Entry<String, Object> entry : apiParams.entrySet()) {
				Object val = entry.getValue();
				try {
					if (val instanceof File) {
						params.put(entry.getKey(),
								((File) entry.getValue()).getAbsoluteFile());
					} else {
						// params.put(entry.getKey(),
						// URLEncoder.encode(String.valueOf( entry.getValue()),
						// encoding));
						params.put(entry.getKey(),
								String.valueOf(entry.getValue()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
		String toUrl = ApplicationConstants.HY_PRE_URL + apiUrl;
		if (method.equalsIgnoreCase("download")) {
			asyncHttpClient.download(toUrl, params, handler);
		} else if (method.equalsIgnoreCase("post")) {
			asyncHttpClient.post(toUrl, params, handler);
		} else {
			if (apiParams != null && !apiParams.isEmpty()) {
				asyncHttpClient.get(toUrl, params, handler);
			} else {
				asyncHttpClient.get(toUrl, handler);
			}
		}
		
	}

	/**
	 * 获取推荐船舶及航线
	 * 
	 * @param handler
	 */
	public void recommendBoat(AsyncHttpResponseHandler handler, String userid,
			String t) {
		if (userid.equals(""))
			userid = "0";
		asyncHttpClient.get(serverUrl + "/appService/recommendBoat/" + userid
				+ "?pageSize=5&curPage=0&t=" + t, handler);
	}

	/**
	 * 获取广告
	 * 
	 * @param handler
	 * @param id
	 */
	public void mobileAdService(AsyncHttpResponseHandler handler, String id) {

		asyncHttpClient.get(serverUrl + "/mobileAdService/listAd/" + id,
				handler);

	}

	/**
	 * 院校答疑
	 * 
	 * @param handler
	 * @param index
	 * @param userid
	 */
	public void collegequestions(AsyncHttpResponseHandler handler, int index,
			String userid) {
		asyncHttpClient.get(serverUrl + "/qaService/user/collegequestions/"
				+ userid + "?pageSize=10&curPage=" + index, handler);
	}

	/**
	 * 修改头像
	 * 
	 * @param handler
	 */
	public void uploadHead(AsyncHttpResponseHandler handler, String mobileNo,
			String path) {
		RequestParams params = new RequestParams();
		try {
			params.put("picture", new File(path));
		} catch (FileNotFoundException e) {
		}
		asyncHttpClient.post(serverUrl
				+ "/userService/mobileUser/uploadheadPortrait/" + mobileNo,
				params, handler);
	}

	/**
	 * 修改密码提示
	 * 
	 * @param handler
	 */
	public void updateQA(AsyncHttpResponseHandler handler, String password,
			String mobileNo, String passwordQuestion, String passwordAnswer) {
		RequestParams params = new RequestParams();
		params.put("password", MD5Util.MD5(password));
		params.put("passwordQuestion", passwordQuestion);
		params.put("passwordAnswer", passwordAnswer);
		asyncHttpClient.post(serverUrl
				+ "/userService/mobileUser/updatePasswdQuestion/" + mobileNo,
				params, handler);
	}

	/**
	 * 获取更新版本
	 * 
	 * @param handler
	 * @param cid
	 */
	public void getVersion(AsyncHttpResponseHandler handler) {

		// asyncHttpClient.get(
		// serverUrl+"/appService/latestVersion/android",
		// handler);
		// 判断网络是否连通
		if (!NetworkUtils.isNetworkAvailable()) {
			SharedPreferencesUtils s = new SharedPreferencesUtils(
					"/mobile/home/update", null);
			String json = s.getParam();
			handler.onSuccess(json);
			return;
		}
		asyncHttpClient.get(serverUrl + "/mobile/home/update", handler);
	}
	public void getzdVersion(AsyncHttpResponseHandler handler) {
		
		// asyncHttpClient.get(
		// serverUrl+"/appService/latestVersion/android",
		// handler);
		// 判断网络是否连通
//		if (!NetworkUtils.isNetworkAvailable()) {
//			SharedPreferencesUtils s = new SharedPreferencesUtils(
//					"/mobile/home/update", null);
//			String json = s.getParam();
//			handler.onSuccess(json);
//			return;
//		}
		asyncHttpClient.get(ApplicationConstants.HY_PRE_URL + ApplicationUrls.version, handler);
	}
	public void getzdVersion1(AsyncHttpResponseHandler handler) {
		
		// asyncHttpClient.get(
		// serverUrl+"/appService/latestVersion/android",
		// handler);
		// 判断网络是否连通
//		if (!NetworkUtils.isNetworkAvailable()) {
//			SharedPreferencesUtils s = new SharedPreferencesUtils(
//					"/mobile/home/update", null);
//			String json = s.getParam();
//			handler.onSuccess(json);
//			return;
//		}
		asyncHttpClient.get(ApplicationConstants.HY_PRE_URL + ApplicationUrls.version, handler);
	}
	
	public void getLastPosition(AsyncHttpResponseHandler handler,String mmsi){
		String url = "http://www.myships.com/myships/10025?type=1&mmsi=";
		asyncHttpClient.get(url + mmsi, handler);
	}
	/**
	 * 上传bug日志
	 * 
	 * @param handler
	 */
	public void uploadBug(AsyncHttpResponseHandler handler,
			String path) {
		RequestParams params = new RequestParams();
		try {
			params.put("mpf", new File(path));
		} catch (FileNotFoundException e) {
		}
		asyncHttpClient.post(ApplicationConstants.HY_PRE_URL
				+ ApplicationUrls.bugUpload,
				params, handler);
	}
}
