package com.eyunda.third.activities.account;

import java.util.HashMap;
import java.util.Map;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.enumeric.FeeItemCode;
import com.eyunda.third.domain.wallet.WalletData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.CustomAlertDialog;
import com.eyunda.tools.CustomPasswordDialog;
import com.hy.client.R;
import com.jungly.gridpasswordview.GridPasswordView;
import com.ta.util.http.AsyncHttpResponseHandler;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 确认支付页面
 */
public class ConfirmPayActivity extends CommonActivity implements OnClickListener {
	Data_loader dataLoader;
	// private long timeout = 5000;
	String orderNoVal, orderNumVal, orderDescVal, payTypeVal,type,bindId;
	int source;//来源，钱包支付还是绑卡支付
	Long walletId;
	FeeItemCode fic;
	WalletData walletData;
	//private ProgressDialog dialog;
	@Bind(R.id.orderNo) TextView orderNo;
	@Bind(R.id.orderNum) TextView orderNum;
	@Bind(R.id.orderDesc) TextView orderDesc;
	@Bind(R.id.payType) TextView payType;
	@Bind(R.id.confirmPay) Button confirmPay;
	Button timeCount;

	private static final int REQUEST_CODE_BANK = 1;
	Dialog dialog = null;
	CustomAlertDialog.Builder customBuilder = null;
	EditText vcode;
	GridPasswordView passwd;
	CustomPasswordDialog pwdDialog = null;
	
	String sign = "";//保存申请验证码后返回的参数
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_confirm_pay);
		ButterKnife.bind(this);
		dataLoader = new Data_loader();
		Intent intent = getIntent();
		orderNoVal = intent.getStringExtra("orderNoVal");
		orderNumVal = intent.getStringExtra("orderNumVal");
		orderDescVal = intent.getStringExtra("orderDescVal");
		payTypeVal = intent.getStringExtra("payTypeVal"); //显示支付类型（钱包还是绑卡）
		type = intent.getStringExtra("type");
		source = intent.getIntExtra("source", 1);//1钱包，2绑卡
		walletId = intent.getLongExtra("walletId", 0L); 
		fic = FeeItemCode.valueOf(type);// 支付类型：预付，充值，结算付款等
		bindId = intent.getStringExtra("bindId");//绑定银行ID 绑卡支付用
		customBuilder = new CustomAlertDialog.Builder(ConfirmPayActivity.this);
		
	}

	
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("确认支付");
		top_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();

			}
		});
		
		initView();
	}


	
	
	private void initView() {
		orderNo.setText(orderNoVal);
		orderNum.setText(orderNumVal);
		orderDesc.setText(orderDescVal);
		payType.setText(payTypeVal);
		
	}


	// 回到钱包列表
	protected void goBackToMyWallet() {
		startActivity(new Intent(getApplicationContext(), WalletHomeActivity.class));
		finish();

	}

	@Override
	public void onClick(View v) {
	}

	
	@OnClick(R.id.confirmPay)
	public void confirmPayClick(){
		showPasswordDialog();
	}
	@OnClick(R.id.changType)
	public void changPayType(){
		finish();
	}


	private void showPasswordDialog(){
		pwdDialog = new CustomPasswordDialog(ConfirmPayActivity.this);
		vcode = (EditText) pwdDialog.getValidCodeView();
		pwdDialog.setOnPositiveListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//pwdDialog = null;
				//调用支付接口
				//指令码＋验证码+支付密码+walletId +type(钱包支付/非绑定支付)＋vCODe（非绑定对方返回的time/钱包支付对方返回的指令码）
				String pwd = pwdDialog.getPwd();
				String vcode = pwdDialog.getVCode();
				
				if(pwd.length() != 6){
					Toast.makeText(getApplicationContext(), "支付密码只能为6位！", Toast.LENGTH_SHORT).show();
				}else{
					if(vcode.isEmpty()){
						Toast.makeText(getApplicationContext(), "验证码不能为空！", Toast.LENGTH_SHORT).show();
					}else{
						if(sign.isEmpty()){
							Toast.makeText(getApplicationContext(), "请先获取验证码！", Toast.LENGTH_SHORT).show();
						}else{
							realPay(pwd,vcode);
							pwdDialog.dismiss();
						}
					}
				}
					
			}
		});
		pwdDialog.setOnNegativeListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pwdDialog.dismiss();
				pwdDialog = null;
				
			}
		});
		pwdDialog.setOnVCodeListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				timeCount = (Button)v;
				TimeCount timer = new TimeCount(60000, 1000);
				timer.start();
				//TODO:调用发送验证码接口
				getVcode(walletId,source,bindId);
			}
		});
		pwdDialog.setOnClearPwdListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pwdDialog.togglePwdText();
			}
		});
		Window dialogWindow = pwdDialog.getWindow();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		p.width = (int) (wm.getDefaultDisplay().getWidth() * 0.9); // 宽度设置为屏幕的0.65
		dialogWindow.setAttributes(p);
		pwdDialog.show();
	}
	/**
	 * 获取验证码
	 * @param walletId
	 * @param source
	 */
	protected void getVcode(Long walletId, int source, String bindId) {
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("walletId", walletId);
		params.put("source", source);
		params.put("bindId", bindId);
		// 结果赋值到sign
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equals("Success")) {
					Map<String, Object> content = (HashMap<String, Object>)cd.getContent();
					sign = (String) content.get("sign");
				} else {
					Toast.makeText(ConfirmPayActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}
			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ConfirmPayActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ConfirmPayActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(ConfirmPayActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ConfirmPayActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}
		};
		dataLoader.getApiResult(handler, "/mobile/pinganpay/getValidCode", params, "post");
	}


	/**
	 * 支付
	 */
	protected void realPay(String pwd, String vcode) {
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("walletId", walletId);
		params.put("source", source);
		params.put("bindId", bindId);
		params.put("sign", sign);
		params.put("verifyCode", vcode);
		params.put("pwd", pwd);
		if(sign.isEmpty()){
			Toast.makeText(ConfirmPayActivity.this, "请获取验证码", Toast.LENGTH_SHORT).show();
			return;
		}else if(pwd.isEmpty()){
			Toast.makeText(ConfirmPayActivity.this, "请输入支付密码", Toast.LENGTH_SHORT).show();
			return;
			
		}else if(vcode.isEmpty()){
			Toast.makeText(ConfirmPayActivity.this, "请输入获取的验证码", Toast.LENGTH_SHORT).show();
			return;
		}
		// 结果赋值到sign
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				dialog = dialogUtil.loading("通知", "支付中...");
			}
			@Override
			public void onSuccess(String arg0) {
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equals("Success")) {
					Toast.makeText(ConfirmPayActivity.this, "支付成功",Toast.LENGTH_LONG).show();
					goBackToMyWallet();
				} else {
					Toast.makeText(ConfirmPayActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}
			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ConfirmPayActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ConfirmPayActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(ConfirmPayActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ConfirmPayActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}
		};
		dataLoader.getApiResult(handler, "/mobile/pinganpay/walletOrBindCardPay", params, "post");
		
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			timeCount.setText("获取验证码");
			timeCount.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			timeCount.setClickable(false);
			timeCount.setText(millisUntilFinished / 1000 + " 秒可重新获取");
		}
	}
}
