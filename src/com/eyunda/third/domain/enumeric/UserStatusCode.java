package com.eyunda.third.domain.enumeric;

public enum UserStatusCode {
	inactivity("未激活"),
	activity("激活");

	private String description;

	private UserStatusCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
