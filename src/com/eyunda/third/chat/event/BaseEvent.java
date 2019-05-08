package com.eyunda.third.chat.event;

import java.util.Date;
import java.util.Map;

import com.eyunda.third.domain.enumeric.LoginSourceCode;
import com.google.gson.Gson;

public class BaseEvent {
	private static final long serialVersionUID = 348549046628446287L;

	protected Map<String, String> eventMap;

	public final static String MESSAGE_TYPE = "messageType";
	public final static String DATE_TIME = "dateTime";
	public final static String SESSION_ID = "sessionId";
	public final static String LOGIN_SOURCE = "loginSource";

	public BaseEvent(Map<String, String> source) {

		this.setEventMap(source);
		this.setDateTime(new Date().getTime());
	}

	public final void setMessageType(String messageType) {
		eventMap.put(MESSAGE_TYPE, messageType);
	}

	public final String getMessageType() {
		return (String) eventMap.get(MESSAGE_TYPE);
	}

	public final void setDateTime(Long dateTime) {
		eventMap.put(DATE_TIME, Long.toString(dateTime));
	}

	public final Long getDateTime() {
		return Long.parseLong(eventMap.get(DATE_TIME));
	}

	public final void setLoginSource(LoginSourceCode loginSource) {
		eventMap.put(LOGIN_SOURCE, loginSource.toString());
	}

	public final LoginSourceCode getLoginSource() {
		return LoginSourceCode.valueOf(eventMap.get(LOGIN_SOURCE));
	}

	public final void setSessionId(String sessionId) {
		eventMap.put(SESSION_ID, sessionId);
	}

	public final String getSessionId() {
		return (String) eventMap.get(SESSION_ID);
	}
	
	public final void setEventMap(Map<String, String> eventMap) {
		this.eventMap = eventMap;
	}

	public final Map<String, String> getEventMap() {
		return eventMap;
	}

	public String toJson() {
		Gson gson = new Gson();
		String json = gson.toJson(eventMap);

		return json;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
