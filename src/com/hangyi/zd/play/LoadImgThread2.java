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

import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.loaders.Data_loader;
import com.hangyi.zd.activity.ShipDynamicFragment;
import com.hangyi.zd.domain.ShipGpsData;
import com.ta.util.http.BinaryHttpResponseHandler;

public class LoadImgThread2 extends Thread {

	private Data_loader dataLoader;
	private ShipDynamicFragment shipDynamicFragment;
	private String threadLock = "";
	private List<String> loadedList = new ArrayList<String>();
	private int imgNum = 0;
	private int imgNumTemp = 0;
	private File sdCardPath = Environment.getExternalStorageDirectory();// 获取SDCard目录
	private File cacheDir = new File(sdCardPath + "/zd/img");
	private List<String> channels;
	
	public LoadImgThread2(Data_loader dataLoader, ShipDynamicFragment shipDynamicFragment,List<String> channels) {
		this.dataLoader = dataLoader;
		this.shipDynamicFragment = shipDynamicFragment;
		this.imgNum = channels.size();
		this.channels = channels;
	}

	public void run() {
		Map<String, Object> apiParams = new HashMap<String, Object>();
		// 指定文件类型  
		String[] allowedContentTypes = new String[] { "image/png", "image/jpeg", "image/jpg" };  
		
		ShipGpsData data;
		while ((data = GpsDataQueue.getInstance().poll()) != null) {
			imgNumTemp = imgNum;
			loadedList.clear();
			
			File shipCacheDir = new File(cacheDir + "/" +data.getShipID());
			
			for(final String no:channels){
				File shipChannelCacheDir = new File(shipCacheDir + "/" + no);
				if (!shipChannelCacheDir.exists()) {  
					shipChannelCacheDir.mkdirs();  
				} 
				final File shipChannelCacheImg = new File(shipChannelCacheDir+"/"+doGpsTime(data.getGpsTime())+".png");
				if (shipChannelCacheImg.exists()){
					imgNumTemp--;
					if(imgNumTemp == 0)
						LoadedList.getInstance().offer(data);
					continue;
				}  
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
					  
							loadedList.add(no);
							if(imgNumTemp == loadedList.size()){
								synchronized (threadLock) {  
									threadLock.notifyAll();  
								}  
							}
					    }  
					  
					    @Override  
					    public void onFailure(Throwable error,  byte[] binaryData) {
							loadedList.add(no);
							if(imgNumTemp == loadedList.size()){
								synchronized (threadLock) {  
									threadLock.notifyAll();  
								}  
							} 
					    }  
					
				}, ApplicationUrls.loadImg+data.getShipID()+"&Channel="+no+"&PictureTime="
				+ data.getGpsTime().replace(" ", "%20"), apiParams, "download");
			}
			if(imgNumTemp != 0)
				synchronized(threadLock) {
					try {
						threadLock.wait();
						LoadedList.getInstance().offer(data);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
//		shipDynamicFragment.callLoadImgThread();
		}
		

	}
	//"GPSTime":"2016-08-04 16:52:25" 转成"GPSTime":"2016-08-04 16:52:00"
		public String doGpsTime(String gpsTime){
			String[] arr = gpsTime.split(":");
			return arr[0]+":"+arr[1]+":" +"00";
		}
}
