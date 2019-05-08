package com.eyunda.third.activities.order;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.main.data.Data_loader;
import com.eyunda.third.GlobalApplication;
import com.hy.client.R;

/**
 * 支付宝个人中心页面
 * 
 * @author guoqiang
 *
 */
public class AliUcActivity extends CommonListActivity implements
		OnClickListener {
	Data_loader data;

	private WebView mWebView;
	private ProgressDialog dialog;

	private MyCount mc;
	final Activity activity = this;
	String orderId;

	class MyCount extends CountDownTimer {   
		public MyCount(long millisInFuture, long countDownInterval) {   
			super(millisInFuture, countDownInterval);   
		}   
		@Override   
		public void onFinish() {   
			Toast.makeText(AliUcActivity.this, "close", Toast.LENGTH_SHORT).show();
			dialog.dismiss();
		}   
		@Override   
		public void onTick(long millisUntilFinished) {   
		}  
	}   

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_pay_view);
		mWebView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		mWebView.getSettings().setSupportZoom(true);

		// 页面加载提示及超时处理
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
					Toast.makeText(AliUcActivity.this, "页面加载中", Toast.LENGTH_SHORT).show();
					//dialog = ProgressDialog.show(AliUcActivity.this, null,"页面加载中，请稍候..");
			}

			/**
			 * onPageFinished指页面加载完成,完成后取消计时器
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				//mc.cancel();
				//dialog.dismiss();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		String sessionId = "";
		if (GlobalApplication.getInstance().getUserData() != null) {
			sessionId = GlobalApplication.getInstance().getUserData().getSessionId();
		}

		mWebView.loadUrl("https://my.alipay.com/portal/i.htm");
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("个人中心");
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
