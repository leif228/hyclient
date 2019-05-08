package com.hangyi.zd;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.eyunda.third.loaders.Data_loader;
import com.hangyi.zd.play.LoadImgRunnable;

public class ImgDownLoadService extends Service {  
	private ExecutorService pool ;
	private Data_loader dataLoader;
	private static final int threadNum = 15;
	public ServiceBinder mBinder = new ServiceBinder(); /* 数据通信的桥梁 */
	
    @Override  
    public void onCreate() {  
        super.onCreate();  
        
        dataLoader = new Data_loader();
    }  
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	
    	return super.onStartCommand(intent, flags, startId);
    } 
  
    @Override  
    public void onDestroy() { 
    	super.onDestroy();  
    	if(pool!=null)
    		pool.shutdown();
    }

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}  
	
	public class ServiceBinder extends Binder { 

	    public void startThreadLoadImg(List<String> channels) {
	    	if(pool!=null)
	    		pool.shutdownNow();
	    	
	    	pool = Executors.newFixedThreadPool(threadNum);
			for(int i=0;i<threadNum;i++){
				pool.execute(new LoadImgRunnable(dataLoader,channels));
			}
		}
	    
	    public void stopThreadLoadImg(){
	    	if(pool!=null)
	    		pool.shutdownNow();
	    }
    }

}
