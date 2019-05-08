package com.hangyi.zd.activity.newplay;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import com.hangyi.zd.activity.ShipDynamicFragment;
import com.hangyi.zd.widge.CommonVideoView;

import android.os.Handler;


public class PlayGpsTimer extends Timer {
	public static PlayGpsTimer instance = null;
	private PlayGpsTask playGpsTask = null;
	private Handler handler;
	private static long speed = Long.valueOf(1000/com.hangyi.zd.activity.newplay.ShipDynamicFragment.playmod); 

	private PlayGpsTimer() {
	}
	
	public void setHandler(Handler handler){
		this.handler = handler;
	}

	public static PlayGpsTimer getInstance() {
		if (instance == null)
			instance = new PlayGpsTimer();

		return instance;
	}
	
	public void resetTime(Long newTime) throws IllegalAccessException, IllegalArgumentException{
		speed = newTime;
		
		if(instance != null&&playGpsTask!=null) {
			Field[] fields = playGpsTask.getClass().getSuperclass().getDeclaredFields();  
			for (Field field : fields)  
			{  
				if (field.getName().endsWith("period"))  
				{  
					if (!field.isAccessible())  
					{  
						field.setAccessible(true);  
					}  
					field.set(playGpsTask, newTime);  
				}  
			}  
		}
	}

	public void startPlayGpsTimer() {
		if (playGpsTask != null) {
			playGpsTask.cancel(); // 将原任务从队列中移除
		}
		playGpsTask = new PlayGpsTask();
//		instance.schedule(playGpsTask, 0, 1000);
		instance.schedule(playGpsTask, 0, speed);
	}
	public void pausePlayGpsTimer() {
		if (playGpsTask != null) {
			playGpsTask.cancel(); // 将原任务从队列中移除
			playGpsTask = null;
			instance.cancel();
			instance.purge();
		}
	}
	
	public void stopPlayGpsTimer() {
		if (playGpsTask != null) {
			playGpsTask.cancel(); // 将原任务从队列中移除
			playGpsTask = null;
		}
		if(instance != null) {
			instance.cancel();
			instance.purge();
			instance = null;
		}
	}

	class PlayGpsTask extends TimerTask {
		public PlayGpsTask() {
		}

		@Override
		public void run() {
			if(handler!=null)
				handler.sendEmptyMessage(2);
		}
	};
}
