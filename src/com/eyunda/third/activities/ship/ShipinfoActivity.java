package com.eyunda.third.activities.ship;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.user.CropImageActivity;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.domain.ConvertData;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.EnumConst.PortCityCode;
import com.eyunda.third.domain.enumeric.WarrantTypeCode;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.domain.ship.TypeData;
import com.eyunda.third.loaders.Data_loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;
//新增船舶
public class ShipinfoActivity extends CommonActivity implements OnClickListener {
	public static final int REQUEST_CODE_GETSYSIMG = 2;
	public static final int REQUEST_CODE_CROPIMG = 3;


	Button menu_basic, menu_class, menu_upload, menu_report, menu_order,menu_auth, save,mDownAtthBtn;
	private Spinner category, name, mWtcSpinner;
	protected PopupWindow popupwindow;
	private View layout;
	private EditText shipname, shipLine, shipdes, shipMMSI, shipEnName, imoNum,
			callNum, length, with, deep, totalWeight, netWeight, tonA, tonB,
			fullContainer, halfContainer, spaceContainer, mouldedDepth,shipMaster;
	private String picturePath, shipNameText, logoUrl="", idCardFront="",idCardBack="", warrant="", certificate="", idCardFrontPath,idCardBackPath, warrantPath, certificatePath;
	DialogUtil dialogUtil;
	ProgressDialog dialog;
	ImageLoader mImageLoader;
	Data_loader data = new Data_loader();
	private ArrayAdapter<SpinnerItem> namesAdapter;
	protected String id;
	private ImageView ship_image,ship_proxy,ship_operation_certificate,ship_identity_front,ship_identity_back;
	private String carrier, ship;
	ArrayList<SpinnerItem> namelists;
	private ArrayList<String> areaPos;
	private ArrayList<String> typePos;
	private String MMsi;
	private ShipData shipData;
	private WarrantTypeCode mWtc;
	private TextView textViewSIF,textViewSIB;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_shipinfo);

		mImageLoader = ImageLoader.getInstance();
		initViews();
		// 接收参数
		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		id = bundle.getString("shipId"); // id=0是新增，id!=0是编辑，从adapter传值过来
		shipData = (ShipData) bundle.getSerializable("shipInfo");
		if (shipData != null) {

			logoUrl = shipData.getShipLogo();
			idCardFront = shipData.getIdCardFront();
			idCardBack = shipData.getIdCardBack();
			certificate = shipData.getCertificate();
			warrant = shipData.getWarrant();
			String name = shipData.getMaster().getTrueName();
			if (name.equals("")) {
				carrier = shipData.getMaster().getLoginName();
			} else
				carrier = name;
			ship = shipData.getTypeData().getTypeName();
			getDatas();
		}

	}

	private void getDatas() {
		// 加载图片
		if (!"".equals(logoUrl))
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + logoUrl,
					ship_image, MyshipAdapter.displayImageOptions);
		if (!"".equals(idCardFront))
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + idCardFront,
					ship_identity_front, MyshipAdapter.displayImageOptions);
		if (!"".equals(idCardBack))
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + idCardBack,
					ship_identity_back, MyshipAdapter.displayImageOptions);
		if (!"".equals(certificate))
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + certificate,
					ship_operation_certificate, MyshipAdapter.displayImageOptions);
		if (!"".equals(warrant))
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL + warrant,
					ship_proxy, MyshipAdapter.displayImageOptions);
		shipMaster.setText(shipData.getShipMaster());
		shipname.setText(shipData.getShipName());
		shipLine.setText(shipData.getKeyWords());
		shipdes.setText(shipData.getShipTitle());
		shipMMSI.setText(shipData.getMmsi());
		shipEnName.setText(shipData.getEnglishName());
		imoNum.setText(shipData.getImo());
		callNum.setText(shipData.getCallsign());
		length.setText(shipData.getLength().toString());
		with.setText(shipData.getBreadth().toString());
		deep.setText(shipData.getDraught().toString());
		mouldedDepth.setText(shipData.getMouldedDepth().toString());
		totalWeight.setText(shipData.getSumTons().toString());
		netWeight.setText(shipData.getCleanTons().toString());
		tonA.setText(shipData.getaTons().toString());
		tonB.setText(shipData.getbTons().toString());
		fullContainer.setText(shipData.getFullContainer().toString());
		halfContainer.setText(shipData.getHalfContainer().toString());
		spaceContainer.setText(shipData.getSpaceContainer().toString());
		if(shipData.getWarrantType().equals(WarrantTypeCode.companyWarrant)){
			mWtcSpinner.setSelection(1);

		}else{
			mWtcSpinner.setSelection(0);

		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		menu_basic.setBackgroundColor(0xFF6db7ff);
		setTitle("基本属性");
		if (!id.equals("0")) {

			setRight(R.drawable.commen_top_right, new OnClickListener() {

				@Override
				public void onClick(View v) {
					// togglePopWindow(v);
					popupwindow.showAsDropDown(v, 0, 0);

				}

			});
		} else {
			setRight("关闭", new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		setSpinner();
	}

	private void initViews() {
		textViewSIF = (TextView) findViewById(R.id.textViewSIF);
		textViewSIB = (TextView) findViewById(R.id.textViewSIB);

		category = (Spinner) findViewById(R.id.leibie);
		name = (Spinner) findViewById(R.id.master);//船东

		mWtcSpinner = (Spinner) findViewById(R.id.AttType);
		shipMaster = (EditText) findViewById(R.id.shipMaster);
		shipname = (EditText) findViewById(R.id.editText2);
		shipLine = (EditText) findViewById(R.id.shipAdv);
		shipdes = (EditText) findViewById(R.id.shipTitle);
		shipMMSI = (EditText) findViewById(R.id.MMSI);
		shipEnName = (EditText) findViewById(R.id.englishName);
		imoNum = (EditText) findViewById(R.id.imo);
		callNum = (EditText) findViewById(R.id.callNum);
		length = (EditText) findViewById(R.id.length);
		with = (EditText) findViewById(R.id.width);
		deep = (EditText) findViewById(R.id.deep);
		totalWeight = (EditText) findViewById(R.id.sumTons);
		netWeight = (EditText) findViewById(R.id.netWeight);
		tonA = (EditText) findViewById(R.id.aji);
		tonB = (EditText) findViewById(R.id.bji);
		fullContainer = (EditText) findViewById(R.id.weightb);
		halfContainer = (EditText) findViewById(R.id.halfb);
		spaceContainer = (EditText) findViewById(R.id.gdb);
		mouldedDepth = (EditText) findViewById(R.id.mouldedDepth);

		ship_image = (ImageView) this.findViewById(R.id.logo);
		ship_image.setOnClickListener(this);
		
		
		save = (Button) this.findViewById(R.id.baocun);
		save.setOnClickListener(this);
		dialogUtil = new DialogUtil(this);

		// 弹出菜单
		LayoutInflater inflater = LayoutInflater.from(this);
		layout = inflater.inflate(R.layout.eyd_popup_view2, null);
		popupwindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		// 设置popupwindow的内容

		ColorDrawable dw = new ColorDrawable(0000000000);
		popupwindow.setBackgroundDrawable(dw);
		popupwindow.setOutsideTouchable(true);
		popupwindow.setFocusable(true);

		menu_basic = (Button) layout.findViewById(R.id.menu_basic);
		menu_basic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closePopWindow();
				Bundle bundle = new Bundle();
				Intent intent = new Intent(getApplicationContext(),
						ShipinfoActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				bundle.putString("shipId", id);// ID
				bundle.putSerializable("shipInfo", shipData);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();

			}
		});

		menu_class = (Button) layout.findViewById(R.id.menu_class);
		menu_class.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closePopWindow();
				Bundle bundle = new Bundle();
				Intent intent = new Intent(getApplicationContext(),
						ShipAttrActivity.class);
				bundle.putString("shipId", id);// ID
				bundle.putSerializable("shipInfo", shipData);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		menu_upload = (Button) layout.findViewById(R.id.menu_upload);
		menu_upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closePopWindow();
				Bundle bundle = new Bundle();
				Intent intent = new Intent(getApplicationContext(),
						UploadActivity.class);
				bundle.putString("shipId", id);// ID
				bundle.putSerializable("shipInfo", shipData);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		menu_report = (Button) layout.findViewById(R.id.menu_report);
		menu_report.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closePopWindow();
				Bundle bundle = new Bundle();
				Intent intent = new Intent(getApplicationContext(),
						ShipDeliveryActivity.class);
				// bundle.putSerializable("user", user);//对象
				bundle.putString("shipId", id);// ID
				bundle.putSerializable("shipInfo", shipData);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		menu_order = (Button) layout.findViewById(R.id.menu_order);
		menu_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closePopWindow();
				Bundle bundle = new Bundle();
				Intent intent = new Intent(getApplicationContext(),
						ContractTemplateActivity.class);
				bundle.putString("shipId", id);// ID
				bundle.putSerializable("shipInfo", shipData);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		menu_auth = (Button) layout.findViewById(R.id.menu_auth);
		menu_auth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closePopWindow();
				Bundle bundle = new Bundle();
				Intent intent = new Intent(getApplicationContext(),
						ShipAuthDataActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				bundle.putString("shipId", id);// ID
				bundle.putSerializable("shipInfo", shipData);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();

			}
		});

	}

	private void setSpinner() {

		ArrayAdapter<SpinnerItem> attrAdapter = new ArrayAdapter<SpinnerItem>(
				this, R.layout.spinner_item, R.id.contentTextView,
				getTypeList());
		category.setAdapter(attrAdapter);
		for (int i = 0; i < typePos.size(); i++) {
			if (ship != null) {
				if (ship.equals(typePos.get(i))) {
					category.setSelection(i);
				}
			}
		}
		ArrayList<SpinnerItem> attList = new ArrayList<SpinnerItem>();
		for (WarrantTypeCode w : WarrantTypeCode.values()) {
			SpinnerItem st = new SpinnerItem(w.name(), w.getDescription());
			attList.add(st);
		}
		ArrayAdapter<SpinnerItem> attrTypeAdapter = new ArrayAdapter<SpinnerItem>(
				this, R.layout.spinner_item, R.id.contentTextView,
				attList);
		mWtcSpinner.setAdapter(attrTypeAdapter);
		mWtcSpinner.setSelection(0);
		mWtc = WarrantTypeCode.personWarrant;

		

		

		namesAdapter = new ArrayAdapter<SpinnerItem>(this,
				R.layout.spinner_item, R.id.contentTextView, getNameList());

	}

	
	/**
	 * 地区
	 * 
	 * @return areaList
	 */
	private List<SpinnerItem> getArealist() {
		ArrayList<SpinnerItem> areaList = new ArrayList<SpinnerItem>();
		areaPos = new ArrayList<String>();
		for (PortCityCode e : PortCityCode.values()) {
			String cur = e.getFullName();
			SpinnerItem items = new SpinnerItem(e.getCode(), cur);
			areaPos.add(cur);
			areaList.add(items);

		}
		return areaList;
	}

	/**
	 * 船舶类别 读取本地文件 typelists
	 * 
	 * @return
	 */
	private List<SpinnerItem> getTypeList() {
		final ArrayList<SpinnerItem> typelists = new ArrayList<SpinnerItem>();
		typePos = new ArrayList<String>();
		String fileName = ApplicationConstants.LF_SEARCH_SHIP_LIST;
		try {
			FileInputStream inputStream = this.openFileInput(fileName);
			byte[] bytes = new byte[1024];
			int len = 0;
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			while ((len = inputStream.read(bytes)) != -1) {
				arrayOutputStream.write(bytes, 0, len);
			}
			inputStream.close();
			arrayOutputStream.close();
			String content = new String(arrayOutputStream.toByteArray());
			ConvertData cd = new ConvertData(content);
			HashMap<String, Object> var = (HashMap<String, Object>) cd.getContent();
			// List dat =(List<String>) var.get("myShipDatas");
			List<Map<String, Object>> dat = (List<Map<String, Object>>) var.get("uncleTypeDatas");
			if (dat.size() > 0) {
				for (int i = 0; i < dat.size(); i++) {
					TypeData type = new TypeData(
							(Map<String, Object>) dat.get(i));
					List<TypeData> list = type.getChildrenDatas();
					for (int j = 0; j < list.size(); j++) {
						// 遍历找到TypeData
						TypeData td = (TypeData) list.get(j);
						typePos.add(td.getTypeName());
						SpinnerItem items = new SpinnerItem(td.getTypeCode(),
								td.getTypeName());
						typelists.add(items);

					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return typelists;
	}

	/**
	 * 代理的船东列表
	 * 
	 * @return namelists
	 */
	private List<SpinnerItem> getNameList() {
		namelists = new ArrayList<SpinnerItem>();
		final String uri = "/mobile/ship/getAgentUsers";
		final Map<String, Object> map = new HashMap<String, Object>();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				
				ConvertData cd = new  ConvertData(arg0,uri,map) ;
				if (cd != null && cd.getReturnCode().equalsIgnoreCase("Success")) {
					HashMap<String, Object> content = (HashMap<String, Object>) cd.getContent();

					List<UserData> lists = (List<UserData>) content.get("agentUsers");
					ArrayList<String> cyrList = new ArrayList<String>();
					int size = lists.size();
					if (size > 0) {
						for (int i = 0; i < lists.size(); i++) {
							UserData trueName =new UserData(
									(Map<String, Object>) lists.get(i));
							String cname = trueName.getTrueName(); // trueName
							if (cname.equals("")) {
								cname = trueName.getLoginName();
							}
							String carrierId = trueName.getId().toString();// carrierId
							cyrList.add(cname);
							SpinnerItem items = new SpinnerItem(carrierId,cname);
							namelists.add(items);
							name.setAdapter(namesAdapter);
							// 设置spinner下拉的默认值
							if (carrier != null) {
								for (int j = 0; j < cyrList.size(); j++) {

									if (carrier.equals(cyrList.get(j))) {

										name.setSelection(j);
										break;
									}
								}
							}
						}
					}
				}
			}

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);

			}
		};
		data.getApiResult(handler, uri, map, "get");

		return namelists;
	}

	public void closePopWindow() {
		popupwindow.dismiss();

	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(
				Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		switch (v.getId()) {
		case R.id.baocun:

			shipNameText = shipname.getText().toString().trim();
			MMsi = shipMMSI.getText().toString().trim();
			if (TextUtils.isEmpty(shipNameText)) {
				Toast.makeText(this, "船名不能为空！", Toast.LENGTH_SHORT).show();
				shipname.requestFocus();
				return;
			} else if (TextUtils.isEmpty(MMsi)) {
				Toast.makeText(this, "MMSI不能为空！", Toast.LENGTH_SHORT).show();
				shipMMSI.requestFocus();
				return;
			}

			saveBasicInfo();
			break;
		case R.id.logo:
			startActivityForResult(i, REQUEST_CODE_GETSYSIMG);
			break;


		case R.id.top_back:
			startActivity(new Intent(this, MyshipActivity.class));
			break;
		case R.id.downTemplate:
			//委托书模板下载
			startActivity(new Intent(this, WTCTemplateDownloadActivity.class));
			break;

		}
	}

	// 保存基本信息
	protected void saveBasicInfo() {

		AsyncHttpResponseHandler handle = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				dialog = dialogUtil.loading("保存中", "请稍候...");
			}

			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				dialog.dismiss();
				Gson gson = new Gson();
				HashMap<String, Object> map = gson.fromJson((String) arg0,
						new TypeToken<Map<String, Object>>() {
						}.getType());
				if (map.get("returnCode").equals("Success")) {
					Toast.makeText(ShipinfoActivity.this,
							(CharSequence) map.get("message"), 3000).show();
					Intent intent = new Intent(ShipinfoActivity.this,
							MyshipActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("success", true);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(ShipinfoActivity.this,
							(CharSequence) map.get("message"), 3000).show();

				}

			}

			@Override
			public void onFailure(Throwable arg0, String content) {
				super.onFailure(arg0, content);
				dialog.dismiss();
				if (content != null && content.equals("can't resolve host"))
					Toast.makeText(ShipinfoActivity.this, "网络连接异常",
							Toast.LENGTH_LONG).show();
				else if (content != null && content.equals("socket time out")) {
					Toast.makeText(ShipinfoActivity.this, "连接服务器超时",
							Toast.LENGTH_LONG).show();
				} else if (content != null) {
					Log.e("error", content);
					Toast.makeText(ShipinfoActivity.this, content,
							Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(ShipinfoActivity.this, "请求失败",
							Toast.LENGTH_LONG).show();

			}
		};
		
		String master = ((SpinnerItem) name.getSelectedItem()).getId();
		String masterName = ((SpinnerItem) name.getSelectedItem()).getValue();
		String typeCode = ((SpinnerItem) category.getSelectedItem()).getId();

		
		if (TextUtils.isEmpty(length.getText().toString())) {
			Toast.makeText(this, "请输入船长", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(with.getText().toString())) {
			Toast.makeText(this, "请输入船宽", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(mouldedDepth.getText().toString())) {
			Toast.makeText(this, "请输入型深", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(deep.getText().toString())) {
			Toast.makeText(this, "请输入吃水深度", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(totalWeight.getText().toString())) {
			Toast.makeText(this, "请输入总吨", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(netWeight.getText().toString())) {
			Toast.makeText(this, "请输入净吨", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(tonA.getText().toString())) {
			Toast.makeText(this, "请输入载重量", Toast.LENGTH_SHORT).show();
			return;
		}
//		if (TextUtils.isEmpty(tonB.getText())) {
//			Toast.makeText(this, "请输入载重B级", Toast.LENGTH_SHORT).show();
//			return;
//		}
		if (TextUtils.isEmpty(fullContainer.getText().toString())) {
			Toast.makeText(this, "请输入载箱量", Toast.LENGTH_SHORT).show();
			return;
		}
//		if (TextUtils.isEmpty(halfContainer.getText())) {
//			Toast.makeText(this, "请输入半重箱", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if (TextUtils.isEmpty(spaceContainer.getText())) {
//			Toast.makeText(this, "请输入吉箱", Toast.LENGTH_SHORT).show();
//			return;
//		}

		Map<String, Object> params = new HashMap<String, Object>();
		if (picturePath != null) {
			params.put("shipLogoFile", new File(picturePath));// 图片Logo
		}else{
//			if(logoUrl.equals("")){
//				Toast.makeText(getApplicationContext(), "请上传"+this.getString(R.string.ship_logo), Toast.LENGTH_LONG).show();
//				return;
//			}
		}
//		if(idCardFrontPath != null){
//			params.put("idCardFrontFile", new File(idCardFrontPath));
//		}else{
//			if(idCardFront.equals("")){
//				Toast.makeText(getApplicationContext(), "请上传"+this.getString(R.string.ship_identity_front), Toast.LENGTH_LONG).show();
//				return;
//			}
//		}
//		if(idCardBackPath != null){
//			params.put("idCardBackFile", new File(idCardBackPath));
//		}else{
//			if(idCardBack.equals("")){
//				Toast.makeText(getApplicationContext(), "请上传"+this.getString(R.string.ship_identity_back), Toast.LENGTH_LONG).show();
//				return;
//			}
//		}
//		if(warrantPath != null){
//			params.put("warrantFile", new File(warrantPath));
//		}else{
//			if(warrant.equals("")){
//				Toast.makeText(getApplicationContext(), "请上传"+this.getString(R.string.ship_proxy), Toast.LENGTH_LONG).show();
//				return;
//			}
//		}
//		if(certificatePath != null){
//			params.put("certificateFile", new File(certificatePath));
//		}else{
//			if(certificate.equals("")){
//				Toast.makeText(getApplicationContext(), "请上传"+this.getString(R.string.ship_operation_certificate), Toast.LENGTH_LONG).show();
//				return;
//			}
//		}
		// params.put("userId", publishId); //发布人id
		params.put("id", Long.parseLong(id)); //shipId
		
		params.put("masterId", master);// 船东Id
		params.put("brokerId", GlobalApplication.getInstance().getUserData().getId());// 船代Id

		params.put("shipType", typeCode); // 船舶类别编码
//		params.put("warrantType", mWtc); // 船舶证件类型
		params.put("shipMaster", masterName);// 船东姓名
		params.put("shipName", shipNameText);// 船舶名
		params.put("imo", imoNum.getText().toString());

		params.put("callsign", callNum.getText().toString());

		params.put("length", length.getText().toString());
		params.put("breadth", with.getText().toString());
		params.put("draught", deep.getText().toString());
		params.put("mouldedDepth", mouldedDepth.getText().toString());
		params.put("sumTons", totalWeight.getText().toString());
		params.put("cleanTons", netWeight.getText().toString());
		params.put("aTons", tonA.getText().toString());
		params.put("bTons", tonB.getText().toString());
		params.put("fullContainer", fullContainer.getText().toString());
		params.put("halfContainer", halfContainer.getText().toString());
		params.put("spaceContainer", spaceContainer.getText().toString());

		params.put("keyWords", shipLine.getText().toString().trim());// 广告词
		params.put("shipTitle", shipdes.getText().toString().trim());// 说明
		params.put("mmsi", MMsi);// mmsi
		params.put("englishName", shipEnName.getText().toString().trim());// 英文名
		data.getApiResult(handle, "/mobile/ship/myShip/saveBaseInfo", params,"post");

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_GETSYSIMG:

				Uri selectedImage = data.getData();

				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				
				
				// 进入头像裁剪页面
				Intent intent = new Intent(ShipinfoActivity.this,
						CropImageActivity.class);
				intent.putExtra("picPath", selectedImage);
				if(requestCode == REQUEST_CODE_GETSYSIMG){
					picturePath = cursor.getString(columnIndex);
					startActivityForResult(intent, REQUEST_CODE_CROPIMG);
				}
				cursor.close();
			break;


			case REQUEST_CODE_CROPIMG:

				// 裁剪后图片
				String cropedImgPath = data.getStringExtra("cropedImgPath");
				if (cropedImgPath != null) {
					if(requestCode == REQUEST_CODE_CROPIMG){
						picturePath = cropedImgPath;
						ship_image.setImageBitmap(BitmapFactory
								.decodeFile(cropedImgPath));
					}
				} else {
					Toast.makeText(this, "裁剪图片失败", Toast.LENGTH_SHORT).show();
				}
				
				break;
				
			}

			
		}
	}
}
