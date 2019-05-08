package com.eyunda.third;

import com.eyunda.third.chat.mina.MessageThread;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

public class PushService extends Service {
	MessageThread thread;
	private PowerManager.WakeLock wakeLock;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override  
    public boolean onUnbind(Intent intent){   
            return false;   
    }   

    @Override  
    public void onCreate(){   
    	wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "run");
    	wakeLock.acquire();
    	
    	thread = new MessageThread();
    	thread.start();
    }   

    @Override  
    public void onStart(Intent intent,int startid){   
    }   

    @Override  
    public void onDestroy(){   
    	if(wakeLock.isHeld())
    		wakeLock.release();
    	wakeLock = null;
    	
    	thread.interrupt();
    }   
}
