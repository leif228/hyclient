package com.eyunda.third.activities.chat;

import android.app.Activity;

public class BaseActivity extends Activity{
	@Override
	protected void onResume() {
		super.onResume();
		//onresume时，取消notification显示
	//	EMChatManager.getInstance().activityResumed();
	}
}
