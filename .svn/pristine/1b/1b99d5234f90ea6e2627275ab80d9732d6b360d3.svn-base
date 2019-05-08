package com.eyunda.third.domain.wallet;

import java.util.Map;

import com.eyunda.third.domain.BaseData;

public class CustAcctData extends BaseData {

	private static final long serialVersionUID = -1L;

	private String custAcctId = "";// 子账户
	private String custType = ""; // 子账户属性
	private String thirdCustId = "";// 交易网会员代码
	private String custName = "";// 子账户名称
	private Double totalBalance = 0D; // 账户可用余额(元)
	private Double totalTranOutAmount = 0D;// 账户可提现金额(元)
	private String tranDate = ""; // 维护日期

	public CustAcctData(Map<String, Object> params) {
		super();
		if (params != null && !params.isEmpty()) {
			this.custAcctId = (String) params.get("custAcctId");
			this.custType = (String) params.get("custType");
			this.thirdCustId = (String) params.get("thirdCustId");
			this.custName = (String) params.get("custName");
			this.totalBalance = (Double) params.get("totalBalance");
			this.totalTranOutAmount = (Double) params.get("totalTranOutAmount");
			this.tranDate = (String) params.get("tranDate");

		}
	}
	
	public String getCustAcctId() {
		return custAcctId;
	}

	public void setCustAcctId(String custAcctId) {
		this.custAcctId = custAcctId;
	}

	public String getCustType() {
		return custType;
	}

	public void setCustType(String custType) {
		this.custType = custType;
	}

	public String getThirdCustId() {
		return thirdCustId;
	}

	public void setThirdCustId(String thirdCustId) {
		this.thirdCustId = thirdCustId;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public Double getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(Double totalBalance) {
		this.totalBalance = totalBalance;
	}

	public Double getTotalTranOutAmount() {
		return totalTranOutAmount;
	}

	public void setTotalTranOutAmount(Double totalTranOutAmount) {
		this.totalTranOutAmount = totalTranOutAmount;
	}

	public String getTranDate() {
		return tranDate;
	}

	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String toString() {
		return "子账户:" + custAcctId + ",子账户属性:" + custType + ",交易网会员代码:" + thirdCustId + ",子账户名称:" + custName
				+ ",账户可用余额(元):" + totalBalance + ",账户可提现金额(元):" + totalTranOutAmount + ",维护日期:" + tranDate;
	}

}
