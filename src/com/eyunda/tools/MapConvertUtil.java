package com.eyunda.tools;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;

public class MapConvertUtil {
	// 将google地图、soso地图、aliyun地图、mapabc地图和amap地图// 所用坐标转换成百度坐标    
	public static LatLng convertFromGoogle(LatLng sourceLatLng){
		CoordinateConverter converter  = new CoordinateConverter();    
		converter.from(CoordType.COMMON);    
		converter.coord(sourceLatLng);    
		LatLng desLatLng = converter.convert();   
		return desLatLng;
	}
	// 将GPS设备采集的原始GPS坐标转换成百度坐标    
	public static LatLng convertFromGPS(LatLng sourceLatLng){
		CoordinateConverter converter  = new CoordinateConverter();    
		converter.from(CoordType.GPS);    
		converter.coord(sourceLatLng);    
		LatLng desLatLng = converter.convert(); 
		return desLatLng;
	}
}
