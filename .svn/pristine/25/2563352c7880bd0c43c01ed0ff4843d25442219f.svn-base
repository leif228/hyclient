package com.eyunda.third.activities.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.util.EncodingUtils;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.adapters.order.MyTextWatcher;
import com.eyunda.third.domain.enumeric.FeeItemCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.third.locatedb.NetworkUtils;
import com.hy.client.R;

/**
 * 个人网站
 *
 */
public class WebPayActivity extends CommonActivity implements OnClickListener {
	Data_loader dataLoader;
	// private long timeout = 5000;
	String title, url, method, params;

	private WebView mWebView;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_web_pay);
		dataLoader = new Data_loader();
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		title = intent.getStringExtra("title");
		method = intent.getStringExtra("method");
		params = intent.getStringExtra("params");
	}

	private void init() {

		final ProgressBar bar = (ProgressBar) findViewById(R.id.myProgressBar);
		final ProgressBar lpb = (ProgressBar) findViewById(R.id.loadingPb);
		mWebView = (WebView) findViewById(R.id.webView);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setBlockNetworkImage(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setUseWideViewPort(true);

		// 缓存webView
		mWebView.getSettings().setAppCacheEnabled(false);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		mWebView.getSettings().setAppCachePath(Environment.getExternalStorageDirectory() + "/eyunda/cache");
		if (!NetworkUtils.isNetworkAvailable()) {
			mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}
		// 页面加载提示及超时处理
		mWebView.setWebViewClient(new WebViewClient() {

			// 创建一个WebViewClient,重写onPageStarted和onPageFinished
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				view.getSettings().setBlockNetworkImage(false);
				lpb.setVisibility(View.GONE);
				// 银行回调后，跳转到
				if (url.contains("/space/pinganpay/returnpage")) {
					// TODO:回跳到钱包
				}
			}

			// 忽略ssl错误提示
			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				handler.proceed();
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient() {
			// 进度条
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
		// Toast.makeText(getApplicationContext(), url,
		// Toast.LENGTH_LONG).show();
		// com.eyunda.tools.log.Log.d("url", url);
		String sessionId = "";
		if (GlobalApplication.getInstance().getUserData() != null) {
			sessionId = GlobalApplication.getInstance().getUserData().getSessionId();
			
			if("get".equalsIgnoreCase(method)){
				if(params.isEmpty()){
					mWebView.loadUrl(url + "&sessionId=" + sessionId);
				}else{
					mWebView.loadUrl(url + "?" + params + "&sessionId=" + sessionId);
				}
			}else{
				//mWebView.postUrl(url, params.getBytes());
				mWebView.postUrl(url, EncodingUtils.getBytes(params, "BASE64"));
			}
		} else {
			Toast.makeText(getApplicationContext(), "请先登录", 1).show();
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle(title);

		init();

		top_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goBackToMyWallet();

			}
		});
	}

	// 回到钱包列表
	protected void goBackToMyWallet() {
		startActivity(new Intent(getApplicationContext(), WalletHomeActivity.class));
		finish();

	}

	@Override
	public void onClick(View v) {
	}

}
