package com.eyunda.third.domain.wallet;

import java.util.Map;

import com.eyunda.third.domain.BaseData;

public class PubPayCnapsBank extends BaseData {
	private static final long serialVersionUID = -1L;
	
	private Long id = 0L;
	private String bankCode = ""; // 超级网银号
	private String status = ""; // 状态:0无效,1有效
	private String bankClsCode = ""; // 大小额联行号银行代码，例如工商银行是102
	private String cityCode = ""; // 城市代码，对应着城市表的PubPayCity.areaCode
	private String bankName = ""; // 银行支行名称
	
	public PubPayCnapsBank(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.id = ((Double) params.get("id")).longValue();
			this.bankCode = (String) params.get("bankCode");
			this.status = (String) params.get("status");
			this.bankClsCode = (String) params.get("bankClsCode");
			this.cityCode = (String) params.get("cityCode");
			this.bankName = (String) params.get("bankName");
		}
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBankClsCode() {
		return bankClsCode;
	}

	public void setBankClsCode(String bankClsCode) {
		this.bankClsCode = bankClsCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	@Override
	public String toString() {
		return bankName;
	}

}
