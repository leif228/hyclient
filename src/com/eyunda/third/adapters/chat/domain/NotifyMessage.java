package com.eyunda.third.adapters.chat.domain;

import java.util.Calendar;

import com.eyunda.third.domain.enumeric.HandleStatusCode;
import com.eyunda.third.domain.enumeric.NotifyTypeCode;
import com.eyunda.third.domain.enumeric.ReadStatusCode;

public class NotifyMessage {
private Long id;
private String title;
private String message;
private Long senderId;
private Long receiverId;
private ReadStatusCode  readStatus;//阅读状态
private HandleStatusCode handleStatus;//后续处理状态
private NotifyTypeCode msgType;//通知类别
private Calendar createTime;



public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public Long getSenderId() {
	return senderId;
}
public void setSenderId(Long senderId) {
	this.senderId = senderId;
}
public Long getReceiverId() {
	return receiverId;
}
public void setReceiverId(Long receiverId) {
	this.receiverId = receiverId;
}
public ReadStatusCode getReadStatus() {
	return readStatus;
}
public void setReadStatus(ReadStatusCode readStatus) {
	this.readStatus = readStatus;
}
public HandleStatusCode getHandleStatus() {
	return handleStatus;
}
public void setHandleStatus(HandleStatusCode handleStatus) {
	this.handleStatus = handleStatus;
}
public NotifyTypeCode getMsgType() {
	return msgType;
}
public void setMsgType(NotifyTypeCode msgType) {
	this.msgType = msgType;
}
public Calendar getCreateTime() {
	return createTime;
}
public void setCreateTime(Calendar createTime) {
	this.createTime = createTime;
}


}
