package com.eyunda.tools;

public class CooordUtil {

	private static final double EARTH_RADIUS = 6378137;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
	 * 
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @return
	 */
	public static double GetDistance(double lng1, double lat1, double lng2, double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 1000.0) / 1000.0;
		return s;
	}

	/** 经度113°53'5.40\"E */
	public static String convertLon(String lon) {
		lon = lon.trim();
		double longitude = 0.0;
		if (lon != null && !"".equals(lon)) {
			double du = Double.parseDouble(lon.substring(0, lon.indexOf("°")));
			double fen = Double.parseDouble(lon.substring(lon.indexOf("°") + 1, lon.indexOf("'")));
			double miao = Double.parseDouble(lon.substring(lon.indexOf("'") + 1, lon.indexOf("\"")));
			longitude = Math.round((du + fen / 60 + miao / 60 / 60) * 100.0) / 100.0;
			if (!lon.substring(lon.length() - 1).equals("E"))
				longitude = -1 * longitude;
		}
		return Double.toString(longitude);
	}

	/** 纬度22°27'15.82\"N */
	public static String convertLat(String lat) {
		lat = lat.trim();
		double latitude = 0.0;
		if (lat != null && !"".equals(lat)) {
			double du = Double.parseDouble(lat.substring(0, lat.indexOf("°")));
			double fen = Double.parseDouble(lat.substring(lat.indexOf("°") + 1, lat.indexOf("'")));
			double miao = Double.parseDouble(lat.substring(lat.indexOf("'") + 1, lat.indexOf("\"")));
			latitude = Math.round((du + fen / 60 + miao / 60 / 60) * 100.0) / 100.0;
			if (!lat.substring(lat.length() - 1).equals("N"))
				latitude = -1 * latitude;
		}
		return Double.toString(latitude);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double distance = GetDistance(118.06, 24.27, 121.29, 31.14);
		System.out.println("厦门到上海的距离(km):" + distance);
	}

}
