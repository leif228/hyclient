package com.eyunda.third.domain.enumeric;

public enum HandleStatusCode {
	nohandle("未处理"),
	handle("已处理"),
	nothandle("不需处理");

	private String description;

	private HandleStatusCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
