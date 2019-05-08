package com.eyunda.third.domain.enumeric;

public enum NormsCode {
	feet20("20英尺"),
	feet40("40英尺"),
	feet45("45英尺");

	private String description;

	private NormsCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
