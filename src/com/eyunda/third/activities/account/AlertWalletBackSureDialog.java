package com.eyunda.third.activities.account;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.eyunda.third.domain.enumeric.SettleStyleCode;
import com.eyunda.third.domain.enumeric.EnumConst.BankCode;
import com.hy.client.R;

public class AlertWalletBackSureDialog {
	public static final String SUBMITSURE = "submitSure";
	public static final String RESULTSURE = "resultSure";
	private Activity context;
	private View view;
	private Builder ad;
	
	private SettleStyleCode settleStyle;
	private Map<String,Object> map;
	private String flag;
	
	@Bind(R.id.title) TextView title;
	@Bind(R.id.ll1) LinearLayout ll1;
	@Bind(R.id.key1) TextView key1;
	@Bind(R.id.value1) TextView value1;
	@Bind(R.id.ll2) LinearLayout ll2;
	@Bind(R.id.key2) TextView key2;
	@Bind(R.id.value2) TextView value2;
	@Bind(R.id.ll3) LinearLayout ll3;
	@Bind(R.id.key3) TextView key3;
	@Bind(R.id.value3) TextView value3;
	@Bind(R.id.ll4) LinearLayout ll4;
	@Bind(R.id.key4) TextView key4;
	@Bind(R.id.value4) TextView value4;
	@Bind(R.id.ll5) LinearLayout ll5;
	@Bind(R.id.key5) TextView key5;
	@Bind(R.id.value5) TextView value5;
	@Bind(R.id.ll6) LinearLayout ll6;
	@Bind(R.id.key6) TextView key6;
	@Bind(R.id.value6) TextView value6;

	public AlertWalletBackSureDialog(Activity context,SettleStyleCode settleStyle,Map<String, Object> map,String flag) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.search_alert_walletbacksure_item, null);
		ButterKnife.bind(this, view);
		
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		this.context = context;
		this.view = view;
		this.ad = ad;
		this.map = map;
		this.settleStyle = settleStyle;
		this.flag = flag;
		
		setview(view);
	}

	private void setview(View view) {
		
		switch (settleStyle) {
		case fill:
			title.setText(SettleStyleCode.fill.getDescription());
			key1.setText("充值银行:");
			value1.setText(BankCode.valueOf((String) map.get("bankCodeStr")).getDescription());
			ll2.setVisibility(View.VISIBLE);
			key2.setText("充值银行卡账号:");
			value2.setText((String) map.get("bankCodeNo"));
			ll3.setVisibility(View.VISIBLE);
			key3.setText("充值金额(元):");
			value3.setText((String) map.get("fillMoney"));
			
			break;
		case fetch:
			title.setText(SettleStyleCode.fetch.getDescription());
			key1.setText("提现到银行卡:");
			value1.setText(BankCode.valueOf((String) map.get("bankCodeStr")).getDescription());
			ll2.setVisibility(View.VISIBLE);
			key2.setText("提现到银行卡账号:");
			value2.setText((String) map.get("bankCodeNo"));
			ll3.setVisibility(View.VISIBLE);
			key3.setText("提现金额(元):");
			value3.setText((String) map.get("fetchMoney"));
			break;
		case turn:
			title.setText(SettleStyleCode.turn.getDescription());
			key1.setText("转账银行账户名:");
			value1.setText((String) map.get("accountName"));
			ll2.setVisibility(View.VISIBLE);
			key2.setText("转账银行卡账号:");
			value2.setText((String) map.get("cardNo"));
			ll3.setVisibility(View.VISIBLE);
			key3.setText("转账银行:");
			value3.setText(((BankCode )map.get("bankCode")).getDescription());
			ll4.setVisibility(View.VISIBLE);
			key4.setText("转账金额(元):");
			value4.setText((String) map.get("turnMoney"));
			
			ll5.setVisibility(View.VISIBLE);
			key5.setText("付款方式:");
			value5.setText(BankCode.valueOf((String) map.get("payBank")).getDescription());
			ll6.setVisibility(View.VISIBLE);
			key6.setText("付款账号:");
			value6.setText((String) map.get("payBankNo"));
			
			break;
		case pay:
			title.setText(SettleStyleCode.pay.getDescription());
			key1.setText("收款银行账户名:");
			value1.setText((String) map.get("accountName"));
			ll2.setVisibility(View.VISIBLE);
			key2.setText("收款银行卡账号:");
			value2.setText((String) map.get("cardNo"));
			ll3.setVisibility(View.VISIBLE);
			key3.setText("收款银行:");
			value3.setText((String )map.get("bankCode"));
			ll4.setVisibility(View.VISIBLE);
			key4.setText("付款金额(元):");
			value4.setText(((Double) map.get("payMoney")).toString());

			
			break;
//		case receive:
//			
//			break;
//		case deduct:
//			
//			break;

		default:
			break;
		}
	}


	public void showAddDialog(DialogInterface.OnClickListener lis) {
		
		if(SUBMITSURE.equals(flag)){
			ad.setTitle("确认处理").setView(view)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setNegativeButton("取消", null).setPositiveButton("确认", lis)
			.show();
			
		}else if(RESULTSURE.equals(flag)){
			switch (settleStyle) {
			case fill:
				ad.setTitle(SettleStyleCode.fill.getDescription()+"成功").setView(view)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("确认", lis)
				.show();
				break;
			case fetch:
				ad.setTitle(SettleStyleCode.fetch.getDescription()+"成功").setView(view)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("确认", lis)
				.show();
				break;
			case turn:
				ad.setTitle(SettleStyleCode.turn.getDescription()+"成功").setView(view)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("确认", lis)
				.show();
				break;
			case pay:
				ad.setTitle(SettleStyleCode.pay.getDescription()+"成功").setView(view)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("确认", lis)
				.show();
				break;
//			case receive:
//				
//				break;
//			case deduct:
//				
//				break;

			default:
				break;
			}
			
			
		}

	}
}
