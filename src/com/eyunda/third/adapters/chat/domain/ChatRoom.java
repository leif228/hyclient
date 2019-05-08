package com.eyunda.third.adapters.chat.domain;

import java.util.Calendar;

public class ChatRoom extends BaseChatRoom {
	private static final long serialVersionUID = 6013572251564847381L;
	private Long id;
	private String roomName;
	private Calendar createTime = Calendar.getInstance();
	private boolean fstDel;// 小号删除
	private boolean sndDel;// 大号删除
	
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

	public Calendar getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Calendar createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		if(notifyMsg==null){
			return toChatUser.getUserData().getNickName()==""?toChatUser.getUserData().getLoginName() : toChatUser.getUserData().getNickName();
		}else{
			return notifyMsg.getTitle();
		}
	}

}
