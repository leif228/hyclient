package com.eyunda.third.chat.utils;

import java.util.Date;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;

public class SIMCardInfo {
	private TelephonyManager telephonyManager;
	private static SIMCardInfo simCardInfo=null;
	private Context context;
	public static synchronized SIMCardInfo getInstance(Context context) {
		if (simCardInfo == null)
			simCardInfo = new SIMCardInfo(context);
		return simCardInfo;
	}

	public SIMCardInfo(Context context) {
		this.context = context;
		telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}
	

	// 获取当前设置的电话号码
	public String getPhoneNumber() {
		String phoneNumber = "0";
		phoneNumber = telephonyManager.getLine1Number();
		if (phoneNumber == null || "".equals(phoneNumber))
			phoneNumber = "0";

		return phoneNumber;
	}

	// 获取当前设置的sim号码
	public String getSimCardNumber() {
		String simNo = "";
		simNo = telephonyManager.getSubscriberId();
		if (simNo == null){
			SharedPreferences sp = context.getSharedPreferences("eyundaBindingCode", Context.MODE_PRIVATE);
			simNo = sp.getString("simNo", "");
			if("".equals(simNo)){
				Random r = new Random();
				simNo = "" + r.nextLong() + (new Date().getTime());
				if(simNo.length()>=20)
					simNo = simNo.substring(simNo.length()-20,simNo.length()); 
				
				Editor editor = sp.edit();
				editor.putString("simNo", simNo);
				editor.commit();
			}
		}
		return simNo;
	}
}
