package com.hangyi.zd.activity.newplay;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import com.hangyi.zd.widge.CommonVideoView;

import android.os.Handler;

public class CacheSeekBarTimer extends Timer {
	public static CacheSeekBarTimer instance = null;
	private CacheSeekBarTimerTask cacheSeekBarTask = null;
	private Handler handler;

	private CacheSeekBarTimer() {
	}
	
	public void setHandler(Handler handler){
		this.handler = handler;
	}

	public static CacheSeekBarTimer getInstance() {
		if (instance == null)
			instance = new CacheSeekBarTimer();

		return instance;
	}

	public void startCacheSeekBarTimer() {
		if (cacheSeekBarTask != null) {
			cacheSeekBarTask.cancel(); // 将原任务从队列中移除
		}
		cacheSeekBarTask = new CacheSeekBarTimerTask();
		instance.schedule(cacheSeekBarTask, 0, 1  * 1000);
	}
	
	public void stopCacheSeekBarTimer() {
		if (cacheSeekBarTask != null) {
			cacheSeekBarTask.cancel(); // 将原任务从队列中移除
			cacheSeekBarTask = null;
		}
		if(instance != null) {
			instance.cancel();
			instance.purge();
			instance = null;
		}
	}
	public void resetTime(Long newTime) throws IllegalAccessException, IllegalArgumentException{
		if(instance != null&&cacheSeekBarTask!=null) {
			 Field[] fields = cacheSeekBarTask.getClass().getSuperclass().getDeclaredFields();  
		        for (Field field : fields)  
		        {  
		            if (field.getName().endsWith("period"))  
		            {  
		                if (!field.isAccessible())  
		                {  
		                    field.setAccessible(true);  
		                }  
		                field.set(cacheSeekBarTask, newTime);  
		            }  
		        }  
		}
	}

	class CacheSeekBarTimerTask extends TimerTask {
		public CacheSeekBarTimerTask() {
		}

		@Override
		public void run() {
			if(handler!=null)
				handler.sendEmptyMessage(CommonVideoView.UPDATE_CACHE_SEEKBAR);
		}
	};
}
