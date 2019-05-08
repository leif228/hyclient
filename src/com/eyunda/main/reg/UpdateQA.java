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

public class UpdateQA extends CommonActivity {
	Data_loader data;
	EditText e1, e2, e3;
	Button submit;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.update_qa);
		data = new Data_loader();

		e1 = (EditText) findViewById(R.id.e1);
		e2 = (EditText) findViewById(R.id.e2);
		e3 = (EditText) findViewById(R.id.e3);
		submit = (Button) findViewById(R.id.submit);

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (e1.getText().toString().equals("")) {

					Toast.makeText(UpdateQA.this, "密码不能为空", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (e2.getText().toString().equals("")) {

					Toast.makeText(UpdateQA.this, "新密码问题不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (e3.getText().toString().equals("")) {

					Toast.makeText(UpdateQA.this, "确认密码问题答案不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
			 

				data.updateQA(new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String arg0) {
						super.onSuccess(arg0);
						// {"result":0,"content":"密码修改成功"}
						Map<String, String> tm = DataConvert.toMap(arg0);
						if (tm != null) {

							if (tm.get("result").equals("0")) {
								Toast.makeText(UpdateQA.this,
										tm.get("content"), Toast.LENGTH_SHORT)
										.show();
							} else
								Toast.makeText(UpdateQA.this,
										tm.get("content"), Toast.LENGTH_SHORT)
										.show();
						}

					}

					@Override
					public void onFailure(Throwable arg0) {
						super.onFailure(arg0);
					}
				}, e1.getText().toString(), Config.USERID, e2.getText()
						.toString(),e3.getText().toString());

			}
		});

	}

}
