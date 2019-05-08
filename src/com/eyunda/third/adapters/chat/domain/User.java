package com.eyunda.third.adapters.chat.domain;

import java.io.Serializable;

import android.text.TextUtils;

import com.eyunda.third.adapters.chat.widget.HanziToPinyin;
import com.eyunda.third.domain.account.UserData;

public class User implements Serializable {
	private static final long serialVersionUID = -1L;
	private UserData userData = null;
	private String header;

	public User() {
		userData = new UserData();
	}

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
		String headerName = null;
		if (!TextUtils.isEmpty(userData.getNickName())) 
			headerName = userData.getNickName();
		 else if(!TextUtils.isEmpty(userData.getTrueName()))
			headerName = userData.getTrueName();
		 else
			 headerName = userData.getLoginName();
			 
		
		if (!headerName.equals("")) {
			if (Character.isDigit(headerName.charAt(0))) {
				this.setHeader("#");
			} else if (headerName.charAt(0) >= 'A'
					&& headerName.charAt(0) <= 'Z') {
				this.setHeader(headerName.charAt(0) + "");
			} else if (headerName.charAt(0) >= 'a'
					&& headerName.charAt(0) <= 'z') {
				this.setHeader((headerName.charAt(0) + "").toUpperCase());
			} else {
				this.setHeader(HanziToPinyin.getInstance()
						.get(headerName.substring(0, 1)).get(0).target
						.substring(0, 1).toUpperCase());
				char header = this.getHeader().toLowerCase().charAt(0);
				if (header < 'a' || header > 'z') {
					this.setHeader("#");
				}
			}
		}

	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	@Override
	public String toString() {
		if (!userData.getNickName() .equals(""))
			return userData.getNickName();
		else if(!userData.getTrueName() .equals(""))
			return userData.getTrueName();
		else
			return userData.getLoginName();
			
	}
}
