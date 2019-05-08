package com.eyunda.third.domain.enumeric;

public enum AttrTypeCode {
	charcode("编码"),
	charstr("字符串"),
	intnum("整数"),
	dblnum("浮点数"),
	booltype("布尔型"),
	datetype("日期型");

	private String description;

	private AttrTypeCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
