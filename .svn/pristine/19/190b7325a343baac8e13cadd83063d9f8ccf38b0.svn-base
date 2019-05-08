package com.eyunda.third.adapters.chat.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BaseChatRoom implements Serializable {
	private static final long serialVersionUID = 6013572251564847381L;
	private int unreadMsgCount = 0;
	private List<ChatMessage> messages = new ArrayList<ChatMessage>();// 对外使用的
	protected User toChatUser;
	private int rdpage = 1;
	private boolean isHaveNextPage=true;
	private boolean ifCleared = false;
	private String recentlyTitle; // 最近聊天主题
	private Calendar recentlyTime; // 最近聊天时间

	protected NotifyMessage notifyMsg;// 是否聊天室只是为通知消息

	// public ChatMessage getLastMsg(){
	// if(!messages.isEmpty()){
	// List<ChatMessage> list=getAllMessagesSort();
	// return list.get(list.size()-1);
	// }else{
	// return null;
	// }
	// }
	

//	public String getRecentlyTitle() {
//		List<ChatMessage> list = getAllMessagesSort();
//		if (!list.isEmpty())
//			return list.get(list.size() - 1).getContent();
//		else
//			return null;
//	}

//	public Calendar getRecentlyTime() {
//		List<ChatMessage> list = getAllMessagesSort();
//		if (!list.isEmpty())
//			return list.get(list.size() - 1).getCreateTime();
//		else
//			return null;
//	}

	
	public void setRecentlyTitle(String recentlyTitle) {
		this.recentlyTitle = recentlyTitle;
	}
	public String getRecentlyTitle() {
		return recentlyTitle;
	}
	public Calendar getRecentlyTime() {
		return recentlyTime;
	}
	public void setRecentlyTime(Calendar recentlyTime) {
		this.recentlyTime = recentlyTime;
	}

	public int getRdpage() {
		return rdpage;
	}

	public void setRdpage(int rdpage) {
		this.rdpage = rdpage;
	}
	

	public boolean isHaveNextPage() {
		return isHaveNextPage;
	}
	public void setHaveNextPage(boolean isHaveNextPage) {
		this.isHaveNextPage = isHaveNextPage;
	}
	public boolean isIfCleared() {
		return ifCleared;
	}

	public void setIfCleared(boolean ifCleared) {
		this.ifCleared = ifCleared;
	}

	public void addUnreadMsgCount() {
		unreadMsgCount++;
	}

	public NotifyMessage getNotifyMsg() {
		return notifyMsg;
	}

	public void setNotifyMsg(NotifyMessage notifyMsg) {
		this.notifyMsg = notifyMsg;
	}

	public List<ChatMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<ChatMessage> messages) {

		this.messages = messages;
	}

	public User getToChatUser() {
		return toChatUser;
	}

	public void setToChatUser(User toChatUser) {
		this.toChatUser = toChatUser;
	}

	public int getUnreadMsgCount() {
		return unreadMsgCount;
	}

	public void setUnreadMsgCount(int unreadMsgCount) {
		this.unreadMsgCount = unreadMsgCount;
	}

	public ChatMessage getMessage(int position) {
		return messages.get(position);
	}

	public void resetUnsetMsgCount() {
		setUnreadMsgCount(0);
	}

	public int getMsgCount() {
		return messages.size();
	}

	public void addMessage(ChatMessage msg) {

		messages.add(msg);
		//messages=getAllMessagesSort();
	}

	@SuppressWarnings("rawtypes")
	public void removeMessage(ChatMessage msg) {
		// 对外删除
		Iterator ite = messages.iterator();
		while (ite.hasNext()) {
			ChatMessage cmsg = (ChatMessage) ite.next();
			if (cmsg.getId().equals(msg.getId()))
				ite.remove();
		}

	}

	public void clearChatRoom() {
		messages.clear();
	}

	public List<ChatMessage> getAllMessagesSort() {

		Collections.sort(messages, new Comparator<ChatMessage>() {

			@Override
			public int compare(ChatMessage lhs, ChatMessage rhs) {
				return lhs.getCreateTime().compareTo(rhs.getCreateTime());
			}
		});
		return messages;
	}
}
