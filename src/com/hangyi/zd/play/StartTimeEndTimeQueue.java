package com.hangyi.zd.play;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.hangyi.zd.domain.StartTimeEndTimeData;

public class StartTimeEndTimeQueue {
	public static StartTimeEndTimeQueue instance = null;
	private BlockingQueue<StartTimeEndTimeData> queue = new LinkedBlockingDeque<StartTimeEndTimeData>();

	private StartTimeEndTimeQueue() {
	}

	public static StartTimeEndTimeQueue getInstance() {
		if (instance == null)
			instance = new StartTimeEndTimeQueue();

		return instance;
	}

	public StartTimeEndTimeData poll() {
		return queue.poll();
	}

	public void offer(StartTimeEndTimeData msg) {
		queue.offer(msg);
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}
	public void clear(){
		queue.clear();
	}
}
