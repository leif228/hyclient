package com.eyunda.third.adapters.chat.manager;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import android.text.TextUtils;

import com.eyunda.third.adapters.chat.domain.ChatRoom;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.adapters.chat.widget.HanziToPinyin;
import com.eyunda.third.domain.enumeric.ApplyStatusCode;

public class ContactManager {
	private static ContactManager instance = null;
	private Map<String, User> contactList=new HashMap<String,User>();

	public static synchronized ContactManager getInstance() {
		if (instance == null)
			instance = new ContactManager();
		return instance;
	}

	public void setContactList(Map<String, User> contactList) {
		this.contactList = contactList;
	} 

	public Map<String, User> getContactList() {
		return contactList;
	}

	public void deleteContact(User user) {
		contactList.remove(user.getUserData().getLoginName());
	}
	public void addContact(User user){
		contactList.put(user.getUserData().getLoginName(),user);
	}
	@SuppressWarnings("rawtypes")
	public User getContact(Long userId){
		Iterator iter = contactList.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			User user=(User) entry.getValue();
			if(user.getUserData().getId().equals(userId)){
				return user;
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public void friendContact(User friendUser) {
		Iterator iter = contactList.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			User user=(User) entry.getValue();
			if(user.getUserData().getId().equals(friendUser.getUserData().getId())){
				user.getUserData().setApplyStatus(ApplyStatusCode.approve);
			}
		}
	}
	
}
