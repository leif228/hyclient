package com.eyunda.third.activities.ship;

import java.util.HashMap;
import java.util.Map;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditTemplateActivity extends CommonActivity {

	private Data_loader data = new Data_loader();
	private String shipId;
	private String tempId;
	private String orderItemId;
	private String content;
	private EditText et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_ship_price);
		Intent intent=getIntent();
		content= intent.getStringExtra("content");
		shipId= intent.getStringExtra("shipId");
		tempId= intent.getStringExtra("orderTemplateId");
		orderItemId= intent.getStringExtra("orderItemId");
		 et = (EditText)findViewById(R.id.tempName);
		et.setText(content);
	}
	protected void onStart() {
		super.onStart();
		setTitle("新增合同条款");
	};
   public void addTemp(View v){
	    HashMap<String, Object> params = new HashMap<String, Object>();
	    params.put("shipId", shipId);
	    params.put("orderTemplateId", tempId);
	    params.put("orderItemId",orderItemId);
	    params.put("content", et.getText().toString());
	    data.getApiResult(handler, "/mobile/ship/myShip/saveOrderItem",params,"post");
   }

	AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
		@Override
		public void onSuccess(String arg0) {
			super.onSuccess(arg0);
			Gson gson = new Gson();
			HashMap<String, Object> result= gson.fromJson(
					(String) arg0,
					new TypeToken<Map<String, Object>>() {
					}.getType());
			if (result.get("returnCode").equals("Success")) {
				Toast.makeText(EditTemplateActivity.this,result.get("message").toString(), Toast.LENGTH_SHORT).show();
                finish();
			}
		}

		@Override
		public void onFailure(Throwable arg0) {
			// TODO Auto-generated method stub
			super.onFailure(arg0);
			Toast.makeText(EditTemplateActivity.this,"提交失败", Toast.LENGTH_SHORT).show();

		}

	};








}
