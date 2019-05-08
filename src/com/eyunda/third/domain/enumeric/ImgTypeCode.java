package com.eyunda.third.domain.enumeric;

public enum ImgTypeCode {
	jpg("jpg"),
	gif("gif"),
	png("png"),
	jpeg("jpeg"),
	bmp("bmp");

	private String description;

	private ImgTypeCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
