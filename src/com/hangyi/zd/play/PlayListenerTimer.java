package com.hangyi.zd.play;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

import com.hangyi.zd.activity.ShipDynamicFragment;
import com.hangyi.zd.widge.CommonVideoView;

public class PlayListenerTimer extends Timer {
	public static PlayListenerTimer instance = null;
	private PlayListenerTimerTask playListenerTimerTask = null;
	private Handler handler;
	private boolean selfPause = false;

	private PlayListenerTimer() {
	}
	
	public void setHandler(Handler handler){
		this.handler = handler;
	}

	public static PlayListenerTimer getInstance() {
		if (instance == null)
			instance = new PlayListenerTimer();

		return instance;
	}

	public void startPlayListenerTimer() {
		if (playListenerTimerTask != null) {
			playListenerTimerTask.cancel(); // 将原任务从队列中移除
		}
		playListenerTimerTask = new PlayListenerTimerTask();
		instance.schedule(playListenerTimerTask, 0, 1  * 1000);
	}
	
	public void stopPlayListenerTimer() {
		if (playListenerTimerTask != null) {
			playListenerTimerTask.cancel(); // 将原任务从队列中移除
			playListenerTimerTask = null;
		}
		if(instance != null) {
			instance.cancel();
			instance.purge();
			instance = null;
		}
	}

	class PlayListenerTimerTask extends TimerTask {
		public PlayListenerTimerTask() {
		}

		@Override
		public void run() {
			if(handler!=null){
				if(LoadedList.needTotalSize<=LoadedList.preLoadedPlay){
					if(!CommonVideoView.isPlaying&&LoadedList.getInstance().loadedSize()==LoadedList.needTotalSize){
						handler.sendEmptyMessage(CommonVideoView.pauseState);
					}
				}else{
					if(LoadedList.getInstance().loadedSize()!=LoadedList.needTotalSize){
						if(LoadedList.getInstance().loadedSize()-ShipDynamicFragment.currPlayPosition>=LoadedList.preLoadedPlay){
							if(CommonVideoView.isPlaying){
								handler.sendEmptyMessage(CommonVideoView.playState);
							}else{
								if(selfPause){
									selfPause = false;
									CommonVideoView.isPlaying = true;
									handler.sendEmptyMessage(CommonVideoView.playState);
								}else
									handler.sendEmptyMessage(CommonVideoView.pauseState);
							}
						}else{
							if(LoadedList.needTotalSize-ShipDynamicFragment.currPlayPosition>LoadedList.preLoadedPlay){
								if(CommonVideoView.isPlaying){
									selfPause = true;
									CommonVideoView.isPlaying = false;
									handler.sendEmptyMessage(CommonVideoView.cacheState);
								}else{
									handler.sendEmptyMessage(CommonVideoView.cacheState);
								}
							}else{
								if(LoadedList.getInstance().loadedSize() <= ShipDynamicFragment.currPlayPosition){
									CommonVideoView.isPlaying = false;
									handler.sendEmptyMessage(CommonVideoView.cacheState);
								}
							}
						}
					}else{
						if(CommonVideoView.isPlaying)
							handler.sendEmptyMessage(CommonVideoView.playState);
						else
							handler.sendEmptyMessage(CommonVideoView.pauseState);
					}
				}	
					
//					if(!CommonVideoView.startedPlay&&LoadedList.getInstance().loadedSize()-ShipDynamicFragment.currPlayPosition>=LoadedList.preLoadedPlay){
//						handler.sendEmptyMessage(CommonVideoView.noPlayedPlay);
//					}else if(CommonVideoView.startedPlay){
//						if(LoadedList.getInstance().loadedSize()!=LoadedList.getInstance().getNeedTotalSize()){
//							if(LoadedList.getInstance().loadedSize()-ShipDynamicFragment.currPlayPosition>=LoadedList.preLoadedPlay){
//								handler.sendEmptyMessage(CommonVideoView.playedPlay);
//							}else{
//								handler.sendEmptyMessage(CommonVideoView.cache);
//							}
//						}else{
//							handler.sendEmptyMessage(CommonVideoView.playedPlay);
//						}
//					}
					
//					if(!CommonVideoView.startedPlay&&LoadedList.getInstance().loadedSize()-ShipDynamicFragment.currPlayPosition>=LoadedList.preLoadedPlay){
//						handler.sendEmptyMessage(CommonVideoView.noPlayedPlay);
//					}else if(CommonVideoView.startedPlay&&LoadedList.getInstance().loadedSize()-ShipDynamicFragment.currPlayPosition>=LoadedList.preLoadedPlay){
//						handler.sendEmptyMessage(CommonVideoView.playedPlay);
//					}else if(CommonVideoView.startedPlay&&LoadedList.getInstance().loadedSize()-ShipDynamicFragment.currPlayPosition<LoadedList.preLoadedPlay
//							&&LoadedList.getInstance().loadedSize()==LoadedList.getInstance().getNeedTotalSize()){
//						handler.sendEmptyMessage(CommonVideoView.playedPlay);
//					}else{
//						handler.sendEmptyMessage(CommonVideoView.cache);
//					}
				
			}
		}
	};
}
