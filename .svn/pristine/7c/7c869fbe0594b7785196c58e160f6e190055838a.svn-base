package com.eyunda.third.activities.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.enumeric.MoneyStyleCode;
import com.eyunda.third.domain.enumeric.PayStyleCode;
import com.eyunda.third.domain.order.OrderData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.log.FilePathGenerator;
import com.eyunda.tools.log.Log;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 新增合同-结算约定
 * 
 * @author guoqiang
 *
 */
public class AddOrderJSActivity extends AddOrderActivity implements
		OnClickListener {
	Data_loader dataLoader;
	private Button btnSave;
	// 页面视图
	private Spinner mSpinner;// 支付方式
	private Spinner mPaySpinner;// 付款方式
	private Spinner payAccountSpinner;// 收款账户
	private EditText payPrice;// 总运费
	private EditText surePrice;// 保证金
	private EditText demurrage;	//滞期费率
	private boolean flag = false;
	private List<SpinnerItem>  mAccountItems;
	ArrayAdapter<SpinnerItem> _AccountAdapter;
	
	//处理保存结果
	AsyncHttpResponseHandler resultHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onFailure(Throwable arg0, String content) {
			super.onFailure(arg0, content);
			Toast.makeText(AddOrderJSActivity.this, content,
					Toast.LENGTH_SHORT).show();
		}
		@Override
		public void onStart(){
			dialog = dialogUtil.loading("通知", "数据保存中，请稍候...");
		}
		@Override
		public void onSuccess(String res) {
			super.onSuccess(res);
			ConvertData cd = new ConvertData(res);
			dialog.dismiss();
			Toast.makeText(AddOrderJSActivity.this, cd.getMessage(),
					Toast.LENGTH_SHORT).show();
			//btnSave.setClickable(false);// 防止重复提交
		}

	};
	//处理保存结果
	AsyncHttpResponseHandler calcHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onFailure(Throwable arg0, String content) {
			super.onFailure(arg0, content);
			Toast.makeText(AddOrderJSActivity.this, content,
					Toast.LENGTH_SHORT).show();
		}
		@Override
		public void onStart(){
		}
		@Override
		public void onSuccess(String res) {
			super.onSuccess(res);
			ConvertData cd = new ConvertData(res);
			if(cd.getReturnCode().equalsIgnoreCase("success")){
				Map<String,Object> content = (Map<String,Object>)cd.getContent();
				Double total = (Double)content.get("deposit");
				surePrice.setText(String.valueOf(total));
			}
		}

	};

	private void autoCal() {
		payPrice.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String count = payPrice.getText().toString();
				if (!TextUtils.isEmpty(count) && flag) {
			        Map<String,Object> apiParam =new HashMap<String, Object>();
			        apiParam.put("total",Double.parseDouble(count));
			        dataLoader.getApiResult(calcHandler, "/mobile/order/myOrder/calcDeposit",apiParam);
				}
			}
		});
	}
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_order_addjsyd);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		RelativeLayout surePriceContainer = (RelativeLayout)findViewById(R.id.surePriceContainer);
		surePriceContainer.setVisibility(View.GONE);
		
		Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();
		orderId=(Long)bundle.getLong("orderId");
		
		//支付方式
		mSpinner = (Spinner)findViewById(R.id.payType);
		// 建立数据源
	    List<SpinnerItem>  mItems= new ArrayList<SpinnerItem>();
	    for(PayStyleCode p:PayStyleCode.values()){
	    	 SpinnerItem si=new SpinnerItem(p.name(),p.getDescription());
	    	 mItems.add(si);
	    }

	    
		// 建立Adapter并且绑定数据源
		ArrayAdapter<SpinnerItem> _Adapter=new ArrayAdapter<SpinnerItem>(this,android.R.layout.simple_spinner_item, mItems);
		//绑定 Adapter到控件
		mSpinner.setAdapter(_Adapter);
		
		
		//付款方式
		mPaySpinner = (Spinner)findViewById(R.id.cashType);
		// 建立数据源
	    List<SpinnerItem>  mPayItems= new ArrayList<SpinnerItem>();
	    for(MoneyStyleCode p:MoneyStyleCode.values()){
	    	 SpinnerItem si=new SpinnerItem(p.name(),p.getDescription());
	    	 mPayItems.add(si);
	    }
		// 建立Adapter并且绑定数据源
		ArrayAdapter<SpinnerItem> _PayAdapter=new ArrayAdapter<SpinnerItem>(this,android.R.layout.simple_spinner_item, mPayItems);
		//绑定 Adapter到控件
		mPaySpinner.setAdapter(_PayAdapter);
		

		//收款方式
		payAccountSpinner = (Spinner)findViewById(R.id.payAccount);
		mAccountItems= new ArrayList<SpinnerItem>();
		// 建立Adapter并且绑定数据源
		_AccountAdapter=new ArrayAdapter<SpinnerItem>(this,android.R.layout.simple_spinner_item, mAccountItems);
		//绑定 Adapter到控件
		payAccountSpinner.setAdapter(_AccountAdapter);
		
		btnSave = (Button)findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);
		btnSave.setClickable(true);
		dataLoader = new Data_loader();
		

		payPrice = (EditText)findViewById(R.id.payPrice);
		demurrage = (EditText)findViewById(R.id.demurrage);
		surePrice = (EditText)findViewById(R.id.surePrice);
		payPrice.setEnabled(false);
		surePrice.setEnabled(false);
		
		loadDate();
	}


	@Override
	protected void onStart() {
		super.onStart();
		setTitle("编辑合同-结算约定");
		btnAddJS.setOnClickListener(null);
		btnAddJS.setBackgroundColor(0xFF6db7ff);
		autoCal();

	}

	@Override
	protected synchronized void loadDate() {
		final Map<String, Object> apiParams = new HashMap<String, Object>();
		apiParams.put("orderId", orderId);
		// 处理展示时的数据
		AsyncHttpResponseHandler showHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				Toast.makeText(AddOrderJSActivity.this, content,
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onStart(){
				dialog = dialogUtil.loading("通知", "数据获取中，请稍候...");
			}
			@Override
			public void onSuccess(String res) {
				super.onSuccess(res);
				
				ConvertData cd = new ConvertData(res,"/mobile/order/myOrder/edit",apiParams);
				HashMap<String, Object> var = (HashMap<String, Object>) cd.getContent();
				HashMap<String, Object> var2 = (HashMap<String, Object>) var.get("orderData");
				OrderData order = new OrderData(var2);


//				//填充spinner数据
				List<HashMap<String, Object>> var3 = (ArrayList<HashMap<String, Object>>) var.get("accountDatas");
				if((var3 !=null) && (var3.size() > 0)){
					//遍历list
					for(HashMap<String, Object> tmp:var3)  
			        {  
			            //System.out.println(tmp);
						SpinnerItem si = new SpinnerItem(((Double)tmp.get("id")).toString(), (String)tmp.get("accountNo"));
						mAccountItems.add(si);
			        }  
					//判断原来order的结算账户是否存在，存在且在mAccountItems中，则设为当前值
//					String accountNo = order.getRcvCardNo();
//					if(!accountNo.equalsIgnoreCase("") && mAccountItems.size()>0){
//						int i = 0;
//						for(SpinnerItem tsi:mAccountItems){
//							if(tsi.getValue().equalsIgnoreCase(accountNo)){
//								payAccountSpinner.setSelection(i);
//								break;
//							}
//							i++;
//						}
//					}
//					_AccountAdapter.notifyDataSetChanged();
					btnSave.setClickable(true);
				}else{
					Toast.makeText(AddOrderJSActivity.this, "承运人没有设置收款账户信息，无法保存",
							Toast.LENGTH_SHORT).show();
					btnSave.setClickable(false);
				}
				// 设置视图，通知页面布局改变
				payPrice.setText(Double.toString(order.getTransFee()));
				surePrice.setText("0");
				demurrage.setText(Double.toString(order.getDemurrage()));	
				//mSpinner.setSelection(((PayStyleCode) order.getPayStyle()).ordinal());
				mPaySpinner.setSelection(((MoneyStyleCode) order.getMoneyStyle()).ordinal());
				dialog.dismiss();
				flag = true;
			}

		};
		dataLoader.getApiResult(showHandler, "/mobile/order/myOrder/edit",
				apiParams, "get");
	}

	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.btnSave){
			
			//String val = ((SpinnerItem)mSpinner.getSelectedItem()).getValue();
			//String seletedVal = ((SpinnerItem)mSpinner.getSelectedItem()).getId();
			String seletedPayVal = ((SpinnerItem)mPaySpinner.getSelectedItem()).getId();
			String seletedAccountVal = ((SpinnerItem)payAccountSpinner.getSelectedItem()).getValue();
			//Toast.makeText(this, val+"-"+id, Toast.LENGTH_SHORT).show();
			//mSpinnerId =  PayStyleCode.valueOf(id).ordinal();
	        //传回服务器
	        Map<String,Object> apiParam =new HashMap<String, Object>();
	        
	        if(TextUtils.isEmpty(demurrage.getText())){
				Toast.makeText(this, "请输入滞期费率", Toast.LENGTH_SHORT).show();
				return;
			}
	        
	        apiParam.put("id",orderId);
	        apiParam.put("moneyStyle",seletedPayVal);

	        apiParam.put("transFee",payPrice.getText());
	        apiParam.put("demurrage",demurrage.getText());

	        apiParam.put("deposit",surePrice.getText());
	        //apiParam.put("payStyle",seletedVal);
	        apiParam.put("accountNo", seletedAccountVal);
	        if(seletedAccountVal.equals("")){
	        	Toast.makeText(getApplicationContext(), "承运人未设置收款账户，请通知承运人先设置收款账户", Toast.LENGTH_LONG).show();
	        }else{
	        	dataLoader.getApiResult(resultHandler, "/mobile/order/myOrder/saveSettle",apiParam,"get");
	        }
		}
	}
	
	
}
