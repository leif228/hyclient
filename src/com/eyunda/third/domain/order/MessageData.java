package com.eyunda.third.domain.order;

import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.HandleStatusCode;
import com.eyunda.third.domain.enumeric.NotifyTypeCode;
import com.eyunda.third.domain.enumeric.ReadStatusCode;

public class MessageData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L; //

	private NotifyTypeCode msgType = null; // 通知类别

	private String title = ""; // 通知标题
	private String message = ""; // 通知内容

	private UserData sender = null;
	private Long senderId = 0L; // 发送者ID

	private UserData receiver = null;
	private Long receiverId = 0L; // 接收者ID

	private ReadStatusCode readStatus = ReadStatusCode.noread; // 阅读状态
	private HandleStatusCode handleStatus = HandleStatusCode.nohandle; // 后续处理状态
	private String createTime = ""; // 发送时间

	@SuppressWarnings("unchecked")
	public MessageData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			
			Object mt = params.get("msgType");
			if (mt != null && NotifyTypeCode.valueOf((String) mt) != null) {
				this.msgType = NotifyTypeCode.valueOf((String) params
						.get("msgType"));
			}
			
			this.title = (String) params.get("title");
			this.message = (String) params.get("message");
			this.sender = new UserData(
					(Map<String, Object>) params.get("sender"));
			this.senderId = ((Double) params.get("senderId")).longValue();
			this.receiver = new UserData(
					(Map<String, Object>) params.get("receiver"));
			this.receiverId = ((Double) params.get("receiverId")).longValue();
			this.readStatus = ReadStatusCode.valueOf((String) params
					.get("readStatus"));
			this.handleStatus = HandleStatusCode.valueOf((String) params
					.get("handleStatus"));
			this.createTime = (String) params.get("createTime");
		}
	}

	public MessageData() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NotifyTypeCode getMsgType() {
		return msgType;
	}

	public void setMsgType(NotifyTypeCode msgType) {
		this.msgType = msgType;
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

	public UserData getSender() {
		return sender;
	}

	public void setSender(UserData sender) {
		this.sender = sender;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public UserData getReceiver() {
		return receiver;
	}

	public void setReceiver(UserData receiver) {
		this.receiver = receiver;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
