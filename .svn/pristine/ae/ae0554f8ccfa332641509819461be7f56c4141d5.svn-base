package com.eyunda.third.activities.home;

import java.util.ArrayList;
import java.util.List;

import com.eyunda.main.CommonActivity;
import com.eyunda.third.domain.SpinnerItem;
import com.eyunda.third.domain.enumeric.CargoBigTypeCode;
import com.eyunda.third.domain.enumeric.CargoTypeCode;
import com.eyunda.third.domain.enumeric.CargoVolumnCode;
import com.eyunda.third.domain.enumeric.CargoWeightCode;
import com.eyunda.third.domain.enumeric.EnumConst.PortCityCode;
import com.hy.client.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class FindCargoActivity extends CommonActivity  implements OnItemClickListener,OnItemSelectedListener{
	protected boolean selected;
	private SpinnerTypeAdapter adp,adp2,adp3,adp4;
	private Spinner spTon;
	private Spinner spType;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.eyd_boat_info_bar);
		initViews();

	}
	@Override
	protected void onStart() {
		super.onStart();
		setTitle("按条件查找货物");
	}
	

	private void initViews() {
		selected = false;
	
		 spType = (Spinner)findViewById(R.id.cargoTypes);
		 spTon = (Spinner)findViewById(R.id.tons);
		Spinner spPort = (Spinner)findViewById(R.id.upPort);
		Spinner spPort2 = (Spinner)findViewById(R.id.downPort);
		Button bt =(Button)findViewById(R.id.btnSave);
		bt.setOnClickListener(myOnlcikListener);
		spType.setOnTouchListener(typeSpinnerTouchListener);
		spTon.setOnTouchListener(typeSpinnerTouchListener);
		spPort.setOnTouchListener(typeSpinnerTouchListener);
		spPort2.setOnTouchListener(typeSpinnerTouchListener);
		spType.setOnItemSelectedListener(this);
		spTon.setOnItemSelectedListener(this);
		spPort.setOnItemSelectedListener(this);
		spPort2.setOnItemSelectedListener(this);

		//tag="选择货类";tag2="选择货量"; tag3="选择装货港";tag4="选择到货港";
		adp = new SpinnerTypeAdapter(this,getTypes());
		adp2 =  new SpinnerTypeAdapter(this,getTons());
		adp3 =  new SpinnerTypeAdapter(this,getUpPort());
		adp4 =  new SpinnerTypeAdapter(this,getDownPort());
		spType.setAdapter(adp);
		spTon.setAdapter(adp2);
		spPort.setAdapter(adp3);
		spPort2.setAdapter(adp4);
	}
	OnClickListener myOnlcikListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			   String seletedTpye = ((SpinnerItem)spType.getSelectedItem()).getId();
			  Intent intent = new Intent(FindCargoActivity.this,CargoResultActivity.class);
			  intent.putExtra("cargoType", seletedTpye);
			  intent.putExtra("teu", tons);
			  intent.putExtra("upPort", upPort);
			  intent.putExtra("downPort", downPort);
              startActivity(intent); 			
		}
	};
	class SpinnerTypeAdapter extends BaseAdapter{
		private List<SpinnerItem> type;
		private LayoutInflater mInflator;
		private TextView text;

		public SpinnerTypeAdapter(FindCargoActivity findCargoActivity,List<SpinnerItem> volume) {
			this.type=volume;
			mInflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return type.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return type.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflator.inflate(R.layout.spinner_item, null);
			}
			text = (TextView) convertView.findViewById(R.id.contentTextView);
//			if (!selected) {
//				text.setText(tags);
//			} else {、
//				text.setText(type.get(position).getValue());
//			}
			text.setText(type.get(position).getValue());
			return convertView;
		}
	}
	private List<SpinnerItem> getDownPort() {	

		ArrayList<SpinnerItem>    cargoList = new ArrayList<SpinnerItem>();
		cargoList.add(new SpinnerItem("", "到达港......"));
		for (PortCityCode e : PortCityCode.values()) {		
			SpinnerItem sp = new SpinnerItem(e.name(), e.getFullName());
			cargoList.add(sp);
		}		
		return cargoList;
	}
	private List<SpinnerItem> getUpPort() {	

		ArrayList<SpinnerItem>    cargoList = new ArrayList<SpinnerItem>();
		cargoList.add(new SpinnerItem("", "起运港......"));
		for (PortCityCode e : PortCityCode.values()) {		
			SpinnerItem sp = new SpinnerItem(e.name(), e.getFullName());
			cargoList.add(sp);
		}		
		return cargoList;
	}
	private List<SpinnerItem> getTons() {	
		ArrayList<SpinnerItem>    cargoList = new ArrayList<SpinnerItem>();
		  cargoList.add(new SpinnerItem("", "货量......"));
			for (CargoVolumnCode e : CargoVolumnCode.values()) {		
				SpinnerItem sp = new SpinnerItem(e.name(), e.getDescription());
				cargoList.add(sp);
			}
		
	
		return cargoList;
	}
	private List<SpinnerItem> getTypes() {	

		ArrayList<SpinnerItem>    cargoList = new ArrayList<SpinnerItem>();
		cargoList.add(new SpinnerItem("", "货类......"));
		for (CargoTypeCode e : CargoTypeCode.getCargoTypeCodes()) {		
			SpinnerItem sp = new SpinnerItem(e.name(), e.getDescription());
			cargoList.add(sp);
		}		
		return cargoList;
	}

	private OnTouchListener typeSpinnerTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			selected = true;
			switch (v.getId()) {
			case R.id.cargoTypes:
				((BaseAdapter) adp).notifyDataSetChanged();
				break;

			case R.id.tons:
				((BaseAdapter) adp2).notifyDataSetChanged();
				break;
			case R.id.upPort:
				((BaseAdapter) adp3).notifyDataSetChanged();
				break;
			case R.id.downPort:
				((BaseAdapter) adp4).notifyDataSetChanged();
				break;
			}

			return false;
		}
	};
	private String tons="";
	private String upPort="";
	private String downPort="";
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}
	

	@Override
	public void onItemSelected(AdapterView<?> parent, View view,
			int position, long id) {
		switch (parent.getId()) {
		case R.id.cargoTypes:
			String jzx=parent.getItemAtPosition(position).toString();
			//typeCode = ((SpinnerItem)parent.getSelectedItem()).getId();
			if(jzx.equals(CargoBigTypeCode.container.getDescription())){
			
				adp2 =  new SpinnerTypeAdapter(this,getTons());
			    spTon.setAdapter(adp2);
			}else
				adp2 =  new SpinnerTypeAdapter(this,getVolume());
			    spTon.setAdapter(adp2);
			break;

		case R.id.tons:
			tons = ((SpinnerItem)parent.getSelectedItem()).getId();

			break;
		case R.id.upPort:
			upPort = ((SpinnerItem)parent.getSelectedItem()).getId();

			break;

		case R.id.downPort:
			downPort = ((SpinnerItem)parent.getSelectedItem()).getId();

			break;
		}

	}
	private List<SpinnerItem> getVolume() {
		ArrayList<SpinnerItem>    cargoList = new ArrayList<SpinnerItem>();
          cargoList.add(new SpinnerItem("", "货量......"));
		for (CargoWeightCode e : CargoWeightCode.values()) {		
			SpinnerItem sp = new SpinnerItem(e.name(), e.getDescription());
			cargoList.add(sp);
		}
		return cargoList;
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}
}
