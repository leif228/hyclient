package com.eyunda.third.activities.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.cargo.CargoData;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.domain.enumeric.CargoTypeCode;
import com.eyunda.third.domain.enumeric.WrapStyleCode;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.DataConvert;
import com.eyunda.tools.log.Log;
import com.hy.client.R;
import com.ta.util.http.AsyncHttpResponseHandler;

/**
 * 新增合同-新增一条干散货
 * 
 * @author guoqiang
 *
 */
public class AddOrderOneGSHActivity extends AddOrderActivity implements
OnClickListener {
	Data_loader dataLoader;

	private Button buttonSave;

	private EditText goodsName;
	private EditText goodsWeight;
	private EditText goodsPrice;
	private EditText goodsFee;
	private EditText goodsRemark;
	private Spinner goodsCate;
	private ArrayAdapter<SpinnerItem> _Adapter;
	private CargoTypeCode cargoType;

	private TextView goodsCateFlag;
	private TextView goodsNameFlag;
	private TextView goodsWeightFlag;
	private TextView goodsPriceFlag;

	private int flag = 1;// 区分当前分类是集装箱还是干散货
	private boolean first = true;//区分是不是第一次展示，对于编辑货物，第一次展示无需计算
	private int request = 1;//区分是从我的货物列表请求（1）还是从合同货物编辑请求（2）
	private Spinner wrapStyle;
	private EditText wrapCount;
	private EditText unitWeight;
	private EditText ctlLength;
	private EditText ctlHeight;
	private EditText ctlWidth;
	private EditText ctlVolume;
	private EditText totaltWeight;
	protected String count;
	protected String unit;
	protected String ctLong;
	protected String ctHeight;
	protected String ctWdith;

	protected String volume_st;

	protected String unitPrice;

	private String cargoId;
	boolean isSelected =false;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_order_one_addgsh);
		dataLoader = new Data_loader();
		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		orderId = (Long) bundle.getLong("orderId");
		cargoId = (String) bundle.getString("cargoId");
		request = (int) bundle.getInt("request");
		initViews();
		setLeft(leftListener());// 重写顶部返回
		loadDate();
	}

	private void initViews() {

		goodsCate = (Spinner) findViewById(R.id.goodsCate);
		wrapStyle = (Spinner) findViewById(R.id.goodsPack);

		// 建立数据源
		final List<SpinnerItem> mItems = getattr();
		// 包装
		ArrayList<SpinnerItem> mTypes = new ArrayList<SpinnerItem>();
		for (WrapStyleCode e : WrapStyleCode.values()) {
			SpinnerItem sp = new SpinnerItem(e + "", e.getDescription());
			mTypes.add(sp);
		}
		ArrayAdapter<SpinnerItem> pack_adapter = new ArrayAdapter<SpinnerItem>(
				this, R.layout.spinner_item, R.id.contentTextView, mTypes);
		wrapStyle.setAdapter(pack_adapter);

		// 建立Adapter并且绑定数据源
		_Adapter = new ArrayAdapter<SpinnerItem>(this, R.layout.spinner_item,
				R.id.contentTextView, mItems);
		// 绑定 Adapter到控件
		goodsCate.setAdapter(_Adapter);

		goodsName = (EditText) findViewById(R.id.goodsName);
		goodsWeight = (EditText) findViewById(R.id.goodsWeight);
		goodsPrice = (EditText) findViewById(R.id.goodsPrice);
		goodsFee = (EditText) findViewById(R.id.goodsFee);
		goodsRemark = (EditText) findViewById(R.id.goodsRemark);
		totaltWeight = (EditText) findViewById(R.id.totaltWeight);

		buttonSave = (Button) findViewById(R.id.buttonSave);
		buttonSave.setOnClickListener(this);
		goodsCate.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					isSelected =true;
				return false;
			}
		});
		goodsCate.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String itval = mItems.get(position).toString();
				if (!"".equals(itval)) {
					CargoBigTypeCode ctc = CargoTypeCode.valueOf(itval).getCargoBigType();
					int index = itval.indexOf(".");
					String ss = itval;
					if (index != -1) {
						ss = itval.substring(index + 1, itval.length());
					}
					if(isSelected){
						if (ctc.equals(CargoBigTypeCode.container)) {// 集装箱
							flag = 1;
							goodsCateFlag.setText("货类");
							goodsName.setText(ss);
							goodsNameFlag.setText("规格");
							goodsWeightFlag.setText("箱量(箱)");
							goodsWeight.setText(wrapCount.getText().toString());

							goodsPriceFlag.setText("单价(元/箱)");

						} else {
							flag = 2;
							goodsCateFlag.setText("货类");
							goodsWeightFlag.setText("货量(吨)");
							goodsWeight.setText(totaltWeight.getText().toString());
							goodsPriceFlag.setText("单价(元/吨)");
							goodsName.setText(ss);
							goodsNameFlag.setText("货名");
						}
					}
					calcAll();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		wrapCount = (EditText) findViewById(R.id.wrapCount);
		unitWeight = (EditText) findViewById(R.id.unitWeight);



		totaltWeight = (EditText) findViewById(R.id.totaltWeight);

		ctlLength = (EditText) findViewById(R.id.length);
		ctlHeight = (EditText) findViewById(R.id.height);
		ctlWidth = (EditText) findViewById(R.id.width);
		ctlVolume = (EditText) findViewById(R.id.volume);

		goodsCateFlag = (TextView) findViewById(R.id.goodsCateFlag);
		goodsNameFlag = (TextView) findViewById(R.id.goodsNameFlag);
		goodsWeightFlag = (TextView) findViewById(R.id.goodsWeightFlag);
		goodsPriceFlag = (TextView) findViewById(R.id.goodsPriceFlag);
		ctlVolume.setEnabled(false);
		totaltWeight.setEnabled(false);
		goodsFee.setEnabled(false);
		autoCal();
	}

	private void autoCal() {
		//数量变化
		wrapCount.addTextChangedListener(new TextWatcher() {
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
				calcAll();

			}
		});
		unitWeight.addTextChangedListener(new TextWatcher() {
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
				calcAll();
			}
		});
		ctlLength.addTextChangedListener(new TextWatcher() {
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
				calcAll();

			}
		});
		ctlHeight.addTextChangedListener(new TextWatcher() {
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
				calcAll();
			}
		});
		ctlWidth.addTextChangedListener(new TextWatcher() {
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
				calcAll();
			}
		});

		goodsPrice.addTextChangedListener(new TextWatcher() {

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
				calcAll();
			}
		});


	}

	private List<SpinnerItem> getattr() {

		ArrayList<String> cargoList = new ArrayList<String>();
		ArrayList<SpinnerItem> cargo = new ArrayList<SpinnerItem>();
		ArrayList<CargoTypeCode> cargoCode = new ArrayList<CargoTypeCode>();

		for (CargoTypeCode e : CargoTypeCode.values()) {
			String cur = e.getDescription();// 所有的项目
			CargoBigTypeCode big = e.getCargoBigType();
			List<CargoTypeCode> CargoName = CargoTypeCode
					.getCargoTypeCodes(big);
			cargoList.add(cur);
			cargoCode.add(e);
		}
		for (int i = 0; i < cargoList.size(); i++) {
			SpinnerItem item = new SpinnerItem(cargoCode.get(i),cargoList.get(i));
			cargo.add(item);
		}

		return cargo;
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(request == 2){
			setTitle("编辑合同-修改货物");
		}else{
			setTitle("编辑合同-新增货物");
		}
		btnAddGSH.setOnClickListener(null);
		btnAddGSH.setBackgroundColor(0xFF6db7ff);
		setRight("关闭", leftListener());
	}

	@Override
	protected synchronized void loadDate() {

		// 加载数据，赋值到视图
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "正在读取数据...");
			}

			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equals("Success")) {
					// 赋值后转换到视图
					Map<String, Object> content = (Map<String, Object>) cd.getContent();
					CargoData cargoData = null;
					if(request == 2){
//						OrderCargoData ocd = new OrderCargoData((Map<String, Object>)content.get("orderCargoData"));
//						cargoData = new CargoData();
//						cargoData.setWrapCount(ocd.getWrapCount());
//						cargoData.setCargoName(ocd.getCargoName());
//						cargoData.setCargoType(ocd.getCargoType());
//						cargoData.setWrapStyle(ocd.getWrapStyle());
//						cargoData.setUnitWeight(ocd.getUnitWeight());
//						cargoData.setFullWeight(ocd.getFullWeight());
//						cargoData.setCtlLength(ocd.getCtlLength());
//						cargoData.setCtlWidth(ocd.getCtlWidth());
//						cargoData.setCtlHeight(ocd.getCtlHeight());
//						cargoData.setCtlVolume(ocd.getCtlVolume());
//						cargoData.setTonTeu(ocd.getTonTeu());
//						cargoData.setPrice(ocd.getPrice());
//						cargoData.setTransFee(ocd.getTransFee());
//						cargoData.setRemark(ocd.getRemark());


					}else{
						cargoData = new CargoData((Map<String, Object>)content.get("cargoData"));
					}
					initData(cargoData);
				} else {
					Toast.makeText(AddOrderOneGSHActivity.this,
							cd.getMessage(), Toast.LENGTH_SHORT).show();

				}
			}

			@Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);
				dialog.dismiss();
				Toast.makeText(AddOrderOneGSHActivity.this, "请检查网络",
						Toast.LENGTH_SHORT).show();
			}

		};
		HashMap<String, Object> params = new HashMap<String, Object>();
		String url = "";
		if(request == 3){//新增货物
			CargoData cargoData = new CargoData();
//			cargoData.setWrapCount(0);
			cargoData.setCargoName(CargoTypeCode.container20e.getDescription());
			cargoData.setCargoType(CargoTypeCode.container20e);
//			cargoData.setWrapStyle(WrapStyleCode.box);
//			cargoData.setUnitWeight(0.0);
//			cargoData.setFullWeight(0.0);
//			cargoData.setCtlLength(0.0);
//			cargoData.setCtlWidth(0.0);
//			cargoData.setCtlHeight(0.0);
//			cargoData.setCtlVolume(0.0);
//			cargoData.setTonTeu(0.0);
			cargoData.setPrice(0.0);
			cargoData.setTransFee(0.0);
			cargoData.setRemark("");
			initData(cargoData);
		}else{
			if(request == 2){//请求合同的货物接口
				url = "/mobile/order/myOrder/getOrderCargoItem";
			}else{
				url = "/mobile/cargo/myCargo/getMyCargo";
			}
			params.put("id", cargoId);
			dataLoader.getApiResult(handler, url,params, "get");
		}
	}

	@Override
	protected void setRight(int resource, OnClickListener listener) {
		// 覆盖父类的setRight
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.buttonSave:
			cargoType = ((SpinnerItem) goodsCate.getSelectedItem())
			.getCargoType();
			final Map<String, Object> map = new HashMap<String, Object>();
			Map<String, Object> apiParams = new HashMap<String, Object>();
			String gPrice = goodsPrice.getText().toString();
			String gFee = goodsFee.getText().toString();
			String gWeight = goodsWeight.getText().toString();
			boolean tmpFlag = true;
			if (TextUtils.isEmpty(wrapCount.getText())) {
				Toast.makeText(AddOrderOneGSHActivity.this, "请输入数量",
						Toast.LENGTH_SHORT).show();
				tmpFlag = false;
			}
			if (TextUtils.isEmpty(unitWeight.getText())) {
				Toast.makeText(AddOrderOneGSHActivity.this, "请输入单重",
						Toast.LENGTH_SHORT).show();
				tmpFlag = false;
			}
			if (TextUtils.isEmpty(totaltWeight.getText())){
				Toast.makeText(AddOrderOneGSHActivity.this, "请输入总重",
						Toast.LENGTH_SHORT).show();
				tmpFlag = false;
			}
			if (TextUtils.isEmpty(ctlLength.getText())) {
				Toast.makeText(AddOrderOneGSHActivity.this, "请输入长",
						Toast.LENGTH_SHORT).show();
				tmpFlag = false;
			}
			if (TextUtils.isEmpty(ctlWidth.getText())) {
				Toast.makeText(AddOrderOneGSHActivity.this, "请输入宽",
						Toast.LENGTH_SHORT).show();
				tmpFlag = false;
			}
			if (TextUtils.isEmpty(ctlHeight.getText())) {
				Toast.makeText(AddOrderOneGSHActivity.this, "请输入高",
						Toast.LENGTH_SHORT).show();
				tmpFlag = false;
			}
			if (TextUtils.isEmpty(ctlVolume.getText())) {
				Toast.makeText(AddOrderOneGSHActivity.this, "请输入体积",
						Toast.LENGTH_SHORT).show();
				tmpFlag = false;
			}
			if ("".equals(gWeight)) {
				Toast.makeText(AddOrderOneGSHActivity.this, "请输入货量",
						Toast.LENGTH_SHORT).show();
				tmpFlag = false;
			}
			if ("".equals(gPrice)) {
				Toast.makeText(AddOrderOneGSHActivity.this, "请输入运价",
						Toast.LENGTH_SHORT).show();
				tmpFlag = false;
			}
			if ("".equals(gFee)) {
				Toast.makeText(AddOrderOneGSHActivity.this, "请输入运费",
						Toast.LENGTH_SHORT).show();
				tmpFlag = false;
			}

			if (tmpFlag) {

				apiParams.put("wrapStyle",
						((SpinnerItem) wrapStyle.getSelectedItem()).getId());
				apiParams.put("wrapCount", wrapCount.getText().toString());
				apiParams.put("unitWeight", unitWeight.getText().toString());
				apiParams.put("fullWeight", totaltWeight.getText().toString());
				apiParams.put("ctlLength", ctlLength.getText().toString());
				apiParams.put("ctlWidth", ctlWidth.getText().toString());
				apiParams.put("ctlHeight", ctlHeight.getText().toString());
				apiParams.put("ctlVolume", ctlVolume.getText().toString());
				//apiParams.put("orderId", Long.parseLong(orderId));

				apiParams.put("cargoType", cargoType);
				apiParams.put("cargoName", "" + goodsName.getText());
				apiParams.put("price",
						Double.parseDouble("" + goodsPrice.getText()));
				apiParams.put("transFee",
						Double.parseDouble("" + goodsFee.getText()));
				apiParams.put("tonTeu",
						Double.parseDouble("" + goodsWeight.getText()));
				apiParams.put("remark", "" + goodsRemark.getText());
				String req = "saveOrderCargo";
				apiParams.put("orderId", orderId);
				if(request == 1){
					apiParams.put("id", "0");//复制货物
				}else if(request == 2){
					apiParams.put("id", Long.parseLong(cargoId));//编辑货物

				}
				Log.v("ccc", apiParams.toString());
				dataLoader.getApiResult(new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						dialog = dialogUtil.loading("通知", "数据保存中，请稍候...");
					}

					@Override
					public void onSuccess(String arg0) {
						// 保存数据
						dialog.dismiss();
						ConvertData cd = new ConvertData(arg0);
						// Log.setLog2FileEnabled(true);
						// Log.setFilePathGenerator(new
						// FilePathGenerator.DefaultFilePathGenerator("eyunda",".log"));
						// Log.d(res);
						if (cd.getReturnCode().equalsIgnoreCase("success")) {
							Toast.makeText(getApplicationContext(), "保存成功",
									Toast.LENGTH_LONG).show();
							Intent intnet = new Intent(
									AddOrderOneGSHActivity.this,
									AddOrderGSHActivity.class);
							Bundle bundle = new Bundle();
							bundle.putLong("orderId", orderId);//合同ID
							intnet.putExtras(bundle);
							startActivity(intnet);
							finish();
						} else {
							Toast.makeText(AddOrderOneGSHActivity.this,
									"保存成功", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Throwable arg0, String content) {
						super.onFailure(arg0, content);
						Toast.makeText(AddOrderOneGSHActivity.this, content,
								Toast.LENGTH_SHORT).show();
					}
				}, "/mobile/order/myOrder/"+req, apiParams, "get");
			}
			break;
		case R.id.top_back:

			break;

		}

	}


	private void initData(CargoData cargoData) {

		if (cargoData != null) {

			goodsName.setText(cargoData.getCargoName());
			goodsWeight.setText(cargoData.getTonTeu().toString());

			goodsPrice.setText(cargoData.getPrice().toString());
			goodsFee.setText(cargoData.getTransFee().toString());
			goodsRemark.setText(cargoData.getRemark());
			wrapCount.setText("");
			unitWeight.setText("");
			totaltWeight.setText(cargoData.getTonTeu().toString());
			ctlLength.setText("");
			ctlWidth.setText("");
			ctlHeight.setText("");
			ctlVolume.setText("");
			CargoBigTypeCode ctc = cargoData.getCargoType().getCargoBigType();
			if (ctc.equals(CargoBigTypeCode.container)) {// 集装箱
				flag = 1;
				goodsNameFlag.setText("规格");
				goodsWeightFlag.setText("货量(箱)");
				goodsPriceFlag.setText("单价(元/箱)");
			}else{
				flag = 2;
				goodsWeightFlag.setText("货量(吨)");
				goodsPriceFlag.setText("单价(元/吨)");
				goodsNameFlag.setText("货名");
			}

			//更新
			ArrayList<String> lists = new ArrayList<String>();
			for (CargoTypeCode e : CargoTypeCode.values()) {
				lists.add(e.getDescription());
			}
			// 下拉默认选项
			String cargoTypeiDes = cargoData.getCargoType().getDescription();
			for (int j = 0; j < lists.size(); j++) {
				if (cargoTypeiDes != null) {
					if (cargoTypeiDes.equals(lists.get(j))) {
						goodsCate.setSelection(j);
					}
				}
			}
			// 包装
			String packStyle = cargoData.getCargoTypeName();
			ArrayList<String> packList = new ArrayList<String>();
			for (WrapStyleCode e : WrapStyleCode.values()) {
				SpinnerItem sp = new SpinnerItem(e + "", e.getDescription());
				packList.add(e.getDescription());
			}

			// 下拉默认选项
			for (int j = 0; j < packList.size(); j++) {
				if (packStyle != null) {
					if (packStyle.equals(packList.get(j))) {
						wrapStyle.setSelection(j);
					}
				}
			}
		}


	}
	private boolean caclAble(){
		//		if(first == true && request == 2){
		//			//从合同货物列表跳转，并且是第一次请求，此时无需计算
		//			return false;
		//		}
		return true;
	}

	private void calcAll(){

		if(caclAble()){
			//计算总重
			unit = unitWeight.getText().toString();
			if (!TextUtils.isEmpty(wrapCount.getText().toString()) && !TextUtils.isEmpty(unit)) {
				Double total = Double.parseDouble(wrapCount.getText().toString())* Double.parseDouble(unit);
				totaltWeight.setText(String.valueOf(DataConvert.getDataWithTwo(total)));
			}

			//计算体积
			ctLong = ctlLength.getText().toString();
			ctWdith = ctlWidth.getText().toString();
			ctHeight = ctlHeight.getText().toString();
			if (!TextUtils.isEmpty(ctLong) && !TextUtils.isEmpty(ctHeight)
					&& !TextUtils.isEmpty(ctWdith)) {
				Double total = Double.parseDouble(ctLong)
						* Double.parseDouble(ctWdith)
						* Double.parseDouble(ctHeight);
				ctlVolume.setText(String.valueOf(DataConvert.getDataWithTwo(total)));
			}

			setVal();

			//计算运费
			if(!TextUtils.isEmpty(goodsPrice.getText().toString()) && !TextUtils.isEmpty(goodsWeight.getText().toString())){
				Double fee = Double.parseDouble(goodsPrice.getText().toString()) * Double.parseDouble(goodsWeight.getText().toString());
				goodsFee.setText(String.valueOf(DataConvert.getDataWithTwo(fee)));
			}

		}
	}

	private void setVal(){
		if(flag == 1){//如果当前选择是集装箱，同步数据到
			count = wrapCount.getText().toString();
			goodsWeight.setText(count);
		}
		//如果当前选择是干散货
		if(flag == 2){
			count = totaltWeight.getText().toString();
			goodsWeight.setText(count);
		}
	}
}
