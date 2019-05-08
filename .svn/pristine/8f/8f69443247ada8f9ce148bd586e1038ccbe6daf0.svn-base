package com.eyunda.third.chat.event;

import java.util.Map;

public class MessageEvent extends BaseEvent {
	private static final long serialVersionUID = -1L;

	
	public static final String ID = "id";// 聊天ID
	public static final String CONTENT = "content";// 聊天内容
	public static final String CHAT_ROOMNAME = "chatRoomName";// 聊天室
	public static final String FROM_USERID = "fromUserId";
	public static final String TO_USERID = "toUserId";
	public static final String FROM_USERNAME = "fromUserName";
	public static final String TO_USERNAME = "toUserName";

	public MessageEvent(Map<String, String> source) {
		super(source);
	}

	public void setId(Long id) {
		eventMap.put(ID, Long.toString(id));
	}

	public Long getId() {
		return Long.parseLong(eventMap.get(ID));
	}
	
	public void setContent(Object content) {
		eventMap.put(CONTENT, (String) content);
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
	
	public void setChatRoomName(String chatRoomName) {
		eventMap.put(CHAT_ROOMNAME, chatRoomName);
	}
	
	public String getChatRoomName(){
		return (String) eventMap.get(CHAT_ROOMNAME);
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
