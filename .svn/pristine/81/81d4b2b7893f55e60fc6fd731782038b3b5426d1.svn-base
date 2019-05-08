package com.eyunda.third.locatedb;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.tools.log.FilePathGenerator;
import com.eyunda.tools.log.Log;

/** 本地缓存，文件处理类 */
public class SharedPreferencesUtils {
	private String fileName;
	private SharedPreferences sp;
	private Context context;

	public SharedPreferencesUtils(String url, Map<String, Object> apiParams) {
//		 Log.setLog2FileEnabled(true);
//		 Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("eyunda",".log"));
		
		context = GlobalApplication.getInstance();
		SharedPreferences eyundaBindingCode = context.getSharedPreferences(
				"eyundaBindingCode", Context.MODE_PRIVATE);
		String userId = eyundaBindingCode.getString("id", "");// 区分不同用户登入
		if (apiParams == null) {
			String tt = userId + url+new HashMap<String, Object>().toString();
			fileName = MD5.toMD5(tt.trim());
//			Log.d("当前用户："+userId+"; 访问url:"+url
//			+"; 请求apiParams==null"+"; MD5后fileName:"+fileName);	
		} else {
			if (apiParams.containsKey(ApplicationConstants.SESSIONID)||apiParams.containsKey(ApplicationConstants.CONTENTMD5)) {
				Iterator iterator = apiParams.keySet().iterator();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					if (ApplicationConstants.SESSIONID.equals(key)) {
						iterator.remove();
					}else if(ApplicationConstants.CONTENTMD5.equals(key)){
						iterator.remove();
					}
				}
			}
			Object[] key = apiParams.keySet().toArray(); 
			Arrays.sort(key); 
			LinkedHashMap<String, Object> lhm = new LinkedHashMap<String, Object>();
			for (int i = 0; i < apiParams.size(); i++) { 
				lhm.put((String) key[i], apiParams.get(key[i])); 
			} 
			String tt = userId + url + lhm.toString();
			fileName = MD5.toMD5(tt.trim());
//			Log.d("当前用户："+userId+"; 访问url:"+url
//		+"; 请求apiParams:"+apiParams.toString()+"; MD5后fileName:"+fileName);		
		}
		sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
	}

	public void setParam(String json) {

			SharedPreferences.Editor editor = sp.edit();
			String contentMD5 = MD5.toMD5(json);
			
			editor.putString(fileName, contentMD5);
			editor.putString(ApplicationConstants.CONTENTMD5, json);
			
//		Log.d("缓存setParam---fileName:"+fileName+";---json:"+json+".");
			editor.commit();
		
	}

	public String getParam() {
		String json = sp.getString(ApplicationConstants.CONTENTMD5, "");
//		Log.d("缓存getParam---fileName:"+fileName+";---json:"+json+".");
		return json;

	}
	
	public String getContentMD5(){
		String contentMD5 = sp.getString(fileName, "");
		return contentMD5;
	}
}
