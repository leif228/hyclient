package com.hangyi.zd.play;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.domain.location.ShipCooordData;
import com.eyunda.third.loaders.Data_loader;
import com.hangyi.zd.domain.ShipModelCode;
import com.hangyi.zd.domain.ShipModelData;
import com.hangyi.zd.domain.ShipModelNoData;
import com.hangyi.zd.domain.UserPowerData;
import com.hangyi.zd.domain.UserPowerShipData;
import com.ta.util.http.BinaryHttpResponseHandler;

public class PageHomeLoadImgRunnable implements Runnable {

	private Data_loader dataLoader;
	private File sdCardPath = Environment.getExternalStorageDirectory();// 获取SDCard目录
	private File cacheDir = new File(sdCardPath + ApplicationConstants.pageHomeImgCachePath);
	
	private ShipCooordData acd;
	private UserPowerData data;
	
	public PageHomeLoadImgRunnable(Data_loader dataLoader, ShipCooordData acd, UserPowerData data) {
		this.dataLoader = dataLoader;
		this.data = data;
		this.acd = acd;
	}

	public void run() {
		Map<String, Object> apiParams = new HashMap<String, Object>();
		// 指定文件类型  
		String[] allowedContentTypes = new String[] { "image/png", "image/jpeg", "image/jpg" };  
		
//		SharedPreferences sp = GlobalApplication.getInstance().getSharedPreferences("UserPowerData", Context.MODE_PRIVATE);
//		String object = sp.getString("UserPower", "");
//		
//		UserPowerData data = null;
//		if(!"".equals(object)){
//			Gson gson = new Gson();
//			data = gson.fromJson(object, new TypeToken<UserPowerData>() {}.getType());
//		}else
//			data = new UserPowerData();
		
		List<ShipModelNoData> smnd = new ArrayList<ShipModelNoData>();
		flag:
			for(UserPowerShipData upsd:data.getUserPowerShipDatas()){
				if(upsd.getShipID().equals(acd.getShipID())){
					for(ShipModelData smd:upsd.getShipModels()){
						if(smd.getModel() == ShipModelCode.five){
							smnd = smd.getModelNolist() ;
							break flag;
						}
					}
				}
			}
		
		File shipCacheDir = new File(cacheDir + "/" +acd.getShipID());
		if (!shipCacheDir.exists()) {  
			shipCacheDir.mkdirs();  
		} 
		for(int i = 0;i<smnd.size();i++){

			final File shipChannelCacheImg = new File(shipCacheDir + "/" + smnd.get(i).getModelNo() + ".png");
			
			dataLoader.getZd_ApiResult(new BinaryHttpResponseHandler(allowedContentTypes) {
				 @Override  
				    public void onSuccess(int statusCode, byte[] binaryData) {  
				        // 下载成功后需要做的工作  
				        Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0,  
				                binaryData.length);  
				        // 压缩格式  
				        CompressFormat format = Bitmap.CompressFormat.JPEG;  
				        // 压缩比例  
				        int quality = 50;  
				        try {  
				        	if (shipChannelCacheImg.exists()) {  
								shipChannelCacheImg.delete();  
							} 
				            // 创建文件  
				        	shipChannelCacheImg.createNewFile();  
				            //  
				            OutputStream stream = new FileOutputStream(shipChannelCacheImg);  
				            // 压缩输出  
				            bmp.compress(format, quality, stream);  
				            stream.flush();
				            // 关闭  
				            stream.close();  
				  
				        } catch (IOException e) {  
				            e.printStackTrace();  
				        }  
				    }  
				  
				    @Override  
				    public void onFailure(Throwable error,  byte[] binaryData) {
				    }  
				
			}, ApplicationUrls.loadImg+acd.getShipID()+"&Channel="+smnd.get(i).getModelNo()+"&PictureTime="
			+ acd.getGpsTime().replace(" ", "%20"), apiParams, "download");
		
		}
	}
	//"GPSTime":"2016-08-04 16:52:25" 转成"GPSTime":"2016-08-04 16-52-00"
		public static String doGpsTime(String gpsTime){
			String[] arr = gpsTime.split(":");
			return arr[0]+"-"+arr[1]+"-" +"00";
		}
}
