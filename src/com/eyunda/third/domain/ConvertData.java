package com.eyunda.third.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class ConvertData implements Serializable {
	private static final long serialVersionUID = -1L;
	private Object content;
	private String message;
	private String returnCode;
	private String MD5String;
	
	
	public ConvertData() {
	}
	
	public ConvertData(String json) {
		try {
			Gson gson = new Gson();
			HashMap<String, Object> result= gson.fromJson(
					json,
					new TypeToken<Map<String, Object>>() {
					}.getType());
			returnCode =(String)result.get("returnCode");
			message = (String)result.get("message");
			content = result.get("content");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ConvertData(String json,String uri,Map<String, Object> params) {
		try {
			Gson gson = new Gson();
			HashMap<String, Object> result= gson.fromJson(
					json,
					new TypeToken<Map<String, Object>>() {
					}.getType());
			returnCode =(String)result.get("returnCode");
			message = (String)result.get("message");
			
			if(result.containsKey(ApplicationConstants.CONTENTMD5CHANGED)){
				boolean contentMD5Changed = (Boolean) result.get(ApplicationConstants.CONTENTMD5CHANGED);
				SharedPreferencesUtils s = new SharedPreferencesUtils(uri, params);
				if(contentMD5Changed&&NetworkUtils.isNetworkAvailable()){
					if(returnCode.equalsIgnoreCase("Success")){
						s.setParam(json);
					}
					content = result.get("content");
				}else{
					String localJsion = s.getParam();
					HashMap<String, Object> old= gson.fromJson(
							localJsion,
							new TypeToken<Map<String, Object>>() {
							}.getType());
					content = old.get("content");
				}

			}else{
				content = result.get("content");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}



	public String getMD5String() {
		return MD5String;
	}


	public void setMD5String(String mD5String) {
		MD5String = mD5String;
	}


	public String getJsonString() {
		Gson gson = new Gson();
		String jsonStr = gson.toJson(this);

		return jsonStr;
	}

	public JSONObject getJson() throws JSONException {
		Gson gson = new Gson();
		String jsonStr = gson.toJson(this);

		JSONObject json = new JSONObject(jsonStr);

		return json;
	}

	public Map<String, Object> getMap() {
		Gson gson = new Gson();
		String jsonStr = gson.toJson(this);

		Map<String, Object> map = gson.fromJson(jsonStr, new TypeToken<Map<String, Object>>() {
		}.getType());

		return map;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
