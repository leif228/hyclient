package com.eyunda.tools;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.renderscript.Type;
import android.widget.Toast;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.home.HomeCategoryActivity;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.enumeric.EnumConst.BigAreaCode;
import com.eyunda.third.domain.enumeric.EnumConst.PortCityCode;
import com.eyunda.third.domain.order.PortData;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.domain.ship.TypeData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.log.FilePathGenerator;
import com.eyunda.tools.log.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ygl.android.view.listview.c;



public class LocalFileUtil {
	/**
	 * 读取地区文件,解析成需要的list
	 * @return
	 */
	public static Map<PortCityCode,List<PortData>> getAreaFile(Context cx){
		Map<PortCityCode,List<PortData>> areas = new HashMap<PortCityCode,List<PortData>>();
		//读文件
		String fileName = ApplicationConstants.LF_AREA_NAME;
		try {
			FileInputStream inputStream = cx.openFileInput(fileName);
			byte[] bytes = new byte[1024];
			int len = 0;
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			while ((len=inputStream.read(bytes)) != -1) {
				arrayOutputStream.write(bytes, 0, len);
			}
			inputStream.close();
			arrayOutputStream.close();

			String content = new String(arrayOutputStream.toByteArray());
			//			Log.setLog2FileEnabled(true);
			//			Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("eyunda",".log"));
			//			Log.d("res",content);
			ConvertData cd = new ConvertData(content);
			content = null;
			HashMap<String, Object> var = (HashMap<String, Object>)cd.getContent();
			List<Map<String, Object>> ports =(ArrayList<Map<String, Object>>)var.get("ports");
			//取出货物列表
			int size = ports.size();
			if(size > 0){
				for(int i=0; i<size; i++){
					PortData pd = new PortData(ports.get(i));
					//获取前两位，查找前两位的PortCityCode，如果存在，
					String code = pd.getPortNo().substring(0, 2);
					PortCityCode tempPCC = PortCityCode.getByCode(code);

					List<PortData> tempList = new ArrayList<PortData>();
					if(tempPCC != null){
						if(areas.containsKey(tempPCC)){
							//取出存在的value(是一个list)，查找当前的PortData,不存在则插入，然后更新map
							tempList = areas.get(tempPCC);
						}
						if(!tempList.contains(pd)){
							tempList.add(pd);
							areas.put(tempPCC, tempList);
						}
					}

				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return areas;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<HashMap<String, Object>> getHomeCateSearchFile(Context cx, int groupPosition, 
			List<Map<String, String>> groupData){
		ArrayList<HashMap<String, Object>> expandList = new ArrayList<HashMap<String, Object>>();
		//读文件
		int count = 5;
		//String fileName = ApplicationConstants.LF_SEARCH_CATEGORY;
		String fileName = ApplicationConstants.LF_SEARCH_CATEGORY_DLR;
		try {
			FileInputStream inputStream = cx.openFileInput(fileName);
			byte[] bytes = new byte[1024];
			int len = 0;
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			while ((len=inputStream.read(bytes)) != -1) {
				arrayOutputStream.write(bytes, 0, len);
			}
			inputStream.close();
			arrayOutputStream.close();
			String content = new String(arrayOutputStream.toByteArray());
			ConvertData cd = new ConvertData(content);
			content = null;
			HashMap<String, Object> var = (HashMap<String, Object>)cd.getContent();
			List<Map<String, Object>> companyDatas =(ArrayList<Map<String, Object>>)var.get("companyDatas");
			String bigArea = groupData.get(groupPosition).get("c_text2"); //当前选择的大区域code
			List<PortCityCode> po = PortCityCode.getPortCities(BigAreaCode.getByCode(bigArea)); //当前BigArea对应的城市列表
			//			 for(int i=0; i<po.size();i++){	
			//				 List<CompanyData> port = getCompanyByAreaCode(po.get(i),companyDatas);  //返回大区域对应的公司列表			
			//				 if(port!=null){
			//				 for(int j=0;j<port.size();j++){					 
			//					 HashMap<String, Object> hashMap = new HashMap<String, Object>();
			//					 hashMap.put("code", groupData.get(groupPosition).get("c_text2") +"_"+port.get(j).getUnitCode());
			//					 hashMap.put("title",port.get(j).getUnitName());
			//					 expandList.add(hashMap);
			//				 }
			//			 }
			//			 }



		} catch (FileNotFoundException e) {
			count--;
			if(count > 0){
				try {//递归调用，保证能读取到数据
					Thread.sleep(3);
					return getHomeCateSearchFile(cx, groupPosition, groupData);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}else{
				//5次后还无法读取说明程序出现问题
				Toast.makeText(cx, "文件无法读取，程序出现异常", Toast.LENGTH_SHORT).show();
				return null;
			}
		} catch (IOException e) {
			Toast.makeText(cx, "文件无法读取，程序出现异常", Toast.LENGTH_SHORT).show();
			return null;
		}
		return expandList;
	}


	//	private static List<CompanyData> getCompanyByAreaCode(PortCityCode portCityCode,List<Map<String, Object>> companyDatas) {
	//		 List<CompanyData> co = new ArrayList<CompanyData>();
	//		 if( companyDatas.size() > 0){			 
	//		 for(int j=0; j<companyDatas.size(); j++){				 
	//			 CompanyData cd = new CompanyData(companyDatas.get(j));
	//				  if(portCityCode.getCode().equals(cd.getPortCity().getCode())){
	//					  co.add(cd);
	//				  }			  
	//			 }
	//			 if(co.size()>0)return co;
	//			
	//		 }
	//		 
	//		return null;
	//	}
	//	

	@SuppressWarnings("unchecked")
	public static ArrayList<HashMap<String, Object>> getShipSortList(Context cx, int groupPosition, 
			List<Map<String, String>> groupData){

		ArrayList<HashMap<String, Object>> arrayList= new ArrayList<HashMap<String,Object>>();

		String fileName = ApplicationConstants.LF_SEARCH_SHIP_LIST;
		try {
			FileInputStream inputStream = cx.openFileInput(fileName);
			byte[] bytes = new byte[1024];
			int len = 0;
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			while ((len=inputStream.read(bytes)) != -1) {
				arrayOutputStream.write(bytes, 0, len);
			}
			inputStream.close();
			arrayOutputStream.close();

			String content = new String(arrayOutputStream.toByteArray());
			ConvertData cd = new ConvertData(content);
			HashMap<String, Object> var = (HashMap<String, Object>)cd.getContent();
			List<Map<String, Object>>   dat = (List<Map<String, Object>>) var.get("uncleTypeDatas");
			if(dat.size()>0){
				for (int i = 0; i < dat.size(); i++) {
					TypeData type = new TypeData((Map<String, Object>) dat.get(i)); 						
					List<TypeData>  list = type.getChildrenDatas();  
					for(int j=0;j<list.size();j++){
						HashMap<String, Object> hashMap = new HashMap<String, Object>();
						//遍历找到TypeData
						TypeData   td = (TypeData )list.get(j);
						String  ship_type =td.getTypeName();

						hashMap.put("code", groupData.get(groupPosition).get("c_text2")+"_"+td.getTypeCode());
						hashMap.put("title", ship_type);
						arrayList.add(hashMap);

					}

				}

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayList;
	}
	public static void getAreas(final Context cx){
		Data_loader data = new Data_loader();
		final Map<String, Object> params = new HashMap<String, Object>();
		data.getApiResult(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				String fileName = ApplicationConstants.LF_AREA_NAME;
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equalsIgnoreCase("success")) {
					FileOutputStream outputStream;
					try {
						outputStream = cx.openFileOutput(fileName,
								Activity.MODE_PRIVATE);
						outputStream.write(arg0.getBytes());
						outputStream.flush();
						outputStream.close();
						outputStream = null;
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {

				}
			}
		}, "/mobile/comm/getPorts", params, "get");
	}
}