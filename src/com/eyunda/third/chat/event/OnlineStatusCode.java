package com.eyunda.third.chat.event;

public enum OnlineStatusCode {
	online("在线"), ofline("离线");// , busy("忙碌"), idle("空闲")

	private String description;

	private OnlineStatusCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
