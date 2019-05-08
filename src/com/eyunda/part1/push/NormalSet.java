package com.eyunda.part1.push;

import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.Config;
import com.eyunda.main.json.DataConvert;
import com.eyunda.part1.data.PartData_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class NormalSet extends CommonActivity {

	PartData_loader data;

	TextView plug_dis;
	private String title, id;
	EditText lab_name;
	Button submit_button;

	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.push_set1);
		data = new PartData_loader();
		plug_dis = (TextView) findViewById(R.id.plug_dis);
		title = getIntent().getStringExtra("title");
		id = getIntent().getStringExtra("id");
		setPush();

		lab_name = (EditText) findViewById(R.id.lab_name);
		submit_button = (Button) findViewById(R.id.submit_but);
		submit_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				data.userFeedBackService(new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String arg0) {
						//{"result":0,"content":"提交成功"}
						Map<String, String> m=DataConvert.toMap(arg0);
						Toast.makeText(NormalSet.this, m.get("content"), Toast.LENGTH_SHORT).show();
						lab_name.setText("");
					}
				}, Config.USERID, id, lab_name.getText().toString());

			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle(title);
	}

	private void setPush() {

		data.pluginServiceDetail(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				try {
					Map<String, String> m = DataConvert.toMap(arg0);
					// plug_name.setText(m.get("name"));
					plug_dis.setText(m.get("description"));
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}, id);
	}

}
