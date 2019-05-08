package com.eyunda.third.activities.user;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.account.BankListActivity;
import com.eyunda.third.activities.account.BindBankCardActivity;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class UpdatePwd extends CommonActivity {
	Data_loader data;
	EditText pwd1, pwd2, pwd3;
	Button submit;
	DialogUtil dialogUtil;
	ProgressDialog dialog;

	private BasePopupWindow popupWindow;// 弹出菜单
	private View btnA;
	private View btnB;
	private View btnC;
	private View btnD;
	private View btnF;
	private View btnG;
	private View btnH;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_user_updatepwd);
		dialogUtil = new DialogUtil(this);
		data = new Data_loader();

		pwd1 = (EditText) findViewById(R.id.pwd1);
		pwd2 = (EditText) findViewById(R.id.pwd2);
		pwd3 = (EditText) findViewById(R.id.pwd3);
		submit = (Button) findViewById(R.id.submit);

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (pwd1.getText().toString().equals("")) {

					Toast.makeText(UpdatePwd.this, "密码不能为空", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (pwd2.getText().toString().equals("")) {

					Toast.makeText(UpdatePwd.this, "新密码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (pwd3.getText().toString().equals("")) {

					Toast.makeText(UpdatePwd.this, "确认密码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (!pwd3.getText().toString()
						.equals(pwd2.getText().toString())) {

					Toast.makeText(UpdatePwd.this, "两次密码不相同",
							Toast.LENGTH_SHORT).show();
					return;
				}
				loadData();
			}
		});
		// data.updatePwd(new AsyncHttpResponseHandler() {
		// @Override
		// public void onSuccess(String arg0) {
		// super.onSuccess(arg0);
		// // {"result":0,"content":"密码修改成功"}
		// Map<String, String> tm = DataConvert.toMap(arg0);
		// if (tm != null) {
		//
		// if (tm.get("result").equals("0")) {
		// Toast.makeText(UpdatePwd.this,
		// tm.get("content"), Toast.LENGTH_SHORT)
		// .show();
		// } else
		// Toast.makeText(UpdatePwd.this,
		// tm.get("content"), Toast.LENGTH_SHORT)
		// .show();
		// }
		//
		// }
		//
		// @Override
		// public void onFailure(Throwable arg0) {
		// // TODO Auto-generated method stub
		// super.onFailure(arg0);
		// }
		// }, pwd1.getText().toString(), Config.USERID, pwd2.getText()
		// .toString());
		//
		// }
		// });
		setPopupWindow();
		
	}

	private void setPopupWindow() {
		popupWindow = new BasePopupWindow(this);
		btnA = popupWindow.getBtnA();
		btnA.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(UpdatePwd.this,
						BasicInfoActivity.class));

			}
		});
		btnB = popupWindow.getBtnB();
		btnB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(UpdatePwd.this,
						AccountListActivity.class));

			}
		});
		btnC = popupWindow.getBtnC();
		btnC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(UpdatePwd.this,
						ApplyAgentActivity.class));

			}
		});
		btnD = popupWindow.getBtnD();
		btnD.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(UpdatePwd.this,
						AgentActivity.class));
			}
		});
		btnF = popupWindow.getBtnF();
		btnF.setVisibility(View.GONE);;
		// 管理子账号
		btnG = popupWindow.getBtnG();
		btnG.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(UpdatePwd.this,
						UserChildListActivity.class));
			}
		});
		
		btnH = popupWindow.getBtnH();
		btnH.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(UpdatePwd.this,BindBankCardActivity.class));
			}
		});
		UserData user = GlobalApplication.getInstance().getUserData();
		if (user != null) {
			if (user.isChildUser()) {
				btnA.setVisibility(View.VISIBLE);
				btnB.setVisibility(View.GONE);
				btnC.setVisibility(View.GONE);
				btnD.setVisibility(View.GONE);
				btnG.setVisibility(View.GONE);
				btnH.setVisibility(View.VISIBLE);
			}else {
				btnA.setVisibility(View.VISIBLE);
				btnB.setVisibility(View.GONE);
				btnH.setVisibility(View.VISIBLE);
				if(user.isRealUser()){
					btnC.setVisibility(View.GONE);
					btnD.setVisibility(View.VISIBLE);
					btnG.setVisibility(View.VISIBLE);
				}else {
					btnC.setVisibility(View.VISIBLE);
					btnD.setVisibility(View.GONE);
					btnG.setVisibility(View.GONE);
				}
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("修改登录密码");
		setRight(R.drawable.top_left, new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.togglePopWindow(v);
			}
		});
	}

	private void loadData() {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("提交中", "请稍候...");
			}

			@Override
			public void onSuccess(String content) {
				dialog.dismiss();
				Log.i("userinfo", content);
				Gson gson = new Gson();
				HashMap<String, Object> map = gson.fromJson((String) content,
						new TypeToken<Map<String, Object>>() {
						}.getType());

				if (map.get("returnCode").equals("Success")) {
					Toast.makeText(UpdatePwd.this, "修改密码成功", 1).show();
				} else {
					Toast.makeText(UpdatePwd.this,
							map.get("message").toString(), 1).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Toast.makeText(UpdatePwd.this, "修改密码失败", 1).show();
				dialog.dismiss();

			}

		};
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", GlobalApplication.getInstance().getUserData().getId());
		params.put("password", pwd1.getText());
		params.put("newpassword", pwd2.getText());

		data.getApiResult(handler, "/mobile/account/myAccount/savePasswd",
				params);

	}
}
