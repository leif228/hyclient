package com.eyunda.third.activities.cargo;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.domain.cargo.CargoData;
import com.hy.client.R;

public class CargoNotifyObjActivity extends CommonActivity implements
		OnClickListener {

	private Button saveBtn;

	private RadioGroup mRadioGroup;
	private RadioButton broker, master;

	private String cargoId;
	private String notifyObj = "broker";
	private CargoData cargoData;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectNetwork().penaltyLog().build());
		setContentView(R.layout.eyd_cargo_notify_obj);

		Bundle bundle = new Bundle();
		bundle = getIntent().getExtras();
		cargoId = (String) bundle.getString("id");
		cargoData = (CargoData) bundle.getSerializable("cargoInfo");

		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		broker = (RadioButton) findViewById(R.id.broker);
		master = (RadioButton) findViewById(R.id.master);
		saveBtn = (Button) findViewById(R.id.buttonSave);
		mRadioGroup.setOnCheckedChangeListener(mChangeRadio);
		saveBtn.setOnClickListener(this);
	}

	private RadioGroup.OnCheckedChangeListener mChangeRadio = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId == broker.getId()) {
				notifyObj = "broker";
			} else if (checkedId == master.getId()) {
				notifyObj = "master";
			}
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
		setTitle("通知对象选择");
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonSave) {
			Intent intent2 = new Intent(CargoNotifyObjActivity.this,
					CargoSendNotifyActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putString("id", cargoId);
			bundle2.putString("notifyObj", notifyObj);
			bundle2.putSerializable("cargoInfo", cargoData);
			intent2.putExtras(bundle2);
			CargoNotifyObjActivity.this.startActivity(intent2);
		}

	}

}
