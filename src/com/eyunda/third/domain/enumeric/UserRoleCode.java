package com.eyunda.third.domain.enumeric;

public enum UserRoleCode {
	broker("代理人"), handler("业务员"), sailor("船员"), master("船东"), owner("货主"), member("普通会员");

	private String description;

	public String getDescription() {
		return description;
	}

	private UserRoleCode(String description) {
		this.description = description;
	}

}
