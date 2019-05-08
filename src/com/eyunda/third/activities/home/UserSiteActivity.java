package com.eyunda.third.activities.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.chat.ChatActivity;
import com.eyunda.third.adapters.chat.domain.User;
import com.eyunda.third.chat.event.LoginStatusCode;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.hy.client.R;
/**
 *  个人网站
 *
 */
public class UserSiteActivity extends CommonActivity implements
OnClickListener {
	Data_loader dataLoader;
	//private long timeout = 5000;
	private WebView mWebView;   

	private String loginName;
	
	protected	Button call_but, answer_but;
	protected LinearLayout liner_bg,liner_bg1,liner_bg2;//聊天和电话背景

	private User toChatUser;


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_site_preview);
		dataLoader = new Data_loader();
		toChatUser = (User) getIntent().getSerializableExtra("toChatUser");
		if(toChatUser!=null)
			loginName = toChatUser.getUserData().getLoginName();
		dataLoader = new Data_loader(); 
	}



	private void init() {
		answer_but = (Button) findViewById(R.id.answer_but);
		call_but = (Button) findViewById(R.id.call_but);
		answer_but.setOnClickListener(this);
		call_but.setOnClickListener(this);
		liner_bg2 = (LinearLayout) findViewById(R.id.liner_bg2); 
		liner_bg1 = (LinearLayout) findViewById(R.id.liner_bg1); 
		liner_bg = (LinearLayout) findViewById(R.id.liner_bg); 

		final ProgressBar bar = (ProgressBar)findViewById(R.id.myProgressBar);
		final ProgressBar lpb = (ProgressBar)findViewById(R.id.loadingPb);
		mWebView = (WebView) findViewById(R.id.webView);       
		mWebView.getSettings().setJavaScriptEnabled(true);      
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setBlockNetworkImage(true);  
		mWebView.getSettings().setBuiltInZoomControls(true); 
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setUseWideViewPort(true);  

		//缓存webView
		mWebView.getSettings().setAppCacheEnabled(false); 
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		mWebView.getSettings().setAppCachePath(Environment.getExternalStorageDirectory()+"/eyunda/cache");
		if(!NetworkUtils.isNetworkAvailable()){
			mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}
		//页面加载提示及超时处理
		mWebView.setWebViewClient(new WebViewClient() {

			//创建一个WebViewClient,重写onPageStarted和onPageFinished
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				view.getSettings().setBlockNetworkImage(false);
				lpb.setVisibility(View.GONE);
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient(){
			//进度条
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					bar.setVisibility(View.GONE);
				} else {
					if (View.GONE == bar.getVisibility()) {
						bar.setVisibility(View.VISIBLE);
					}
					bar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);

			}

		});
		String url = ApplicationConstants.HTTP_PRE+loginName+ApplicationConstants.SERVER_URL_SHORT+"/portal/site/index?c=GSJJ";
		Log.i("self", url);
		mWebView.loadUrl(url);  

	}


	@Override
	protected void onStart() {
		super.onStart();
		init();
		
		if(toChatUser!=null){
			setTitle(toChatUser.getUserData().getTrueName());
		}else{
			Toast.makeText(getApplicationContext(),"取不到用户信息!", Toast.LENGTH_SHORT).show();
			liner_bg1.setBackgroundColor(0xff696969);
			answer_but.setOnClickListener(null);
			liner_bg.setBackgroundColor(0xff696969);
			call_but.setOnClickListener(null);
		}
		/*
		setRightBtn(R.drawable.ico_cargo, new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginStatusCode isLogin = GlobalApplication.getInstance().getLoginStatus();
				UserData userData=GlobalApplication.getInstance().getUserData();
				
				if (isLogin.equals(LoginStatusCode.logined)) {
					if (userData != null && !userData.isChildUser()) {

						Intent intent = new Intent(UserSiteActivity.this,AddCargoActivity.class);
						Long uid = toChatUser.getUserData().getId();
						intent.putExtra("brokerId",toChatUser.getUserData().getId());
						intent.putExtra("orderId", "0");
						intent.putExtra("type",2L);//对人说我有货要运
						startActivity(intent);

					} else {
						Toast.makeText(getApplicationContext(),
								"子账号无操作此项的权限!", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "请登录后再试",
							Toast.LENGTH_SHORT).show();

				}
			}
		});*/
		top_commit.setVisibility(View.GONE);
		
	}
	
	@Override
	public void onClick(View v) {	
		UserData ud = GlobalApplication.getInstance().getUserData();
//		if(ud!=null&&ud.isChildUser()){
//			Toast.makeText(getApplicationContext(), "子帐号不能操作!", Toast.LENGTH_SHORT).show();
//			return ;
//		}

		switch (v.getId()) {
		case R.id.call_but://打电话
			String mobile = toChatUser.getUserData().getMobile();
			if(!"".equals(mobile)){
				Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ toChatUser.getUserData().getMobile()));
				startActivity(phoneIntent);
			}else
				Toast.makeText(getApplicationContext(), "用户没有设置电话！",
						Toast.LENGTH_LONG).show();
			break;

		case R.id.answer_but: //聊天
			LoginStatusCode isLogin = GlobalApplication.getInstance().getLoginStatus();
			UserData userData=GlobalApplication.getInstance().getUserData();
			
			if (isLogin.equals(LoginStatusCode.logined)){
				if(userData!=null&&!userData.getId().equals(toChatUser.getUserData().getId()))
					startActivity(new Intent(this,ChatActivity.class).putExtra("toChatUser", toChatUser));
				else
				Toast.makeText(getApplicationContext(), "不能与自己聊天",
							Toast.LENGTH_LONG).show();
			}else
				Toast.makeText(getApplicationContext(), "请登录后再试",
						Toast.LENGTH_LONG).show();
			break;
		}
	}

}
