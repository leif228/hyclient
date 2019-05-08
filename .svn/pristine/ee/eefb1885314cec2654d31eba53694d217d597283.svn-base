package com.hangyi.tools;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.domain.location.ShipCooordData;
import com.eyunda.tools.CalendarUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hangyi.zd.activity.newplay.GpsDataQueue;
import com.hangyi.zd.domain.BaiduWeatherData;
import com.hangyi.zd.domain.MapPortData;
import com.hangyi.zd.domain.ModulePower;
import com.hangyi.zd.domain.NodeCode;
import com.hangyi.zd.domain.PoliceData;
import com.hangyi.zd.domain.PositionData;
import com.hangyi.zd.domain.ShipCKGpssData;
import com.hangyi.zd.domain.ShipGpsData;
import com.hangyi.zd.domain.ShipInfoData;
import com.hangyi.zd.domain.ShipModelCode;
import com.hangyi.zd.domain.ShipModelData;
import com.hangyi.zd.domain.ShipModelNoData;
import com.hangyi.zd.domain.ShipOneCKHcData;
import com.hangyi.zd.domain.ShipSCData;
import com.hangyi.zd.domain.ShipVoyageData;
import com.hangyi.zd.domain.ShipVoyageNodeData;
import com.hangyi.zd.domain.StaEndTimeImgData;
import com.hangyi.zd.domain.TimeSrcData;
import com.hangyi.zd.domain.UserPowerData;
import com.hangyi.zd.domain.UserPowerShipData;

public class ParseJson {
	
	/*{"Data":[{"ShipName":"桂平飞达0829","Detail":{"State":1,"ShipID":"0x104E","GPS":
{"GPSTime":"2016-07-20 14:15:46","GPSLongitude":113.453252,"GPSLatitude":23.092530,"GPSSpeed":0.00,"GPSCourse":0.00},
"PowerManagement":{"MainVoltage":14.376,"BackUpVoltage":0.427,"Temperature":52,"State":0}}},
{"ShipName":"桂平飞达630","Detail":{"State":1,"ShipID":"0x1048","GPS":
{"GPSTime":"2016-07-20 14:15:34","GPSLongitude":112.860775,"GPSLatitude":23.300173,"GPSSpeed":0.00,"GPSCourse":0.00},
"PowerManagement":{"MainVoltage":15.819,"BackUpVoltage":12.780,"Temperature":43,"State":0}}},
{"ShipName":"桂桂平货2368","Detail":{"State":1,"ShipID":"0x1058","GPS":
{"GPSTime":"2016-07-20 14:15:56","GPSLongitude":112.839588,"GPSLatitude":23.011370,"GPSSpeed":0.00,"GPSCourse":0.00},
"PowerManagement":{"MainVoltage":15.835,"BackUpVoltage":12.837,"Temperature":45,"State":0}}},
{"ShipName":"桂桂平货6678","Detail":{"State":1,"ShipID":"0x1073","GPS":
{"GPSTime":"2016-07-20 14:15:25","GPSLongitude":113.556630,"GPSLatitude":22.652707,"GPSSpeed":0.00,"GPSCourse":0.00},
"PowerManagement":{"MainVoltage":15.786,"BackUpVoltage":12.950,"Temperature":49,"State":1}}}]}*/
	
	/*{"Data":[{"ShipName":"\u6842\u5e73\u98de\u8fbe0829","Detail":{}}
	,{"ShipName":"\u6842\u5e73\u98de\u8fbe630","Detail":{}}
	,{"ShipName":"\u6842\u6842\u5e73\u8d272368","Detail":{"1":{"State":1,"ShipID":"0x1058","GPS":{"GPSTime":"2016-08-12 16:05:23","GPSLongitude":112.827938,"GPSLatitude":23.015078,"GPSSpeed":0,"GPSCourse":0,"BDLongitude":"112.83954491603","BDLatitude":"23.01820125859"},"PowerManagement":{"MainVoltage":15.835,"BackUpVoltage":12.926,"Temperature":41,"State":0}}}}
	,{"ShipName":"\u6842\u6842\u5e73\u8d276678","Detail":{"1":{"State":1,"ShipID":"0x1073","GPS":{"GPSTime":"2016-08-12 16:06:20","GPSLongitude":112.827995,"GPSLatitude":23.015173,"GPSSpeed":0,"GPSCourse":0,"BDLongitude":"112.83959855763","BDLatitude":"23.01829120055"},"PowerManagement":{"MainVoltage":15.843,"BackUpVoltage":12.748,"Temperature":38,"State":0}}}}
	,{"ShipName":"\u7ca4\u5b89\u987a621","Detail":{"1":{"State":1,"ShipID":"0x112C","GPS":{"GPSTime":"2016-08-12 16:06:43","GPSLongitude":113.562217,"GPSLatitude":23.086857,"GPSSpeed":0.1,"GPSCourse":0,"BDLongitude":"113.57390474748","BDLatitude":"23.08974270398"},"PowerManagement":{"MainVoltage":16.004,"BackUpVoltage":12.966,"Temperature":43,"State":0}}}}
	,{"ShipName":"\u7ca4\u5efa\u822a128","Detail":{"1":{"State":1,"ShipID":"0x112F","GPS":{"GPSTime":"2016-08-12 16:07:34","GPSLongitude":113.567787,"GPSLatitude":23.088315,"GPSSpeed":0,"GPSCourse":0,"BDLongitude":"113.57947463522","BDLatitude":"23.09114431024"},"PowerManagement":{"MainVoltage":16.012,"BackUpVoltage":12.74,"Temperature":42,"State":0}}}}]}*/
	public static ArrayList<ShipCooordData> parserHome(String text) {
		ArrayList<ShipCooordData> list = new ArrayList<ShipCooordData>();
		try {
			JSONObject jsonObject = new JSONObject(text);
			JSONArray jsonArray = jsonObject.getJSONArray("Data");
			for (int i = 0; i < jsonArray.length(); i++) {
				ShipCooordData GPSEntity = new ShipCooordData();
				
				JSONObject shipObject = jsonArray.getJSONObject(i);
				if(shipObject.has("ShipName"))
					GPSEntity.setShipName(Util.decodeUnicode(shipObject.getString("ShipName")));
				
				JSONObject detailObject = new JSONObject();
				if(shipObject.has("Detail"))
					detailObject = shipObject.getJSONObject("Detail");
				
				if(detailObject.has("State")){
//					JSONObject Object1 = detailObject.getJSONObject("1");
					GPSEntity.setDetailState(detailObject.getString("State"));
					GPSEntity.setShipID(detailObject.getString("ShipID"));
					if(detailObject.has("GPS")){
						JSONObject gPSObject = detailObject.getJSONObject("GPS");
						GPSEntity.setGpsTime(gPSObject.getString("GPSTime"));
						GPSEntity.setGpsLongitude(gPSObject.getString("GPSLongitude"));
						GPSEntity.setGpsLatitude(gPSObject.getString("GPSLatitude"));
						GPSEntity.setGpsSpeed(gPSObject.getString("GPSSpeed"));
						GPSEntity.setGpsCourse(gPSObject.getString("GPSCourse"));
						GPSEntity.setBdLongitude(gPSObject.getString("BDLongitude"));
						GPSEntity.setBdLatitude(gPSObject.getString("BDLatitude"));
					}
					if(detailObject.has("PowerManagement")){
						JSONObject powerManagementObject = detailObject.getJSONObject("PowerManagement");
						GPSEntity.setMainVoltage(powerManagementObject.getString("MainVoltage"));
						GPSEntity.setBackUpVoltage(powerManagementObject.getString("BackUpVoltage"));
						GPSEntity.setTemperature(powerManagementObject.getString("Temperature"));
						GPSEntity.setPowerManagementState(powerManagementObject.getString("State"));
					}
				}else
					continue;
				
					
				list.add(GPSEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("解析出错", "解析出错");
		}
		return list;
	}
//	public static ArrayList<ShipCooordData> parserHome(String text) {
//		ArrayList<ShipCooordData> list = new ArrayList<ShipCooordData>();
//		try {
//			JSONObject jsonObject = new JSONObject(text);
//			JSONArray jsonArray = jsonObject.getJSONArray("Data");
//			for (int i = 0; i < jsonArray.length(); i++) {
//				ShipCooordData GPSEntity = new ShipCooordData();
//				
//				JSONObject shipObject = jsonArray.getJSONObject(i);
//				if(shipObject.has("ShipName"))
//					GPSEntity.setShipName(Util.decodeUnicode(shipObject.getString("ShipName")));
//				
//				JSONObject detailObject = new JSONObject();
//				if(shipObject.has("Detail"))
//					detailObject = shipObject.getJSONObject("Detail");
//				
//				if(detailObject.has("State"))
//					GPSEntity.setDetailState(detailObject.getString("State"));
//				if(detailObject.has("ShipID"))
//					GPSEntity.setShipID(detailObject.getString("ShipID"));
//				
//				if(detailObject.has("GPS")){
//					JSONObject gPSObject = detailObject.getJSONObject("GPS");
//					GPSEntity.setGpsTime(gPSObject.getString("GPSTime"));
//					GPSEntity.setGpsLongitude(gPSObject.getString("GPSLongitude"));
//					GPSEntity.setGpsLatitude(gPSObject.getString("GPSLatitude"));
//					GPSEntity.setGpsSpeed(gPSObject.getString("GPSSpeed"));
//					GPSEntity.setGpsCourse(gPSObject.getString("GPSCourse"));
//				}else
//					continue;
//				if(detailObject.has("PowerManagement")){
//					JSONObject powerManagementObject = detailObject.getJSONObject("PowerManagement");
//					GPSEntity.setMainVoltage(powerManagementObject.getString("MainVoltage"));
//					GPSEntity.setBackUpVoltage(powerManagementObject.getString("BackUpVoltage"));
//					GPSEntity.setTemperature(powerManagementObject.getString("Temperature"));
//					GPSEntity.setPowerManagementState(powerManagementObject.getString("State"));
//				}
//				list.add(GPSEntity);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.i("解析出错", "解析出错");
//		}
//		return list;
//	}
	
	/*{"Datas":[{"Data":"0x104E,桂平飞达0829,5,1,2016-07-01 00:00:00,2017-12-31 23:59:59"},
    {"Data":"0x104E,桂平飞达0829,5,2,2016-07-01 00:00:00,2017-12-31 23:59:59"},
    {"Data":"0x104E,桂平飞达0829,5,3,2016-07-01 00:00:00,2017-12-31 23:59:59"},
    {"Data":"0x104E,桂平飞达0829,0,0,2016-07-01 00:00:00,2017-12-31 23:59:59"}]}*/
	
	/*{
	    "Datas": [
	        {
	            "Data": "0x104E,桂平飞达0829,5,1,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x104E,桂平飞达0829,5,2,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x104E,桂平飞达0829,5,3,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x104E,桂平飞达0829,0,0,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x104E,桂平飞达0829,4,0,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x1048,桂平飞达630,5,1,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x1048,桂平飞达630,5,2,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x1048,桂平飞达630,5,3,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x1048,桂平飞达630,0,0,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x1048,桂平飞达630,4,0,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x1058,桂桂平货2368,5,1,2016-07-01 00:00:00,2017-12-31 22:59:59"
	        },
	        {
	            "Data": "0x1058,桂桂平货2368,5,2,2016-07-01 00:00:00,2017-12-31 22:59:59"
	        },
	        {
	            "Data": "0x1058,桂桂平货2368,5,3,2016-07-01 00:00:00,2017-12-31 22:59:59"
	        },
	        {
	            "Data": "0x1058,桂桂平货2368,0,0,2016-07-01 00:00:00,2017-12-31 22:59:59"
	        },
	        {
	            "Data": "0x1058,桂桂平货2368,4,0,2016-07-01 00:00:00,2017-12-31 22:59:59"
	        },
	        {
	            "Data": "0x1073,桂桂平货6678,4,0,2016-07-01 00:00:00,2017-12-31 22:59:59"
	        },
	        {
	            "Data": "0x1073,桂桂平货6678,0,0,2016-07-01 00:00:00,2017-12-31 22:59:59"
	        },
	        {
	            "Data": "0x1073,桂桂平货6678,5,1,2016-07-01 00:00:00,2017-12-31 22:59:59"
	        },
	        {
	            "Data": "0x1073,桂桂平货6678,5,2,2016-07-01 00:00:00,2017-12-31 22:59:59"
	        },
	        {
	            "Data": "0x1073,桂桂平货6678,5,3,2016-07-01 00:00:00,2017-12-31 22:59:59"
	        },
	        {
	            "Data": "0x112C,粤安顺621,0,0,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x112C,粤安顺621,4,0,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x112C,粤安顺621,5,1,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x112C,粤安顺621,5,2,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x112C,粤安顺621,5,3,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x112C,粤安顺621,5,4,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x112F,粤建航128,0,0,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x112F,粤建航128,4,0,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x112F,粤建航128,5,1,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x112F,粤建航128,5,2,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x112F,粤建航128,5,3,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        },
	        {
	            "Data": "0x112F,粤建航128,5,4,2016-07-01 00:00:00,2017-12-31 23:59:59"
	        }
	    ],
	    "ModulePower": {
	        "PlayBack": "true",
	        "MultiCamera": "true",
	        "VoyageManage": "true",
	        "ByPort": "true",
	        "ByShipOwner": "true",
	        "ByCustomer": "true",
	        "AlarmSetting": "true",
	        "AlarmMessage": "true",
	        "ShipCount": "true"
	    }
	}*/

	/*ID号，船名，模块类型，模块编号，开始时间，结束时间
	模块类型：0gps,1流量计，2转速，3液位计，4设备状态，5货舱画面*/
	
	/*这个是在获取权限时，json里面新添加功能模块授权的内容，分别是：
	（1）历史回放（2）多组镜头（3）航次管理（4）航线-按码头（5）航线-按船公司
	（6）航线-按客户（7）报警设置（8）报警信息（9）驳船统计*/
	public static UserPowerData parserUserPower(String text) {
		Map<String,UserPowerShipData> userPowerShipDataMap = new HashMap<String,UserPowerShipData>();//String = "ID号"
		Map<String,ShipModelData> shipModelDataMap = new HashMap<String,ShipModelData>();//String = "ID号"+"模块类型"
		Map<String,ShipModelNoData> shipModelNoDataMap = new HashMap<String,ShipModelNoData>();//String = "ID号"+"模块类型"+"模块编号"
		
		UserPowerData entity = new UserPowerData();
		try {
			JSONObject jsonObject = new JSONObject(text);
			JSONArray jsonArray = jsonObject.getJSONArray("Datas");
			JSONObject modulePowerObject = jsonObject.getJSONObject("ModulePower");
			
			if(modulePowerObject!=null){
				ModulePower mp = new ModulePower();
				mp.setPlayBack(Boolean.valueOf(modulePowerObject.getString("PlayBack")));
				mp.setMultiCamera(Boolean.valueOf(modulePowerObject.getString("MultiCamera")));
				mp.setVoyageManage(Boolean.valueOf(modulePowerObject.getString("VoyageManage")));
				mp.setByPort(Boolean.valueOf(modulePowerObject.getString("ByPort")));
				mp.setByShipOwner(Boolean.valueOf(modulePowerObject.getString("ByShipOwner")));
				mp.setByCustomer(Boolean.valueOf(modulePowerObject.getString("ByCustomer")));
				mp.setAlarmSetting(Boolean.valueOf(modulePowerObject.getString("AlarmSetting")));
				mp.setAlarmMessage(Boolean.valueOf(modulePowerObject.getString("AlarmMessage")));
				mp.setShipCount(Boolean.valueOf(modulePowerObject.getString("ShipCount")));
				
				SharedPreferences sp = GlobalApplication.getInstance().getSharedPreferences(ApplicationConstants.ModulePowerData_SharedPreferences, Context.MODE_PRIVATE);
				Editor editor = sp.edit();
				String s = mp.getJsonString();
				editor.putString("ModulePower", s);
				
				editor.commit();
			}
			
			for (int i = 0; i < jsonArray.length(); i++) {
				
				JSONObject shipObject = jsonArray.getJSONObject(i);
				String str = shipObject.getString("Data");
				String[] arr = str.split(",");
				for(int j=0;j<arr.length;j++){
					UserPowerShipData upsd = userPowerShipDataMap.get(arr[0]);
					if(upsd == null){
						upsd = new UserPowerShipData();
						userPowerShipDataMap.put(arr[0], upsd);
						
						upsd.setShipID(arr[0]);
						upsd.setShipName(arr[1]);
					}
					
					ShipModelData smd = shipModelDataMap.get(arr[0]+arr[2]);
					if(smd == null){
						smd = new ShipModelData();
						shipModelDataMap.put(arr[0]+arr[2], smd);
						
						smd.setShipID(arr[0]);
						smd.setShipName(arr[1]);
						smd.setModel(ShipModelCode.values()[Integer.valueOf(arr[2])]);
						
						upsd.getShipModels().add(smd);
					}
					
					ShipModelNoData smnd = shipModelNoDataMap.get(arr[0] +arr[2] +arr[3]);
					if(smnd == null){
						smnd = new ShipModelNoData();
						shipModelNoDataMap.put(arr[0]+arr[2]+arr[3], smnd);
						
						smnd.setShipID(arr[0]);
						smnd.setShipName(arr[1]);
						smnd.setModelNo(arr[3]);
						smnd.setModelPowerStartTime(arr[4]);
						smnd.setModelPowerEndTime(arr[5]);
						
						smd.getModelNolist().add(smnd);
					}
				}
				
			}
			Iterator it = userPowerShipDataMap.keySet().iterator();   
			while(it.hasNext()){   
				String key=it.next().toString(); 
				
				UserPowerShipData ups = userPowerShipDataMap.get(key);
				
				entity.getUserPowerShipDatas().add(ups);
			}   
		} catch (Exception e) {
			return null;
		}
		return entity;
	}
	
	public static List<ShipGpsData> getHistoryGps(String result)
			throws Exception {
		List<ShipGpsData> gpsDatas = new ArrayList<ShipGpsData>();
		try {
			if (result == null || "".equals(result))
				return gpsDatas;

			// SharedPreferences sp =
			// GlobalApplication.getInstance().getSharedPreferences("UserPowerData",
			// Context.MODE_PRIVATE);
			// String object = sp.getString("UserPower", "");
			//
			// UserPowerData data = null;
			// if(!"".equals(object)){
			// Gson gson = new Gson();
			// data = gson.fromJson(object, new TypeToken<UserPowerData>()
			// {}.getType());
			// }else
			// data = new UserPowerData();

			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("Data");

			for (int i = 0; i <jsonArray.length(); i++) {
//			for (int i = jsonArray.length()-1; i >=0; i--) {
				ShipGpsData shipGpsData = new ShipGpsData();

				JSONObject shipObject = jsonArray.getJSONObject(i);
				String ShipID = shipObject.getString("ShipID");
				shipGpsData.setShipID(ShipID);

				// List<String> channels = new ArrayList<String>();
				// flag:
				// for(UserPowerShipData upsd:data.getUserPowerShipDatas()){
				// if(upsd.getShipID().equals(ShipID)){
				// for(ShipModelData smd:upsd.getShipModels()){
				// if(smd.getModel() == ShipModelCode.five){
				// for(ShipModelNoData smnd:smd.getModelNolist()){
				// channels.add(smnd.getModelNo());
				// }
				// break flag;
				// }else
				// continue;
				// }
				// }else
				// continue;
				// }
				// shipGpsData.setChannels(channels);

				if (shipObject.has("GPS")) {
					JSONObject gPSObject = shipObject.getJSONObject("GPS");
					shipGpsData.setGpsTime(gPSObject.getString("GPSTime"));
					shipGpsData.setGpsLongitude(Double.valueOf(gPSObject
							.getString("GPSLongitude")));
					shipGpsData.setGpsLatitude(Double.valueOf(gPSObject
							.getString("GPSLatitude")));
					shipGpsData.setGpsSpeed(Double.valueOf(gPSObject
							.getString("GPSSpeed")));
					shipGpsData.setGpsCourse(Double.valueOf(gPSObject
							.getString("GPSCourse")));
					shipGpsData.setBdgpsLatitude(Double.valueOf(gPSObject
							.getString("BDLatitude")));
					shipGpsData.setBdgpsLongitude(Double.valueOf(gPSObject
							.getString("BDLongitude")));
				}
				if (shipObject.has("PowerManagement")) {
					JSONObject powerManagementObject = shipObject
							.getJSONObject("PowerManagement");
					shipGpsData.setMainVoltage(Double
							.valueOf(powerManagementObject
									.getString("MainVoltage")));
					shipGpsData.setBackUpVoltage(Double
							.valueOf(powerManagementObject
									.getString("BackUpVoltage")));
					shipGpsData.setTemperature(Double
							.valueOf(powerManagementObject
									.getString("Temperature")));
					shipGpsData.setPowerManagementState(Double
							.valueOf(powerManagementObject.getString("State")));
				}
				if((0 == shipGpsData.getBdgpsLatitude()||0 == shipGpsData.getBdgpsLongitude())
						&&(0 == shipGpsData.getGpsLatitude()||0 == shipGpsData.getGpsLongitude()))
					continue;
				
				gpsDatas.add(shipGpsData);
				GpsDataQueue.getInstance().offer(shipGpsData);
			}
			return gpsDatas;
		} catch (Exception e) {
			throw new Exception("数据解析出错！");
		}
	}
	/*收藏船舶的格式：
	[{"ShipID":4147,"ShipName":"\u4e30\u6e903368","Focus":"1"},{"ShipID":4225,"ShipName":"\u6a2a\u53bf\u5e73\u798f899","Focus":"0"},{"ShipID":4365,"ShipName":"\u7ca4\u5e7f\u5dde\u8d270522","Focus":"0"},{"ShipID":4633,"ShipName":"\u7ca4\u660e\u8fbe08","Focus":"0"},{"ShipID":4423,"ShipName":"\u4e1c\u8fd0899","Focus":"0"},{"ShipID":4226,"ShipName":"\u4e1c\u8fd0768","Focus":"0"},{"ShipID":4422,"ShipName":"\u7ca4\u60e0\u5dde\u8d279663","Focus":"0"},{"ShipID":4184,"ShipName":"\u6842\u6842\u5e73\u8d272368","Focus":"0"},{"ShipID":4505,"ShipName":"\u987a\u94e7333","Focus":"0"},{"ShipID":4377,"ShipName":"\u7ca4\u5e7f\u5dde\u8d270568","Focus":"0"},{"ShipID":4211,"ShipName":"\u6842\u6842\u5e73\u8d276678","Focus":"0"},{"ShipID":4504,"ShipName":"\u987a\u94e7666","Focus":"0"},{"ShipID":4391,"ShipName":"\u9e3f\u5bcc366","Focus":"0"},{"ShipID":4468,"ShipName":"\u6842\u6842\u5e73\u8d27268","Focus":"0"},{"ShipID":4396,"ShipName":"\u7ca4\u5b89\u987a621","Focus":"0"},{"ShipID":4641,"ShipName":"\u4e9a\u8bfa6609","Focus":"0"},{"ShipID":4631,"ShipName":"\u7ca4\u5e7f\u5dde\u8d272188\u8239","Focus":"0"},{"ShipID":4499,"ShipName":"\u6842\u817e\u53bf\u8d270488","Focus":"0"},{"ShipID":4480,"ShipName":"\u5e73\u5357\u6c38\u4f73338","Focus":"0"},{"ShipID":4174,"ShipName":"\u6842\u5e73\u98de\u8fbe0829","Focus":"0"},{"ShipID":4209,"ShipName":"\u987a\u94e7189","Focus":"0"},{"ShipID":4399,"ShipName":"\u7ca4\u5efa\u822a128","Focus":"0"},{"ShipID":4394,"ShipName":"\u4e1c\u8fd0188","Focus":"0"},{"ShipID":4612,"ShipName":"\u51ef\u7fd4999","Focus":"0"},{"ShipID":4375,"ShipName":"\u987a\u5b8f\u6d77228","Focus":"0"},{"ShipID":4388,"ShipName":"\u589e\u9a8f\u822a808","Focus":"0"},{"ShipID":4379,"ShipName":"\u7ca4\u5efa\u822a0006","Focus":"0"},{"ShipID":4181,"ShipName":"\u589e\u9a70\u822a136","Focus":"0"},{"ShipID":4384,"ShipName":"\u7ca4\u5e7f\u5dde\u8d271898","Focus":"0"}]
	解释：关注粤明达08，不关注东运768*/
	public static List<ShipSCData> parseShipSC(String arg2) throws Exception {
		List<ShipSCData> list = new ArrayList<ShipSCData>();
		if(arg2!=null&&!"".equals(arg2)){
			JSONArray jsonArray = new JSONArray(arg2);
			for (int i = 0; i <jsonArray.length(); i++) {
				JSONObject shipObject = jsonArray.getJSONObject(i);
				ShipSCData s = new ShipSCData();
				
				s.setShipID(Util.IntToHex(Integer.valueOf(shipObject.getString("ShipID"))));
				s.setShipName(shipObject.getString("ShipName"));
				s.setFocus(shipObject.getString("Focus"));
				
				list.add(s);
			}
			// 排序
//			Collections.sort(list, new Comparator<ShipSCData>() {
//
//				@Override
//				public int compare(ShipSCData lhs, ShipSCData rhs) {
//					return Integer.valueOf(rhs.getFocus()).compareTo(Integer.valueOf(lhs.getFocus()));
//				}
//			});
		}
		
		return list;
	}
	
	/*  "ShipID": "0x112A",
    "ShipName": "东运188",
    "Length": "49.9",
    "Breadth": "12.8",
    "Draught": "4.2",
    "ClassA": "1700",
    "ClassB": "2100",
    "DriveStyle": "后置驾驶",
    "ShipType": "自卸船",
    "ShipOwner": "横县平福船务",
    "Contact": "黎永桃",
    "Tel": "12345678901",
    "IMO": "0",
    "MMSI": "0"*/

	public static ShipInfoData parserShipInfo(String arg2) {
		ShipInfoData s = new ShipInfoData();
		if(arg2!=null&&!"".equals(arg2)){
			try {
				JSONObject shipObject = new JSONObject(arg2);
				
				s.setShipID(shipObject.getString("ShipID"));
				s.setShipName(shipObject.getString("ShipName"));
				s.setLength("null".equals(shipObject.getString("Length"))?"0":shipObject.getString("Length"));
				s.setBreadth("null".equals(shipObject.getString("Breadth"))?"0":shipObject.getString("Breadth"));
				s.setDraught("null".equals(shipObject.getString("Draught"))?"0":shipObject.getString("Draught"));
				s.setClassA("null".equals(shipObject.getString("ClassA"))?"0":shipObject.getString("ClassA"));
				s.setClassB("null".equals(shipObject.getString("ClassB"))?"0":shipObject.getString("ClassB"));
				s.setDriveStyle("null".equals(shipObject.getString("DriveStyle"))?"":shipObject.getString("DriveStyle"));
				s.setShipType("null".equals(shipObject.getString("ShipType"))?"":shipObject.getString("ShipType"));
				s.setShipOwner("null".equals(shipObject.getString("ShipOwner"))?"":shipObject.getString("ShipOwner"));
				s.setContact("null".equals(shipObject.getString("Contact"))?"":shipObject.getString("Contact"));
				s.setTel("null".equals(shipObject.getString("Tel"))?"":shipObject.getString("Tel"));
				s.setIMO("null".equals(shipObject.getString("IMO"))?"":shipObject.getString("IMO"));
				s.setMMSI("null".equals(shipObject.getString("MMSI"))?"":shipObject.getString("MMSI"));
				
			} catch (JSONException e) {
			}
		}
		
		return s;
	}
	
	/*[{"VorageNumber":"V2016081801","ShipName":"\u7ca4\u5efa\u822a128","ShipID":"112F","Start":"\u73e0\u7535\u5357\u6c99\u7801\u5934","End":"\u5e7f\u7535\u7801\u5934","EventType":"9","EventValue":"0","Weather":"\u5e7f\u5dde\u5e02,\u5927\u96e8\u8f6c\u96f7\u9635\u96e8,\u4e1c\u5357\u98ce4-5\u7ea7,30 ~ 25\u2103","EventTime":"2016-08-18 19:08:19"},{"VorageNumber":"V2016081603","ShipName":"\u7ca4\u5b89\u987a621","ShipID":"112C","Start":"\u73e0\u7535\u5357\u6c99\u7801\u5934","End":"\u5e7f\u7535\u7801\u5934","EventType":"12","EventValue":"0","Weather":"\ufeff\u5e7f\u5dde\u5e02,\u5927\u5230\u66b4\u96e8\u8f6c\u5c0f\u5230\u4e2d\u96e8,\u4e1c\u98ce4-5\u7ea7,29 ~ 25\u2103","EventTime":"2016-08-18 05:29:23"},{"VorageNumber":"V2016092801","ShipName":"\u6842\u6842\u5e73\u8d272368","ShipID":"1058","Start":"\u795e\u534e\u9ad8\u680f\u6e2f\u7801\u5934","End":"\u6052\u76ca\u7535\u5382\u7801\u5934","EventType":"9","EventValue":"0","Weather":"\u4f5b\u5c71\u5e02,\u6674\u8f6c\u591a\u4e91,\u5fae\u98ce,35 ~ 27\u2103","EventTime":"2016-09-23 02:39:00"},{"VorageNumber":"V2016092801","ShipName":"\u6842\u6842\u5e73\u8d272368","ShipID":"1058","Start":"\u795e\u534e\u9ad8\u680f\u6e2f\u7801\u5934","End":"\u6052\u76ca\u7535\u5382\u7801\u5934","EventType":"12","EventValue":"0","Weather":"\u4f5b\u5c71\u5e02,\u6674\u8f6c\u591a\u4e91,\u5fae\u98ce,35 ~ 27\u2103","EventTime":"2016-09-23 02:39:00"},{"VorageNumber":"V2016092801","ShipName":"\u6842\u6842\u5e73\u8d272368","ShipID":"1058","Start":"\u795e\u534e\u9ad8\u680f\u6e2f\u7801\u5934","End":"\u6052\u76ca\u7535\u5382\u7801\u5934","EventType":"10","EventValue":"0","Weather":"\u4f5b\u5c71\u5e02,\u6674\u8f6c\u591a\u4e91,\u5fae\u98ce,35 ~ 27\u2103","EventTime":"2016-09-23 02:57:00"},{"VorageNumber":"V2016092801","ShipName":"\u5929\u80fd8\u53f7","ShipID":"111A","Start":"\u65b0\u6c99\u6e2f\u7801\u5934","End":"\u5e7f\u7535\u7801\u5934","EventType":"9","EventValue":"0","Weather":"\u5e7f\u5dde\u5e02,\u9635\u96e8,\u5fae\u98ce,28 ~ 21\u2103","EventTime":"2016-09-28 11:21:56"},{"VorageNumber":"V2016100801","ShipName":"\u6842\u6842\u5e73\u8d276678","ShipID":"1073","Start":"\u5e7f\u7535\u7801\u5934","End":"\u897f\u57fa\u7801\u5934","EventType":"9","EventValue":"0","Weather":"\u4f5b\u5c71\u5e02,\u591a\u4e91,\u5fae\u98ce,31 ~ 24\u2103","EventTime":"2016-10-08 14:08:02"},{"VorageNumber":"V2016101001","ShipName":"\u4e1c\u8fd0188","ShipID":"112A","Start":"\u65fa\u9686\u7535\u5382\u7801\u5934","End":"\u6052\u76ca\u7535\u5382\u7801\u5934","EventType":"9","EventValue":"0","Weather":"\u5e7f\u5dde\u5e02,\u591a\u4e91\u8f6c\u9635\u96e8,\u5317\u98ce3-4\u7ea7,30 ~ 22\u2103","EventTime":"2016-10-10 15:12:31"},{"VorageNumber":"V2016101001","ShipName":"\u5e73\u798f389","ShipID":"1083","Start":"\u795e\u534e\u9ad8\u680f\u6e2f\u7801\u5934","End":"\u6052\u76ca\u7535\u5382\u7801\u5934","EventType":"9","EventValue":"0","Weather":"\u5e7f\u5dde\u5e02,\u591a\u4e91\u8f6c\u9635\u96e8,\u5317\u98ce3-4\u7ea7,30 ~ 22\u2103","EventTime":"2016-10-10 15:45:50"},{"VorageNumber":"V2016101101","ShipName":"\u6842\u5e73\u98de\u8fbe630","ShipID":"1048","Start":"\u795e\u534e\u9ad8\u680f\u6e2f\u7801\u5934","End":"\u6c38\u6cf0\u7801\u5934\uff1f","EventType":"9","EventValue":"0","Weather":"\u4e2d\u5c71\u5e02,\u5c0f\u96e8,\u5fae\u98ce,28 ~ 22\u2103","EventTime":"2016-10-11 09:16:07"},{"VorageNumber":"V2016101101","ShipName":"\u7ca4\u5efa\u822a0006","ShipID":"111B","Start":"\u73e0\u7535\u5357\u6c99\u7801\u5934","End":"\u65fa\u9686\u7535\u5382\u7801\u5934","EventType":"9","EventValue":"0","Weather":"\u5e7f\u5dde\u5e02,\u5c0f\u96e8,\u5fae\u98ce,28 ~ 21\u2103","EventTime":"2016-10-11 10:37:16"}]*/
	public static List<PoliceData> parserPolice(String arg2) throws Exception{
		List<PoliceData> list = new ArrayList<PoliceData>();
		try {
			JSONArray array = new JSONArray(arg2);
			for(int i=0;i<array.length();i++){
				JSONObject o = array.getJSONObject(i);
				PoliceData p = new PoliceData();
				p.setVorageNumber(o.getString("VorageNumber"));
				p.setShipName(Util.decodeUnicode(o.getString("ShipName")));
				p.setShipID(o.getString("ShipID"));
				p.setStart(o.getString("Start"));
				p.setEnd(o.getString("End"));
				p.setEventType(o.getString("EventType"));
				p.setEventValue(o.getString("EventValue"));
//				p.setWeather(Util.decodeUnicode(o.getString("Weather")));
				p.setEventTime(o.getString("EventTime"));
				p.setnID(o.getString("nID"));
				p.setbRead(o.getBoolean("bRead"));
				
				list.add(p);
			}
		} catch (Exception e) {
			throw new Exception("解析出错");
		}
		return list;
	}

	public static List<ShipOneCKHcData> getCKHCGps(String s) throws Exception{
		List<ShipOneCKHcData> list = new ArrayList<ShipOneCKHcData>();
		
//		String s = Util.readFileData(start+end);
		
		JSONArray array = new JSONArray(s);
		for(int i=0;i<array.length();i++){
			JSONObject oneObject = array.getJSONObject(i);
			
			ShipOneCKHcData sod = new ShipOneCKHcData();
			sod.setRouteDistance(oneObject.getString("RouteDistance"));
			sod.setRouteTime(oneObject.getString("RouteTime"));
			sod.setRoutePlayRemain(oneObject.getString("RoutePlayRemain"));
			
			JSONArray oneLine = oneObject.getJSONArray("ShipLine");
			List<ShipCKGpssData> list1 = new ArrayList<ShipCKGpssData>();
			for(int j=0;j<oneLine.length();j++){
				
				JSONObject o = oneLine.getJSONObject(j);
				ShipCKGpssData p = new ShipCKGpssData();
				p.setSN(o.getString("SN"));
				p.setLatitude(o.getString("Latitude"));
				p.setLongitude(o.getString("Longitude"));
				
				list1.add(p);
			}
			sod.setList(list1);
			list.add(sod);
		}
		
		return list;
		
		
//		JSONArray array = new JSONArray(s);
//		for(int i=0;i<array.length();i++){
//			JSONArray one = array.getJSONArray(i);
//			
//			ShipOneCKHcData sod = new ShipOneCKHcData();
//			List<ShipCKGpssData> list1 = new ArrayList<ShipCKGpssData>();
//			for(int j=0;j<one.length();j++){
//				
//				JSONObject o = one.getJSONObject(j);
//				ShipCKGpssData p = new ShipCKGpssData();
//				p.setSN(o.getString("SN"));
//				p.setLatitude(o.getString("Latitude"));
//				p.setLongitude(o.getString("Longitude"));
//				
//				list1.add(p);
//			}
//			sod.setList(list1);
//			list.add(sod);
//		}
//		return list;
	}
	/*[{"nID":"1","0":"1","Port_name":"\u73e0\u7535\u5357\u6c99\u7801\u5934","1":"\u73e0\u7535\u5357\u6c99\u7801\u5934","x":"113.576051","2":"113.576051","y":"22.814783","3":"22.814783","Latitude":"22.814783","4":"22.814783","Longitude":"113.576051","5":"113.576051","Latitude1":"22.829783","6":"22.829783","Longitude1":"113.596051","7":"113.596051","Create_auther":"yty","8":"yty","Create_time":"2016-07-14 14:19:23","9":"2016-07-14 14:19:23","Modify_time":"2016-07-14 14:19:23","10":"2016-07-14 14:19:23"},{"nID":"2","0":"2","Port_name":"\u65fa\u9686\u7535\u5382\u7801\u5934","1":"\u65fa\u9686\u7535\u5382\u7801\u5934","x":"113.566960","2":"113.566960","y":"23.084009","3":"23.084009","Latitude":"23.084009","4":"23.084009","Longitude":"113.566960","5":"113.566960","Latitude1":"23.094909","6":"23.094909","Longitude1":"113.586960","7":"113.586960","Create_auther":"yty","8":"yty","Create_time":"2016-07-14 14:20:05","9":"2016-07-14 14:20:05","Modify_time":"2016-07-14 14:20:05","10":"2016-07-14 14:20:05"},{"nID":"3","0":"3","Port_name":"\u795e\u534e\u9ad8\u680f\u6e2f\u7801\u5934","1":"\u795e\u534e\u9ad8\u680f\u6e2f\u7801\u5934","x":"113.202951","2":"113.202951","y":"21.946770","3":"21.946770","Latitude":"21.946770","4":"21.946770","Longitude":"113.202951","5":"113.202951","Latitude1":"21.973430","6":"21.973430","Longitude1":"113.243795","7":"113.243795","Create_auther":"yty","8":"yty","Create_time":"2016-07-28 12:05:07","9":"2016-07-28 12:05:07","Modify_time":"2016-07-28 12:05:07","10":"2016-07-28 12:05:07"},{"nID":"4","0":"4","Port_name":"\u65b0\u6c99\u6e2f\u7801\u5934","1":"\u65b0\u6c99\u6e2f\u7801\u5934","x":"113.528841","2":"113.528841","y":"23.014079","3":"23.014079","Latitude":"23.014079","4":"23.014079","Longitude":"113.528841","5":"113.528841","Latitude1":"23.034079","6":"23.034079","Longitude1":"113.538841","7":"113.538841","Create_auther":"yty","8":"yty","Create_time":"2016-07-28 14:36:09","9":"2016-07-28 14:36:09","Modify_time":"2016-07-28 14:36:09","10":"2016-07-28 14:36:09"},{"nID":"5","0":"5","Port_name":"\u897f\u57fa\u7801\u5934","1":"\u897f\u57fa\u7801\u5934","x":"113.510000","2":"113.510000","y":"23.057057","3":"23.057057","Latitude":"23.057057","4":"23.057057","Longitude":"113.510000","5":"113.510000","Latitude1":"23.067057","6":"23.067057","Longitude1":"113.520552","7":"113.520552","Create_auther":"yty","8":"yty","Create_time":"2016-07-28 14:38:44","9":"2016-07-28 14:38:44","Modify_time":"2016-07-28 14:38:44","10":"2016-07-28 14:38:44"},{"nID":"6","0":"6","Port_name":"\u6d77\u660c\u7801\u5934","1":"\u6d77\u660c\u7801\u5934","x":"113.542172","2":"113.542172","y":"22.997424","3":"22.997424","Latitude":"22.997424","4":"22.997424","Longitude":"113.542172","5":"113.542172","Latitude1":"23.007424","6":"23.007424","Longitude1":"113.552172","7":"113.552172","Create_auther":"yty","8":"yty","Create_time":"2016-07-28 14:39:57","9":"2016-07-28 14:39:57","Modify_time":"2016-07-28 14:39:57","10":"2016-07-28 14:39:57"},{"nID":"7","0":"7","Port_name":"\u5e7f\u7535\u7801\u5934","1":"\u5e7f\u7535\u7801\u5934","x":"113.230418","2":"113.230418","y":"23.140146","3":"23.140146","Latitude":"23.140146","4":"23.140146","Longitude":"113.230418","5":"113.230418","Latitude1":"23.148146","6":"23.148146","Longitude1":"113.238418","7":"113.238418","Create_auther":"yty","8":"yty","Create_time":"2016-07-28 14:45:20","9":"2016-07-28 14:45:20","Modify_time":"2016-07-28 14:45:20","10":"2016-07-28 14:45:20"},{"nID":"8","0":"8","Port_name":"\u6052\u76ca\u7535\u5382\u7801\u5934","1":"\u6052\u76ca\u7535\u5382\u7801\u5934","x":"112.834733","2":"112.834733","y":"22.998185","3":"22.998185","Latitude":"22.998185","4":"22.998185","Longitude":"112.834733","5":"112.834733","Latitude1":"23.028185","6":"23.028185","Longitude1":"112.854733","7":"112.854733","Create_auther":"yty","8":"yty","Create_time":"2016-07-28 14:53:07","9":"2016-07-28 14:53:07","Modify_time":"2016-07-28 14:53:07","10":"2016-07-28 14:53:07"},{"nID":"18","0":"18","Port_name":"\u5927\u76db","1":"\u5927\u76db","x":"113.538343","2":"113.538343","y":"23.065014","3":"23.065014","Latitude":"23.065014","4":"23.065014","Longitude":"113.538343","5":"113.538343","Latitude1":"23.052777","6":"23.052777","Longitude1":"113.546391","7":"113.546391","Create_auther":"superadmin","8":"superadmin","Create_time":"2016-08-23 15:02:48","9":"2016-08-23 15:02:48","Modify_time":"2016-08-23 15:02:48","10":"2016-08-23 15:02:48"},{"nID":"20","0":"20","Port_name":"\u6c38\u6cf0\u7801\u5934\uff1f","1":"\u6c38\u6cf0\u7801\u5934\uff1f","x":"112.871512","2":"112.871512","y":"23.303339","3":"23.303339","Latitude":"23.303339","4":"23.303339","Longitude":"112.871512","5":"112.871512","Latitude1":"23.300152","6":"23.300152","Longitude1":"112.872231","7":"112.872231","Create_auther":"superadmin","8":"superadmin","Create_time":"2016-10-08 15:09:50","9":"2016-10-08 15:09:50","Modify_time":"2016-10-08 15:09:50","10":"2016-10-08 15:09:50"},{"nID":"21","0":"21","Port_name":"\u6d2a\u6885\u7406\u6587\u9020\u7eb8\u5382\u7801\u5934","1":"\u6d2a\u6885\u7406\u6587\u9020\u7eb8\u5382\u7801\u5934","x":"113.604027","2":"113.604027","y":"22.936605","3":"22.936605","Latitude":"22.936605","4":"22.936605","Longitude":"113.604027","5":"113.604027","Latitude1":"22.936904","6":"22.936904","Longitude1":"113.604242","7":"113.604242","Create_auther":"superadmin","8":"superadmin","Create_time":"2016-10-08 15:58:15","9":"2016-10-08 15:58:15","Modify_time":"2016-10-08 15:58:15","10":"2016-10-08 15:58:15"},{"nID":"22","0":"22","Port_name":"\u4e2d\u5c71\u7535\u5382","1":"\u4e2d\u5c71\u7535\u5382","x":"113.337984","2":"113.337984","y":"22.695223","3":"22.695223","Latitude":"22.695223","4":"22.695223","Longitude":"113.337984","5":"113.337984","Latitude1":"22.695256","6":"22.695256","Longitude1":"113.337949","7":"113.337949","Create_auther":"superadmin","8":"superadmin","Create_time":"2016-10-08 16:34:54","9":"2016-10-08 16:34:54","Modify_time":"2016-10-08 16:34:54","10":"2016-10-08 16:34:54"},{"nID":"23","0":"23","Port_name":"\u5e7f\u5dde\u7eb8\u5382","1":"\u5e7f\u5dde\u7eb8\u5382","x":"113.518292","2":"113.518292","y":"22.748760","3":"22.748760","Latitude":"22.748760","4":"22.748760","Longitude":"113.518292","5":"113.518292","Latitude1":"22.748760","6":"22.748760","Longitude1":"113.518149","7":"113.518149","Create_auther":"superadmin","8":"superadmin","Create_time":"2016-10-08 16:48:05","9":"2016-10-08 16:48:05","Modify_time":"2016-10-08 16:48:05","10":"2016-10-08 16:48:05"},{"nID":"24","0":"24","Port_name":"\u4e2d\u5c71\u8054\u5408\u9e3f\u5174\u9020\u7eb8\u6709\u9650\u516c\u53f8","1":"\u4e2d\u5c71\u8054\u5408\u9e3f\u5174\u9020\u7eb8\u6709\u9650\u516c\u53f8","x":"113.332810","2":"113.332810","y":"22.490079","3":"22.490079","Latitude":"22.490079","4":"22.490079","Longitude":"113.332810","5":"113.332810","Latitude1":"22.490113","6":"22.490113","Longitude1":"113.333205","7":"113.333205","Create_auther":"superadmin","8":"superadmin","Create_time":"2016-10-08 16:54:45","9":"2016-10-08 16:54:45","Modify_time":"2016-10-08 16:54:45","10":"2016-10-08 16:54:45"}]*/
	public static List<MapPortData> parserMapPort(String s) {
		List<MapPortData> list = new ArrayList<MapPortData>();
		try {
			JSONArray array = new JSONArray(s);
			for(int i=0;i<array.length();i++){
				JSONObject o = array.getJSONObject(i);
				MapPortData md = new MapPortData();
				md.setPort_name(Util.decodeUnicode(o.getString("Port_name")));
				md.setX(o.getString("x"));
				md.setY(o.getString("y"));
				
				list.add(md);
			}
		} catch (Exception e) {
			return null;
		}
		return list;
	}
	
/**{"ShipID":4396,"ShipName":"\u7ca4\u5b89\u987a621","Voyages":
 * [{"Voyage":{"VoyageNum":"V20161101","StartPort":"\u897f\u57fa\u7801\u5934","EndPort":"\u5927\u76db\u951a\u5730","Voyage":
 * [{"nID":"0","Stage":"0","Value":"0","String":"\u5e7f\u5dde\u5e02,\u591a\u4e91,\u5317\u98ce3-4\u7ea7,25 ~ 16\u2103","opTime":"2016-11-02 11:14:44"},
 * {"nID":"0","Stage":"9","Value":"0","String":"\u5e7f\u5dde\u5e02,\u591a\u4e91,\u5317\u98ce3-4\u7ea7,25 ~ 16\u2103","opTime":"2016-11-02 11:15:23"}]}}]}
 * @throws Exception */
	public static ShipVoyageData parserHangchi(String result) throws Exception {
		ShipVoyageData shipVoyageData = new ShipVoyageData();
		if (result != null && !"".equals(result)) {
			JSONObject obj = new JSONObject(result);
			
			String shipId = "0x" + Integer.toHexString(Integer.parseInt(obj.getString("ShipID"))).toUpperCase();
			shipVoyageData.setShipID(shipId);
			String shipName = Util.decodeUnicode(obj.getString("ShipName"));
			shipVoyageData.setShipName(shipName);

			List<ShipVoyageNodeData> nodeDatas = new ArrayList<ShipVoyageNodeData>();
			
			JSONArray jsonArray1 = obj.getJSONArray("Voyages");
			for (int j = 0; j < jsonArray1.length(); j++) {
				JSONObject o = jsonArray1.getJSONObject(j);
				JSONObject obj1 = o.getJSONObject("Voyage");
				shipVoyageData.setVoyageNum(obj1.getString("VoyageNum"));
				shipVoyageData.setStartPort(obj1.getString("StartPort"));
				shipVoyageData.setEndPort(obj1.getString("EndPort"));
				JSONArray jsonArray2 = obj1.getJSONArray("Voyage");
				for (int i = 0; i < jsonArray2.length(); i++) {
					JSONObject obj2 = jsonArray2.getJSONObject(i);
					ShipVoyageNodeData svnd = new ShipVoyageNodeData();
					svnd.setShipID(shipId);
					svnd.setShipName(shipName);
					svnd.setNid(obj2.getString("nID"));
					svnd.setStage(obj2.getString("Stage"));
					svnd.setValue(obj2.getString("Value"));
					svnd.setOpTime(obj2.getString("opTime"));
					svnd.setNodeCode(NodeCode.getByN(Integer.parseInt(svnd.getStage())));
					
					svnd.setWeather(decodeWeather(obj2.getString("String")));
					
					nodeDatas.add(svnd);
				}
			}
			// 排序
			Collections.sort(nodeDatas, new Comparator<ShipVoyageNodeData>() {

				@Override
				public int compare(ShipVoyageNodeData lhs, ShipVoyageNodeData rhs) {
					return CalendarUtil.parseYYYY_MM_DD_HH_MM_SS(lhs.getOpTime()).compareTo(
							CalendarUtil.parseYYYY_MM_DD_HH_MM_SS(rhs.getOpTime()));
				}
			});
			shipVoyageData.setNodes(nodeDatas);
		}
		return shipVoyageData;
	}
	private static BaiduWeatherData decodeWeather(String json){
		try {
			if(null != json&&(!"".equals(json))){
				String stringStr = Util.decodeUnicode(json);
				JSONObject objs = new JSONObject(stringStr);
				
				BaiduWeatherData b = new BaiduWeatherData();
				b.setaPI(objs.getString("API"));
				
				JSONObject weartherObj = objs.getJSONObject("WeatherStr");
				b.setCity(weartherObj.getString("City"));
				b.setWeather(weartherObj.getString("Weather"));
				b.setWind(weartherObj.getString("Wind"));
				b.setTemperature(weartherObj.getString("Temperature"));
				
				return b;
			}
		} catch (Exception e) {
		}
		return null;
	}
	public static StaEndTimeImgData decodeStaEndTimeImgData(String json){
		try {
			if(null != json&&(!"".equals(json))){
				JSONObject objs = new JSONObject(json);
				
				StaEndTimeImgData b = new StaEndTimeImgData();
				b.setShipID(objs.getString("ShipID"));
				b.setChannel(objs.getString("Channel"));
				b.setStartTime(objs.getString("StartTime"));
				b.setEndTime(objs.getString("EndTime"));
				
				JSONArray jsonArray2 = objs.getJSONArray("Picture");
				List<String> Picture = new ArrayList<String>();
				for(int i=0;i<jsonArray2.length();i++){
					Picture.add(jsonArray2.getString(i));
				}
				b.setPicture(Picture);
				
				JSONArray jsonArray3 = objs.getJSONArray("Content");
				List<TimeSrcData> Content = new ArrayList<TimeSrcData>();
				for(int i=0;i<jsonArray3.length();i++){
					TimeSrcData t = new TimeSrcData();
					JSONObject obj3 = jsonArray3.getJSONObject(i);
					
					t.setTime(obj3.getString("Time"));
					int index = obj3.getInt("Src");        //Src>=0是Picture数组的下标，Src=-1是无图片
					if(index != -1)
						t.setSrc(Picture.get(index));
					
					Content.add(t);
				}
				b.setContent(Content);
				
				return b;
			}
		} catch (Exception e) {
		}
		return null;
	}

	public static List<PositionData> getPortArea(String result) {
		List<PositionData> positionDatas = new ArrayList<PositionData>();
		try {
			if (result != null && !"".equals(result)) {
				Gson gson = new Gson();
				List<Map<String, String>> maps = gson.fromJson(result, new TypeToken<List<Map<String, String>>>() {
				}.getType());

				for (Map<String, String> map : maps) {
					PositionData positionData = new PositionData();
					positionData.setLongitude(Double.parseDouble((String) map.get("x")));
					positionData.setLatitude(Double.parseDouble((String) map.get("y")));
					
					positionData.setPalng1(Double.parseDouble((String) map.get("Longitude")));
					positionData.setPalat1(Double.parseDouble((String) map.get("Latitude")));
					
					positionData.setPalng2(Double.parseDouble((String) map.get("Longitude1")));
					positionData.setPalat2(Double.parseDouble((String) map.get("Latitude1")));
					
					positionData.setPortName((String) map.get("Port_name"));
					positionDatas.add(positionData);
				}
			}
		} catch (Exception e) {
		}
		return positionDatas;
	}
	
	/**{"Data":{"State":1,"ShipID":"0x1136","GPS":{"GPSTime":"2016-12-11 17:31:29","GPSLongitude":113.53759,"GPSLatitude":23.062355,"GPSSpeed":0,"GPSCourse":0,"BDLongitude":"113.54925917307","BDLatitude":"23.06567305102"},"PowerManagement":{"MainVoltage":15.947,"BackUpVoltage":13.03,"Temperature":38,"State":0}}}*/
	public static ShipCooordData parserSTPosition(String arg2) {
		try {
			JSONObject jsonObject = new JSONObject(arg2);
			JSONObject dataObject = jsonObject.getJSONObject("Data");
			
			ShipCooordData scd = new ShipCooordData();
			scd.setShipID(dataObject.getString("ShipID"));
			
			JSONObject gpsObject = dataObject.getJSONObject("GPS");
			scd.setGpsTime(gpsObject.getString("GPSTime"));
			scd.setGpsLongitude(gpsObject.getString("GPSLongitude"));
			scd.setGpsLatitude(gpsObject.getString("GPSLatitude"));
			scd.setGpsCourse(gpsObject.getString("GPSCourse"));
			scd.setGpsSpeed(gpsObject.getString("GPSSpeed"));
			scd.setBdLongitude(gpsObject.getString("BDLongitude"));
			scd.setBdLatitude(gpsObject.getString("BDLatitude"));
			
			JSONObject pmObject = dataObject.getJSONObject("PowerManagement");
			scd.setBackUpVoltage(pmObject.getString("BackUpVoltage"));
			scd.setTemperature(pmObject.getString("Temperature"));
			scd.setMainVoltage(pmObject.getString("MainVoltage"));
			scd.setPowerManagementState(pmObject.getString("State"));
			
			return scd;
			
		} catch (Exception e) {
		}
		return null;
	}
	/**{"TotalDistance":4.7573764444444,"TotalSeconds":2689,"Voyage":[{"Distance":3.3195042222222,"Seconds":1683,"StartTime":"2016-11-11 20:11:40","StartGPSLongitude":113.565088,"StartGPSLatitude":23.088715,"EndTime":"2016-11-11 20:40:40","EndGPSLongitude":113.541753,"EndGPSLatitude":23.071252},{"Distance":1.4378722222222,"Seconds":1006,"StartTime":"2016-11-11 21:01:40","StartGPSLongitude":113.541573,"StartGPSLatitude":23.070622,"EndTime":"2016-11-11 21:19:37","EndGPSLongitude":113.534052,"EndGPSLatitude":23.058963}]}*/
	public static String parseSSDistance(String arg2) {
		try {
			JSONObject jsonObject = new JSONObject(arg2);
			String ss = jsonObject.getString("TotalDistance");
			BigDecimal   b   =   new   BigDecimal(Double.valueOf(ss)); 
			double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
			return String.valueOf(f1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
