package com.eyunda.third.activities.account;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.domain.enumeric.FeeItemCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.log.Log;
import com.google.gson.Gson;
import com.google.gson.internal.bind.BigIntegerTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;
/**
 * 钱包 充值页面
 * @author
 */

public class FillMoneyActivity extends CommonActivity implements
OnClickListener{
	private Button next_btn;
	private EditText trueName, accountNo,  totalFee;
	private TextView balancetv, balance;
	private LinearLayout linear2;
	
	DialogUtil dialogUtil;
	ProgressDialog dialog;
	Data_loader data;
	
	String bankCodeNo = null;
	String bankCodeStr = null;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_wallet_fillfetch);
		dialogUtil = new DialogUtil(this);
		data = new Data_loader();
		
	}

	@Override
	public void onStart() {
		super.onStart();
		setTitle("充值");
	
		setview();
	}



	private void setview() {


		totalFee = (EditText) findViewById(R.id.totalFee);
		next_btn = (Button) findViewById(R.id.next_btn);

		balance = (TextView) findViewById(R.id.balance);
		balancetv = (TextView) findViewById(R.id.balancetv);
		linear2 = (LinearLayout)findViewById(R.id.linear2);
		balance.setVisibility(View.GONE);
		balancetv.setVisibility(View.GONE);
		linear2.setVisibility(View.GONE);

		next_btn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next_btn:
			//createObservable();
			String params = totalFee.getText().toString().trim();
			if(TextUtils.isEmpty(params)){
				Toast.makeText(FillMoneyActivity.this, "请填写充值金额", 1).show();
				
			}else{
//				startActivity(new Intent(FillMoneyActivity.this, WebPayActivity.class)
////						.putExtra("orderId", 0L)
////						.putExtra("params", params)
////						.putExtra("ficStr", FeeItemCode.inaccount.name())
//						.putExtra("url", ApplicationConstants.SERVER_URL+"/mobile/pinganpay/orderPay/?walletId=0&orderId=0&feeItem="+FeeItemCode.inaccount+"&payMoney="+params)
//						.putExtra("title", "充值"));
				startActivity(new Intent(FillMoneyActivity.this, GeneralPayActivity.class).putExtra("title", "充值").putExtra("valueId", "0").putExtra("value", params).putExtra("type", FeeItemCode.inaccount.name()));
				finish();
				
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 	
	private Subscriber<String> subscriber;
	private Observable<String> observable;
	onCreate -> createSubscriber();
	onStart -> createObservable();
	private void createSubscriber(){
		subscriber = new Subscriber<String>() {

			@Override
			public void onCompleted() {
				
			}

			@Override
			public void onError(Throwable arg0) {
				
			}

			@Override
			public void onNext(String arg0) {
				
			}
		};
	}
	
	private void bindSubscriber(){
		observable.subscribe(subscriber);
	}
	private void createObservable(){
		observable = Observable.create(new OnSubscribe<String>() {

			@Override
			public void call(Subscriber<? super String> subscriber) {
				subscriber.onNext(nextStep());
				subscriber.onCompleted();
			}

		});
		bindSubscriber();
	}
	private String nextStep() {
		if(!TextUtils.isEmpty(totalFee.getText().toString().trim())){
			Toast.makeText(FillMoneyActivity.this, "请填写充值金额", 1).show();
			return "false";
		}else{
			return "success";
		}

	}
	**/
	
}
