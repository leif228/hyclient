package com.hangyi.zd.play;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.hangyi.zd.domain.ShipGpsData;

public class LoadedList {
	public static LoadedList instance = new LoadedList();
	private BlockingQueue<ShipGpsData> queue;
	public static final int preLoadedPlay = 20;
	public static int needTotalSize = 0;

	private LoadedList() {
	}

	public static LoadedList getInstance() {

		return instance;
	}

	public void initQueue(int needTotalSize) {
		LoadedList.needTotalSize = needTotalSize;
		if(0 != needTotalSize)
			queue = new LinkedBlockingDeque<ShipGpsData>(needTotalSize);
		else
			queue = new LinkedBlockingDeque<ShipGpsData>();
	}

	public int loadedSize() {
		if(queue!=null)
			return queue.size();
		else
			return 0;
	}

	public ShipGpsData poll() {
		return queue.poll();
	}

	public void offer(ShipGpsData msg) {
		queue.offer(msg);
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	public void clear(){
		if(queue!=null)
			queue.clear();
	}

}
