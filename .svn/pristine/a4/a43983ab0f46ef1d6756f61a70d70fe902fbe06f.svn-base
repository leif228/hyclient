package com.eyunda.third.activities.order;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.third.GlobalApplication;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.enumeric.EnumConst.PortCityCode;
import com.eyunda.third.domain.order.OrderData;
import com.eyunda.third.domain.order.PortData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.AreaSelect;
import com.eyunda.tools.LocalFileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;
/**
 * 新增合同-装卸约定
 * @author guoqiang
 *
 */
public class AddOrderZXActivity extends AddOrderActivity implements
		OnClickListener{
	Data_loader dataLoader;

	private Button btnSave ;
	private Button tyr;//托运人
	private TextView tyrId;//托运人Id
	private Button zhuanghuoDate;//装货日期
	private Button daohuoDate;//到货日期
	private Button startPort;//起始港口
	private Button endPort;//终止港口
	private String startPortNo;//起始港口编号
	private String endPortNo;//终止港口编号

	private String seletedDate;//当前选择的日期
	private TextView currentBtn;//当前选择的日期按钮
	
	
	private EditText upTime;//装货时间
	private EditText downTime;//卸货时间
	private TextView receiver;//接货人
	private TextView receMobile;//接货人电话
	private TextView receAddress;//接货人地址

    private static final int SHOW_DATAPICK = 0;
    private static final int DATE_DIALOG_ID = 1; 
	private int mYear; 
    private int mMonth;
    private int mDay;

    private Long userId;

	private RelativeLayout test_pop_layout;//当前页面视图

	
	//处理保存结果
	AsyncHttpResponseHandler resultHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onFailure(Throwable arg0, String content) {
			super.onFailure(arg0, content);
			Toast.makeText(AddOrderZXActivity.this, content,Toast.LENGTH_SHORT).show();
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
			Toast.makeText(AddOrderZXActivity.this, cd.getMessage(),Toast.LENGTH_SHORT).show();
			
		}

	};

	private Button addStart;

	private Button addEnd;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_order_zxyd);
		dataLoader = new Data_loader();
		// 获取弹出的layout
	    test_pop_layout = (RelativeLayout)findViewById(R.id.eyd_order_zxyd_layout);
		Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();
		orderId=(Long)bundle.getLong("orderId");
		
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR); 
        mMonth = c.get(Calendar.MONTH); 
        mDay = c.get(Calendar.DAY_OF_MONTH);

        initViews();
        loadDate();

	}





	private void initViews() {
		
		btnSave = (Button)findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);
		
		tyr = (Button)findViewById(R.id.tyr);
		
		tyr.setOnClickListener(this);
		
		tyrId = (TextView)findViewById(R.id.tyrId);
		
		startPort = (Button) findViewById(R.id.startPort);
		//startPort.setFocusable(true);
		startPort.setOnClickListener(this);
		//startPort.requestFocus();
		
		endPort = (Button) findViewById(R.id.endPort);
		//endPort.setFocusable(true);
		endPort.setOnClickListener(this);
		
		zhuanghuoDate=(Button) findViewById(R.id.zhuanghuoDate);
		//zhuanghuoDate.setFocusable(true);
		zhuanghuoDate.setOnClickListener(this);

		daohuoDate=(Button) findViewById(R.id.daohuoDate);
		//daohuoDate.setFocusable(true);
		daohuoDate.setOnClickListener(this);
		
		addStart = (Button)findViewById(R.id.addStart);
		addEnd = (Button)findViewById(R.id.addEnd);
		addStart.setOnClickListener(this);
		addEnd.setOnClickListener(this);
		upTime = (EditText)findViewById(R.id.upTime);
		downTime = (EditText)findViewById(R.id.downTime);
		receiver = (TextView) findViewById(R.id.receiver);
		receMobile =  (TextView) findViewById(R.id.receMobile);
		receAddress =  (TextView) findViewById(R.id.receAddress);

	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("编辑合同-装卸约定"); 
		btnAddZX.setOnClickListener(null);
		btnAddZX.setBackgroundColor(0xFF6db7ff);

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
				Toast.makeText(AddOrderZXActivity.this, content,
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onStart(){
				dialog = dialogUtil.loading("通知", "数据加载中，请稍候...");
			}
			@Override
			public void onSuccess(String res) {
				super.onSuccess(res);
				ConvertData cd = new ConvertData(res,"/mobile/order/myOrder/edit",apiParams);
				HashMap<String, Object> var = (HashMap<String, Object>) cd.getContent();
				HashMap<String, Object> var2 = (HashMap<String, Object>) var.get("orderData");
				OrderData order = new OrderData(var2);

				//startPortNo=, endPortNo=, upDate=2015-01-12, downDate=2015-01-14, upTime=0.0, downTime=0.0, receiver=, receMobile=, receAddress=, 
				// 设置视图，通知页面布局改变
				String resstartPortNo= order.getStartPortNo();
				String resstartEndNo= order.getEndPortNo();
				String resupDate= order.getUpDate();
				String resdownDate= order.getDownDate();
				

				
//				payTon.setText(Double.toString(order.getGoodWeight()));
//				payOnePrice.setText(Double.toString(order.getPrice()));
//				payPrice.setText(Double.toString(order.getTransFee()));
//				delayPrice.setText(Double.toString(order.getDemurrage()));
//				surePrice.setText(Double.toString(order.getDeposit()));
//				mSpinner.setSelection(((PayStyleCode) order.getPayStyle()).ordinal());
				
				PortData sp = order.getStartPort();
				PortData ep = order.getEndPort();
				startPortNo = order.getStartPortNo();
				endPortNo = order.getEndPortNo();
				startPort.setText(sp.getFullName());
				startPort.setTag(order.getStartPortNo());
				endPort.setText(ep.getFullName());
				endPort.setTag(order.getEndPortNo());
				
				if(order.getUpDate().equals("")){
					zhuanghuoDate.setHint("装货日期:"+getCurrentDateTime());
				}else{
					zhuanghuoDate.setText(order.getUpDate());
				}
				if(order.getDownDate().equals("")){
					daohuoDate.setHint("装货日期:"+getCurrentDateTime());
				}else{
					daohuoDate.setText(order.getDownDate());
				}
				upTime.setText(order.getUpTime()+"");
				downTime.setText(order.getDownTime()+"");
				receiver.setText(order.getReceiver());
				receMobile.setText(order.getReceMobile());
				receAddress.setText(order.getReceAddress());
				
				//有托运人，修改
				if(order.getOwner() != null){
					if(order.getOwner().getTrueName().equalsIgnoreCase("")){
						tyr.setText(order.getOwner().getLoginName());
					}else{
						tyr.setText(order.getOwner().getTrueName());
					}
					tyrId.setText(order.getOwner().getId().toString());
					userId = order.getOwner().getId();
				}
				dialog.dismiss();
			}

		};
		
			dataLoader.getApiResult(showHandler, "/mobile/order/myOrder/edit",apiParams, "get");

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()){
		case R.id.btnSave:
			//保存记录
			boolean flag = true;
			Map<String,Object> apiParams = new HashMap<String, Object>();
			apiParams.put("id", orderId);
//			apiParams.put("startPortNo", startPort.getTag());
//			apiParams.put("endPortNo", endPort.getTag());
			if(tyrId.getText().toString().equals("0")){
				Toast.makeText(AddOrderZXActivity.this, "请先选择托与人", Toast.LENGTH_SHORT).show();
				return;
			}
			if(TextUtils.isEmpty(startPort.getTag().toString())){
				Toast.makeText(AddOrderZXActivity.this, "请先选择起始港", Toast.LENGTH_SHORT).show();
				return;
			}
			if(TextUtils.isEmpty(endPort.getTag().toString())){
				Toast.makeText(AddOrderZXActivity.this, "请先选择到达港", Toast.LENGTH_SHORT).show();
				return;
			}
			if(TextUtils.isEmpty(upTime.getText())){
				Toast.makeText(AddOrderZXActivity.this, "请输入装货时间", Toast.LENGTH_SHORT).show();
				return;
			}
			if(TextUtils.isEmpty(downTime.getText())){
				Toast.makeText(AddOrderZXActivity.this, "请输入卸货时间", Toast.LENGTH_SHORT).show();
				return;
			}
			apiParams.put("startPortNo", startPort.getTag().toString());
			apiParams.put("endPortNo", endPort.getTag().toString());
			apiParams.put("upDate", zhuanghuoDate.getText().toString());
			apiParams.put("downDate", daohuoDate.getText().toString());
			//Double ut = Double.valueOf(upTime.getText().toString());
			apiParams.put("id", orderId);
			apiParams.put("upTime",   Integer.parseInt(upTime.getText().toString()));
			apiParams.put("downTime",  Integer.parseInt(downTime.getText().toString()));
			apiParams.put("receiver", receiver.getText().toString());
			apiParams.put("receMobile", receMobile.getText().toString());
			apiParams.put("receAddress", receAddress.getText().toString());

			dataLoader.getApiResult(resultHandler, "/mobile/order/myOrder/saveUpDownLoad",apiParams,"get");

			break;
		case R.id.zhuanghuoDate:
		case R.id.daohuoDate:
			if(v.getId() == R.id.zhuanghuoDate){
				currentBtn = zhuanghuoDate;
			}else{
				currentBtn = daohuoDate;
			}
			Message msg = new Message();
            msg.what = AddOrderZXActivity.SHOW_DATAPICK; 
            AddOrderZXActivity.this.dateandtimeHandler.sendMessage(msg);
			break;
		case R.id.startPort:
		case R.id.endPort:
			
			//选择结果显示的地方
			AreaSelect asStart = new AreaSelect(this, startPort);
			AreaSelect asEnd = new AreaSelect(this, endPort);
			AreaSelect pointAt =null;
			if(v.getId() == R.id.startPort){
				pointAt = asStart;
			}else{
				pointAt = asEnd;
			}
//			PopupWindow popupWindow = pointAt.makePopupWindow();
			int[] xy = new int[2];
			test_pop_layout.getLocationOnScreen(xy);
			int localHeight = pointAt.getHeight();
//			popupWindow.showAtLocation(test_pop_layout,Gravity.CENTER|Gravity.BOTTOM, 0, -localHeight); 

			break;
		case R.id.tyr:
			boolean isCarrier = GlobalApplication.getInstance().getUserData().isRealUser();
			if(isCarrier){
				Intent intent = new Intent(AddOrderZXActivity.this,AddOrderTYRActivity.class);
				Bundle bundle = new Bundle();
				bundle.putLong("orderId", orderId);//合同ID
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}else{
				Toast.makeText(getApplication(), "只有代理人才能修改托运人信息", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.addStart:
		case R.id.addEnd:	
			LayoutInflater inflater = LayoutInflater.from(AddOrderZXActivity.this);
			View view = inflater.inflate(R.layout.college_item, null);
			final EditText et = (EditText)view.findViewById(R.id.port);
			final Spinner sp = (Spinner)view.findViewById(R.id.city);
			ArrayList<SpinnerItem> items = new ArrayList<SpinnerItem>();
			for(PortCityCode p : PortCityCode.values()){
				SpinnerItem si = new SpinnerItem(p.getCode(), p.getDescription());
				items.add(si);
			}
			ArrayAdapter<SpinnerItem>  adapter = new ArrayAdapter<SpinnerItem>(this, android.R.layout.simple_spinner_item,items);
			sp.setAdapter(adapter);
			new AlertDialog.Builder(AddOrderZXActivity.this)
			.setTitle("新增港口")
			.setView(view)
			.setNegativeButton("取消", null)
			.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					  String port =et.getText().toString();
					  String cityCode =((SpinnerItem) sp.getSelectedItem()).getId();
					 addNew(port,cityCode);
					
				}

				private void addNew(final String port, final String cityCode) {
					AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
						@Override
						public void onSuccess(String arg0) {
							super.onSuccess(arg0);
							Gson gson = new Gson();
							HashMap<String, Object> result= gson.fromJson((String) arg0,
									new TypeToken<Map<String, Object>>() {}.getType());
							if (result.get("returnCode").equals("Success")) {
								LocalFileUtil.getAreas(getApplicationContext());
								Toast.makeText(AddOrderZXActivity.this, (CharSequence) result.get("message"),
										Toast.LENGTH_SHORT).show();
							}else{
								Toast.makeText(AddOrderZXActivity.this, (CharSequence) result.get("message"),
										Toast.LENGTH_SHORT).show();
							}
						}
						@Override
						public void onFailure(Throwable arg0, String arg1) {
							super.onFailure(arg0, arg1);
						}
					};
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("portCityCode", cityCode);
					params.put("portName", port);
					dataLoader.getApiResult(handler, "/mobile/ship/myShip/saveNewPort",params,"get");
					
				}
			})
			.show();
			break;
		}
		
	}


	/**
     * 设置当前日期
	 * @return 
     */
    private String getCurrentDateTime(){
       final Calendar c = Calendar.getInstance(); 
        
       mYear = c.get(Calendar.YEAR); 
       mMonth = c.get(Calendar.MONTH); 
       mDay = c.get(Calendar.DAY_OF_MONTH);
   
       String newDate = updateDateDisplay();
       return newDate;
    }

    /**
     * 更新日期显示
     */
    private String updateDateDisplay(){
    	String newDate = new StringBuilder().append(mYear).append("-")
               .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
               .append((mDay < 10) ? "0" + mDay : mDay).toString();
    	
    	return newDate;
    }
    /**
     * 设置控件选择时间
     */
    private void setCurrentText(String str){
    	currentBtn.setText(str);
    }
    /**
     * 日期控件的事件
     */ 
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() { 
   
       public void onDateSet(DatePicker view, int year, int monthOfYear, 
              int dayOfMonth) { 
           mYear = year; 
           mMonth = monthOfYear; 
           mDay = dayOfMonth; 
 
           seletedDate = updateDateDisplay();
           setCurrentText(seletedDate);
       } 
    };
    @Override 
    protected Dialog onCreateDialog(int id) { 
       switch (id) { 
       case DATE_DIALOG_ID: 
    	   getCurrentDateTime();
           return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, 
                  mDay);
       }
       return null; 
    } 
   
    @Override 
    protected void onPrepareDialog(int id, Dialog dialog) { 
       switch (id) { 
       case DATE_DIALOG_ID: 
           ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay); 
           break;
       }
    } 
    Handler dateandtimeHandler = new Handler() {
    	   
        @Override 
        public void handleMessage(Message msg) { 
            switch (msg.what) { 
            case AddOrderZXActivity.SHOW_DATAPICK: 
                showDialog(DATE_DIALOG_ID); 
                break;
            } 
        } 
    
     };


	 
	

}
