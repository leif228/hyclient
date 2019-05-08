package com.eyunda.third.activities.account;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

//
public class BindBankCardNextActivity extends CommonActivity {
	
	public static final String RESEND = "reSend";

//	TimeCount timer;

	@Bind(R.id.btnSave)
	Button btnSave;
	@Bind(R.id.close)
	Button btnClose;
	@Bind(R.id.submit)
	Button btnSubmit;

	@Bind(R.id.tansNum)
	EditText tansNum;
	@Bind(R.id.paypw)
	EditText paypw;
	
	@Bind(R.id.sm)
	TextView sm;
	@Bind(R.id.mobile4)
	TextView mobile4;
	
	Data_loader data;
	private ProgressDialog dialog;

	private String paymentNo;

	private String serialNo;

	private String revMobilePhone;

	private String fetchBody;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_bindcard_next);
		ButterKnife.bind(this);
		data = new Data_loader();
//		timer = new TimeCount(10000, 1000);// 构造CountDownTimer对象
//		timer.start();
		
		Intent i = getIntent();
		paymentNo = i.getStringExtra("paymentNo");
		serialNo = i.getStringExtra("serialNo");
		revMobilePhone = i.getStringExtra("revMobilePhone");
		fetchBody = i.getStringExtra("fetchBody");
		
		sm.setText(fetchBody);
		mobile4.setText("手机后四位:"+revMobilePhone);
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("提现确认");
	}

	// 重新发送验证码
//	@OnClick(R.id.btnSave)
//	void translate(View v) {  //
//		btnSave.setClickable(false);
//		
//		Intent intent = new Intent();
//		intent.putExtra("MessageCode", RESEND);
//		
//		setResult(RESULT_OK,intent);
//		
//		timer.cancel();
//		finish();
//	}
	// 
	@OnClick(R.id.close)
	void close(View v) {  //
//		timer.cancel();
		finish();
		
	}
	// 
	@OnClick(R.id.submit)
	void submit(View v) {  //
		if(TextUtils.isEmpty(paypw.getText().toString().trim())){
			Toast.makeText(this, "请输入支付密码！", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(tansNum.getText().toString().trim())){
			Toast.makeText(this, "请输入短信验证码！", Toast.LENGTH_SHORT).show();
			return;
		}
		
		loadData(paypw.getText().toString().trim(),tansNum.getText().toString().trim());
//		Intent intent = new Intent();
//		intent.putExtra("MessageCode", tansNum.getText().toString().trim());
//		
//		setResult(RESULT_OK,intent);
		
//		timer.cancel();
//		finish();

	}

	protected void loadData(String paypw,String message) {
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
					String result = (String) cd.getContent();
					
					DialogUtil dialogAlert = new DialogUtil(BindBankCardNextActivity.this);
					dialogAlert.showDialogFromConfig("提现结果确认", result, new Handler() {
						@Override
						public void handleMessage(Message msg) {
							super.handleMessage(msg);
							BindBankCardNextActivity.this.finish();
						}
					});
				} else {
					Toast.makeText(BindBankCardNextActivity.this, cd.getMessage(),Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();

				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(BindBankCardNextActivity.this, "网络连接异常",Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(BindBankCardNextActivity.this, "连接服务器超时",Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Toast.makeText(BindBankCardNextActivity.this, content,Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(BindBankCardNextActivity.this, "未知异常",Toast.LENGTH_LONG).show();
			}

		};
		params.put("paymentNo",paymentNo);
		params.put("serialNo",serialNo);
		params.put("paypwd", paypw);
		params.put("MessageCode", message);
		data.getApiResult(handler, "/mobile/wallet/myWallet/surefetch", params, "post");
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btnSave.setText("重新发送验证码");
			btnSave.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btnSave.setClickable(false);
			btnSave.setText(millisUntilFinished / 1000 + "秒后需重新获取验证码");
		}
	}

}
