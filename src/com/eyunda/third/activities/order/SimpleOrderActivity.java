package com.eyunda.third.activities.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.ship.ShipPreviewActivity;
import com.eyunda.third.activities.ship.widget.DatePickerFragment;
import com.eyunda.third.adapters.CommonAdapter;
import com.eyunda.third.adapters.ViewHolder;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.domain.enumeric.CargoTypeCode;
import com.eyunda.third.domain.enumeric.EnumConst.PortCityCode;
import com.eyunda.third.domain.enumeric.MoneyStyleCode;
import com.eyunda.third.domain.order.OrderContainerData;
import com.eyunda.third.domain.order.OrderData;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.AreaSelect;
import com.eyunda.tools.CustomAlertDialog;
import com.eyunda.tools.LocalFileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 简化版 合同
 * 
 * @author Li guang
 *
 */
public class SimpleOrderActivity extends AddOrderActivity
		implements OnClickListener, OnItemSelectedListener, OnTouchListener {

	private static final int SEARCH_CONSIGNER = 0;
	private static final int REQUEST_CODE_SHIPIMG = 1;
	private ImageView userImg;
	private ImageView carrierImg;
	private ImageView shipImg;
	private ImageLoader mImageLoader;
	private EditText cargoName, ctvolume, unitPrice, totalPrice, receiver, phone, cargo_tiaokuan, cargo_address;
	private TextView userName, shipper, shipName, start, end, zhuanghuoDate, daohuoDate, tv_weight, tv_price, tv_cargo,
			consignorCp, carrierCp, shipMaster;
	private Spinner cashType;
	private RelativeLayout test_pop_layout;
	private Button addStart, addEnd, delShip;
	private Data_loader data;

	private Spinner cargType;

	protected String unit;
	protected String volumes;
	protected String ctWrapCount;
	protected String ctUnitWeight;
	private String cargos;
	protected String wrapName;

	protected String moneyStyle;

	boolean isSelected = false;

	private LinearLayout containerBoxA, containerBoxB, tv_remark_container;

	@Bind(R.id.price1)
	EditText price1;
	@Bind(R.id.price2)
	EditText price2;
	@Bind(R.id.price3)
	EditText price3;
	@Bind(R.id.price4)
	EditText price4;
	@Bind(R.id.price5)
	EditText price5;
	@Bind(R.id.price6)
	EditText price6;

	@Bind(R.id.tonTeu6)
	EditText tonTeu6;
	@Bind(R.id.tonTeu5)
	EditText tonTeu5;
	@Bind(R.id.tonTeu4)
	EditText tonTeu4;
	@Bind(R.id.tonTeu3)
	EditText tonTeu3;
	@Bind(R.id.tonTeu2)
	EditText tonTeu2;
	@Bind(R.id.tonTeu1)
	EditText tonTeu1;
	@Bind(R.id.cargo_delay_fee)
	EditText cargo_delay_fee;// 滞期费
	@Bind(R.id.cargo_up_during)
	EditText cargo_up_during;// 滞装货费
	@Bind(R.id.cargo_down_during)
	EditText cargo_down_during;// 滞允许卸货时间费

	@Bind(R.id.sureDate)
	EditText sureDate;

	@Bind(R.id.btnSave)
	Button btnSave;

	OrderData orderData;
	UserData currentUser;
	Long shipMasterId = 0L;
	List<SpinnerItem> mSpinnerCargos, mPayItems;
	ArrayAdapter<SpinnerItem> cargoAdapter, _PayAdapter, payTypeAdapter, accountAdapter;

	private Long ownerId;// 从聊天过来，建立合同默认选择货物的ownerId
	private Long shipId;// 合同船只

	List<UserData> agentUsers = new ArrayList<UserData>();// 用户的代理人列表
	List<ShipData> agentShips = new ArrayList<ShipData>();// 用户的代理人列表

	CustomAlertDialog.Builder customBuilder = null;
	private CommonAdapter<SpinnerItem> ownerAdapter, shipAdapter;
	private ArrayList<SpinnerItem> ownerSpinnerList = new ArrayList<SpinnerItem>();
	private ArrayList<SpinnerItem> shipSpinnerList = new ArrayList<SpinnerItem>();
	private Dialog customDialog;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_order);
		ButterKnife.bind(this);
		Intent it = getIntent();

		orderId = it.getLongExtra("orderId", 0L);
		ownerId = it.getLongExtra("ownerId", 0L);

		currentUser = GlobalApplication.getInstance().getUserData();

		mImageLoader = ImageLoader.getInstance();
		data = new Data_loader();
		customBuilder = new CustomAlertDialog.Builder(SimpleOrderActivity.this);
		mSpinnerCargos = new ArrayList<SpinnerItem>();
		mPayItems = new ArrayList<SpinnerItem>();

		containerBoxA = (LinearLayout) findViewById(R.id.containerBoxA);
		containerBoxB = (LinearLayout) findViewById(R.id.containerBoxB);

		test_pop_layout = (RelativeLayout) findViewById(R.id.simplelayout);

		userImg = (ImageView) findViewById(R.id.userLogo);
		carrierImg = (ImageView) findViewById(R.id.carrierLogo);
		shipImg = (ImageView) findViewById(R.id.shipLogo);
		userName = (TextView) findViewById(R.id.user);
		consignorCp = (TextView) findViewById(R.id.company1);
		carrierCp = (TextView) findViewById(R.id.company2);
		shipName = (TextView) findViewById(R.id.shipName);
		shipper = (TextView) findViewById(R.id.shipper);
		shipMaster = (TextView) findViewById(R.id.rlShipper);
		start = (TextView) findViewById(R.id.start_port);
		end = (TextView) findViewById(R.id.end_port);
		zhuanghuoDate = (TextView) findViewById(R.id.zhuanghuoDate);
		daohuoDate = (TextView) findViewById(R.id.daohuoDate);

		tv_weight = (TextView) findViewById(R.id.cargoWeightTag);
		tv_price = (TextView) findViewById(R.id.cargoPriceTag);
		tv_cargo = (TextView) findViewById(R.id.cargoNameTag);

		cargo_address = (EditText) findViewById(R.id.cargo_address);
		cargo_tiaokuan = (EditText) findViewById(R.id.cargo_tiaokuan);
		cargoName = (EditText) findViewById(R.id.cargoName);

		ctvolume = (EditText) findViewById(R.id.ctvol);
		unitPrice = (EditText) findViewById(R.id.unitPrice);
		totalPrice = (EditText) findViewById(R.id.totalPrice);
		receiver = (EditText) findViewById(R.id.cargo_receiver);
		phone = (EditText) findViewById(R.id.cargo_phone);

		cargType = (Spinner) findViewById(R.id.select_cargo);// 货类
		cashType = (Spinner) findViewById(R.id.cashType); // 付款方式 到付，预付

		addStart = (Button) findViewById(R.id.addStart);
		addEnd = (Button) findViewById(R.id.addEnd);
		delShip = (Button) findViewById(R.id.del);

		start.setOnClickListener(this);
		addStart.setOnClickListener(this);
		addEnd.setOnClickListener(this);

		end.setOnClickListener(this);
		userImg.setOnClickListener(this);

		zhuanghuoDate.setOnClickListener(this);
		daohuoDate.setOnClickListener(this);
		cargType.setOnItemSelectedListener(this);
		cargType.setOnTouchListener(this);
		btnSave.setClickable(false);

		shipImg.setOnClickListener(this);
		delShip.setOnClickListener(this);

		initSpinner();
		getDatas();

		autoCal();

	}

	private void initSpinner() {
		// 货类
		cargoAdapter = new ArrayAdapter<SpinnerItem>(this, R.layout.spinner_item, R.id.contentTextView, mSpinnerCargos);
		cargType.setAdapter(cargoAdapter);
		// 付款方式
		_PayAdapter = new ArrayAdapter<SpinnerItem>(this, android.R.layout.simple_spinner_item, mPayItems);
		cashType.setAdapter(_PayAdapter);

		ownerAdapter = new CommonAdapter<SpinnerItem>(getApplicationContext(), ownerSpinnerList,
				R.layout.eyd_popup_item) {
			@Override
			public void convert(ViewHolder helper, SpinnerItem item) {
				helper.setText(R.id.spinnerValue, item.getValue());
				helper.setText(R.id.spinnerId, item.getId());
				helper.setText(R.id.spinnerCid, item.getData());
			}
		};
		shipAdapter = new CommonAdapter<SpinnerItem>(getApplicationContext(), shipSpinnerList,
				R.layout.eyd_popup_item) {
			@Override
			public void convert(ViewHolder helper, SpinnerItem item) {
				helper.setText(R.id.spinnerValue, item.getValue());
				helper.setText(R.id.spinnerId, item.getId());
				helper.setText(R.id.spinnerCid, item.getData());
			}
		};
	}

	private void setSpinner(OrderData od) {
		// 货类
		boolean flag = true;
		int i = 0;
		for (CargoTypeCode d : CargoTypeCode.values()) {
			String jzx = CargoBigTypeCode.container.getDescription();// 集装箱
			String name = d.getDescription();
			SpinnerItem sp = null;
			if (od.getCargoType().equals(d)) {
				cargType.setSelection(i);
			}
			if (!name.contains(jzx)) {
				sp = new SpinnerItem(d.name(), d.getDescription());
				mSpinnerCargos.add(sp);
				i++;
			} else {
				if (flag) {
					sp = new SpinnerItem(d.name(), d.getCargoBigType().getDescription());
					mSpinnerCargos.add(sp);
					i++;
					flag = false;
				}
			}

		}
		if (od.getCargoType() == null) {
			cargType.setSelection(0);
		}
		cargoAdapter.notifyDataSetChanged();

		// 付款方式
		int j = 0;
		for (MoneyStyleCode p : MoneyStyleCode.values()) {
			SpinnerItem si = new SpinnerItem(p.name(), p.getDescription());
			mPayItems.add(si);
			if (od.getMoneyStyle().equals(p)) {
				cashType.setSelection(j);
			}
			j++;
		}
		if (od.getMoneyStyle() == null) {
			cashType.setSelection(0);
		}
		_PayAdapter.notifyDataSetChanged();

	}

	private void setDeafault(OrderData orderData) {

		if (orderData.getCargoType() != null) {
			if (orderData.getCargoType().getDescription().contains("集装箱")) {
				containerBoxA.setVisibility(View.GONE);
				containerBoxB.setVisibility(View.VISIBLE);
				// 集装箱赋值
				List<OrderContainerData> ccds = orderData.getContainers();
				if (ccds.size() == 6) {
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

				} else {
					Toast.makeText(getApplicationContext(), "集装箱数据错误，请修正", Toast.LENGTH_LONG).show();
				}

			} else {
				containerBoxB.setVisibility(View.GONE);
				containerBoxA.setVisibility(View.VISIBLE);

			}

		} else {
			cargType.setSelection(0);
			containerBoxB.setVisibility(View.GONE);
			containerBoxA.setVisibility(View.VISIBLE);

		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!orderId.equals(0L)) {
			setTitle("修改合同");
			setRight("", null);
		} else {
			setTitle("新增合同");
			setRight("", null);
		}

	}

	private void autoCal() {

		ctvolume.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				unit = unitPrice.getText().toString();

			}

			@Override
			public void afterTextChanged(Editable s) {
				volumes = ctvolume.getText().toString();
				if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(unit)) {
					Double total = Double.parseDouble(s.toString()) * Double.parseDouble(unit);
					totalPrice.setText(String.valueOf(total));

				}
			}
		});
		unitPrice.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				unit = unitPrice.getText().toString();
				if (!TextUtils.isEmpty(volumes) && !TextUtils.isEmpty(unit)) {
					Double total = Double.parseDouble(volumes) * Double.parseDouble(unit);
					totalPrice.setText(String.valueOf(total));
				}
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

	/**
	 * 获取初始数据
	 */
	private void getDatas() {
		final Map<String, Object> params = new HashMap<String, Object>();

		params.put("orderId", orderId);
		if(ownerId > 0){
			params.put("ownerId", ownerId);
		}
		final String tmpUri = "/mobile/order/myOrder/edit";
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "数据获取中，请稍后...");
			}

			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				dialog.dismiss();

				ConvertData cd = new ConvertData(arg0, tmpUri, params);
				if (cd.getReturnCode().equals("Success")) {
					Map<String, Object> map = (Map<String, Object>) cd.getContent();
					// List<Map<String, Object>> cargoList = (List<Map<String,
					// Object>>) map.get("cargos");
					Map<String, Object> orderMap = (HashMap<String, Object>) map.get("orderData");

					orderData = new OrderData(orderMap);
					orderId = orderData.getId();
					cargoName.setText(orderData.getCargoName());

					List<HashMap<String, Object>> agentUsersList = (ArrayList<HashMap<String, Object>>) map
							.get("agentUsers");
					for (HashMap<String, Object> tmp : agentUsersList) {
						UserData userData = new UserData(tmp);
						agentUsers.add(userData);
						SpinnerItem si = new SpinnerItem(userData.getId().toString(), userData.getTrueName(),
								userData.getId().toString());
						ownerSpinnerList.add(si);
					}

					List<HashMap<String, Object>> agentShipsList = (ArrayList<HashMap<String, Object>>) map
							.get("agentShips");
					if(agentShipsList != null){
						for (HashMap<String, Object> tmp : agentShipsList) {
							ShipData shipData = new ShipData(tmp);
							agentShips.add(shipData);
							SpinnerItem si = new SpinnerItem(shipData.getId().toString(),
									shipData.getShipName() + "(船东：" + shipData.getMaster().getTrueName() + ")",
									shipData.getShipLogo());
							shipSpinnerList.add(si);
						}
						shipAdapter.notifyDataSetChanged();
					}
					// 付款方式
					moneyStyle = orderData.getMoneyStyle().getDescription();

					shipId = orderData.getShipId();

					// 船舶图片
					mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + orderData.getShipData().getShipLogo(),
							shipImg, MyshipAdapter.displayImageOptions);

					// 承运人头像
					mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + orderData.getMaster().getUserLogo(),
							carrierImg, MyshipAdapter.displayImageOptions);
					// 托运人
					if (ownerId == 0L) {
						userName.setText(orderData.getOwner().getTrueName());
						// 托运人头像
						mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + orderData.getOwner().getUserLogo(),
								userImg, MyshipAdapter.displayImageOptions);
						consignorCp.setText(orderData.getOwner().getUnitName());
					} else if (ownerId > 0) {
						setOwner();
					}

					shipMaster.setText(orderData.getMaster().getTrueName());//   承运人

					if (orderData.getShipData().getTypeData() != null) {
						shipper.setText(orderData.getShipData().getTypeData().getTypeName());
						shipName.setText(orderData.getShipData().getShipName());
						delShip.setVisibility(View.VISIBLE);
					}

					carrierCp.setText(orderData.getMaster().getUnitName());

					receiver.setText(orderData.getReceiver());
					phone.setText(orderData.getReceMobile());
					start.setText(orderData.getStartPort().getFullName());
					start.setTag(orderData.getStartPortNo());
					end.setText(orderData.getEndPort().getFullName());
					end.setTag(orderData.getEndPortNo());
					zhuanghuoDate.setText(orderData.getUpDate());
					daohuoDate.setText(orderData.getDownDate());
					cargo_tiaokuan.setText(orderData.getOrderContent());
					cargo_address.setText(orderData.getReceAddress());
					//  货量
					ctvolume.setText(orderData.getTonTeu().toString());
					// 运价
					unitPrice.setText(orderData.getPrice().toString());
					// 运费
					totalPrice.setText(orderData.getTransFee().toString());
					// 滞期费
					cargo_delay_fee.setText(orderData.getDemurrage().toString());
					// 允许装货时间
					cargo_up_during.setText(orderData.getUpTime().toString());
					// 允许卸货时间
					cargo_down_during.setText(orderData.getDownTime().toString());

					sureDate.setText(orderData.getSuretyDays().toString());

					shipMasterId = orderData.getMasterId();
					setSpinner(orderData);
					setDeafault(orderData);
					if (canChangShip(orderData) && (orderData.getShipData() != null)) {
						if (orderData.getShipId() > 0)
							delShip.setVisibility(View.VISIBLE);
					}
					btnSave.setClickable(true);
					ownerAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(getApplicationContext(), cd.getMessage(), Toast.LENGTH_SHORT).show();
					finish();
				}
			}

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				dialog.dismiss();
			}
		};

		data.getApiResult(handler, tmpUri, params, "get");
	}

	// 从备选的货主列表中选中当前指定的用户
	private void setOwner() {
		// 从agentUser中选出ownerId对应的名称和头像
		for (UserData userData : agentUsers) {
			if (ownerId.equals(userData.getId())) {
				orderData.setOwnerId(ownerId);
				orderData.setOwner(userData);
				mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + userData.getUserLogo(), userImg,
						MyshipAdapter.displayImageOptions);
				userName.setText(userData.getTrueName());
				consignorCp.setText(userData.getUnitName());
				cargo_address.setText(userData.getAddress());
				//
				orderData.setOwner(userData);
				orderData.setOwnerId(userData.getId());
				return;
			}
		}
	}

	TextWatcher myTextWatche = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			String result = calcTotal();
			totalPrice.setText(result);

		}
	};

	private String calcTotal() {
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
		if (!TextUtils.isEmpty(tonTeu1Str) && !TextUtils.isEmpty(price1Str)) {
			res1 = Double.parseDouble(tonTeu1Str) * Double.parseDouble(price1Str);
		}
		if (!TextUtils.isEmpty(tonTeu2Str) && !TextUtils.isEmpty(price2Str)) {
			res2 = Double.parseDouble(tonTeu2Str) * Double.parseDouble(price2Str);
		}
		if (!TextUtils.isEmpty(tonTeu3Str) && !TextUtils.isEmpty(price3Str)) {
			res3 = Double.parseDouble(tonTeu3Str) * Double.parseDouble(price3Str);
		}
		if (!TextUtils.isEmpty(tonTeu4Str) && !TextUtils.isEmpty(price4Str)) {
			res4 = Double.parseDouble(tonTeu4Str) * Double.parseDouble(price4Str);
		}
		if (!TextUtils.isEmpty(tonTeu5Str) && !TextUtils.isEmpty(price5Str)) {
			res5 = Double.parseDouble(tonTeu5Str) * Double.parseDouble(price5Str);
		}
		if (!TextUtils.isEmpty(tonTeu6Str) && !TextUtils.isEmpty(price6Str)) {
			res6 = Double.parseDouble(tonTeu6Str) * Double.parseDouble(price6Str);
		}
		Double total = res1 + res2 + res3 + res4 + res5 + res6;
		return String.valueOf(round(total, 3));

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

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.start_port:
		case R.id.end_port:
			AreaSelect asStart = new AreaSelect(this, start);
			AreaSelect asEnd = new AreaSelect(this, end);
			AreaSelect pointAt = null;
			if (v.getId() == R.id.start_port) {
				pointAt = asStart;
			} else {
				pointAt = asEnd;
			}
//			PopupWindow popupWindow = pointAt.makePopupWindow();
			int[] xy = new int[2];
			test_pop_layout.getLocationOnScreen(xy);
			int localHeight = pointAt.getHeight();
//			popupWindow.showAtLocation(test_pop_layout, Gravity.CENTER | Gravity.BOTTOM, 0, -localHeight);
			break;
		case R.id.zhuanghuoDate:
			DatePickerFragment datePicker = new DatePickerFragment(zhuanghuoDate);
			datePicker.show(getFragmentManager(), "datePicker");
			break;
		case R.id.daohuoDate:
			DatePickerFragment datePicker2 = new DatePickerFragment(daohuoDate);
			datePicker2.show(getFragmentManager(), "datePicker2");
			break;
		case R.id.userLogo:
			// if(userId.equals(carrierId)){//当前用户在合同中角色为承运人时，不能选择船和托运人。
			// return;
			showOwners();
			// Intent intent = new Intent(this, FindTYRActivity.class);
			// intent.putExtra("id", orderId);
			// startActivityForResult(intent, SEARCH_CONSIGNER);
			break;
		case R.id.shipLogo:
//			if (!canChangShip(orderData)) { // 当前用户在合同中角色是托运人，不能选择。
//				return;
//			} else {
				// Intent i = new Intent(this, FindShipActivity.class);
				// i.putExtra("orderId", orderId);
				// startActivityForResult(i, REQUEST_CODE_SHIPIMG);
				showShips();
//			}
			break;
		case R.id.del: // 点击船舶，可为空.
//			if (!canChangShip(orderData)) { // 当前用户在合同中角色是托运人，不能选择。
//				return;
//			} else {
				new AlertDialog.Builder(SimpleOrderActivity.this).setTitle("删除船舶?")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								changeShip(0L);

							}
						}).setNegativeButton("取消", null).show();
			//}
			break;
		case R.id.addStart:
		case R.id.addEnd:
			LayoutInflater inflater = LayoutInflater.from(SimpleOrderActivity.this);
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
			new AlertDialog.Builder(SimpleOrderActivity.this).setTitle("新增港口").setView(view)
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
										Toast.makeText(SimpleOrderActivity.this, (CharSequence) result.get("message"),
												Toast.LENGTH_SHORT).show();
									} else {
										Toast.makeText(SimpleOrderActivity.this, (CharSequence) result.get("message"),
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
							data.getApiResult(handler, "/mobile/ship/myShip/saveNewPort", params, "get");

						}

					}).show();

			break;
		}
	}

	// 显示备选用户
	private void showOwners() {
		customDialog = null;
		customBuilder.setTitle("选择托运人").setAdapter(ownerAdapter, new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SpinnerItem si = ownerAdapter.getItem(position);
				customDialog.dismiss();
				ownerId = Long.parseLong(si.getId());
				setOwner();
			}
		});
		customDialog = customBuilder.create();
		customDialog.show();

	}

	// 显示备选船舶
	private void showShips() {
		customDialog = null;
		customBuilder.setTitle("选择船舶").setAdapter(shipAdapter, new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SpinnerItem si = shipAdapter.getItem(position);
				customDialog.dismiss();
				shipId = Long.parseLong(si.getId());
				changeShip(shipId);
			}
		});
		customDialog = customBuilder.create();
		customDialog.show();
	}

	// 保存
	public void saveOrder(View view) {
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("通知", "数据获取中，请稍后...");
			}

			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				dialog.dismiss();
				ConvertData cd = new ConvertData(arg0);
				if (cd.getReturnCode().equals("Success")) {
					Toast.makeText(getApplicationContext(), cd.getMessage(), Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(SimpleOrderActivity.this, MyOrderActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("success", true);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), cd.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);

				dialog.dismiss();
			}
		};

		if (TextUtils.isEmpty(zhuanghuoDate.getText().toString())) {
			Toast.makeText(this, "请输入装货日期", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(daohuoDate.getText().toString())) {
			Toast.makeText(this, "请输入到货日期", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(cargo_delay_fee.getText().toString())) {
			Toast.makeText(this, "请输入滞期费率", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(cargo_up_during.getText().toString())) {
			Toast.makeText(this, "请输入装货时间", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(cargo_down_during.getText().toString())) {
			Toast.makeText(this, "请输入卸货时间", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(start.getText().toString())) {
			Toast.makeText(this, "请输入起运港", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(end.getText().toString())) {
			Toast.makeText(this, "请输入到达港", Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(ctvolume.getText().toString())) {
			Toast.makeText(this, "请输入货量", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(unitPrice.getText().toString())) {
			Toast.makeText(this, "请输入运价", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(totalPrice.getText().toString())) {
			Toast.makeText(this, "请输入运费", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(receiver.getText().toString())) {
			Toast.makeText(this, "请输入收货人姓名", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(phone.getText().toString())) {
			Toast.makeText(this, "请输入收货人电话", Toast.LENGTH_SHORT).show();
			return;
		}
		int tmpDate = 0;
		if (TextUtils.isEmpty(sureDate.getText().toString())) {
			Toast.makeText(this, "请输入资金托管日期", Toast.LENGTH_SHORT).show();
			return;
		} else {
			tmpDate = Integer.parseInt(sureDate.getText().toString());
			if (tmpDate > 90) {
				Toast.makeText(this, "资金托管日期不能大于90", Toast.LENGTH_SHORT).show();
				return;
			} else if (tmpDate < 0) {
				Toast.makeText(this, "资金托管日期不能小于0", Toast.LENGTH_SHORT).show();
				return;
			}
		}

		String seletedPayVal = ((SpinnerItem) cashType.getSelectedItem()).getId(); // 付款方式

		String typeCode = ((SpinnerItem) cargType.getSelectedItem()).getId();

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("id", orderData.getId());
		params.put("ownerId", orderData.getOwnerId());// 货主
		params.put("agentId", orderData.getAgentId());// 货代
		params.put("masterId", shipMasterId);// 船东
		params.put("brokerId", orderData.getBrokerId());// 船代
		params.put("handlerId", orderData.getHandlerId());// 操作员
		params.put("shipId", shipId);// 船舶
		params.put("status", "edit");
		params.put("startPortCity", start.getText().toString());
		params.put("endPortCity", end.getText().toString());
		params.put("startPortNo", start.getTag()); // 起始港
		params.put("endPortNo", end.getTag()); // 到达港
		params.put("cargoType", typeCode); // 货类
		params.put("suretyDays", tmpDate); // 担保期
		if (CargoTypeCode.valueOf(typeCode).getDescription().contains("集装箱")) {
			params.put("cargoType", "container20e"); // 货类
			// 集装箱
			params.put("cargoName1", CargoTypeCode.container20e.getShortDesc());
			params.put("cargoName2", CargoTypeCode.container20f.getShortDesc());
			params.put("cargoName3", CargoTypeCode.container40e.getShortDesc());
			params.put("cargoName4", CargoTypeCode.container20f.getShortDesc());
			params.put("cargoName5", CargoTypeCode.container45e.getShortDesc());
			params.put("cargoName6", CargoTypeCode.container45f.getShortDesc());
			params.put("cargoName1", CargoTypeCode.container20e.getShortDesc());
			// 保存六个属性
			params.put("tonTeu1", Long.parseLong(tonTeu1.getText().toString()));
			params.put("tonTeu2", Long.parseLong(tonTeu2.getText().toString()));
			params.put("tonTeu3", Long.parseLong(tonTeu3.getText().toString()));
			params.put("tonTeu4", Long.parseLong(tonTeu4.getText().toString()));
			params.put("tonTeu5", Long.parseLong(tonTeu5.getText().toString()));
			params.put("tonTeu6", Long.parseLong(tonTeu6.getText().toString()));
			params.put("price1", Double.valueOf(price1.getText().toString()));
			params.put("price2", Double.valueOf(price2.getText().toString()));
			params.put("price3", Double.valueOf(price3.getText().toString()));
			params.put("price4", Double.valueOf(price4.getText().toString()));
			params.put("price5", Double.valueOf(price5.getText().toString()));
			params.put("price6", Double.valueOf(price6.getText().toString()));
			// 货名
			params.put("cargoName", "集装箱");
		} else {
			params.put("cargoName", cargoName.getText().toString());// 货名
		}
		params.put("tonTeu", ctvolume.getText().toString());// 货量（箱量）
		params.put("price", unitPrice.getText().toString()); // 单价
		params.put("transFee", totalPrice.getText().toString()); // 总价
		params.put("demurrage", Double.parseDouble(cargo_delay_fee.getText().toString()));
		params.put("upDate", zhuanghuoDate.getText().toString()); // 装货日
		params.put("downDate", daohuoDate.getText().toString()); // 到货日
		params.put("upTime", Integer.parseInt(cargo_up_during.getText().toString()));
		params.put("downTime", Integer.parseInt(cargo_down_during.getText().toString()));
		params.put("receiver", receiver.getText().toString()); // 收货人
		params.put("receMobile", phone.getText().toString()); // 收货电话
		params.put("receAddress", cargo_address.getText().toString());
		params.put("moneyStyle", seletedPayVal);
		params.put("orderContent", cargo_tiaokuan.getText().toString());

		data.getApiResult(handler, "/mobile/order/myOrder/saveOrderAll", params, "get");
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == RESULT_OK && requestCode == SEARCH_CONSIGNER) {// 查找托运人
//			UserData user = (UserData) data.getSerializableExtra("userData");
//			String trueName = user.getTrueName();
//			if (trueName.equals("")) {
//				userName.setText(user.getNickName());
//			} else
//				userName.setText(trueName);
//			consignorCp.setText(user.getUnitName());
//
//			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + user.getUserLogo(), userImg,
//					MyshipAdapter.displayImageOptions);
//
//		}
//		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_SHIPIMG) {// 换船
//			ShipData shipDataPri = (ShipData) data.getSerializableExtra("orderData");
//			changeShip(shipDataPri.getId());
//		}
//	}

	/**
	 * 换船后更新船东账户信息
	 * 
	 * @param myShipId
	 *            船舶Id
	 */
	public void changeShip(final Long myShipId) {

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("shipId", myShipId);
		final String getAccountUrl = "/mobile/order/changeShip";
		AsyncHttpResponseHandler getAccountHandler = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				ConvertData cd = new ConvertData(arg0, getAccountUrl, params);
				if (cd.getReturnCode().equals("Success")) {
					HashMap<String, Object> content = (HashMap<String, Object>) cd.getContent();
					List<Map<String, Object>> accountDataMap = (List<Map<String, Object>>) content.get("bankCardDatas");
					String orderContent = (String) content.get("orderContent");
					UserData upData = new UserData((Map<String, Object>)content.get("upData"));
					ShipData sd = null;
					if (myShipId > 0) {
						sd = new ShipData((Map<String, Object>) content.get("shipData"));
						// 船舶信息更新
						shipper.setText(sd.getTypeData().getTypeName());
						shipName.setText(sd.getShipName());
						mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + sd.getShipLogo(), shipImg,
								MyshipAdapter.displayImageOptions);

						shipId = sd.getId();
						// 船东信息更新
						shipMasterId = sd.getMasterId();
						carrierCp.setText(sd.getMaster().getUnitName());
						shipMaster.setText(sd.getMaster().getTrueName());//   承运人
						mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + sd.getMaster().getUserLogo(),
								carrierImg, MyshipAdapter.displayImageOptions);
						delShip.setVisibility(View.VISIBLE);
						
						orderData.setMaster(sd.getMaster());
						orderData.setMasterId(sd.getMaster().getId());
					} else {
						shipper.setText("");
						shipName.setText("");
						shipImg.setImageResource(R.drawable.img_load_failed);
						shipId = 0L;
						//船东换成当前用户的父代理
						orderData.setMaster(upData);
						orderData.setMasterId(upData.getId());
						shipMasterId = upData.getId();
						carrierCp.setText(upData.getUnitName());
						shipMaster.setText(upData.getTrueName());
						mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + upData.getUserLogo(),
								carrierImg, MyshipAdapter.displayImageOptions);
						delShip.setVisibility(View.GONE);
					}

					// 合同条款更新
					cargo_tiaokuan.setText(orderContent);

				} else {
					Toast.makeText(getApplicationContext(), cd.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);
			}
		};
		data.getApiResult(getAccountHandler, getAccountUrl, params, "get");
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()) {
		case R.id.select_cargo:
			cargos = cargType.getItemAtPosition(position).toString();
			// Toast.makeText(CargoActivity.this,"点击的是:"+
			// typeCode,Toast.LENGTH_SHORT).show();

			if (isSelected) {
				if (cargos.toString().contains(CargoBigTypeCode.container.getDescription())) {
					containerBoxA.setVisibility(View.GONE);
					containerBoxB.setVisibility(View.VISIBLE);
					cargoName.setText(cargos.toString());
					cleanData();
				} else {
					containerBoxB.setVisibility(View.GONE);
					containerBoxA.setVisibility(View.VISIBLE);
					String name = cargos.toString();
					cargoName.setText(name.substring(name.indexOf(".") + 1));
					tv_cargo.setText("货名");
					tv_weight.setText("货量(吨)");
					tv_price.setText("运价(元/吨)");
					cleanData();
				}
			}
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN)
			isSelected = true;
		return false;
	}

	//TODO： 只有船代及其业务员才能换船
	private boolean canChangShip(OrderData od) {
		if (od != null && (currentUser.getId().equals(od.getBroker().getId()))) {
			return true;
		}
		return false;
	}

	private void cleanData() {
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
		unitPrice.setText("0.0");
		ctvolume.setText("0");
		totalPrice.setText("0.0");
	}
}
