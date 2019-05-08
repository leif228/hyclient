package com.eyunda.third.domain.enumeric;

public enum SearchRlsCode {
	shipsearch("船舶"),
	cargosearch("货物");

	private String description;

	private SearchRlsCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
