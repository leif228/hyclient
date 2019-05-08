package com.eyunda.third.domain.enumeric;

public enum ReleaseStatusCode {
	unpublish("未发布"),
	publish("已发布");

	private String description;

	private ReleaseStatusCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
