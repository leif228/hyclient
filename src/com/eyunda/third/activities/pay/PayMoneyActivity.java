package com.eyunda.third.activities.pay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.account.AlertWalletBackSureDialog;
import com.eyunda.third.activities.account.BankListActivity;
import com.eyunda.third.activities.account.ConfirmPayActivity;
import com.eyunda.third.activities.account.GeneralPayActivity;
import com.eyunda.third.adapters.order.OrderAddTYRAdapter;
import com.eyunda.third.adapters.order.WalletPayTextWatcher;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.account.AccountData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.EnumConst.BankCode;
import com.eyunda.third.domain.enumeric.FeeItemCode;
import com.eyunda.third.domain.wallet.UserBankData;
import com.eyunda.third.domain.wallet.WalletData;
import com.eyunda.third.domain.enumeric.SettleStyleCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.StringUtil;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
//付款
public class PayMoneyActivity extends CommonActivity {
	private static final int REQUEST_BACK = 1;
	private static final int CHANG_BANK = 2;
	private Data_loader mDataLoader;
	private ImageLoader mImageLoader;
	private DialogUtil mDialogUtil;
	private ProgressDialog mDialog;	
	private UserData currentUser;

	@Bind(R.id.sureDate) EditText sureDate;
	@Bind(R.id.descript) EditText descript;
	@Bind(R.id.btnSave) Button btnSave;
	@Bind(R.id.tansNum) EditText tansNum;
	@Bind(R.id.useSure) CheckBox useSure;
	@Bind(R.id.title_ll) LinearLayout title_ll;
	@Bind(R.id.title_ll_tv) TextView title_ll_tv;
	@Bind(R.id.receiveAccount) EditText receiveAccount;
	@Bind(R.id.loginName) AutoCompleteTextView loginName;
	

	private String rcvBankCode;


	
	private Long rcvUserId = 0l;
	private WalletPayTextWatcher walletPayTextWatcher;
	protected String rcvAccountName;
	protected String rcvCardNo;
	

	private List<SpinnerItem> bankList;

	int source = 1;
	private int sureDay = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());
		setContentView(R.layout.eyd_money_pay);
		ButterKnife.bind(this);
		

		currentUser = GlobalApplication.getInstance().getUserData();
		mDataLoader=new Data_loader();
		mImageLoader = ImageLoader.getInstance();
		mDialogUtil = new DialogUtil(this);
		
		autoSearch();
		//initData();
	}


	private void autoSearch() {
		OrderAddTYRAdapter autocompleteAdapter = new OrderAddTYRAdapter(this,R.layout.eyd_auto_user_item);
		walletPayTextWatcher = new WalletPayTextWatcher(loginName, autocompleteAdapter);
		loginName.setAdapter(autocompleteAdapter);
		loginName.addTextChangedListener(walletPayTextWatcher);
		loginName.setThreshold(1);
		loginName.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
								String info = ((OrderAddTYRAdapter) parent.getAdapter()).getItem(position);
								AccountData accountData = walletPayTextWatcher.getSelectedShip(info);
								receiveAccount.setEnabled(false);
								receiveAccount.setText("平安见证宝("+accountData.getAccounter()+":"+accountData.getAccountNo()+")");
								
								loginName.setText(accountData.getAccounter());
								
								rcvUserId = accountData.getUserId();
								rcvAccountName = accountData.getAccounter();
								rcvCardNo = accountData.getAccountNo();
								rcvBankCode = BankCode.EYUNDA.toString();
								
//								showBoatInfo(curSeclectShip);
//				CharSequence keyWords = (CharSequence) parent.getItemAtPosition(position);
			}
		});
	}



	@Override
	protected void onStart() {
		super.onStart();
		setTitle("支付");
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

	
	@OnCheckedChanged(R.id.useSure) void checkedChanged(boolean ch){ 
		if(ch){
			sureDate.setEnabled(true);
			sureDate.setFocusable(true);
		}else{
			sureDate.setText("0");
			sureDate.setEnabled(false);
		}
	}
	
	//保存
	@OnClick(R.id.btnSave) void translate(View v){
		final Map<String, Object> params = new HashMap<String, Object>();
		
		//直接付款
		if(!TextUtils.isEmpty(receiveAccount.getText().toString().trim()))
			params.put("loginName", loginName.getText().toString());
		else{
			Toast.makeText(PayMoneyActivity.this, "收款账户信息为空，请输入收款人登录名进行查找", 1).show();
			return;
		}
		
		//回显用
		params.put("accountName", rcvAccountName);//收款银行账户名
		params.put("cardNo", rcvCardNo);//收款银行卡账号
		params.put("bankCode", "平安见证宝");//收款银行
		
		//形成订单用参数
		params.put("walletId", 0L);
		params.put("orderId", rcvUserId);//收款方ID
		params.put("feeItem", FeeItemCode.face);

		if(!TextUtils.isEmpty(tansNum.getText().toString().trim())){
			double num = 0d;
			try {
				num = Double.valueOf(tansNum.getText().toString().trim());
			} catch (NumberFormatException e) {
				tansNum.setText("");
				Toast.makeText(PayMoneyActivity.this, "请正确输入金额", 1).show();
				return;
			}
			params.put("payMoney", num);
		}else{
			Toast.makeText(PayMoneyActivity.this, "请填写金额", 1).show();
			return;
		}
		params.put("remark", descript.getText().toString());
		
		if(useSure.isChecked()){
			String sd = sureDate.getText().toString();
			try {
				sureDay = Integer.valueOf(sd);
			} catch (NumberFormatException e) {
				sureDate.setText("");
				Toast.makeText(PayMoneyActivity.this, "资金托管期只能为1~30天", 1).show();
				return;
			}
			if(1<=sureDay&&sureDay<=30)
				params.put("suretyDay",sureDay);
			else{
				Toast.makeText(PayMoneyActivity.this, "资金托管期只能为1~30天", 1).show();
				return;
			}	
		}else{
			params.put("suretyDay",0);
		}

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "请稍候...");
			}

			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0,"/mobile/wallet/myWallet/pay", params);
				if (cd.getReturnCode().equals("Success")) {
					//成功后跳转到支付确认页面
					Map<String, Object> content = (HashMap<String, Object>)cd.getContent();
//					netpayurl = (String)content.get("netpayurl");
//					nobindpayurl = (String)content.get("nobindpayurl");
//					returnurl = (String)content.get("returnurl");
//					notifyurl = (String)content.get("notifyurl");
					Map<String, Object> walletDataMap = (HashMap<String, Object>) content.get("walletData");
					WalletData walletData = new WalletData(walletDataMap);

					startActivity(new Intent(PayMoneyActivity.this, GeneralPayActivity.class)
							.putExtra("title", "直接支付")
							.putExtra("valueId", walletData.getId().toString())
							.putExtra("value", "")
							.putExtra("hasWalletId", true)
							.putExtra("type", FeeItemCode.face.name())
							.putExtra("remark", walletData.getBody())
							.putExtra("suretyDays", sureDay)
							);
					finish();
					
				} else {
					Toast.makeText(PayMoneyActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(PayMoneyActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(PayMoneyActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(PayMoneyActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(PayMoneyActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
		

		submitSure(PayMoneyActivity.this, SettleStyleCode.pay, handler, "/mobile/pinganpay/payAction", params, "post");

	}


	
}