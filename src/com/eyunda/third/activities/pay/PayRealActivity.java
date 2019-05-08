package com.eyunda.third.activities.pay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.eyunda.main.CommonActivity;
import com.eyunda.main.view.DialogUtil;
import com.eyunda.third.GlobalApplication;
import com.eyunda.third.activities.account.BankListActivity;
import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.EnumConst.BankCode;
import com.eyunda.third.loaders.Data_loader;
import com.hy.client.R;
import com.nostra13.universalimageloader.core.ImageLoader;
//扫码付款
public class PayRealActivity extends CommonActivity {


	private Data_loader mDataLoader;
	private ImageLoader mImageLoader;
	private DialogUtil mDialogUtil;
	private ProgressDialog mDialog;

	
	private UserData currentUser;
	@Bind(R.id.btnSave) Button btnSave;

	@Bind(R.id.tansNum) TextView tansNum;

	@Bind(R.id.userName) TextView userName;

	@Bind(R.id.descript) TextView descript;
	@Bind(R.id.useSure) TextView useSure;
	@Bind(R.id.sureDate) TextView sureDate;
	@Bind(R.id.surex) LinearLayout surex;

	String uName,num,qcodeId,desc;
	int uSure,sDate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyd_money_pay_real);
		ButterKnife.bind(this);
		Intent intent = getIntent();
		uName = intent.getStringExtra("name");
		num = intent.getStringExtra("num");
		desc = intent.getStringExtra("desc");
		qcodeId = intent.getStringExtra("qcodeId");
		uSure = intent.getIntExtra("useSure", 0);
		sDate = intent.getIntExtra("sureDate", 0);
		currentUser = GlobalApplication.getInstance().getUserData();
		
		mDataLoader=new Data_loader();
		mImageLoader = ImageLoader.getInstance();
		mDialogUtil = new DialogUtil(this);
		
		initData();
	}
	

	private void initData(){
		
	}
	
	private void initView(){
		tansNum.setText(num);
		userName.setText(uName);
		descript.setText(desc);
		sureDate.setText(sDate);
		if(uSure>0){
			useSure.setText("担保交易");
			surex.setVisibility(View.VISIBLE);
		}else{
			useSure.setText("无担保");
			surex.setVisibility(View.GONE);
		}
	}

	
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("扫描付款");
		initView();
	}

	//保存
	@OnClick(R.id.btnSave) void translate(View v){
		
	}

}
