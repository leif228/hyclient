package com.hangyi.zd.activity.newplay;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ImgLocalSaveService extends Service {  
	private ExecutorService pool ;
	private static final int threadNum = 3;
	public ServiceBinder mBinder = new ServiceBinder(); /* 数据通信的桥梁 */
	
    @Override  
    public void onCreate() {  
        super.onCreate();  
        
    }  
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	
    	return super.onStartCommand(intent, flags, startId);
    } 
  
    @Override  
    public void onDestroy() { 
    	super.onDestroy();  
    	if(pool!=null){
    		pool.shutdownNow();
    		pool = null;
    	}
    }

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}  
	
	public class ServiceBinder extends Binder {

	    public synchronized void startThreadSaveImg(SaveImgRunnable sir) {
	    	if(pool == null)
	    		pool = Executors.newFixedThreadPool(threadNum);
	    	
			pool.execute(sir);
		}
	    
	    public void stopThreadLoadImg(){
	    	if(pool!=null){
	    		pool.shutdownNow();
	    		pool = null;
	    	}
	    }
    }

}
