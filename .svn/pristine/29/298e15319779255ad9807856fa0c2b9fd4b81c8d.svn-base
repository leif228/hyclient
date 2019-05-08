package com.eyunda.third.chat.event;

import java.util.Map;

public class LogoutEvent extends BaseEvent {
	private static final long serialVersionUID = -1L;

	public static final String USERID = "userId";
	public static final String LOGIN_NAME = "loginName";
	public static final String TRUE_NAME = "trueName";
	public static final String NICK_NAME = "nickName";

	public LogoutEvent(Map<String, String> source) {
		super(source);
	}

	public void setUserId(Long userId) {
		eventMap.put(USERID, Long.toString(userId));
	}

	public Long getUserId() {
		return Long.parseLong(eventMap.get(USERID));
	}

	public void setLoginName(String loginName) {
		eventMap.put(LOGIN_NAME, loginName);
	}

	public String getLoginName() {
		return (String) eventMap.get(LOGIN_NAME);
	}

	public void setTrueName(String trueName) {
		eventMap.put(TRUE_NAME, trueName);
	}

	public String getTrueName() {
		return (String) eventMap.get(TRUE_NAME);
	}

	public void setNickName(String nickName) {
		eventMap.put(NICK_NAME, nickName);
	}

	public String getNickName() {
		return (String) eventMap.get(NICK_NAME);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
