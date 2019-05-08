package com.eyunda.third.activities.cargo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;






import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.order.OrderPreviewActivity;
import com.eyunda.third.activities.order.SimpleOrderActivity;
import com.eyunda.third.activities.user.CropImageActivity;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.cargo.CargoContainerData;
import com.eyunda.third.domain.cargo.CargoData;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.domain.enumeric.CargoStatusCode;
import com.eyunda.third.domain.enumeric.CargoTypeCode;
import com.eyunda.third.domain.enumeric.EnumConst.PortCityCode;
import com.eyunda.third.domain.enumeric.SourceCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.AreaSelect;
import com.eyunda.tools.LocalFileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

public class AddCargoActivity extends CommonActivity {
	private static final int REQUEST_CODE_CARGOIMG = 2;
	public static final int REQUEST_CODE_CROPIMG = 3;
	private Data_loader mDataLoader;
	private ImageLoader mImageLoader;
	private DialogUtil mDialogUtil;
	private ProgressDialog mDialog;
	
	@Bind(R.id.ownerSpinner) Spinner ownerSpinner;
	@Bind(R.id.cargoCate) Spinner cargoCate;
	@Bind(R.id.start_port) TextView startPort;
	@Bind(R.id.end_port)  TextView endPort;
	@Bind(R.id.cargoName)  EditText cargoName;
	@Bind(R.id.cargoNameTag)  TextView cargoNameTag;
	@Bind(R.id.cargoWeightTag)  TextView cargoWeightTag;
	@Bind(R.id.cargoPriceTag)  TextView cargoPriceTag;
	@Bind(R.id.cargoLogo) ImageView cargoLogo;
	@Bind(R.id.simplelayout) RelativeLayout mContainer;
	
	@Bind(R.id.cargoWeight) EditText cargoWeight;
	@Bind(R.id.cargoPrice) EditText cargoPrice;
	@Bind(R.id.totalPrice) EditText totalPrice;
	@Bind(R.id.priceDesc) EditText priceDesc;
	@Bind(R.id.containerBoxA) LinearLayout containerBoxA;
	@Bind(R.id.containerBoxB) LinearLayout containerBoxB;
	@Bind(R.id.price1) EditText price1;
	@Bind(R.id.price2) EditText price2;
	@Bind(R.id.price3) EditText price3;
	@Bind(R.id.price4) EditText price4;
	@Bind(R.id.price5) EditText price5;
	@Bind(R.id.price6) EditText price6;
	
	@Bind(R.id.tonTeu6) EditText tonTeu6;
	@Bind(R.id.tonTeu5) EditText tonTeu5;
	@Bind(R.id.tonTeu4) EditText tonTeu4;
	@Bind(R.id.tonTeu3) EditText tonTeu3;
	@Bind(R.id.tonTeu2) EditText tonTeu2;
	@Bind(R.id.tonTeu1) EditText tonTeu1;
	@Bind(R.id.pubStatus) RadioGroup pubStatus;
	@Bind(R.id.privateCargo) RadioButton privateCargo;
	@Bind(R.id.publicCargo) RadioButton publicCargo;

	private int source;
	private Long shipId, cargoId, brokerId;
	
	boolean isSelected = false;
	String selectCargoCate;
	private String picturePath;//货物图片路径
	
	private UserData currentUser,owner;
	private Map<Long,UserData> owners;
	
	CargoData cargoData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_cargo);
		ButterKnife.bind(this);
		Intent intent = getIntent();
		source = intent.getIntExtra("source", SourceCode.fromAddCargo.ordinal());//1对船说我有货要运，2对人说我有货要运，3对网站说我有货要运。4编辑货物
		brokerId = intent.getLongExtra("brokerId", 0L);//大于0表示从个人网站而来
		shipId = intent.getLongExtra("shipId", 0L);//大于0表示从船舶详情而来
		cargoId = intent.getLongExtra("cargoId", 0L);//大于0表示编辑货物

		owners = new HashMap<Long, UserData>();
		currentUser = GlobalApplication.getInstance().getUserData();
		
		mDataLoader=new Data_loader();
		mImageLoader = ImageLoader.getInstance();
		mDialogUtil = new DialogUtil(this);
		cargoData = new CargoData();
		//getSpinner();
		initData();
	}
	

	private void initData(){
		final HashMap<String, Object> params  = new HashMap<String, Object>();
		params.put("id", cargoId);
		final String uri = "/mobile/cargo/myCargo/getMyCargo";
		//读取接口数据
		AsyncHttpResponseHandler ahr = new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				//读取数据
				ConvertData cd = new ConvertData(arg0,uri, params);
				if (cd.getReturnCode().equals("Success")) {
					Map r = (HashMap<String, Object>) cd.getContent();
					
					List<Map<String,Object>> agentUsers = (ArrayList<Map<String,Object>>) r.get("agentUsers");
					int size = agentUsers.size();
					final List<SpinnerItem> mUsers = new ArrayList<SpinnerItem>();
					if (size > 0) {
						//赋值spinner
						for (Map<String,Object> d : agentUsers) {
							UserData u = new UserData(d);
							owners.put(u.getId(), u);
							SpinnerItem sp  = null;
							if(TextUtils.isEmpty(u.getTrueName())){
								sp = new SpinnerItem(u.getId().toString(), u.getLoginName());
							}else{
								sp = new SpinnerItem(u.getId().toString(), u.getTrueName());
							}
							//cargoTypeNams.add(d.getDescription());
							mUsers.add(sp);
						}

					}
					ArrayAdapter<SpinnerItem> cargoAdapter = new ArrayAdapter<SpinnerItem>(getApplicationContext(), R.layout.spinner_item,R.id.contentTextView, mUsers);
					ownerSpinner.setAdapter(cargoAdapter);
					
					cargoData = new CargoData((Map<String,Object>)r.get("cargoData"));
					
					initView();
					setSpinner();
	
				}else{
					Toast.makeText(getApplicationContext(), cd.getMessage(), Toast.LENGTH_SHORT).show();
				}

			}
			@Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);
				Toast.makeText(getApplicationContext(), "获取货主信息失败，请稍后再试", Toast.LENGTH_SHORT).show();

			}
		};
		
		mDataLoader.getApiResult(ahr, uri,params,"get");
	}
	
	private void initView(){
		cargoName.setText(cargoData.getCargoName());
		//货物图片
		if(TextUtils.isEmpty(cargoData.getCargoImage())){
			if(!TextUtils.isEmpty(cargoData.getCargoTypeName())){
				mImageLoader.displayImage(ApplicationConstants.SERVER_URL+ "/img/cargoImage/" +cargoData.getCargoTypeName()+".jpg", 
					cargoLogo,GlobalApplication.displayImageOptions);
			}
		}else{
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL +cargoData.getCargoImage(), cargoLogo, GlobalApplication.displayImageOptions);
		}

		cargoWeight.setText(cargoData.getTonTeu().toString());
		cargoPrice.setText(cargoData.getPrice().toString());
		totalPrice.setText(cargoData.getTransFee().toString());
		priceDesc.setText(cargoData.getRemark());
		
		
		//是不是集装箱
		if(cargoData.isContainer()){
			List<CargoContainerData> ccds = cargoData.getContainers();
			if(ccds.size() == 6){
				tonTeu1.setText(ccds.get(0).getTonTeu().toString());
				tonTeu2.setText(ccds.get(1).getTonTeu().toString());
				tonTeu3.setText(ccds.get(2).getTonTeu().toString());
				tonTeu4.setText(ccds.get(3).getTonTeu().toString());
				tonTeu5.setText(ccds.get(4).getTonTeu().toString());
				tonTeu6.setText(ccds.get(5).getTonTeu().toString());
				
				price1.setText(ccds.get(0).getPrice().toString());
				price2.setText(ccds.get(1).getPrice().toString());
				price3.setText(ccds.get(2).getPrice().toString());
				price4.setText(ccds.get(3).getPrice().toString());
				price5.setText(ccds.get(4).getPrice().toString());
				price6.setText(ccds.get(5).getPrice().toString());
				
			}else{
				Toast.makeText(getApplicationContext(), "集装箱数据错误，请修正", Toast.LENGTH_LONG).show();
			}
			containerBoxA.setVisibility(View.GONE);
			containerBoxB.setVisibility(View.VISIBLE);
		}else{
			containerBoxB.setVisibility(View.GONE);
			containerBoxA.setVisibility(View.VISIBLE);
		}
		
		startPort.setText(cargoData.getStartPortData().getFullName());
		startPort.setTag(cargoData.getStartPortData().getPortNo());
		endPort.setText(cargoData.getEndPortData().getFullName());
		endPort.setTag(cargoData.getEndPortData().getPortNo());
		
		publicCargo.setText(CargoStatusCode.publish.getDescription());
		publicCargo.setTag(CargoStatusCode.publish);
		privateCargo.setText(CargoStatusCode.nopublish.getDescription());
		privateCargo.setTag(CargoStatusCode.nopublish);
		if(cargoData.getCargoStatus() == null){
			cargoData.setCargoStatus(CargoStatusCode.nopublish);
		}
		pubStatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                //获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton)AddCargoActivity.this.findViewById(radioButtonId);
                cargoData.setCargoStatus((CargoStatusCode)rb.getTag());
            }
        });
		
		if(cargoData.getCargoStatus() == CargoStatusCode.nopublish){
			privateCargo.setChecked(true);
			publicCargo.setChecked(false);

		}else if(cargoData.getCargoStatus() == CargoStatusCode.publish){
			privateCargo.setChecked(false);
			publicCargo.setChecked(true);
		}else{
			privateCargo.setChecked(true);
			publicCargo.setChecked(false);
		}
	}

	private void setSpinner(){
		// 货类
		List<SpinnerItem> cargos = new ArrayList<SpinnerItem>();
		List<String> cargoTypeNams = new ArrayList<String>();
		Boolean flag = true;
		for (CargoTypeCode d : CargoTypeCode.values()) {
			String jzx = CargoBigTypeCode.container.getDescription();//集装箱
			String name = d.getDescription();
			SpinnerItem sp = null;
			if (!name.contains(jzx)) {
				sp = new SpinnerItem(d.name(), d.getDescription());
				cargoTypeNams.add(d.getDescription());
				cargos.add(sp);
			}else {
				if(flag){
					sp = new SpinnerItem(d.name(), d.getCargoBigType().getDescription());
					cargoTypeNams.add( d.getCargoBigType().getDescription());
					cargos.add(sp);
					flag = false;
				}
			}

		}
		ArrayAdapter<SpinnerItem> cargoAdapter = new ArrayAdapter<SpinnerItem>(this, R.layout.spinner_item,R.id.contentTextView, cargos);
		cargoCate.setAdapter(cargoAdapter);
		
		
		int ownerSize = ownerSpinner.getCount();
		int cateSize = cargoCate.getCount();
		if(cargoData.getCargoType() ==null){
			ownerSpinner.setSelection(0);
			cargoCate.setSelection(0);
		}else{
			for(int i=0; i<ownerSize; i++){
				SpinnerItem osi = (SpinnerItem)ownerSpinner.getItemAtPosition(i);
				if(osi.getId().equals(cargoData.getOwnerId().toString())){
					ownerSpinner.setSelection(i);
					osi = null;
					break;
				}
				
			}
			for(int j=0; j<cateSize; j++){
				SpinnerItem csi =  (SpinnerItem)cargoCate.getItemAtPosition(j);
				if(csi.getValue().equals(cargoData.getCargoType().getDescription())){
					cargoCate.setSelection(j);
					csi = null;
					break;
				}
			}
		}

		
	}
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("货物编辑");
		autoCal();
	}
	
	TextWatcher myTextWatche = new TextWatcher() {			
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}			
		@Override
		public void afterTextChanged(Editable s) {
			String result = calcTotal();
			totalPrice.setText(result);
			cargoData.setPrice(Double.parseDouble(result));
		}
	};
	
	private void autoCal() {
		cargoWeight.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}			
			@Override
			public void afterTextChanged(Editable s) {
				String result = calc();
				totalPrice.setText(result);
				cargoData.setPrice(Double.parseDouble(result));
			}
		});
		cargoPrice.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}			
			@Override
			public void afterTextChanged(Editable s) {
				String result = calc();
				totalPrice.setText(result);
				cargoData.setPrice(Double.parseDouble(result));
			}
		});
		tonTeu1.addTextChangedListener(myTextWatche);
		tonTeu2.addTextChangedListener(myTextWatche);
		tonTeu3.addTextChangedListener(myTextWatche);
		tonTeu4.addTextChangedListener(myTextWatche);
		tonTeu5.addTextChangedListener(myTextWatche);
		tonTeu6.addTextChangedListener(myTextWatche);
		price1.addTextChangedListener(myTextWatche);
		price2.addTextChangedListener(myTextWatche);
		price3.addTextChangedListener(myTextWatche);
		price4.addTextChangedListener(myTextWatche);
		price5.addTextChangedListener(myTextWatche);
		price6.addTextChangedListener(myTextWatche);

	}
	
	private String calcTotal(){
		String tonTeu1Str = tonTeu1.getText().toString();
		String tonTeu2Str = tonTeu2.getText().toString();
		String tonTeu3Str = tonTeu3.getText().toString();
		String tonTeu4Str = tonTeu4.getText().toString();
		String tonTeu5Str = tonTeu5.getText().toString();
		String tonTeu6Str = tonTeu6.getText().toString();

		String price1Str = price1.getText().toString();
		String price2Str = price2.getText().toString();
		String price3Str = price3.getText().toString();
		String price4Str = price4.getText().toString();
		String price5Str = price5.getText().toString();
		String price6Str = price6.getText().toString();
		Double res1 = 0.0;
		Double res2 = 0.0;
		Double res3 = 0.0;
		Double res4 = 0.0;
		Double res5 = 0.0;
		Double res6 = 0.0;
		if(!TextUtils.isEmpty(tonTeu1Str) && !TextUtils.isEmpty(price1Str)){
			res1 = Double.parseDouble(tonTeu1Str) * Double.parseDouble(price1Str);
		}
		if(!TextUtils.isEmpty(tonTeu2Str) && !TextUtils.isEmpty(price2Str)){
			res2 = Double.parseDouble(tonTeu2Str) * Double.parseDouble(price2Str);
		}
		if(!TextUtils.isEmpty(tonTeu3Str) && !TextUtils.isEmpty(price3Str)){
			res3 = Double.parseDouble(tonTeu3Str) * Double.parseDouble(price3Str);
		}
		if(!TextUtils.isEmpty(tonTeu4Str) && !TextUtils.isEmpty(price4Str)){
			res4 = Double.parseDouble(tonTeu4Str) * Double.parseDouble(price4Str);
		}
		if(!TextUtils.isEmpty(tonTeu5Str) && !TextUtils.isEmpty(price5Str)){
			res5 = Double.parseDouble(tonTeu5Str) * Double.parseDouble(price5Str);
		}
		if(!TextUtils.isEmpty(tonTeu6Str) && !TextUtils.isEmpty(price6Str)){
			res6 = Double.parseDouble(tonTeu6Str) * Double.parseDouble(price6Str);
		}
		Double total = res1+res2+res3+res4+res5+res6;
		return String.valueOf(round(total,3));
		
	}
	private String calc(){
		String weight = cargoWeight.getText().toString();
		String price = cargoPrice.getText().toString();
		double total = 0.0;
		if(!TextUtils.isEmpty(weight) && !TextUtils.isEmpty(price))
			total = round(Double.parseDouble(cargoWeight.getText().toString())*Double.parseDouble(cargoPrice.getText().toString()),3);
		return String.valueOf(total);
	}
	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v 需要四舍五入的数字
	 * @param scale 小数点后保留几位
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


	//设定起运港终止港口
	@OnClick({R.id.start_port,R.id.end_port}) void startPort(View v){
		AreaSelect asStart = new AreaSelect(this, startPort);
		AreaSelect asEnd = new AreaSelect(this, endPort);
		AreaSelect pointAt = null;
		if (v.getId() == R.id.start_port) {
			pointAt = asStart;
		} else {
			pointAt = asEnd;
		}
//		PopupWindow popupWindow = pointAt.makePopupWindow();
		int[] xy = new int[2];
		mContainer.getLocationOnScreen(xy);
		int localHeight = pointAt.getHeight();
//		popupWindow.showAtLocation(mContainer, Gravity.CENTER | Gravity.BOTTOM, 0, -localHeight);
	}
	//重置货主信息
	@OnItemSelected(R.id.ownerSpinner) void onOwnerItemSelected(int position){
		owner = owners.get(Long.valueOf(((SpinnerItem) ownerSpinner.getSelectedItem()).getId()));
		cargoData.setOwnerId(Long.valueOf(((SpinnerItem) ownerSpinner.getSelectedItem()).getId()));
	}
	//货类下拉
	@OnItemSelected(R.id.cargoCate) void onCateItemSelected(int position){
		SpinnerItem si = (SpinnerItem)cargoCate.getItemAtPosition(position);
		selectCargoCate = si.toString();
		if(selectCargoCate.equals("集装箱")){
			cargoData.setCargoType(CargoTypeCode.container20e);
		}else{
			cargoData.setCargoType(CargoTypeCode.valueOf(si.getId()));
		}


			if (selectCargoCate.contains(CargoBigTypeCode.container.getDescription())) {
				containerBoxA.setVisibility(View.GONE);
				containerBoxB.setVisibility(View.VISIBLE);
				cargoName.setText(selectCargoCate);
				//cleanData();
			} else {
				containerBoxB.setVisibility(View.GONE);
				containerBoxA.setVisibility(View.VISIBLE);
				cargoNameTag.setText("货名");
				cargoWeightTag.setText("货量(吨)");
				cargoPriceTag.setText("运价(元/吨)");
				cargoName.setText(selectCargoCate.substring(selectCargoCate.indexOf(".")+1));
				//cleanData();
			}

	}
	private void cleanData(){
		tonTeu1.setText("0");
		tonTeu2.setText("0");
		tonTeu3.setText("0");
		tonTeu4.setText("0");
		tonTeu5.setText("0");
		tonTeu6.setText("0");
		
		price1.setText("0.0");
		price2.setText("0.0");
		price3.setText("0.0");
		price4.setText("0.0");
		price5.setText("0.0");
		price6.setText("0.0");
		cargoPrice.setText("0.0");
		cargoWeight.setText("0");
		totalPrice.setText("0.0");
	}
	//货物图片
	@OnClick(R.id.cargoLogo) void cargoLogo(View v){
		Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, REQUEST_CODE_CARGOIMG);
	}
	//添加港口
	@OnClick({R.id.addEnd,R.id.addStart}) void addEnd(View v){
	    LayoutInflater inflater = LayoutInflater.from(AddCargoActivity.this);
		View view = inflater.inflate(R.layout.college_item, null);
		final EditText et = (EditText) view.findViewById(R.id.port);
		final Spinner sp = (Spinner) view.findViewById(R.id.city);
		ArrayList<SpinnerItem> items = new ArrayList<SpinnerItem>();
		for (PortCityCode p : PortCityCode.values()) {
			SpinnerItem si = new SpinnerItem(p.getCode(), p.getDescription());
			items.add(si);
		}
		ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<SpinnerItem>(this,
				android.R.layout.simple_spinner_item, items);
		sp.setAdapter(adapter);
		new AlertDialog.Builder(AddCargoActivity.this).setTitle("新增港口").setView(view)
				.setNegativeButton("取消", null).setPositiveButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String port = et.getText().toString();
						String cityCode = ((SpinnerItem) sp.getSelectedItem()).getId();
						addNew(port, cityCode);

					}

					private void addNew(final String port, final String cityCode) {
						AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String arg0) {
								super.onSuccess(arg0);
								Gson gson = new Gson();
								HashMap<String, Object> result = gson.fromJson((String) arg0,
										new TypeToken<Map<String, Object>>() {
								}.getType());
								if (result.get("returnCode").equals("Success")) {
									// 当新增港口保存成功后，重新写入地区文件到本地
									LocalFileUtil.getAreas(getApplicationContext());
									Toast.makeText(AddCargoActivity.this, (CharSequence) result.get("message"),
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(AddCargoActivity.this, (CharSequence) result.get("message"),
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
						mDataLoader.getApiResult(handler, "/mobile/ship/myShip/saveNewPort", params, "get");

					}
				}).show();
	
	}
	//保存货物
	@OnClick(R.id.btnSave) void btnSave(View v) {
		if(currentUser != null){
			final HashMap<String, Object> map  = new HashMap<String, Object>();
			final String saveUrl = "/mobile/cargo/saveMyCargo";
			//读取数据
			//货类
			String typeCode = ((SpinnerItem) cargoCate.getSelectedItem()).getId();
			map.put("cargoType", typeCode);
			if(CargoTypeCode.container20e.name().equals(typeCode)){
				//集装箱
				map.put("cargoName1", CargoTypeCode.container20e.getShortDesc());
				map.put("cargoName2", CargoTypeCode.container20f.getShortDesc());
				map.put("cargoName3", CargoTypeCode.container40e.getShortDesc());
				map.put("cargoName4", CargoTypeCode.container20f.getShortDesc());
				map.put("cargoName5", CargoTypeCode.container45e.getShortDesc());
				map.put("cargoName6", CargoTypeCode.container45f.getShortDesc());
				map.put("cargoName1", CargoTypeCode.container20e.getShortDesc());
				//保存六个属性
				map.put("tonTeu1", Long.parseLong(tonTeu1.getText().toString()));
				map.put("tonTeu2", Long.parseLong(tonTeu2.getText().toString()));
				map.put("tonTeu3", Long.parseLong(tonTeu3.getText().toString()));
				map.put("tonTeu4", Long.parseLong(tonTeu4.getText().toString()));
				map.put("tonTeu5", Long.parseLong(tonTeu5.getText().toString()));
				map.put("tonTeu6", Long.parseLong(tonTeu6.getText().toString()));
				map.put("price1", Double.valueOf(price1.getText().toString()));
				map.put("price2", Double.valueOf(price2.getText().toString()));
				map.put("price3", Double.valueOf(price3.getText().toString()));
				map.put("price4", Double.valueOf(price4.getText().toString()));
				map.put("price5", Double.valueOf(price5.getText().toString()));
				map.put("price6", Double.valueOf(price6.getText().toString()));
				//货名
				map.put("cargoName", "集装箱");
				//货量
				map.put("tonTeu", 0);
			}else{
				//货名
				map.put("cargoName", cargoName.getText());
				//货量
				map.put("tonTeu", Integer.parseInt(cargoWeight.getText().toString()));
			}

			// 货类
			map.put("cargoType", ((SpinnerItem) cargoCate.getSelectedItem()).getId());
			
			if (picturePath != null) {
				map.put("cargoImageFile", new File(picturePath));// 图片Logo
			}
			


			map.put("id", cargoData.getId());
			map.put("agentId", currentUser.getId());
			map.put("cargoStatus", cargoData.getCargoStatus());
			map.put("periodCode", cargoData.getPeriodCode());
			map.put("receiver", owner.getTrueName());
			map.put("receMobile", owner.getMobile());
			map.put("receAddress", owner.getAddress());	
			
			String ownerIdStr = ((SpinnerItem) ownerSpinner.getSelectedItem()).getId();
			map.put("ownerId", Long.parseLong(ownerIdStr));
			
			map.put("startPortNo", startPort.getTag());
			map.put("endPortNo", endPort.getTag());
			map.put("cargoType", cargoData.getCargoType());

			map.put("price", Double.parseDouble(cargoPrice.getText().toString()));
			map.put("transFee", Double.parseDouble(totalPrice.getText().toString()));
			map.put("remark", priceDesc.getText().toString());
			//如果是对船舶说我有货要运
			map.put("shipId", shipId);
			//如果是对人说我有货要运
			map.put("brokerId", brokerId);
			map.put("source", source);
			if(TextUtils.isEmpty(cargoName.getText().toString())){
				Toast.makeText(getApplicationContext(), "请输入货名", Toast.LENGTH_LONG).show();
				return;
			}
			if(TextUtils.isEmpty(cargoWeight.getText().toString())){
				Toast.makeText(getApplicationContext(), "请输入货量", Toast.LENGTH_LONG).show();
				return;
			}
			
			if(TextUtils.isEmpty(cargoPrice.getText().toString())){
				Toast.makeText(getApplicationContext(), "请输入运价", Toast.LENGTH_LONG).show();
				return;
			}
			if(TextUtils.isEmpty(totalPrice.getText().toString())){
				Toast.makeText(getApplicationContext(), "请输入运费", Toast.LENGTH_LONG).show();
				return;
			}
			if(TextUtils.isEmpty(endPort.getText().toString())){
				Toast.makeText(getApplicationContext(), "请选择到达港口", Toast.LENGTH_LONG).show();
				return;
			}
			if(TextUtils.isEmpty(endPort.getText().toString())){
				Toast.makeText(getApplicationContext(), "请选择到达港口", Toast.LENGTH_LONG).show();
				return;
			}
			
			
			AsyncHttpResponseHandler ahr = new AsyncHttpResponseHandler(){
				@Override
				public void onStart() {
					super.onStart();
					mDialog = dialogUtil.loading("通知", "正在保存数据...");
				}
				@Override
				public void onSuccess(String arg0) {
					super.onSuccess(arg0);
					mDialog.dismiss();
					ConvertData cd = new ConvertData(arg0);
					if(cd.getReturnCode().equalsIgnoreCase("success")){
						cargoId = ((Double)cd.getContent()).longValue();
						Intent intent = new Intent();

						if(source == SourceCode.fromAddOrder.ordinal() || source == SourceCode.fromChat.ordinal()){
							//跳转到合同编辑
							intent.putExtra("btnEdit", true);
							intent.putExtra("orderId", cargoId.toString());
							intent.setClass(AddCargoActivity.this,OrderPreviewActivity.class);
						}else{
							//跳转到货物列表?货物详情页？
							intent.setClass(AddCargoActivity.this,CargoListActivity.class);
						}
						startActivity(intent);
						finish();
					}else{
						Toast.makeText(getApplicationContext(), cd.getMessage(), Toast.LENGTH_SHORT).show();
					}
					
				}
				@Override
				public void onFailure(Throwable arg0) {
					super.onFailure(arg0);
					mDialog.dismiss();
					Toast.makeText(getApplicationContext(), "获取失败，请稍后再试"+arg0.getMessage()+arg0.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
	
				}
			};

			//读取货物数据并保存
			mDataLoader.getApiResult(ahr, saveUrl,map,"post");

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
			Intent intent = new Intent(AddCargoActivity.this,CropImageActivity.class);
			intent.putExtra("picPath", selectedImage);
			startActivityForResult(intent,AddCargoActivity.REQUEST_CODE_CROPIMG);
			cursor.close();
		}
		// 裁剪后图片
		if (resultCode == RESULT_OK && requestCode == AddCargoActivity.REQUEST_CODE_CROPIMG) {
			String cropedImgPath = data.getStringExtra("cropedImgPath");
			if (cropedImgPath != null) {
				picturePath = cropedImgPath;
				cargoLogo.setImageBitmap(BitmapFactory.decodeFile(cropedImgPath));
			} else {
				Toast.makeText(AddCargoActivity.this, "裁剪图片失败", Toast.LENGTH_SHORT).show();
			}

		}

	}
}
