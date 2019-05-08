package com.eyunda.third.locatedb;

import java.util.List;

import com.eyunda.third.adapters.chat.domain.BindingCode;
import com.ta.TAApplication;
import com.ta.util.db.TASQLiteDatabase;

public class EydLocalDB {

	private TASQLiteDatabase sqLiteDatabase;
	TAApplication application;
	private static EydLocalDB db;

	private EydLocalDB(TAApplication application) {
		if (sqLiteDatabase == null) 
			sqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
	}

	public static EydLocalDB get(TAApplication application) {

		if (db == null)
			db = new EydLocalDB(application);
		return db;
	}

	public void setBindingCode(String bindingCode) {
		BindingCode bc = new BindingCode();
		bc.setBindingCode(bindingCode);
		
		if (sqLiteDatabase.hasTable(BindingCode.class)){
			sqLiteDatabase.update(bc, null);
		}else{
			sqLiteDatabase.creatTable(BindingCode.class);
			sqLiteDatabase.update(bc, null);
		}
	}

	public String getBindingCode() {
		if (sqLiteDatabase.hasTable(BindingCode.class)){
			
			List<BindingCode> list = sqLiteDatabase.query(BindingCode.class, false, null, null,
					null, null, null);
			for (int i = 0; i < list.size(); i++) {
				BindingCode bindingCode = list.get(i);
				return bindingCode.getBindingCode();
			}
		}
		return null;
	}

}
