package com.eyunda.third.chat.event;

import java.util.Map;

public class StatusEvent extends BaseEvent {
	private static final long serialVersionUID = 6619170001649508819L;

	public static final String FROM_USERID = "fromUserId";
	public static final String TO_USERID = "toUserId";
	public static final String FROM_USERNAME = "fromUserName";
	public static final String TO_USERNAME = "toUserName";
	public static final String ONLINE_STATUS = "onlineStatus";

	public StatusEvent(Map<String, String> source) {
		super(source);
	}

	public void setFromUserId(Long fromUserId) {
		eventMap.put(FROM_USERID, Long.toString(fromUserId));
	}

	public Long getFromUserId() {
		return Long.parseLong(eventMap.get(FROM_USERID));
	}

	public void setToUserId(Long toUserId) {
		eventMap.put(TO_USERID, Long.toString(toUserId));
	}

	public Long getToUserId() {
		return Long.parseLong(eventMap.get(TO_USERID));
	}

	public void setFromUserName(String fromUserName) {
		eventMap.put(FROM_USERNAME, fromUserName);
	}

	public String getFromUserName() {
		return (String) eventMap.get(FROM_USERNAME);
	}

	public void setToUserName(String toUserName) {
		eventMap.put(TO_USERNAME, toUserName);
	}

	public String getToUserName() {
		return (String) eventMap.get(TO_USERNAME);
	}

	public void setOnlineStatus(OnlineStatusCode onlineStatus) {
		eventMap.put(ONLINE_STATUS, onlineStatus.toString());
	}

	public OnlineStatusCode getOnlineStatus() {
		return OnlineStatusCode.valueOf(eventMap.get(ONLINE_STATUS));
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
