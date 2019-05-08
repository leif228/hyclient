package com.eyunda.third.activities.cargo;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonListActivity;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.ship.ShipinfoActivity;
import com.eyunda.third.activities.user.CropImageActivity;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.cargo.CargoData;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.domain.enumeric.CargoTypeCode;
import com.eyunda.third.domain.enumeric.EnumConst.PortCityCode;
import com.eyunda.third.domain.enumeric.EnumConst.RecentPeriodCode;
import com.eyunda.third.domain.enumeric.WrapStyleCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.AreaSelect;
import com.eyunda.tools.LocalFileUtil;
import com.eyunda.tools.log.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

public class CargoActivity extends CommonListActivity implements OnClickListener,OnItemSelectedListener {

	private static final int REQUEST_CODE_CARGOIMG = 2;
	private static final int SEARCH_CONSIGNER = 4;
	private Spinner cargo;
	private EditText cargoName;
	private EditText volume;
	private EditText totalPrice;
	private EditText describe;
	private EditText unitPrice;
	private TextView startPort;
	private Spinner date;
	private TextView endPort;
	private Button btnSave;
	private Data_loader data;
	private String cargoId;
	private String periodCode;
	private CargoTypeCode typeCode;
	private RelativeLayout test_pop_layout;//当前页面视图
	private ImageView cargoImg;
	private ImageLoader mImageLoader;
	private String picturePath;
	private TextView tv_weight;
	private TextView tv_price;
	private TextView tv_cargo;
	private String cargoType;
	private String cargos;
	private String periodTime;
	private Spinner pack;
	private EditText unitWeight;
	private EditText lenght;
	private EditText width;
	private EditText height;
	private EditText volume_st;
	private EditText totalWeight;
	private EditText volume_sl;
	private String packStyle;
	protected String ctWrapCount;
	protected String ctUnitWeight;
	protected String ctLong;
	protected String ctHeight;
	protected String ctWdith;
	protected String volumes;
	protected String unit;
	private TextView userName;
	private EditText receiver;
	private EditText receiver_phone;
	private EditText receiver_address;
	private String status;
	private ImageView userLogo;
	private RelativeLayout imageLayout;

	private Long consigerId;
	private String myName;
	private String myImg;
	private Button addStart;
	private Button addEnd;
	boolean isSelected =false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_cargo);
		data = new Data_loader();
		mImageLoader = ImageLoader.getInstance();
		setViews();
		getDatas(); 
		loadDate();
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("货物编辑");
		autoCal();
	}


	private void getDatas() {
		Bundle  bundle = new Bundle();
		bundle = getIntent().getExtras();
		cargoId = (String) bundle.getString("id");
		status= (String) bundle.getString("cargoStatus");

		consigerId =(Long)bundle.getLong("consigerId");
		myName =  (String) bundle.getString("myName");
		myImg = (String) bundle.getString("myImg");
		String addr = (String) bundle.getString("receiver_address");
		String phone = (String) bundle.getString("receiver_phone");

		userName.setText(myName);
		receiver.setText(myName);
		receiver_address.setText(addr);
		receiver_phone.setText(phone);
		mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +myImg, userLogo,
				MyshipAdapter.displayImageOptions);	

		CargoData cargoData  = (CargoData) bundle.getSerializable("cargoInfo");

		if(cargoData!=null){
			// consigerId = cargoData.getConsignerId();
			cargoType =cargoData.getCargoType().getDescription();
			periodTime = cargoData.getPeriodCode().toString();
			packStyle = cargoData.getCargoTypeName();
			startPort.setTag(cargoData.getStartPortNo());
			consigerId =cargoData.getAgent().getId();
			endPort.setTag(cargoData.getEndPortNo());
			cargoName.setText(cargoData.getCargoName());
			volume.setText(cargoData.getTonTeu().toString());

			startPort.setText(cargoData.getStartPortData().getFullName());
			endPort.setText(cargoData.getEndPortData().getFullName());
			unitPrice.setText(cargoData.getPrice().toString());
			totalPrice.setText(cargoData.getTransFee().toString());
			describe.setText(cargoData.getRemark());
			volume_sl.setText(cargoData.getContainerTeus().toString());
			unitWeight.setText("");
			totalWeight.setText("");
			lenght.setText("");
			width.setText("");
			height.setText("");
			volume_st.setText("");
			receiver.setText(cargoData.getReceiver());
			receiver_address.setText(cargoData.getReceAddress());
			receiver_phone.setText(cargoData.getReceMobile());
			String name =cargoData.getAgent().getTrueName();
			if(userName.equals("")){
				userName.setText(cargoData.getAgent().getNickName());
			}else
				userName.setText(name);
			if(!cargoId.equalsIgnoreCase("0") ){

				mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +cargoData.getCargoImage(), cargoImg,
						MyshipAdapter.displayImageOptions);	

				mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +cargoData.getAgent().getUserLogo(), this.userLogo,
						MyshipAdapter.displayImageOptions);	

			}
		}



	}





	private void setViews() {
		// 获取弹出的layout
		test_pop_layout = (RelativeLayout)findViewById(R.id.cargo_Layout);
		imageLayout = (RelativeLayout)findViewById(R.id.cargoImage_layout);
		cargoImg = (ImageView)findViewById(R.id.cargo_Img); //货物图片
		userLogo = (ImageView)findViewById(R.id.user_Logo); //承运人图片
		cargoName =(EditText)findViewById(R.id.cargo_Name); //货物规格
		volume = (EditText)findViewById(R.id.ctvol); //载货量/载箱量
		describe = (EditText)findViewById(R.id.cargo_describe);  //备注
		unitPrice=(EditText)findViewById(R.id.unitPrice);   //单价
		totalPrice=(EditText)findViewById(R.id.totalPrice); //运费
     
		volume_sl=(EditText)findViewById(R.id.ctCount); //数量
		unitWeight =(EditText)findViewById(R.id.ctUnit); //单重
		totalWeight =(EditText)findViewById(R.id.ctTotal); //总重
		lenght =(EditText)findViewById(R.id.ctLength); //长
		width =(EditText)findViewById(R.id.ctWidth); //宽
		height =(EditText)findViewById(R.id.ctHeight); //高
		volume_st =(EditText)findViewById(R.id.ctVolume); //体积
		receiver =(EditText)findViewById(R.id.cargo_receiver); //接货人
		receiver_phone =(EditText)findViewById(R.id.cargo_phone); //接货电话
		receiver_address =(EditText)findViewById(R.id.cargo_place); //接货地址
		addStart = (Button)findViewById(R.id.addStart);
		addEnd = (Button)findViewById(R.id.addEnd);

		cargo=(Spinner)findViewById(R.id.select_cargo);   //货类
		date = (Spinner)findViewById(R.id.select_date);    //期限
		pack = (Spinner)findViewById(R.id.select_pack);    //包装
		startPort=(TextView)findViewById(R.id.start_port);  //起始港
		endPort=(TextView)findViewById(R.id.end_port);      //终止港
		btnSave = (Button)findViewById(R.id.cargo_Save);    //保存
		userName =(TextView)findViewById(R.id.consigorName);
		tv_weight=(TextView)findViewById(R.id.cargoWeightTag);     
		tv_price=(TextView)findViewById(R.id.cargoPriceTag);     
		tv_cargo=(TextView)findViewById(R.id.cargoNameTag);    

		userLogo.setOnClickListener(this);
		cargoImg.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		cargo.setOnItemSelectedListener(this);
		date.setOnItemSelectedListener(this);
		startPort.setOnClickListener(this);
		endPort.setOnClickListener(this);
		addStart.setOnClickListener(this);
		addEnd.setOnClickListener(this);
        cargo.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					isSelected=true;
					break;

				default:
					break;
				}
				return false;
			}
		});
	}

	/**
	 * 加入自动计算总值
	 */
	private void autoCal() {
		volume.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {


			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				unit =unitPrice.getText().toString();

			}

			@Override
			public void afterTextChanged(Editable s) {
				volumes =volume.getText().toString();
				if(!TextUtils.isEmpty(volumes) &&!TextUtils.isEmpty(unit) ){
					Double total = Double.parseDouble(volumes)*Double.parseDouble(unit);
					totalPrice.setText(String.valueOf(total));

				}
			}
		});
		unitPrice.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				unit =unitPrice.getText().toString();
				if(!TextUtils.isEmpty(volumes) &&!TextUtils.isEmpty(unit) ){
					Double total = Double.parseDouble(volumes)*Double.parseDouble(unit);
					totalPrice.setText(String.valueOf(total));
				}
			}
		});
		volume_sl.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				ctWrapCount =volume_sl.getText().toString();
				//包含集装箱

				if(!TextUtils.isEmpty(ctWrapCount) &&!TextUtils.isEmpty(ctUnitWeight) ){
					Double total = Double.parseDouble(ctWrapCount)*Double.parseDouble(ctUnitWeight);			    		
					if(cargos.contains(CargoBigTypeCode.container.getDescription())){
						volume.setText(ctWrapCount);
						totalWeight.setText(String.valueOf(total));
					}else{
						totalWeight.setText(String.valueOf(total));
						volume.setText(String.valueOf(total));
					}
				}


			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				ctUnitWeight =unitWeight.getText().toString();
			}			
			@Override
			public void afterTextChanged(Editable s) {




			}
		});
		unitWeight.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}			
			@Override
			public void afterTextChanged(Editable s) {
				ctUnitWeight =unitWeight.getText().toString();
				if(!TextUtils.isEmpty(ctWrapCount) &&!TextUtils.isEmpty(ctUnitWeight)){
					Double total = Double.parseDouble(ctWrapCount)*Double.parseDouble(ctUnitWeight);
					//	totalWeight.setText(String.valueOf(total));
					double totals = round(total,3);
					totalWeight.setText(String.valueOf(totals));
					if(!cargos.contains(CargoBigTypeCode.container.getDescription())){
						volume.setText(String.valueOf(total));
					}
				}

			}
		});


		lenght.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}			
			@Override
			public void afterTextChanged(Editable s) {
				ctLong =lenght.getText().toString();
				if(!TextUtils.isEmpty(ctLong) &&!TextUtils.isEmpty(ctHeight) &&!TextUtils.isEmpty(ctWdith)){
					Double total = Double.parseDouble(ctLong)*Double.parseDouble(ctWdith)*Double.parseDouble(ctHeight);
					double totals = round(total,3);
					volume_st.setText(String.valueOf(totals));
				}

			}
		});
		height.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}			
			@Override
			public void afterTextChanged(Editable s) {
				ctHeight =height.getText().toString();
				if(!TextUtils.isEmpty(ctLong) &&!TextUtils.isEmpty(ctHeight) &&!TextUtils.isEmpty(ctWdith)){
					Double total = Double.parseDouble(ctLong)*Double.parseDouble(ctWdith)*Double.parseDouble(ctHeight);
					double totals = round(total,3);
					volume_st.setText(String.valueOf(totals));
				}	
			}
		});
		width.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}			
			@Override
			public void afterTextChanged(Editable s) {
				ctWdith = width.getText().toString();
				if(!TextUtils.isEmpty(ctLong) &&!TextUtils.isEmpty(ctHeight) &&!TextUtils.isEmpty(ctWdith)){
					Double total = Double.parseDouble(ctLong)*Double.parseDouble(ctWdith)*Double.parseDouble(ctHeight);
					double totals = round(total,3);
					volume_st.setText(String.valueOf(totals));
				}
			}
		});

	}
	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(Double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}

		BigDecimal b = null == v ? new BigDecimal("0.0") : new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * spinner绑定数据
	 */
	@Override
	protected void loadDate() {

		//货类
		ArrayList<SpinnerItem>    cargoList = new ArrayList<SpinnerItem>();
		ArrayList<String>    lists= new ArrayList<String>();
		ArrayList<CargoTypeCode> cargoCode = new ArrayList<CargoTypeCode>();
		for (CargoTypeCode e : CargoTypeCode.values()) {
			cargoCode.add(e);
			lists.add(e.getDescription());
		}
		for(int i=0;i<cargoCode.size();i++){
			SpinnerItem sp = new SpinnerItem(cargoCode.get(i), lists.get(i));
			cargoList.add(sp);
		}     
		ArrayAdapter<SpinnerItem>  Cargo_adapter = new ArrayAdapter<SpinnerItem>(this, R.layout.spinner_item,R.id.contentTextView,cargoList);
		cargo.setAdapter(Cargo_adapter);
		//下拉默认选项
		for(int j=0;j<lists.size();j++){
			if(cargoType!=null){
				if(cargoType.equals(lists.get(j))){
					cargo.setSelection(j);
				}
			}
		}

		//期限
		ArrayList<SpinnerItem>    cargoListDate = new ArrayList<SpinnerItem>();
		ArrayList<String>   times= new ArrayList<String>();

		for(RecentPeriodCode p : RecentPeriodCode.values()){
			SpinnerItem  sp = new SpinnerItem(p.getCode(),p.getDescription());
			times.add(p.getCode());
			cargoListDate.add(sp);
		}
		ArrayAdapter<SpinnerItem>  date_adapter = new ArrayAdapter<SpinnerItem>(this, R.layout.spinner_item,R.id.contentTextView,cargoListDate);
		date.setAdapter(date_adapter);
		//下拉默认选项
		for(int k=0;k<times.size();k++){
			if(periodTime!=null){
				if(periodTime.equals(times.get(k))){
					date.setSelection(k);
				}
			}
		}
		//包装

		ArrayList<SpinnerItem> mTypes = new ArrayList<SpinnerItem>();
		ArrayList<String>   packList= new ArrayList<String>();
		for(WrapStyleCode e : WrapStyleCode.values()){
			SpinnerItem sp = new SpinnerItem(e+"",e.getDescription());
			packList.add(e.getDescription());
			mTypes.add(sp);
		}
		ArrayAdapter<SpinnerItem> pack_adapter = new ArrayAdapter<SpinnerItem>(
				this, R.layout.spinner_item,R.id.contentTextView, mTypes);
		pack.setAdapter(pack_adapter);
		//下拉默认选项
		for(int j=0;j<packList.size();j++){
			if(packStyle!=null){
				if(packStyle.equals(packList.get(j))){
					pack.setSelection(j);
				}
			}
		}


	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_Logo:
			Intent intent = new Intent(this,FindTYRActivity.class);
			intent.putExtra("id", cargoId);
			startActivityForResult(intent, SEARCH_CONSIGNER);
			break;
		case R.id.cargo_Img:
			Intent i = new Intent(Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, REQUEST_CODE_CARGOIMG);
			break;
		case R.id.start_port: //起始港
		case R.id.end_port://终止港
			//选择结果显示的地方
			AreaSelect asStart = new AreaSelect(this, startPort);
			AreaSelect asEnd = new AreaSelect(this, endPort);
			AreaSelect pointAt =null;
			if(v.getId() == R.id.start_port){
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
		case R.id.addStart:
		case R.id.addEnd:
			LayoutInflater inflater = LayoutInflater.from(CargoActivity.this);
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
			new AlertDialog.Builder(CargoActivity.this)
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
								Toast.makeText(CargoActivity.this, (CharSequence) result.get("message"),
										Toast.LENGTH_SHORT).show();
							}else{
								Toast.makeText(CargoActivity.this, (CharSequence) result.get("message"),
										Toast.LENGTH_SHORT).show();
							}
						}
						@Override
						public void onFailure(Throwable arg0, String arg1) {
							// TODO Auto-generated method stub
							super.onFailure(arg0, arg1);
							Toast.makeText(CargoActivity.this, arg1,
									Toast.LENGTH_SHORT).show();
						}
					};
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("portCityCode", cityCode);
					params.put("portName", port);
					data.getApiResult(handler, "/mobile/ship/myShip/saveNewPort",params,"get");

				}
			})
			.show();

			break;
		case R.id.cargo_Save: //保存信息
			if(TextUtils.isEmpty(startPort.getText().toString())){
				Toast.makeText(this, "起运港不能为空！", Toast.LENGTH_SHORT).show();
				return;

			}else if(TextUtils.isEmpty(endPort.getText().toString())){
				Toast.makeText(this, "到达港不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}else if(TextUtils.isEmpty(volume_sl.getText())){
				Toast.makeText(this, "数量不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}else if(TextUtils.isEmpty(unitWeight.getText())){
				Toast.makeText(this, "单重不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}else if(TextUtils.isEmpty(totalWeight.getText())){
				Toast.makeText(this, "总重不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}else if(TextUtils.isEmpty(lenght.getText())){
				Toast.makeText(this, "长不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}else if(TextUtils.isEmpty(width.getText())){
				Toast.makeText(this, "宽不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}else if(TextUtils.isEmpty(height.getText())){
				Toast.makeText(this, "高不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}else if(TextUtils.isEmpty(volume.getText())){
				Toast.makeText(this, "箱量(货量)不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}else if(TextUtils.isEmpty(unitPrice.getText())){
				Toast.makeText(this, "单价不能为空！", Toast.LENGTH_SHORT).show();
				return;

			}else if(TextUtils.isEmpty(totalPrice.getText())){
				Toast.makeText(this, "运费不能为空！", Toast.LENGTH_SHORT).show();
				totalPrice.requestFocus();
				return;
			}
			Map<String,Object> apiParams = new HashMap<String, Object>();

			if (picturePath != null ) 
				apiParams.put("cargoImageFile", new File(picturePath));// 图片


			if(status!=null)
				apiParams.put("cargoStatus", status);


			apiParams.put("id", Long.parseLong(cargoId));
			UserData  user = GlobalApplication.getInstance().getUserData();
//			if(user!=null && !user.isCarrier()){ //不是代理人操作
//				apiParams.put("consignerId",user.getId());
//			}else { //代理人添加其他托运人
				apiParams.put("consignerId",consigerId);
//
//
//			}
			apiParams.put("cargoName", cargoName.getText()); 
			apiParams.put("tonTeu", volume.getText().toString());
			apiParams.put("remark", describe.getText().toString());
			apiParams.put("price", unitPrice.getText().toString());
			apiParams.put("periodCode",periodCode);
			apiParams.put("cargoType",typeCode);
			apiParams.put("startPortNo",startPort.getTag().toString());
			apiParams.put("endPortNo",endPort.getTag().toString());
			apiParams.put("transFee", Double.valueOf(totalPrice.getText().toString()));

			apiParams.put("wrapStyle", ((SpinnerItem)pack.getSelectedItem()).getId());
			apiParams.put("unitWeight", unitWeight.getText().toString());
			apiParams.put("fullWeight", totalWeight.getText().toString());
			apiParams.put("ctlLength", lenght.getText().toString());
			apiParams.put("ctlWidth", width.getText().toString());
			apiParams.put("ctlHeight", height.getText().toString());
			apiParams.put("ctlVolume", volume_st.getText().toString());		
			apiParams.put("wrapCount", volume_sl.getText().toString());	


			apiParams.put("receiver", receiver.getText().toString());				
			apiParams.put("receMobile", receiver_phone.getText().toString());				
			apiParams.put("receAddress", receiver_address.getText().toString());				
			Log.v("params",apiParams.toString());

			data.getApiResult(handler, "/mobile/cargo/myCargo/saveMyCargo",apiParams,"post");
			break;
		}


	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//调用系统图库选择图片
		if (requestCode == REQUEST_CODE_CARGOIMG && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int	columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			// 进入头像裁剪页面
			Intent intent = new Intent(CargoActivity.this,CropImageActivity.class);
			intent.putExtra("picPath", selectedImage);
			startActivityForResult(intent,ShipinfoActivity.REQUEST_CODE_CROPIMG);
			Log.v("path:", picturePath);
			cursor.close();
		}
		// 裁剪后图片
		if (resultCode == RESULT_OK && requestCode == ShipinfoActivity.REQUEST_CODE_CROPIMG) {
			String cropedImgPath = data.getStringExtra("cropedImgPath");
			if (cropedImgPath != null) {
				picturePath = cropedImgPath;
				cargoImg.setImageBitmap(BitmapFactory
						.decodeFile(cropedImgPath));
			} else {
				Toast.makeText(CargoActivity.this, "裁剪图片失败", Toast.LENGTH_SHORT).show();
			}

		}
		if(resultCode == RESULT_OK &&requestCode ==SEARCH_CONSIGNER){
			String  code  =  data.getStringExtra("cargoStatus");
			UserData user =(UserData) data.getSerializableExtra("userData");
			this.status=code;
			consigerId = user.getId();
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +user.getUserLogo(), this.userLogo,
					MyshipAdapter.displayImageOptions);	
			String userName =user.getTrueName();
			if(userName.equals("")){
				this.userName.setText(user.getLoginName());
				this.receiver.setText(user.getLoginName());
			}else
				this.userName.setText(userName);
			this.receiver.setText(user.getTrueName());

			this.receiver_address.setText(user.getAddress());
			this.receiver_phone.setText(user.getMobile());

		}




	}
	AsyncHttpResponseHandler  handler = new AsyncHttpResponseHandler(){

		@Override
		public void onStart() {
			super.onStart();
			dialog = dialogUtil.loading("通知", "数据保存中，请稍候...");
		}
		@Override
		public void onSuccess(String arg0) {
			super.onSuccess(arg0);
			dialog.dismiss();
			Gson gson = new Gson();
			HashMap<String, Object> result= gson.fromJson((String) arg0,
					new TypeToken<Map<String, Object>>() {}.getType());
			if (result.get("returnCode").equals("Success")) {

				Toast.makeText(CargoActivity.this, (CharSequence) result.get("message"),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(CargoActivity.this,CargoListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("success", true);
				startActivity(intent);
				finish();

			}else{
				Toast.makeText(CargoActivity.this, (CharSequence) result.get("message"),
						Toast.LENGTH_SHORT).show();
			}
		}
		@Override
		public void onFailure(Throwable arg0, String content) {
			super.onFailure(arg0, content);
			dialog.dismiss();
			if (content != null && content.equals("socket time out")) {
				Toast.makeText(CargoActivity.this, "连接服务器超时",
						Toast.LENGTH_LONG).show();
			}else if(content!=null){
				Toast.makeText(CargoActivity.this, "提交失败",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(CargoActivity.this, content,Toast.LENGTH_LONG).show();	
			}
		}

	};


	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.select_cargo:
			typeCode = ((SpinnerItem)cargo.getSelectedItem()).getCargoType();
			cargos =cargo.getItemAtPosition(position).toString();
			// Toast.makeText(CargoActivity.this,"点击的是:"+ typeCode,Toast.LENGTH_SHORT).show();
			final String jzx =CargoBigTypeCode.container.getDescription();
			if(isSelected){
			String name[] =cargos.split("\\.");  
			if(cargos.contains(jzx)) {
				cargoName.setText(name[1]);
				tv_cargo.setText("规格");
				tv_weight.setText("箱量(个)");
				tv_price.setText("运价(元/个)");
				volume.setText(volume_sl.getText());

			}else{
				cargoName.setText(name[1]);
				tv_cargo.setText("货名");
				tv_weight.setText("货量(吨)");
				tv_price.setText("运价(元/吨)");

				volume.setText(totalWeight.getText());
			}
			}
			break;

		case R.id.select_date:
			periodCode = ((SpinnerItem)date.getSelectedItem()).getId();
			//	Toast.makeText(CargoActivity.this,"点击的是:"+ periodCode,Toast.LENGTH_SHORT).show();
			break;

		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}


}
