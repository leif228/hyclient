package com.eyunda.third.domain.ship;

import java.util.Map;

import com.eyunda.third.domain.BaseData;

public class ShipAttrData extends BaseData {
	private static final long serialVersionUID = -1L;

	private String shipCode = ""; // 船舶编码
	private String attrNameCode = ""; // 属性值编码
	private String attrValue = "";

	public ShipAttrData(Map<String, Object> params) {
		super();
		if(params != null && !params.isEmpty()){
			this.shipCode = (String) params.get("shipCode");
			this.attrNameCode = (String) params.get("attrNameCode");
			this.attrValue = (String) params.get("attrValue");
		}
	}

	public ShipAttrData() {
		super();
	}

	public String getAttrNameCode() {
		return attrNameCode;
	}

	public void setAttrNameCode(String attrNameCode) {
		this.attrNameCode = attrNameCode;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public String getShipCode() {
		return shipCode;
	}

	public void setShipCode(String shipCode) {
		this.shipCode = shipCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ShipAttrData [shipCode=" + shipCode + ", attrNameCode="
				+ attrNameCode + ", attrValue=" + attrValue + "]";
	}
	

}
