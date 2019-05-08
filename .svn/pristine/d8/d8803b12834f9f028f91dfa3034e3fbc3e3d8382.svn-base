package com.eyunda.main.reg;

import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.Config;
import com.eyunda.main.data.Data_loader;
import com.eyunda.main.json.DataConvert;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class UpdatePwd extends CommonActivity {
	Data_loader data;
	EditText pwd1, pwd2, pwd3;
	Button submit;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.updatepwd);
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

				data.updatePwd(new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String arg0) {
						super.onSuccess(arg0);
						// {"result":0,"content":"密码修改成功"}
						Map<String, String> tm = DataConvert.toMap(arg0);
						if (tm != null) {

							if (tm.get("result").equals("0")) {
								Toast.makeText(UpdatePwd.this,
										tm.get("content"), Toast.LENGTH_SHORT)
										.show();
							} else
								Toast.makeText(UpdatePwd.this,
										tm.get("content"), Toast.LENGTH_SHORT)
										.show();
						}

					}

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub
						super.onFailure(arg0);
					}
				}, pwd1.getText().toString(), Config.USERID, pwd2.getText()
						.toString());

			}
		});

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		setTitle("修改密码");
	}
}
