package com.eyunda.third.activities.order;


import java.io.File;
import java.util.List;

import org.apache.http.cookie.Cookie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.main.data.Data_loader;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.tools.pay.PayEntity;
import com.hangyi.tools.Util;
import com.hy.client.R;

/**
 * 支付页面
 * 
 * @author guoqiang
 *
 */
public class PayActivity extends CommonListActivity implements
		OnClickListener {
	Data_loader data;

	private WebView mWebView;
	private ProgressDialog dialog;

	final Activity activity = this;
	String orderId;


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_pay_view);

		Intent intent = this.getIntent(); 
		PayEntity pe=(PayEntity)intent.getSerializableExtra("payData");

		


		
		mWebView = (WebView) findViewById(R.id.webView);
		mWebView.requestFocusFromTouch();
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setSupportZoom(true);//支持缩放
		webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
		webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
//		webSettings.setPluginsEnabled(true);  //支持插件 
		// 缓存webView
		webSettings.setDomStorageEnabled(true); 
		webSettings.setAppCacheEnabled(true);
		webSettings.setDatabaseEnabled(true); 
		File f = new File(Environment.getExternalStorageDirectory()
				+ "/zd/cache");
		if (!f.exists()) {
			f.mkdir();
		}
		webSettings.setDatabasePath(f.getAbsolutePath());
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		webSettings.setAppCachePath(f.getAbsolutePath());

		// 页面加载提示及超时处理
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
//				dialog = ProgressDialog.show(PayActivity.this, null,
//						"页面加载中，请稍候..");
				Toast.makeText(PayActivity.this, "页面加载中，请稍候...", Toast.LENGTH_SHORT).show();
			}

			/**
			 * onPageFinished指页面加载完成,完成后取消计时器
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				//dialog.dismiss();
			}
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		String sessionId = "";
		if(GlobalApplication.getInstance() != null){
			sessionId = GlobalApplication.getInstance().getUserData().getSessionId();
		}
		
		String PHPSESSID = "";
        List<Cookie> list = GlobalApplication.getInstance().getCookies();
        for (Cookie cookie : list) {
            if (cookie.getName().equals("PHPSESSID")) {
            	PHPSESSID = cookie.getValue();
            }
        }
		
		mWebView.loadUrl(ApplicationConstants.ZDJAVA_PRE_URL
				+ "/client/monitor/mobHistory/?PHPSESSID=" + PHPSESSID);
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("历史回放");
		// Toast.makeText(this, orderId+orderNum, Toast.LENGTH_LONG).show();
	}

	@Override
	protected synchronized void loadDate() {

	}

	@Override
	public void onClick(View v) {


	}

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

}
