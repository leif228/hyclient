package com.hangyi.zd.activity.newplay;

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
import android.util.Base64;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.ApplicationUrls;
import com.eyunda.third.loaders.Data_loader;
import com.hangyi.tools.ParseJson;
import com.hangyi.zd.domain.StaEndTimeImgData;
import com.hangyi.zd.domain.StartTimeEndTimeData;
import com.hangyi.zd.domain.TimeSrcData;
import com.hangyi.zd.play.StartTimeEndTimeQueue;
import com.ta.util.http.AsyncHttpResponseHandler;

public class LoadImgRunnable implements Runnable {

	private Data_loader dataLoader;
	private String threadLock = "";
	private volatile List<String> loadedChannels = new ArrayList<String>();
	private int imgNum = 0;
	private int imgNumTemp = 0;
	private File sdCardPath = Environment.getExternalStorageDirectory();// 获取SDCard目录
	private File cacheDir = new File(sdCardPath + ApplicationConstants.imgCachePath);
	private List<String> channels;
	private String shipID;
	private ImgLocalSaveService.ServiceBinder mBinderService;
	private int startTimeEndTimeMins = 0;
	private List<SaveImgData> sids = new ArrayList<SaveImgData>();
	
	public LoadImgRunnable(String shipID, Data_loader dataLoader, List<String> channels, ImgLocalSaveService.ServiceBinder mBinderService) {
		this.dataLoader = dataLoader;
		this.imgNum = channels.size();
		this.channels = channels;
		this.shipID = shipID;
		this.mBinderService = mBinderService;
	}

	public void run() {
		Map<String, Object> apiParams = new HashMap<String, Object>();
		// 指定文件类型  
		String[] allowedContentTypes = new String[] { "image/png", "image/jpeg", "image/jpg" };  
		
		StartTimeEndTimeData data;
		while ((data = StartTimeEndTimeQueue.getInstance().poll()) != null) {
			startTimeEndTimeMins = data.getMins();
			imgNumTemp = imgNum;
			loadedChannels.clear();
			
			File shipCacheDir = new File(cacheDir + "/" +shipID);
			
			for(final String no:channels){
				final File shipChannelCacheDir = new File(shipCacheDir + "/" + no);
				if (!shipChannelCacheDir.exists()) {  
					shipChannelCacheDir.mkdirs();  
				} 
 
				dataLoader.getZd_ApiResult(
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String arg2) {
								super.onSuccess(arg2);
								StaEndTimeImgData s = ParseJson.decodeStaEndTimeImgData(arg2);
								saveImg(shipChannelCacheDir,s);
								
//								SaveImgData sid = new SaveImgData(shipChannelCacheDir, s);
//								sids.add(sid);
								
//								SaveImgRunnable2 sir = new SaveImgRunnable2(shipChannelCacheDir,s,startTimeEndTimeMins);
//								mBinderService.startThreadSaveImg(sir);
								
								loadedChannels.add(no);
								if(imgNumTemp == loadedChannels.size()){
									synchronized (threadLock) {  
										threadLock.notifyAll();  
									}  
								}
							}
							@Override
							public void onFailure(Throwable error, String content) {
								super.onFailure(error, content);
								loadedChannels.add(no);
								if(imgNumTemp == loadedChannels.size()){
									synchronized (threadLock) {  
										threadLock.notifyAll();  
									}  
								} 
							}
						}, ApplicationUrls.loadImg2+shipID+"&Channel="+no
						+"&StartTime=" + data.getStartTime().replace(" ", "%20")
						+"&EndTime=" + data.getEndTime().replace(" ", "%20"), apiParams, "get"
				);
			}
			if(imgNumTemp != 0)
				synchronized(threadLock) {
					try {
						threadLock.wait();
						LoadedList.getInstance().add(startTimeEndTimeMins);
//						SaveImgRunnable sir = new SaveImgRunnable(sids,startTimeEndTimeMins);
//						mBinderService.startThreadSaveImg(sir);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		}
	}

	protected void saveImg(File shipChannelCacheDir, StaEndTimeImgData s) {
		if(s == null)
			return;
		for(TimeSrcData t:s.getContent()){
			File shipChannelCacheImg = new File(shipChannelCacheDir+"/"+doGpsTime(t.getTime())+".png");
			if (shipChannelCacheImg.exists()){
				continue;
			}else{
				String[] arr = t.getSrc().split(",");
				if(arr.length!=2)
					continue;
				byte[] decodedString = Base64.decode(arr[1], Base64.DEFAULT);
				Bitmap bitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
				
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
		            bitMap.compress(format, quality, stream);  
		            stream.flush();
		            // 关闭  
		            stream.close();  
		            if(!bitMap.isRecycled()){
						bitMap.recycle();
						bitMap = null;
					}
		  
		        } catch (IOException e) {  
		            e.printStackTrace();  
		        }  
			} 
		}
		System.gc();
	}

	//"GPSTime":"2016-08-04 16:52:25" 转成"GPSTime":"2016-08-04 16-52-00"
	public static String doGpsTime(String gpsTime){
		String[] arr = gpsTime.split(":");
		return arr[0]+"-"+arr[1]+"-" +"00";
	}
}
