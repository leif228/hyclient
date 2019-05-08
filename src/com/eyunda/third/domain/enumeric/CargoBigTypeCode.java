package com.eyunda.third.domain.enumeric;

public enum CargoBigTypeCode {
	container("集装箱"),

	bulk("干散货"),

	danger("危险品");

	private String description;

	public String getDescription() {
		return description;
	}

	private CargoBigTypeCode(String description) {
		this.description = description;
	}
}
