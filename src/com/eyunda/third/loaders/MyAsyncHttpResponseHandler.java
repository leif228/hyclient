package com.eyunda.third.loaders;

import android.content.Context;
import android.content.Intent;

import com.eyunda.third.activities.ship.MyshipActivity;
import com.eyunda.third.activities.user.LoginActivity;
import com.ta.util.http.AsyncHttpResponseHandler;

public class MyAsyncHttpResponseHandler extends AsyncHttpResponseHandler {
	private Context mContext;
	public MyAsyncHttpResponseHandler(Context c){
		super();
		mContext = c;
	}
	@Override
	public void onSuccess(String s) {
		// TODO Auto-generated method stub
		super.onSuccess(s);
		if(s.contains("session已丢失,请重新登录!")){
			mContext.startActivity(new Intent(mContext.getApplicationContext(),LoginActivity.class));
		}
	
	}
}
