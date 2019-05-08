package com.hangyi.zd.activity.newplay;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.CalendarUtil;
import com.hangyi.zd.domain.StartTimeEndTimeData;
import com.hangyi.zd.play.StartTimeEndTimeQueue;

public class ImgDownLoadService extends Service {  
	private ExecutorService pool ;
	private Data_loader dataLoader;
	private static final int threadNum = 8;
	private static final int mins = 10;
	public ServiceBinder mBinder = new ServiceBinder(); /* 数据通信的桥梁 */
	
	private ImgLocalSaveService.ServiceBinder mBinderService;
	private ServiceConnection connection = new ServiceConnection() {  
		@Override  
		public void onServiceDisconnected(ComponentName name) {  
		}  
		
		@Override  
		public void onServiceConnected(ComponentName name, IBinder service) {  
			mBinderService = (ImgLocalSaveService.ServiceBinder) service;
		}  
	};
	
    @Override  
    public void onCreate() {  
        super.onCreate();  
        
        dataLoader = new Data_loader();
        Intent bindIntent = new Intent(this, ImgLocalSaveService.class);  
		bindService(bindIntent, connection, Context.BIND_AUTO_CREATE); 
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

	    public void startThreadLoadImg(String shipID, List<String> channels, Calendar start, Calendar end) {
	    	initStartTimeEndTimeQueue(start ,end);
	    	
	    	if(pool!=null){
	    		pool.shutdownNow();
	    		pool = null;
	    	}
	    	
	    	pool = Executors.newFixedThreadPool(threadNum);
	    	
	    	//1.并行加载
//			for(int i=0;i<threadNum;i++){
//				pool.execute(new LoadImgRunnable(shipID ,dataLoader, channels, mBinderService));
//			}
	    	
	    	//2.顺序加载
	    	StartTimeEndTimeData data;
			while ((data = StartTimeEndTimeQueue.getInstance().poll()) != null) {
				pool.execute(new LoadImgRunnable2(shipID ,dataLoader, channels, mBinderService,data));
			}
		}
	    
	    public void stopThreadLoadImg(){
	    	if(pool!=null){
	    		pool.shutdownNow();
	    		pool = null;
	    	}
	    	mBinderService.stopThreadLoadImg();
	    }
    }

	private void initStartTimeEndTimeQueue(Calendar start, Calendar end) {
		StartTimeEndTimeQueue.getInstance().clear();
		
		start = CalendarUtil.getTheSecondZero(start);
		end = CalendarUtil.getTheSecondZero(end);
		
		long timeOne=start.getTimeInMillis();
		long timeTwo=end.getTimeInMillis();
		int minute=(int) ((timeTwo-timeOne)/(1000*60));//转化minute
		
		if(minute<=mins){
			StartTimeEndTimeData sd = new StartTimeEndTimeData();
			sd.setStartTime(CalendarUtil.toYYYY_MM_DD_HH_MM_SS(start));
			sd.setEndTime(CalendarUtil.toYYYY_MM_DD_HH_MM_SS(end));
			
			StartTimeEndTimeQueue.getInstance().offer(sd);
		}else{
			while (true) {
				Calendar tempc = CalendarUtil.addMinutes(start, mins);
				if(tempc.getTimeInMillis()<end.getTimeInMillis()){
					StartTimeEndTimeData sd = new StartTimeEndTimeData();
					sd.setStartTime(CalendarUtil.toYYYY_MM_DD_HH_MM_SS(start));
					sd.setEndTime(CalendarUtil.toYYYY_MM_DD_HH_MM_SS(tempc));
					sd.setMins(this.getMins(start, tempc));
					
					StartTimeEndTimeQueue.getInstance().offer(sd);
					
//					tempc = CalendarUtil.addMinutes(tempc, 1); //下一分钟0-30,31-61
					start = tempc;
				}else{
					StartTimeEndTimeData sd = new StartTimeEndTimeData();
					sd.setStartTime(CalendarUtil.toYYYY_MM_DD_HH_MM_SS(start));
					sd.setEndTime(CalendarUtil.toYYYY_MM_DD_HH_MM_SS(end));
					sd.setMins(this.getMins(start, end));
					
					StartTimeEndTimeQueue.getInstance().offer(sd);
					
					break;
				}
			}
		}
	}
	
	private int getMins(Calendar start,Calendar end){
		long timeOne=start.getTimeInMillis();
		long timeTwo=end.getTimeInMillis();
		int minute=(int) ((timeTwo-timeOne)/(1000*60));//转化minute
		
		return minute;
	}

}
