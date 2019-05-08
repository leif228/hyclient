package com.eyunda.third.activities.ship;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;

import com.eyunda.third.ApplicationConstants;
import com.eyunda.third.activities.user.CropImageActivity;
import com.eyunda.third.adapters.ship.MyshipAdapter;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.enumeric.WarrantTypeCode;
import com.eyunda.third.domain.ship.ShipData;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.util.http.AsyncHttpResponseHandler;

public class ShipAuthDataActivity extends ShipinfoActivity {
	public static final int REQUEST_IF_GETSYSIMG = 4;
	public static final int REQUEST_IF_CROPIMG = 5;
	public static final int REQUEST_IB_GETSYSIMG = 6;
	public static final int REQUEST_IB_CROPIMG = 7;
	public static final int REQUEST_SP_GETSYSIMG = 8;
	public static final int REQUEST_SP_CROPIMG = 9;
	public static final int REQUEST_SC_GETSYSIMG = 10;
	public static final int REQUEST_SC_CROPIMG = 11;
	
	Data_loader dataLoader;
	private String shipId;
	private Spinner mWtcSpinner;
	ImageLoader mImageLoader;
	private ImageView ship_image,ship_proxy,ship_operation_certificate,ship_identity_front,ship_identity_back;
	private String idCardFront="",idCardBack="", warrant="", certificate="", idCardFrontPath,idCardBackPath, warrantPath, certificatePath;

	private WarrantTypeCode mWtc;
	private TextView textViewSIF,textViewSIB;
	
	private ShipData shipData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataLoader = new Data_loader();
		mImageLoader = ImageLoader.getInstance();
		setContentView(R.layout.eyd_ship_auth_data);
		initViews();
		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		shipId = bundle.getString("shipId"); // id=0是新增，id!=0是编辑，从adapter传值过来
		shipData = (ShipData) bundle.getSerializable("shipInfo");
		if (shipData != null) {

			idCardFront = shipData.getIdCardFront();
			idCardBack = shipData.getIdCardBack();
			certificate = shipData.getCertificate();
			warrant = shipData.getWarrant();

		}
		loadData();
		
	}
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("认证资料");

	}


	private void initViews() {
		textViewSIF = (TextView) findViewById(R.id.textViewSIF);
		textViewSIB = (TextView) findViewById(R.id.textViewSIB);
		mWtcSpinner = (Spinner) findViewById(R.id.AttType);
		ship_operation_certificate = (ImageView) this.findViewById(R.id.ship_operation_certificate);
		ship_operation_certificate.setOnClickListener(this);
		ship_proxy = (ImageView) this.findViewById(R.id.ship_proxy);
		ship_proxy.setOnClickListener(this);
		ship_identity_front = (ImageView) this.findViewById(R.id.ship_identity_front);
		ship_identity_front.setOnClickListener(this);
		ship_identity_back = (ImageView) this.findViewById(R.id.ship_identity_back);
		ship_identity_back.setOnClickListener(this);
		mDownAtthBtn = (Button)findViewById(R.id.downTemplate);
		mDownAtthBtn.setOnClickListener(this);
		
		save = (Button) this.findViewById(R.id.baocun);
		save.setOnClickListener(this);
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
		mWtcSpinner.setOnItemSelectedListener(new WtcSpinnerOnItemSelectedListener());
		mWtc = WarrantTypeCode.personWarrant;

	}
	AsyncHttpResponseHandler showHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
		
		}

		public void onFailure(Throwable arg0, String content) {
			super.onFailure(arg0, content);
			
			Toast.makeText(ShipAuthDataActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
		}


		public void onSuccess(String arg0) {
			

		}

	};


	private void loadData(){
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
		if(shipData.getWarrantType().equals(WarrantTypeCode.companyWarrant)){
			mWtcSpinner.setSelection(1);
			changeLab(2);
		}else{
			mWtcSpinner.setSelection(0);
			changeLab(1);
		}
	}

	private void changeLab(int current){
		if(current == 1){
			textViewSIB.setText("船东身份证反面");
			textViewSIF.setText("船东身份证正面");
			mWtc = WarrantTypeCode.personWarrant;
		}else{
			textViewSIB.setText("营业执照");
			textViewSIF.setText("组织机构代码证");
			mWtc = WarrantTypeCode.companyWarrant;
		}
	}
	public class WtcSpinnerOnItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent,
	        View view, int pos, long id) {
	    	if(parent.getItemAtPosition(pos).toString().equals(WarrantTypeCode.companyWarrant.getDescription())){
	    		changeLab(2);
	    	}else{
	    		changeLab(1);
	    	}
	     
	    }

	    public void onNothingSelected(AdapterView parent) {
	      // Do nothing.
	    }
	}
	@Override
	public void onClick(View v) {
		Intent i = new Intent(
				Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		switch (v.getId()) {
		case R.id.baocun:
			
			break;
		case R.id.ship_operation_certificate:
			startActivityForResult(i, REQUEST_SC_GETSYSIMG);
			break;
		case R.id.ship_proxy:
			startActivityForResult(i, REQUEST_SP_GETSYSIMG);
			break;
		case R.id.ship_identity_front:
			startActivityForResult(i, REQUEST_IF_GETSYSIMG);
			break;
		case R.id.ship_identity_back:	
			startActivityForResult(i, REQUEST_IB_GETSYSIMG);

			break;
		case R.id.downTemplate:
			//委托书模板下载
			startActivity(new Intent(this, WTCTemplateDownloadActivity.class));
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_IF_GETSYSIMG:
			case REQUEST_IB_GETSYSIMG:
			case REQUEST_SC_GETSYSIMG:
			case REQUEST_SP_GETSYSIMG:
				Uri selectedImage = data.getData();

				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				
				
				// 进入头像裁剪页面
				Intent intent = new Intent(ShipAuthDataActivity.this,
						CropImageActivity.class);
				intent.putExtra("picPath", selectedImage);
				if(requestCode == REQUEST_IF_GETSYSIMG){
					idCardFrontPath = cursor.getString(columnIndex);
					startActivityForResult(intent, REQUEST_IF_CROPIMG);
				}else if(requestCode == REQUEST_IB_GETSYSIMG){
					idCardBackPath = cursor.getString(columnIndex);
					startActivityForResult(intent, REQUEST_IB_CROPIMG);
				}else if(requestCode == REQUEST_SP_GETSYSIMG){
					warrantPath = cursor.getString(columnIndex);
					startActivityForResult(intent, REQUEST_SP_CROPIMG);
				}else if(requestCode == REQUEST_SC_GETSYSIMG){
					certificatePath = cursor.getString(columnIndex);
					startActivityForResult(intent, REQUEST_SC_CROPIMG);
				}
				
				cursor.close();
				break;

			case REQUEST_IB_CROPIMG:
			case REQUEST_IF_CROPIMG:
			case REQUEST_SC_CROPIMG:
			case REQUEST_SP_CROPIMG:
				// 裁剪后图片
				String cropedImgPath = data.getStringExtra("cropedImgPath");
				if (cropedImgPath != null) {
					if(requestCode == REQUEST_IB_CROPIMG){
						idCardBackPath = cropedImgPath;
						ship_identity_back.setImageBitmap(BitmapFactory.decodeFile(cropedImgPath));
					}else if(requestCode == REQUEST_IF_CROPIMG){
						idCardFrontPath = cropedImgPath;
						ship_identity_front.setImageBitmap(BitmapFactory.decodeFile(cropedImgPath));
					}else if(requestCode == REQUEST_SC_CROPIMG){
						certificatePath = cropedImgPath;
						ship_operation_certificate.setImageBitmap(BitmapFactory.decodeFile(cropedImgPath));
					}else if(requestCode == REQUEST_SP_CROPIMG){
						warrantPath = cropedImgPath;
						ship_proxy.setImageBitmap(BitmapFactory.decodeFile(cropedImgPath));
					}
				} else {
					Toast.makeText(this, "裁剪图片失败", Toast.LENGTH_SHORT).show();
				}
				
				break;
			}

			
			
		}
	}
}
