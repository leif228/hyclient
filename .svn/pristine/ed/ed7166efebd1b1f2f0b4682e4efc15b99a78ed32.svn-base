package com.eyunda.main.reg;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.data.Data_loader;
import com.eyunda.main.json.DataConvert;
import com.eyunda.main.view.DialogUtil;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class Forgetpwd extends CommonActivity {
	EditText number, question, answer, newpwd, repwd, verification;
	Button submit, getvar_but;
	Data_loader data;
	ProgressDialog dialog;
	DialogUtil dialogUtil;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_user_activity_forgetpwd);
		data = new Data_loader();
		dialogUtil = new DialogUtil(this);
		number = (EditText) findViewById(R.id.number);
		question = (EditText) findViewById(R.id.question);
		verification = (EditText) findViewById(R.id.verification);
		answer = (EditText) findViewById(R.id.answer);
		newpwd = (EditText) findViewById(R.id.number);
		repwd = (EditText) findViewById(R.id.number);

		submit = (Button) findViewById(R.id.submit);

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (number.getText().toString().equals("")) {

					Toast.makeText(Forgetpwd.this, "手机号不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				// if (question.getText().toString().equals("")) {
				//
				// Toast.makeText(Forgetpwd.this, "密码提示问题不能为空",
				// Toast.LENGTH_SHORT).show();
				// return;
				// }
				if (verification.getText().toString().equals("")) {

					Toast.makeText(Forgetpwd.this, "验证码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (newpwd.getText().toString().equals("")) {

					Toast.makeText(Forgetpwd.this, "新密码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (newpwd.getText().toString().length()<6) {
					Toast.makeText(Forgetpwd.this, "密码不能少于6位",
							Toast.LENGTH_SHORT).show();
					return;

				}
				if (repwd.getText().toString().equals("")) {

					Toast.makeText(Forgetpwd.this, "再次确认密码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (!newpwd.getText().toString()
						.equals(repwd.getText().toString())) {
					Toast.makeText(Forgetpwd.this, "两次密码不一致",
							Toast.LENGTH_SHORT).show();
					return;
				}
				data.resetPasswd(
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String arg0) {
								super.onSuccess(arg0);
								// {"result":0,"content":"密码修改成功"}
								Map<String, String> tm = DataConvert
										.toMap(arg0);
								if (tm != null) {

									if (tm.get("result").equals("0")) {
										Toast.makeText(Forgetpwd.this,
												tm.get("content"),
												Toast.LENGTH_SHORT).show();
									} else
										Toast.makeText(Forgetpwd.this,
												tm.get("content"),
												Toast.LENGTH_SHORT).show();
								}

							}

							@Override
							public void onFailure(Throwable arg0) {
								super.onFailure(arg0);
							}
						}, newpwd.getText().toString(), number.getText()
								.toString(),
						verification.getText().toString());

			}
		});
		getvar_but = (Button) findViewById(R.id.getvar_but);
		getvar_but.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getVer(number.getText().toString());
			}
		});

	}

	private boolean butEnable = true;

	class rThread extends Thread {
		int index = 120;

		public rThread() {

		}

		@Override
		public void run() {

			while (!butEnable) {
				index--;
				if (index < 1) {
					butEnable = true;
					runOnUiThread(new Runnable() {
						public void run() {
							getvar_but.setText("获取验证码");
						}
					});
				} else
					runOnUiThread(new Runnable() {
						public void run() {
							getvar_but.setText(index + "");
						}
					});

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * 手机号验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	private void getVer(String mobileNo) {
		if (mobileNo.equals("")) {
			Toast.makeText(Forgetpwd.this, "手机号不能为空", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		if (!isMobile(mobileNo)) {

			Toast.makeText(Forgetpwd.this, "输入正确的手机号", Toast.LENGTH_SHORT)
					.show();
			return;

		}
		if (!butEnable) {
			Toast.makeText(Forgetpwd.this, "120秒后再次尝试", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		butEnable = false;
		new rThread().start();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("获取验证码中", "请稍候...");
			}

			@Override
			public void onSuccess(String content) {
				System.out.println(content);
				// Map<String, String> m = DataConvert.toMap(content);

				Toast.makeText(Forgetpwd.this, "验证码已发送", Toast.LENGTH_SHORT)
						.show();

				dialog.dismiss();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				dialog.dismiss();

			}

		};

		data.getVerForGetpwd(handler, mobileNo);

	}

}
