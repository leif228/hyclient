package com.eyunda.third.chat.event;

import java.util.Map;

public class NotifyEvent extends BaseEvent {
	private static final long serialVersionUID = -1L;

	public static final String ID = "id";// 通知ID
	public static final String MSG_TYPE = "msgType";// 通知类别
	public static final String TITLE = "title";// 通知标题
	public static final String CONTENT = "content";// 通知内容
	public static final String FROM_USERID = "fromUserId";
	public static final String TO_USERID = "toUserId";
	public static final String FROM_USERNAME = "fromUserName";
	public static final String TO_USERNAME = "toUserName";

	public NotifyEvent(Map<String, String> source) {
		super(source);
	}

	public void setId(Long id) {
		eventMap.put(ID, Long.toString(id));
	}

	public Long getId() {
		return Long.parseLong(eventMap.get(ID));
	}

	public void setMsgType(String msgType) {
		eventMap.put(MSG_TYPE, msgType);
	}

	public String getMsgType() {
		return eventMap.get(MSG_TYPE);
	}

	public void setTitle(String title) {
		eventMap.put(TITLE, title);
	}

	public String getTitle() {
		return eventMap.get(TITLE);
	}

	public void setContent(String content) {
		eventMap.put(CONTENT, content);
	}

	public String getContent() {
		return (String) eventMap.get(CONTENT);
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
