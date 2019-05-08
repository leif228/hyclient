package com.eyunda.main.util;

import java.util.HashMap;
import java.util.Map;

public class PlugUtil {

	public static Map<String, String> ALLPLUG = new HashMap<String, String>();

	public static boolean isFavor(String id) {

		return ALLPLUG.containsKey(id);
	}

	public static void addFavor(String id) {
		ALLPLUG.put(id, "");
	}

	public static void removeFavor(String id) {
		ALLPLUG.remove(id);
	}
}
