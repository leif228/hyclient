package com.eyunda.third.domain.enumeric;

public enum PayStyleCode {
	alipay("支付宝"),

	pinganpay("平安银行见证宝");

	private String description;

	private PayStyleCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
