package com.hangyi.zd.activity.newplay;

import java.util.List;

public class SaveImgRunnable implements Runnable {

	private List<SaveImgData> list;
	private int startTimeEndTimeMins = 0;
	
	public SaveImgRunnable(List<SaveImgData> list, int startTimeEndTimeMins) {
		this.list = list;
		this.startTimeEndTimeMins = startTimeEndTimeMins;
	}

	public void run() {
		try {
			if(list == null)
				return;
			for(SaveImgData sid:list){
				sid.saveImg();
			}
			LoadedList.getInstance().add(startTimeEndTimeMins);
		} catch (Exception e) {
		}
	}
}
