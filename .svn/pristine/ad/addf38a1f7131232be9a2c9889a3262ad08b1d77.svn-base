package com.hangyi.zd;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import com.eyunda.third.GlobalApplication;
import com.eyunda.third.domain.location.ShipCooordData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangyi.zd.domain.ShipModelCode;
import com.hangyi.zd.domain.ShipModelData;
import com.hangyi.zd.domain.UserPowerData;
import com.hangyi.zd.domain.UserPowerShipData;
import com.hangyi.zd.play.PageHomeLoadImgRunnable;

public class PageHomeImgDownLoadService extends Service {  
	private ExecutorService pool ;
	private Data_loader dataLoader;
	private static final int threadNum = 10;
	public ServiceBinder mBinder = new ServiceBinder(); /* 数据通信的桥梁 */
	
    @Override  
    public void onCreate() {  
        super.onCreate();  
        
        dataLoader = new Data_loader();
        pool = Executors.newFixedThreadPool(threadNum);
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

	    public void startThreadLoadImg(List<ShipCooordData> list) {
	    	SharedPreferences sp = GlobalApplication.getInstance().getSharedPreferences("UserPowerData", Context.MODE_PRIVATE);
			String object = sp.getString("UserPower", "");
			
			UserPowerData data = null;
			if(!"".equals(object)){
				Gson gson = new Gson();
				data = gson.fromJson(object, new TypeToken<UserPowerData>() {}.getType());
			}else
				data = new UserPowerData();
	    	
	    	for(ShipCooordData acd:list){
	    		pool.execute(new PageHomeLoadImgRunnable(dataLoader,acd,data));
	    	}
		}
	    
	    public void stopThreadLoadImg(){
	    	if(pool!=null)
	    		pool.shutdownNow();
	    }
    }

}
