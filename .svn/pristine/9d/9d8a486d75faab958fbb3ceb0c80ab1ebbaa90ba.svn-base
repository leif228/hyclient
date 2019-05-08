package com.eyunda.third.adapters.chat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.eyunda.third.adapters.chat.domain.ChatMessage;
import com.eyunda.third.adapters.chat.domain.ChatRoom;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.adapters.chat.manager.ChatRoomManager;
import com.eyunda.third.adapters.chat.manager.ContactManager;

public class DemoApplication extends Application {

	public static Context applicationContext;
	private static DemoApplication instance;
	public final String PREF_USERNAME = "username";
	private String userName = null;
	private static final String PREF_PWD = "pwd";
	private String password = null;
	private User currentUser;// 为登入用户

	@Override
	public void onCreate() {
		super.onCreate();

		applicationContext = this;
		instance = this;

	}

	public static DemoApplication getInstance() {
		return instance;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public String getUserName() {
		if (userName == null) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(applicationContext);
			userName = preferences.getString(PREF_USERNAME, null);
		}
		return userName;
	}

	public String getPassword() {
		if (password == null) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(applicationContext);
			password = preferences.getString(PREF_PWD, null);
		}
		return password;
	}

	public void setUserName(String username) {
		if (username != null) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString(PREF_USERNAME, username).commit()) {
				userName = username;
			}
		}
	}

	public void setPassword(String pwd) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(applicationContext);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putString(PREF_PWD, pwd).commit()) {
			password = pwd;
		}
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout() {
		// 先调用sdk logout，在清理app中自己的数据
		// EMChatManager.getInstance().logout();
		// DbOpenHelper.getInstance(applicationContext).closeDB();
		// reset password to null
		setPassword(null);
		ContactManager.getInstance().setContactList(null);

	}

}
