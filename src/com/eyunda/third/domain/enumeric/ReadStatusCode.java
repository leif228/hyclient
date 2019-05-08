package com.eyunda.third.domain.enumeric;

public enum ReadStatusCode {
	noread("未读"),
	read("已读");

	private String description;

	private ReadStatusCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
