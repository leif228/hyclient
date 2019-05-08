package com.eyunda.main.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
 * 全局共享变量
 * @author guoqiang
 *
 */
public class SharedPreferencesutil {
	private static String content = "config";
	private static String gklc = "gklc";
	private static String cklc = "cklc";
	private static String yklc = "yklc";
	private static String zklc = "zklc";
	private static String loadimg = "loadimg";

	public static void saveGKLC(Context context, String id) {
		SharedPreferences sharedPre = context.getSharedPreferences(content,
				Context.MODE_PRIVATE);
		Editor editor = sharedPre.edit();
		editor.putString(gklc, id);
		editor.commit();
	}

	public static String getGKLC(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences(content,
				Context.MODE_PRIVATE);
		return sharedPre.getString(gklc, "");
	}
	
	public static void saveCKLC(Context context, String id) {
		SharedPreferences sharedPre = context.getSharedPreferences(content,
				Context.MODE_PRIVATE);
		Editor editor = sharedPre.edit();
		editor.putString(cklc, id);
		editor.commit();
	}

	public static String getCKLC(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences(content,
				Context.MODE_PRIVATE);
		return sharedPre.getString(cklc, "");
	}
	
	public static void saveYKLC(Context context, String id) {
		SharedPreferences sharedPre = context.getSharedPreferences(content,
				Context.MODE_PRIVATE);
		Editor editor = sharedPre.edit();
		editor.putString(yklc, id);
		editor.commit();
	}

	public static String getYKLC(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences(content,
				Context.MODE_PRIVATE);
		return sharedPre.getString(yklc, "");
	}
	
	public static void saveZKLC(Context context, String id) {
		SharedPreferences sharedPre = context.getSharedPreferences(content,
				Context.MODE_PRIVATE);
		Editor editor = sharedPre.edit();
		editor.putString(zklc, id);
		editor.commit();
	}

	public static String getZKLC(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences(content,
				Context.MODE_PRIVATE);
		return sharedPre.getString(zklc, "");
	}
	
	
	public static String getSplashURL(Context context){
		SharedPreferences sharedPre = context.getSharedPreferences(content,
				Context.MODE_PRIVATE);
		return sharedPre.getString(loadimg, "");
	}
	
	public static void setSplashURL(Context context, String url) {
		SharedPreferences sharedPre = context.getSharedPreferences(content,
				Context.MODE_PRIVATE);
		Editor editor = sharedPre.edit();
		editor.putString(loadimg, url);
		editor.commit();
	}

}
