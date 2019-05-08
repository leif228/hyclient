package com.eyunda.third.adapters.chat.domain;

import java.io.Serializable;
import java.util.Calendar;

import com.eyunda.third.domain.enumeric.ReadStatusCode;
import com.google.gson.Gson;

public class ChatMessage implements Serializable{
	private static final long serialVersionUID = 6013572251564847381L;
	private Long id=0l;
	private String roomName="";
	private Long receiverId=0l;
	private Long senderId=0l;
	private String content="";
	private Calendar createTime=Calendar.getInstance();
	public ReadStatusCode readStatus=ReadStatusCode.noread;
	public Direct direct;
	public Status status;
	public  Type type;
	private boolean fstDel=false;//小号删除
	private boolean sndDel=false;//大号删除
	
	public boolean isAcked=true;//用于判断语音文件是否已读过
	public boolean isDelivered;
	
	public transient int progress = 0;
	

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isFstDel() {
		return fstDel;
	}

	public void setFstDel(boolean fstDel) {
		this.fstDel = fstDel;
	}

	public boolean isSndDel() {
		return sndDel;
	}

	public void setSndDel(boolean sndDel) {
		this.sndDel = sndDel;
	}


	public enum Direct {
		SEND, RECEIVE;
	}
	
	public enum Type {
		TXT,IMAGE,FILE,VOICE;
	}

	public enum Status {
		SUCCESS, FAIL, INPROGRESS, CREATE,SENDING;
	}

	public Direct getDirect() {
		return direct;
	}

	public void setDirect(Direct direct) {
		this.direct = direct;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean isAcked() {
		return isAcked;
	}

	public void setAcked(boolean flag) {
		isAcked = flag;
	}

	public boolean isDelivered() {
		return isDelivered;
	}

	public void setDelivered(boolean flag) {
		isDelivered = flag;
	}

	public ReadStatusCode getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(ReadStatusCode readStatus) {
		this.readStatus = readStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Calendar getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Calendar createTime) {
		this.createTime = createTime;
	}
	public String toJson() {
		Gson gson = new Gson();
		String json = gson.toJson(this);

		return json;
	}

}
