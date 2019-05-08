package com.eyunda.main.localdata;

import java.util.List;

import android.app.Application;

import com.cat.entity.TestDataEntity;
import com.ta.TAApplication;
import com.ta.util.db.TASQLiteDatabase;

public class LocalData {
	private TASQLiteDatabase sqLiteDatabase;
	TAApplication application;
	private static LocalData db;

	private LocalData(TAApplication application) {
		if (sqLiteDatabase == null){
			sqLiteDatabase = application.getSQLiteDatabasePool()
					.getSQLiteDatabase();
			if (sqLiteDatabase != null)
			{
				sqLiteDatabase.creatTable(UserInfo.class);

			}
		
		}
	}

	public static LocalData get(TAApplication application) {

		if (db == null)
			db = new LocalData(application);
		return db;
	}
	
	public void addUser(UserInfo user){
		if(sqLiteDatabase.hasTable(UserInfo.class))
			sqLiteDatabase.dropTable(UserInfo.class);
			sqLiteDatabase.creatTable(UserInfo.class);

		 
		sqLiteDatabase.insert(user);
	}
	
	public String getUserHead(){
		List<UserInfo> list = sqLiteDatabase.query(UserInfo.class,
				false, null, null, null, null, null);
		for (int i = 0; i < list.size(); i++)
		{
			UserInfo user = list.get(i);
			 return user.getUserHead();
		}
		return null;
	}
	public Boolean getUserIfLogined(){
		List<UserInfo> list = sqLiteDatabase.query(UserInfo.class,
				false, null, null, null, null, null);
		for (int i = 0; i < list.size(); i++)
		{
			UserInfo user = list.get(i);
			 return user.getIfLogined();
		}
		return null;
	}
	
	public void setUserHead(String headString){
		List<UserInfo> list = sqLiteDatabase.query(UserInfo.class,
				false, null, null, null, null, null);
		UserInfo user = null;
		for (int i = 0; i < list.size(); i++)
		{
			  user = list.get(i);
			break;
		}
		 if(user!=null){
			 user.setUserHead(headString);
			 sqLiteDatabase.update(user);
		 }
		
		
	}
	public void setUserIfLogined(Boolean ifLogined){
		List<UserInfo> list = sqLiteDatabase.query(UserInfo.class,
				false, null, null, null, null, null);
		UserInfo user = null;
		for (int i = 0; i < list.size(); i++)
		{
			  user = list.get(i);
			break;
		}
		 if(user!=null){
			 user.setIfLogined(ifLogined);
			 sqLiteDatabase.update(user);
		 }
	}
	public UserInfo getUser(){
		List<UserInfo> list = sqLiteDatabase.query(UserInfo.class,
				false, null, null, null, null, null);
		for (int i = 0; i < list.size(); i++)
		{
			UserInfo user = list.get(i);
			 return user;
		}
		return null;
		
	}
	public void delUser(){
		if(sqLiteDatabase.hasTable(UserInfo.class))
			sqLiteDatabase.dropTable(UserInfo.class);
		
	}
	
	

}
