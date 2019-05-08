package com.eyunda.third.activities.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.domain.enumeric.CargoTypeCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.MapSerializable;
import com.eyunda.tools.log.Log;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 新增合同-新增一条合同条款
 * @author guoqiang
 *
 */
public class AddOrderOneTemplateActivity extends AddOrderActivity implements
		OnClickListener{
	Data_loader dataLoader;

	private Button buttonSave;

	private EditText tempName;

	private String tempId;
	private String oldTempName;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_order_one_addtemp);
		dataLoader = new Data_loader();
		Bundle bundle = new Bundle();
		
        bundle = this.getIntent().getExtras();
		orderId=(Long)bundle.getLong("orderId");
		tempId=(String)bundle.getString("tempId");
		oldTempName = (String)bundle.getString("tempName");
		
		initViews();
		loadDate();
	}





	private void initViews() {
		
		tempName = (EditText)findViewById(R.id.tempName);
		buttonSave = (Button) findViewById(R.id.buttonSave);
		buttonSave.setOnClickListener(this);
		
	}

	
	
	@Override
	protected void onStart() {
		super.onStart();
		if(tempId.equals("0")){
			setTitle("编辑合同-新增合同条款"); 
		}else{
			setTitle("编辑合同-编辑合同条款"); 
		}
		btnAddGSH.setOnClickListener(null);
		btnAddGSH.setBackgroundColor(0xFF6db7ff);
	}

	@Override
	protected synchronized void loadDate() {
		if(!tempId.equals("0")){
			//修改模板
			tempName.setText(oldTempName);
		}
	}
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){

			case R.id.buttonSave:
				
				Map<String, Object> apiParams = new HashMap<String, Object>();
		        if(TextUtils.isEmpty(tempName.getText())){
					Toast.makeText(this, "合同条款内容不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				
				apiParams.put("orderId",orderId);
				apiParams.put("content",tempName.getText());
				if(!tempId.equals("0")){
					apiParams.put("id",Long.parseLong(tempId));
				}else
					apiParams.put("id","0");
				dataLoader.getApiResult(new AsyncHttpResponseHandler(){
					@Override
					public void onStart(){
						dialog = dialogUtil.loading("通知", "数据保存中，请稍候...");
					}
					@Override
					public void onSuccess(String arg0) {
						super.onSuccess(arg0);
						dialog.dismiss();
						ConvertData cd = new ConvertData(arg0);
						Toast.makeText(AddOrderOneTemplateActivity.this, cd.getMessage(),Toast.LENGTH_SHORT).show();
						if(cd.getReturnCode().equalsIgnoreCase("success")){
							//返回上一个list视图
							Intent intnet = new Intent(AddOrderOneTemplateActivity.this,
									AddOrderTKActivity.class);
							intnet.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intnet.putExtra("orderId", orderId);
							startActivity(intnet);
							finish();
						}
					}

					@Override
					public void onFailure(Throwable arg0, String content) {
						super.onFailure(arg0, content);
						Toast.makeText(AddOrderOneTemplateActivity.this, content,
								Toast.LENGTH_SHORT).show();
					}
				}, "/mobile/order/myOrder/saveOrderItem",apiParams,"get");
				
				break;

		}
		
	}




}
