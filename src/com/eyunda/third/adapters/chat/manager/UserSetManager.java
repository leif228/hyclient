package com.eyunda.third.adapters.chat.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.util.Log;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.user.BasicInfoActivity;
import com.eyunda.third.domain.account.AccountData;
import com.eyunda.third.domain.account.OperatorData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.eyunda.third.locatedb.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ta.util.http.AsyncHttpResponseHandler;


public class UserSetManager {
	private static UserSetManager instance = null;

	private OperatorData operatorData = new OperatorData();
	private List<AccountData> accountDatas=new ArrayList<AccountData>();
	private String pinganAccountNo = "";
	private String syncMobile = "no";
	Data_loader data=new Data_loader();

	public static UserSetManager getInstance() {
		if (instance == null)
			instance = new UserSetManager();
		return instance;
	}
	
	public List<AccountData> getAccountDatas() {
		return accountDatas;
	}
	
	public void setAccountDatas(List<AccountData> accountDatas) {
		this.accountDatas = accountDatas;
	}

	public String getPinganAccountNo() {
		return pinganAccountNo;
	}

	public void setPinganAccountNo(String pinganAccountNo) {
		this.pinganAccountNo = pinganAccountNo;
	}

	public String getSyncMobile() {
		return syncMobile;
	}

	public void setSyncMobile(String syncMobile) {
		this.syncMobile = syncMobile;
	}

	public OperatorData getOperatorData() {
		return operatorData;
	}

	public void setOperatorData(OperatorData operatorData) {
		this.operatorData = operatorData;
	}
	
	public void delAccount(Long id){
		AccountData ad = null;
		for(AccountData a:accountDatas){
			if(a.getId().equals(id))
				ad = a;
		}
		if(ad!=null)
			accountDatas.remove(ad);
	}

	/** 异步去加载账户、申请为代理人信息 
	 * @param basicInfoActivity */
	public  void synLoadDate(final Activity basicInfoActivity) {
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();

			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String content) {
				try {
					Gson gson = new Gson();
					HashMap<String, Object> map = gson.fromJson((String) content,
							new TypeToken<Map<String, Object>>() {
							}.getType());

					if (map.get("returnCode").equals("Success")) {
						if (map.containsKey(ApplicationConstants.CONTENTMD5CHANGED)){
							boolean contentMD5Changed = (Boolean) map.get(ApplicationConstants.CONTENTMD5CHANGED);
							SharedPreferencesUtils s = new SharedPreferencesUtils("/mobile/account/myAccount", null);
							if(contentMD5Changed&&NetworkUtils.isNetworkAvailable()){
								s.setParam(content);
							}else{
								String localJsion = s.getParam();
								map = gson.fromJson(localJsion, new TypeToken<Map<String, Object>>() {
								}.getType());
							}
						} 
						
						Map<String, Object> contentMap=(Map<String, Object>) map.get("content");
						OperatorData operatorData=new OperatorData((Map<String, Object>) contentMap.get("operatorData"));
						UserData userData = new UserData( (Map<String, Object>) contentMap.get("userData"));
						
						setOperatorData(operatorData);
						setPinganAccountNo((String) contentMap.get("accountNo"));
						setSyncMobile((String) contentMap.get("syncMobile"));
						GlobalApplication.getInstance().getUserData().setRoles(userData.getRoles());
						GlobalApplication.getInstance().getUserData().setRoleCodes(userData.getRoleCodes());
						GlobalApplication.getInstance().getUserData().setRealUser(userData.isRealUser());
						GlobalApplication.getInstance().getUserData().setRoleDesc(userData.getRoleDesc());
						GlobalApplication.getInstance().getUserData().setShortRoleDesc(userData.getShortRoleDesc());
						GlobalApplication.getInstance().getUserData().setChildUser(userData.isChildUser());
						GlobalApplication.getInstance().getUserData().setMobile(userData.getMobile());
						GlobalApplication.getInstance().getUserData().setUnitName(userData.getUnitName());
						
						((BasicInfoActivity) basicInfoActivity).refresh();
					} else {
						Log.i("账户、申请为代理人信息", "异步加载失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
		};
		data.getApiResult(handler, "/mobile/account/myAccount");
	}
}
