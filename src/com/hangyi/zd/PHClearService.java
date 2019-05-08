package com.hangyi.zd;

import java.io.File;

import com.eyunda.third.ApplicationConstants;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

public class PHClearService extends Service {  
  
    Thread clearThread;
    File sdCardPath = Environment.getExternalStorageDirectory();// 获取SDCard目录
	File cacheDir = new File(sdCardPath + ApplicationConstants.pageHomeImgCachePath);
    
    @Override  
    public void onCreate() {  
        super.onCreate();  
        doDelete();
    }  
    
    public void doDelete(){
    	if(cacheDir.exists()&&cacheDir.isDirectory()&&cacheDir.listFiles().length>0){
    		clearThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					DeleteFile(cacheDir);
					clearThread=null;
				}
			});
    		clearThread.start();
    	}
    }
    
    public void DeleteFile(File file) { 
        if (file.exists() == false) { 
            return; 
        } else { 
            if (file.isFile()) { 
                file.delete(); 
                return; 
            } 
            if (file.isDirectory()) { 
                File[] childFile = file.listFiles(); 
                if (childFile == null || childFile.length == 0) { 
                    file.delete(); 
                    return; 
                } 
                for (File f : childFile) { 
                    DeleteFile(f); 
                } 
                file.delete(); 
            } 
        } 
    } 
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	
//    	if(clearThread == null)
    		doDelete();
    	
    	return super.onStartCommand(intent, flags, startId);
    }
  
    @Override  
    public void onDestroy() { 
    	clearThread =null;
    	super.onDestroy();  
    }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}  

}
