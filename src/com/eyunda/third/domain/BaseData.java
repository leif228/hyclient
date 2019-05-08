package com.eyunda.third.domain;

import java.io.Serializable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BaseData implements Serializable {
	private static final long serialVersionUID = -1L;

	public BaseData() {
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
