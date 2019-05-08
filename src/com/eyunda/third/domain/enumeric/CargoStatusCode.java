package com.eyunda.third.domain.enumeric;

public enum CargoStatusCode {
	nopublish("私有"),
	publish("公开");

	private String description;

	private CargoStatusCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
