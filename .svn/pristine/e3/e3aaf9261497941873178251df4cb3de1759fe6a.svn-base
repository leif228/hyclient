package com.eyunda.third.activities.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.enumeric.EvalTypeCode;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

public class OrderCommentActivity extends CommonListActivity {
	private Spinner satisfy;
	private EditText comment;
	private ArrayAdapter<SpinnerItem> spinnerAdapter;
	private Button btnSave;
	private String orderId;
	private Data_loader dataLoader;
	protected DialogUtil dialogUtil;
	protected ProgressDialog dialog;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		dialogUtil = new DialogUtil(this);
		setContentView(R.layout.eyd_order_addcomment);
		dataLoader = new Data_loader();
		Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();
		orderId = (String)bundle.getString("orderId");
		initView();
		loadDate();
	}
	@Override
	protected void onStart() {
		super.onStart();
		
		
		setTitle("发表评论");
		setRight("关闭", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				closeWindow();
			}
		});
	}

	private void initView(){
		satisfy = (Spinner)findViewById(R.id.satisfy);
		comment = (EditText)findViewById(R.id.comment);
		btnSave = (Button)findViewById(R.id.button_save);

		btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//读取spinner，调接口保存
				String sid =((SpinnerItem)satisfy.getSelectedItem()).getId();
				EvalTypeCode etc = EvalTypeCode.valueOf(Integer.parseInt(sid));
				String com = comment.getText().toString();
				Map<String, Object> apiParams = new HashMap<String, Object>();
				apiParams.put("id", Long.parseLong(orderId));
				apiParams.put("evalType",etc);
				apiParams.put("evalContent",com);
				dataLoader.getApiResult(new AsyncHttpResponseHandler(){
					@Override
					public void onStart(){
						dialog = dialogUtil.loading("通知", "数据保存中，请稍候...");
					}
					@Override
					public void onSuccess(String arg0) {
						//保存数据
						ConvertData cd = new ConvertData(arg0);
//						Log.setLog2FileEnabled(true);
//						Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("eyunda",".log"));
//						Log.d(res);
						if(cd.getReturnCode().equalsIgnoreCase("success")){
							dialog.dismiss();
							//返回上一个list视图
							Toast.makeText(OrderCommentActivity.this, cd.getMessage(),Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(OrderCommentActivity.this,MyOrderActivity.class);
							startActivity(intent);
							finish();
						}else{
							dialog.dismiss();
							Toast.makeText(OrderCommentActivity.this, cd.getMessage(),Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Throwable arg0, String content) {
						super.onFailure(arg0, content);
						Toast.makeText(OrderCommentActivity.this, content,Toast.LENGTH_SHORT).show();
					}
				}, "/mobile/order/myOrder/approvalOrder",apiParams,"post");
			}
		});
	}
	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void loadDate() {
		// 建立数据源
	    List<SpinnerItem>  mItems= getattr();
	    // 建立Adapter并且绑定数据源
 		spinnerAdapter=new ArrayAdapter<SpinnerItem>(this,R.layout.spinner_item,R.id.contentTextView,mItems);
 		//绑定 Adapter到控件
 		satisfy.setAdapter(spinnerAdapter);
	}
	
	private List<SpinnerItem> getattr() {	
		ArrayList<SpinnerItem> cargo= new ArrayList<SpinnerItem>();
		for (EvalTypeCode e : EvalTypeCode.values()) {
		     SpinnerItem item = new SpinnerItem(e.ordinal()+"", e.getDescription());
		     cargo.add(item);
		}
		return cargo;
	}
	
	/**
	 * 覆盖返回按钮
	 */

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		closeWindow();
	}
	private void closeWindow() {
		//返回列表
		Intent intent = new Intent(OrderCommentActivity.this,MyOrderActivity.class);
		startActivity(intent);
		finish();
	}


}
