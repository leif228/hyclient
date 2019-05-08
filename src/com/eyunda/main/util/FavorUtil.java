package com.eyunda.main.util;

import java.util.HashMap;
import java.util.Map;


public class FavorUtil {
	private static Map<String, String> ALLFAVORS = new HashMap<String, String>();//院校
	private static Map<String, String> ALLFAVORSINFO = new HashMap<String, String>();//资讯
	public static boolean isFavor(String id){
		 
		return  ALLFAVORS.containsKey(id);
	}
	public static boolean isFavorinfo(String id){
		 
		return  ALLFAVORSINFO.containsKey(id);
	}
	
	
	public static void addFavor(String id){
		 ALLFAVORS.put(id, "");
	}
	public static void addFavorInfo(String id){
		ALLFAVORSINFO.put(id, "");
	}
	public static void removeFavor(String id){
		 ALLFAVORS.remove(id);
	}
	public static void removeFavorInfo(String id){
		ALLFAVORSINFO.remove(id);
	}
	
	public static void clear( ){
		 ALLFAVORS.clear();
	}
	public static void clearInfo( ){
		ALLFAVORSINFO.clear();
	}
	public static boolean ifNull( ){
		return ALLFAVORS.size()==0;
	}
	public static boolean ifNullInfo( ){
		return ALLFAVORSINFO.size()==0;
	}
}
