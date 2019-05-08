package com.eyunda.third.activities.user;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.MenuActivity;
import com.eyunda.third.activities.account.BindBankCardActivity;
import com.eyunda.third.adapters.chat.manager.UserSetManager;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.common.CommonListActivity;
import com.eyunda.third.domain.account.OperatorData;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.loaders.Data_loader;
import com.eyunda.tools.ImageCompress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ApplyAgentActivity extends CommonListActivity implements
		OnClickListener {
	private static final String TAG = "ApplyAgentActivity";
	private static final int REQUEST_CODE_GETSYSIMG = 1;
	private static final int REQUEST_CODE_CROPIMG_CARDFRONT = 3;
	private static final int REQUEST_CODE_CROPIMG_CARDREVERSE = 4;
	private static final int REQUEST_CODE_CROPIMG_BUSINESS = 5;
	private static final int REQUEST_CODE_CROPIMG_TAX = 6;
	// private Spinner et_company_name = null;

	// private Button bt_area;
	private ScrollView test_pop_layout;

	private Button company_state_value, apply_agent_submit, apply_agent_back;
	private EditText et_unitName,
	// et_company_address, et_postcode, et_telephone, et_faxes,
	et_legal_representative;
	private ImageView img_card_front, img_card_reverse, img_business_license,
			img_tax_certificate;

	private int type = 0;
	private String picPath = null;
	private String card_frontPath = null;
	private String card_reversePath = null;
	private String business_licensePath = null;
	private String tax_certificatePath = null;

	private BasePopupWindow popupWindow;// 弹出菜单
	DialogUtil dialogUtil;
	ProgressDialog dialog;
	Data_loader data;
	OperatorData operatorData;
	// String comAdSelected;
	// List<CompanyData> list;

	ImageCompress compress;
	ImageCompress.CompressOptions options;
	private View btnA;
	private View btnB;
	private View btnC;
	private View btnD;
	private View btnF;
	private View btnG;
	private View btnH;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eyd_user_activity_apply_agent);
		operatorData = UserSetManager.getInstance().getOperatorData();
		dialogUtil = new DialogUtil(this);
		data = new Data_loader();
		compress = new ImageCompress();
		options = new ImageCompress.CompressOptions();
		// et_company_name = (Spinner) findViewById(R.id.et_company_name);
		// bt_area = (Button) this.findViewById(R.id.bt_area);
		test_pop_layout = (ScrollView) this.findViewById(R.id.test_pop_layout);
		// final BigAreaSelect asStart = new BigAreaSelect(this, bt_area);
		// bt_area.setOnClickListener(new OnClickListener() {

		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// PopupWindow popupw = asStart.makePopupWindow();
		// int[] xy = new int[2];
		// test_pop_layout.getLocationOnScreen(xy);
		// int localHeight = asStart.getHeight();
		// popupw.showAtLocation(test_pop_layout, Gravity.CENTER
		// | Gravity.BOTTOM, 0, -localHeight);
		//
		// }
		// });
		company_state_value = (Button) this
				.findViewById(R.id.company_state_value);

		apply_agent_submit = (Button) this
				.findViewById(R.id.apply_agent_submit);
		apply_agent_back = (Button) this.findViewById(R.id.apply_agent_back);

		img_card_front = (ImageView) this.findViewById(R.id.img_card_front);
		img_card_reverse = (ImageView) this.findViewById(R.id.img_card_reverse);
		img_business_license = (ImageView) this
				.findViewById(R.id.img_business_license);
		img_tax_certificate = (ImageView) this
				.findViewById(R.id.img_tax_certificate);
		img_card_front.setOnClickListener(this);
		img_card_reverse.setOnClickListener(this);
		img_business_license.setOnClickListener(this);
		img_tax_certificate.setOnClickListener(this);
		apply_agent_submit.setOnClickListener(this);
		apply_agent_back.setOnClickListener(this);

		// et_company_address = (EditText) this
		// .findViewById(R.id.et_company_address);
		// et_postcode = (EditText) this.findViewById(R.id.et_postcode);
		// et_telephone = (EditText) this.findViewById(R.id.et_telephone);
		// et_faxes = (EditText) this.findViewById(R.id.et_faxes);
		et_unitName = (EditText) this.findViewById(R.id.et_unitName);
		et_legal_representative = (EditText) this
				.findViewById(R.id.et_legal_representative);
		setPopupWindow();
		setView();
	}

	private void setPopupWindow() {
		popupWindow = new BasePopupWindow(this);
		btnA = popupWindow.getBtnA();
		btnA.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(ApplyAgentActivity.this,
						BasicInfoActivity.class));
			}
		});
		btnB = popupWindow.getBtnB();
		btnB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(ApplyAgentActivity.this,
						AccountListActivity.class));
			}
		});
		btnC = popupWindow.getBtnC();
		btnC.setVisibility(View.GONE);

		btnD = popupWindow.getBtnD();
		btnD.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(ApplyAgentActivity.this,
						AgentActivity.class));
			}
		});
		btnF = popupWindow.getBtnF();
		btnF.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(ApplyAgentActivity.this,
						UpdatePwd.class));
			}
		});
		// 管理子账号
		btnG = popupWindow.getBtnG();
		btnG.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(ApplyAgentActivity.this,
						UserChildListActivity.class));
			}
		});
		btnH = popupWindow.getBtnH();
		btnH.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.closePopWindow();// 销毁popupwindow
				startActivity(new Intent(ApplyAgentActivity.this,BindBankCardActivity.class));
			}
		});
		UserData user = GlobalApplication.getInstance().getUserData();
		if (user != null) {
			if (user.isChildUser()) {
				btnA.setVisibility(View.VISIBLE);
				btnB.setVisibility(View.GONE);
				btnD.setVisibility(View.GONE);
				btnF.setVisibility(View.VISIBLE);
				btnG.setVisibility(View.GONE);
				btnH.setVisibility(View.VISIBLE);
			}else {
				btnA.setVisibility(View.VISIBLE);
				btnB.setVisibility(View.GONE);
				btnF.setVisibility(View.VISIBLE);
				btnH.setVisibility(View.VISIBLE);
				if(user.isRealUser()){
					btnD.setVisibility(View.VISIBLE);
					btnG.setVisibility(View.VISIBLE);
				}else {
					btnD.setVisibility(View.GONE);
					btnG.setVisibility(View.GONE);
				}
			}
		}
	}

	// @SuppressWarnings({ "rawtypes", "unchecked" })
	private void setView() {
		// List<String> list_spinner_account_way = new ArrayList<String>();
		// list = UserSetManager.getInstance().getCompanyDatas();
		if (operatorData != null) {
			// // spinner
			// if (operatorData.getCompanyData() != null
			// && !operatorData.getCompanyData().getUnitCode().equals("")) {
			// list_spinner_account_way.add(operatorData.getCompanyData()
			// .getUnitName());
			// if (list.size() > 0) {
			// for (CompanyData cd : list) {
			// if (cd.getUnitName().equals(
			// operatorData.getCompanyData().getUnitName()))
			// continue;
			// list_spinner_account_way.add(cd.getUnitName());
			// }
			// }
			// } else {
			// if (list.size() > 0) {
			// for (CompanyData cd : list) {
			// list_spinner_account_way.add(cd.getUnitName());
			// }
			// }
			// }
			// // others
			company_state_value.setText(operatorData.getStatus()
					.getDescription());
			// // et_company_address.setText(operatorData.getAddress());
			// // et_postcode.setText(operatorData.getPostCode());
			// // et_telephone.setText(operatorData.getTelephone());
			// // et_faxes.setText(operatorData.getFax());
			et_legal_representative.setText(operatorData.getLegalPerson());
			// // if (operatorData.getAreaCode() != null
			// // && !operatorData.getAreaCode().equals("")) {
			// // for (PortCityCode pcc : PortCityCode.values()) {
			// // if (pcc.getCode().equals(operatorData.getAreaCode()))
			// // bt_area.setText(pcc.getDescription());
			// // }
			// // }
			// // imgs
			et_unitName.setText(operatorData.getUserData().getUnitName());
			
			ImageLoader mImageLoader = ImageLoader.getInstance();
			mImageLoader.init(GlobalApplication.getInstance()
					.getImageLoaderConfiguration());
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL
					+ operatorData.getIdCardFront(), img_card_front,
					MyshipAdapter.displayImageOptions);
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL
					+ operatorData.getIdCardBack(), img_card_reverse,
					MyshipAdapter.displayImageOptions);
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL
					+ operatorData.getBusiLicence(), img_business_license,
					MyshipAdapter.displayImageOptions);
			mImageLoader.displayImage(ApplicationConstants.IMAGE_URL
					+ operatorData.getTaxLicence(), img_tax_certificate,
					MyshipAdapter.displayImageOptions);
		}// else {
			// if (list.size() > 0) {
		// for (CompanyData cd : list) {
		// list_spinner_account_way.add(cd.getUnitName());
		// }
		// }
		// }
		// ArrayAdapter adapter_spinner_account_way = new ArrayAdapter(this,
		// R.layout.spinner_item, R.id.contentTextView,
		// list_spinner_account_way);
		// et_company_name.setAdapter(adapter_spinner_account_way);
		// et_company_name.setPrompt("公司名称");
		// et_company_name
		// .setOnItemSelectedListener(new SpinnerOnSelectedListener());
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.apply_agent_submit:
			// if (card_frontPath == null) {
			// Toast.makeText(this, "请选择头像图片！", 1000).show();
			// break;
			// } else if (card_reversePath == null) {
			// Toast.makeText(this, "请选择签名图片！", 1000).show();
			// break;
			// } else if (business_licensePath == null) {
			// Toast.makeText(this, "请选择签名图片！", 1000).show();
			// break;
			// } else if (tax_certificatePath == null) {
			// Toast.makeText(this, "请选择图章图片！", 1000).show();
			// break;
			// }
			// File card_frontfile = new File(card_frontPath);
			// File card_reversefile = new File(card_reversePath);
			// File business_licensefile = new File(business_licensePath);
			// File tax_certificatefile = new File(tax_certificatePath);
			//
			// if (card_frontfile != null && card_reversefile != null
			// && business_licensefile != null
			// && tax_certificatefile != null) {
			// // int request = UploadUtil.uploadFile(file, requestURL);
			// // if(request==200){
			// Toast.makeText(this, "基本信息提交成功！", Toast.LENGTH_SHORT).show();
			// Intent intent = new Intent(this, LoginActivity.class);
			//
			// startActivity(intent);
			// }
			// }
			String unitName = et_unitName.getText().toString().trim();
			if (TextUtils.isEmpty(unitName)) {
				Toast.makeText(this, "公司名称不能为空！", Toast.LENGTH_SHORT).show();
				et_unitName.requestFocus();
				return;
			} 
			loadDate(unitName);
			break;
		case R.id.apply_agent_back:
			Intent intent1 = new Intent(this, MenuActivity.class);

			startActivity(intent1);
			break;
		case R.id.img_card_front:
			type = 1;
			getSystemPic();
			break;
		case R.id.img_card_reverse:
			type = 2;
			getSystemPic();
			break;
		case R.id.img_business_license:
			type = 3;
			getSystemPic();
			break;
		case R.id.img_tax_certificate:
			type = 4;
			getSystemPic();
			break;

		default:
			break;
		}
	}

	private void getSystemPic() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, REQUEST_CODE_GETSYSIMG);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			// 裁剪后图片
			if (requestCode == REQUEST_CODE_CROPIMG_CARDFRONT) {
				String cropedImgPath = data.getStringExtra("cropedImgPath");
				if (cropedImgPath != null) {
					card_frontPath = cropedImgPath;
					img_card_front.setImageBitmap(BitmapFactory
							.decodeFile(cropedImgPath));
				} else {
					Toast.makeText(ApplyAgentActivity.this, "裁剪图片失败", 1).show();
				}
			}
			if (requestCode == REQUEST_CODE_CROPIMG_CARDREVERSE) {
				String cropedImgPath = data.getStringExtra("cropedImgPath");
				if (cropedImgPath != null) {
					card_reversePath = cropedImgPath;
					img_card_reverse.setImageBitmap(BitmapFactory
							.decodeFile(cropedImgPath));
				} else {
					Toast.makeText(ApplyAgentActivity.this, "裁剪图片失败", 1).show();
				}
			}
			if (requestCode == REQUEST_CODE_CROPIMG_BUSINESS) {
				String cropedImgPath = data.getStringExtra("cropedImgPath");
				if (cropedImgPath != null) {
					business_licensePath = cropedImgPath;
					img_business_license.setImageBitmap(BitmapFactory
							.decodeFile(cropedImgPath));
				} else {
					Toast.makeText(ApplyAgentActivity.this, "裁剪图片失败", 1).show();
				}
			}
			if (requestCode == REQUEST_CODE_CROPIMG_TAX) {
				String cropedImgPath = data.getStringExtra("cropedImgPath");
				if (cropedImgPath != null) {
					tax_certificatePath = cropedImgPath;
					img_tax_certificate.setImageBitmap(BitmapFactory
							.decodeFile(cropedImgPath));
				} else {
					Toast.makeText(ApplyAgentActivity.this, "裁剪图片失败", 1).show();
				}
			}

			// 取图库图片
			if (requestCode == REQUEST_CODE_GETSYSIMG) {
				/**
				 * 当选择的图片不为空的话，在获取到图片的途径
				 */
				Uri uri = data.getData();
				Log.e(TAG, "uri = " + uri);
				try {
					// 处理图片分辨率太大imageview不能显视出来
					options.uri = uri;
					options.maxWidth = getWindowManager().getDefaultDisplay()
							.getWidth();
					options.maxHeight = getWindowManager().getDefaultDisplay()
							.getHeight();
					Bitmap bitmap = compress.compressFromUri(this, options);

					String[] pojo = { MediaStore.Images.Media.DATA };

					Cursor cursor = managedQuery(uri, pojo, null, null, null);
					if (cursor != null) {
						ContentResolver cr = this.getContentResolver();
						int colunm_index = cursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						String picPath = cursor.getString(colunm_index);
						/***
						 * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，
						 * 你选择的文件就不一定是图片了， 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
						 */
						if (picPath.endsWith("jpg") || picPath.endsWith("png")
								|| picPath.endsWith("jpeg")) {
							if (type == 1) {
								Intent intent = new Intent(
										ApplyAgentActivity.this,
										CropImageActivity.class);
								intent.putExtra("picPath", uri);
								startActivityForResult(intent,
										REQUEST_CODE_CROPIMG_CARDFRONT);
								// Bitmap bitmap = BitmapFactory.decodeStream(cr
								// .openInputStream(uri));
								// card_frontPath = picPath;
								// img_card_front.setImageBitmap(bitmap);
							} else if (type == 2) {
								Intent intent = new Intent(
										ApplyAgentActivity.this,
										CropImageActivity.class);
								intent.putExtra("picPath", uri);
								startActivityForResult(intent,
										REQUEST_CODE_CROPIMG_CARDREVERSE);
								// Bitmap bitmap = BitmapFactory.decodeStream(cr
								// .openInputStream(uri));
								// card_reversePath = picPath;
								// img_card_reverse.setImageBitmap(bitmap);
							} else if (type == 3) {
								Intent intent = new Intent(
										ApplyAgentActivity.this,
										CropImageActivity.class);
								intent.putExtra("picPath", uri);
								startActivityForResult(intent,
										REQUEST_CODE_CROPIMG_BUSINESS);
								// Bitmap bitmap = BitmapFactory.decodeStream(cr
								// .openInputStream(uri));
								// business_licensePath = picPath;
								// img_business_license.setImageBitmap(bitmap);
							} else {
								Intent intent = new Intent(
										ApplyAgentActivity.this,
										CropImageActivity.class);
								intent.putExtra("picPath", uri);
								startActivityForResult(intent,
										REQUEST_CODE_CROPIMG_TAX);
								// Bitmap bitmap = BitmapFactory.decodeStream(cr
								// .openInputStream(uri));
								// tax_certificatePath = picPath;
								// img_tax_certificate.setImageBitmap(bitmap);
							}

						} else {
							alert();
						}
					} else {
						alert();
					}

				} catch (Exception e) {
				}
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void alert() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("您选择的不是有效的图片")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						picPath = null;
					}
				}).create();
		dialog.show();
	}

	// class SpinnerOnSelectedListener implements OnItemSelectedListener {
	//
	// @Override
	// public void onItemSelected(AdapterView<?> adapterView, View view,
	// int position, long id) {
	// comAdSelected = adapterView.getItemAtPosition(position).toString();
	// System.out.println(comAdSelected);
	// }
	//
	// @Override
	// public void onNothingSelected(AdapterView<?> adapterView) {
	// System.out.println("nothingSelected");
	//
	// }
	//
	// }

	@Override
	public void onStart() {
		super.onStart();
		setTitle("申请成为代理人");
		setRight(R.drawable.commen_top_right, new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.togglePopWindow(v);
			}
		});

	}

	// @Override
	// protected void onDestroy() {
	// finish();
	// }

	@Override
	protected BaseAdapter setAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	protected void loadDate(String unitName) {

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

				dialog = dialogUtil.loading("提交中", "请稍候...");
			}

			@Override
			public void onSuccess(String content) {
				dialog.dismiss();
				Log.i("userinfo", content);
				Gson gson = new Gson();
				HashMap<String, Object> map = gson.fromJson((String) content,
						new TypeToken<Map<String, Object>>() {
						}.getType());

				if (map.get("returnCode").equals("Success")) {
					Toast.makeText(ApplyAgentActivity.this, "信息提交成功", 1).show();
					// UserSetManager.getInstance().synLoadDate();
					startActivity(new Intent(ApplyAgentActivity.this,
							BasicInfoActivity.class));
					finish();
				} else {
					Toast.makeText(ApplyAgentActivity.this,
							map.get("message").toString(), 1).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Toast.makeText(ApplyAgentActivity.this, "信息提交失败", 1).show();
				dialog.dismiss();

			}

		};

		Map<String, Object> params = new HashMap<String, Object>();
		if (operatorData != null) {
			params.put("id", operatorData.getId());
			params.put("applyTimeSt", operatorData.getApplyTimeSt());
			params.put("status", operatorData.getStatus());
			// if (operatorData.getCompanyData() != null)
			// params.put("unitCode", operatorData.getCompanyData()
			// .getUnitCode());
		}
		// if (comAdSelected != null) {
		// for (int i = 0; i < list.size(); i++) {
		// if (list.get(i).getUnitName().equals(comAdSelected))
		// params.put("unitCode", list.get(i).getUnitCode());
		// }
		// }
		/*
		 * if (!bt_area.getText().equals("请选择")) { PortCityCode arr[] =
		 * PortCityCode.values(); for (PortCityCode pcc : arr) { if
		 * (pcc.getDescription().equals(bt_area.getText()))
		 * params.put("areaCode", pcc.getCode()); } }
		 */
		params.put("userId", GlobalApplication.getInstance().getUserData()
				.getId());
		 params.put("unitName", unitName);
		// params.put("address", et_company_address.getText());
		// params.put("postCode", et_postcode.getText());
		// params.put("telephone", et_telephone.getText());
		// params.put("fax", et_faxes.getText());
		params.put("legalPerson", et_legal_representative.getText().toString()
				.trim());
		if (card_frontPath != null)
			params.put("idCardFrontFile", new File(card_frontPath));
		if (card_reversePath != null)
			params.put("idCardBackFile", new File(card_reversePath));
		if (business_licensePath != null)
			params.put("busiLicenceFile", new File(business_licensePath));
		if (tax_certificatePath != null)
			params.put("taxLicenceFile", new File(tax_certificatePath));
		data.getApiResult(handler, "/mobile/account/myAccount/saveApply",
				params);
	}

	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub
		
	}

}
