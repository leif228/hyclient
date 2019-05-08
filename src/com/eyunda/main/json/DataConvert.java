package com.eyunda.main.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataConvert {

	public static List<Map<String, String>> toArrayList(String array) {
		if (array != null && array.startsWith("\ufeff")) {
			array = array.substring(1);
		}
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		try {
			JSONArray jdata = new JSONArray(array);

			for (int i = 0; i < jdata.length(); i++) {
				JSONObject obj = jdata.getJSONObject(i);
				Map<String, String> map1 = new HashMap<String, String>();
				for (Iterator iterator = obj.keys(); iterator.hasNext();) {
					String key = (String) iterator.next();
					if (obj.get(key) instanceof String) {
						map1.put(key, obj.getString(key));
					} else if (obj.get(key) instanceof Integer) {
						map1.put(key, String.valueOf(obj.getInt(key)));
					} else if (obj.get(key) instanceof Long) {
						map1.put(key, String.valueOf(obj.getLong(key)));
					} else if (obj.get(key) instanceof Double) {

					} else {
						map1.put(key, obj.get(key).toString());
					}
					if (obj.get(key) == null
							|| obj.get(key).toString().equals("null")) {
						map1.put(key, "");
					}

				}

				list.add(map1);
			}

		} catch (Exception e) {
			return null;
		}
		return list;
	}

	public static Map<String, String> toMap(String json) {
		if (json != null && json.startsWith("\ufeff")) {
			json = json.substring(1);
		}

		Map<String, String> map = new HashMap<String, String>();
		try {
			JSONObject jdata = new JSONObject(json);
			for (Iterator<?> iterator = jdata.keys(); iterator.hasNext();) {
				String key = (String) iterator.next();
				if (jdata.get(key) instanceof String) {
					map.put(key, jdata.getString(key));
				} else if (jdata.get(key) instanceof Integer) {
					map.put(key, String.valueOf(jdata.getInt(key)));
				} else if (jdata.get(key) instanceof Long) {
					map.put(key, String.valueOf(jdata.getLong(key)));
				} else if (jdata.get(key) instanceof Double) {
					map.put(key, jdata.get(key) + "");
				} else {
					map.put(key, jdata.get(key).toString());
				}
				if (jdata.get(key) == null
						|| jdata.get(key).toString().equals("null")) {
					map.put(key, "");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return map;

	}
}