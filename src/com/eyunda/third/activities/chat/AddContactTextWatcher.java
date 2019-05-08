package com.eyunda.third.activities.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.order.OrderAddTYRAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.loaders.SynData_loader;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;


public class AddContactTextWatcher implements TextWatcher {

	private final AutoCompleteTextView mTextView;
	private final OrderAddTYRAdapter mAdapter;
	private CharSequence mLastSearchString;
	private static final int AUTOCOMPLETION_DELAY = 600; // in milliseconds
	private final CountDownTimer mTimer;
	private Context context;
	private String[] evilDummyResults = {};
	
	private HashMap<String,UserData> allUser;//保存本次请求的备选用户
	
	
	public AddContactTextWatcher(AutoCompleteTextView autoCompleteTextView,
			OrderAddTYRAdapter autoCompleteAdapter) {
		mTextView = autoCompleteTextView;
		mAdapter = autoCompleteAdapter;
		mTimer = getAutoCompletionTimer();
		context = GlobalApplication.getInstance();
		allUser = new HashMap<String,UserData>();
	}

//	AsyncHttpResponseHandler asynHandler = new AsyncHttpResponseHandler() {
//		@Override
//		public void onSuccess(String arg0) {
//			super.onSuccess(arg0);
//			ConvertData cd= new ConvertData(arg0);
//			if(cd.getReturnCode().equals("Success")){
//				List r = (ArrayList<UserData>)cd.getContent();
//				//List content = (ArrayList<String>)r.get("orderDatas");
//			}else{
//				Toast.makeText(context, cd.getMessage(), Toast.LENGTH_SHORT).show();
//			}
//		}
//		@Override
//		public void onFailure(Throwable arg0,String content) {
//			Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
//		};
//	};
	@SuppressWarnings("unchecked")
	private CountDownTimer getAutoCompletionTimer() {
		return new CountDownTimer(AUTOCOMPLETION_DELAY, AUTOCOMPLETION_DELAY) {
			@Override
			public void onTick(long l) {
			}

			@Override
			public void onFinish() {
				if (mLastSearchString.length() > 0) {
					try {
						// Do the API call here
						SynData_loader synDL = new SynData_loader();
						//
						Map<String, Object> apiParams = new HashMap<String, Object>();
						apiParams.put("keyWords", mLastSearchString);
						//调用查找托运人接口
						String arg0 = synDL.getApiResult("/mobile/contact/myContact/seek", apiParams);
						ConvertData cd= new ConvertData(arg0);
						synDL = null;
						allUser = new HashMap<String,UserData>();
						if(cd.getReturnCode().equals("Success")){
							
							List r = (ArrayList<UserData>)cd.getContent();
							List<String> list = new ArrayList<String>(); 
							for(int i=0; i<r.size();i++){
								Map<String,Object> tmp =  (HashMap<String,Object>)r.get(i);
								UserData u = new UserData(tmp);
								String uName = "";
								if(!u.getTrueName().equals("")){
									 uName = u.getTrueName();
								}else{
									uName = u.getLoginName();
								}
								allUser.put(uName, u);
								list.add(uName);
							}
							mAdapter.clear();
							evilDummyResults=list.toArray(new String[1]);
							if (evilDummyResults.length > 0) {
								for (String result : evilDummyResults) {
									mAdapter.add(result);
								}
							}
						}else{
							Toast.makeText(context, "找不到该用户", Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
		};
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i1,
			int i2) {
		// empty implementation because we dont need to do anything here
	}

	@Override
	public void onTextChanged(CharSequence currentSearchString, int start,
			int before, int count) {
		if (currentSearchString.length() < mTextView.getThreshold()) {
			cancelCountDownTimer();
			if (!mAdapter.isEmpty())
				mAdapter.clear();
			return;
		}

		mLastSearchString = currentSearchString;
		resetCountDownTimer();
	}

	@Override
	public void afterTextChanged(Editable editable) {
		// empty implementation because we dont need to do anything here
	}

	/**
	 * Resets the AutoCompletion Timer
	 */
	private void resetCountDownTimer() {
		cancelCountDownTimer();
		mTimer.start();
	}

	/**
	 * Cancels the AutoCompletions Timer
	 */
	private void cancelCountDownTimer() {
		mTimer.cancel();
	}
	
	//返回当前选择的user对象
	public UserData getSelectedUser(String uName){
		if(null !=allUser && allUser.size()>0){
			UserData u = allUser.get(uName);
			if(null != u)return u;
		}
		return null;
	}

}
