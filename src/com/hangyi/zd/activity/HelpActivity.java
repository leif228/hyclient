package com.hangyi.zd.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.main.data.Data_loader;
import com.eyunda.third.activities.chat.ChatActivity;
import com.eyunda.third.activities.ship.ShipPreviewActivity;
import com.eyunda.third.adapters.chat.domain.User;
import com.hy.client.R;
/**
 *
 */
public class HelpActivity extends CommonListActivity implements
OnClickListener {
	Data_loader data;
	private WebView mWebView;   
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_order_template_preview);

		mWebView = (WebView) findViewById(R.id.webView);       
        WebSettings webSettings = mWebView.getSettings();       
        webSettings.setJavaScriptEnabled(true);       
        webSettings.setAllowFileAccess(true);
        mWebView.getSettings().setSupportZoom(true);

        //页面加载提示及超时处理
        mWebView.setWebViewClient(new WebViewClient() {
             //创建一个WebViewClient,重写onPageStarted和onPageFinished
             //onPageStarted中启动一个计时器,到达设置时间后利用handle发送消息给activity执行超时后的动作.
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //Log.d("testTimeout", "onPageStarted...........");
                super.onPageStarted(view, url, favicon);
                dialog = ProgressDialog.show(HelpActivity.this,null,"页面加载中，请稍候..");
//                timer = new Timer();
//                TimerTask tt = new TimerTask() {
//                    @Override
//                    public void run() {
//                         //超时后,首先判断页面加载进度,超时并且进度小于100,就执行超时后的动作
//                        if (OrderTemplatePreviewActivity.java.this.mWebView.getProgress() < 100) {
//                            //Log.d("testTimeout", "timeout...........");
//                            Message msg = new Message();
//                            msg.what = 1;
//                            mHandler.sendMessage(msg);//mhandler,去处理超时问题
//                            timer.cancel();
//                            timer.purge();
//                        }
//                    }
//                };
//                timer.schedule(tt, timeout, 1);
            }

            /**
             * onPageFinished指页面加载完成,完成后取消计时器
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dialog.dismiss();
                //Log.d("testTimeout", "onPageFinished...");
                //Log.d("testTimeout", "++"+ OrderTemplatePreviewActivity.java.this.mWebView.getProgress());
//                timer.cancel();
//                timer.purge();
            }
        });
        //mWebView.loadUrl("http://api.eyunda.com/appService/orderInfo/"+orderId);   
        mWebView.loadUrl("file:///android_asset/html/help.html");   
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("关于帮助");
//		setRight("关闭", new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
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
