package com.eyunda.third.activities.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.enumeric.EnumConst.BankCode;
import com.eyunda.third.domain.wallet.CustAcctData;
import com.eyunda.third.domain.enumeric.SettleStyleCode;
import com.eyunda.third.domain.enumeric.YesNoCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.log.Log;
import com.hy.client.R;
import com.pingan.bank.libs.fundverify.Common;
import com.pingan.bank.libs.fundverify.FundVerifyBack;
import com.pingan.bank.libs.fundverify.PAFundVerify;
import com.ta.util.http.AsyncHttpResponseHandler;
/**
 * 钱包 充值、提现 页面
 * @author
 */

public class FillFetchActivity extends CommonListActivity implements
OnClickListener{
	private Button next_btn;
	private EditText trueName, accountNo,  totalFee;
	private TextView bankCode, bankCode_btn, balancetv, balance;
	
	private String paymentNo;
	private String serialNo;
	private String revMobilePhone;
	private String fetchBody;

	DialogUtil dialogUtil;
	ProgressDialog dialog;
	Data_loader data;
	
	String bankCodeNo = null;
	String bankCodeStr = null;
	private String flag = null;
	private String changUserBankId = null;
	
	private ProgressDialog mDialog;
	private String orderid; //第三方流水号
	private HashMap<String, Object> rparams;
	
	private Double totalTranOutAmount = 0.0;
	
	private static final int REQUEST_CODE_BANK = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_wallet_fillfetch);
		dialogUtil = new DialogUtil(this);
		data = new Data_loader();
		
		flag = getIntent().getStringExtra("flag");
		setview();
	}

	private void setview() {
//		trueName = (EditText) findViewById(R.id.trueName);
//		accountNo = (EditText) findViewById(R.id.accountNo);
		bankCode = (TextView) findViewById(R.id.bankCode);
		totalFee = (EditText) findViewById(R.id.totalFee);
		next_btn = (Button) findViewById(R.id.next_btn);
		bankCode_btn = (TextView) findViewById(R.id.bankCode_btn);
		balance = (TextView) findViewById(R.id.balance);
		balancetv = (TextView) findViewById(R.id.balancetv);
		if("fetch".equals(flag)){
			balance.setVisibility(View.VISIBLE);
			balancetv.setVisibility(View.VISIBLE);
		}else{
			balance.setVisibility(View.GONE);
			balancetv.setVisibility(View.GONE);
		}
		next_btn.setOnClickListener(this);
		bankCode_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next_btn:
			loadDate();
			
			break;
		case R.id.bankCode_btn:
			startActivityForResult(new Intent(this, BankListActivity.class)
				.putExtra("flag", BankListActivity.BINDED_BANK_CARD).putExtra(BankListActivity.BINDED_BANK_CARD_INCLUDE_EYUNDA, false),REQUEST_CODE_BANK);
			break;
		default:
			break;
		}
	}
	
	private void submitSure(Activity context,SettleStyleCode settleStyle,final AsyncHttpResponseHandler handler,final String url ,final Map<String, Object> params,final String getPost){
		AlertWalletBackSureDialog ad = new AlertWalletBackSureDialog(context,settleStyle,params,AlertWalletBackSureDialog.SUBMITSURE);
		ad.showAddDialog(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				data.getApiResult(handler, url, params, getPost);
			}
		});
	}
	private void resultSure(final Activity context,SettleStyleCode settleStyle,Map<String, Object> params){
		AlertWalletBackSureDialog ad = new AlertWalletBackSureDialog(context,settleStyle,params,AlertWalletBackSureDialog.RESULTSURE);
		ad.showAddDialog(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				context.finish();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_BANK) {
				bankCodeStr = data.getStringExtra("bankCode");
				bankCodeNo = data.getStringExtra("codeNo");
				changUserBankId = data.getStringExtra("userBankId");
				
				BankCode bc = BankCode.valueOf(bankCodeStr);
				bankCode_btn.setText("〉");
				
				String temp = "";
				if(bankCodeNo!=null&&bankCodeNo.length()>4){
					String b=bankCodeNo.substring(bankCodeNo.length()-4,bankCodeNo.length()); 
					temp = bc.getDescription() +"-("+ b + ")";
				}else
					temp = bc.getDescription() +"-("+ bankCodeNo + ")";
					
				bankCode.setText(temp);
			}
		}
		
		if (requestCode == Common.CODE_Verify) {
			if (resultCode == Common.Result_OK) {
				String sign = data.getStringExtra(Common.PAY_RESULT_BACK);
				Toast.makeText(FillFetchActivity.this, sign,Toast.LENGTH_LONG).show();
				
				//TODO 不确定返回结果是什么，待测试验证
				pinganValidatedPWGoApis(sign,orderid);
			} else if (resultCode == Common.Result_Failed) {
				String str = data.getStringExtra(Common.PAY_RESULT_BACK);
				Toast.makeText(FillFetchActivity.this, str,Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(FillFetchActivity.this, "取消",Toast.LENGTH_LONG).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void pinganValidatedPWGoApis(String sign, String orderid) {
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "请稍候...");
			}
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equals("Success")) {
					resultSure(FillFetchActivity.this, SettleStyleCode.fetch, rparams);
				} else {
					Toast.makeText(FillFetchActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}
			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(FillFetchActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(FillFetchActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(FillFetchActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(FillFetchActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}
		};
		params.put("sign", sign);
		params.put("orderid", orderid);
		data.getApiResult(handler, "/mobile/wallet/myWallet/pinganApi6005", params, "get");
	}

	@Override
	public void onStart() {
		super.onStart();
		if("fill".equals(flag))
			setTitle("充值");
		else if("fetch".equals(flag))
			setTitle("提现");
		
		loadBalance();
	}

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {

		rparams = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("提交中", "请稍候...");
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String content) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(content);

				if (cd.getReturnCode().equals("Success")) {
					if("fill".equals(flag)){
						
						resultSure(FillFetchActivity.this, SettleStyleCode.fill, rparams);
					}else if("fetch".equals(flag)){
//						resultSure(FillFetchActivity.this, SettleStyleCode.fetch, rparams);
						
						Map<String, Object> map = (Map<String, Object>) cd.getContent();
//						orderid = map.get("orderid");
//						showmDialog();
//						start(PAFundVerify.TYPE_DEFAULT,map);
						
						 paymentNo=(String)map.get("paymentNo");
						 serialNo=(String)map.get("serialNo");
						 revMobilePhone=(String)map.get("revMobilePhone");
						 fetchBody=(String)map.get("fetchBody");
						 
						 startActivity(new Intent(FillFetchActivity.this,BindBankCardNextActivity.class)
						                    .putExtra("paymentNo", paymentNo)
						                    .putExtra("serialNo", serialNo)
						                    .putExtra("revMobilePhone", revMobilePhone)
						                    .putExtra("fetchBody", fetchBody));
						 finish();
						
					}
				} else {
					Toast.makeText(FillFetchActivity.this, cd.getMessage(), 1).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Toast.makeText(FillFetchActivity.this, "信息提交失败", 1).show();
				dialog.dismiss();

			}

		};
//		if(bankCodeStr!=null)
//			params.put("bank", BankCode.valueOf(bankCodeStr));
//		else{
//			Toast.makeText(FillFetchActivity.this, "请选择银行", 1).show();
//			return;
//		}
//		params.put("bankCardNo", bankCodeNo);
		
		if(changUserBankId!=null){
			rparams.put("userBankId", Long.valueOf(changUserBankId));
			
			rparams.put("bankCodeStr", bankCodeStr);//回显用
			rparams.put("bankCodeNo", bankCodeNo);//回显用
		}
		else{
			Toast.makeText(FillFetchActivity.this, "请选择银行", 1).show();
			return;
		}
		
		if("fill".equals(flag)){
			if(!TextUtils.isEmpty(totalFee.getText().toString().trim()))
				rparams.put("fillMoney", totalFee.getText().toString());
			else{
				Toast.makeText(FillFetchActivity.this, "请填写充值金额", 1).show();
				return;
			}
			submitSure(FillFetchActivity.this,SettleStyleCode.fill,handler,"/mobile/wallet/myWallet/fill",rparams,"post");
//			data.getApiResult(handler, "/mobile/wallet/myWallet/fill", rparams);
			
		}else if("fetch".equals(flag)){
			if(!TextUtils.isEmpty(totalFee.getText().toString().trim()))
				rparams.put("fetchMoney", totalFee.getText().toString());
			else{
				Toast.makeText(FillFetchActivity.this, "请填写提现金额", 1).show();
				return;
			}
			double fee = 0.0;
			try {
				fee = Double.valueOf(totalFee.getText().toString().trim());
			} catch (Exception e) {
				Toast.makeText(FillFetchActivity.this, "请填写正确金额数！", 1).show();
				return ;
			}
			if(fee==0||fee>totalTranOutAmount){
				Toast.makeText(FillFetchActivity.this, "最多可提现金额"+totalTranOutAmount+"元！", 1).show();
				return;
			}
			submitSure(FillFetchActivity.this,SettleStyleCode.fetch,handler,"/mobile/wallet/myWallet/fetch",rparams,"post");
//			data.getApiResult(handler, "/mobile/wallet/myWallet/fetch", rparams);
			
		}
	}
	private void showmDialog(){
		mDialog = new ProgressDialog(FillFetchActivity.this);
		mDialog.setTitle("提示");
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(false);
		mDialog.setMessage("请稍候");
		try {
			mDialog.show();
		} catch (Exception e) {
		}
	}
	private void start(final int type, Map<String, String> map) {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
		PAFundVerify fundVerify = new PAFundVerify(FillFetchActivity.this, ApplicationConstants.APP_KEY,
				false);
		fundVerify.start(new FundVerifyBack() {

			@Override
			public void startCheck() {
				Log.d("Test", "startCheck");
			}

			@Override
			public void processCheck() {
				Log.d("Test", "processCheck");
			}

			@Override
			public void finishCheck() {
				Log.d("Test", "finishCheck");
			}

			@Override
			public void failedCheck(String error) {
				Log.d("Test", "failedCheck->" + error);
			}
		}, getFormData(type,map), type);

	}
	private HashMap<String, String> getFormData(int type, Map<String, String> map) {
		if (type == PAFundVerify.TYPE_DEFAULT) {
			String orig = String.format("%s&%s&", map.get("P2PCode"), map.get("custAccId"));
			map.put("orig", orig);
		}
		return (HashMap<String, String>) map;
	}
	
	//加载余额
	private void loadBalance() {
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(String arg0) {
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equals("Success")) {
						Map<String,Object> content =  (HashMap<String,Object>)cd.getContent();
						totalTranOutAmount = (Double)content.get("totalBalance");
						balance.setText(((Double)content.get("totalTranOutAmount")).toString());
				} else {
					Toast.makeText(FillFetchActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}
			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(FillFetchActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(FillFetchActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(FillFetchActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(FillFetchActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}
		};
		data.getApiResult(handler, "/mobile/wallet/getAccoutNum", params, "get");
	}
}
