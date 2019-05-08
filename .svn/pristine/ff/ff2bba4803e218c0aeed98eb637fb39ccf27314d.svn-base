package com.hangyi.zd.activity.newplay;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.hangyi.zd.domain.ShipGpsData;

public class GpsDataQueue {
	public static GpsDataQueue instance = null;
	private BlockingQueue<ShipGpsData> queue = new LinkedBlockingDeque<ShipGpsData>();

	private GpsDataQueue() {
	}

	public static GpsDataQueue getInstance() {
		if (instance == null)
			instance = new GpsDataQueue();

		return instance;
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
		queue.clear();
	}
	public int size(){
		return queue.size();
	}
}
