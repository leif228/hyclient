package com.eyunda.third.activities.home;

import android.app.ProgressDialog;
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
import com.eyunda.third.activities.cargo.AddCargoActivity;
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
public class WebSiteActivity extends CommonActivity implements
OnClickListener {
	Data_loader dataLoader;
	//private long timeout = 5000;

	private WebView mWebView;   
	private ProgressDialog dialog;
	private String loginName,url,title;
	
	protected	Button call_but, answer_but;
	protected LinearLayout liner_bg,liner_bg1;//聊天和电话背景
	


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_site_preview);
		dataLoader = new Data_loader();
		Intent intent = getIntent();
		url  = intent.getStringExtra("url");
		//title  = intent.getStringExtra("title");
		title="通知公告";
	}



	private void init() {
		answer_but = (Button) findViewById(R.id.answer_but);
		call_but = (Button) findViewById(R.id.call_but);
		answer_but.setOnClickListener(this);
		call_but.setOnClickListener(this);
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
				
		mWebView.loadUrl(url);  

	}


	@Override
	protected void onStart() {
		super.onStart();
		init();
		
		
		liner_bg1.setBackgroundColor(0xff696969);
		liner_bg1.setVisibility(View.GONE);
		answer_but.setOnClickListener(null);
		liner_bg.setBackgroundColor(0xff696969);
		call_but.setOnClickListener(null);
		liner_bg.setVisibility(View.GONE);
		
//		setRightBtn(R.drawable.ico_cargo, new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				LoginStatusCode isLogin = GlobalApplication.getInstance().getLoginStatus();
//				UserData userData=GlobalApplication.getInstance().getUserData();
//				
//			}
//		});
		top_commit.setVisibility(View.GONE);
		setTitle(title);
	}
	
	@Override
	public void onClick(View v) {	
	}

}
