package com.eyunda.third.activities.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.adapters.CommonAdapter;
import com.eyunda.third.adapters.ViewHolder;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.enumeric.FeeItemCode;
import com.eyunda.third.domain.enumeric.PlantBank;
import com.eyunda.third.domain.wallet.UserBankData;
import com.eyunda.third.domain.wallet.WalletData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.CustomAlertDialog;
import com.eyunda.tools.CustomPasswordDialog;
import com.eyunda.tools.StringUtil;
import com.eyunda.tools.log.Log;
import com.hy.client.R;
import com.pingan.bank.libs.fundverify.Common;
import com.ta.util.http.AsyncHttpResponseHandler;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 通用支付页面
 */
public class GeneralPayActivity extends CommonActivity implements OnClickListener {
	Data_loader dataLoader;
	// private long timeout = 5000;
	String title, valueId, value, type, postParmas, remark;
	String netpayurl, nobindpayurl,returnurl,notifyurl;
	Boolean hasWalletId;
	int suretyDays;
	FeeItemCode fic;
	WalletData walletData;
	
	EditText passwd,vcode;
	
	
	//private ProgressDialog dialog;
	@Bind(R.id.orderNo) TextView orderNo;
	@Bind(R.id.orderNum) TextView orderNum;
	@Bind(R.id.orderDesc) TextView orderDesc;
	@Bind(R.id.walletPay) Button walletPay;
	@Bind(R.id.nonBandingPay) Button nonBandingPay;
	@Bind(R.id.bandingPay) Button bandingPay;
	@Bind(R.id.bandCardPay) Button bandCardPay;
	
	private static final int REQUEST_CODE_BANK = 1;
	Dialog dialog = null;
	CustomAlertDialog.Builder customBuilder = null;
	CustomPasswordDialog pwdDialog = null;
	private ArrayList<SpinnerItem> dataList,bindBanks;
	private CommonAdapter<SpinnerItem> smpAdapter,bindBankAdapter;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_general_pay);
		ButterKnife.bind(this);
		dataLoader = new Data_loader();
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		valueId = intent.getStringExtra("valueId");
		value = intent.getStringExtra("value");
		type = intent.getStringExtra("type");
		hasWalletId = intent.getBooleanExtra("hasWalletId", false);
		suretyDays= intent.getIntExtra("suretyDays", 0);
		if(intent.getStringExtra("remark") != null){
			remark = intent.getStringExtra("remark");
		}else{
			remark = "";
		}
		fic = FeeItemCode.valueOf(type);
		postParmas = "";
		customBuilder = new CustomAlertDialog.Builder(GeneralPayActivity.this);
		disableBtns();
	}

	private void disableBtns(){
		nonBandingPay.setClickable(false);
	}
	private void enableBtns(){
		nonBandingPay.setClickable(true);
	}
	@Override
	protected void onStart() {
		super.onStart();
		setTitle(title);
		top_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goBackToMyWallet();

			}
		});
		loadData();
		initBankLists();
		initBandBanks();
	}


	private void initBankLists(){
		dataList = new ArrayList<SpinnerItem>();
		for(PlantBank p: PlantBank.values()){
			SpinnerItem si = new SpinnerItem(p.name(), p.getDescription(), p.getCode());
			dataList.add(si);
		}
		smpAdapter = new CommonAdapter<SpinnerItem>(GeneralPayActivity.this,dataList,R.layout.eyd_popup_item) {

			@Override
			public void convert(ViewHolder helper, SpinnerItem item) {

				helper.setText(R.id.spinnerValue, item.getValue());
				helper.setText(R.id.spinnerId, item.getId());
				helper.setText(R.id.spinnerCid, item.getData());
			}
		};
    }
	//初始化已绑定银行卡列表
    private void initBandBanks() {
    	bindBanks = new ArrayList<SpinnerItem>();
    	bindBankAdapter =  new CommonAdapter<SpinnerItem>(GeneralPayActivity.this,bindBanks,R.layout.eyd_chat_row_contact) {
			@Override
			public void convert(ViewHolder helper, SpinnerItem item) {
				helper.setImageByUrl(R.id.avatar, ApplicationConstants.IMAGE_URL+item.getValue());
				helper.setText(R.id.name, item.getData());
				helper.setText(R.id.signature, item.getFlag());
			}
		};
	}
	// 回到钱包列表
	protected void goBackToMyWallet() {
		startActivity(new Intent(getApplicationContext(), WalletHomeActivity.class));
		finish();

	}

	@Override
	public void onClick(View v) {
	}

	private void loadData() {
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("feeItem", fic);
		if(hasWalletId){
			params.put("walletId", Long.parseLong(valueId));
			params.put("orderId", 0L);
			params.put("payMoney", 0.00D);
			params.put("suretyDays", 0);
		}else{
			switch (fic) {
			case prefee://预付
				params.put("walletId", 0L);
				params.put("orderId", Long.parseLong(valueId));
				params.put("payMoney", 0.00D);
				params.put("suretyDays", 0);
				break;

			case inaccount:
				
				params.put("walletId", 0L);
				params.put("orderId", 0L);
				params.put("payMoney", Double.parseDouble(value));
				params.put("suretyDays", 0);
				break;
			case face:
				params.put("walletId", 0L);
				params.put("orderId", Long.parseLong(valueId));
				params.put("payMoney", Double.parseDouble(value));
				params.put("suretyDays", suretyDays);
				params.put("remark", remark);
				break;
			default:
				
				break;
			}
		}
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "获取信息中...");
			}
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equals("Success")) {
					Map<String, Object> content = (HashMap<String, Object>)cd.getContent();
					netpayurl = (String)content.get("netpayurl");
					nobindpayurl = (String)content.get("nobindpayurl");
					returnurl = (String)content.get("returnurl");
					notifyurl = (String)content.get("notifyurl");
					Map<String, Object> walletDataMap = (HashMap<String, Object>) content.get("walletData");
					walletData = new WalletData(walletDataMap);
					fic = walletData.getFeeItem();
					enableBtns();
					initView();
					getBindBanks();
				} else {
					Toast.makeText(GeneralPayActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}
			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(GeneralPayActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(GeneralPayActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(GeneralPayActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(GeneralPayActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}
		};
		dataLoader.getApiResult(handler, "/mobile/pinganpay/payAction", params, "post");
	}
	
	private void initView(){
		if(walletData != null){
			orderNo.setText(walletData.getPaymentNo());
			orderNum.setText(walletData.getTotalFee().toString());
			orderDesc.setText(walletData.getBody());
		}
		if(fic.equals(FeeItemCode.inaccount)){
			walletPay.setVisibility(View.GONE);
		}else{
			walletPay.setVisibility(View.VISIBLE);
		}
		//TODO:检查是否已经绑定过卡，如果绑定过则只显示使用绑定卡支付，否则只显示非绑定卡支付
	}

	@OnClick(R.id.nonBandingPay)
	public void nonBandingPayClick(){
		startActivity(new Intent(getApplicationContext(),WebPayActivity.class)
		.putExtra("params", "")
		.putExtra("method", "get")
		.putExtra("url", ApplicationConstants.SERVER_URL+"/mobile/pinganpay/autoJump/?type=1&walletId="+walletData.getId()+"&feeItem="+fic.name())
		.putExtra("title", "易运达")
		);

		
	}
	@OnClick(R.id.bandingPay)
	public void bandingPayClick(){
		//选择支付银行
		showBanks();
		//startActivity(new Intent(this,BankListActivity.class).putExtra("flag", BankListActivity.BINDED_BANK_CARD).putExtra(BankListActivity.BINDED_BANK_CARD_INCLUDE_EYUNDA, false));
	}
	//显示备选银行列表
	private void showBanks() {
		dialog = null;
		customBuilder.setTitle("选择银行").setAdapter(smpAdapter, new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SpinnerItem si = smpAdapter.getItem(position);
				dialog.dismiss();
				startActivity(new Intent(getApplicationContext(),WebPayActivity.class)
						.putExtra("params", "")
						.putExtra("method", "get")
						.putExtra("url", ApplicationConstants.SERVER_URL+"/mobile/pinganpay/autoJump/?type=2&walletId="+walletData.getId()+"&plantBankId="+si.getData()+"&feeItem="+fic.name())
						.putExtra("title", "易运达")
				);
				
			}
		});
		dialog = customBuilder.create();
		dialog.show();

	}

	@OnClick(R.id.bandCardPay)
	public void bandCardPayClick(){
		showBindBanks();
	}
	private void showBindBanks(){
		dialog = null;
		customBuilder.setTitle("选择银行").setAdapter(bindBankAdapter, new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SpinnerItem si = bindBankAdapter.getItem(position);
				//Toast.makeText(getApplicationContext(), si.toString()+si.getCid()+si.getData()+si.getId()+si.getValue()+si.getFlag(), Toast.LENGTH_LONG).show();
				dialog.dismiss();
				startActivity(new Intent(GeneralPayActivity.this, ConfirmPayActivity.class)
						.putExtra("orderNoVal", walletData.getPaymentNo())
						.putExtra("orderNumVal", walletData.getTotalFee().toString())
						.putExtra("orderDescVal", walletData.getBody())
						.putExtra("payTypeVal", si.getData()+"("+StringUtil.shortAccountStr(si.getFlag(),6)+")")
						.putExtra("type", fic.name())
						.putExtra("walletId", walletData.getId())
						.putExtra("source", 2)
						.putExtra("bindId", si.getId())
						);
				
			}
		});
		dialog = customBuilder.create();
		dialog.show();
	}
	@OnClick(R.id.walletPay)
	public void walletPay(){
		startActivity(new Intent(GeneralPayActivity.this, ConfirmPayActivity.class)
				.putExtra("orderNoVal", walletData.getPaymentNo())
				.putExtra("orderNumVal", walletData.getTotalFee().toString())
				.putExtra("orderDescVal", walletData.getBody())
				.putExtra("payTypeVal", "易运达钱包")
				.putExtra("type", fic.name())
				.putExtra("walletId", walletData.getId())
				.putExtra("source", 1)
				.putExtra("bindId", "")
				);
	}


	protected void getBindBanks() {
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("walletId", walletData.getId());
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				ConvertData cd = new ConvertData(arg0,"/mobile/pinganpay/getBindCards", params);
				if (cd.getReturnCode().equals("Success")) {
					List content = (ArrayList<UserBankData>) cd.getContent();

					int size = content!=null?content.size():0;
					if (size > 0) {
						for (int i = 0; i < size; i++) {
							UserBankData data = new UserBankData((Map<String, Object>) content.get(i));
							SpinnerItem s = new SpinnerItem();
							s.setId(data.getBindId());
							s.setData(data.getBankName());
							s.setValue(data.getBankCode().getIcon());
							s.setCid(data.getId().toString());
							s.setFlag("账号后四位("+data.getCardNo()+")");
							
							bindBanks.add(s);
						}
						showUseBindBankBtn(true);
					} else {
						showUseBindBankBtn(false);
					}
					bindBankAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(GeneralPayActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(GeneralPayActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(GeneralPayActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(GeneralPayActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(GeneralPayActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
		dataLoader.getApiResult(handler, "/mobile/pinganpay/getBindCards", params, "get");
	}
	/**
	 * 控制显示使用绑定卡支付还是新绑定并支付
	 * @param b
	 */
	protected void showUseBindBankBtn(boolean b) {
//		if(b){
//			bandCardPay.setVisibility(View.VISIBLE);
//			bandingPay.setVisibility(View.GONE);
//		}else{
//			bandCardPay.setVisibility(View.GONE);
//			bandingPay.setVisibility(View.VISIBLE);
//		}
		if(b){
			bandCardPay.setVisibility(View.VISIBLE);
			bandingPay.setVisibility(View.VISIBLE);
		}else{
			bandCardPay.setVisibility(View.GONE);
			bandingPay.setVisibility(View.VISIBLE);
		}
	}
	

}
