package com.eyunda.part1.plughome;

import android.os.Bundle;

import com.eyunda.main.Config;
import com.eyunda.third.activities.PageHomeActivity;
import com.hy.client.R;

public class HomeActivity extends PageHomeActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (Config.IFLOGIN) {
			//用户已经登录，做相关处理
		} else {
			//未登录修改logo位置的文字
			login_name.setText("登录/注册");
			login_type.setText("登录后，推荐更多精彩");
			//设置默认头像
			userHead.setImageResource(R.drawable.login_nologin);
		}
		//调用船舶列表，动态展示到视图

	}


}
