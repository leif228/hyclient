package com.eyunda.third.domain.enumeric;

public enum MoneyStyleCode {
	direct("一次性付款");

	private String description;

	private MoneyStyleCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
