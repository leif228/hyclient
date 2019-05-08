package com.eyunda.third.activities.oil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;

import com.eyunda.main.CommonListActivity;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.home.GasOrderInActivity;
import com.eyunda.third.activities.user.LoginActivity;
import com.eyunda.third.chat.event.LoginStatusCode;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.hy.client.R;

public class OilPreviewActivity extends CommonListActivity implements
OnClickListener {
	Data_loader dataLoader;	 
	protected	Button btnBuy;

	private WebView mWebView;
    String oilId ;
    String oilName;


    
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_oil_preview);
		Intent intent = getIntent(); 
		oilId = intent.getStringExtra("id");
		oilName = intent.getStringExtra("name");

		btnBuy = (Button) findViewById(R.id.btnBuy);
		
		btnBuy.setOnClickListener(this);

		

	
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
        mWebView.setWebViewClient(new WebViewClient() {
        	
            
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
		
       	 mWebView.loadUrl(ApplicationConstants.SERVER_URL+"/mobile/gas/gasWaresDetail?id="+oilId);   
       
	}

	final LoginStatusCode isLogin = GlobalApplication.getInstance().getLoginStatus();
	final UserData userData=GlobalApplication.getInstance().getUserData();

	@Override
	protected void onStart() {
		super.onStart();
		if(oilName!=null){
			setTitle(oilName);
		}

		if(userData!=null && isLogin.equals(LoginStatusCode.logined)){
			setRight("购买", new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplication(),GasOrderInActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("id", oilId);
				    intent.putExtras(bundle);
					startActivity(intent);
					
				}
			});
		}else{
			btnBuy.setText("登陆后购买");
			setRight("登陆", new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplication(),LoginActivity.class);
					startActivity(intent);
					finish();
				}
			});
		}

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnBuy: 	
			if(userData!=null && isLogin.equals(LoginStatusCode.logined)){
				Intent intent = new Intent(getApplication(),GasOrderInActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("id", oilId);
			    intent.putExtras(bundle);
				startActivity(intent);
			}else{
				Intent intent = new Intent(getApplication(),LoginActivity.class);
				startActivity(intent);
				finish();
			}
			break;
		}


	}

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub
		
	}




}
