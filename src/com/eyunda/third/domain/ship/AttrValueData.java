package com.eyunda.third.domain.ship;

import java.util.Map;

import com.eyunda.third.domain.BaseData;

public class AttrValueData extends BaseData {
	private static final long serialVersionUID = -1L;

	private Long id = 0L; // ID
	private String attrValueCode = ""; // 属性值编码
	private String attrValue = ""; // 属性值描述

	public AttrValueData(Map<String, Object> params) {
		super();
		if(params != null && !params.isEmpty()){
			this.id = ((Double) params.get("id")).longValue();
			this.attrValueCode = (String) params.get("attrValueCode");
			this.attrValue = (String) params.get("attrValue");
		}
	}

	public AttrValueData() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAttrValueCode() {
		return attrValueCode;
	}

	public void setAttrValueCode(String attrValueCode) {
		this.attrValueCode = attrValueCode;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "AttrValueData [id=" + id + ", attrValueCode=" + attrValueCode
				+ ", attrValue=" + attrValue + "]";
	}

}
