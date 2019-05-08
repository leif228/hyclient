package com.eyunda.third.domain;

import com.eyunda.third.domain.enumeric.CargoTypeCode;

public class SpinnerItem {
	private String id="";
	private String cid="";
	private String value="";
	private String data="";
	private String flag="";
	private CargoTypeCode cargoType;
	public SpinnerItem() {
		id="";
		value = "";
		
	}
	public SpinnerItem(String id,String value) {
		this.id = id;
		this.value = value;
	}
  
	
	public SpinnerItem(CargoTypeCode cargoTypeCode, String value) {
		this.cargoType =cargoTypeCode;
		this.value =value;
	}
	
	public SpinnerItem(String id, String value, String data) {
		this.id = id;
		this.value = value;
		this.data =data;
	}
	public SpinnerItem(String id, String value, String data, String flag) {
		this.id = id;
		this.value = value;
		this.data =data;
		this.flag =flag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	@Override
	public String toString() {
		return value;
	}

	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public CargoTypeCode getCargoType() {
		return cargoType;
	}
	public void setCargoType(CargoTypeCode cargoType) {
		this.cargoType = cargoType;
	}
	
	
	
}
