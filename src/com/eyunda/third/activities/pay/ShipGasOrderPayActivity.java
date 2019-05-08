package com.eyunda.third.activities.pay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.eyunda.third.activities.ship.ShipGasOrderActivity;
import com.eyunda.tools.pay.PayEntity;
import com.hy.client.R;

/**
 * 支付页面
 * 
 * @author guoqiang
 *
 */
public class ShipGasOrderPayActivity extends CommonListActivity implements
		OnClickListener {
	Data_loader data;

	private WebView mWebView;
	private ProgressDialog dialog;

	final Activity activity = this;
	
	private String shipId="";
	private String shipName="";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_pay_view);

		Intent intent = this.getIntent();
		PayEntity pe = (PayEntity) intent.getSerializableExtra("payData");
		shipId = (String) intent.getStringExtra("shipId");
		shipName = (String) intent.getStringExtra("shipName");
		
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
				// dialog = ProgressDialog.show(PayActivity.this, null,
				// "页面加载中，请稍候..");
				Toast.makeText(ShipGasOrderPayActivity.this, "页面加载中，请稍候...",
						Toast.LENGTH_SHORT).show();
			}

			/**
			 * onPageFinished指页面加载完成,完成后取消计时器
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// dialog.dismiss();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		String sessionId = "";
		if (GlobalApplication.getInstance() != null) {
			sessionId = GlobalApplication.getInstance().getUserData()
					.getSessionId();
		}

		String sellerEmail = pe.getSeller();
		String orderNo = pe.getOrderNo();
		String orderDesc = pe.getBody();
		String totalFee = pe.getPrice();
			mWebView.loadUrl(ApplicationConstants.SERVER_URL
					+ "/payment/gasOrderPay/?sellerEmail=" + sellerEmail
					+ "&orderNo=" + orderNo + "&orderDesc=" + orderDesc
					+ "&totalFee=" + totalFee);
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("支付页面");
		// Toast.makeText(this, orderId+orderNum, Toast.LENGTH_LONG).show();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Intent intent = new Intent(ShipGasOrderPayActivity.this,ShipGasOrderActivity.class);
		intent.putExtra("shipId", shipId);
		intent.putExtra("shipName", shipName);
		startActivity(intent);
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
