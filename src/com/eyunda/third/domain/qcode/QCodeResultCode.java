package com.eyunda.third.domain.qcode;
//二维码扫描结果类型
public enum QCodeResultCode {
	pay("付款"), 
	fetch("收款");

	private String description;

	private QCodeResultCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
