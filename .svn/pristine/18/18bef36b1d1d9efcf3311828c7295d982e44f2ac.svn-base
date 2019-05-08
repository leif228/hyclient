package com.eyunda.third.chat.mina;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import com.eyunda.third.chat.event.BaseEvent;

public class MessageQueue {
	public static MessageQueue instance = null;
	private Queue<BaseEvent> queue = new LinkedBlockingDeque<BaseEvent>();

	private MessageQueue() {
	}

	public static MessageQueue getInstance() {
		if (instance == null)
			instance = new MessageQueue();

		return instance;
	}

	public synchronized BaseEvent poll() {
		return queue.poll();
	}

	public synchronized void offer(BaseEvent msg) {
		queue.offer(msg);
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
