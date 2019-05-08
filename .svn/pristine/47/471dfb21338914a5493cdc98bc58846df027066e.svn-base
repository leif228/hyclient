package com.eyunda.third.domain.chat;

import java.util.Calendar;
import java.util.Map;

import com.eyunda.third.domain.BaseData;
import com.eyunda.third.domain.enumeric.ReadStatusCode;


public class ChatMessageData extends BaseData {

	private static final long serialVersionUID = -1L;
	private Long id=0l;
	private String roomName = ""; // 聊天室
	private Long receiverId = 0L; // 接收者ID
	private Long senderId = 0L; // 发送者ID
	private String content = ""; // 消息内容
	private ReadStatusCode readStatus = ReadStatusCode.noread; // 阅读状态
	private String createTime = String.valueOf(Calendar.getInstance().getTime().getTime());// 发送时间
	private Boolean fstDel = false; // 小号删除
	private Boolean sndDel = false; // 大号删除
	
	public ChatMessageData(){}
	public ChatMessageData(Map<String, Object> params) {
		super();
		if(params != null && !params.isEmpty()){
			this.id = ((Double) params.get("id")).longValue();
			this.roomName=(String) params.get("roomName");
			this.receiverId=((Double) params.get("receiverId")).longValue();
			this.senderId=((Double) params.get("senderId")).longValue();
			this.content=(String) params.get("content");
			this.readStatus = ReadStatusCode.valueOf((String) params.get("readStatus"));
			this.createTime=(String) params.get("createTime");
			this.fstDel=(Boolean) params.get("fstDel");
			this.sndDel=(Boolean) params.get("sndDel");
		}
		
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 取得聊天室
	 */
	public String getRoomName() {
		return roomName;
	}

	/**
	 * 设置聊天室
	 */
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	/**
	 * 取得接收者ID
	 */
	public Long getReceiverId() {
		return receiverId;
	}

	/**
	 * 设置接收者ID
	 */
	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	/**
	 * 取得发送者ID
	 */
	public Long getSenderId() {
		return senderId;
	}

	/**
	 * 设置发送者ID
	 */
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	/**
	 * 取得消息内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置消息内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	public ReadStatusCode getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(ReadStatusCode readStatus) {
		this.readStatus = readStatus;
	}



	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Boolean getFstDel() {
		return fstDel;
	}
	public void setFstDel(Boolean fstDel) {
		this.fstDel = fstDel;
	}
	public Boolean getSndDel() {
		return sndDel;
	}
	public void setSndDel(Boolean sndDel) {
		this.sndDel = sndDel;
	}



}
