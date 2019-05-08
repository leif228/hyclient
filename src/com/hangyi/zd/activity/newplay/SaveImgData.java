package com.hangyi.zd.activity.newplay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.hangyi.zd.domain.StaEndTimeImgData;
import com.hangyi.zd.domain.TimeSrcData;

public class SaveImgData{

	private File shipChannelCacheDir;
	private StaEndTimeImgData s;
	
	public SaveImgData(File shipChannelCacheDir, StaEndTimeImgData s) {
		this.shipChannelCacheDir = shipChannelCacheDir;
		this.s = s;
	}

	public void saveImg() {
		try {
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
			            if(!bitMap.isRecycled())
			            	bitMap.recycle();
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
		} catch (Exception e) {
		}
	}


	//"GPSTime":"2016-08-04 16:52:25" 转成"GPSTime":"2016-08-04 16-52-00"
	public static String doGpsTime(String gpsTime){
		String[] arr = gpsTime.split(":");
		return arr[0]+"-"+arr[1]+"-" +"00";
	}
}
