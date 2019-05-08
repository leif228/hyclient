package com.eyunda.third.activities.pay;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.account.AlertWalletBackSureDialog;
import com.eyunda.third.activities.account.BankListActivity;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.SettleStyleCode;
import com.eyunda.third.domain.enumeric.EnumConst.BankCode;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;
//转账
public class TranslateActivity extends CommonActivity {

	private static final int REQUEST_BACK = 1;
	private static final int CHANG_BANK = 2;
	private Data_loader mDataLoader;
	private ImageLoader mImageLoader;
	private DialogUtil mDialogUtil;
	private ProgressDialog mDialog;

	
	private UserData currentUser;
	@Bind(R.id.btnSave) Button btnSave;
	@Bind(R.id.cardNo) EditText cardNo;
	@Bind(R.id.tansNum) EditText tansNum;
	@Bind(R.id.bankId) TextView bankId;

	@Bind(R.id.bankName) TextView bankName;

	@Bind(R.id.userName) EditText userName;
	@Bind(R.id.addEnd) Button addEnd;
	@Bind(R.id.selectBank) RelativeLayout selectBank;
	@Bind(R.id.descript) EditText descript;
	
	@Bind(R.id.chang) TextView chang;
	@Bind(R.id.changBankName) TextView changBankName;
	private String changBankCodeStr = "EYUNDA";//默认我的钱包支付
	private String changBankCodeNo = "88888";//默认我的钱包帐号
	private String bankCode;
	private String changUserBankId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_money_translate);
		ButterKnife.bind(this);
		Intent intent = getIntent();
		currentUser = GlobalApplication.getInstance().getUserData();
		
		mDataLoader=new Data_loader();
		mImageLoader = ImageLoader.getInstance();
		mDialogUtil = new DialogUtil(this);
		
		initData();
	}
	

	private void initData(){
		
	}
	
	private void initView(){
		
	}

	
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("转账");
	}
	
	private void submitSure(Activity context,SettleStyleCode settleStyle,final AsyncHttpResponseHandler handler,final String url ,final Map<String, Object> params,final String getPost){
		AlertWalletBackSureDialog ad = new AlertWalletBackSureDialog(context,settleStyle,params,AlertWalletBackSureDialog.SUBMITSURE);
		ad.showAddDialog(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mDataLoader.getApiResult(handler, url, params, getPost);
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

	//更换付款银行
	@OnClick(R.id.chang) void changBank(View v){
		Intent i = new Intent(getApplicationContext(),BankListActivity.class);
		i.putExtra("flag", BankListActivity.BINDED_BANK_CARD);
		startActivityForResult(i, CHANG_BANK);
	}
	//保存
	@OnClick(R.id.btnSave) void translate(View v){
		final Map<String, Object> params = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "请稍候...");
			}

			@SuppressWarnings({ "unchecked"})
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0,"/mobile/wallet/myWallet/turn", params);
				if (cd.getReturnCode().equals("Success")) {
					resultSure(TranslateActivity.this,SettleStyleCode.turn,params);
//					Toast.makeText(TranslateActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
//					finish();
				} else {
					Toast.makeText(TranslateActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(TranslateActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(TranslateActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(TranslateActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(TranslateActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
		if(!TextUtils.isEmpty(userName.getText().toString().trim()))
			params.put("accountName", userName.getText().toString());
		else{
			Toast.makeText(TranslateActivity.this, "请填写姓名", 1).show();
			return;
		}
		if(!TextUtils.isEmpty(cardNo.getText().toString().trim()))
			params.put("cardNo", cardNo.getText().toString());
		else{
			Toast.makeText(TranslateActivity.this, "请填写卡号", 1).show();
			return;
		}
		if(bankCode!=null)
			params.put("bankCode", BankCode.valueOf(bankCode));
		else{
			Toast.makeText(TranslateActivity.this, "请选择银行", 1).show();
			return;
		}
		if(!TextUtils.isEmpty(tansNum.getText().toString().trim()))
			params.put("turnMoney", tansNum.getText().toString());
		else{
			Toast.makeText(TranslateActivity.this, "请填写金额", 1).show();
			return;
		}
		params.put("remark", descript.getText().toString());
		
		if(changUserBankId!=null)
			params.put("payUserBankId", Long.valueOf(changUserBankId) );
			
		params.put("payBank", changBankCodeStr );//回显用
		params.put("payBankNo", changBankCodeNo );//回显用
		
		submitSure(TranslateActivity.this, SettleStyleCode.turn, handler, "/mobile/wallet/myWallet/turn", params, "post");
//		mDataLoader.getApiResult(handler, "/mobile/wallet/myWallet/turn", params, "post");
	}
	//选择银行
	@OnClick({R.id.selectBank,R.id.addEnd}) void selectBank(View v){
		Intent i = new Intent(getApplicationContext(),BankListActivity.class);
		startActivityForResult(i, REQUEST_BACK);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//选择银行
		if (requestCode == REQUEST_BACK && resultCode == RESULT_OK && null != data) {
			bankCode = data.getExtras().getString("bankCode");
			String cardName = BankCode.valueOf(bankCode).getDescription();
			bankId.setText(bankCode);
			bankName.setText(cardName);
		}
		//更换付款银行
		if (requestCode == CHANG_BANK && resultCode == RESULT_OK && null != data) {
			changBankCodeStr = data.getStringExtra("bankCode");
			changBankCodeNo = data.getStringExtra("codeNo");
			changUserBankId = data.getStringExtra("userBankId");
			
			BankCode bc = BankCode.valueOf(changBankCodeStr);
			
			String temp = "";
			if(changBankCodeNo!=null&&changBankCodeNo.length()>4){
				String b=changBankCodeNo.substring(changBankCodeNo.length()-4,changBankCodeNo.length()); 
				temp = bc.getDescription() +"-("+ b + ")";
			}else
				temp = bc.getDescription() +"-("+ changBankCodeNo + ")";
			
			changBankName.setText("使用"+temp+"付款，");
		}
	}
}
