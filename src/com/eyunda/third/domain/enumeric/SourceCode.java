package com.eyunda.third.domain.enumeric;
/**
 * 创建货物时来源
 *
 */
public enum SourceCode {
	fromChat("来源于聊天"),
	fromAddOrder("来源于新增合同"),
	fromAddCargo("来源于新增货物");

	private String description;

	private SourceCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
